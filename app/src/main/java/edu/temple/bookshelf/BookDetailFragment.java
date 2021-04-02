package edu.temple.bookshelf;

import android.app.Activity;
import android.content.Context;
import android.content.res.Configuration;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;

import android.os.Parcel;
import android.os.Parcelable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import org.w3c.dom.Text;

import edu.temple.bookshelf.R;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link BookDetailFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class BookDetailFragment extends Fragment implements Parcelable {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String BookNameParam = "param1";
    private static final String AuthorParam = "param2";
    private static final String imageParam = "param3";

    // TODO: Rename and change types of parameters
    ImageView imageView ;
    TextView textViewBook;
    TextView textViewAuthor ;
    Book book ;
    boolean setRescources = false;
    ItemDetailFragmentInterface parentActivity;

    public BookDetailFragment() {
        // Required empty public constructor
    }

    protected BookDetailFragment(Parcel in) {
        book = in.readParcelable(Book.class.getClassLoader());
        setRescources = in.readByte() != 0;
    }

    public static final Creator<BookDetailFragment> CREATOR = new Creator<BookDetailFragment>() {
        @Override
        public BookDetailFragment createFromParcel(Parcel in) {
            return new BookDetailFragment(in);
        }

        @Override
        public BookDetailFragment[] newArray(int size) {
            return new BookDetailFragment[size];
        }
    };

    public static BookDetailFragment newInstance(Book book) {
        BookDetailFragment fragment = new BookDetailFragment();
        Bundle args = new Bundle();
        args.putBoolean("yes",true);
        args.putParcelable("newbook", book);
        fragment.setArguments(args);
        return fragment;
    }
    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        if(context instanceof BookDetailFragment.ItemDetailFragmentInterface){ //check if activity has the interface implement
            parentActivity = (BookDetailFragment.ItemDetailFragmentInterface) context; //storing a reference to parent here in memory
        }else{ // if not implemented, Tell them to in error
            throw new RuntimeException("Please Implement the Item List Interface before attaching this fragment");
        }
    }
    @Override
    public void onDetach() { // on detach is used to stop storing the context parent,
        // to stop memory leaks required
        super.onDetach();
        parentActivity = null;
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) { //change saved state so that it saves the things you want it to
        super.onSaveInstanceState(outState);
        outState.putParcelable("savebook", book);
        outState.putBoolean("yes", setRescources);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if(savedInstanceState == null){ // if there is no saved data
            if (getArguments() != null) {
                this.book =  getArguments().getParcelable("newbook");
                this.setRescources = getArguments().getBoolean("yes");
            }
        }else { //using saved instance state
            if (getArguments() != null) {
                this.book = savedInstanceState.getParcelable("savebook");
                this.setRescources = savedInstanceState.getBoolean("yes");
            }
        }

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View layout = inflater.inflate(R.layout.fragment_book_detail, container, false);
        this.imageView = layout.findViewById(R.id.imageView);
        this.textViewBook = layout.findViewById(R.id.Book);
        this.textViewAuthor = layout.findViewById(R.id.author);


        if(this.book != null){
            displayBook(book);
        }
        return layout;
    }

    public BookDetailFragment displayBook(Book Book){
        //this.setRescources = true;
        this.book = Book;
        textViewBook.setGravity(View.TEXT_ALIGNMENT_CENTER);
        textViewAuthor.setGravity(View.TEXT_ALIGNMENT_CENTER);
        imageView.setImageResource(Book.getImage());
        textViewBook.setText(Book.getTitle());
        textViewAuthor.setText(Book.getAuthor());
        textViewBook.setTextSize(30);
        textViewAuthor.setTextSize(24);
        textViewBook.setTextSize(25);
        textViewAuthor.setTextSize(20);
        textViewAuthor.setPadding(0,0,0,0);
        textViewBook.setPadding(0,0,0,0);
        this.setRescources = true;
        return this;


    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeParcelable(book, flags);
        dest.writeByte((byte) (setRescources ? 1 : 0));
    }


    interface ItemDetailFragmentInterface{

    }
}