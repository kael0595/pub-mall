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
import java.time.LocalDateTime;

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
        if (!files[0].isEmpty()) {
            try {
                for (MultipartFile file : files) {
                    String fileName = file.getOriginalFilename();
                    String ext = FilenameUtils.getExtension(String.valueOf(new File(file.getOriginalFilename())));
                    String saveFileName = String.valueOf(System.currentTimeMillis()) + "." + ext;
                    int fileSize = (int) file.getSize();

                    file.transferTo(new File(filePath, saveFileName));

                    FileUploadEntity fileUpload = new FileUploadEntity();
                    fileUpload.setFileName(saveFileName);
                    fileUpload.setOriginalName(fileName);
                    fileUpload.setSize(fileSize);
                    fileUpload.setExt(ext);
                    fileUpload.setFilePath("/uploads/" + saveFileName);
                    fileUpload.setProduct(product);

                    fileRepository.save(fileUpload);

                }
            } catch (IOException e) {
                e.printStackTrace();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }


    }

    public void delete(FileUploadEntity file) {
        file.setDeleted(true);
        file.setDeleteDt(LocalDateTime.now());
        fileRepository.save(file);
    }
}
