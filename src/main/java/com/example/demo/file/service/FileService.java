package com.example.demo.file.service;

import com.example.demo.file.repository.FileRepository;
import com.example.demo.product.entity.Product;
import groovy.util.logging.Slf4j;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.apache.commons.io.FilenameUtils;
import com.example.demo.file.entity.FileUploadEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
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

                    FileUploadEntity fileUpload = FileUploadEntity.builder()
                            .originalName(fileName)
                            .fileName(saveFileName)
                            .filePath("/uploads/" + saveFileName)
                            .ext(ext)
                            .size(fileSize)
                            .product(product)
                            .build();

                    fileRepository.save(fileUpload);

                }
            } catch (IOException e) {
                e.printStackTrace();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }


    }

    @Transactional
    public void modify(MultipartFile[] files, Product product) throws IOException {

        List<FileUploadEntity> fileUploadEntityList = fileRepository.findAllByProduct(product);

        for (FileUploadEntity fileUploadEntity : fileUploadEntityList) {
            File file = new File(fileUploadEntity.getFilePath());
            if (file.exists()) {
                file.delete();
            }
            fileRepository.delete(fileUploadEntity);
        }

        try {
            for (MultipartFile file : files) {
                String fileName = file.getOriginalFilename();
                String ext = FilenameUtils.getExtension(String.valueOf(new File(file.getOriginalFilename())));
                String saveFileName = String.valueOf(System.currentTimeMillis()) + "." + ext;
                int fileSize = (int) file.getSize();

                file.transferTo(new File(filePath, saveFileName));

                FileUploadEntity fileUpload = FileUploadEntity.builder()
                        .originalName(fileName)
                        .fileName(saveFileName)
                        .filePath("/uploads/" + saveFileName)
                        .ext(ext)
                        .size(fileSize)
                        .product(product)
                        .build();

                fileRepository.save(fileUpload);
            }
        } catch (IOException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void delete(FileUploadEntity file) {
        file.setDeleted(true);
        file.setDeleteDt(LocalDateTime.now());
        fileRepository.save(file);
    }

    public FileUploadEntity findById(Long fileId) {
        return fileRepository.findById(fileId).orElse(null);
    }
}
