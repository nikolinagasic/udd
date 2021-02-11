package com.la.service.publish;

import com.la.model.Book;
import com.la.model.Script;
import com.la.model.publish.PublishBookRequest;
import com.la.model.users.Editor;
import com.la.model.users.Lector;
import com.la.model.users.Writer;
import com.la.repository.*;
import org.apache.commons.lang.RandomStringUtils;
import org.camunda.bpm.engine.delegate.BpmnError;
import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.JavaDelegate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;

@Service
public class PrintService implements JavaDelegate {

    @Autowired
    BookRepository bookRepository;

    @Autowired
    WriterRepository writerRepository;

    @Autowired
    EditorRepository editorRepository;

    @Autowired
    LectorRepository lectorRepository;

    @Autowired
    GenreRepository genreRepository;

    @Autowired
    PublisherRepository publisherRepository;

    @Autowired
    ScriptRepository scriptRepository;

    @Override
    public void execute(DelegateExecution delegateExecution) throws Exception {
        try {
            PublishBookRequest publishBookRequest = (PublishBookRequest) delegateExecution.getVariable("publishBookRequest");

            Book book = new Book();
            book.setEditor((Editor) editorRepository.findByUsername(publishBookRequest.getEditor()));
            book.setWriter((Writer) writerRepository.findByUsername(publishBookRequest.getWriter()));
            book.setLector((Lector) lectorRepository.findByUsername(publishBookRequest.getLector()));
            book.setGenre(genreRepository.findByValue(publishBookRequest.getGenre()));
            book.setIsbn(generateISBN().toUpperCase());
            book.setKeyWords(null);
            book.setPagesNumber(522);
            book.setPrice(200.99);
            book.setPublishedYear(LocalDate.now().getYear());
            book.setSynopsis(publishBookRequest.getSynopsis());
            book.setTitle(publishBookRequest.getTitle());
            book.setPublisher(publisherRepository.findAll().get(0));

            Script script = new Script();
            script.setPath(publishBookRequest.getPath());
            script = scriptRepository.save(script);

            book.setScript(script);

            bookRepository.save(book);
        }
        catch (Exception e) {
            e.printStackTrace();
            throw new BpmnError("PrintFailed");
        }

    }

    public static String generateISBN() {
        String generatedString = RandomStringUtils.random(13, true, true);
        return generatedString;
    }
}
