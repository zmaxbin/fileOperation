package com.maxbin.fileoperation.domain;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

@Entity
public class FileEntity {

    @Id
    @GeneratedValue
    private Long id;

    private String filename;
    private String fileupdatetime;
    private Long filesize;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getFilename() {
        return filename;
    }

    public void setFilename(String filename) {
        this.filename = filename;
    }

    public String getFileupdatetime() {
        return fileupdatetime;
    }

    public void setFileupdatetime(String fileupdatetime) {
        this.fileupdatetime = fileupdatetime;
    }

    public Long getFilesize() {
        return filesize;
    }

    public void setFilesize(Long filesize) {
        this.filesize = filesize;
    }
}
