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

import java.util.ArrayList;

import joker.anime_v2.Adapter.RecycleAdapter;
import joker.anime_v2.ItemData.AnimeInfo;
import joker.anime_v2.LoadData.LoadAnimeList;
import joker.anime_v2.R;

/**
 * Created by Tu Van Ninh on 25/12/2016.
 */
public class AnimeListFragment extends Fragment implements LoadAnimeList.LoadAnimeListResponse{

    ArrayList<AnimeInfo>[] animeList;
    RecycleAdapter[] adapter;
    int[] curPage;
    int curList;
    ArrayList<String> linkCategories, catergories;
    LoadAnimeList loadAnimeList;
    Context mContext;
    RecyclerView rv;
    ArrayAdapter madapter;

    public AnimeListFragment(){
        animeList = new ArrayList[41];
        adapter = new RecycleAdapter[41];
        curPage = new int[41];
        linkCategories = new ArrayList<>();
        curList = 0;
        catergories = new ArrayList<>();

        linkCategories.add("http://animehay.com/the-loai/phim-anime?page=");
        linkCategories.add("http://animehay.com/the-loai/phim-anime/hanh-dong?page=");
        linkCategories.add("http://animehay.com/the-loai/phim-anime/tinh-cam?page=");
        linkCategories.add("http://animehay.com/the-loai/phim-anime/lich-su?page=");
        linkCategories.add("http://animehay.com/the-loai/phim-anime/hai-huoc?page=");
        linkCategories.add("http://animehay.com/the-loai/phim-anime/vien-tuong?page=");
        linkCategories.add("http://animehay.com/the-loai/phim-anime/vo-thuat?page=");
        linkCategories.add("http://animehay.com/the-loai/phim-anime/gia-tuong?page=");
        linkCategories.add("http://animehay.com/the-loai/phim-anime/kinh-di?page=");
        linkCategories.add("http://animehay.com/the-loai/phim-anime/phieu-luu?page=");
        linkCategories.add("http://animehay.com/the-loai/phim-anime/hoc-duong?page=");
        linkCategories.add("http://animehay.com/the-loai/phim-anime/doi-thuong?page=");
        linkCategories.add("http://animehay.com/the-loai/phim-anime/sieu-nhien?page=");
        linkCategories.add("http://animehay.com/the-loai/phim-anime/harem?page=");
        linkCategories.add("http://animehay.com/the-loai/phim-anime/ecchi?page=");
        linkCategories.add("http://animehay.com/the-loai/phim-anime/shounen?page=");
        linkCategories.add("http://animehay.com/the-loai/phim-anime/phep-thuat?page=");
        linkCategories.add("http://animehay.com/the-loai/phim-anime/tro-choi?page=");
        linkCategories.add("http://animehay.com/the-loai/phim-anime/tham-tu?page=");
        linkCategories.add("http://animehay.com/the-loai/phim-anime/mystery?page=");
        linkCategories.add("http://animehay.com/the-loai/phim-anime/drama?page=");
        linkCategories.add("http://animehay.com/the-loai/phim-anime/seinen?page=");
        linkCategories.add("http://animehay.com/the-loai/phim-anime/ac-quy?page=");
        linkCategories.add("http://animehay.com/the-loai/phim-anime/ma-ca-rong?page=");
        linkCategories.add("http://animehay.com/the-loai/phim-anime/psychological?page=");
        linkCategories.add("http://animehay.com/the-loai/phim-anime/shoujo?page=");
        linkCategories.add("http://animehay.com/the-loai/phim-anime/shounen-ai?page=");
        linkCategories.add("http://animehay.com/the-loai/phim-anime/tragedy?page=");
        linkCategories.add("http://animehay.com/the-loai/phim-anime/mecha?page=");
        linkCategories.add("http://animehay.com/the-loai/phim-anime/sieu-nang-luc?page=");
        linkCategories.add("http://animehay.com/the-loai/phim-anime/parody?page=");
        linkCategories.add("http://animehay.com/the-loai/phim-anime/quan-doi?page=");
        linkCategories.add("http://animehay.com/the-loai/phim-anime/live-action?page=");
        linkCategories.add("http://animehay.com/the-loai/phim-anime/shoujo-ai?page=");
        linkCategories.add("http://animehay.com/the-loai/phim-anime/josei?page=");
        linkCategories.add("http://animehay.com/the-loai/phim-anime/the-thao?page=");
        linkCategories.add("http://animehay.com/the-loai/phim-anime/am-nhac?page=");
        linkCategories.add("http://animehay.com/the-loai/phim-anime/samurai?page=");
        linkCategories.add("http://animehay.com/the-loai/phim-anime/tokusatsu?page=");
        linkCategories.add("http://animehay.com/the-loai/phim-anime/space?page=");
        linkCategories.add("http://animehay.com/the-loai/phim-anime/thriller?page=");

        catergories.add("Trang Chủ");
        catergories.add("Hành Động");
        catergories.add("Tình Cảm");
        catergories.add("Lịch Sử");
        catergories.add("Hài Hước");
        catergories.add("Viễn Tưởng");
        catergories.add("Võ Thuật");
        catergories.add("Giả Tưởng");
        catergories.add("Kinh Dị");
        catergories.add("Phiêu Lưu");
        catergories.add("Học Đường");
        catergories.add("Đời Thường");
        catergories.add("Siêu Nhiên");
        catergories.add("Harem");
        catergories.add("Ecchi");
        catergories.add("Shounen");
        catergories.add("Phép Thuật");
        catergories.add("Trò Chơi");
        catergories.add("Thám Tử");
        catergories.add("Mystery");
        catergories.add("Drama");
        catergories.add("Seinen");
        catergories.add("Ác Quỷ");
        catergories.add("Ma Cà Rồng");
        catergories.add("Psychological");
        catergories.add("Shoujo");
        catergories.add("Shounen Ai");
        catergories.add("Tragedy");
        catergories.add("Mecha");
        catergories.add("Siêu Năng Lực");
        catergories.add("Parody");
        catergories.add("Quân Đội");
        catergories.add("Live Action");
        catergories.add("Shoujo AI");
        catergories.add("Josei");
        catergories.add("Thể Thao");
        catergories.add("Âm Nhạc");
        catergories.add("Samurai");
        catergories.add("Tokusatsu");
        catergories.add("Space");
        catergories.add("Thriller");

        for (int i=0; i<41; i++){
            animeList[i] = new ArrayList<>();
            curPage[i] = 1;
        }
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.layout_anime_list, container, false);
        mContext = rootView.getContext();
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
        for (int i=0; i<41; i++){
            adapter[i] = new RecycleAdapter(mContext, animeList[i]);
        }
        if (animeList[curList].size() == 0) loadData();
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
