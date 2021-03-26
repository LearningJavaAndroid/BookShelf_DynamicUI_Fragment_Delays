package edu.temple.bookshelf;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.os.Bundle;
import android.widget.ListView;

public class MainActivity extends AppCompatActivity implements BookListFragment.ItemListFragmentInterface, BookDetailFragment.ItemDetailFragmentInterface{

    BookList list;
    BookDetailFragment bookDetailFragment;
    Boolean container2present;
    Book book;
    int[] images;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        images = new int[]{R.drawable.image1, R.drawable.image2, R.drawable.image3, R.drawable.image4, R.drawable.image5, R.drawable.image6, R.drawable.image7, R.drawable.image8, R.drawable.image9, R.drawable.image10,};

        container2present = findViewById(R.id.container2) != null;
        System.out.println("line: 30");
        list = new BookList();
        createBooklists();

        if(!(getSupportFragmentManager().findFragmentById(R.id.container1) instanceof BookListFragment)) {
            System.out.println("line: 35");
            getSupportFragmentManager()
                    .beginTransaction()
                    .add(R.id.container1, BookListFragment.newInstance(list))
                    .commit();
        }
        if(container2present){ //if landscape just make empty detail frag once
            if(!(getSupportFragmentManager().findFragmentById(R.id.container2) instanceof BookDetailFragment)){
                bookDetailFragment = new BookDetailFragment();
                book = new Book("Book Title","Book Author",R.drawable.ic_launcher_background);
                getSupportFragmentManager().beginTransaction()
                        .add(R.id.container2, bookDetailFragment)
                        .commit();
            }
        }
        System.out.println("line:51");

    }

    public void createBooklists() { // initialize data

        //all arrays are 10 in size/length
        //BookList list = new BookList();
        String[] BookName = getResources().getStringArray(R.array.Book);
        String[] BookAuthor = getResources().getStringArray(R.array.Author);

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
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.container1, BookDetailFragment.newInstance(list.getBook(position)))
                    .addToBackStack(null)
                    .commit();
        }else{ // when its in landscape , two fragments present
            if(!(getSupportFragmentManager().findFragmentById(R.id.container2) instanceof BookDetailFragment)){ // if there isnt a bookdetial frag yet
                bookDetailFragment = new BookDetailFragment();
                book = new Book("Book Title","Book Author",R.drawable.ic_launcher_background);
                getSupportFragmentManager().beginTransaction()
                        .add(R.id.container2, BookDetailFragment.newInstance(book))
                        .commit();
            }

            //detailfrag is null
            bookDetailFragment.displayBook(list.getBook(position));
        }

    }
}