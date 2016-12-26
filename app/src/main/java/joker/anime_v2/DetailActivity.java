package joker.anime_v2;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import joker.anime_v2.ItemData.AnimeInfo;
import joker.anime_v2.LoadData.LoadDataFilm;

public class DetailActivity extends AppCompatActivity implements LoadDataFilm.LoadDataFilmResponse{

    private CollapsingToolbarLayout collapsingToolbar;
    private ImageView userCoverView;
    private AppBarLayout appBarLayout;
    AnimeInfo animeInfo;
    TextView animeTitle, numEps, tvDescription;
    Button playButton;
    LoadDataFilm loadDataFilm;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        animeInfo = (AnimeInfo) getIntent().getSerializableExtra("AnimeInformation");
        initLayouts();
        initListeners();
    }

    private void initListeners() {
        collapsingToolbar.setTitle(" ");
        appBarLayout.addOnOffsetChangedListener(new AppBarLayout.OnOffsetChangedListener() {
            boolean isShow = false;
            int scrollRange = -1;

            @Override
            public void onOffsetChanged(AppBarLayout appBarLayout, int verticalOffset) {
                if (scrollRange == -1) {
                    scrollRange = appBarLayout.getTotalScrollRange();
                }
                if (scrollRange + verticalOffset == 0) {
                    collapsingToolbar.setTitle(animeInfo.getName());
                    isShow = true;
                } else if (isShow) {
                    collapsingToolbar.setTitle(" ");//carefull there should a space between double quote otherwise it wont work
                    isShow = false;
                }
            }
        });
        playButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loadData();
            }
        });
    }

    void loadData(){
        loadDataFilm = new LoadDataFilm(this);
        loadDataFilm.delegate = this;
        loadDataFilm.execute(animeInfo.getAnimeURL());
    }

    private void initLayouts() {
        collapsingToolbar = (CollapsingToolbarLayout) findViewById(R.id.collapsing_toolbar);
        appBarLayout = (AppBarLayout) findViewById(R.id.appBarLayout);
        animeTitle = (TextView) findViewById(R.id.animeTitle);
        tvDescription = (TextView) findViewById(R.id.animeDescription);
        numEps = (TextView) findViewById(R.id.episode);
        userCoverView = (ImageView) findViewById(R.id.anime_cover);
        playButton = (Button) findViewById(R.id.playButton);
        Picasso.with(this).load(animeInfo.getImgURL()).into(userCoverView);
        animeTitle.setText(animeInfo.getName());
        tvDescription.setText(animeInfo.getDesFilm());
        numEps.setText(animeInfo.getNumEps());

    }

    @Override
    public void processFinish(String href) {
        Intent intent = new Intent(this, WatchAnimeActivity.class);
        intent.putExtra("animeURL", href);
        startActivity(intent);
    }
}
