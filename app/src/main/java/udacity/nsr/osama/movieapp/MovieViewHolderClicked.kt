package udacity.nsr.osama.movieapp

import android.app.Activity
import android.app.ActivityOptions
import android.content.Context
import android.content.Intent

interface MovieViewHolderClicked {
    fun onMoviePosterClicked(adapterPosition: Int, v: MovieViewHolder)
}


class MVHC(val context: Context) : MovieViewHolderClicked {

    companion object {
        var MOVIE_INDEX = "MOVIE_INDEX"
    }

    override fun onMoviePosterClicked(adapterPosition: Int, v: MovieViewHolder) {
        val intent = Intent(context, MovieDetailsActivity::class.java)
        intent.putExtra(MOVIE_INDEX, adapterPosition)
        val options = ActivityOptions.makeSceneTransitionAnimation(context as Activity, v.movieImgView, "moviePoster")
        context.startActivity(intent, options.toBundle())
    }
}