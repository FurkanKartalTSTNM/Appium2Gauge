package com.testinium.model;

public enum Folder {
    REPORTS("reports/");

    private String folderName;

    Folder(String folderName) {this.folderName = folderName;}

    public String getFolderName() {return folderName;}
}
