package com.la.service.publish;

import com.la.model.enums.PublishStatus;
import com.la.model.publish.PublishBookRequest;
import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.ExecutionListener;

public class AllBetaReadersCommented implements ExecutionListener {
    @Override
    public void notify(DelegateExecution delegateExecution) {
        try {
            System.err.println("ALL BETA READERS COMMENTED . . .");
            PublishBookRequest publishBookRequest = (PublishBookRequest) delegateExecution.getVariable("publishBookRequest");
            publishBookRequest.setStatus(PublishStatus.WAITING_COMMENT_CHECK.toString());
            delegateExecution.setVariable("publishBookRequest", publishBookRequest);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
