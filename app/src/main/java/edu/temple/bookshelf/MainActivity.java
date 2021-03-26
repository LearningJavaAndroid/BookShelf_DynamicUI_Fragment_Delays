package edu.temple.bookshelf;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

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

        if(list ==null){
            list = new BookList();
            createBooklists();
        }

        if (!(getSupportFragmentManager().findFragmentById(R.id.container) instanceof BookListFragment)) { //do we have list frag in container 1 yet
            System.out.println("==============Line: 34==================");
            getSupportFragmentManager().popBackStackImmediate();
            getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.container, BookListFragment.newInstance(list))
                    .commit();
        }


        if(container2present){ //if landscape just make empty detail frag once
            System.out.println("==============Line: 42==================");
            if(!(getSupportFragmentManager().findFragmentById(R.id.container2) instanceof BookDetailFragment)){ // if there isnt one detail frag created
                System.out.println("==============Line: 44==================");
                bookDetailFragment = new BookDetailFragment();
                getSupportFragmentManager().beginTransaction()
                        .add(R.id.container2, bookDetailFragment)
                        .commit();
            }

        }
        System.out.println("===============line:51===================");

    }

    @Override
    public void itemClicked(int position) {// onclick for container list one
        if (!container2present) { // if its not in landscape, keep making them replacing fragments
            //if(!(getSupportFragmentManager().findFragmentById(R.id.container2) instanceof BookDetailFragment)){
                System.out.println("==============Line: 75==================");
                getSupportFragmentManager().beginTransaction()
                        .replace(R.id.container, bookDetailFragment.newInstance(list.getBook(position)))
                        .addToBackStack("detail")
                        .commit();
            //}

        }else{ // when its in landscape , two fragments present
            if(!(getSupportFragmentManager().findFragmentById(R.id.container2) instanceof BookDetailFragment)){ // if there isnt a bookdetail frag yet
                System.out.println("==============Line: 82==================");
                bookDetailFragment = new BookDetailFragment();
                //book = new Book("Book Title","Book Author",R.drawable.ic_launcher_background);
                getSupportFragmentManager().beginTransaction()
                        .add(R.id.container2, bookDetailFragment.newInstance(list.getBook(position)))
                        .commit();
            }else{
                bookDetailFragment.displayBook(list.getBook(position));
            }

        }

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
}