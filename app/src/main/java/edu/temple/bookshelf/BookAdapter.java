package edu.temple.bookshelf;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
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
        TextView Author;
        LinearLayout linearLayout;


        if(convertView == null){
            linearLayout = new LinearLayout(context);
            BookName = new TextView(context);
            Author = new TextView(context);
            BookName.setTextSize(22);
            Author.setTextSize(16);
            BookName.setPadding(15,20,0,5);
            Author.setPadding(15,0,0,0);
            linearLayout.setOrientation(LinearLayout.VERTICAL);
            linearLayout.addView(BookName);
            linearLayout.addView(Author);

        }else{
            linearLayout = (LinearLayout) convertView;
            BookName = (TextView) linearLayout.getChildAt(0);
            Author = (TextView) linearLayout.getChildAt(1);

        }
        BookName.setText(bookList.get(position).title);
        Author.setText(bookList.get(position).author);
        return linearLayout;
    }
}
