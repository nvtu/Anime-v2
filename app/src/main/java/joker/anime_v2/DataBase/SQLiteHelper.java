package joker.anime_v2.DataBase;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;

import joker.anime_v2.ItemData.AnimeInfo;

/**
 * Created by Tu Van Ninh on 26/12/2016.
 */
public class SQLiteHelper extends SQLiteOpenHelper {

    private static final int DATABASE_VERSION = 1;
    public static final String DATABASE_NAME = "SQLiteMovieDataBase.db";
    public static final String TABLE_NAME[] = {"HISTORY", "FAVORITE"};
    public static final String COLUMN_MOVIE_NAME = "MOVIE_NAME";
    public static final String COLUMN_NUM_EPS = "NUM_EPS";
    public static final String COLUMN_IMAGE_LINK = "IMAGE_LINKS";
    public static final String COLUMN_MOVIE_LINK = "MOVIE_LINKS";
    public static final String COLUMN_DESCRIPTION = "DESCRIPTION";

    public SQLiteHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }


    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE " + TABLE_NAME[0] + " ( " + COLUMN_MOVIE_NAME + " NVARCHAR PRIMARY KEY, " + COLUMN_NUM_EPS + " NVARCHAR, " +
        COLUMN_IMAGE_LINK + " NVARCHAR, " + COLUMN_MOVIE_LINK + " NVARCHAR, " + COLUMN_DESCRIPTION + " NVARCHAR)");
        db.execSQL("CREATE TABLE " + TABLE_NAME[1] + " ( " + COLUMN_MOVIE_NAME + " NVARCHAR PRIMARY KEY, " + COLUMN_NUM_EPS + " NVARCHAR, " +
                COLUMN_IMAGE_LINK + " NVARCHAR, " + COLUMN_MOVIE_LINK + " NVARCHAR, " + COLUMN_DESCRIPTION + " NVARCHAR)");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }

    public boolean updateHistory(String movieName, String curEps, String curLink){
        if (!checkRecordExist(movieName, 0)) return false;
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put(COLUMN_NUM_EPS, curEps);
        cv.put(COLUMN_MOVIE_LINK, curLink);
        db.update(TABLE_NAME[0], cv, COLUMN_MOVIE_NAME + " = ?", new String[]{movieName});
        return true;
    }

    public boolean checkRecordExist(String movieName, int tableId){
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM " + TABLE_NAME[tableId] + " WHERE " + COLUMN_MOVIE_NAME + " = ?", new String[] {movieName});
        boolean ans = cursor.getCount() > 0;
        cursor.close();
        return ans;
    }

    public boolean insertHistory(AnimeInfo animeInfo, String curEps, String curLink){
        if (checkRecordExist(animeInfo.getName(), 0)) return false;
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put(COLUMN_MOVIE_NAME, animeInfo.getName());
        cv.put(COLUMN_IMAGE_LINK, animeInfo.getImgURL());
        cv.put(COLUMN_MOVIE_LINK, curLink);
        cv.put(COLUMN_NUM_EPS, curEps);
        cv.put(COLUMN_DESCRIPTION, animeInfo.getDesFilm());
        db.insert(TABLE_NAME[0], null, cv);
        return true;
    }

    public boolean insertFavorite(AnimeInfo animeInfo){
        if (checkRecordExist(animeInfo.getName(), 1)) return false;
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put(COLUMN_MOVIE_NAME, animeInfo.getName());
        cv.put(COLUMN_IMAGE_LINK, animeInfo.getImgURL());
        cv.put(COLUMN_MOVIE_LINK, animeInfo.getAnimeURL());
        cv.put(COLUMN_NUM_EPS, animeInfo.getNumEps());
        cv.put(COLUMN_DESCRIPTION, animeInfo.getDesFilm());
        db.insert(TABLE_NAME[1], null, cv);
        return true;
    }

    public int deleteFromHistory(String movieName){
        SQLiteDatabase db = this.getWritableDatabase();
        return db.delete(TABLE_NAME[0], COLUMN_MOVIE_NAME + " = ?", new String[] {movieName});
    }

    public int deleteFromFavorite(String movieName){
        SQLiteDatabase db = this.getWritableDatabase();
        return db.delete(TABLE_NAME[1], COLUMN_MOVIE_NAME + " = ?", new String[] {movieName});
    }

    public void deleteAllHistory(){
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL("DELETE FROM " + TABLE_NAME[0]);
    }

    public void deleteAllFavorite(){
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL("DELETE FROM " + TABLE_NAME[1]);
    }

    public ArrayList<AnimeInfo> getAllFavorite(){
        ArrayList<AnimeInfo> animeList = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM " + TABLE_NAME[1], null);
        cursor.moveToFirst();
        while (!cursor.isAfterLast()){
            String name = cursor.getString(cursor.getColumnIndex(COLUMN_MOVIE_NAME)),
                    imgURL = cursor.getString(cursor.getColumnIndex(COLUMN_IMAGE_LINK)),
                    animeURL = cursor.getString(cursor.getColumnIndex(COLUMN_MOVIE_LINK)),
                    desc = cursor.getString(cursor.getColumnIndex(COLUMN_DESCRIPTION)),
                    numEps = cursor.getString(cursor.getColumnIndex(COLUMN_NUM_EPS));
            animeList.add(new AnimeInfo(name, animeURL, numEps, imgURL, desc, ""));
            cursor.moveToNext();
        }
        cursor.close();
        db.close();
        return animeList;
    }

    public ArrayList<AnimeInfo> getAllHistory(){
        ArrayList<AnimeInfo> animeList = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM " + TABLE_NAME[0], null);
        cursor.moveToFirst();
        while (!cursor.isAfterLast()){
            String name = cursor.getString(cursor.getColumnIndex(COLUMN_MOVIE_NAME)),
                    imgURL = cursor.getString(cursor.getColumnIndex(COLUMN_IMAGE_LINK)),
                    animeURL = cursor.getString(cursor.getColumnIndex(COLUMN_MOVIE_LINK)),
                    numEps = cursor.getString(cursor.getColumnIndex(COLUMN_NUM_EPS)),
                    desc = cursor.getString(cursor.getColumnIndex(COLUMN_DESCRIPTION));
            animeList.add(new AnimeInfo(name, animeURL, numEps, imgURL, desc, ""));
            cursor.moveToNext();
        }
        cursor.close();
        db.close();
        return animeList;
    }
}
