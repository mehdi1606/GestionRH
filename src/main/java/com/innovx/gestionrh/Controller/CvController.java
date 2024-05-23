package com.innovx.gestionrh.Controller;



import com.innovx.gestionrh.Service.FileSearchService;
import com.innovx.gestionrh.Service.FileStorageService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

@RestController
@RequestMapping("/api/cvs")
public class CvController {


    @Autowired
    private FileSearchService fileSearchService;
    @Autowired
    private FileStorageService fileStorageService;

    @PostMapping("/upload")
    public ResponseEntity<?> uploadFiles(@RequestParam("files") MultipartFile[] files) {
        try {
            for (MultipartFile file : files) {
                fileStorageService.save(file);
            }
            return ResponseEntity.status(HttpStatus.OK).body("Files uploaded successfully!");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.EXPECTATION_FAILED).body("Failed to upload files.");
        }
    }
    @GetMapping("/keyword")
    public ResponseEntity<?> searchFiles(@RequestParam("keyword") String keyword) {
        List<String> matchingFiles = fileSearchService.searchKeyword(keyword);
        if (matchingFiles.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("No files found containing the keyword.");
        } else {
            return ResponseEntity.status(HttpStatus.OK).body(matchingFiles);
        }
    }
}
