# Glide-KTX
A set of kotlin extensions for Glide image loader library.

## Gradle
Add a dependency code to your module's build.gradle file:
```groovy
dependencies {
    implementation 'com.chayangkoon.champ:glide-ktx:1.0.0'
}
```
And add below codes to your root project's build.gradle file:
```groovy
allprojects {
    repositories {
        jcenter()
    }
}
```
## Basic Usage
Load image into an `ImageView` is easy with load extension function:
```kotlin
// URL
imageView.load("https://www.example.com/image.jpg")

// Resource
imageView.load(R.drawable.img_example)

// Uri
imageView.load(imageUri)

// File
imageView.load(File("/path/image.jpg"))

// And so on...
```
## Custom requests
Requests can be customized with trailing lambda:
```kotlin
imageView.load("https://www.example.com/image.jpg") {
	transform(CircleCrop())
}

// And can be set transition with pass to second parameter

imageView.load("https://www.example.com/image.jpg", withCrossFade()) {
	transform(CircleCrop())
}
```
## Non-View Targets
In addition to load an image into an ImageView, you can use asynchronous loads into your own `CustomTarget` with lambda:
```kotlin
Glide.with(activity)
    .load("https://www.example.com/image.jpg")
    .intoCustomTarget({ resource, transition ->
        // handle image result
    }, {
        // handle error drawable
    })
```
Or `CustomViewTarget`. 
```kotlin
Glide.with(activity)
    .load("https://www.example.com/image.jpg")
    .intoCustomViewTarget(imageView, { resource, transition ->
        // handle image resource
    }, {
        // handle error drawable
    })
```
## Background Threads
To load an image on background Thread, You can use submit() and then use `getOnCoroutine()` for get image on coroutine. It will execute on background thread by default: 
```kotlin
val futureTarget: FutureTarget<Bitmap> = Glide.with(this)
    .asBitmap()
    .load(https://www.example.com/image.jpg)
    .submit()

coroutineScope.launch {
    val bitmap = futureTarget.getOnCoroutine()
    // handle bitmap result
}
```

## License
```
Copyright 2020 Chayangkoon Tirawanon

Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
You may obtain a copy of the License at

   http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License.
```
part BSD and MIT See the [LICENSE](https://github.com/champChayangkoon/Glide-KTX/blob/master/LICENSE) file for details.
