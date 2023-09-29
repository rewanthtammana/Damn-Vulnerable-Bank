package com.app.damnvulnerablebank;

import java.io.Serializable;

public class FileInfo implements Serializable {
    private String fileName;
    private String fileID;

    public FileInfo(String fileName, String fileID) {
        this.fileName = fileName;
        this.fileID = fileID;
    }

    public FileInfo() { }

    public String getFileName() {
        return fileName;
    }

    public String getFileID() {
        return fileID;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public void setFileID(String fileID) {
        this.fileID = fileID;
    }
}
