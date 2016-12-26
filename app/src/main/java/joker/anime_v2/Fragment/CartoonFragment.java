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
public class CartoonFragment extends Fragment implements LoadAnimeList.LoadAnimeListResponse{

    private static final int REFRESH_DELAY = 4500;
    ArrayList<AnimeInfo>[] animeList;
    RecycleAdapter[] adapter;
    int[] curPage;
    int curList;
    ArrayList<String> linkCategories;
    LoadAnimeList loadCartoonList;
    private Context mContext;
    private ArrayList<String> catergories;
    RecyclerView rv;
    private FireworkyPullToRefreshLayout mPullRefreshView;

    private boolean mIsRefreshing;


    public CartoonFragment(){
        animeList = new ArrayList[10];
        adapter = new RecycleAdapter[10];
        curPage = new int[10];
        linkCategories = new ArrayList<>();
        curList = 0;
        catergories = new ArrayList<>();

        linkCategories.add("http://animehay.com/the-loai/phim-hoat-hinh?page=");
        linkCategories.add("http://animehay.com/the-loai/phim-hoat-hinh/viet-nam?page=");
        linkCategories.add("http://animehay.com/the-loai/phim-hoat-hinh/my?page=");
        linkCategories.add("http://animehay.com/the-loai/phim-hoat-hinh/trung-quoc?page=");
        linkCategories.add("http://animehay.com/the-loai/phim-hoat-hinh/han-quoc?page=");
        linkCategories.add("http://animehay.com/the-loai/phim-hoat-hinh/malaysia?page=");
        linkCategories.add("http://animehay.com/the-loai/phim-hoat-hinh/anh?page=");
        linkCategories.add("http://animehay.com/the-loai/phim-hoat-hinh/phap?page=");
        linkCategories.add("http://animehay.com/the-loai/phim-hoat-hinh/dai-loan?page=");
        linkCategories.add("http://animehay.com/the-loai/phim-hoat-hinh/khac?page=");

        catergories.add("Trang Chủ");
        catergories.add("Việt Nam");
        catergories.add("Mỹ");
        catergories.add("Trung Quốc");
        catergories.add("Hàn Quốc");
        catergories.add("Malaysia");
        catergories.add("Anh");
        catergories.add("Pháp");
        catergories.add("Đài Loan");
        catergories.add("Khác");

        for (int i=0; i<10; i++){
            animeList[i] = new ArrayList<>();
            curPage[i] = 1;
        }
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.layout_cartoon_list, container, false);
        mContext = rootView.getContext();
        initFireWorksLayout(rootView);
        initComponents(rootView);
        initLayouts(rootView);
        setHasOptionsMenu(true);
        return rootView;
    }

    private void loadData(){
        loadCartoonList = new LoadAnimeList(mContext);
        loadCartoonList.delegate = this;
        loadCartoonList.execute(linkCategories.get(curList) + String.valueOf(curPage[curList]));
    }

    private void initFireWorksLayout(View rootView){
        mPullRefreshView = (FireworkyPullToRefreshLayout) rootView.findViewById(R.id.fireWorkRefresh);
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

    private void initComponents(View rootView) {
        for (int i=0; i<10; i++){
            adapter[i] = new RecycleAdapter(rootView.getContext(), animeList[i]);
        }
        if (animeList[curList].size() == 0) loadData();
    }

    private void initLayouts(View rootView) {
        rv = (RecyclerView) rootView.findViewById(R.id.rvc);
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
        inflater.inflate(R.menu.cartoon_menu, menu);
        MenuItem item = menu.findItem(R.id.spinnerct);
        Spinner spinner = (Spinner) MenuItemCompat.getActionView(item);
        ArrayAdapter adapter = new ArrayAdapter(mContext, R.layout.spinner_item, catergories);
        spinner.setAdapter(adapter);
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
            case R.id.backct:
                // do stuff, like showing settings fragment
                if (curPage[curList] > 1){
                    curPage[curList]--;
                    loadData();
                }
                else Toast.makeText(mContext, "You're already in the first page!", Toast.LENGTH_SHORT).show();
                return true;
            case R.id.nextct:
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
