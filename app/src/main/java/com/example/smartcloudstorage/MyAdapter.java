package com.example.smartcloudstorage;

import android.app.DownloadManager;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Environment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import static android.os.Environment.DIRECTORY_DOWNLOADS;

public class MyAdapter extends RecyclerView.Adapter<MyViewHolder> {

    DownloadFiles downloadFiles;
    ArrayList<DownModel> downModels;
    private StorageReference storageReference = FirebaseStorage.getInstance().getReference();
    private FirebaseAuth fAuth = FirebaseAuth.getInstance();


    public MyAdapter(DownloadFiles downloadFiles, ArrayList<DownModel> downModels) {
        this.downloadFiles = downloadFiles;
        this.downModels = downModels;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {

        LayoutInflater layoutInflater = LayoutInflater.from(downloadFiles.getBaseContext());
        View view = layoutInflater.inflate(R.layout.elements, null, false);

        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final MyViewHolder myViewHolder, final int i) {

        myViewHolder.mName.setText(downModels.get(i).getName());
        myViewHolder.mLink.setText(downModels.get(i).getLink());
        myViewHolder.mDownload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                StorageReference riversRef = storageReference.child(fAuth.getCurrentUser().getEmail()).child(downModels.get(i).getName());

                Task<Uri> url = riversRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {

                    public void onSuccess(Uri uri) {
                        downloadFile(myViewHolder.mName.getContext(),downModels.get(i).getName(),DIRECTORY_DOWNLOADS,uri.toString());
                        return;
                    }
                });

            }
        });

        myViewHolder.mDelete.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View view) {
                Log.d("TAG","Delete");

                StorageReference riversRef = storageReference.child(fAuth.getCurrentUser().getEmail()).child(downModels.get(i).getName());
                riversRef.delete();

                FirebaseFirestore.getInstance().collection(fAuth.getCurrentUser().getEmail())
                        .whereEqualTo("name",downModels.get(i).getName())
                        .get()
                        .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                            @Override
                            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {


                                List<DocumentSnapshot> snapshotList = queryDocumentSnapshots.getDocuments();
                                for(DocumentSnapshot snapshot: snapshotList){
                                    FirebaseFirestore.getInstance().collection(fAuth.getCurrentUser().getEmail()).document(snapshot.getId()).delete();
                                }

                            }
                        }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {

                            }
                        });

                myViewHolder.itemView.setVisibility(View.INVISIBLE);

            }
        });


    }

    public void downloadFile(Context context, String fileName,String destinationDirectory, String url) {

        File file = new File(Environment.getExternalStorageDirectory(),"Download/"+fileName);
        DownloadManager downloadmanager = (DownloadManager) context.
                getSystemService(Context.DOWNLOAD_SERVICE);
        Uri uri = Uri.parse(url);
        DownloadManager.Request request = new DownloadManager.Request(uri)

        .setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED)
        .setDestinationUri(Uri.fromFile(file));
        downloadmanager.enqueue(request);
    }


    @Override
    public int getItemCount() {
        return downModels.size();
    }
}
