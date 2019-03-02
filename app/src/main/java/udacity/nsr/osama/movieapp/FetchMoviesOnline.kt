package udacity.nsr.osama.movieapp

import android.os.AsyncTask
import android.util.Log
import org.json.JSONObject
import java.net.HttpURLConnection
import java.net.URL
import java.util.*
import kotlin.collections.ArrayList
import kotlin.collections.HashMap


class FetchMoviesOnline(private val adapter: MovieAdapter, private val int: Int) : AsyncTask<String, Unit, ArrayList<MovieModel>>() {
    override fun doInBackground(vararg params: String?): ArrayList<MovieModel> {
//        if (!params.isEmpty()) return
        return decodeJson(this.getJsonStringFrom(URL(params[0]))!!)
    }

    override fun onPostExecute(result: ArrayList<MovieModel>?) {
        super.onPostExecute(result)
        MainActivity.MOVIES(int)!!.addAll(result!!)
        adapter.setNewSrc(MainActivity.MOVIES(int)!!)
    }

    private fun getJsonStringFrom(url: URL): String? {
        val urlConnection = url.openConnection() as HttpURLConnection
        try {
            val inStream = urlConnection.inputStream
            val scanner = Scanner(inStream).useDelimiter("\\A")
            return if (scanner.hasNext()) scanner.next() else null
        } finally {
            urlConnection.disconnect()
        }
    }

    private fun decodeJson(jsonString: String): ArrayList<MovieModel> {
        val movieList: ArrayList<MovieModel> = ArrayList()
        Log.d("decodeJson:jsonString", jsonString)
        val jsonArray = JSONObject(jsonString).getJSONArray("results")!!
        for (i in 0 until jsonArray.length()) {
            val movie = jsonArray.getJSONObject(i)
            val originalTitle = movie.getString("original_title")!!
            val overview = movie.getString("overview")!!
            val posterPath = movie.getString("poster_path")!!
            val backdropPath = movie.getString("backdrop_path")
            val releaseDate = movie.getString("release_date")!!
            val voteAverage = movie.getDouble("vote_average")
            val voteCount = movie.getInt("vote_count")
            val video = movie.getBoolean("video")
            val adult = movie.getBoolean("adult")
            val genresArray = movie.getJSONArray("genre_ids")
            val genreIds = mutableListOf<Int>()
            (0 until genresArray.length()).forEach { index -> genreIds.add(genresArray.getInt(index)) }
            movieList.add(MovieModel(originalTitle, overview, posterPath, backdropPath, releaseDate, genreIds, voteAverage, voteCount, video, adult))
        }
        return movieList
    }

    // it's okay as this AsyncTask will finish before the others
    inner class FetchMovieGenres : AsyncTask<String, Unit, Map<Int, String>>() {
        override fun doInBackground(vararg params: String?): Map<Int, String> {
            return this.decodeJson(getJsonStringFrom(URL(params[0]))!!)
        }

        private fun decodeJson(jsonString: String): Map<Int, String> {
            val genres: HashMap<Int, String> = HashMap()
            val jsonArray = JSONObject(jsonString).getJSONArray("genres")!!
            for (i in 0 until jsonArray.length()) {
                val genre = jsonArray.getJSONObject(i)
                genres[genre.getInt("id")] = genre.getString("name")
            }
            return genres
        }

        override fun onPostExecute(result: Map<Int, String>?) {
            super.onPostExecute(result)
            MainActivity.Genres.putAll(result!!)
        }
    }
}