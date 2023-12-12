plugins {
    id("com.android.application")
    id("com.google.gms.google-services")
}

android {
    namespace = "com.example.instagramclone"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.example.instagramclone"
        minSdk = 27
        targetSdk = 33
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(getDefaultProguardFile("proguard-android-optimize.txt"), "proguard-rules.pro")
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }
}

dependencies {

    implementation("androidx.appcompat:appcompat:1.6.1")
    //Dépendances liés à la gestion du bottom_menu
    implementation("com.google.android.material:material:1.10.0")
    implementation("androidx.constraintlayout:constraintlayout:2.1.4")

    //Dépendances liés aux fonctionnalités de réseau social
    implementation("com.hendraanggrian.appcompat:socialview:0.1")
    //Dépendances liés aux fonctionnalité d'auto-complétion pour les mentions et les hashtags
    implementation("com.hendraanggrian.appcompat:socialview-autocomplete:0.1")
    //Dépendances liés aux fonctionnalité de rognage d'image dans  l'application.
    implementation ("com.theartofdev.edmodo:android-image-cropper:2.7.0")
    //Dépendances liés à l'image en cercle
    implementation ("de.hdodenhof:circleimageview:3.1.0")
    //Dépendances liés à l'image de profile
    implementation("com.squareup.picasso:picasso:2.8")

    //Dépendances de firebase
    implementation("com.google.firebase:firebase-auth:22.3.0")
    implementation("com.google.firebase:firebase-database:20.3.0")
    implementation("com.google.firebase:firebase-storage:20.3.0")
    implementation("com.google.firebase:firebase-functions:20.4.0")
    testImplementation("junit:junit:4.13.2")
    androidTestImplementation("androidx.test.ext:junit:1.1.5")
    androidTestImplementation("androidx.test.espresso:espresso-core:3.5.1")
}