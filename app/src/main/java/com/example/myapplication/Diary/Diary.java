package com.example.myapplication.Diary;

//db 적용시 필요한 코드
public class Diary {
    private int id;
    private String title;
    private String content;
    private byte[] image;

    public Diary(String title, String content, byte[] image, int id) {
        this.title = title;
        this.content = content;
        this.image = image;
        this.id = id;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public byte[] getImage() {
        return image;
    }

    public void setImage(byte[] image) {
        this.image = image;
    }


}

