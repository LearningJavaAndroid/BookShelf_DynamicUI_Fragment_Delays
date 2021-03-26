package edu.temple.bookshelf;

import android.app.Activity;
import android.content.Context;
import android.content.res.Configuration;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;

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
public class BookDetailFragment extends Fragment {

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
    ItemDetailFragmentInterface parentActivity;

    public BookDetailFragment() {
        // Required empty public constructor
    }

    public static BookDetailFragment newInstance(Book book) {
        BookDetailFragment fragment = new BookDetailFragment();
        Bundle args = new Bundle();
        args.putParcelable("book", book);
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
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

            if (getArguments() != null) {
                this.book = getArguments().getParcelable("book");
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

    public void displayBook(Book book){

        textViewBook.setGravity(View.TEXT_ALIGNMENT_CENTER);
        textViewAuthor.setGravity(View.TEXT_ALIGNMENT_CENTER);
        imageView.setImageResource(book.getImage());
        textViewBook.setText(book.getTitle());
        textViewAuthor.setText(book.getAuthor());
        textViewBook.setTextSize(30);
        textViewAuthor.setTextSize(24);
        textViewBook.setTextSize(25);
        textViewAuthor.setTextSize(20);
        textViewAuthor.setPadding(0,0,0,0);
        textViewBook.setPadding(0,0,0,0);



    }

    interface ItemDetailFragmentInterface{

    }
}