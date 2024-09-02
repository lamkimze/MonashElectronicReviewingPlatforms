package com.example.myapplication;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.SeekBar;
import android.widget.TextView;

import java.util.Timer;
import java.util.TimerTask;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link CompetitionRecordsFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class CompetitionRecordsFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    TextView showCount1, showCount2, showCount3, showCount4, showCount5;
    SeekBar seekBar1, seekBar2, seekBar3, seekBar4, seekBar5;
    int counter1 = 0;
    Handler handler = new Handler();

    public CompetitionRecordsFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment CompetitionRecordsFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static CompetitionRecordsFragment newInstance(String param1, String param2) {
        CompetitionRecordsFragment fragment = new CompetitionRecordsFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }


    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View fragmentView =  inflater.inflate(R.layout.fragment_competition_records, container, false);

        seekBar1 = (SeekBar) fragmentView.findViewById(R.id.seekBar1);
        showCount1 = fragmentView.findViewById(R.id.top1Count);

        Bitmap gygBitmap = BitmapFactory.decodeResource(getResources(), R.drawable.guzmanygomez);
        Bitmap scaledGygBitmap = Bitmap.createScaledBitmap(gygBitmap, 90, 90, true);
        Drawable gygDrawable= new BitmapDrawable(getResources(), scaledGygBitmap);
        seekBar1.setThumb(gygDrawable);


        Timer t = new Timer();
        TimerTask tt = new TimerTask() {
            @Override
            public void run() {
                counter1++;

                // Update the UI on the main thread
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        seekBar1.setProgress(counter1);
                        showCount1.setText(String.valueOf(counter1));
                    }
                });

                if(counter1 == 100){
                    t.cancel();
                }
            }
        };
        t.schedule(tt, 0, 100);

        return fragmentView;
    }

}