package edu.temple.bookshelf;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.annotation.SuppressLint;
import android.app.ActionBar;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.ServiceConnection;
import android.content.res.Resources;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Looper;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONObject;

import java.net.MalformedURLException;
import java.util.ArrayList;
import java.util.Objects;

import edu.temple.audiobookplayer.AudiobookService;

public class MainActivity extends AppCompatActivity implements BookListFragment.ItemListFragmentInterface, BookDetailFragment.ItemDetailFragmentInterface, ControlFragment.ControlInterface{

    BookList list;
    private Book selectedbook, playingBook;

    private ControlFragment controlFragment;
    BookDetailFragment bookDetailFragment;
    BookListFragment bookListFragment;

    Button Search;

    private final String KEY_SELECTED_BOOK = "selectedBook", KEY_PLAYING_BOOK = "playingBook";
    private final String KEY_BOOKLIST = "searchedbook";

    Intent serviceIntent;
    AudiobookService.BookProgress progress;
    AudiobookService.MediaControlBinder mediaControlBinder = null;

    private boolean serviceConnected;
    Boolean container2present;



    Handler mProgressHandler = new Handler(Looper.getMainLooper(), (message) ->{

        if(message.obj != null && playingBook != null){
            controlFragment.updateProgress((int) (((float) ((AudiobookService.BookProgress) message.obj).getProgress()/ playingBook.getDuration()*100)));
            controlFragment.setNowPlaying("Now Playing: {playingBook.getTitle()}");
        }

        return true;
    });

    ServiceConnection serviceConnection = new ServiceConnection() { //set service connection
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            mediaControlBinder = (AudiobookService.MediaControlBinder) service;
            Log.d("mediaControlBinder", "onServiceConnected");
            mediaControlBinder.setProgressHandler(mProgressHandler);
            serviceConnected = true;


        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            serviceConnected = false;
            Log.d("mediaControlBinder", "onServiceDisconnected");
        }
    };


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Search = findViewById(R.id.SearchButton);

        container2present = findViewById(R.id.container2) != null;

        serviceIntent = new Intent(this, AudiobookService.class);
        bindService(serviceIntent, serviceConnection, BIND_AUTO_CREATE);

        Search.setBackgroundColor(getResources().getColor(R.color.purple_500));
        Search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent launchIntent = new Intent(MainActivity.this, BookSearchActivity.class);
                startActivityForResult(launchIntent, 111);
            }
        });

        if(savedInstanceState == null){ // if the app first load
            list = new BookList(); //initialize everything
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
            playingBook = savedInstanceState.getParcelable(KEY_PLAYING_BOOK);
            selectedbook = savedInstanceState.getParcelable(KEY_SELECTED_BOOK);
            Log.d("log_tag", "container2present = " + container2present );
            if(container2present){ // if landscape,
                    if(bookDetailFragment != null){ //if bookDetail is not null, there is one data saved, it could still be blank
                        if(bookDetailFragment.setRescources){
                            Log.d("log_tag", "line 61:");
                            //Toast.makeText(this, "rotate itemClicked: "+bookDetailFragment.book.getAuthor(), Toast.LENGTH_SHORT).show();
                            getSupportFragmentManager().popBackStackImmediate();
                            getSupportFragmentManager().beginTransaction()
                                    .replace(R.id.container2,bookDetailFragment)
                                    .commit();

                            Book original_book = bookDetailFragment.book;
                            new Handler(Looper.getMainLooper()).postDelayed(new Runnable() {
                                @Override
                                public void run() {
                                    Log.d("log_tag", "Portrait4: " + original_book.getTitle() );
                                    bookDetailFragment.displayBook(original_book);
                                }
                            }, 50);

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
                             Log.d("log_tag", "Portrait3: " + bookDetailFragment.book.getTitle() );

                             Book original_book = bookDetailFragment.book;
                             new Handler(Looper.getMainLooper()).postDelayed(new Runnable() {
                                 @Override
                                 public void run() {
                                     Log.d("log_tag", "Portrait4: " + original_book.getTitle() );
                                     bookDetailFragment.displayBook(original_book);
                                 }
                             }, 50);
                         }else{
                             Log.d("log_tag", "line 124:");
                             //Toast.makeText(this, "line: 123", Toast.LENGTH_SHORT).show();
                             getSupportFragmentManager().beginTransaction()
                                     .replace(R.id.container, bookListFragment)
                                     .commit();
                         }

                     }
                }



            }

        }
        View decorView = getWindow().getDecorView();
        // Hide the status bar.
        int uiOptions = View.SYSTEM_UI_FLAG_FULLSCREEN;
        decorView.setSystemUiVisibility(uiOptions);
        // Remember that you should never show the action bar if the
        // status bar is hidden, so hide that too if necessary.
