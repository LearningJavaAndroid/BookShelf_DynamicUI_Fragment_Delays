package edu.temple.bookshelf;

import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.os.Parcel;
import android.os.Parcelable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import java.util.ArrayList;

import edu.temple.bookshelf.R;

/**
 * A fragment representing a list of Items.
 */
public class BookListFragment extends Fragment implements Parcelable{


    private int mColumnCount = 1;
    private BookList bookList = new BookList();
    ItemListFragmentInterface parentActivity;

    /**
     * Mandatory empty constructor for the fragment manager to instantiate the
     * fragment (e.g. upon screen orientation changes).
     */
    public BookListFragment() {

    }

    protected BookListFragment(Parcel in) {
        mColumnCount = in.readInt();
        bookList = in.readParcelable(BookList.class.getClassLoader());
    }

    public static final Creator<BookListFragment> CREATOR = new Creator<BookListFragment>() {
        @Override
        public BookListFragment createFromParcel(Parcel in) {
            return new BookListFragment(in);
        }

        @Override
        public BookListFragment[] newArray(int size) {
            return new BookListFragment[size];
        }
    };

    // TODO: Customize parameter initialization
    @SuppressWarnings("unused")
    public static BookListFragment newInstance(BookList booklist) {
        BookListFragment fragment = new BookListFragment();
        Bundle args = new Bundle();
        args.putParcelableArrayList("list", booklist.bookList);
        fragment.setArguments(args);
        return fragment;
    }
    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        if(context instanceof ItemListFragmentInterface){ //check if activity has the interface implement
            parentActivity = (ItemListFragmentInterface) context; //storing a reference to parent here in memory
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
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putParcelableArrayList("list", bookList.bookList);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if((savedInstanceState == null) && (getArguments() != null)){
                this.bookList.bookList =  getArguments().getParcelableArrayList("list");
        }else{
            if(getArguments() != null){
                this.bookList.bookList = savedInstanceState.getParcelableArrayList("list");
            }
        }



    }
    public void displayList(BookList list){

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        ListView view = (ListView) inflater.inflate(R.layout.fragment_book_list_view, container, false);

        // Set the adapter

        view.setAdapter(new BookAdapter(getActivity(), bookList));


        view.setOnItemClickListener(new AdapterView.OnItemClickListener(){
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id){
                parentActivity.itemClicked(position);
            }
        });

        return view;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(mColumnCount);
        dest.writeParcelable(bookList, flags);
    }

    interface ItemListFragmentInterface{
        public void itemClicked(int position);
    }


}