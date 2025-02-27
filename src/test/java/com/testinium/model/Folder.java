package com.testinium.model;

public enum Folder {
    REPORTS("reports/"),
    SCREENHOTS("screenshots/");
    private String folderName;

    Folder(String folderName) {this.folderName = folderName;}

    public String getFolderName() {return folderName;}
}
