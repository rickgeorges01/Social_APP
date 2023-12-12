package com.example.instagramclone;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.StorageTask;
import com.hendraanggrian.appcompat.socialview.widget.SocialAutoCompleteTextView;
import com.theartofdev.edmodo.cropper.CropImage;

import java.util.HashMap;
import java.util.List;
import java.util.Locale;

public class PostActivity extends AppCompatActivity {

    private Uri imageUri;
    private String imageUrl;
    private ImageView close;
    private ImageView image_added;
    private TextView post;
    SocialAutoCompleteTextView description;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post);

        close = findViewById(R.id.close);
        image_added = findViewById(R.id.image_added);
        post = findViewById(R.id.post);
        description = findViewById(R.id.description);

        //Redirection vers la page d'acceuil après le clic du bouton close
        close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(PostActivity.this, HomeActivity.class));
                finish();
            }
        });
        //Initialisation d'un post à partir d'un clic
        post.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Vérifier si l'image a été sélectionnée
                if (imageUri != null) {
                    upload();
                } else {
                    Toast.makeText(PostActivity.this, "Aucune image sélectionnée", Toast.LENGTH_SHORT).show();
                }
            }
        });
        Log.d("PostActivity", "onCreate: Activity started successfully");
        // Déclenche le processus de sélection et de rognage d'image en utilisant la bibliothèque CropImage.
        CropImage.activity().start(PostActivity.this);
        Log.d("PostActivity", "CropImage activity has been started");
    }

    private void upload() {
        ProgressDialog pd = new ProgressDialog(this);
        pd.setMessage("Téléchargement");
        pd.show();

        // Téléchargement de l'image et stockage des données dans Firebase
        if (imageUri != null){

            StorageReference filePath = FirebaseStorage.getInstance().getReference("Posts").child(System.currentTimeMillis()+ "." + getFileExtension(imageUri));
            StorageTask uploadTask = filePath.putFile(imageUri);
            uploadTask.continueWithTask(new Continuation() {
                @Override
                public Object then(@NonNull Task task) throws Exception {
                    if (!task.isSuccessful()){
                        throw task.getException();
                    }
                    return filePath.getDownloadUrl();
                }
            }).addOnCompleteListener(new OnCompleteListener<Uri>() {
                @Override
                public void onComplete(@NonNull Task<Uri> task) {
                    Uri downloadUri = task.getResult();
                    imageUrl = downloadUri.toString();

                    DatabaseReference ref = FirebaseDatabase.getInstance().getReference("Posts");
                    String postId = ref.push().getKey();

                    HashMap<String, Object> map = new HashMap<>();
                    map.put("postid", postId);
                    map.put("imageurl", imageUrl);
                    map.put("description", description.getText().toString());
                    map.put("publisher", FirebaseAuth.getInstance().getCurrentUser().getUid());

                    ref.child(postId).setValue(map);
                    DatabaseReference  mHashTagRef = FirebaseDatabase.getInstance().getReference().child("HashTags");
                    List<String> hashTags = description.getHashtags();
                    if(!hashTags.isEmpty()){
                        for(String tag : hashTags){
                            map.clear();
                            map.put("tag",tag.toLowerCase());
                            map.put("postid", postId);

                            mHashTagRef.child(tag.toLowerCase()).child(postId).setValue(map);
                        }
                    }
                        pd.dismiss();
                    startActivity(new Intent(PostActivity.this,HomeActivity.class));
                    finish();
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    pd.dismiss();
                    Toast.makeText(PostActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                }
            });

        } else{
            Toast.makeText(this, "Pas d'image sélectionné", Toast.LENGTH_SHORT).show();
        }
    }

    private String getFileExtension(Uri uri) {
        return MimeTypeMap.getSingleton().getExtensionFromMimeType(this.getContentResolver().getType(uri));
    }


    // La méthode onActivityResult est appelée après que l'utilisateur ait terminé le rognage de l'image.
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        // Vérifie si le résultat provient de CropImage et si le résultat est OK.
        if (requestCode ==  CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE && resultCode == RESULT_OK){
            // Obtient le résultat du rognage d'image et récupère l'URI de l'image rognée.
            CropImage.ActivityResult result = CropImage.getActivityResult(data);
            imageUri = result.getUri();
            // Affiche l'image rognée dans la vue ImageView.
            image_added.setImageURI(imageUri);
        }else {
            // Si le rognage de l'image échoue, affiche un message Toast et redirige vers HomeActivity.
            Toast.makeText(this, "Photo pas intégré, veuillez réssayer", Toast.LENGTH_SHORT).show();
            startActivity(new Intent(PostActivity.this, HomeActivity.class));
            finish();
        }
    }
}