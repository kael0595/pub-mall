package com.example.demo.file.repository;

import com.example.demo.file.entity.FileUploadEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface FileRepository extends JpaRepository<FileUploadEntity, Long> {
}
