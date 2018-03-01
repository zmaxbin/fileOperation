package com.maxbin.fileoperation.repository;

import com.maxbin.fileoperation.domain.FileEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface FileRepository extends JpaRepository<FileEntity, Long> {
    List<FileEntity> findByFilename(String name);
}
