package com.la.service.registration;

import com.la.model.enums.WriterMembershipStatus;
import com.la.model.registration.WriterMembershipRequest;
import com.la.repository.WriterMembershipRequestRepository;
import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.JavaDelegate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Calendar;

@Service
public class UpdateWriterMembershipRequestService implements JavaDelegate {

    @Autowired
    private WriterMembershipRequestRepository writerMembershipRequestRepository;

    @Override
    public void execute(DelegateExecution delegateExecution) throws Exception {
        String username = (String) delegateExecution.getVariable("registeredUser");
        WriterMembershipRequest request = writerMembershipRequestRepository.findByUsername(username);

        int timesAttempted = request.getAttemptsNumber();

        request.setStatus(WriterMembershipStatus.WAITING_SUBMIT);
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.DAY_OF_MONTH, 14);
        request.setPaymentDeadline(calendar.getTime());

        delegateExecution.setVariable("submissionDeadline", calendar.getTime());
        delegateExecution.setVariable("timesAttempted", timesAttempted);
    }
}
