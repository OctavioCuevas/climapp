# climapp
Práctica individual final para [Kodemia](https://kodemia.mx)

# Autor
* Octavio Cuevas Conde

[![An old rock in the desert](/linkedin.png "Linkedin Octavio Cuevas")](https://www.linkedin.com/in/octavio-cuevas/)

# Lenguaje
* Kotlin

# Dependencias
    //Retrofit / Gson
    implementation 'com.squareup.retrofit2:retrofit:2.9.0'
    implementation 'com.squareup.retrofit2:converter-gson:2.9.0'

    //Coroutines
    implementation 'org.jetbrains.kotlinx:kotlinx-coroutines-core:1.6.0'
    implementation 'androidx.lifecycle:lifecycle-runtime-ktx:2.4.1'
    implementation "androidx.lifecycle:lifecycle-viewmodel-ktx:2.4.1"

    //Coil
    implementation 'io.coil-kt:coil:1.4.0'

    testImplementation 'junit:junit:4.13.2'
    androidTestImplementation 'androidx.test.ext:junit-ktx:1.1.3'
    androidTestImplementation 'androidx.test.espresso:espresso-core:3.4.0'

    // Google Play services location
    implementation 'com.google.android.gms:play-services-location:19.0.1'

    // Android Splashscreen
    // En Android 12 no es necesaria
    implementation 'androidx.core:core-splashscreen:1.0.0-beta01'

    //Shared Preferences
    implementation 'androidx.preference:preference:1.2.0'
    implementation 'androidx.navigation:navigation-fragment-ktx:2.4.1'
    implementation 'androidx.navigation:navigation-ui-ktx:2.4.1'
