package com.la.service.registration;

import com.la.model.users.BoardMember;
import com.la.repository.BoardMemberRepository;
import org.camunda.bpm.engine.delegate.BpmnError;
import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.JavaDelegate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AssignBoardMembersService implements JavaDelegate {

    @Autowired
    public BoardMemberRepository boardMemberRepository;

    @Override
    public void execute(DelegateExecution delegateExecution) throws Exception {
        List<BoardMember> boardMembers = boardMemberRepository.findAll();

        if (boardMembers.size() == 0) {
            throw new BpmnError("NoAvailableBoardMembersError");
        }

        System.out.println("svi board memberi su" + boardMembers.size());

        delegateExecution.setVariable("board_members", boardMembers);
    }
}
