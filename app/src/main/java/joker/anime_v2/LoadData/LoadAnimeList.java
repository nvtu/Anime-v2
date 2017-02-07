package joker.anime_v2.LoadData;

import android.content.Context;
import android.util.Log;

import org.jsoup.Jsoup;
import org.jsoup.select.Elements;

import java.util.ArrayList;

import joker.anime_v2.ItemData.AnimeInfo;

/**
 * Created by Tu Van Ninh on 25/12/2016.
 */
public class LoadAnimeList extends LoadDataTask{

    ArrayList<AnimeInfo> animeList;
    public LoadAnimeListResponse delegate = null;

    public LoadAnimeList(Context context) {
        super(context);
        animeList = new ArrayList<>();
    }

    @Override
    protected void onPostExecute(String s) {
        super.onPostExecute(s);

        animeList.clear();
        org.jsoup.nodes.Document doc = Jsoup.parse(s);
        Elements film = doc.body().select("div.list-movie").select("div.items");
        for (org.jsoup.nodes.Element it: film){
//            String yearFilm = it.select("div.year-film").text();
//            String desFilm = it.select("div.des-film").text();
            String filmURL = it.select("a").attr("href");
            String name = it.select("a").attr("title");
            String imgURL = it.select("a").select("img").attr("src");
            String numEps = it.select("span.time").text();
            AnimeInfo animeInfo = new AnimeInfo(name, filmURL, numEps, imgURL, "", "");
            animeList.add(animeInfo);
            Log.d("abc", imgURL);
        }
        delegate.processFinish(animeList);
    }

    public interface LoadAnimeListResponse{
        void processFinish(ArrayList<AnimeInfo> animeList);
    }
}
