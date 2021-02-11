package com.la.service.publish;

import com.la.model.Genre;
import com.la.model.publish.PublishBookRequest;
import com.la.model.users.Reader;
import com.la.repository.GenreRepository;
import com.la.repository.ReaderRepository;
import org.camunda.bpm.engine.delegate.BpmnError;
import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.JavaDelegate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SetBetaReadersService implements JavaDelegate {

    @Autowired
    ReaderRepository readerRepository;

    @Autowired
    GenreRepository genreRepository;

    @Override
    public void execute(DelegateExecution delegateExecution) throws Exception {
        try {
            PublishBookRequest publishBookRequest = (PublishBookRequest) delegateExecution.getVariable("publishBookRequest");

            Genre genre = genreRepository.findByValue(publishBookRequest.getGenre());

            List<Reader> readerList = readerRepository.findByBetaIsTrueAndBetaReaderGenresContains(genre);

            System.err.println(readerList.size());
            if (readerList.size() == 0) {
                throw new BpmnError("UserNotFound");
            }

            delegateExecution.setVariable("betaBefore", readerList);
        } catch (Exception e) {
            e.printStackTrace();
            throw new BpmnError("UserNotFound");
        }

    }
}
