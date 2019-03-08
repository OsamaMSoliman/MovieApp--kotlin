package udacity.nsr.osama.movieapp.online

import java.net.HttpURLConnection
import java.net.URL
import java.util.*

class NetworkHelper {
    companion object {
        fun getJsonStringFrom(url: URL): String? {
            val urlConnection = url.openConnection() as HttpURLConnection
            try {
                val inStream = urlConnection.inputStream
                val scanner = Scanner(inStream).useDelimiter("\\A")
                return if (scanner.hasNext()) scanner.next() else null
            } finally {
                urlConnection.disconnect()
            }
        }
    }
}