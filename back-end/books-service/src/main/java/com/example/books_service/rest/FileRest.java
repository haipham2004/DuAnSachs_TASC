package com.example.books_service.rest;

import com.example.books_service.dto.response.UpLoadFileResponse;
import com.example.books_service.service.FileService;
import jakarta.annotation.Resource;
import lombok.Getter;
import org.apache.tomcat.util.http.fileupload.FileUploadException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.FileSystemResource;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.Instant;
import java.util.Arrays;
import java.util.List;

@RestController
public class FileRest {

    private FileService fileService;

    @Autowired
    public FileRest(FileService fileService) {
        this.fileService = fileService;
    }

    @Value("${haipham.upload-file-base-uri}")
    private String baseURI;

    @PostMapping("/files")
    public ResponseEntity<UpLoadFileResponse> upload(
            @RequestParam(name = "file", required = false) MultipartFile file,
            @RequestParam(name="folder",required = false) String folder) throws URISyntaxException, IOException {
        if(file.isEmpty() || file==null){
            throw new FileUploadException(("File không tồn tại"));
        }
        String fileName = file.getOriginalFilename();
        List<String> allowedExtensions = Arrays.asList("pdf", "jpg", "jpeg", "png", "doc", "docx");
        // Validate extension
        boolean isValidExtension = allowedExtensions.stream().anyMatch(ext -> fileName.toLowerCase().endsWith("." + ext));
        if (!isValidExtension) {
            throw new FileUploadException(("File không đúng định dạng"+allowedExtensions.toString()));
        }


        fileService.createUploadFolder(baseURI+folder);
        String uploadFile= fileService.store(file,folder);
        UpLoadFileResponse upLoadFileResponse= new UpLoadFileResponse(uploadFile, Instant.now());
        return ResponseEntity.ok().body(upLoadFileResponse);
    }



}
