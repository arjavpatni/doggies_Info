package com.arjavp.dogs.util

import android.content.Context
import android.widget.ImageView
import androidx.databinding.BindingAdapter
import androidx.swiperefreshlayout.widget.CircularProgressDrawable
import com.arjavp.dogs.R
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions

val PERMISSION_SEND_SMS = 234

fun getProgressDrawable(context: Context): CircularProgressDrawable{
    return CircularProgressDrawable(context).apply {
        strokeWidth = 10f
        centerRadius = 50f
        start()
    }//this fun gives a spinner image while image from url is loaded.
}
//extension fun for imageView element:
fun ImageView.loadImage(uri: String?, progressDrawable: CircularProgressDrawable){
    //using glide to load uri of image in ImageView
    val options = RequestOptions()
        .placeholder(progressDrawable)
        .error(R.mipmap.ic_dog_icon)
    Glide.with(context)
        .setDefaultRequestOptions(options)
        .load(uri)
        .into(this)// this refers to ImageView
}
//we have extended imageView class by a function loadImage we created.

//name given to bindingAdapter becomes available as a parameter in item_dog.xml file.
@BindingAdapter("android:imageUrl") //this annotation make below fun available to layout (to dataBinding library)
fun loadImage(view: ImageView, url: String?){
    view.loadImage(url, getProgressDrawable(view.context))
}