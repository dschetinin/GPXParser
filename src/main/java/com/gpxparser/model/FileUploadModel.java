package com.gpxparser.model;

import org.springframework.web.multipart.MultipartFile;

/**
 * User: dschetinin <a href="mailto:schetinin.d@gmail.com"/>schetinin.d@gmail.com</a>
 * Date: 10/12/2016
 * Time: 3:00 PM
 * To change this template use File | Settings | File Templates.
 */
public class FileUploadModel {

    private String comment;

    private MultipartFile file;

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public MultipartFile getFile() {
        return file;
    }

    public void setFile(MultipartFile file) {
        this.file = file;
    }
}
