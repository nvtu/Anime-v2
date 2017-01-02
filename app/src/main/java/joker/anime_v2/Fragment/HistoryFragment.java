package joker.anime_v2.Fragment;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;

import joker.anime_v2.Adapter.RecycleAdapter;
import joker.anime_v2.DataBase.SQLiteHelper;
import joker.anime_v2.ItemData.AnimeInfo;
import joker.anime_v2.R;
import joker.anime_v2.widget.SwitchButton;

/**
 * Created by Tu Van Ninh on 25/12/2016.
 */
public class HistoryFragment extends Fragment{

    private RecyclerView rv;
    public static RecycleAdapter adapter;
    public static ArrayList<AnimeInfo> animeList;
    private SwitchButton switchButton;
    private TextView tv;
    Context mContext;

    public HistoryFragment(){

    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.layout_history_fragment, container, false);
        mContext = rootView.getContext();
        initLayouts(rootView);
        return rootView;
    }

    private void initLayouts(View rootView) {
        tv = (TextView) rootView.findViewById(R.id.tvHistory);
        switchButton = (SwitchButton) rootView.findViewById(R.id.switchButton);
        rv = (RecyclerView) rootView.findViewById(R.id.rvh);
        GridLayoutManager gridLayoutManager = new GridLayoutManager(rootView.getContext(), 2);
        rv.setHasFixedSize(true);
        rv.setLayoutManager(gridLayoutManager);
        animeList = new ArrayList<>();

        if (switchButton.isChecked()){
            switchToFavorite();
        }
        else{
            swithToHistory();
        }


        switchButton.setOnCheckedChangeListener(new SwitchButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(SwitchButton s, boolean isChecked) {
                if (isChecked) {
                   switchToFavorite();
                } else {
                   swithToHistory();
                }
            }
        });
    }

    public void swithToHistory(){
        SQLiteHelper db = new SQLiteHelper(mContext);
        animeList.clear();
        animeList.addAll(db.getAllHistory());
        adapter = new RecycleAdapter(mContext, animeList, true, 1);
        rv.setAdapter(adapter);
        tv.setText("HISTORY");
        db.close();
    }

    public void switchToFavorite(){
        SQLiteHelper db = new SQLiteHelper(mContext);
        animeList.clear();
        animeList.addAll(db.getAllFavorite());
        adapter = new RecycleAdapter(mContext, animeList, true, 2);
        rv.setAdapter(adapter);
        tv.setText("FAVORITE");
        db.close();
    }
}
