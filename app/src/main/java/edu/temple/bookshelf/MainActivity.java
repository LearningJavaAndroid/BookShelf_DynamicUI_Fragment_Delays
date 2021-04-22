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

public class MainActivity extends AppCompatActivity implements BookListFragment.ItemListFragmentInterface, BookDetailFragment.ItemDetailFragmentInterface{

    BookList list;
    Book book;
    BookDetailFragment bookDetailFragment;
    Boolean container2present;
    BookListFragment bookListFragment;
    Button Search;
    ImageButton playButton;
    ImageButton pauseButton;
    ImageButton stopButton;
    TextView textView;
    SeekBar seekBar;
    Intent serviceIntent;
    AudiobookService.BookProgress progress;
    int finalPer = 0;
    IntentFilter intentFilter;
    private boolean serviceConnected;
    AudiobookService.MediaControlBinder mediaControlBinder = null;

    Handler mProgressHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) { //setting the seekbar
            progress = (AudiobookService.BookProgress) msg.obj;
            int curProg = progress.getProgress();
            Uri media = progress.getBookUri();
            if((curProg >= finalPer) && (curProg % finalPer == 0)){
                seekBar.setProgress(seekBar.getProgress());
            }
            Log.d("mediaControlBinder", "setProgressHandler - " + seekBar.getProgress() + " finalPer: "+finalPer + " TotalDuration: "+book.getDuration() + " RealTime: " + progress.getProgress());
        }
    };

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

    BroadcastReceiver broadcastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {

        }
    };


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Search = findViewById(R.id.SearchButton);
        playButton = findViewById(R.id.playButton);
        pauseButton = findViewById(R.id.pauseButton);
        stopButton = findViewById(R.id.stopButton);
        seekBar = findViewById(R.id.seekBar);
        textView = findViewById(R.id.textView);
        container2present = findViewById(R.id.container2) != null;


//        Search.setPadding(0,0,0,1);
        Search.setBackgroundColor(getResources().getColor(R.color.purple_500));
        Search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent launchIntent = new Intent(MainActivity.this, BookSearchActivity.class);
                startActivityForResult(launchIntent, 111);
            }
        });

        playButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if( mediaControlBinder != null && book != null )
                {
                    textView.setText("Now Playing: " + book.getTitle());
                    double percent = book.getDuration()/1000; // get percent transfer
                    finalPer = (int) Math.ceil(percent);
                    mediaControlBinder.play(book.getID());
                }
            }
        });
        pauseButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if( mediaControlBinder != null )
                    mediaControlBinder.pause();
            }
        });

        stopButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if( mediaControlBinder != null )
                    mediaControlBinder.stop();
            }
        });

        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() { //seekbar listener
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {

            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                if( mediaControlBinder != null ){
//                    int time = seekBar.getProgress();
                    mediaControlBinder.seekTo(seekBar.getProgress());
                    Log.d("mediaControlBinder", "setting progress - " + seekBar.getProgress());
                }
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

        serviceIntent = new Intent(MainActivity.this, AudiobookService.class);
        bindService(serviceIntent, serviceConnection, Context.BIND_AUTO_CREATE );
        startService(serviceIntent);


    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) { //on return from activity/ custom dialog
        super.onActivityResult(requestCode, resultCode, data);
        if(data != null){
            if (requestCode == 111 && resultCode == RESULT_OK) {
                Bundle bundle = data.getBundleExtra("BUNDLE");
                list = bundle.getParcelable("Objects");
                int i = 0;
                while(i < list.sizeBookList()){
                    int id = list.getBook(i).getID();
                    if(id == 1){
                        list.getBook(i).setDuration(765000);
                    }else if(id == 2){
                        list.getBook(i).setDuration(16798);
                    }else if(id == 3){
                        list.getBook(i).setDuration(15213);
                    }else if(id == 4){
                        list.getBook(i).setDuration(30230);
                    }else if(id == 5){
                        list.getBook(i).setDuration(11977);
                    }else if(id == 6){
                        list.getBook(i).setDuration(47204);
                    }else{
                        list.getBook(i).setDuration(61274);
                    }
                    i++;
                }
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
        //outState.putParcelable("curBook",this.book);
    }

    @Override
    public void itemClicked(int position) {// onclick to manipulate bookdetailFragment

        book = list.getBook(position);
        seekBar.setMax(book.getDuration());

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
    protected void onStart() {
        super.onStart();
//        registerReceiver(broadcastReceiver, intentFilter);
    }

    @Override
    protected void onStop() {
        super.onStop();
//        unregisterReceiver(broadcastReceiver);
    }

    @Override
    public void onDestroy() {
        stopService(serviceIntent);
        unbindService(serviceConnection);

        super.onDestroy();
    }
}