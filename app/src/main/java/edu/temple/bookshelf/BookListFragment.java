package edu.temple.bookshelf;

import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

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
public class BookListFragment extends Fragment {


    private int mColumnCount = 1;
    private BookList bookList;
    ItemListFragmentInterface parentActivity;

    /**
     * Mandatory empty constructor for the fragment manager to instantiate the
     * fragment (e.g. upon screen orientation changes).
     */
    public BookListFragment() {

    }

    // TODO: Customize parameter initialization
    @SuppressWarnings("unused")
    public static BookListFragment newInstance(BookList bookList) {
        BookListFragment fragment = new BookListFragment();
        Bundle args = new Bundle();
        args.putParcelableArrayList("list", bookList.bookList);
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
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            this.bookList.bookList =  getArguments().getParcelableArrayList("list");
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        ListView view = (ListView) inflater.inflate(R.layout.fragment_book_list_view, container, false);

        // Set the adapter
        if(getActivity() != null){
            view.setAdapter(new BookAdapter(getActivity(), bookList));
        }

        view.setOnItemClickListener(new AdapterView.OnItemClickListener(){
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id){
                parentActivity.itemClicked(position);
            }
        });

        return view;
    }
    interface ItemListFragmentInterface{
        public void itemClicked(int position);
    }


}