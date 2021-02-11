package com.la.service;

import com.la.model.publish.PublishBookRequest;

import java.util.HashMap;
import java.util.List;

public interface PublishBookService {

    PublishBookRequest makePublishBookRequest(HashMap<String, Object> publishBookRequestMap, String writerUsername);

    List<String> getReaders(HashMap<String, Object> readersMap);

}
