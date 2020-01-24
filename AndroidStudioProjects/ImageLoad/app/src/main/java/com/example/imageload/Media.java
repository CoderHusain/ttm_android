package com.example.imageload;

public class Media {
    public int mediaId;
    public String mediaType;
    public String mediaUrl;
    public int mediaCount;

    public Media(int mediaId,String mediaType,String mediaUrl,int mediaCount){
        this.mediaId = mediaId;
        this.mediaType = mediaType;
        this.mediaUrl = mediaUrl;
        this.mediaCount = mediaCount;
    }
}
