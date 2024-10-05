package com.project.optics.services;

import org.springframework.core.io.PathResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Service;

import java.nio.file.Path;

@Service
public class FileService {

    public HttpHeaders prepareDownloadHeaders(String filename) {
        HttpHeaders headers = new HttpHeaders();
        headers.add(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=" + filename);
        return headers;
    }

    public Resource loadFileAsResource(Path filePath) {
        return new PathResource(filePath);
    }
}
