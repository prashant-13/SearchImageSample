package com.example.sampletestsearchimage.adapters

import android.content.Context
import android.content.Intent
import android.graphics.drawable.Drawable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.request.RequestListener
import com.example.sampletestsearchimage.R
import com.example.sampletestsearchimage.Utilities.Utils
import com.example.sampletestsearchimage.activity.ViewImageActivity
import com.example.sampletestsearchimage.database.entity.ImageResponse
import kotlinx.android.synthetic.main.list_item.view.*

class ImageListAdapter(val context: Context, private val list: List<ImageResponse>) : RecyclerView.Adapter<ImageListAdapter.MyViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.list_item, parent, false)
        return MyViewHolder(view)
    }

    override fun getItemCount(): Int {
        return list.size
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val imageList = list[position]
        holder.setData(imageList, position)
    }

    inner class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        var imageResponse: ImageResponse? = null
        var currentPosition: Int = 0

        init {
            itemView.imageView.setOnClickListener {

                imageResponse?.let {
                    val intent = Intent(context, ViewImageActivity::class.java)
                    intent.putExtra(ViewImageActivity.TAG_IMAGE_DATA, imageResponse)
                    context.startActivity(intent)
                }
            }
        }

        fun setData(imageResponse: ImageResponse?, pos: Int) {
            imageResponse?.let {
                if (!Utils.isNullOrEmptyList(imageResponse.images)) {
                    val images = imageResponse.images[0]
                    Glide.with(context)
                            .load(images.link)
                            .centerCrop()
                            .override(200, 200)
                            .diskCacheStrategy(DiskCacheStrategy.ALL)
                            .listener(object : RequestListener<Drawable> {
                                override fun onLoadFailed(p0: GlideException?, p1: Any?, p2: com.bumptech.glide.request.target.Target<Drawable>?, p3: Boolean): Boolean {
                                    itemView.progressBar.visibility = View.GONE
                                    return false
                                }

                                override fun onResourceReady(p0: Drawable?, p1: Any?, p2: com.bumptech.glide.request.target.Target<Drawable>?, p3: DataSource?, p4: Boolean): Boolean {
                                    itemView.progressBar.visibility = View.GONE
                                    return false
                                }
                            })
                            .into(itemView.imageView)
                }
            }
            this.imageResponse = imageResponse
            this.currentPosition = pos
        }
    }
}
