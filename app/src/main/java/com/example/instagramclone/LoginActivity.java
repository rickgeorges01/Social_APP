package com.example.instagramclone;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

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
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class LoginActivity extends AppCompatActivity {

    /*** Déclartion des variables ****/
    private EditText email;
    private EditText password;
    private Button login;
    private TextView registerUser;
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        /**Récupération des identifiants des composants présents sur l'activity_login**/
        email = findViewById(R.id.email);
        password = findViewById(R.id.password);
        login = findViewById(R.id.login);
        registerUser = findViewById(R.id.register_user);
        mAuth = FirebaseAuth.getInstance();

        /** Définition  d'un écouteur de clic pour le "S'inscrire" **/
        registerUser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(LoginActivity.this, RegisterActivity.class).addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK));
            }
        });
        /** Définition d'un écouteur de clic pour le bouton "Connexion" **/
        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Récupèration et conversion en String du texte saisi dans les EditText
                String txt_email = email.getText().toString();
                String txt_password = password.getText().toString();

                //Définition des conditions de connexion
                if (TextUtils.isEmpty(txt_email) || TextUtils.isEmpty(txt_password)){
                    Toast.makeText(LoginActivity.this, "Veuillez remplir tout les champs", Toast.LENGTH_SHORT).show();
                } else {
                    loginUser(txt_email , txt_password);
                }
            }

            private void loginUser(String email, String password) {

                // Connexion d'un compte utilisateur avec email et mot de passe dans Firebase Auth
                mAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {

                    // Vérifie si les identifiants correspondent et redirige vers la page de connexion
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()){
                            Toast.makeText(LoginActivity.this, "Connexion établie!", Toast.LENGTH_SHORT).show();
                            try {
                                Intent intent = new Intent(LoginActivity.this, HomeActivity.class);
                                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                startActivity(intent);
                                finish();
                            }catch (Exception e){
                                Log.e("LoginActivity", "Erreur lors du lancement de HomeActivity", e);
                            }

                        }
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    // Affiche un message d'erreur si la création du compte échoue
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(LoginActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });
    }
}