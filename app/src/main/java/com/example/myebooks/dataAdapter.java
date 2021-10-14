package com.example.myebooks;

import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;

import de.hdodenhof.circleimageview.CircleImageView;


public class dataAdapter extends FirebaseRecyclerAdapter<filemodal,dataAdapter.myholder> {

    public dataAdapter(@NonNull FirebaseRecyclerOptions<filemodal> options) {
        super(options);
    }

    @Override
    protected void onBindViewHolder(@NonNull myholder myholder, int i, @NonNull filemodal filemodal) {

        myholder.header.setText(filemodal.getFilename());

        myholder.img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(myholder.img.getContext(), showpdf.class);
                intent.putExtra("filename",filemodal.getFilename());
                intent.putExtra("fileurl",filemodal.getFileurl());

                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                myholder.img.getContext().startActivity(intent);

            }
        });
    }

    @NonNull
    @Override
    public myholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.oneview,parent,false);
        return new myholder(view);
    }

    public class myholder extends RecyclerView.ViewHolder{

        ImageView img;
        CircleImageView logoimg;
        TextView header;

        public myholder(@NonNull View itemView) {
            super(itemView);

            img = itemView.findViewById(R.id.image);
            header = itemView.findViewById(R.id.name);
            logoimg = itemView.findViewById(R.id.logoimage);
        }
    }
}

