package com.la.controller.registration;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import com.la.dto.FormFieldsDTO;
import com.la.dto.FormSubmissionDTO;
import com.la.dto.SelectOptionDTO;
import com.la.handler.RegistrationStartHandler;
import com.la.model.dtos.BoardMemberWorkToApproveDTO;
import com.la.model.dtos.WriterMembershipRequestDataNeededDTO;
import com.la.model.enums.Opinion;
import com.la.model.enums.WriterMembershipStatus;
import com.la.model.registration.BoardMemberComment;
import com.la.model.registration.SubmittedWork;
import com.la.model.registration.WriterMembershipRequest;
import com.la.model.users.BoardMember;
import com.la.repository.BoardMemberCommentRepository;
import com.la.repository.BoardMemberRepository;
import com.la.repository.SubmittedWorkRepository;
import com.la.repository.WriterMembershipRequestRepository;
import com.la.security.TokenUtils;
import com.la.service.file.FileService;
import org.camunda.bpm.engine.*;
import org.camunda.bpm.engine.form.FormField;
import org.camunda.bpm.engine.form.TaskFormData;
import org.camunda.bpm.engine.identity.User;
import org.camunda.bpm.engine.repository.ProcessDefinition;
import org.camunda.bpm.engine.runtime.ProcessInstance;
import org.camunda.bpm.engine.task.Task;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.lang.reflect.Field;
import java.util.*;

@RestController
@RequestMapping(value = "/api/registration")
public class RegistrationController {

    @Autowired
    private RepositoryService repositoryService;

    @Autowired
    private IdentityService identityService;

    @Autowired
    private RuntimeService runtimeService;

    @Autowired
    private TaskService taskService;

    @Autowired
    private FormService formService;

    @Autowired
    private TokenUtils tokenUtils;

    @Autowired
    private WriterMembershipRequestRepository writerMembershipRequestRepository;

    @Autowired
    private SubmittedWorkRepository submittedWorkRepository;

    @Autowired
    private BoardMemberRepository boardMemberRepository;

    @Autowired
    private BoardMemberCommentRepository boardMemberCommentRepository;

    @Autowired
    private FileService fileService;

    @Autowired
    private RegistrationStartHandler registrationStartHandler;

    // 6.1 KORAK / PREDUSLOV
    @GetMapping(value = "/request/self/{token:.+}")
    public ResponseEntity<?> getDataNeeded(@PathVariable String token) {
        String username = tokenUtils.getUsernameFromToken(token);
        User user = identityService.createUserQuery().userId(username).singleResult();

        if (user != null) {
            WriterMembershipRequest request = writerMembershipRequestRepository.findByUsername(username);

            WriterMembershipRequestDataNeededDTO dataNeeded = new WriterMembershipRequestDataNeededDTO();
            dataNeeded.setAttemptsNumber(request.getAttemptsNumber());
            dataNeeded.setPaymentDeadline(request.getPaymentDeadline());
            dataNeeded.setStatus(request.getStatus());
            dataNeeded.setSubmissionDeadline(request.getSubmissionDeadline());
            dataNeeded.setFilesPosted(request.getFilesPosted());

            return new ResponseEntity<>(dataNeeded, HttpStatus.OK);
        }
        return new ResponseEntity<>("Not existing user", HttpStatus.BAD_REQUEST);
    }

    // 6.2 KORAK / DOBAVLJANJE FORME ZA UPLOAD 2 PDFA
    @GetMapping(value = "/upload-work-form")
    public ResponseEntity<FormFieldsDTO> getUploadWorkForm(@RequestHeader("Authorization") String token) {
        token = token.substring(7);
        String username = tokenUtils.getUsernameFromToken(token);

        ProcessInstance processInstance = hasUserAlreadyStartedProcess("Process_registration", username);

        Task task = taskService.createTaskQuery().processInstanceId(processInstance.getId()).list().get(0);
        TaskFormData taskFormData = formService.getTaskFormData(task.getId());
        List<FormField> formFields = taskFormData.getFormFields();

        return new ResponseEntity<>(new FormFieldsDTO(task.getId(), formFields, processInstance.getId(),
                "https://localhost:8080/api/registration/writer-upload/", "https://localhost:8080/api/registration/upload-submitted-work/"), HttpStatus.OK);
    }