//        ActionBar actionBar = getActionBar();
//        actionBar.hide();

//        serviceIntent = new Intent(MainActivity.this, AudiobookService.class);
//        bindService(serviceIntent, serviceConnection, Context.BIND_AUTO_CREATE );
//        startService(serviceIntent);


    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) { //on return from activity/ custom dialog
        super.onActivityResult(requestCode, resultCode, data);
        if(data != null){
            if (requestCode == 111 && resultCode == RESULT_OK) {
                Bundle bundle = data.getBundleExtra("BUNDLE");
                list = bundle.getParcelable("Objects");
                Log.d("log_tag", "OnactivityResult: 158: size: "+list.sizeBookList());
                getSupportFragmentManager()
                        .beginTransaction()
                        .replace(R.id.container, bookListFragment = BookListFragment.newInstance(list))
                        .commit();

            }
        }


    }

    @Override
    protected void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putParcelable("list",this.list);
        outState.putParcelable("detailfrag", this.bookDetailFragment);
        outState.putParcelable("listfrag", this.bookListFragment);
        outState.putParcelable(KEY_PLAYING_BOOK,this.playingBook);
        outState.putParcelable(KEY_SELECTED_BOOK, this.selectedbook);
    }

    @Override
    public void itemClicked(int position) {// onclick to manipulate bookdetailFragment
        selectedbook = list.getBook(position);

         if (!container2present) { // if its portrait, keep making them replacing fragments
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.container, bookDetailFragment = BookDetailFragment.newInstance(list.getBook(position)))
                    .addToBackStack(null)
                    .commit();



        }else{ // when its in landscape , two fragments present
            Fragment fragment = (BookDetailFragment)(getSupportFragmentManager().findFragmentById(R.id.container2));

            Log.d("log_tag", "Landscape Item click1");

            if((!bookDetailFragment.setRescources)){ // if there isnt a bookdetail frag yet
                //System.out.println("==============Line: 82==================");
                Log.d("log_tag", "Landscape Item click2");
                getSupportFragmentManager().beginTransaction()
                        .replace(R.id.container2, bookDetailFragment = BookDetailFragment.newInstance(list.getBook(position)))
                        .commit();
            }else{ // if there is a bookdetail frag in container 2 already, then just do display book
                Log.d("log_tag", "Landscape Item click3");
                if(fragment instanceof BookDetailFragment){
                    //bookDetailFragment = (BookDetailFragment) fragment;
                    Log.d("log_tag", "Landscape Item click4");
//                    getSupportFragmentManager().beginTransaction()
//                            .replace(R.id.container2, bookDetailFragment = bookDetailFragment.displayBook(list.getBook(position)))
//                            .commitNow();
                    bookDetailFragment = bookDetailFragment.displayBook(list.getBook(position));
                }

                Toast.makeText(this, ""+bookDetailFragment.book.getTitle(), Toast.LENGTH_SHORT).show();
            }

        }

    }


    @Override
    public void play() {
        if(selectedbook != null){
            playingBook = selectedbook;
            controlFragment.setNowPlaying("Now Playing: {playingBook.getTitle()}");
            if(serviceConnected){
                mediaControlBinder.play(selectedbook.getID());
            }

            startService(serviceIntent);
        }
    }

    @Override
    public void pause() {
        if(serviceConnected){
            mediaControlBinder.pause();
        }
    }

    @Override
    public void stop() {
        if(serviceConnected){
            mediaControlBinder.stop();

            stopService(serviceIntent);
        }
    }

    @Override
    public void changePosition(int progress) {
        if(serviceConnected){
            mediaControlBinder.seekTo((int) ((progress/100f)*playingBook.getDuration()));
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unbindService(serviceConnection);
    }
}