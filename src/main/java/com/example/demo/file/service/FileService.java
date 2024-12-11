package com.example.demo.file.service;

import com.example.demo.file.repository.FileRepository;
import lombok.RequiredArgsConstructor;
import org.apache.commons.io.FilenameUtils;
import com.example.demo.file.entity.FileUploadEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;

@Service
@RequiredArgsConstructor
public class FileService {

    private final FileRepository fileRepository;

    private String filePath =System.getProperty("user.dir") + "/uploads/";

    public void fileUpload(MultipartFile[] files, long id) throws IOException {

        File chkFile = new File(filePath);

        if (!chkFile.exists()) {
            chkFile.mkdirs();
        }

        for (MultipartFile file : files) {
            String fileName = file.getOriginalFilename();
            String ext = FilenameUtils.getExtension(String.valueOf(new File(file.getOriginalFilename())));
            String newFileName = String.valueOf(System.currentTimeMillis()) + "." + ext;
            int fileSize = (int) file.getSize();

            file.transferTo(new File(filePath, newFileName));

            FileUploadEntity fileUpload = new FileUploadEntity();
            fileUpload.setFileName(newFileName);
            fileUpload.setOriginalName(fileName);
            fileUpload.setSize(fileSize);
            fileUpload.setExt(ext);
            fileUpload.setFilePath(filePath);

            fileRepository.save(fileUpload);

        }
    }
}
