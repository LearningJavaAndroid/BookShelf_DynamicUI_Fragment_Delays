package edu.temple.bookshelf;

import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.SeekBar;
import android.widget.TextView;


public class ControlFragment extends Fragment {

    private ControlInterface parentActivity;
    TextView textView;
    SeekBar seekBar;


    public ControlFragment() {
        // Required empty public constructor
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);

        if(context instanceof ControlInterface){
            parentActivity = (ControlInterface) context;

        }else{
            throw new RuntimeException("Please Implement ControlFragment.ControlInterface");
        }
    }

    public static ControlFragment newInstance() {
        ControlFragment fragment = new ControlFragment();
        return fragment;
    }

//    @Override
//    public void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        if (getArguments() != null) {
//
//        }
//        if(savedInstanceState != null){
//
//        }
//    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View l =  inflater.inflate(R.layout.fragment_control, container, false);

        textView = l.findViewById(R.id.textView);
        seekBar = l.findViewById(R.id.seekBar);
        l.findViewById(R.id.playButton).setOnClickListener((view)->{
            parentActivity.play();
        });
        l.findViewById(R.id.pauseButton).setOnClickListener((view)->{
            parentActivity.pause();
        });
        l.findViewById(R.id.stopButton).setOnClickListener((view)->{
            parentActivity.stop();
        });

        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                if(fromUser){
                    parentActivity.changePosition(progress);
                }
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
        return l;
    }

    public void setNowPlaying(String title){
        textView.setText(title);
    }
    public void updateProgress(int progress){
        seekBar.setProgress(progress);
    }
    interface ControlInterface{
        void play();
        void pause();
        void stop();
        void changePosition(int progress);
    }
}