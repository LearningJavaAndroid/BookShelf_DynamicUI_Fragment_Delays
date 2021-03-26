package edu.temple.bookshelf;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

public class BookAdapter extends BaseAdapter {

    Context context;
    BookList bookList;

    public BookAdapter(Context context, BookList bookList){
        this.context = context;
        this.bookList = bookList;
    }

    @Override
    public int getCount() {
        return bookList.size();
    }

    @Override
    public Object getItem(int position) {
        return bookList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        TextView BookName;


        if(convertView == null){
            BookName = new TextView(context);
            BookName.setTextSize(22);
            BookName.setPadding(15,20,0,5);

        }else{
            BookName = (TextView) convertView;

        }
        BookName.setText(bookList.get(position).title);
        return BookName;
    }
}
