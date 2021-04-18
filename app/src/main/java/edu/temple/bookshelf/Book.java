package edu.temple.bookshelf;

import android.os.Parcel;
import android.os.Parcelable;

import java.io.Serializable;

public class Book implements Parcelable{

    private int id;
    private String coverURL;
    private String title;
    private String author;
    private int duration;

    public Book(){ }

    public Book(String newURL,String title,String author, int id){
        this.id = id;
        this.coverURL = newURL;
        this.title = title;
        this.author = author;
    }

    public Book(String newURL,String title,String author, int id, int duration){
        this.id = id;
        this.coverURL = newURL;
        this.title = title;
        this.author = author;
        this.duration = duration;
    }


    protected Book(Parcel in) {
        id = in.readInt();
        coverURL = in.readString();
        title = in.readString();
        author = in.readString();
        duration = in.readInt();
    }

    public static final Creator<Book> CREATOR = new Creator<Book>() {
        @Override
        public Book createFromParcel(Parcel in) {
            return new Book(in);
        }

        @Override
        public Book[] newArray(int size) {
            return new Book[size];
        }
    };

    public String getURL(){
        return this.coverURL;
    }

    public int getID(){
        return this.id;
    }

    public String getTitle(){
        return this.title;
    }

    public String getAuthor(){
        return this.author;
    }

    public int getDuration(){return this.duration;}

    public void setDuration(int duration) {
        this.duration = duration;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(id);
        dest.writeString(coverURL);
        dest.writeString(title);
        dest.writeString(author);
        dest.writeInt(duration);
    }
}
