package edu.temple.bookshelf;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

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
    private String BookName;
    private String Author;
    private int Images;
    ImageView imageView;
    TextView textViewBook;
    TextView textViewAuthor;
    public BookDetailFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment BookDetailFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static BookDetailFragment newInstance(Book book) {
        BookDetailFragment fragment = new BookDetailFragment();
        Bundle args = new Bundle();
        args.putString(BookNameParam, book.title);
        args.putString(AuthorParam, book.author);
        args.putInt(imageParam,book.image);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            BookName = getArguments().getString(BookNameParam);
            Author = getArguments().getString(AuthorParam);
            Images = getArguments().getInt(imageParam);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View layout = inflater.inflate(R.layout.fragment_book_detail, container, false);
        ImageView imageView = layout.findViewById(R.id.imageView);
        TextView textViewBook = layout.findViewById(R.id.Book);
        TextView textViewAuthor = layout.findViewById(R.id.author);

        return layout;
    }

    public void displayBook(Book book){
        imageView.setImageResource(book.image);
        textViewBook.setText(book.title);
        textViewAuthor.setText(book.author);
    }
}