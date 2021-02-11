package com.la.service.publish;

import org.camunda.bpm.engine.RuntimeService;
import org.camunda.bpm.engine.TaskService;
import org.camunda.bpm.engine.delegate.DelegateTask;
import org.camunda.bpm.engine.delegate.TaskListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class EditorDecidedOnCreate implements TaskListener {

    @Autowired
    TaskService taskService;

    @Autowired
    RuntimeService runtimeService;

    @Override
    public void notify(DelegateTask delegateTask) {
        try {
            String editor = (String) runtimeService.getVariable(delegateTask.getProcessInstanceId(), "editor");
            delegateTask.setAssignee(editor);
            System.err.println("Editor assigned to task. Task created.");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
