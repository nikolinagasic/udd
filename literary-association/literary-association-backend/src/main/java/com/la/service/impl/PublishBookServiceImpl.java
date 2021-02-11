package com.la.service.impl;

import com.la.model.Genre;
import com.la.model.enums.PublishStatus;
import com.la.model.publish.PublishBookRequest;
import com.la.model.users.Reader;
import com.la.model.users.SysUser;
import com.la.repository.GenreRepository;
import com.la.repository.ReaderRepository;
import com.la.repository.UserRepository;
import com.la.service.PublishBookService;
import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

@Service
public class PublishBookServiceImpl implements PublishBookService {

    @Autowired
    private UserRepository<SysUser> userRepository;

    @Autowired
    private GenreRepository genreRepository;

    @Autowired
    private ReaderRepository readerRepository;

    public PublishBookRequest makePublishBookRequest(HashMap<String, Object> publishBookRequestMap, String writerUsername) {
        String title = (String) publishBookRequestMap.get("title");
        String synopsis = (String) publishBookRequestMap.get("synopsis");
        Long genreId = Long.parseLong((String) publishBookRequestMap.get("genre"));

        SysUser sysUser = userRepository.findByUsername(writerUsername);
        Genre genre = genreRepository.findById(genreId).get();

        PublishBookRequest publishBookRequest = new PublishBookRequest();
        publishBookRequest.setTitle(title);
        publishBookRequest.setSynopsis(synopsis);
        publishBookRequest.setGenre(genre.getValue());
        publishBookRequest.setWriter(sysUser.getUsername());

        return publishBookRequest;
    }

    public List<String> getReaders(HashMap<String, Object> readersMap){
        String[] usernames = ((String) readersMap.get("beta_readers")).split(",");
        List<String> readers = new ArrayList<>();
        for(String username : usernames){
            readers.add(username);
        }

        return readers;
    }
}
