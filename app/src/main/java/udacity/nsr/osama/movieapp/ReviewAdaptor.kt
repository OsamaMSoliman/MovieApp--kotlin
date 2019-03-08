package udacity.nsr.osama.movieapp

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.ViewGroup
import udacity.nsr.osama.movieapp.models.ReviewModel

class ReviewAdaptor(private val reviews: ArrayList<ReviewModel>) : RecyclerView.Adapter<ReviewViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ReviewViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.review_item, parent, false)
        return ReviewViewHolder(view)
    }

    override fun getItemCount(): Int = reviews.size

    override fun onBindViewHolder(holder: ReviewViewHolder, position: Int) {
        holder.authorTextView.text = reviews[position].author
        holder.contentTextView.text = reviews[position].content
    }

}