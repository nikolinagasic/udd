package com.la.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import com.la.dto.FormFieldsDTO;
import com.la.dto.FormSubmissionDTO;
import com.la.dto.SelectOptionDTO;
import com.la.model.enums.PublishStatus;
import com.la.model.publish.*;
import com.la.security.TokenUtils;
import com.la.service.PublishBookService;
import com.la.service.file.FileService;
import org.camunda.bpm.engine.*;
import org.camunda.bpm.engine.delegate.BpmnError;
import org.camunda.bpm.engine.form.FormField;
import org.camunda.bpm.engine.form.TaskFormData;
import org.camunda.bpm.engine.identity.User;
import org.camunda.bpm.engine.repository.ProcessDefinition;
import org.camunda.bpm.engine.runtime.ProcessInstance;
import org.camunda.bpm.engine.task.Task;
import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

@RestController
@RequestMapping(value = "publish")
@CrossOrigin
public class PublishBookController {

    @Autowired
    private RuntimeService runtimeService;

    @Autowired
    private RepositoryService repositoryService;

    @Autowired
    private IdentityService identityService;

    @Autowired
    private TaskService taskService;

    @Autowired
    private FormService formService;

    @Autowired
    private TokenUtils tokenUtils;

    @Autowired
    private PublishBookService publishBookService;

    @Autowired
    private FileService fileService;


    /**
     * Returns PublishBookRequest with status of request to Writer
     *
     * @param token
     * @return PublishBookRequest
     */
    @GetMapping(value = "/writer/status/{token:.+}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<PublishBookRequest> getWriterStatus(@PathVariable String token) {
        // GET LOGGED IN WRITER USERNAME
        String username = tokenUtils.getUsernameFromToken(token);
        User user = identityService.createUserQuery().userId(username).singleResult();

        // Get Writer Publish Book Info
        ProcessInstance processInstance = hasUserAlreadyStartedProcess("Publish_Book", user.getId());
        PublishBookRequest publishBookRequest = (PublishBookRequest) runtimeService.getVariable(processInstance.getId(), "publishBookRequest");

        return new ResponseEntity<>(publishBookRequest, HttpStatus.OK);
    }

    /**
     * This method returns first form (title, genre and synopsys) for publish book request to Writer
     *
     * @param token
     * @return FormFieldsDTO
     */
    @GetMapping(value = "/writer/form/{token:.+}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<FormFieldsDTO> getFormFieldsWriter(@PathVariable String token) throws NoSuchFieldException, IllegalAccessException, JsonProcessingException {
        // GET LOGGED IN WRITER USERNAME
        String username = tokenUtils.getUsernameFromToken(token);
        User user = identityService.createUserQuery().userId(username).singleResult();

        // CAMUNDA USER EXISTS?
        if (user != null) {
            // CHECKING IF USER ALREADY STARTED THIS PROCESS
            ProcessInstance processInstance = hasUserAlreadyStartedProcess("Publish_Book", user.getId());
            if (processInstance != null) {
                // CHECK IF HE STILL DIDN'T SUBMIT (CURRENTLY ACTIVE TASK IN PROCESS IS WRITER_PUBLISH_FORM)
                Task task = taskService.createTaskQuery().active().processInstanceId(processInstance.getId()).singleResult();
                if (task.getTaskDefinitionKey().equals("Writer_Publish_Form")) {
                    // SEND WRITER FIELDS FOR PUBLISH BOOK FORM
                    List<FormField> formFields = formService.getTaskFormData(task.getId()).getFormFields();
                    return new ResponseEntity<>(new FormFieldsDTO(task.getId(), formFields, processInstance.getId(), "https://localhost:8080/publish/writer/form", ""), HttpStatus.OK);
                } else {
                    // HE ALREADY SUBMITTED
                    // WE CALL ON FRONTEND PROCESS STATUS VARIABLE
                    System.err.println("Sending no response");
                    FormFieldsDTO formFieldsDTO = new FormFieldsDTO();
                    return new ResponseEntity<>(formFieldsDTO, HttpStatus.OK);
                }
            }
            System.err.println("Writer publishing first time ... ");
            // IF USER DIDNT ALREADY STARTED PROCESS
            ProcessInstance pi = runtimeService.startProcessInstanceByKey("Publish_Book");
            runtimeService.setVariable(pi.getId(), "writer", user.getId());

            // GET FIRST TASK
            Task task = taskService.createTaskQuery().processInstanceId(pi.getId()).list().get(0);

            task.setAssignee(user.getId());
            taskService.saveTask(task);

            // GET TASK FORM DATA
            TaskFormData taskFormData = formService.getTaskFormData(task.getId());
            List<FormField> formFields = taskFormData.getFormFields();

            // ADD GENRES TO OPTIONS PROPERTY (CONVERT TO GENERIC LIST)
            List<Object> genres = (List<Object>) runtimeService.getVariables(task.getExecutionId()).get("genres");
            String genresJSON = mapListToJSON(genres, "id", "value", false);

            formFields.get(1).getProperties().put("options", genresJSON);

            // SEND WRITER FIELDS FOR PUBLISH BOOK FORM
            return new ResponseEntity<>(new FormFieldsDTO(task.getId(), formFields, pi.getId(), "https://localhost:8080/publish/writer/form", ""), HttpStatus.OK);
        }
        return new ResponseEntity<>(null, HttpStatus.OK);
    }

