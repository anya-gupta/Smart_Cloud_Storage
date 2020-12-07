package com.example.smartcloudstorage;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.gun0912.tedpermission.PermissionListener;
import com.gun0912.tedpermission.TedPermission;

import java.io.File;
import java.io.IOException;
import java.security.Permission;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    //a constant to track the file chooser intent
    private static final int PICK_IMAGE_REQUEST = 234;

    //Buttons
    private Button buttonChoose;
    private Button buttonUpload;
    private Button downloadBtn;

    private static String downloadurl = "";
    //ImageView
    private ImageView imageView;

    //a Uri object to store file path
    private Uri filePath;

    private StorageReference storageReference = FirebaseStorage.getInstance().getReference();

    private FirebaseAuth fAuth;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        //testing
        //startActivity(new Intent(getApplicationContext(),DownloadFiles.class));


        //getting views from layout
        buttonChoose = (Button) findViewById(R.id.buttonChoose);
        buttonUpload = (Button) findViewById(R.id.buttonUpload);

        imageView = (ImageView) findViewById(R.id.imageView);


        //attaching listener
        buttonChoose.setOnClickListener(this);
        buttonUpload.setOnClickListener(this);


        imageView.setVisibility(View.VISIBLE);
        fAuth = FirebaseAuth.getInstance();
        Drawable myDrawable = getResources().getDrawable(R.mipmap.file_choose);
        imageView.setImageDrawable(myDrawable);


        if (fAuth.getCurrentUser() == null || (!fAuth.getCurrentUser().isEmailVerified())) {
            startActivity(new Intent(getApplicationContext(), Login.class));
            finish();
        } else {
            askPermission();
        }


    }

    private void askPermission() {

        PermissionListener permissionListener = new PermissionListener() {

            @Override
            public void onPermissionGranted() {
            }

            @Override
            public void onPermissionDenied(List<String> deniedPermissions) {
            }
        };

        TedPermission.with(MainActivity.this)
                .setPermissionListener(permissionListener)
                .setPermissions(Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                .check();

    }


    private void showFileChooser() {
        Intent intent = new Intent();
        intent.setType("*/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_IMAGE_REQUEST);
    }

    //handling the image chooser activity result
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null) {
            filePath = data.getData();

            String retrieveFileName = data.getData().getLastPathSegment().toLowerCase();

            if (retrieveFileName.contains("image")) {
                Drawable myDrawable = getResources().getDrawable(R.mipmap.jpg_view_foreground);
                imageView.setImageDrawable(myDrawable);


            } else if (retrieveFileName.contains(".mp3")) {
                Drawable myDrawable = getResources().getDrawable(R.mipmap.mp3_view_foreground);
                imageView.setImageDrawable(myDrawable);
            } else if (retrieveFileName.contains(".mp4")) {
                Drawable myDrawable = getResources().getDrawable(R.mipmap.video_view_foreground);
                imageView.setImageDrawable(myDrawable);

            } else if (retrieveFileName.contains(".pdf")) {
                Drawable myDrawable = getResources().getDrawable(R.mipmap.pdf_view_foreground);
                imageView.setImageDrawable(myDrawable);

            } else if (retrieveFileName.contains(".docx")) {
                Drawable myDrawable = getResources().getDrawable(R.mipmap.doc_view_foreground);
                imageView.setImageDrawable(myDrawable);

            } else if (retrieveFileName.contains(".pptx")) {
                Drawable myDrawable = getResources().getDrawable(R.mipmap.pptx_view_foreground);
                imageView.setImageDrawable(myDrawable);

            } else if (retrieveFileName.contains(".ppt")) {
                Drawable myDrawable = getResources().getDrawable(R.mipmap.ppt_view_foreground);
                imageView.setImageDrawable(myDrawable);

            } else if (retrieveFileName.contains(".apk")) {
                Drawable myDrawable = getResources().getDrawable(R.mipmap.apk_view_foreground);
                imageView.setImageDrawable(myDrawable);

            } else if (retrieveFileName.contains(".txt")) {
                Drawable myDrawable = getResources().getDrawable(R.mipmap.txt_view_foreground);
                imageView.setImageDrawable(myDrawable);

            } else if (retrieveFileName.contains(".xls")) {
                Drawable myDrawable = getResources().getDrawable(R.mipmap.xls_view_foreground);
                imageView.setImageDrawable(myDrawable);

            } else if (retrieveFileName.contains(".xlsx")) {
                Drawable myDrawable = getResources().getDrawable(R.mipmap.xlsx_view_foreground);
                imageView.setImageDrawable(myDrawable);

            } else if (retrieveFileName.contains(".odt")) {
                Drawable myDrawable = getResources().getDrawable(R.mipmap.odt_view_foreground);
                imageView.setImageDrawable(myDrawable);

            } else if (retrieveFileName.contains(".doc")) {
                Drawable myDrawable = getResources().getDrawable(R.mipmap.doc_view_foreground);
                imageView.setImageDrawable(myDrawable);

            } else if (retrieveFileName.contains(".html")) {
                Drawable myDrawable = getResources().getDrawable(R.mipmap.html_view_foreground);
                imageView.setImageDrawable(myDrawable);

            } else if (retrieveFileName.contains(".htm")) {
                Drawable myDrawable = getResources().getDrawable(R.mipmap.htm_view_foreground);
                imageView.setImageDrawable(myDrawable);

            } else if (retrieveFileName.contains(".ods")) {
                Drawable myDrawable = getResources().getDrawable(R.mipmap.ods_view_foreground);
                imageView.setImageDrawable(myDrawable);

            } else if (retrieveFileName.contains(".zip")) {
                Drawable myDrawable = getResources().getDrawable(R.mipmap.zip_view_foreground);
                imageView.setImageDrawable(myDrawable);

            } else if (retrieveFileName.contains(".gif")) {
                Drawable myDrawable = getResources().getDrawable(R.mipmap.gif_view_foreground);
                imageView.setImageDrawable(myDrawable);

            } else {
                Drawable myDrawable = getResources().getDrawable(R.mipmap.unrecognizable_format);
                imageView.setImageDrawable(myDrawable);

            }

            EditText choosenFileName = findViewById(R.id.fileNameToBeSaved);
            if(retrieveFileName.contains("image")){
                choosenFileName.setText(retrieveFileName+".jpg");
            }
            else {
                choosenFileName.setText(retrieveFileName);
            }
        }
    }

    //this method will upload the file
    private void uploadFile() {

        EditText fileName = findViewById(R.id.fileNameToBeSaved);
        if(fileName.getText().toString().contains(".jpg") || fileName.getText().toString().contains(".mp3") || fileName.getText().toString().contains(".mp4")
                || fileName.getText().toString().contains(".pdf") || fileName.getText().toString().contains(".txt") || fileName.getText().toString().contains(".doc")
                || fileName.getText().toString().contains(".docx") || fileName.getText().toString().contains(".ppt") || fileName.getText().toString().contains(".pptx")
                || fileName.getText().toString().contains(".apk") || fileName.getText().toString().contains(".xls") || fileName.getText().toString().contains(".xlsx")
                || fileName.getText().toString().contains(".zip") || fileName.getText().toString().contains(".gif") || fileName.getText().toString().contains(".htm")
                || fileName.getText().toString().contains(".html") || fileName.getText().toString().contains(".jpg") || fileName.getText().toString().contains(".jpg")
                || fileName.getText().toString().contains(".ods") || fileName.getText().toString().contains(".ods")) {


            //if there is a file to upload
            if (filePath != null) {
                //displaying a progress dialog while upload is going on
                ProgressDialog progressDialog = new ProgressDialog(this);
                progressDialog.setTitle("Uploading");
                progressDialog.show();

                //testing

                String name = fileName.getText().toString().trim();
                StorageReference riversRef = storageReference.child(fAuth.getCurrentUser().getEmail()).child(name);
                String finalName = name;
                riversRef.putFile(filePath)
                        .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                            @Override
                            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                                //if the upload is successfull
                                //hiding the progress dialog
                                progressDialog.dismiss();

                                //and displaying a success toast
                                Toast.makeText(getApplicationContext(), "File Uploaded ", Toast.LENGTH_LONG).show();

                                Map<String, Object> user = new HashMap<>();

                                user.put("name", riversRef.getName());
                                user.put("link", "");

                                FirebaseFirestore db = FirebaseFirestore.getInstance();
                                DocumentReference documentReference = db.collection(fAuth.getCurrentUser().getEmail()).document();

                                documentReference.set(user);

                            }
                        })
                        .addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception exception) {
                                //if the upload is not successfull
                                //hiding the progress dialog
                                progressDialog.dismiss();

                                //and displaying error message
                                Toast.makeText(getApplicationContext(), exception.getMessage(), Toast.LENGTH_LONG).show();
                            }
                        })
                        .addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                            @Override
                            public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {
                                //calculating progress percentage
                                double progress = (100.0 * taskSnapshot.getBytesTransferred()) / taskSnapshot.getTotalByteCount();

                                //displaying percentage in progress dialog
                                progressDialog.setMessage("Uploaded " + ((int) progress) + "%...");
                            }
                        });
            }
            //if there is not any file
            else {
                //you can display an error toast
                Toast.makeText(getApplicationContext(), "error", Toast.LENGTH_SHORT).show();
            }
        }else{
            fileName.setError("File Extension required!");
            return;
        }
    }

    @Override
    public void onClick(View view) {
        //if the clicked button is choose
        if (view == buttonChoose) {
            showFileChooser();
        }
        //if the clicked button is upload
        else if (view == buttonUpload) {
            uploadFile();
        } else {
            return;
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.logout_id:
                fAuth.signOut();
                startActivity(new Intent(getApplicationContext(), Login.class));
                finish();
                break;
            default:
                Toast.makeText(MainActivity.this, "Logout not button", Toast.LENGTH_SHORT).show();
                break;
        }

        return true;
    }

    public void showUploads(View view) {
        startActivity(new Intent(getApplicationContext(), DownloadFiles.class));
        finish();
    }

}








