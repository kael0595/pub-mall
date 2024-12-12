package com.example.demo.file.service;

import com.example.demo.file.repository.FileRepository;
import com.example.demo.product.entity.Product;
import jakarta.transaction.Transactional;
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

    private String filePath = System.getProperty("user.dir") + "/src/main/resources/static/uploads/";

    @Transactional
    public void fileUpload(MultipartFile[] files, Product product) throws IOException {

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
            fileUpload.setFilePath("/uploads/" + newFileName);
            fileUpload.setProduct(product);

            fileRepository.save(fileUpload);

        }
    }

    public void delete(FileUploadEntity file) {
        file.setDeleted(true);
        fileRepository.save(file);
    }
}
