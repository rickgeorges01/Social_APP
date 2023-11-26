package com.example.instagramclone;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;

public class MainActivity extends AppCompatActivity {

    /*** DECLARATION DES VARIABLES ****/
    private ImageView logoImage;
    private LinearLayout linearLayout;
    private Button register;
    private Button login;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

    /**Récupération des identifiants des composants présents sur l'activity_main**/

        logoImage = findViewById(R.id.logo_image);
        linearLayout = findViewById(R.id.Linear_layout);
        register = findViewById(R.id.register);
        login = findViewById(R.id.login);

        /*** ANIMATION DU LINEAR LAYOUT  ****/

        linearLayout.animate().alpha(0f).setDuration(1);

        // Actions à effectuer lors du clic sur logoImage
        TranslateAnimation animation = new TranslateAnimation(0,0,0,-1000);
        animation.setDuration(1000);
        animation.setFillAfter(false);
        animation.setAnimationListener(new MyAnimationListener());

        logoImage.setAnimation(animation);

        // Configuration du listener pour gérer les clics sur le bouton 'register'.
        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this , RegisterActivity.class).addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP));
            }
        });

        // Configuration du listener pour gérer les clics sur le bouton 'login'.
        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this , LoginActivity.class).addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP));
            }
        });
    }

    private class MyAnimationListener implements Animation.AnimationListener {

        @Override
        public void onAnimationStart(Animation animation) {

        }

        @Override
        public void onAnimationEnd (Animation animation) {
            //Actions à effectuer à la fin de l'animation
                logoImage.clearAnimation();
                logoImage.setVisibility(View.INVISIBLE);
                linearLayout.animate().alpha(1f).setDuration(1000);
        }

        @Override
        public void onAnimationRepeat(Animation animation) {

        }
    }
}