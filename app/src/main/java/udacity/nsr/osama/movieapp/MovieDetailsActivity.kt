package udacity.nsr.osama.movieapp

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.View
import com.bumptech.glide.Glide
import kotlinx.android.synthetic.main.activity_movie_details.*

class MovieDetailsActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_movie_details)

        if (intent.hasExtra(MVHC.MOVIE_INDEX)) {
            val movie: MovieModel = MainActivity.MOVIES()?.get(intent.getIntExtra(MVHC.MOVIE_INDEX, 0))!!
            adultPlus18TextView.visibility = if (movie.isAdult) View.VISIBLE else View.INVISIBLE
            voteCountTextView.text = movie.votersCount.toString()
            ratingBar.rating = movie.rating.toFloat() / 2
            movie.genres.forEach {
                genreTextView.append(MainActivity.Genres[it] + "|")
            }
            releaseDateTextView.text = "(${movie.releaseDate})"
            overviewTextView.text = movie.summary
            titleTextView.text = movie.originalTitle
            Glide.with(this)
                    .load(resources.getString(R.string.base_image_url_w342) + movie.poster)
                    .centerCrop()
                    .placeholder(R.drawable.loading_animation_1)
                    .error(R.drawable.error_icon)
                    .into(posterImageView)
            Glide.with(this)
                    .load(resources.getString(R.string.base_image_url_w500) + movie.backdrop_path)
                    .centerCrop()
                    .placeholder(R.drawable.loading_animation_0)
                    .error(R.drawable.error_icon)
                    .into(backdropImageView)
        }
    }
}
