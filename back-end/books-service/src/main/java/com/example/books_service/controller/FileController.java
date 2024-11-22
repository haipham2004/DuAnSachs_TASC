package com.example.books_service.controller;

import com.example.books_service.dto.response.ApiResponse;
import com.example.books_service.dto.response.UpLoadFileResponse;
import com.example.books_service.service.FileService;
import org.apache.tomcat.util.http.fileupload.FileUploadException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.net.URISyntaxException;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@RestController
public class FileController {

    private FileService fileService;

    @Autowired
    public FileController(FileService fileService) {
        this.fileService = fileService;
    }

    @Value("${haipham.upload-file-base-uri}")
    private String baseURI;

    @PostMapping("/files")
    public ApiResponse<List<UpLoadFileResponse>> upload(
            @RequestParam(name = "file") MultipartFile[] files,
            @RequestHeader(name = "upload-type", defaultValue = "book") String uploadType) throws URISyntaxException, IOException {

        List<UpLoadFileResponse> responses = new ArrayList<>();

        if (files.length == 0) {
            throw new FileUploadException("Không có tệp nào được chọn để tải lên.");
        }

        // Kiểm tra từng tệp
        for (MultipartFile file : files) {
            if (file.isEmpty()) {
                throw new FileUploadException("File không tồn tại");
            }

            String fileName = file.getOriginalFilename();
            List<String> allowedExtensions = Arrays.asList("pdf", "jpg", "jpeg", "png", "doc", "docx");
            boolean isValidExtension = allowedExtensions.stream().anyMatch(ext -> fileName.toLowerCase().endsWith("." + ext));
            if (!isValidExtension) {
                throw new FileUploadException("File không đúng định dạng: " + allowedExtensions.toString());
            }

            // Tiến hành lưu tệp hoặc xử lý khác...
            fileService.createUploadFolder(baseURI + uploadType);
            String uploadFile = fileService.store(file, uploadType);

            // Lưu thông tin phản hồi cho từng tệp
            UpLoadFileResponse upLoadFileResponse = new UpLoadFileResponse(uploadFile, Instant.now());
            responses.add(upLoadFileResponse);
        }
          return ApiResponse.<List<UpLoadFileResponse>>builder().data(responses).build();
    }






}
