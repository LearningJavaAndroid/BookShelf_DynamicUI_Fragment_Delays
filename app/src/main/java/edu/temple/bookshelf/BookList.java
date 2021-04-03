package edu.temple.bookshelf;

import android.os.Parcel;
import android.os.Parcelable;

import java.io.Serializable;
import java.util.ArrayList;

public class BookList implements Parcelable {

    private ArrayList<Book> bookList;

    public BookList(){
        bookList = new ArrayList<Book>();
    }


    protected BookList(Parcel in) {
        bookList = in.createTypedArrayList(Book.CREATOR);
    }

    public static final Creator<BookList> CREATOR = new Creator<BookList>() {
        @Override
        public BookList createFromParcel(Parcel in) {
            return new BookList(in);
        }

        @Override
        public BookList[] newArray(int size) {
            return new BookList[size];
        }
    };
    public ArrayList getBookList(){
        return this.bookList;
    }

    public void add(Book book){
        bookList.add(book);
    }

    public void remove(int pos){
        bookList.remove(pos);
    }

    public void modify(Book book, int pos){
        bookList.set(pos, book);
    }

    public Book getBook(int pos){
        return bookList.get(pos);
    }
    public int sizeBookList(){
        return bookList.size();
    }

    public void setBookList(ArrayList newBookList){
        bookList = newBookList;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeTypedList(bookList);
    }
}
