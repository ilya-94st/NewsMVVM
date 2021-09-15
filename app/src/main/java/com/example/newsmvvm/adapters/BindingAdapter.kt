package com.example.newsmvvm.adapters

import android.widget.ImageView
import androidx.databinding.BindingAdapter
import com.bumptech.glide.Glide

@BindingAdapter("loadImage")
fun ImageView.loadImage(image: String?){
    Glide.with(this).load(image).into(this)
}