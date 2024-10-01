package com.example.myapplication;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.os.Handler;
import android.os.Looper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.myapplication.Database.CRUD_Business;
import com.example.myapplication.Database.CRUD_Image;
import com.example.myapplication.Database.CRUD_Review;
import com.example.myapplication.Database.CRUD_User;
import com.example.myapplication.Database.DatabaseHelper;

import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link CompetitionRecordsFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class CompetitionRecordsFragment extends Fragment {

    ArrayList<Restaurant> listRestaurant = new ArrayList<>();
    FunctionalLibraries functionalLibraries = new FunctionalLibraries();
    int userId;

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
    int counter2 = 0;
    int counter3 = 0;
    int counter4 = 0;
    int counter5 = 0;


    float businessNo1Rating;
    float businessNo2Rating;
    float businessNo3Rating;
    float businessNo4Rating;
    float businessNo5Rating;
    Handler handler = new Handler();

    DatabaseHelper dbHelper;
    CRUD_Business crudBusiness;
    CRUD_User crudUser;
    CRUD_Image crudImage;
    CRUD_Review crudReview;

    ArrayList<Restaurant> top5 = new ArrayList<>();

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
        initializeDatabase();
//        try {
//            dbHelper = new DatabaseHelper(getContext());
//            CRUD_Business crudBusiness = new CRUD_Business(dbHelper);
//            ArrayList<Restaurant> dbRestaurants = crudBusiness.getAllRestaurants();
//            listRestaurant.addAll(dbRestaurants);
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
        functionalLibraries.binarySort(listRestaurant, "restaurant");
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View fragmentView =  inflater.inflate(R.layout.fragment_competition_records, container, false);

        seekBar1 = (SeekBar) fragmentView.findViewById(R.id.seekBar1);
        showCount1 = fragmentView.findViewById(R.id.top1Count);
        seekBar2 = (SeekBar) fragmentView.findViewById(R.id.seekBar2);
        showCount2 = fragmentView.findViewById(R.id.top2Count);
        seekBar3 = (SeekBar) fragmentView.findViewById(R.id.seekBar3);
        showCount3 = fragmentView.findViewById(R.id.top3Count);
        seekBar4 = (SeekBar) fragmentView.findViewById(R.id.seekBar4);
        showCount4 = fragmentView.findViewById(R.id.top4Count);
        seekBar5 = (SeekBar) fragmentView.findViewById(R.id.seekBar5);
        showCount5 = fragmentView.findViewById(R.id.top5Count);

        seekBar1.setEnabled(false);
        seekBar2.setEnabled(false);
        seekBar3.setEnabled(false);
        seekBar4.setEnabled(false);
        seekBar5.setEnabled(false);


//        Bitmap gygBitmap = BitmapFactory.decodeResource(getResources(), R.drawable.guzmanygomez);
//        Bitmap scaledGygBitmap = Bitmap.createScaledBitmap(gygBitmap, 90, 90, true);
//        Drawable gygDrawable= new BitmapDrawable(getResources(), scaledGygBitmap);
//        seekBar1.setThumb(gygDrawable);
        Bitmap number1Bitmap = crudImage.getBusinessImage(top5.get(0).getId());
        Bitmap number1ScaledBitmap = Bitmap.createScaledBitmap(number1Bitmap, 90, 90, true);
        Drawable number1Drawable = new BitmapDrawable(getResources(), number1ScaledBitmap);
        seekBar1.setThumb(number1Drawable);

        Bitmap number2Bitmap = crudImage.getBusinessImage(top5.get(1).getId());
        Bitmap number2ScaledBitmap = Bitmap.createScaledBitmap(number2Bitmap, 90, 90, true);
        Drawable number2Drawable = new BitmapDrawable(getResources(), number2ScaledBitmap);
        seekBar2.setThumb(number2Drawable);

        Bitmap number3Bitmap = crudImage.getBusinessImage(top5.get(2).getId());
        Bitmap number3ScaledBitmap = Bitmap.createScaledBitmap(number3Bitmap, 90, 90, true);
        Drawable number3Drawable = new BitmapDrawable(getResources(), number3ScaledBitmap);
        seekBar3.setThumb(number3Drawable);

        Bitmap number4Bitmap = crudImage.getBusinessImage(top5.get(3).getId());
        Bitmap number4ScaledBitmap = Bitmap.createScaledBitmap(number4Bitmap, 90, 90, true);
        Drawable number4Drawable = new BitmapDrawable(getResources(), number4ScaledBitmap);
        seekBar4.setThumb(number4Drawable);

        Bitmap number5Bitmap = crudImage.getBusinessImage(top5.get(4).getId());
        Bitmap number5ScaledBitmap = Bitmap.createScaledBitmap(number5Bitmap, 90, 90, true);
        Drawable number5Drawable = new BitmapDrawable(getResources(), number5ScaledBitmap);
        seekBar5.setThumb(number5Drawable);


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
                        showCount1.setText(String.valueOf(counter1)+"%");
                    }
                });

                if(counter1 >= (businessNo1Rating/5)*100){
                    t.cancel();
                }
            }
        };
        Timer s = new Timer();
        TimerTask ss = new TimerTask() {
            @Override
            public void run() {
                counter2++;

                // Update the UI on the main thread
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        seekBar2.setProgress(counter2);
                        showCount2.setText(String.valueOf(counter2)+"%");
                    }
                });

                if(counter2 >= (businessNo2Rating/5)*100){
                    s.cancel();
                }
            }
        };
        Timer no3t = new Timer();
        TimerTask no3tt = new TimerTask() {
            @Override
            public void run() {
                counter3++;

                // Update the UI on the main thread
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        seekBar3.setProgress(counter3);
                        showCount3.setText(String.valueOf(counter3)+"%");
                    }
                });

                if(counter3 >= (businessNo3Rating/5)*100){
                    no3t.cancel();
                }
            }
        };
        Timer no4t = new Timer();
        TimerTask no4tt = new TimerTask() {
            @Override
            public void run() {
                counter4++;

                // Update the UI on the main thread
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        seekBar4.setProgress(counter4);
                        showCount4.setText(String.valueOf(counter4)+"%");
                    }
                });

                if(counter4 >= (businessNo4Rating/5)*100){
                    no4t.cancel();
                }
            }
        };
        Timer no5t = new Timer();
        TimerTask no5tt = new TimerTask() {
            @Override
            public void run() {
                counter5++;

                // Update the UI on the main thread
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        seekBar5.setProgress(counter5);
                        showCount5.setText(String.valueOf(counter5)+"%");
                    }
                });

                if(counter5 >= (businessNo5Rating/5)*100){
                    no5t.cancel();
                }
            }
        };
        t.schedule(tt, 0, 100);
        s.schedule(ss, 0, 100);
        no3t.schedule(no3tt, 0, 100);
        no4t.schedule(no4tt, 0, 100);
        no5t.schedule(no5tt, 0, 100);



        return fragmentView;
    }

    private void initializeDatabase() {

        try {
            dbHelper = new DatabaseHelper(getContext());
            crudBusiness = new CRUD_Business(dbHelper);
            crudUser = new CRUD_User(dbHelper);
            crudImage = new CRUD_Image(dbHelper);
            crudReview = new CRUD_Review(dbHelper);
            ArrayList<Restaurant> dashboardRestaurantsTop5 = crudBusiness.getTop5RatedBusinesses();
            top5.addAll(dashboardRestaurantsTop5);

            businessNo1Rating = crudBusiness.getBusinessStarRating(dashboardRestaurantsTop5.get(0));
            businessNo2Rating = crudBusiness.getBusinessStarRating(dashboardRestaurantsTop5.get(1));
            businessNo3Rating = crudBusiness.getBusinessStarRating(dashboardRestaurantsTop5.get(2));
            businessNo4Rating = crudBusiness.getBusinessStarRating(dashboardRestaurantsTop5.get(3));
            businessNo5Rating = crudBusiness.getBusinessStarRating(dashboardRestaurantsTop5.get(4));

            ArrayList<Restaurant> dbRestaurants = crudBusiness.getAllRestaurants();
            listRestaurant.addAll(dbRestaurants);

            // Notify success on the main thread
            new Handler(Looper.getMainLooper()).post(() ->
                    Toast.makeText(getContext(), "Database initialized successfully!", Toast.LENGTH_LONG).show()
            );
        } catch (Exception e) {
            e.printStackTrace();

            // Notify failure on the main thread
            new Handler(Looper.getMainLooper()).post(() ->
                    Toast.makeText(getContext(), "Database initialization failed!", Toast.LENGTH_LONG).show()
            );
        }

    }

}