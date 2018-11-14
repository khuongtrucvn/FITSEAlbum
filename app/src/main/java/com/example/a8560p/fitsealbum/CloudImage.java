package com.example.a8560p.fitsealbum;

public class CloudImage {
    public String NameImage;
    public  String LinkImage;

    public CloudImage(){ }

    public CloudImage(String nameImage, String linkImage ){
        NameImage=nameImage;
        LinkImage=linkImage;
    }

    public String getUrl(){
        return LinkImage;
    }

}
