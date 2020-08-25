package com.example.sampletestsearchimage.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.sampletestsearchimage.R
import com.example.sampletestsearchimage.database.entity.ImageComments
import kotlinx.android.synthetic.main.list_item_comment.view.*

class CommentListAdapter(val context: Context, private val list: List<ImageComments>) : RecyclerView.Adapter<CommentListAdapter.MyViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.list_item_comment, parent, false)
        return MyViewHolder(view)
    }

    override fun getItemCount(): Int {
        return list.size
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val comment = list[position]
        holder.setData(comment, position)
    }

    inner class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        var comments: ImageComments? = null
        var currentPosition: Int = 0

        fun setData(comments: ImageComments, pos: Int) {
            comments?.let {
                itemView.tvComment.text = comments.comment
            }
            this.comments = comments
            this.currentPosition = pos
        }
    }
}
