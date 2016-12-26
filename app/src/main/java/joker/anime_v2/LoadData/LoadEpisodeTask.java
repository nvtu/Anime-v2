package joker.anime_v2.LoadData;

import android.content.Context;

import org.jsoup.Jsoup;
import org.jsoup.select.Elements;

import java.util.ArrayList;

/**
 * Created by Tu Van Ninh on 25/12/2016.
 */
public class LoadEpisodeTask extends LoadDataTask {

    ArrayList<String> epsList;
    String movieURL;
    boolean firstLoad;
    public LoadEpisodeTaskResponse delegate = null;

    public LoadEpisodeTask(Context context, boolean firstLoad) {
        super(context);
        this.firstLoad = firstLoad;
        epsList = new ArrayList<>();
    }

    @Override
    protected void onPostExecute(String s) {
        super.onPostExecute(s);
        org.jsoup.nodes.Document doc = Jsoup.parse(s);
        String body = doc.body().toString();
        int start = body.indexOf("file: \"") + 7;
        int end = body.indexOf("\",label:", start);
        if (start < 0 || end < 0) return;
        movieURL = body.substring(start, end);

        if (firstLoad) {
            Elements eps = doc.body().select("div.ep_film").select("a");
            for (org.jsoup.nodes.Element it : eps) {
                String epsInfo = it.attr("href");
                epsList.add(epsInfo);
            }
            firstLoad = false;
            delegate.processFinish(epsList, movieURL);
        }
        else delegate.processFinish(movieURL);
    }

    public interface LoadEpisodeTaskResponse{
        void processFinish(ArrayList<String> epsList, String movieURL);
        void processFinish(String movieURL);
    }
}
