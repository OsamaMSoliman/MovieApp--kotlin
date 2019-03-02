package udacity.nsr.osama.movieapp

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.ViewGroup
import com.bumptech.glide.Glide


class MovieAdapter(private val cachedContext: Context) : RecyclerView.Adapter<MovieViewHolder>(){

    private val mvhc : MVHC by lazy {
        MVHC(cachedContext)
    }

    private lateinit var movies: ArrayList<MovieModel>

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MovieViewHolder {
        val v = LayoutInflater.from(parent.context).inflate(R.layout.movie_item, parent, false)
        return MovieViewHolder(v,mvhc)
    }

    override fun getItemCount(): Int = try {
        movies.size
    } catch (e: Exception) {
        0
    }

    override fun onBindViewHolder(holder: MovieViewHolder, position: Int) {
        Glide.with(cachedContext)
                .load(cachedContext.resources.getString(R.string.base_image_url_w342) + movies[position].poster)
                .centerCrop()
                .placeholder(getRandomLoadingImage())
                .error(R.drawable.error_icon)
                .into(holder.movieImgView)
        holder.movieTitleTextView.text = movies[position].originalTitle
        holder.movieRatingTextView.text = movies[position].rating.toString()
    }

    private fun getRandomLoadingImage(): Int {
        val images = cachedContext.resources.obtainTypedArray(R.array.loading_images)
        try {
            val choice = (Math.random() * images.length()).toInt()
            return images.getResourceId(choice, R.drawable.loading_animation_0)
        } finally {
            images.recycle()
        }
    }

    fun setNewSrc(movies: ArrayList<MovieModel>) {
        this.movies = movies
        this.notifyDataSetChanged()
    }
}
