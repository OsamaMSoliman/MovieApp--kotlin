package udacity.nsr.osama.movieapp

import android.support.v7.widget.RecyclerView
import android.view.View
import android.widget.TextView

class ReviewViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
    val authorTextView: TextView = itemView.findViewById(R.id.authorTextView)
    val contentTextView = itemView.findViewById<TextView>(R.id.contentTextView)
}