    // 7.1 KORAK / SLANJE UPLOADOVANIH PDF FAJLOVA
    @PostMapping(value = "/upload-submitted-work/{processInstanceId}")
    public ResponseEntity<?> uploadSubmittedWork(@RequestBody MultipartFile file, @PathVariable String processInstanceId) {
        try {
            if (file != null) {
                String username = (String) runtimeService.getVariable(processInstanceId, "registeredUser");
                WriterMembershipRequest request = writerMembershipRequestRepository.findByUsername(username);

                String path = fileService.saveUploadedFile(file, processInstanceId, "__" + (request.getFilesPosted() + 1));

                int files = request.getFilesPosted();
                ++files;
                request.setFilesPosted(files);

                if (files >= 2) {
                    request.setStatus(WriterMembershipStatus.WAITING_OPINION);
                    int attempts = request.getAttemptsNumber();
                    request.setAttemptsNumber(++attempts);
                }
                writerMembershipRequestRepository.save(request);

                // DODATI CUVANJE SUBMITTED WORKA
                SubmittedWork submittedWork = new SubmittedWork();
                submittedWork.setPath(path);
                submittedWork.setReviewed(false);
                submittedWork.setWriterMembershipRequest(request);
                submittedWorkRepository.save(submittedWork);

                return new ResponseEntity<>(HttpStatus.OK);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
    }

    // 7.2 / SLANJE FORME PDFOVA ZA CAMUNDU
    @PostMapping(value = "/writer-upload/{taskId}")
    public ResponseEntity<HttpStatus> postUploadFileForm(@RequestBody List<FormSubmissionDTO> fieldValues, @PathVariable String taskId) {
        HashMap<String, Object> map = this.mapListToDto(fieldValues);

        Task task = taskService.createTaskQuery().taskId(taskId).singleResult();
        String username = (String) runtimeService.getVariable(task.getProcessInstanceId(), "registeredUser");
        WriterMembershipRequest request = writerMembershipRequestRepository.findByUsername(username);
        if (request.getAttemptsNumber() >= 1) {
            Object o = map.get("uploadWorkFile2");
            map.remove("uploadWorkFile2");
            map.put("uploadMoreWork" + request.getAttemptsNumber(), o);
        }
        formService.submitTaskForm(taskId, map);

        System.err.println("POST UPLOAD FILE FORM . . .");

        return new ResponseEntity<>(HttpStatus.OK);
    }

    // 8. KORAK / BOARD MEMBER DOBAVALJA SVOJE AKTIVNE TAKOVE ZA PREGLED RADA
    @GetMapping(value = "/writer-submitted-work")
    public ResponseEntity<?> getWriterSubmittedWork(@RequestHeader("Authorization") String token) {
        token = token.substring(7);
        String username = tokenUtils.getUsernameFromToken(token);
        System.out.println("TOKEN JE " + token + " I USERNAME " + username);

        List<BoardMemberWorkToApproveDTO> boardMemberWorkToApproveDTOS = new ArrayList<>();

        List<ProcessInstance> allProcessInstances = getAllRunningProcessInstances("Process_registration");

        for (ProcessInstance pi : allProcessInstances) {
            Task task = taskService.createTaskQuery().processInstanceId(pi.getId()).taskAssignee(username).active().singleResult();

            if (task != null) {
                WriterMembershipRequest request = writerMembershipRequestRepository.findByUsername((String) runtimeService.getVariables(pi.getId()).get("registeredUser"));


                //if po tome da li je komentar postavljen za ovaj rad
                BoardMemberWorkToApproveDTO workToApproveDTO = new BoardMemberWorkToApproveDTO();
                workToApproveDTO.setWriterFirstName(request.getFirstName());
                workToApproveDTO.setWriterLastName(request.getLastName());

                List<String> filenames = new ArrayList<>();

                for (int i = 1; i < request.getFilesPosted() + 1; i++) {
                    filenames.add(fileService.getResourceFilePath(pi.getId() + "__" + i));
                }
                workToApproveDTO.setFilenames(filenames);
                workToApproveDTO.setTaskId(task.getId());
                workToApproveDTO.setProcessInstanceId(pi.getId());
                boardMemberWorkToApproveDTOS.add(workToApproveDTO);
            }
        }

        return new ResponseEntity<>(boardMemberWorkToApproveDTOS, HttpStatus.OK);
    }

    // 8.bonus / DOWNLOAD PDFA RADA
    @GetMapping("/download/{filename:.+}")
    public ResponseEntity<?> downloadFileFromLocal(@PathVariable String filename) {
        System.err.println("DOWNLOADING FILE . . .");

        Resource resource = fileService.downloadFile(filename);
        return ResponseEntity.ok()
                .contentType(MediaType.parseMediaType("application/octet-stream"))
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + resource.getFilename() + "\"")
                .body(resource);
    }

    // 9. KORAK / DOBAVLJANJE FORME
    @GetMapping(value = "/leave-comment-form/{processInstanceId}")
    public ResponseEntity<FormFieldsDTO> leaveCommentForm(@PathVariable String processInstanceId) throws IllegalAccessException, NoSuchFieldException, JsonProcessingException {
        Task task = taskService.createTaskQuery().processInstanceId(processInstanceId).list().get(0);
        TaskFormData taskFormData = formService.getTaskFormData(task.getId());
        List<FormField> formFields = taskFormData.getFormFields();

        List<Object> possibleOpinions = (List<Object>) runtimeService.getVariables(task.getExecutionId()).get("boardMemberPossibleOpinions");
        String possibleOpinionsString = mapListToJSON(possibleOpinions, "id", "value");

        formFields.get(1).getProperties().put("options", possibleOpinionsString);

        return new ResponseEntity<>(new FormFieldsDTO(task.getId(), formFields, processInstanceId,
                "https://localhost:8080/api/registration/board-member/leave-comment", ""), HttpStatus.OK);

    }

    // 10. KORAK
    @PostMapping(value = "/board-member/leave-comment/{taskId}")
    public ResponseEntity<?> submitComment(@RequestBody List<FormSubmissionDTO> formSubmissionDTOS,
                                           @PathVariable String taskId, @RequestHeader("Authorization") String token) {
        token = token.substring(7);
        BoardMember boardMember = boardMemberRepository.findByUsername(tokenUtils.getUsernameFromToken(token));

        Task task = taskService.createTaskQuery().taskId(taskId).singleResult();
        HashMap<String, Object> map = this.mapListToDto(formSubmissionDTOS);

        BoardMemberComment comment = new BoardMemberComment();
        comment.setBoardMember(boardMember);
        comment.setDate(new Date());
        comment.setReviewed(false);
        comment.setWriterMembershipRequest(writerMembershipRequestRepository.findByUsername((String) runtimeService.getVariables(task.getProcessInstanceId()).get("registeredUser")));
        formSubmissionDTOS.forEach(formField -> {
            if (formField.getFieldId().equals("comment")) {
                comment.setText(formField.getFieldValue());
            }
            if (formField.getFieldValue().equals("1")) {
                comment.setOpinion(Opinion.APPROVED);
            } else if (formField.getFieldValue().equals("2")) {
                comment.setOpinion(Opinion.NOT_APPROVED);
            } else {
                comment.setOpinion(Opinion.MORE_MATERIAL);
            }
        });
        boardMemberCommentRepository.save(comment);

        formService.submitTaskForm(taskId, map);
        return new ResponseEntity<>(Collections.singletonMap("processInstanceId", task.getProcessInstanceId()), HttpStatus.OK);
    }

    // 11. KORAK / WRITER JE APPROVED, MORA DA PLATI
    @PostMapping(value = "/writer/pay/{membershipId}")
    public ResponseEntity<?> writerPay(@PathVariable Long membershipId, @RequestHeader("Authorization") String token) {
        String username = tokenUtils.getUsernameFromToken(token.substring(7));

        List<ProcessInstance> allProcessInstances = getAllRunningProcessInstances("Process_registration");

        Task task = null;
        for (ProcessInstance pi : allProcessInstances) {
            task = taskService.createTaskQuery().processInstanceId(pi.getId()).taskAssignee(username).active().singleResult();
            break;
        }

        if (task != null) {
            runtimeService.setVariable(task.getProcessInstanceId(), "membership_id", membershipId.toString());
            taskService.complete(task.getId());
            return new ResponseEntity<>(HttpStatus.OK);
        }
        return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
    }

    private String mapListToJSON(List<Object> objects, String valueFieldName, String labelFieldName) throws NoSuchFieldException, IllegalAccessException, JsonProcessingException {
        List<SelectOptionDTO> selectOptionDTOList = new ArrayList<>();

        for (Object object : objects) {
            Field valueField = object.getClass().getDeclaredField(valueFieldName);
            Field labelField = object.getClass().getDeclaredField(labelFieldName);
            valueField.setAccessible(true);
            labelField.setAccessible(true);
            selectOptionDTOList.add(new SelectOptionDTO(String.valueOf(valueField.get(object)), String.valueOf(labelField.get(object))));
        }

        ObjectWriter ow = new ObjectMapper().writer().withDefaultPrettyPrinter();
        String json = ow.writeValueAsString(selectOptionDTOList);

//        System.err.println(json);

        return json;
    }


    private HashMap<String, Object> mapListToDto(List<FormSubmissionDTO> list) {
        HashMap<String, Object> map = new HashMap<String, Object>();
        for (FormSubmissionDTO temp : list) {
            map.put(temp.getFieldId(), temp.getFieldValue());
        }

        return map;
    }

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

    private ProcessInstance hasUserAlreadyStartedProcess(String processDefinitionId, String userId) {
        List<ProcessInstance> processInstances = getAllRunningProcessInstances(processDefinitionId);

        for (ProcessInstance processInstance : processInstances) {
            String username = (String) runtimeService.getVariable(processInstance.getId(), "registeredUser");
            if (username.equals(userId)) {
//                System.err.println("User already started this process.");
                return processInstance;
            }
        }
        return null;
    }

}
