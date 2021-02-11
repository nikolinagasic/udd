package com.la.service.registration;

import com.la.model.enums.Opinion;
import com.la.model.registration.BoardMemberComment;
import com.la.model.registration.SubmittedWork;
import com.la.model.registration.WriterMembershipRequest;
import com.la.repository.BoardMemberCommentRepository;
import com.la.repository.SubmittedWorkRepository;
import com.la.repository.WriterMembershipRequestRepository;
import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.JavaDelegate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CheckOpinionsService implements JavaDelegate {

    @Autowired
    private SubmittedWorkRepository submittedWorkRepository;

    @Autowired
    private BoardMemberCommentRepository boardMemberCommentRepository;

    @Autowired
    private WriterMembershipRequestRepository writerMembershipRequestRepository;

    @Override
    public void execute(DelegateExecution delegateExecution) throws Exception {
        String username = (String) delegateExecution.getVariable("registeredUser");
        WriterMembershipRequest request = writerMembershipRequestRepository.findByUsername(username);

        List<BoardMemberComment> boardMemberComments = boardMemberCommentRepository.findByWriterMembershipRequestAndReviewedIsFalse(request);
        System.err.println("BOARD MEMBERS COUNT " + boardMemberComments.size());

        int approved = (int) boardMemberComments.stream().filter(comment -> comment.getOpinion().equals(Opinion.APPROVED)).count();
        int notApproved = (int) boardMemberComments.stream().filter(comment -> comment.getOpinion().equals(Opinion.NOT_APPROVED)).count();
        int needMoreWork = (int) boardMemberComments.stream().filter(comment -> comment.getOpinion().equals(Opinion.MORE_MATERIAL)).count();

        if (((float) notApproved >= ((float) (approved + notApproved + needMoreWork) / 2)) || (needMoreWork == 0 && notApproved > 0)) {
            delegateExecution.setVariable("submitted_work_status", "not_approved");
            boardMemberComments.forEach(boardMemberComment -> {
                boardMemberComment.setReviewed(true);
                boardMemberCommentRepository.save(boardMemberComment);
            });
        } else if (needMoreWork > 0) {
            delegateExecution.setVariable("submitted_work_status", "need_more_work");
        } else {
            delegateExecution.setVariable("submitted_work_status", "approved");
        }
    }
}
