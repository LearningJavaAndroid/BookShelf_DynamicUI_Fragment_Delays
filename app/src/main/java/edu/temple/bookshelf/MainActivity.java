package edu.temple.bookshelf;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

public class MainActivity extends AppCompatActivity {

    BookList list;
    BookListFragment listFragment;
    BookDetailFragment bookDetailFragment;

    int column = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        list = new BookList();
        createBooklists();
        listFragment = BookListFragment.newInstance(list);

        getSupportFragmentManager()
                .beginTransaction()
                .add(R.id.container1, listFragment) // put arguements instead the new ImageFragment()
                .add(R.id.container1, bookDetailFragment)
                //add the next fragment here
                .commit();


    }

    public void createBooklists() {

        //all arrays are 10 in size/length
        String[] BookName = getResources().getStringArray(R.array.Book);
        String[] BookAuthor = getResources().getStringArray(R.array.Author);
        int[] images = new int[]{R.drawable.image1, R.drawable.image2, R.drawable.image3, R.drawable.image4, R.drawable.image5, R.drawable.image6, R.drawable.image7, R.drawable.image8, R.drawable.image9, R.drawable.image10,};

        int i = 0;
        while (i < getResources().getStringArray(R.array.Author).length) {
            Book book = new Book(BookName[i], BookAuthor[i], images[i]);
            list.add(book);
            i++;
        }

    }

}