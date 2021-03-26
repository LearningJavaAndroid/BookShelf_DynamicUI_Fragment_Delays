package edu.temple.bookshelf;

import android.os.Parcelable;

import java.io.Serializable;

public class Book implements Serializable{
    String title;
    String author;
    int image;

    public Book(String title, String author, int image){
        this.title =  title;
        this.author = author;
        this.image = image;
    }

    public String getTitle(){
        return this.title;
    }

    public String getAuthor(){
        return this.author;
    }

    public int getImage(){
        return this.image;
    }

}
