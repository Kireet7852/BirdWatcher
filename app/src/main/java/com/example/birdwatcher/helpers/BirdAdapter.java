package com.example.birdwatcher.helpers;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.example.birdwatcher.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

import org.checkerframework.checker.nullness.qual.NonNull;

import java.util.ArrayList;
import java.util.List;

public class BirdAdapter extends RecyclerView.Adapter<BirdAdapter.BirdViewHolder> {

    private DatabaseReference mDatabase;
    private List<Bird> mBirdList;

    public BirdAdapter(DatabaseReference database) {
        mDatabase = database;
        mBirdList = new ArrayList<>();


        //new data has been add
        mDatabase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                mBirdList.clear();

                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    Bird bird = dataSnapshot.getValue(Bird.class);
                    mBirdList.add(bird);
                }

                notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.e("BirdAdapter", "Error fetching data", error.toException());
            }
        });
    }

    @NonNull
    @Override
    public BirdViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.save_data_item, parent, false);
        return new BirdViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull BirdViewHolder holder, int position) {
        Bird bird = mBirdList.get(position);

        holder.mSpeciesTextView.setText(bird.getBird_species());
//        holder.mPredictionTextView.setText(bird.getPrediction());
        holder.mDateTimeTextView.setText(bird.getDate_time());
        holder.mAddressTextView.setText(bird.getCity()+", "+bird.getCountry());
        holder.mRecognitionTypeTextView.setText(bird.getRecognition_type());
        holder.mUserTextView.setText(bird.getU_email());
        // set other views accordingly
    }

    @Override
    public int getItemCount() {
        return mBirdList.size();
    }




    public static class BirdViewHolder extends RecyclerView.ViewHolder {
        private TextView mSpeciesTextView;
        private TextView mPredictionTextView;
        private TextView mDateTimeTextView;
        private TextView mAddressTextView;
        private TextView mRecognitionTypeTextView;
        private TextView mUserTextView;

        public BirdViewHolder(@NonNull View itemView) {
            super(itemView);

            mSpeciesTextView = itemView.findViewById(R.id.tv_bird_species);
//            mPredictionTextView = itemView.findViewById(R.id.prediction_textview);
            mDateTimeTextView = itemView.findViewById(R.id.tv_date_time);
            mAddressTextView = itemView.findViewById(R.id.tv_address);
            mRecognitionTypeTextView = itemView.findViewById(R.id.tv_recognition_type);
            mUserTextView = itemView.findViewById(R.id.tv_user_id);
            // get other views accordingly
        }
    }
}
