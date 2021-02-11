package com.la.service.registration;

import com.la.model.registration.WriterMembershipRequest;
import com.la.model.users.Writer;
import com.la.repository.MembershipRepository;
import com.la.repository.RoleRepository;
import com.la.repository.WriterMembershipRequestRepository;
import com.la.repository.WriterRepository;
import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.JavaDelegate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.HashSet;

@Service
public class CreateWriterService implements JavaDelegate {

    @Autowired
    private WriterMembershipRequestRepository writerMembershipRequestRepository;

    @Autowired
    private WriterRepository writerRepository;

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private MembershipRepository membershipRepository;

    @Override
    public void execute(DelegateExecution delegateExecution) throws Exception {
        String username = (String) delegateExecution.getVariable("registeredUser");
        String membershipId = (String) delegateExecution.getVariable("membership_id");
        WriterMembershipRequest request = writerMembershipRequestRepository.findByUsername(username);

        Writer writer = new Writer();
        writer.setMembership(membershipRepository.getOne(Long.parseLong(membershipId)));
        writer.setUsername(request.getUsername());
        writer.setPassword(request.getPassword());
        writer.setFirstName(request.getFirstName());
        writer.setLastName(request.getLastName());
        writer.setState(request.getState());
        writer.setCity(request.getCity());
        writer.setActive(request.isActive());
        writer.setDeleted(request.isDeleted());
        writer.setGenres(new HashSet<>(request.getGenres()));
        writer.setEmail(request.getEmail());
        writer.setRoles(Collections.singleton(roleRepository.getOne(4L)));

        try {
            //ne brise
            writerMembershipRequestRepository.delete(request);
        } catch (Exception e) {
            e.printStackTrace();
        }
        writerRepository.save(writer);

        System.err.println("END OF PROCESS...");
    }
}
