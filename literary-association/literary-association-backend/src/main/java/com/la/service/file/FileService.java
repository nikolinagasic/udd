package com.la.service.file;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.ServletContext;
import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;

@Service
public class FileService {

    @Value("${dataDir}")
    String dataDir;

    public String saveUploadedFile(MultipartFile file, String processInstanceId) throws IOException {
        return saveUploadedFile(file, processInstanceId, "");
    }

    public String saveUploadedFile(MultipartFile file, String processInstanceId, String numberSuffix) throws IOException {
        String retVal = null;
        if (!file.isEmpty()) {
            byte[] bytes = file.getBytes();
            Path path = Paths.get(getResourceFilePath(dataDir) + File.separator + processInstanceId + numberSuffix + ".pdf");
            Files.write(path, bytes);
            retVal = path.toString();
        }
        return retVal;
    }

    public String getResourceFilePath(String path) {
        return new FileSystemResource(path).getFile().getAbsolutePath();
    }

    public Resource downloadFile(String filename) {
        Path path = Paths.get(getResourceFilePath(dataDir) + File.separator + filename);
        Resource resource = null;
        try {
            resource = new UrlResource(path.toUri());
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        return resource;
    }
}
