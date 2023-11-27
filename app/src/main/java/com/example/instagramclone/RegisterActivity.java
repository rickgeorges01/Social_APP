package com.example.instagramclone;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;

public class RegisterActivity extends AppCompatActivity {

    /*** Déclartion des variables ****/
    private EditText pseudo;
    private EditText firstname;
    private EditText name;
    private EditText email;
    private EditText password;
    private Button register;
    private TextView loginUser;

    private DatabaseReference mRootDef;
    private FirebaseAuth mAuth;
    ProgressDialog pd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        /**Récupération des identifiants des composants présents sur l'activity_register**/
        pseudo = findViewById(R.id.pseudo);
        firstname = findViewById(R.id.firstname);
        name = findViewById(R.id.name);
        email = findViewById(R.id.email);
        password = findViewById(R.id.password);
        register = findViewById(R.id.register);
        loginUser = findViewById(R.id.login_user);

        // Obtention d'une référence à la racine de la base de données Firebase
        mRootDef = FirebaseDatabase.getInstance().getReference();

        // Obtention d'une instance de FirebaseAuth pour gérer l'auth des users
        mAuth = FirebaseAuth.getInstance();

        //Barre de progression
        pd = new ProgressDialog(this);

        /** Définition  d'un écouteur de clic pour le "Connectez-vous" **/
        loginUser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(RegisterActivity.this , LoginActivity.class));
            }
        });

        /** Définition d'un écouteur de clic pour le bouton "Register" **/
        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Récupèration et conversion en String du texte saisi dans les EditText
               String txt_pseudo = pseudo.getText().toString();
               String txt_firstname = firstname.getText().toString();
               String txt_name = name.getText().toString();
               String txt_email = email.getText().toString();
               String txt_password = password.getText().toString();

               //Définition des conditions d'inscription
               if(TextUtils.isEmpty(txt_pseudo) || TextUtils.isEmpty(txt_firstname) || TextUtils.isEmpty(txt_name)
                       || TextUtils.isEmpty(txt_email) || TextUtils.isEmpty(txt_password)){
                   Toast.makeText(RegisterActivity.this, "Veuillez remplir tout les champs !", Toast.LENGTH_SHORT).show();
               } else if (txt_password.length() < 6){
                   Toast.makeText(RegisterActivity.this, "Mot de passe trop court !", Toast.LENGTH_SHORT).show();
               } else {
                   registerUser(txt_pseudo, txt_firstname, txt_name, txt_email, txt_password);
               }
            }
        });
    }

    /** Enregistrement des utilisateurs **/
    private void registerUser(String pseudo, String firstname, String name, String email, String password) {

        //Configuration de la Barre de progression
        pd.setMessage("Please wait !");
        pd.show();

        // Création d'un compte utilisateur avec email et mot de passe dans Firebase Auth
        mAuth.createUserWithEmailAndPassword(email, password).addOnSuccessListener(new OnSuccessListener<AuthResult>() {

          //  Préparation et ajout des données de l'utilisateur à enregistrer dans Firebase Database
            @Override
            public void onSuccess(AuthResult authResult) {
                HashMap<String, Object> map = new HashMap<>();
                map.put("id", mAuth.getCurrentUser().getUid());
                map.put("Pseudo", pseudo);
                map.put("Nom", name);
                map.put("Prenom", firstname);
                map.put("E-mail", email);

                // Enregistrement des données de l'utilisateur dans la base de données Firebase sous le nœud "Users"
                mRootDef.child("Users").child(mAuth.getCurrentUser().getUid()).setValue(map).addOnCompleteListener(new OnCompleteListener<Void>() {

                    // Vérifie si l'enregistrement des données est réussi et redirige vers la page de connexion
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if(task.isSuccessful()){
                            pd.dismiss();
                            Toast.makeText(RegisterActivity.this, "Votre compte a été crée avec sucess, connectez-vous !", Toast.LENGTH_SHORT).show();
                            try {
                                Intent intent = new Intent(RegisterActivity.this, LoginActivity.class);
                                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                startActivity(intent);
                                finish();
                            } catch (Exception e){
                                Log.e("RegisterActivity", "Erreur lors du lancement de HomeActivity", e);
                            }

                        }
                    }
                });
            }

        }).addOnFailureListener(new OnFailureListener() {
            // Affiche un message d'erreur si la création du compte échoue
            @Override
            public void onFailure(@NonNull Exception e) {
                pd.dismiss();
                Toast.makeText(RegisterActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}