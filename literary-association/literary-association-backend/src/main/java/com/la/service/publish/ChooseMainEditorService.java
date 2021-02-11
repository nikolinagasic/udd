package com.la.service.publish;

import com.la.model.publish.PublishBookRequest;
import com.la.model.users.SysUser;
import com.la.repository.UserRepository;
import org.camunda.bpm.engine.TaskService;
import org.camunda.bpm.engine.delegate.BpmnError;
import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.JavaDelegate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Random;

@Service
public class ChooseMainEditorService implements JavaDelegate {

    @Autowired
    UserRepository<SysUser> userRepository;

    @Autowired
    TaskService taskService;

    @Override
    public void execute(DelegateExecution delegateExecution) throws Exception {
        try {
            List<SysUser> userList = userRepository.findByType("EDITOR");
            Random rand = new Random();
            SysUser randUser = userList.get(rand.nextInt(userList.size()));

            if (randUser == null) {
                throw new BpmnError("UserNotFound");
            }

            delegateExecution.setVariable("editor", randUser.getUsername());

            PublishBookRequest publishBookRequest = (PublishBookRequest) delegateExecution.getVariable("publishBookRequest");
            publishBookRequest.setEditor(randUser.getUsername());
            delegateExecution.setVariable("publishBookRequest", publishBookRequest);

            System.err.println("Choosen editor with ID : " + delegateExecution.getVariable("editor"));
        } catch (Exception e) {
            e.printStackTrace();
            throw new BpmnError("UserNotFound");
        }
    }
}
