package edu.temple.bookshelf;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.os.Bundle;
import android.widget.ListView;

public class MainActivity extends AppCompatActivity implements BookListFragment.ItemListFragmentInterface {

    BookList list;
    BookListFragment listFragment;
    BookDetailFragment bookDetailFragment;
    Boolean container2present;
    FragmentManager fragmentManager;
    Book book;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        fragmentManager = getSupportFragmentManager();
        container2present = findViewById(R.id.container2) != null;

        if(savedInstanceState == null) { // first initialize
            list = new BookList();
            createBooklists();
            fragmentManager.beginTransaction()
                    .replace(R.id.container1, listFragment = BookListFragment.newInstance(list))
                    .commit();
        }


        if(container2present){ //if landscape just make empty detail frag once
            book = new Book("Book Title","Book Author",R.drawable.ic_launcher_background);
            fragmentManager.beginTransaction()
                    .replace(R.id.container2, bookDetailFragment = BookDetailFragment.newInstance(book))
                    .commit();
        }


    }

    public void createBooklists() { // initialize data

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

    @Override
    public void itemClicked(int position) {// onclick for container list one
        if (!container2present) { // if its not in landscape, keep making then replacing fragments
            fragmentManager.beginTransaction()
                    .replace(R.id.container1, bookDetailFragment = BookDetailFragment.newInstance(list.getBook(position)))
                    .commit();
        }else{ // when its in landscape , two fragments present
            System.out.println(position);
            bookDetailFragment.displayBook(list.getBook(position));
        }

    }
}