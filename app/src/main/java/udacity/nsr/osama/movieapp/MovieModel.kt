package udacity.nsr.osama.movieapp

data class MovieModel(val originalTitle: String,
                      val summary: String,
                      val poster: String,
                      val backdrop_path: String,
                      val releaseDate: String,
                      var genres: List<Int>,
                      val rating: Double,
                      val votersCount: Int,
                      val video: Boolean,
                      val isAdult: Boolean)
