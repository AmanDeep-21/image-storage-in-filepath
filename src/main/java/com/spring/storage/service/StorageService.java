package com.spring.storage.service;

import com.spring.storage.entity.FileData;
import com.spring.storage.respository.FileDataRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.Optional;

@Service
public class StorageService {

    @Autowired
    private FileDataRepository fileDataRepository;

    private static final String FOLDER_PATH = "/Users/coder/Documents/Spring-Projects/MyImageStorageDirectory/";

    public String uploadImageToFileSystem(MultipartFile file) throws IOException {

        String filePath = FOLDER_PATH + file.getOriginalFilename();
        FileData fileData = fileDataRepository.save(FileData.builder()
                        .name(file.getOriginalFilename())
                        .type(file.getContentType())
                        .filePath(filePath)
                        .build());

        file.transferTo(new File(filePath));

        if (fileData != null) {
            return "File uploaded successfully : " + filePath;
        }

        return null;
    }

    public byte[] downloadImageFromFileSystem(String fileName) throws IOException {
        Optional<FileData> fileData = fileDataRepository.findByName(fileName);
        String filePath = fileData.get().getFilePath();
        byte[] files = Files.readAllBytes(new File(filePath).toPath());
        return files;
    }
}
