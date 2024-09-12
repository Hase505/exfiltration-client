package com.hase505.entities;


public class ExfiltrationFile extends ExfiltrationData{
    private byte[] data;
    private String fileName;
    private String pathToFile;

    public ExfiltrationFile(byte[] data, String fileName, String pathToFile){
        super(data);
        this.fileName = fileName;
        this.pathToFile = pathToFile;
    }
}
