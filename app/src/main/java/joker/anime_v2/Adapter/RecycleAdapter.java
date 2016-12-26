package joker.anime_v2.Adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import joker.anime_v2.DetailActivity;
import joker.anime_v2.ItemData.AnimeInfo;
import joker.anime_v2.R;

/**
 * Created by Tu Van Ninh on 25/12/2016.
 */
public class RecycleAdapter extends RecyclerView.Adapter<RecycleAdapter.ViewHolder>{

    ArrayList<AnimeInfo> animeList;
    Context mContext;

    public RecycleAdapter(Context mContext, ArrayList<AnimeInfo> animeList) {
        this.animeList = animeList;
        this.mContext = mContext;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        final View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {
        holder.animeEpisode.setText(animeList.get(position).getNumEps());
        holder.animeName.setText(animeList.get(position).getName());
        Picasso.with(mContext).load(animeList.get(position).getImgURL()).fit().into(holder.animeImage);
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mContext, DetailActivity.class);
                intent.putExtra("AnimeInformation", animeList.get(position));
                mContext.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return animeList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        TextView animeName, animeEpisode;
        ImageView animeImage;

        public ViewHolder(final View itemView) {
            super(itemView);
            animeImage = (ImageView) itemView.findViewById(R.id.animeImage);
            animeName = (TextView) itemView.findViewById(R.id.animeName);
            animeEpisode = (TextView) itemView.findViewById(R.id.animeEpisode);
        }
    }
}