    /**
     * This method receives form field values and makes publish book request
     *
     * @param fieldValues
     * @param taskId
     * @return HttpStatus
     */
    @PostMapping(value = "/writer/form/{taskId}", produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<HttpStatus> postFormWriter(@RequestBody List<FormSubmissionDTO> fieldValues, @PathVariable String taskId) {

        try {
            HashMap<String, Object> map = this.mapListToDto(fieldValues);

            Task task = taskService.createTaskQuery().taskId(taskId).singleResult();
            String processInstanceId = task.getProcessInstanceId();
            PublishBookRequest publishBookRequest = publishBookService.makePublishBookRequest(map, task.getAssignee());
            runtimeService.setVariable(processInstanceId, "publishBookRequest", publishBookRequest);
            formService.submitTaskForm(taskId, map);

            System.err.println("SUBMITING WRITER'S REQUEST. WAITING EDITOR TO REVIEW . . .");
        } catch (Exception e) {
            e.printStackTrace();
            throw new BpmnError("InvalidForm");
        }

        return new ResponseEntity<>(HttpStatus.OK);
    }

    /**
     * This method sends all active tasks for book publish to Editor
     *
     * @param token String
     * @return List<RequestView>
     */
    @GetMapping(value = "/requests/{token:.+}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<RequestView>> getRequests(@PathVariable String token) {
        // GET LOGGED IN USER USERNAME
        String username = tokenUtils.getUsernameFromToken(token);
        User user = identityService.createUserQuery().userId(username).singleResult();

        // exists as camunda user
        if (user != null) {
            List<ProcessInstance> processInstances = getAllRunningProcessInstances("Publish_Book");

            PublishBookRequest publishBookRequest;
            Task task;

            List<RequestView> requestViews = new ArrayList<>();

            for (ProcessInstance processInstance : processInstances) {
                task = taskService.createTaskQuery().processInstanceId(processInstance.getId()).taskAssignee(username).active().singleResult();
                if (task != null) {
                    publishBookRequest = (PublishBookRequest) runtimeService.getVariable(task.getExecutionId(), "publishBookRequest");

                    RequestView requestView = new RequestView();
                    requestView.setTaskId(task.getId());
                    requestView.setProcessInstanceId(processInstance.getId());
                    requestView.setPublishBookRequest(publishBookRequest);

                    System.err.println(formService.getTaskFormData(task.getId()).getFormFields());
                    if (formService.getTaskFormData(task.getId()).getFormFields().size() > 0) {
                        requestView.setTaskIsForm(true);
                    } else {
                        requestView.setTaskIsForm(false);
                    }

                    requestViews.add(requestView);
                }
            }
            System.err.println("SHOWING REQUESTS  . . .");

            return new ResponseEntity<>(requestViews, HttpStatus.OK);
        }

        return new ResponseEntity<>(null, HttpStatus.OK);
    }

    /**
     * This method receives Editor decision (approve, deny) on Writer publish book request
     * Returns form field (explanation) if denied
     *
     * @param decision
     * @param taskId
     * @return FormFieldsDTO
     */
    @PostMapping(value = "/editor/decision/{taskId}", produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<FormFieldsDTO> postDecision(@RequestBody Decision decision, @PathVariable String taskId) {

        try {
            Task task = taskService.createTaskQuery().taskId(taskId).singleResult();
            String processInstanceId = task.getProcessInstanceId();

            if (!task.getName().equals("Editor Refuse Form")) {
                runtimeService.setVariable(processInstanceId, "editorApproved", decision.getApproved());
                taskService.complete(taskId);

                if (decision.getApproved()) {
                    System.err.println("EDITOR APPROVED SCRIPT . . .");

                    PublishBookRequest publishBookRequest = (PublishBookRequest) runtimeService.getVariable(processInstanceId, "publishBookRequest");
                    // Set request status
                    // Set deadline
                    publishBookRequest.setStatus(PublishStatus.WAITING_SUBMIT.toString());
                    publishBookRequest.setDeadline((DateTime.now().plusDays(10)).toLocalDate().toString());
                    runtimeService.setVariable(processInstanceId, "publishBookRequest", publishBookRequest);
                }
            }
            // If rejecting initiated send form fields from next task and wait for final deny
            if (!decision.getApproved()) {
                System.err.println("EDITOR DIDN'T APPROVE SCRIPT. WAITING EXPLANATION . . .");

                Task nextTask = taskService.createTaskQuery().processInstanceId(processInstanceId).active().singleResult();
                TaskFormData taskFormData = formService.getTaskFormData(nextTask.getId());
                List<FormField> formFields = taskFormData.getFormFields();
                return new ResponseEntity<>(new FormFieldsDTO(nextTask.getId(), formFields, processInstanceId, "https://localhost:8080/publish/editor/refuse", ""), HttpStatus.OK);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return new ResponseEntity<>(null, HttpStatus.OK);
    }

    /**
     * Saves deny explanation from Editor
     *
     * @param fieldValues
     * @param taskId
     * @return HttpStatus
     */
    @PostMapping(value = "/editor/refuse/{taskId}", produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<HttpStatus> postFormEditorRefuse(@RequestBody List<FormSubmissionDTO> fieldValues, @PathVariable String taskId) {

        try {
            HashMap<String, Object> map = this.mapListToDto(fieldValues);

            Task task = taskService.createTaskQuery().taskId(taskId).singleResult();
            String processInstanceId = task.getProcessInstanceId();
            runtimeService.setVariable(processInstanceId, "explanation", fieldValues.get(0).getFieldValue());
            formService.submitTaskForm(taskId, map);

            System.err.println("EDITOR POSTED EXPLANATION . . .");
        } catch (Exception e) {
            e.printStackTrace();
            throw new BpmnError("InvalidForm");
        }

        return new ResponseEntity<>(HttpStatus.OK);
    }

    /**
     * @param token
     * @return FormFieldsDTO
     */
    @GetMapping(value = "/writer/form/upload/{token:.+}")
    public ResponseEntity<FormFieldsDTO> getUploadFileForm(@PathVariable String token) {
        // GET LOGGED IN WRITER USERNAME
        String username = tokenUtils.getUsernameFromToken(token);
        User user = identityService.createUserQuery().userId(username).singleResult();

        // CAMUNDA USER EXISTS?
        if (user != null) {
            ProcessInstance processInstance = hasUserAlreadyStartedProcess("Publish_Book", user.getId());
            if (processInstance != null) {
                Task task = taskService.createTaskQuery().active().processInstanceId(processInstance.getId()).taskAssignee(username).singleResult();
                System.err.println(task.getName());
                if (task.getName().equals("Script Submit Form")) {
                    // GET TASK FORM DATA
                    TaskFormData taskFormData = formService.getTaskFormData(task.getId());
                    List<FormField> formFields = taskFormData.getFormFields();

                    return new ResponseEntity<>(new FormFieldsDTO(task.getId(), formFields, task.getProcessInstanceId(), "https://localhost:8080/publish/writer/form/upload/", "https://localhost:8080/publish/upload/"), HttpStatus.OK);
                }
            }
        }

        System.err.println("SENDING FILE UPLOAD FORM . . .");

        return new ResponseEntity<>(new FormFieldsDTO(), HttpStatus.OK);
    }

    /**
     * @param fieldValues
     * @param taskId
     * @return HttpStatus
     */
    @PostMapping(value = "/writer/form/upload/{taskId}")
    public ResponseEntity<HttpStatus> postUploadFileForm(@RequestBody List<FormSubmissionDTO> fieldValues, @PathVariable String taskId) {

        try {
            HashMap<String, Object> map = this.mapListToDto(fieldValues);
            formService.submitTaskForm(taskId, map);

            System.err.println("POST UPLOAD FILE FORM . . .");
        } catch (Exception e) {
            e.printStackTrace();
            throw new BpmnError("InvalidForm");
        }

        return new ResponseEntity<>(HttpStatus.OK);
    }

    /**
     * @param file
     * @param processInstanceId
     * @return HttpStatus
     */
    @PostMapping(value = "/upload/{processInstanceId}")
    public ResponseEntity<HttpStatus> uploadFile(@RequestBody MultipartFile file, @PathVariable String processInstanceId) {
        try {
            if (file != null) {
                PublishBookRequest publishBookRequest = (PublishBookRequest) runtimeService.getVariable(processInstanceId, "publishBookRequest");
                String path = fileService.saveUploadedFile(file, processInstanceId);
                publishBookRequest.setPath(path);
                switch (publishBookRequest.getStatus()) {
                    case ("WAITING_SUBMIT"): {
                        publishBookRequest.setStatus(PublishStatus.WAITING_PLAGIARISM_CHECK.toString());
                        break;
                    }
                    case ("WAITING_COMMENT_CHECK"):
                    case ("WAITING_CHANGES"): {
                        publishBookRequest.setStatus(PublishStatus.WAITING_SUGGESTIONS.toString());
                        break;
                    }
                    case ("WAITING_CORRECTION"): {
                        publishBookRequest.setStatus(PublishStatus.WAITING_LECTOR_REVIEW.toString());
                        break;
                    }
                    default: {
                        break;
                    }
                }
                runtimeService.setVariable(processInstanceId, "publishBookRequest", publishBookRequest);

                System.err.println("FILE SAVED. WAITING PLAGIARISM CHECK . . .");

                return new ResponseEntity<>(HttpStatus.OK);
            }

            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
    }

    /**
     * @param processInstanceId
     * @return List<Plagiat>
     */
    @GetMapping(value = "/editor/plagiats/{processInstanceId}")
    public ResponseEntity<List<Plagiat>> getPlagiats(@PathVariable String processInstanceId) {
        try {
            List<Plagiat> plagiatList = (List<Plagiat>) runtimeService.getVariable(processInstanceId, "plagiats");

            System.err.println("SHOWING PLAGIATS LIST . . .");

            return new ResponseEntity<>(plagiatList, HttpStatus.OK);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return new ResponseEntity<>(null, HttpStatus.NO_CONTENT);
    }

    /**
     * @param decision
     * @param taskId
     * @return HttpStatus
     */
    @PostMapping(value = "/editor/plagiats/{taskId}")
    public ResponseEntity<HttpStatus> decideIfOriginal(@RequestBody Decision decision, @PathVariable String taskId) {
        try {
            Task task = taskService.createTaskQuery().taskId(taskId).singleResult();
            if (decision.getApproved()) {
                System.err.println("EDITOR THINKS IT'S ORIGINAL. WAITING READING . . .");

                runtimeService.setVariable(task.getProcessInstanceId(), "editorOriginal", true);
                PublishBookRequest publishBookRequest = (PublishBookRequest) runtimeService.getVariable(task.getProcessInstanceId(), "publishBookRequest");
                publishBookRequest.setStatus(PublishStatus.WAITING_READING.toString());
                runtimeService.setVariable(task.getProcessInstanceId(), "publishBookRequest", publishBookRequest);
            } else {
                System.err.println("EDITOR THINKS IT'S PLAGIAT. SENDING EMAIL TO WRITER . . .");

                runtimeService.setVariable(task.getProcessInstanceId(), "editorOriginal", false);
            }

            taskService.complete(taskId);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return new ResponseEntity<>(HttpStatus.OK);
    }

    /**
     * @param filename
     * @return ResponseEntity
     */
    @GetMapping("/download/{filename:.+}")
    public ResponseEntity downloadFileFromLocal(@PathVariable String filename) {
        try {
            System.err.println("DOWNLOADING FILE . . .");

            Resource resource = fileService.downloadFile(filename);
            return ResponseEntity.ok()
                    .contentType(MediaType.parseMediaType("application/octet-stream"))
                    .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + resource.getFilename() + "\"")
                    .body(resource);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return ResponseEntity.badRequest().body("ERROR");
    }

    /**
     * @param decision
     * @param taskId
     * @return FormFieldsDTO
     */
    @PostMapping(value = "/editor/decision/2/{taskId}", produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<FormFieldsDTO> postDecision2(@RequestBody Decision decision, @PathVariable String taskId) {
        try {
            Task task = taskService.createTaskQuery().taskId(taskId).singleResult();
            if (!task.getName().equals("Editor Refuse Form")) {
                if (decision.getApproved()) {
                    System.err.println("EDITOR APPROVED AFTER READING . . .");

                    runtimeService.setVariable(task.getProcessInstanceId(), "editorApproved", true);
                    PublishBookRequest publishBookRequest = (PublishBookRequest) runtimeService.getVariable(task.getProcessInstanceId(), "publishBookRequest");
                    publishBookRequest.setStatus(PublishStatus.WAITING_AFTER_READING.toString());
                    runtimeService.setVariable(task.getProcessInstanceId(), "publishBookRequest", publishBookRequest);
                } else {
                    System.err.println("EDITOR DIDN'T APPROVE. SHOWING EXPLANATION FORM . . .");

                    runtimeService.setVariable(task.getProcessInstanceId(), "editorApproved", false);
                }
                taskService.complete(taskId);
            }

            if (!decision.getApproved()) {
                Task nextTask = taskService.createTaskQuery().processInstanceId(task.getProcessInstanceId()).active().singleResult();
                TaskFormData taskFormData = formService.getTaskFormData(nextTask.getId());
                List<FormField> formFields = taskFormData.getFormFields();
                return new ResponseEntity<>(new FormFieldsDTO(nextTask.getId(), formFields, task.getProcessInstanceId(), "https://localhost:8080/publish/editor/refuse", ""), HttpStatus.OK);
            }
        } catch (Exception e) {
            e.printStackTrace();
            throw new BpmnError("InvalidForm");
        }

        return new ResponseEntity<>(null, HttpStatus.OK);
    }

    /**
     * If beta readers choosed returns list of beta readers and task id for posting selection
     *
     * @param decision
     * @param taskId
     * @return BetaReadersEditor
     */
    @PostMapping(value = "/editor/send-to-beta/{taskId}", produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<FormFieldsDTO> wannaSendToBeta(@RequestBody Decision decision, @PathVariable String taskId) throws IllegalAccessException, NoSuchFieldException, JsonProcessingException {
        try {
            Task task = taskService.createTaskQuery().taskId(taskId).singleResult();

            if (!task.getName().equals("Choose Beta Readers")) {
                if (decision.getApproved()) {
                    System.err.println("EDITOR WANTS TO SEND TO BETA . . .");

                    runtimeService.setVariable(task.getProcessInstanceId(), "sendToBeta", true);
                } else {
                    System.err.println("EDITOR WANTS TO SEND TO LECTOR . . .");

                    runtimeService.setVariable(task.getProcessInstanceId(), "sendToBeta", false);
                }
                taskService.complete(taskId);
            }

            if (decision.getApproved()) {
                List<Object> readerList = (List<Object>) runtimeService.getVariable(task.getProcessInstanceId(), "betaBefore");
                Task nextTask = taskService.createTaskQuery().processInstanceId(task.getProcessInstanceId()).active().singleResult();
                System.err.println("SENDING BETA READERS FORM (MULTIPLE SELECT) . . .");

                String readersJSON = mapListToJSON(readerList, "username", "username", true);

                TaskFormData taskFormData = formService.getTaskFormData(nextTask.getId());
                List<FormField> formFields = taskFormData.getFormFields();

                formFields.get(0).getProperties().put("options", readersJSON);

                return new ResponseEntity<>(new FormFieldsDTO(nextTask.getId(), formFields, task.getProcessInstanceId(), "https://localhost:8080/publish/editor/choose-beta", ""), HttpStatus.OK);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return new ResponseEntity<>(HttpStatus.OK);
    }

    /**
     * @param fieldValues
     * @param taskId
     * @return HttpStatus
     */
    @PostMapping(value = "/editor/choose-beta/{taskId}", produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<HttpStatus> postChoosenBetaReaders(@RequestBody List<FormSubmissionDTO> fieldValues, @PathVariable String taskId) {

        try {
            HashMap<String, Object> map = this.mapListToDto(fieldValues);

            Task task = taskService.createTaskQuery().taskId(taskId).singleResult();

            List<String> readers = publishBookService.getReaders(map);
            runtimeService.setVariable(task.getProcessInstanceId(), "beta", readers);
            PublishBookRequest publishBookRequest = (PublishBookRequest) runtimeService.getVariable(task.getProcessInstanceId(), "publishBookRequest");
            publishBookRequest.setDeadline((DateTime.now().plusDays(5)).toLocalDate().toString());
            publishBookRequest.setStatus(PublishStatus.WAITING_BETA_READERS.toString());
            runtimeService.setVariable(task.getProcessInstanceId(), "publishBookRequest", publishBookRequest);

            formService.submitTaskForm(taskId, map);

            System.err.println("BETA READERS HAVE BEEN CHOOSEN . . .");
        } catch (Exception e) {
            e.printStackTrace();
            throw new BpmnError("InvalidForm");
        }

        return new ResponseEntity<>(HttpStatus.OK);
    }

    /**
     * Gets comment form for beta-reader
     *
     * @param taskId
     * @return
     */
    @GetMapping(value = "beta-reader/{taskId}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<FormFieldsDTO> getBetaReaderCommentForm(@PathVariable String taskId) {
        try {
            Task task = taskService.createTaskQuery().taskId(taskId).singleResult();
            TaskFormData taskFormData = formService.getTaskFormData(taskId);
            List<FormField> formFields = taskFormData.getFormFields();

            return new ResponseEntity<>(new FormFieldsDTO(taskId, formFields, task.getProcessInstanceId(), "https://localhost:8080/publish/beta-reader/form/comment", ""), HttpStatus.OK);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
    }

    /**
     * @param fieldValues
     * @param taskId
     * @return HttpStatus
     */
    @PostMapping(value = "/beta-reader/form/comment/{taskId}")
    public ResponseEntity<HttpStatus> postComment(@RequestBody List<FormSubmissionDTO> fieldValues, @PathVariable String taskId) {
        try {
            Task task = taskService.createTaskQuery().taskId(taskId).singleResult();

            PublishBookRequest publishBookRequest = (PublishBookRequest) runtimeService.getVariable(task.getProcessInstanceId(), "publishBookRequest");
            List<BetaReaderComment> betaReaderComments = publishBookRequest.getBetaReaderCommentList();

            if (betaReaderComments == null) {
                System.err.println("FIRST BETA READER COMMENTED . . .");

                List<BetaReaderComment> betaReaderCommentsNew = new ArrayList<>();
                betaReaderCommentsNew.add(new BetaReaderComment(fieldValues.get(0).getFieldValue(), task.getAssignee()));
                publishBookRequest.setBetaReaderCommentList(betaReaderCommentsNew);
            } else {
                System.err.println("ANOTHER ONE HAS COMMENTED . . .");

                betaReaderComments.add(new BetaReaderComment(fieldValues.get(0).getFieldValue(), task.getAssignee()));
                publishBookRequest.setBetaReaderCommentList(betaReaderComments);
            }

            runtimeService.setVariable(task.getProcessInstanceId(), "publishBookRequest", publishBookRequest);

            HashMap<String, Object> map = this.mapListToDto(fieldValues);
            formService.submitTaskForm(taskId, map);
        } catch (Exception e) {
            e.printStackTrace();
            // TO DO EXCEPTION??
        }

        return new ResponseEntity<>(HttpStatus.OK);
    }

    /**
     * @param decision
     * @param taskId
     * @return FormFieldsDTO
     */
    @PostMapping(value = "/editor/need-more-changes/{taskId}", produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<FormFieldsDTO> needMoreChanges(@RequestBody Decision decision, @PathVariable String taskId) {

        try {
            Task task = taskService.createTaskQuery().taskId(taskId).singleResult();
            PublishBookRequest publishBookRequest = (PublishBookRequest) runtimeService.getVariable(task.getProcessInstanceId(), "publishBookRequest");

            if (!task.getName().equals("Editor Suggestions")) {
                if (decision.getApproved() == null) {
                    System.err.println("READY FOR PRINTING . . .");
                    runtimeService.setVariable(task.getProcessInstanceId(), "moreChanges", 3);
                } else {
                    if (decision.getApproved()) {
                        System.err.println("EDITOR WANTS MORE CHANGES . . .");
                        publishBookRequest.setStatus(PublishStatus.WAITING_SUGGESTIONS.toString());
                        runtimeService.setVariable(task.getProcessInstanceId(), "moreChanges", 2);
                    } else {
                        System.err.println("EDITOR WAITS LECTOR REVIEW . . .");
                        publishBookRequest.setStatus(PublishStatus.WAITING_LECTOR_REVIEW.toString());
                        runtimeService.setVariable(task.getProcessInstanceId(), "moreChanges", 1);
                    }
                }

                runtimeService.setVariable(task.getProcessInstanceId(), "publishBookRequest", publishBookRequest);
                taskService.complete(taskId);
            }

            if (decision.getApproved() != null) {
                if (decision.getApproved()) {
                    Task nextTask = taskService.createTaskQuery().processInstanceId(task.getProcessInstanceId()).active().singleResult();
                    TaskFormData taskFormData = formService.getTaskFormData(nextTask.getId());
                    List<FormField> formFields = taskFormData.getFormFields();
                    System.err.println("SENDING SUGGESTION FORM . . .");

                    return new ResponseEntity<>(new FormFieldsDTO(nextTask.getId(), formFields, task.getProcessInstanceId(), "https://localhost:8080/publish/editor/form/suggestion", ""), HttpStatus.OK);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return new ResponseEntity<>(null, HttpStatus.OK);
    }

    /**
     * @param fieldValues
     * @param taskId
     * @return HttpStatus
     */
    @PostMapping(value = "/editor/form/suggestion/{taskId}")
    public ResponseEntity<HttpStatus> postSuggestion(@RequestBody List<FormSubmissionDTO> fieldValues, @PathVariable String taskId) {
        try {
            HashMap<String, Object> map = this.mapListToDto(fieldValues);

            Task task = taskService.createTaskQuery().taskId(taskId).singleResult();
            PublishBookRequest publishBookRequest = (PublishBookRequest) runtimeService.getVariable(task.getProcessInstanceId(), "publishBookRequest");
            publishBookRequest.setStatus(PublishStatus.WAITING_CHANGES.toString());
            publishBookRequest.setSuggestion((String) map.get("suggestion"));
            publishBookRequest.setDeadline((DateTime.now().plusDays(10)).toLocalDate().toString());

            runtimeService.setVariable(task.getProcessInstanceId(), "publishBookRequest", publishBookRequest);

            formService.submitTaskForm(taskId, map);

            System.err.println("WAITING WRITER CHANGES . . .");
        } catch (Exception e) {
            e.printStackTrace();
            throw new BpmnError("InvalidForm");
        }

        return new ResponseEntity<>(HttpStatus.OK);
    }

    /**
     * @param decision
     * @param taskId
     * @return FormFieldsDTO
     */
    @PostMapping(value = "/lector/need-correction/{taskId}", produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<FormFieldsDTO> postNeedCorrection(@RequestBody Decision decision, @PathVariable String taskId) {
        try {
            Task task = taskService.createTaskQuery().taskId(taskId).singleResult();
            PublishBookRequest publishBookRequest = (PublishBookRequest) runtimeService.getVariable(task.getProcessInstanceId(), "publishBookRequest");

            if (!task.getName().equals("Lector Correction")) {
                if (decision.getApproved()) {
                    System.err.println("LECTOR THINKS SCRIPT NEEDS CORRECTION . . .");

                    runtimeService.setVariable(task.getProcessInstanceId(), "needsCorrection", true);
                    publishBookRequest.setStatus(PublishStatus.WAITING_LECTOR_REVIEW.toString());
                } else {
                    System.err.println("LECTOR WAITS EDITOR'S SUGGESTIONS . . .");

                    runtimeService.setVariable(task.getProcessInstanceId(), "needsCorrection", false);
                    publishBookRequest.setCorrection("Script doesn't need corrections");
                    publishBookRequest.setStatus(PublishStatus.WAITING_SUGGESTIONS.toString());
                }

                runtimeService.setVariable(task.getProcessInstanceId(), "publishBookRequest", publishBookRequest);
                taskService.complete(taskId);
            }

            if (decision.getApproved()) {
                Task nextTask = taskService.createTaskQuery().processInstanceId(task.getProcessInstanceId()).active().singleResult();
                TaskFormData taskFormData = formService.getTaskFormData(nextTask.getId());
                List<FormField> formFields = taskFormData.getFormFields();
                return new ResponseEntity<>(new FormFieldsDTO(nextTask.getId(), formFields, task.getProcessInstanceId(), "https://localhost:8080/publish/lector/form/correction", ""), HttpStatus.OK);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return new ResponseEntity<>(null, HttpStatus.OK);
    }

    /**
     * @param fieldValues
     * @param taskId
     * @return HttpStatus
     */
    @PostMapping(value = "/lector/form/correction/{taskId}")
    public ResponseEntity<HttpStatus> postCorrection(@RequestBody List<FormSubmissionDTO> fieldValues, @PathVariable String taskId) {
        try {
            HashMap<String, Object> map = this.mapListToDto(fieldValues);

            Task task = taskService.createTaskQuery().taskId(taskId).singleResult();
            PublishBookRequest publishBookRequest = (PublishBookRequest) runtimeService.getVariable(task.getProcessInstanceId(), "publishBookRequest");
            publishBookRequest.setStatus(PublishStatus.WAITING_CORRECTION.toString());
            publishBookRequest.setCorrection((String) map.get("correction"));
            publishBookRequest.setDeadline((DateTime.now().plusDays(10)).toLocalDate().toString());

            runtimeService.setVariable(task.getProcessInstanceId(), "publishBookRequest", publishBookRequest);

            formService.submitTaskForm(taskId, map);

            System.err.println("WAITING WRITER CORRECTION . . .");
        } catch (Exception e) {
            e.printStackTrace();
            throw new BpmnError("InvalidForm");
        }

        return new ResponseEntity<>(HttpStatus.OK);
    }

    /**
     * @param list
     * @return HashMap<String, Object>
     */
    private HashMap<String, Object> mapListToDto(List<FormSubmissionDTO> list) {
        HashMap<String, Object> map = new HashMap<String, Object>();
        for (FormSubmissionDTO temp : list) {
            map.put(temp.getFieldId(), temp.getFieldValue());
        }

        return map;
    }

    private String mapListToJSON(List<Object> objects, String valueFieldName, String labelFieldName, boolean superClassFields) throws NoSuchFieldException, IllegalAccessException, JsonProcessingException {
        List<SelectOptionDTO> selectOptionDTOList = new ArrayList<>();

        for (Object object : objects) {
            Field valueField;
            Field labelField;
            if (superClassFields) {
                valueField = object.getClass().getSuperclass().getDeclaredField(valueFieldName);
                labelField = object.getClass().getSuperclass().getDeclaredField(labelFieldName);
            } else {
                valueField = object.getClass().getDeclaredField(valueFieldName);
                labelField = object.getClass().getDeclaredField(labelFieldName);
            }
            valueField.setAccessible(true);
            labelField.setAccessible(true);
            selectOptionDTOList.add(new SelectOptionDTO(String.valueOf(valueField.get(object)), String.valueOf(labelField.get(object))));
        }

        ObjectWriter ow = new ObjectMapper().writer().withDefaultPrettyPrinter();
        String json = ow.writeValueAsString(selectOptionDTOList);

        System.err.println(json);

        return json;
    }

    /**
     * @param processDefinitionId
     * @return List<ProcessInstance>
     */
    private List<ProcessInstance> getAllRunningProcessInstances(String processDefinitionId) {
        // query for latest process definition with given name
        ProcessDefinition myProcessDefinition =
                repositoryService.createProcessDefinitionQuery()
                        .processDefinitionName(processDefinitionId)
                        .latestVersion()
                        .singleResult();

        // list all running/unsuspended instances of the process
        List<ProcessInstance> processInstances =
                runtimeService.createProcessInstanceQuery()
                        .processDefinitionId(myProcessDefinition.getId())
                        .active() // we only want the unsuspended process instances
                        .list();

        return processInstances;
    }

    /**
     * @param processDefinitionId
     * @param userId
     * @return ProcessInstance
     */
    private ProcessInstance hasUserAlreadyStartedProcess(String processDefinitionId, String userId) {
        List<ProcessInstance> processInstances = getAllRunningProcessInstances(processDefinitionId);

        for (ProcessInstance processInstance : processInstances) {
            String username = (String) runtimeService.getVariable(processInstance.getId(), "writer");
            if (username.equals(userId)) {
                System.err.println("User already started this process.");
                return processInstance;
            }
        }
        return null;
    }
}
