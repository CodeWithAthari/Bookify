
# Bookify
[![PDF Book App Tutorial](https://img.youtube.com/vi/Rh-Nhsd2g8w/maxresdefault.jpg)](https://youtu.be/Rh-Nhsd2g8w)

Here is list of dependencies you can use
``` gradle
    // Top-level build file where you can add configuration options common to all sub-projects/modules.
    plugins {
        id 'com.android.application' version '7.4.0' apply false
        id 'com.android.library' version '7.4.0' apply false
        id 'org.jetbrains.kotlin.android' version '1.7.21' apply false
        id 'com.google.gms.google-services' version '4.3.15' apply false
    }
    
    ...

    plugins {
        id 'com.android.application'
        id 'org.jetbrains.kotlin.android'
        id 'kotlin-kapt'
        id 'com.google.gms.google-services'
    }
    
    ...

    implementation "com.airbnb.android:lottie:6.1.0"
    implementation 'com.github.bumptech.glide:glide:4.15.1'

    def lifecycle_version = "2.5.1"
    implementation "androidx.lifecycle:lifecycle-viewmodel:$lifecycle_version"
    implementation "androidx.lifecycle:lifecycle-viewmodel-ktx:$lifecycle_version"

    implementation 'org.jetbrains.kotlinx:kotlinx-coroutines-android:1.6.4'

    // Firebase Libs
    implementation platform('com.google.firebase:firebase-bom:32.2.0')
    implementation 'com.google.firebase:firebase-analytics-ktx'
    implementation 'com.google.firebase:firebase-database-ktx'

```

Here are the credits & learning material
- Download Manager
    - [Medium - Downloading File Properly in Android](https://medium.com/@aungkyawmyint_26195/downloading-file-properly-in-android-d8cc28d25aca)
    - [Android Developer - DownloadManager](https://developer.android.com/reference/android/app/DownloadManager)
    - [Android Developer - DownloadManager.Query](https://developer.android.com/reference/android/app/DownloadManager.Query)
    - [Android Developer - DownloadManager.Request](https://developer.android.com/reference/android/app/DownloadManager.Request)
- Cursor
    - [Android Developer - Cursor](https://developer.android.com/reference/android/database/Cursor)
    - [Introduction to Cursor in Android](https://www.edureka.co/blog/introduction-to-cursor-in-android/)
- Nested Recycler View
    - [Optimizing Nested RecyclerView](https://proandroiddev.com/optimizing-nested-recyclerview-a9b7830a4ba7)
    - [Medium - Nested RecyclerView in Android](https://medium.com/nerd-for-tech/nested-recyclerview-in-android-e5afb2b9771a#:~:text=Let%E2%80%99s%20talk%20about%20Optimizations%20of%20RecyclerView)
- Spring Animation
    - [Android Developer - Spring Animation](https://developer.android.com/develop/ui/views/animations/spring-animation)
    - [Medium - Spring Back RecyclerView: The Basics](https://medium.com/swlh/spring-back-recyclerview-the-basics-beebe3477cad)
    - [GitHub - Android Animation Samples](https://github.com/KaustubhPatange/android-animation-samples)
- StackoverFlow
    - [Stack Overflow - Android Studio Says Duplicate Class Found](https://stackoverflow.com/questions/75239367/android-studio-says-duplicate-class-found)
- Github
    - [GitHub - FlutterEbookApp](https://github.com/JideGuru/FlutterEbookApp/tree/master)
    - [GitHub - SpringScrollHelper.kt](https://github.com/KaustubhPatange/android-animation-samples/blob/master/SpringBack-RecyclerView/app/src/main/java/com/kpstv/dampingrecyclerview/ui/helpers/SpringScrollHelper.kt)
 
  ## My Socials

You can give my socials a try

[Instagram 📷](https://www.instagram.com/athar_android/)
[Github 👨‍💻](https://github.com/CodeWithAthari) 
[Twitter 💬](https://twitter.com/ZamanAthari)
