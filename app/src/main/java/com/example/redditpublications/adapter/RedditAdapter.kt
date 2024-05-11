package com.example.redditpublications.adapter

import android.animation.ObjectAnimator
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.redditpublications.R
import com.example.redditpublications.model.RedditModel
import com.squareup.picasso.Picasso

class RedditAdapter(private var publications: List<RedditModel>, var context: Context)
    : RecyclerView.Adapter<RedditAdapter.MyViewHolder>() {

    class MyViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val image: ImageView = view.findViewById(R.id.image)
        val author: TextView = view.findViewById(R.id.author)
        val amountOfComments: TextView = view.findViewById(R.id.amountOfComments)
        val createdDate: TextView = view.findViewById(R.id.createdDate)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.activity_publication, parent, false)
        return MyViewHolder(view)
    }

    override fun getItemCount(): Int {
        return publications.count()
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        Picasso.get().load(publications[position].image).into(holder.image)
        holder.author.text = publications[position].author
        holder.createdDate.text = publications[position].createdDate
        holder.amountOfComments.text = publications[position].amountOfComments.toString()

        var isImageExpanded = false

        val scaleUpX = ObjectAnimator.ofFloat(holder.image, "scaleX", 5f)
        val scaleUpY = ObjectAnimator.ofFloat(holder.image, "scaleY", 5f)
        val moveUp = ObjectAnimator.ofFloat(holder.image, "translationY", -280f)
        scaleUpX.duration = 0
        scaleUpY.duration = 0
        moveUp.duration = 0

        val scaleDownX = ObjectAnimator.ofFloat(holder.image, "scaleX", 1f)
        val scaleDownY = ObjectAnimator.ofFloat(holder.image, "scaleY", 1f)
        val moveDown = ObjectAnimator.ofFloat(holder.image, "translationY", 1f)
        scaleDownX.duration = 0
        scaleDownY.duration = 0
        moveDown.duration = 0

        holder.image.setOnClickListener {
            if (isImageExpanded) {
                scaleDownX.start()
                scaleDownY.start()
                moveDown.start()
            } else {
                scaleUpX.start()
                scaleUpY.start()
                moveUp.start()
            }
            isImageExpanded = !isImageExpanded
        }
    }
}