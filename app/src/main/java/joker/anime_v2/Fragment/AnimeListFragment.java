package joker.anime_v2.Fragment;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.Toast;

import com.cleveroad.pulltorefresh.firework.FireworkyPullToRefreshLayout;

import java.util.ArrayList;

import joker.anime_v2.Adapter.RecycleAdapter;
import joker.anime_v2.ItemData.AnimeInfo;
import joker.anime_v2.LoadData.LoadAnimeList;
import joker.anime_v2.R;

/**
 * Created by Tu Van Ninh on 25/12/2016.
 */
public class AnimeListFragment extends Fragment implements LoadAnimeList.LoadAnimeListResponse{

    private static final int REFRESH_DELAY = 4500;
    ArrayList<AnimeInfo>[] animeList;
    RecycleAdapter[] adapter;
    int[] curPage;
    int curList;
    ArrayList<String> linkCategories, catergories;
    LoadAnimeList loadAnimeList;
    Context mContext;
    RecyclerView rv;
    ArrayAdapter madapter;
    FireworkyPullToRefreshLayout mPullRefreshView;

    private boolean mIsRefreshing;


    public AnimeListFragment(){
        animeList = new ArrayList[60];
        adapter = new RecycleAdapter[60];
        curPage = new int[60];
        linkCategories = new ArrayList<>();
        curList = 0;
        catergories = new ArrayList<>();

        linkCategories.add("http://m.animetvn.com/danh-sach/phim-moi.html?p=");
        linkCategories.add("http://m.animetvn.com/the-loai/hanh-dong.html?p=");
        linkCategories.add("http://m.animetvn.com/the-loai/phieu-luu.html?p=");
        linkCategories.add("http://m.animetvn.com/the-loai/hoc-duong.html?p=");
        linkCategories.add("http://m.animetvn.com/the-loai/hai-huoc.html?p=");
        linkCategories.add("http://m.animetvn.com/the-loai/doi-thuong.html?p=");
        linkCategories.add("http://m.animetvn.com/the-loai/trinh-tham.html?p=");
        linkCategories.add("http://m.animetvn.com/the-loai/drama.html?p=");
        linkCategories.add("http://m.animetvn.com/the-loai/ecchi.html?p=");
        linkCategories.add("http://m.animetvn.com/the-loai/harem.html?p=");
        linkCategories.add("http://m.animetvn.com/the-loai/gia-tuong.html?p=");
        linkCategories.add("http://m.animetvn.com/the-loai/hikikomori.html?p=");
        linkCategories.add("http://m.animetvn.com/the-loai/lich-su.html?p=");
        linkCategories.add("http://m.animetvn.com/the-loai/kinh-di.html?p=");
        linkCategories.add("http://m.animetvn.com/the-loai/tinh-tay-ba.html?p=");
        linkCategories.add("http://m.animetvn.com/the-loai/phep-thuat.html?p=");
        linkCategories.add("http://m.animetvn.com/the-loai/mecha.html?p=");
        linkCategories.add("http://m.animetvn.com/the-loai/am-nhac.html?p=");
        linkCategories.add("http://m.animetvn.com/the-loai/bi-an.html?p=");
        linkCategories.add("http://m.animetvn.com/the-loai/psychological.html?p=");
        linkCategories.add("http://m.animetvn.com/the-loai/tinh-yeu.html?p=");
        linkCategories.add("http://m.animetvn.com/the-loai/vu-tru.html?p=");
        linkCategories.add("http://m.animetvn.com/the-loai/the-thao.html?p=");
        linkCategories.add("http://m.animetvn.com/the-loai/tragedy.html?p=");
        linkCategories.add("http://m.animetvn.com/the-loai/ma-ca-rong.html?p=");
        linkCategories.add("http://m.animetvn.com/the-loai/sieu-nhien.html?p=");
        linkCategories.add("http://m.animetvn.com/the-loai/shoujo-ai.html?p=");
        linkCategories.add("http://m.animetvn.com/the-loai/shounen-ai.html?p=");
        linkCategories.add("http://m.animetvn.com/the-loai/yuri.html?p=");
        linkCategories.add("http://m.animetvn.com/the-loai/yaoi.html?p=");
        linkCategories.add("http://m.animetvn.com/the-loai/shoujo.html?p=");
        linkCategories.add("http://m.animetvn.com/the-loai/shounen.html?p=");
        linkCategories.add("http://m.animetvn.com/the-loai/live-action.html?p=");
        linkCategories.add("http://m.animetvn.com/the-loai/tokusatsu.html?p=");
        linkCategories.add("http://m.animetvn.com/the-loai/thriller.html?p=");
        linkCategories.add("http://m.animetvn.com/the-loai/sieu-nang-luc.html?p=");
        linkCategories.add("http://m.animetvn.com/the-loai/kids.html?p=");
        linkCategories.add("http://m.animetvn.com/the-loai/game.html?p=");
        linkCategories.add("http://m.animetvn.com/the-loai/hoan-doi-gioi-tinh.html?p=");
        linkCategories.add("http://m.animetvn.com/the-loai/dau-sung.html?p=");
        linkCategories.add("http://m.animetvn.com/the-loai/josei.html?p=");
        linkCategories.add("http://m.animetvn.com/the-loai/kemonomimi.html?p=");
        linkCategories.add("http://m.animetvn.com/the-loai/vo-thuat.html?p=");
        linkCategories.add("http://m.animetvn.com/the-loai/quan-doi.html?p=");
        linkCategories.add("http://m.animetvn.com/the-loai/ninja.html?p=");
        linkCategories.add("http://m.animetvn.com/the-loai/tieu-thuyet.html?p=");
        linkCategories.add("http://m.animetvn.com/the-loai/the-gioi-song-song.html?p=");
        linkCategories.add("http://m.animetvn.com/the-loai/parody.html?p=");
        linkCategories.add("http://m.animetvn.com/the-loai/police.html?p=");
        linkCategories.add("http://m.animetvn.com/the-loai/samurai.html?p=");
        linkCategories.add("http://m.animetvn.com/the-loai/vien-tuong.html?p=");
        linkCategories.add("http://m.animetvn.com/the-loai/japanese-drama.html?p=");
        linkCategories.add("http://m.animetvn.com/the-loai/bao-luc.html?p=");
        linkCategories.add("http://m.animetvn.com/the-loai/du-hanh-thoi-gian.html?p=");
        linkCategories.add("http://m.animetvn.com/the-loai/cartoon.html?p=");
        linkCategories.add("http://m.animetvn.com/the-loai/ac-quy.html?p=");
        linkCategories.add("http://m.animetvn.com/the-loai/thien-than.html?p=");
        linkCategories.add("http://m.animetvn.com/the-loai/seinen.html?p=");
        linkCategories.add("http://m.animetvn.com/the-loai/cars.html?p=");



        catergories.add("Trang Chủ");
        catergories.add("Hành Động");
        catergories.add("Phiêu Lưu");
        catergories.add("Học Đường");
        catergories.add("Hài Hước");
        catergories.add("Đời Thường");
        catergories.add("Trinh Thám");
        catergories.add("Drama");
        catergories.add("Ecchi");
        catergories.add("Harem");
        catergories.add("Giả Tưởng");
        catergories.add("Hikikomori");
        catergories.add("Lịch Sử");
        catergories.add("Kinh Dị");
        catergories.add("Tình Tay Ba");
        catergories.add("Phép Thuật");
        catergories.add("Mecha");
        catergories.add("Âm Nhạc");
        catergories.add("Bí Ẩn");
        catergories.add("Psychological");
        catergories.add("Tình Yêu");
        catergories.add("Vũ Trụ");
        catergories.add("Thể Thao");
        catergories.add("Tragedy");
        catergories.add("Ma Cà Rồng");
        catergories.add("Siêu Nhiên");
        catergories.add("Shoujo Ai");
        catergories.add("Shounen Ai");
        catergories.add("Yuri");
        catergories.add("Yaoi");
        catergories.add("Shoujo");
        catergories.add("Shounen");
        catergories.add("Live Action");
        catergories.add("Tokusatsu");
        catergories.add("Thriller");
        catergories.add("Siêu Năng Lực");
        catergories.add("Kids");
        catergories.add("Game");
        catergories.add("Hoán Đổi Giới Tính");
        catergories.add("Đấu Súng");
        catergories.add("Josei");
        catergories.add("Kemonomimi");
        catergories.add("Võ Thuật");
        catergories.add("Quân Đội");
        catergories.add("Ninja");
        catergories.add("Tiểu Thuyết");
        catergories.add("Thế Giới Song Song");
        catergories.add("Parody");
        catergories.add("Police");
        catergories.add("Samurai");
        catergories.add("Viễn Tưởng");
        catergories.add("Japanese Drama");
        catergories.add("Bạo Lực");
        catergories.add("Du Hành Thời Gian");
        catergories.add("Cartoon");
        catergories.add("Ác Quỷ");
        catergories.add("Thiên Thần");
        catergories.add("Seinen");
        catergories.add("Cars");


        for (int i=0; i<60; i++){
            animeList[i] = new ArrayList<>();
            curPage[i] = 1;
        }
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.layout_anime_list, container, false);
        mContext = rootView.getContext();
        initFireWorksLayout(rootView);
        initComponents();
        initLayouts(rootView);
        setHasOptionsMenu(true);
        return rootView;
    }

    private void loadData(){
        loadAnimeList = new LoadAnimeList(mContext);
        loadAnimeList.delegate = this;
        loadAnimeList.execute(linkCategories.get(curList) + String.valueOf(curPage[curList]));
    }

    private void initComponents() {
        madapter = new ArrayAdapter(mContext, R.layout.spinner_item, catergories);
        for (int i=0; i<60; i++){
            adapter[i] = new RecycleAdapter(mContext, animeList[i]);
        }
        if (animeList[curList].size() == 0){
            loadData();
        }

    }

    private void initFireWorksLayout(View rootView){
        mPullRefreshView = (FireworkyPullToRefreshLayout) rootView.findViewById(R.id.pullToRefresh);
        mPullRefreshView.setOnRefreshListener(new FireworkyPullToRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                mIsRefreshing = true;
                mPullRefreshView.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        mPullRefreshView.setRefreshing(mIsRefreshing = false);
                        loadData();
                    }
                }, REFRESH_DELAY);
            }
        });
    }

    private void initLayouts(View rootView) {
        rv = (RecyclerView) rootView.findViewById(R.id.rvs);
        GridLayoutManager gridLayoutManager = new GridLayoutManager(rootView.getContext(), 2);
        rv.setHasFixedSize(true);
        rv.setLayoutManager(gridLayoutManager);
        rv.setAdapter(adapter[curList]);
    }

    @Override
    public void processFinish(ArrayList<AnimeInfo> animeList) {
        if (animeList.size() > 0){
            this.animeList[curList].clear();
            this.animeList[curList].addAll(animeList);
            adapter[curList].notifyDataSetChanged();
            rv.setAdapter(adapter[curList]);
        }
        else Toast.makeText(mContext, "You're already in the last page!", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.anime_menu, menu);
        MenuItem item = menu.findItem(R.id.spinner);
        Spinner spinner = (Spinner) MenuItemCompat.getActionView(item);
        spinner.setAdapter(madapter);
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                curList = position;
                loadData();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        switch (id) {
            case R.id.back:
                // do stuff, like showing settings fragment
                if (curPage[curList] > 1){
                    curPage[curList]--;
                    loadData();
                }
                else Toast.makeText(mContext, "You're already in the first page!", Toast.LENGTH_SHORT).show();
                return true;
            case R.id.next:
                curPage[curList]++;
                loadData();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onResume() {
        super.onResume();
    }
}
