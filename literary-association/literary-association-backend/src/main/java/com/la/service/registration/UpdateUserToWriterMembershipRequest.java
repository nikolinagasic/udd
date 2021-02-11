package com.la.service.registration;

import com.la.model.enums.WriterMembershipStatus;
import com.la.model.registration.WriterMembershipRequest;
import com.la.model.users.SysUser;
import com.la.repository.RoleRepository;
import com.la.repository.UserRepository;
import com.la.repository.WriterMembershipRequestRepository;
import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.JavaDelegate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.parameters.P;
import org.springframework.stereotype.Service;

import java.util.Calendar;
import java.util.Collections;
import java.util.Date;

@Service
public class UpdateUserToWriterMembershipRequest implements JavaDelegate {

    @Autowired
    private UserRepository<SysUser> userRepository;

    @Autowired
    private WriterMembershipRequestRepository writerMembershipRequestRepository;

    @Autowired
    private RoleRepository roleRepository;

    @Override
    public void execute(DelegateExecution delegateExecution) throws Exception {

        SysUser user = (SysUser) userRepository.findByUsername(delegateExecution.getVariable("registeredUser").toString());
        WriterMembershipRequest writerMembershipRequest = new WriterMembershipRequest();

        writerMembershipRequest.setUsername(user.getUsername());
        writerMembershipRequest.setFirstName(user.getFirstName());
        writerMembershipRequest.setLastName(user.getLastName());
        writerMembershipRequest.setPassword(user.getPassword());
        writerMembershipRequest.setCity(user.getCity());
        writerMembershipRequest.setState(user.getState());
        writerMembershipRequest.setEmail(user.getEmail());
        writerMembershipRequest.setActive(true);
        writerMembershipRequest.setGenres(user.getGenres());
        writerMembershipRequest.setRoles(Collections.singleton(roleRepository.findById(7L).get()));
        writerMembershipRequest.setDeleted(user.isDeleted());
        writerMembershipRequest.setStatus(WriterMembershipStatus.WAITING_SUBMIT);
        writerMembershipRequest.setAttemptsNumber(0);
        writerMembershipRequest.setFilesPosted(0);

        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.DAY_OF_MONTH, 14);

        writerMembershipRequest.setSubmissionDeadline(calendar.getTime());
        delegateExecution.setVariable("submissionDeadline", calendar.getTime());

        try {
            userRepository.deleteById(user.getId());
            writerMembershipRequestRepository.save(writerMembershipRequest);
//            delegateExecution.setVariable(user.getUsername() + "pid", delegateExecution.getProcessInstanceId());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
