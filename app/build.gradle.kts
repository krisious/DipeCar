plugins {
    id("com.android.application")
}

android {
    namespace = "com.example.dipecar"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.example.dipecar"
        minSdk = 21
        targetSdk = 34
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }
}

dependencies {
    implementation("com.google.code.gson:gson:2.9.0")
    implementation("com.squareup.retrofit2:retrofit:2.9.0")
    implementation("com.squareup.retrofit2:converter-gson:2.9.0")

    implementation("com.pixplicity.easyprefs:EasyPrefs:1.10.0")
    implementation("br.com.liveo:navigationdrawer-material:2.5.1")
    implementation("cn.pedant.sweetalert:library:1.3")
    implementation("com.squareup.picasso:picasso:2.5.2")
    implementation("de.hdodenhof:circleimageview:2.1.0")
    implementation("com.mikepenz:itemanimators:1.0.0@aar")
    implementation ("com.github.Grenderg:toasty:1.5.2")


    implementation("androidx.appcompat:appcompat:1.6.1")
    implementation("com.google.android.material:material:1.10.0")
    implementation("androidx.constraintlayout:constraintlayout:2.1.4")
    testImplementation("junit:junit:4.13.2")
    androidTestImplementation("androidx.test.ext:junit:1.1.5")
    androidTestImplementation("androidx.test.espresso:espresso-core:3.5.1")
}