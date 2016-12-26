package joker.anime_v2.ItemData;

import java.io.Serializable;

/**
 * Created by Tu Van Ninh on 25/12/2016.
 */
public class AnimeInfo implements Serializable {
    String name;
    String animeURL;
    String numEps;
    String imgURL;
    String desFilm;
    String yearFilm;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAnimeURL() {
        return animeURL;
    }

    public void setAnimeURL(String animeURL) {
        this.animeURL = animeURL;
    }

    public String getNumEps() {
        return numEps;
    }

    public void setNumEps(String numEps) {
        this.numEps = numEps;
    }

    public String getImgURL() {
        return imgURL;
    }

    public void setImgURL(String imgURL) {
        this.imgURL = imgURL;
    }

    public String getDesFilm() {
        return desFilm;
    }

    public void setDesFilm(String desFilm) {
        this.desFilm = desFilm;
    }

    public String getYearFilm() {
        return yearFilm;
    }

    public void setYearFilm(String yearFilm) {
        this.yearFilm = yearFilm;
    }

    public AnimeInfo(String name, String animeURL, String numEps, String imgURL, String desFilm, String yearFilm) {

        this.name = name;
        this.animeURL = animeURL;
        this.numEps = numEps;
        this.imgURL = imgURL;
        this.desFilm = desFilm;
        this.yearFilm = yearFilm;
    }
}
