package udacity.nsr.osama.movieapp.online


import android.content.Context
import android.support.v4.content.AsyncTaskLoader
import org.json.JSONObject
import udacity.nsr.osama.movieapp.models.ReviewModel
import java.net.URL


internal class FetchReviewsAndTrailersOnline(context: Context, val link: String) :
        AsyncTaskLoader<Pair<ArrayList<String>, ArrayList<ReviewModel>>>(context) {

    companion object {
        const val LOADER_ID = 1
        const val URL_KEY = "URL_KEY"
    }

    override fun onStartLoading() {
        if (link.isEmpty()) return
        forceLoad()
    }

    // COMPLETED (9) Override loadInBackground
    override fun loadInBackground(): Pair<ArrayList<String>, ArrayList<ReviewModel>>? {
        val jsonString = NetworkHelper.getJsonStringFrom(URL(link))
        return decodeJson(jsonString)
    }

    private fun decodeJson(jsonString: String?): Pair<ArrayList<String>, ArrayList<ReviewModel>> {
        val pair = Pair(ArrayList<String>(), ArrayList<ReviewModel>())
        if (jsonString == null) return pair
        val jsonObject = JSONObject(jsonString)
        val videosJsonArray = jsonObject.getJSONObject("videos").getJSONArray("results")!!
        val reviewsJsonArray = jsonObject.getJSONObject("reviews").getJSONArray("results")!!
        for (i in 0 until videosJsonArray.length()) {
            pair.first += videosJsonArray.getJSONObject(i).getString("key")
        }
        for (i in 0 until reviewsJsonArray.length()) {
            val x = reviewsJsonArray.getJSONObject(i)
            pair.second += ReviewModel(x.getString("author"), x.getString("content"))
        }
        return pair
    }
}
