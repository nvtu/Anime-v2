package joker.anime_v2.LoadData;

import android.content.Context;

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
        Elements film = doc.body().select("div.film-block").select("div.col-film");
        for (org.jsoup.nodes.Element it: film){
            String yearFilm = it.select("div.year-film").text();
            String desFilm = it.select("div.des-film").text();
            String filmURL = it.select("div.w").select("a.thumb").attr("href");
            String name = it.select("div.w").select("a.thumb").attr("title");
            String imgURL = it.select("div.w").select("a.thumb").select("img").attr("src");
            String numEps = it.select("div.w").select("span.no").text();
            imgURL = imgURL.substring(imgURL.indexOf("&url=")+5, imgURL.length());
            AnimeInfo animeInfo = new AnimeInfo(name, filmURL, numEps, imgURL, desFilm, yearFilm);
            animeList.add(animeInfo);
        }
        delegate.processFinish(animeList);
    }

    public interface LoadAnimeListResponse{
        void processFinish(ArrayList<AnimeInfo> animeList);
    }
}
