package udacity.nsr.osama.movieapp

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.StaggeredGridLayoutManager
import android.view.Menu
import android.view.MenuItem
import android.widget.LinearLayout
import kotlinx.android.synthetic.main.activity_main.*
import udacity.nsr.osama.movieapp.models.MovieModel
import udacity.nsr.osama.movieapp.online.FetchMoviesOnline


class MainActivity : AppCompatActivity() {

    //TODO use the static vars right.. their current usage is meaningless
    companion object {
        var lastMovieListSelected: Int = R.string.TMDB_popular_movies
        val Genres: HashMap<Int, String> = HashMap()
        private val movies: HashMap<Int, ArrayList<MovieModel>> =
                hashMapOf(R.string.TMDB_popular_movies to ArrayList(),
                        R.string.TMDB_top_rated_movies to ArrayList())

        fun MOVIES() = movies[lastMovieListSelected]
        fun MOVIES(int: Int): ArrayList<MovieModel>? {
            lastMovieListSelected = int
            return movies[lastMovieListSelected]
        }
    }


    lateinit var adapter: MovieAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

//        recyclerView.layoutManager = GridLayoutManager(this, calculateNoOfColumns())
        recyclerView.layoutManager = StaggeredGridLayoutManager(calculateNoOfColumns(),LinearLayout.VERTICAL)
        this.adapter = MovieAdapter(this)
        recyclerView.adapter = this.adapter
        val x = FetchMoviesOnline(this.adapter, R.string.TMDB_popular_movies)
        x.FetchMovieGenres().execute(BuildUrl(R.string.TMDB_Genres_List))
        x.execute(BuildUrl(R.string.TMDB_popular_movies))
    }


    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.sorting, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.most_popular_sort -> {
                FetchMoviesOnline(this.adapter, R.string.TMDB_popular_movies).execute(BuildUrl(R.string.TMDB_popular_movies))
            }
            R.id.top_rated_sort -> {
                FetchMoviesOnline(this.adapter, R.string.TMDB_top_rated_movies).execute(BuildUrl(R.string.TMDB_top_rated_movies))
            }
            else -> return super.onOptionsItemSelected(item)
        }
        return true
    }

    private fun calculateNoOfColumns(scalingFactor: Int = 200): Int {
        val displayMetrics = resources.displayMetrics
        val dpWidth = displayMetrics.widthPixels / displayMetrics.density
        val noOfColumns = (dpWidth / scalingFactor).toInt()
        return if (noOfColumns < 2) 2 else noOfColumns
    }


    private fun BuildUrl(int: Int): String {
        val str = StringBuilder(resources.getString(R.string.TMDB_URL))
        str.append(resources.getString(int))
        str.append(resources.getString(R.string.TMDB_API_Key))
        str.append(resources.getString(R.string.TMDB_Key_v3))
        MainActivity.MOVIES(int)?.let { str.append(resources.getString(R.string.TMDB_page_number) + (it.size / 20 + 1)) }
        return str.toString()
    }
}
