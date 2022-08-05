package com.example.blogapp;

import java.io.Serializable;

public class BlogModel implements Serializable {
    String title, content ,user_id,image,blog_id;



    public BlogModel(String blog_id, String title, String content, String user_id, String image) {
        this.title = title;
        this.content = content;
        this.user_id = user_id;
        this.image = image;
        this.blog_id = blog_id;
    }

    public String getTitle() {
        return title;
    }

    public String getContent() {
        return content;
    }

    public String getUser_id() {
        return user_id;
    }

    public String getImage() {
        return image;
    }

    public BlogModel() {
    }

    public String getBlog_id() {
        return blog_id;
    }
}
