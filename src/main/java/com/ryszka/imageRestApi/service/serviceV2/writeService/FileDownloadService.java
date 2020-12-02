package com.ryszka.imageRestApi.service.serviceV2.writeService;

import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import javax.persistence.SecondaryTable;
import javax.servlet.http.HttpServletResponse;

@Service
public class FileDownloadService {
    public static final String FILE_DIRECTORY = "C:\\xampp\\htdocs\\files\\";

    public Resource getFileSystem(String filename, String filePath, HttpServletResponse resp) {
        return getResource(filename, filePath,resp);
    }

    private Resource getResource(String filename, String filePath, HttpServletResponse resp) {
        resp.setContentType("image/jpeg");
        resp.setHeader("Content-Disposition", "attachment; filename=" + filename);
        return new FileSystemResource(FILE_DIRECTORY + filePath + "\\" + filename);
    }
}
