package edu.temple.bookshelf;

import android.os.Bundle;

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
public class BookDetailFragment extends Fragment{

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String BookNameParam = "param1";
    private static final String AuthorParam = "param2";
    private static final String imageParam = "param3";

    // TODO: Rename and change types of parameters
    ImageView imageView;
    TextView textViewBook;
    TextView textViewAuthor;
    //static BookDetailFragment fragment;
    Book book;

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
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            this.book = (Book) getArguments().getParcelable("book");
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
        imageView.setImageResource(book.image);
        textViewBook.setText(book.title);
        textViewAuthor.setText(book.author);
        textViewBook.setTextSize(30);
        textViewAuthor.setTextSize(24);
        textViewBook.setGravity(View.TEXT_ALIGNMENT_CENTER);
        textViewAuthor.setGravity(View.TEXT_ALIGNMENT_CENTER);
    }
}