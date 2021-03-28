package edu.temple.bookshelf;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.os.Bundle;
import android.widget.Toast;

import java.util.Objects;
public class MainActivity extends AppCompatActivity implements BookListFragment.ItemListFragmentInterface, BookDetailFragment.ItemDetailFragmentInterface{

    BookList list;
    BookDetailFragment bookDetailFragment;
    Boolean container2present;
    BookListFragment bookListFragment;
    //Book book;
    int layoutStateBefore = 1; // 1 for portrait, 2 for landscape
    int[] images;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        images = new int[]{R.drawable.image1, R.drawable.image2, R.drawable.image3, R.drawable.image4, R.drawable.image5, R.drawable.image6, R.drawable.image7, R.drawable.image8, R.drawable.image9, R.drawable.image10,};

        container2present = findViewById(R.id.container2) != null;

        if(savedInstanceState == null){ // if the app first load
            list = new BookList(); //initialize everything
            createBooklists();
            getSupportFragmentManager()
                    .beginTransaction()
                    .add(R.id.container, bookListFragment = BookListFragment.newInstance(list))
                    .commit();
            if(container2present){ //if landscape just make empty detail frag once

                    bookDetailFragment = new BookDetailFragment();

                    getSupportFragmentManager().beginTransaction()
                            .add(R.id.container2, bookDetailFragment)
                            .hide(bookDetailFragment)
                            .commit();

            }
        }else{ // if there  is data saved
            list = savedInstanceState.getParcelable("list");
            bookListFragment = savedInstanceState.getParcelable("listfrag");
            bookDetailFragment = savedInstanceState.getParcelable("detailfrag");
            //Toast.makeText(this, "rotate itemClicked: "+bookDetailFragment.book.author, Toast.LENGTH_SHORT).show();
            if(container2present){ // if landscape,
                    if(bookDetailFragment != null){ //if bookDetail is not null, there is one data saved, it could still be blank
                        if(bookDetailFragment.setRescources){
                            Toast.makeText(this, "rotate itemClicked: "+bookDetailFragment.book.author, Toast.LENGTH_SHORT).show();
                            getSupportFragmentManager().popBackStackImmediate();
                            getSupportFragmentManager().beginTransaction()
                                    .add(R.id.container2,bookDetailFragment)
                                    .commit();
                        }else{

                            bookDetailFragment = new BookDetailFragment();
                            getSupportFragmentManager().beginTransaction()
                                    .add(R.id.container2, bookDetailFragment)
                                    .commit();
                        }

                        //Toast.makeText(this, "rotate itemClicked", Toast.LENGTH_SHORT).show();
                    }else{ //if it is null, make a blank one
                        bookDetailFragment = new BookDetailFragment();

                        getSupportFragmentManager().beginTransaction()
                                .add(R.id.container2, bookDetailFragment)
                                .commit();
                       // Toast.makeText(this, "rotate itemClicked", Toast.LENGTH_SHORT).show();
                    }
            }else{ // if portrait
                // if there is not a detail showing in the container
                if(bookDetailFragment != null){
                     Fragment fragment = getSupportFragmentManager().findFragmentById(R.id.container);

                     if(fragment instanceof BookListFragment) { // if container has list fragment
                         //bookListFragment = (BookListFragment) fragment; // not sure if i should send fragments through savedstate since
                         if(bookDetailFragment.setRescources){
                             getSupportFragmentManager().beginTransaction()
                                     .hide(fragment)
                                     .add(R.id.container, bookDetailFragment)
                                     .addToBackStack(null)
                                     .commit();
                             //Toast.makeText(this, "rotate itemClicked", Toast.LENGTH_SHORT).show();
                         }

                     }
                }



            }

        }

    }

    @Override
    protected void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putParcelable("list",this.list);
        outState.putParcelable("detailfrag", this.bookDetailFragment);
        outState.putParcelable("listfrag", this.bookListFragment);
        //outState.putParcelable("curBook",this.book);
    }

    @Override
    public void itemClicked(int position) {// onclick to manipulate bookdetailFragment
        //Toast.makeText(this, ""+position+ " "+container2present+ " "+(getSupportFragmentManager().findFragmentById(R.id.container2) instanceof BookDetailFragment), Toast.LENGTH_SHORT).show();
        if (!container2present) { // if its portrait, keep making them replacing fragments
            //Toast.makeText(this,  "Line: 117 :"+position,Toast.LENGTH_SHORT).show();
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.container, bookDetailFragment = BookDetailFragment.newInstance(list.getBook(position)))
                    .addToBackStack(null)
                    .commit();
            // when item is clicked and add to back stack is called


        }else{ // when its in landscape , two fragments present
            if(!(getSupportFragmentManager().findFragmentById(R.id.container2) instanceof BookDetailFragment)){ // if there isnt a bookdetail frag yet
                //Toast.makeText(this,  "Line: 148 :"+position,Toast.LENGTH_SHORT).show();
                //System.out.println("==============Line: 82==================");
                getSupportFragmentManager().beginTransaction()
                        .add(R.id.container2, bookDetailFragment = BookDetailFragment.newInstance(list.getBook(position)))
                        .commit();
            }else{ // if there is a bookdetail frag
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