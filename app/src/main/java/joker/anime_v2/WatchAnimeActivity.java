package joker.anime_v2;

import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.tuvanninh.universalvideoview.UniversalMediaController;
import com.example.tuvanninh.universalvideoview.UniversalVideoView;

import java.util.ArrayList;

import joker.anime_v2.Adapter.EpisodeAdapter;
import joker.anime_v2.DataBase.SQLiteHelper;
import joker.anime_v2.Fragment.HistoryFragment;
import joker.anime_v2.ItemData.AnimeInfo;
import joker.anime_v2.LoadData.LoadEpisodeTask;

public class WatchAnimeActivity extends AppCompatActivity implements LoadEpisodeTask.LoadEpisodeTaskResponse, UniversalVideoView.VideoViewCallback{

    private static final String TAG = "WatchingFilmActivity";
    String filmURL;
    EpisodeAdapter epsAdapter;
    ArrayList<String> listEps;
    LoadEpisodeTask loadEpisodeTask;
    GridView gridView;
    ImageView gotoFilm;
    EditText inpText;
    UniversalVideoView mVideoView;
    UniversalMediaController mMediaController;
    View mVideoLayout;
    AnimeInfo animeInfo;
    private int mSeekPosition = 0;
    private int cachedHeight;
    private int curEps = 0;
    private boolean isFullscreen;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_watch_anime);
        filmURL = getIntent().getStringExtra("animeURL");
        animeInfo = (AnimeInfo) getIntent().getSerializableExtra("animeInfo");
        initLayout();

    }

    private void initLayout() {
        inpText = (EditText) findViewById(R.id.episode);
        gotoFilm = (ImageView) findViewById(R.id.go);
        gotoFilm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int position = Integer.valueOf(inpText.getText().toString());
                if (position > 0 && position < listEps.size() + 1){
                    curEps = position - 1;
                    loadData(listEps.get(position - 1));
                }
                else Toast.makeText(WatchAnimeActivity.this, "Your input is invalid", Toast.LENGTH_SHORT).show();
            }
        });
        loadEpisodeTask = new LoadEpisodeTask(this,true);
        loadEpisodeTask.delegate = this;
        gridView = (GridView) findViewById(R.id.listEps);
        listEps = new ArrayList<>();
        epsAdapter = new EpisodeAdapter(this, R.layout.item_eps, listEps);
        gridView.setAdapter(epsAdapter);
        gridView.setNumColumns(5);
        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                TextView vEps = (TextView) view.findViewById(R.id.epsInfo);
                vEps.setTextColor(getResources().getColor(R.color.red));
                curEps = position;
                loadData(listEps.get(position));
            }
        });
        loadEpisodeTask.execute(filmURL);
        mVideoLayout = findViewById(R.id.video_layout);
        mVideoView = (UniversalVideoView) findViewById(R.id.videoView);
        mMediaController = (UniversalMediaController) findViewById(R.id.media_controller);
        mVideoView.setMediaController(mMediaController);
        mVideoView.setVideoViewCallback(this);
        setVideoAreaSize();

        mVideoView.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mp) {
            }
        });
    }

    private void loadData(String url){
        loadEpisodeTask = new LoadEpisodeTask(this,false);
        loadEpisodeTask.delegate = this;
        loadEpisodeTask.execute(url);
    }

    private void setVideoAreaSize() {
        mVideoLayout.post(new Runnable() {
            @Override
            public void run() {
                int width = mVideoLayout.getWidth();
                cachedHeight = (int) (width * 405f / 720f);
                ViewGroup.LayoutParams videoLayoutParams = mVideoLayout.getLayoutParams();
                videoLayoutParams.width = ViewGroup.LayoutParams.MATCH_PARENT;
                videoLayoutParams.height = cachedHeight;
                mVideoLayout.setLayoutParams(videoLayoutParams);
            }
        });
    }

    private void startFilm(final String filmPath){
        SQLiteHelper db = new SQLiteHelper(this);
        if (db.checkRecordExist(animeInfo.getName(), 0)){
            db.updateHistory(animeInfo.getName(), String.valueOf(curEps+1), animeInfo.getAnimeURL());
            HistoryFragment.animeList.clear();
            HistoryFragment.animeList.addAll(db.getAllHistory());
            HistoryFragment.adapter.notifyDataSetChanged();
        }
        else db.insertHistory(animeInfo, String.valueOf(curEps+1), animeInfo.getAnimeURL());
        db.close();
        final Handler handler = new Handler();
        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        mVideoView.setVideoPath(filmPath);
                        mSeekPosition = 0;
                        mVideoView.start();
                    }
                });
            }
        };
        new Thread(runnable).start();
    }

    @Override
    protected void onPause() {
        super.onPause();
        Log.d(TAG, "onPause ");
        if (mVideoView != null) {
            mSeekPosition = mVideoView.getCurrentPosition();
            mVideoView.pause();
        }

    }

    @Override
    protected void onResume() {
        super.onResume();
        if (mVideoView != null && mSeekPosition >= 0){
            mVideoView.seekTo(mSeekPosition);
            mVideoView.start();
        }
    }

    @Override
    public void onScaleChange(boolean isFullscreen) {
        this.isFullscreen = isFullscreen;

        if (isFullscreen) {
            ViewGroup.LayoutParams layoutParams = mVideoLayout.getLayoutParams();
            layoutParams.width = ViewGroup.LayoutParams.MATCH_PARENT;
            layoutParams.height = ViewGroup.LayoutParams.MATCH_PARENT;
            mVideoLayout.setLayoutParams(layoutParams);


        } else {
            ViewGroup.LayoutParams layoutParams = mVideoLayout.getLayoutParams();
            layoutParams.width = ViewGroup.LayoutParams.MATCH_PARENT;
            layoutParams.height = this.cachedHeight;
            mVideoLayout.setLayoutParams(layoutParams);

        }

        switchTitleBar(!isFullscreen);
    }

    private void switchTitleBar(boolean show) {

        android.support.v7.app.ActionBar supportActionBar = getSupportActionBar();
        if (supportActionBar != null) {
            if (show) {
                supportActionBar.show();
            } else {
                supportActionBar.hide();
            }
        }
    }

    @Override
    public void onPause(MediaPlayer mediaPlayer) {
        Log.d(TAG, "onPause UniversalVideoView callback");
        mSeekPosition = mediaPlayer.getCurrentPosition();
    }

    @Override
    public void onStart(MediaPlayer mediaPlayer) {
        Log.d(TAG, "onStart UniversalVideoView callback");
        mediaPlayer.seekTo(mSeekPosition);
    }

    @Override
    public void onBufferingStart(MediaPlayer mediaPlayer) {
        Log.d(TAG, "onBufferingStart UniversalVideoView callback");
    }

    @Override
    public void onBufferingEnd(MediaPlayer mediaPlayer) {
        Log.d(TAG, "onBufferingEnd UniversalVideoView callback");

    }

    @Override
    public void onBackPressed() {
        if (this.isFullscreen) {
            mVideoView.setFullscreen(false);
        } else {
            Intent intent = new Intent(this, DetailActivity.class);
            intent.putExtra("AnimeInformation", animeInfo);
            startActivity(intent);
            finish();
            super.onBackPressed();
        }
    }

    @Override
    public void processFinish(ArrayList<String> epsList, String movieURL) {
        listEps.addAll(epsList);
        epsAdapter.notifyDataSetChanged();
        startFilm(movieURL);
    }

    @Override
    public void processFinish(String movieURL) {
        startFilm(movieURL);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.love_menu, menu);
        MenuItem item = menu.getItem(0);
        int itemId = item.getItemId();
        if (itemId == R.id.love){
            SQLiteHelper db = new SQLiteHelper(this);
            if (db.checkRecordExist(animeInfo.getName(), 1)){
                item.setIcon(R.drawable.loveselect);
            }
            else{
                item.setIcon(R.drawable.lovenotselect);
            }
            db.close();
        }
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int itemId = item.getItemId();
        if (itemId == R.id.love){
            SQLiteHelper db = new SQLiteHelper(this);
            if (db.checkRecordExist(animeInfo.getName(), 1)){
                db.deleteFromFavorite(animeInfo.getName());
                HistoryFragment.animeList.clear();
                HistoryFragment.animeList.addAll(db.getAllFavorite());
                HistoryFragment.adapter.notifyDataSetChanged();
                item.setIcon(R.drawable.lovenotselect);
            }
            else{
                db.insertFavorite(animeInfo);
                item.setIcon(R.drawable.loveselect);
            }
            db.close();
        }
        return super.onOptionsItemSelected(item);
    }


}
