package com.tm.invoice.mongo.domain;

import java.util.UUID;

import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

import org.hibernate.annotations.Type;
import org.springframework.data.mongodb.core.mapping.Document;

import com.tm.commonapi.web.core.data.IEntity;

@Document(collection = "profileImage")
public class ProfileImage implements IEntity<UUID> {

    private static final long serialVersionUID = -7084115455113105276L;

    @Id
    @Column(name = "id")
    @Type(type = "uuid-char")
    @GeneratedValue(generator = "id")
    private UUID id;

    @Column(name = "employeeId")
    private Long employeeId;

    @Column(name = "fileName")
    private String fileName;

    @Column(name = "mimeType")
    private String mimeType;

    @Column(name = "size")
    private Long size;

    @Column(name = "directory")
    private String directory;

    public Long getEmployeeId() {
        return employeeId;
    }

    public String getFileName() {
        return fileName;
    }

    public String getMimeType() {
        return mimeType;
    }

    public Long getSize() {
        return size;
    }

    public String getDirectory() {
        return directory;
    }

    public void setEmployeeId(Long employeeId) {
        this.employeeId = employeeId;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public void setMimeType(String mimeType) {
        this.mimeType = mimeType;
    }

    public void setSize(Long size) {
        this.size = size;
    }

    public void setDirectory(String directory) {
        this.directory = directory;
    }

    @Override
    public UUID getId() {
        return id;
    }

    @Override
    public void setId(UUID id) {
        this.id = id;
    }
}
