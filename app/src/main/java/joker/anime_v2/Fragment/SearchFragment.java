package joker.anime_v2.Fragment;

import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import com.miguelcatalan.materialsearchview.MaterialSearchView;

import java.util.ArrayList;
import java.util.TreeMap;

import joker.anime_v2.Adapter.RecycleAdapter;
import joker.anime_v2.ItemData.AnimeInfo;
import joker.anime_v2.LoadData.LoadAnimeList;
import joker.anime_v2.R;

/**
 * Created by Tu Van Ninh on 25/12/2016.
 */
public class SearchFragment extends Fragment implements LoadAnimeList.LoadAnimeListResponse{

    private MaterialSearchView searchView;
    private RecycleAdapter adapter;
    ArrayList<AnimeInfo> animeList;
    LoadAnimeList loadAnimeList;
    Integer curPage = 1;
    String q;

    public SearchFragment(){

    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.layout_search_fragment, container, false);
        initLayouts(rootView);
        setHasOptionsMenu(true);
        return rootView;
    }

    private void loadData(){
        loadAnimeList = new LoadAnimeList(getContext());
        loadAnimeList.delegate = this;
        loadAnimeList.execute(q+curPage);
    }

    private void initLayouts(final View rootView) {
        animeList = new ArrayList<>();
        adapter = new RecycleAdapter(rootView.getContext(), animeList);
        RecyclerView rv = (RecyclerView) rootView.findViewById(R.id.rvsearch);
        GridLayoutManager gridLayoutManager = new GridLayoutManager(rootView.getContext(), 2);
        rv.setHasFixedSize(true);
        rv.setLayoutManager(gridLayoutManager);
        rv.setAdapter(adapter);

        searchView = (MaterialSearchView) rootView.findViewById(R.id.search_view);
        searchView.setOnQueryTextListener(new MaterialSearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                //Do some magic
                curPage = 1;
                q = "http://m.animetvn.com/tim-kiem/" + Uri.encode(query) + ".html?p=";
                loadData();
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                //Do some magic
                return false;
            }
        });

        searchView.setOnSearchViewListener(new MaterialSearchView.SearchViewListener() {
            @Override
            public void onSearchViewShown() {
                //Do some magic
            }

            @Override
            public void onSearchViewClosed() {
                //Do some magic
            }
        });

    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu_main, menu);
        MenuItem item = menu.findItem(R.id.action_search);
        searchView.setMenuItem(item);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.backk){
            if (q == null || q.isEmpty()) return true;
            curPage--;
            loadData();
            return true;
        }
        else if (id == R.id.nextt){
            if (q == null || q.isEmpty()) return true;
            curPage++;
            loadData();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void processFinish(ArrayList<AnimeInfo> animeList) {
        boolean isDiff = true;
        TreeMap<String,Boolean> map = new TreeMap<>();
        for (AnimeInfo it : this.animeList){
            map.put(it.getName(), Boolean.TRUE);
        }
        for (AnimeInfo it : animeList){
            if (map.containsKey(it.getName())){
                isDiff = false;
                break;
            }
        }
        if (!isDiff || animeList.size() == 0){
            if (curPage == 0) curPage++;
            else curPage--;
            return;
        }
        this.animeList.clear();
        this.animeList.addAll(animeList);
        adapter.notifyDataSetChanged();
    }

}
