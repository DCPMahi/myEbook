package com.example.myebooks;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.FileUtils;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionDeniedResponse;
import com.karumi.dexter.listener.PermissionGrantedResponse;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.single.PermissionListener;

public class Upload extends AppCompatActivity {

    ImageButton cancel;
    TextView br;
    FloatingActionButton upload;
    ImageView pdf,browse;
    TextInputEditText filename;

    Uri filepath;
    DatabaseReference databaseReference;
    StorageReference storageReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_upload);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        cancel = findViewById(R.id.cancel);
        br = findViewById(R.id.browse_txt);
        browse = findViewById(R.id.browse);
        pdf = findViewById(R.id.pdf);

        br.setVisibility(View.INVISIBLE);
        cancel.setVisibility(View.INVISIBLE);
        pdf.setVisibility(View.INVISIBLE);

        filename = findViewById(R.id.filename);


        storageReference = FirebaseStorage.getInstance().getReference();
        databaseReference = FirebaseDatabase.getInstance().getReference("sybooks");

        browse.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Dexter.withActivity(Upload.this)
                        .withPermission(Manifest.permission.READ_EXTERNAL_STORAGE)
                        .withListener(new PermissionListener() {
                            @Override
                            public void onPermissionGranted(PermissionGrantedResponse response) {
                                Intent intent = new Intent();
                                intent.setType("application/pdf");
                                intent.setAction(Intent.ACTION_GET_CONTENT);
                                startActivityForResult(Intent.createChooser(intent,"choose pdf file"),101);
                            }

                            @Override
                            public void onPermissionDenied(PermissionDeniedResponse response) {

                            }

                            @Override
                            public void onPermissionRationaleShouldBeShown(PermissionRequest permission, PermissionToken token) {
                                token.continuePermissionRequest();
                            }
                        }).check();
            }
        });

        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                pdf.setVisibility(View.INVISIBLE);
                cancel.setVisibility(View.INVISIBLE);
                browse.setVisibility(View.VISIBLE);
                filename.setText("");
            }
        });

        upload = findViewById(R.id.upload);
        upload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                uploadToFirebase(filepath);
            }
        });
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == 101 && resultCode == RESULT_OK){
            filepath = data.getData();

            pdf.setVisibility(View.VISIBLE);
            cancel.setVisibility(View.VISIBLE);
            browse.setVisibility(View.INVISIBLE);
            br.setVisibility(View.INVISIBLE);
        }
    }


    private void uploadToFirebase(Uri filepath) {

        ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setTitle("File Uploading");

        String file = filename.getText().toString();
        if(file.isEmpty()){
            Toast.makeText(getApplicationContext(),"First Enter a Filename",Toast.LENGTH_SHORT).show();
        }
        else{

            StorageReference reference = storageReference.child("Ebooks/"+System.currentTimeMillis()+".pdf");
            reference.putFile(filepath)
                    .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {

                            reference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                @Override
                                public void onSuccess(Uri uri) {
                                    filemodal obj = new filemodal(filename.getText().toString(),uri.toString());

                                    databaseReference.child(databaseReference.push().getKey()).setValue(obj);
                                    progressDialog.dismiss();
                                    Toast.makeText(getApplicationContext(),"File is uploaded successfully!",Toast.LENGTH_SHORT).show();

                                    browse.setVisibility(View.VISIBLE);
                                    pdf.setVisibility(View.INVISIBLE);
                                    cancel.setVisibility(View.INVISIBLE);
                                    br.setVisibility(View.INVISIBLE);
                                    filename.setText("");

                                }
                            });

                        }
                    })
                    .addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onProgress(@NonNull UploadTask.TaskSnapshot snapshot) {
                            int per = (int) ((100 * snapshot.getBytesTransferred()) / snapshot.getTotalByteCount());
                            progressDialog.setMessage("Uploaded : "+ per + "%");
                            progressDialog.show();
                        }
                    });
        }
    }

}