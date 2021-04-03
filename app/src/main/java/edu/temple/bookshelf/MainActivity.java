package edu.temple.bookshelf;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONObject;

import java.net.MalformedURLException;
import java.util.ArrayList;
import java.util.Objects;
public class MainActivity extends AppCompatActivity implements BookListFragment.ItemListFragmentInterface, BookDetailFragment.ItemDetailFragmentInterface{

    BookList list;
    BookDetailFragment bookDetailFragment;
    Boolean container2present;
    BookListFragment bookListFragment;
    TextView Search;
    int layoutStateBefore = 1; // 1 for portrait, 2 for landscape
    int[] images;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Search = findViewById(R.id.SearchButton);
        container2present = findViewById(R.id.container2) != null;
        //images = new int[]{R.drawable.image1, R.drawable.image2, R.drawable.image3, R.drawable.image4, R.drawable.image5, R.drawable.image6, R.drawable.image7, R.drawable.image8, R.drawable.image9, R.drawable.image10,};

        Search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent launchIntent = new Intent(MainActivity.this, BookSearchActivity.class);
                startActivityForResult(launchIntent, 111);
            }
        });

        if(savedInstanceState == null){ // if the app first load
            list = new BookList(); //initialize everything
            //createBooklists();
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
            Log.d("log_tag", "container2present = " + container2present );
            if(container2present){ // if landscape,
                    if(bookDetailFragment != null){ //if bookDetail is not null, there is one data saved, it could still be blank
                        if(bookDetailFragment.setRescources){
                            Log.d("log_tag", "line 61:");
                            Toast.makeText(this, "rotate itemClicked: "+bookDetailFragment.book.author, Toast.LENGTH_SHORT).show();
                            getSupportFragmentManager().popBackStackImmediate();
                            getSupportFragmentManager().beginTransaction()
                                    .replace(R.id.container2,bookDetailFragment)
                                    .commit();

                            Book original_book = bookDetailFragment.book;
                            new Handler(Looper.getMainLooper()).postDelayed(new Runnable() {
                                @Override
                                public void run() {
                                    Log.d("log_tag", "Portrait4: " + original_book.title );
                                    bookDetailFragment.displayBook(original_book);
                                }
                            }, 100);

                        }else{
                            Log.d("log_tag", "line 77:");
                            bookDetailFragment = new BookDetailFragment();
                            getSupportFragmentManager().beginTransaction()
                                    .add(R.id.container2, bookDetailFragment)
                                    .commit();
                        }

                    }else{ //if it is null, make a blank one
                        bookDetailFragment = new BookDetailFragment();
                        Log.d("log_tag", "line 87:");
                        getSupportFragmentManager().beginTransaction()
                                .add(R.id.container2, bookDetailFragment)
                                .commit();
                    }
            }else{ // if portrait
                //Log.d("log_tag", "Portrait: " + bookDetailFragment.book.title );

                // if there is not a detail showing in the container
                if(bookDetailFragment != null){
                     Fragment fragment = getSupportFragmentManager().findFragmentById(R.id.container);
                    //Log.d("log_tag", "Portrait1: " + bookDetailFragment.book.title );
                    Log.d("log_tag", "line 100:");
                     if(fragment instanceof BookListFragment) { // if container has list fragment
                         //Log.d("log_tag", "Portrait2: " + bookDetailFragment.book.title );
                         if(bookDetailFragment.setRescources){
                             Log.d("log_tag", "line 106:");
                             getSupportFragmentManager().popBackStackImmediate();
                             getSupportFragmentManager().beginTransaction()
                                     .replace(R.id.container, bookDetailFragment)
                                     .addToBackStack(null)
                                     .commit();
                             Log.d("log_tag", "Portrait3: " + bookDetailFragment.book.title );

                             Book original_book = bookDetailFragment.book;
                             new Handler(Looper.getMainLooper()).postDelayed(new Runnable() {
                                 @Override
                                 public void run() {
                                     Log.d("log_tag", "Portrait4: " + original_book.title );
                                     bookDetailFragment.displayBook(original_book);
                                 }
                             }, 50);
                         }else{
                             Log.d("log_tag", "line 124:");
                             Toast.makeText(this, "line: 123", Toast.LENGTH_SHORT).show();
                             getSupportFragmentManager().beginTransaction()
                                     .replace(R.id.container, bookListFragment)
                                     .commit();
                         }

                     }
                }



            }

        }

    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) { //on return from activity/ custom dialog
        super.onActivityResult(requestCode, resultCode, data);
        if(data != null){
            if (requestCode == 111 && resultCode == RESULT_OK) {
                Bundle bundle = data.getBundleExtra("BUNDLE");
                list = bundle.getParcelable("Objects");

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
         if (!container2present) { // if its portrait, keep making them replacing fragments
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.container, bookDetailFragment = BookDetailFragment.newInstance(list.getBook(position)))
                    .addToBackStack(null)
                    .commit();
            // when item is clicked and add to back stack is called

        }else{ // when its in landscape , two fragments present
            Fragment fragment = (BookDetailFragment)(getSupportFragmentManager().findFragmentById(R.id.container2));

            Log.d("log_tag", "Landscape Item click1");

            if((!bookDetailFragment.setRescources)){ // if there isnt a bookdetail frag yet
                //System.out.println("==============Line: 82==================");
                Log.d("log_tag", "Landscape Item click2");
                getSupportFragmentManager().beginTransaction()
                        .replace(R.id.container2, bookDetailFragment = BookDetailFragment.newInstance(list.getBook(position)))
                        .commit();
            }else{ // if there is a bookdetail frag
                Log.d("log_tag", "Landscape Item click3");
                if(fragment instanceof BookDetailFragment){
                    //bookDetailFragment = (BookDetailFragment) fragment;
                    Log.d("log_tag", "Landscape Item click4");
                    getSupportFragmentManager().beginTransaction()
                            .replace(R.id.container2, bookDetailFragment = bookDetailFragment.displayBook(list.getBook(position)))
                            .commitNow();
                }

                Toast.makeText(this, ""+bookDetailFragment.book.title, Toast.LENGTH_SHORT).show();
            }

        }

    }


}