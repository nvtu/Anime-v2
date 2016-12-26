package joker.anime_v2;

import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.TextView;

import com.example.tuvanninh.universalvideoview.UniversalMediaController;
import com.example.tuvanninh.universalvideoview.UniversalVideoView;

import java.util.ArrayList;

import joker.anime_v2.Adapter.EpisodeAdapter;
import joker.anime_v2.LoadData.LoadEpisodeTask;

public class WatchAnimeActivity extends AppCompatActivity implements LoadEpisodeTask.LoadEpisodeTaskResponse, UniversalVideoView.VideoViewCallback{

    private static final String TAG = "WatchingFilmActivity";
    String filmURL;
    EpisodeAdapter epsAdapter;
    ArrayList<String> listEps;
    LoadEpisodeTask loadEpisodeTask;
    GridView gridView;
    UniversalVideoView mVideoView;
    UniversalMediaController mMediaController;
    View mVideoLayout;
    private int mSeekPosition = 0;
    private int cachedHeight;
    private boolean isFullscreen;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_watch_anime);
        filmURL = getIntent().getStringExtra("animeURL");
        initLayout();

    }

    private void initLayout() {
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


}
