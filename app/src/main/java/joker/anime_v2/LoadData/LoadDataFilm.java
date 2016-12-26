package joker.anime_v2.LoadData;

import android.content.Context;

import org.jsoup.Jsoup;
import org.jsoup.select.Elements;

/**
 * Created by Tu Van Ninh on 25/12/2016.
 */
public class LoadDataFilm extends LoadDataTask {

    String href;
    public LoadDataFilmResponse delegate = null;

    public LoadDataFilm(Context context) {
        super(context);
    }

    @Override
    protected void onPostExecute(String s) {
        super.onPostExecute(s);
        org.jsoup.nodes.Document doc = Jsoup.parse(s);
        Elements film = doc.body().select("a.button_film_watch");
        href = film.attr("href");
        delegate.processFinish(href);
    }

    public interface LoadDataFilmResponse{
        void processFinish(String href);
    }
}
