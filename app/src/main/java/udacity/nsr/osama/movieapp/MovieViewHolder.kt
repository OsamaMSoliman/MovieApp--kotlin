package udacity.nsr.osama.movieapp

import android.support.v7.widget.RecyclerView
import android.view.View
import android.widget.ImageView
import android.widget.TextView

class MovieViewHolder(itemView: View, val listener: MovieViewHolderClicked) : RecyclerView.ViewHolder(itemView), View.OnClickListener {
    override fun onClick(v: View?) {
        listener.onMoviePosterClicked(adapterPosition,this)
    }

    val movieImgView = itemView.findViewById<ImageView>(R.id.movieImgView)
    val movieTitleTextView = itemView.findViewById<TextView>(R.id.movieTitleTextView)!!
    val movieRatingTextView: TextView = itemView.findViewById(R.id.movieRatingTextView)

    init {
        itemView.setOnClickListener(this)
    }
}
