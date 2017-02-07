package joker.anime_v2;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import joker.anime_v2.DataBase.SQLiteHelper;
import joker.anime_v2.Fragment.HistoryFragment;
import joker.anime_v2.ItemData.AnimeInfo;
import joker.anime_v2.LoadData.LoadDataFilm;

public class DetailActivity extends AppCompatActivity implements LoadDataFilm.LoadDataFilmResponse{

    private CollapsingToolbarLayout collapsingToolbar;
    private ImageView userCoverView;
    private AppBarLayout appBarLayout;
    AnimeInfo animeInfo;
    TextView animeTitle, numEps, tvDescription;
    FloatingActionButton playButton;
    LoadDataFilm loadDataFilm;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        animeInfo = (AnimeInfo) getIntent().getSerializableExtra("AnimeInformation");
        loadData();
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

    }

    private void loadData(){
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
        playButton = (FloatingActionButton) findViewById(R.id.playButton);
        Picasso.with(this).load(animeInfo.getImgURL()).into(userCoverView);
        animeTitle.setText(animeInfo.getName());
//        tvDescription.setText(animeInfo.getDesFilm());
        numEps.setText(animeInfo.getNumEps());

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

    @Override
    public void processFinish(final String href, String description) {
        tvDescription.setText(description);
        animeInfo.setDesFilm(description);
        playButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(DetailActivity.this, WatchAnimeActivity.class);
                intent.putExtra("animeURL", href);
                intent.putExtra("animeInfo", animeInfo);
                startActivity(intent);
                finish();
            }
        });

    }
}
