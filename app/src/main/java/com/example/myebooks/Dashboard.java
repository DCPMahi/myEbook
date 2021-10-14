package com.example.myebooks;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class Dashboard extends AppCompatActivity {

    FloatingActionButton button;
    public RecyclerView rclv;
    public dataAdapter adapter;

    DatabaseReference mbase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);

        button = findViewById(R.id.floatingActionButton);

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(Dashboard.this,Upload.class));
            }
        });

        rclv = findViewById(R.id.recyclerview);
        rclv.setLayoutManager(new LinearLayoutManager(this));

        mbase = FirebaseDatabase.getInstance().getReference().child("sybooks");

        FirebaseRecyclerOptions<filemodal> options
                = new FirebaseRecyclerOptions.Builder<filemodal>()
                .setQuery(mbase, filemodal.class)
                .build();

        Toast.makeText(getApplicationContext(),"Data is fetching...",Toast.LENGTH_SHORT).show();

        adapter = new dataAdapter(options);
        adapter.startListening();
        rclv.setAdapter(adapter);
    }


}