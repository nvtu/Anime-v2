package joker.anime_v2.LoadData;

import android.content.Context;
import android.util.Log;

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
        Elements film = doc.body().select("div.play").select("a.play_info");
        href = film.attr("href");
        String description = doc.body().select("div.noidungphim").select("p").text();
        delegate.processFinish(href, description);
        Log.d("abcd", href);
    }

    public interface LoadDataFilmResponse{
//        void processFinish(String href);
        void processFinish(String href, String description);
    }
}
