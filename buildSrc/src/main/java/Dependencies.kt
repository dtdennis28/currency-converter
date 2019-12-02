object Versions {
    val dagger = "2.25.2"
    val rxJava = "2.2.2"
    val retrofit = "2.6.2"

    // Support type libs
    val androidXCore = "1.1.0"
    val constraintLayout = "1.1.3"
    val recyclerview = "1.0.0"
    val cardView = "1.0.0"
    val androidXLifecycle = "2.1.0"

    val androidGradle = "3.5.1"
    val kotlin = "1.3.50"

    val junit = "4.12"
    val espresso = "3.1.1"
    val mockito = "3.1.0"
    val mockitoKotlin = "2.2.0"
    val robolectric = "4.3.1"
}

object Libraries {
    val androidGradlePlugin = "com.android.tools.build:gradle:${Versions.androidGradle}"
    val kotlinGradlePlugin = "org.jetbrains.kotlin:kotlin-gradle-plugin:${Versions.kotlin}"
    val kotlinStdLib = "org.jetbrains.kotlin:kotlin-stdlib-jdk7:${Versions.kotlin}"

    val appCompat = "androidx.appcompat:appcompat:${Versions.androidXCore}"
    val ktx = "androidx.core:core-ktx:${Versions.androidXCore}"
    val constraintLayout = "androidx.constraintlayout:constraintlayout:${Versions.constraintLayout}"
    val recyclerView = "androidx.recyclerview:recyclerview:${Versions.recyclerview}"
    val cardView = "androidx.cardview:cardview:${Versions.cardView}"

    val dagger = "com.google.dagger:dagger:${Versions.dagger}"
    val daggerCompiler = "com.google.dagger:dagger-compiler:${Versions.dagger}"

    val androidXLifecycle = "androidx.lifecycle:lifecycle-extensions:${Versions.androidXLifecycle}"
    val androidXLifecycleRx = "androidx.lifecycle:lifecycle-reactivestreams:${Versions.androidXLifecycle}"

    val rxJava = "io.reactivex.rxjava2:rxjava:${Versions.rxJava}"

    val retrofit = "com.squareup.retrofit2:retrofit:${Versions.retrofit}"
    val retrofitRxJavaAdapter = "com.squareup.retrofit2:adapter-rxjava2:${Versions.retrofit}"
    val retrofitGson = "com.squareup.retrofit2:converter-gson:${Versions.retrofit}"

    val junit = "junit:junit:${Versions.junit}"
    val androidXTestCore = "androidx.test:core:${Versions.androidXCore}"
    val androidXJUnit = "androidx.test.ext:junit:${Versions.androidXCore}"
    val robolectric = "org.robolectric:robolectric:${Versions.robolectric}"
    val espresso = "androidx.test.espresso:espresso-core:${Versions.espresso}"

    val mockito = "org.mockito:mockito-core:${Versions.mockito}"

    val mockitoKotlin = "com.nhaarman.mockitokotlin2:mockito-kotlin:${Versions.mockitoKotlin}"
}