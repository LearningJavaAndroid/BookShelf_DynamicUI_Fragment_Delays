package edu.temple.bookshelf;

import java.io.Serializable;
import java.util.ArrayList;

public class BookList implements Serializable {

    ArrayList<Book> bookList;

    public BookList(){
        bookList = new ArrayList<Book>();
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

    public Book get(int pos){
        Book book = bookList.get(pos);
        return book;
    }
    public int size(){
        return bookList.size();
    }

}
