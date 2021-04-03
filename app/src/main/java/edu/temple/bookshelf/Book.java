package edu.temple.bookshelf;

import android.os.Parcel;
import android.os.Parcelable;

import java.io.Serializable;

public class Book implements Parcelable{

    //int id;
    int image;
    //String coverURL;
    String title;
    String author;

    public Book(){

    }
    public Book(String title,String author, int image){
        this.title = title;
        this.author = author;
        this.image = image;
    }

    public Book(String newURL,String title,String author, int id, int image){
        //this.id = id;
        //this.coverURL = newURL;
        this.title = title;
        this.author = author;
        this.image = image;
    }

    protected Book(Parcel in) {
        //id = in.readInt();
        image = in.readInt();
        //coverURL = in.readString();
        title = in.readString();
        author = in.readString();
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

//    public String getURL(){
//        return this.coverURL;
//    }

//    public int getID(){
//        return this.id;
//    }

    public int getImage(){
        return this.image;
    }

    public String getTitle(){
        return this.title;
    }

    public String getAuthor(){
        return this.author;
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        //dest.writeInt(id);
        dest.writeInt(image);
        //dest.writeString(coverURL);
        dest.writeString(title);
        dest.writeString(author);
    }
}
