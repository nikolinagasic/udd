package com.la.service.registration;

import com.la.dto.FormSubmissionDTO;
import com.la.model.Genre;
import com.la.model.users.Reader;
import com.la.repository.GenreRepository;
import com.la.repository.ReaderRepository;
import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.JavaDelegate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
public class UpdateReaderService implements JavaDelegate {

    @Autowired
    private GenreRepository genreRepository;

    @Autowired
    private ReaderRepository readerRepository;

    @Override
    public void execute(DelegateExecution delegateExecution) throws Exception {

        Reader reader = readerRepository.findByUsername(delegateExecution.getVariable("registeredUser").toString());
        System.out.println("KORISNIK JE " + reader.getUsername());

        List<FormSubmissionDTO> preferences = (List<FormSubmissionDTO>) delegateExecution.getVariable("betaReaderWantedGenres");
        preferences.forEach(formField -> {
            if (formField.getFieldId().equals("wantedGenres")) {
                String[] genres = formField.getFieldValue().split(",");

                Set<Genre> genreSet = new HashSet<>();
                for (String genre : genres) {
                    genreSet.add(genreRepository.findById(Long.parseLong(genre)).get());
                }
                reader.setBetaReaderGenres(genreSet);
            }
        });
        reader.setBeta(true);
        readerRepository.save(reader);
    }
}
