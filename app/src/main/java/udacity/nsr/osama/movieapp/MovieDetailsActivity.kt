package udacity.nsr.osama.movieapp

import android.content.ActivityNotFoundException
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.support.v4.app.LoaderManager
import android.support.v4.content.Loader
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import android.view.View
import android.widget.TextView
import com.bumptech.glide.Glide
import com.google.android.flexbox.FlexboxLayout
import kotlinx.android.synthetic.main.activity_movie_details.*
import udacity.nsr.osama.movieapp.models.MovieModel
import udacity.nsr.osama.movieapp.models.ReviewModel
import udacity.nsr.osama.movieapp.online.FetchReviewsAndTrailersOnline


class MovieDetailsActivity : AppCompatActivity(), LoaderManager.LoaderCallbacks<Pair<ArrayList<String>, ArrayList<ReviewModel>>> {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_movie_details)

        if (intent.hasExtra(MVHC.MOVIE_INDEX)) {
            val movie: MovieModel = MainActivity.MOVIES()?.get(intent.getIntExtra(MVHC.MOVIE_INDEX, 0))!!
            adultPlus18TextView.visibility = if (movie.isAdult) View.VISIBLE else View.INVISIBLE
            voteCountTextView.text = movie.votersCount.toString()
            ratingBar.rating = movie.rating.toFloat() / 2
            movie.genres.forEach {
                //                genreTextView.append(MainActivity.Genres[it] + "|")
                MainActivity.Genres[it]?.let { it1 -> createGenreTextView(it1) }
            }
            releaseDateTextView.text = "(${movie.releaseDate})"
            overviewTextView.text = movie.summary
//            titleTextView.text = movie.originalTitle
            titleTextView.title = movie.originalTitle
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

            val bundle = Bundle()
            bundle.putString(FetchReviewsAndTrailersOnline.URL_KEY, constructLink(movie._id))
            supportLoaderManager.initLoader(FetchReviewsAndTrailersOnline.LOADER_ID, bundle, this)
        }
    }

    private fun createGenreTextView(text: String) {
        val textView = TextView(this)
        textView.text = text
        textView.background = resources.getDrawable(R.drawable.round_rect, null)
        textView.setPadding(1, 0, 1, 0)
        val params = FlexboxLayout.LayoutParams(
                FlexboxLayout.LayoutParams.WRAP_CONTENT,
                FlexboxLayout.LayoutParams.WRAP_CONTENT
        )
        params.setMargins(2, 1, 2, 1)
        textView.layoutParams = params
        genreLinearLayout.addView(textView)
    }

    private fun constructLink(id: Int): String {
        val link = StringBuilder(resources.getString(R.string.TMDB_URL))
        link.append(resources.getString(R.string.TMDB_movie))
        link.append(id)
        link.append(getString(R.string.TMDB_API_Key))
        link.append(resources.getString(R.string.TMDB_Key_v3))
        link.append(resources.getString(R.string.TMDB_append_to_response))
        link.append(resources.getString(R.string.TMDB_videos_and_reviews))
        return link.toString()
    }

    lateinit var trailerUrlId: String

    fun playTrailer(v: View) {
        val appIntent = Intent(Intent.ACTION_VIEW, Uri.parse("vnd.youtube:$trailerUrlId"))
        val webIntent = Intent(Intent.ACTION_VIEW,
                Uri.parse("http://www.youtube.com/watch?v=$trailerUrlId"))
        try {
            startActivity(appIntent)
        } catch (ex: ActivityNotFoundException) {
            startActivity(webIntent)
        }
    }

    override fun onLoadFinished(loader: Loader<Pair<ArrayList<String>, ArrayList<ReviewModel>>>, data: Pair<ArrayList<String>, ArrayList<ReviewModel>>?) {
        trailerBtn.isEnabled = true
        trailerUrlId = data?.first!![0]
        reviewRecyclerView.adapter = ReviewAdaptor(data.second)
        reviewRecyclerView.layoutManager = LinearLayoutManager(this)
    }

    override fun onLoaderReset(loader: Loader<Pair<ArrayList<String>, ArrayList<ReviewModel>>>) {}

    override fun onCreateLoader(id: Int, bundle: Bundle?): Loader<Pair<ArrayList<String>, ArrayList<ReviewModel>>> {
        return when (id) {
            FetchReviewsAndTrailersOnline.LOADER_ID -> FetchReviewsAndTrailersOnline(this, bundle!!.getString(FetchReviewsAndTrailersOnline.URL_KEY, ""))
            else -> throw Exception("Undefined Loader ID")
        }
    }
}
