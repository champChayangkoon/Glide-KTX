package io.github.champchayangkoon.glidektxexample

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.load.resource.bitmap.CircleCrop
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions.withCrossFade
import io.github.champchayangkoon.glide.ktx.load
import com.chayangkoon.champ.glidektxexample.R
import kotlinx.android.synthetic.main.activity_main.*

/**
 * Example url by Lukasz Szmigiel on Unsplash
 */
class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val url = "https://images.unsplash.com/photo-1441974231531-c6227db76b6e?ixlib=rb-1.2.1&ixid=eyJhcHBfaWQiOjEyMDd9&auto=format&fit=crop&w=1651&q=80"
        imageView.load(url, withCrossFade(1000)) {
            transform(CircleCrop())
        }
    }
}