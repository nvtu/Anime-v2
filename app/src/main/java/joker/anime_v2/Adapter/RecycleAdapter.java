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

import cn.refactor.lib.colordialog.ColorDialog;
import cn.refactor.lib.colordialog.PromptDialog;
import joker.anime_v2.DataBase.SQLiteHelper;
import joker.anime_v2.DetailActivity;
import joker.anime_v2.ItemData.AnimeInfo;
import joker.anime_v2.R;

/**
 * Created by Tu Van Ninh on 25/12/2016.
 */
public class RecycleAdapter extends RecyclerView.Adapter<RecycleAdapter.ViewHolder>{

    private final int HISTORY = 1;
    private final int FAVORITE = 2;
    ArrayList<AnimeInfo> animeList;
    Context mContext;
    int mode;
    boolean isHistory = false;

    public RecycleAdapter(Context mContext, ArrayList<AnimeInfo> animeList) {
        this.animeList = animeList;
        this.mContext = mContext;
    }

    public RecycleAdapter(Context mContext, ArrayList<AnimeInfo> animeList, boolean isHistory, int mode) {
        this.animeList = animeList;
        this.mContext = mContext;
        this.isHistory = isHistory;
        this.mode = mode;
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
        if (isHistory){
            holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    ColorDialog dialog = new ColorDialog(mContext);
                    dialog.setTitle("WARNING");
                    dialog.setContentText("Do you really want to delete it? You cannot undo your choice!");
                    dialog.setPositiveListener("DELETE", new ColorDialog.OnPositiveListener() {
                        @Override
                        public void onClick(ColorDialog dialog) {
                            dialog.dismiss();
                            SQLiteHelper db = new SQLiteHelper(mContext);
                            int res = 0;
                            if (mode == HISTORY){
                                res = db.deleteFromHistory(animeList.get(position).getName());
                            }
                            else if (mode == FAVORITE){
                                res = db.deleteFromFavorite(animeList.get(position).getName());
                            }
                            if (res > 0){
                                new PromptDialog(mContext)
                                        .setDialogType(PromptDialog.DIALOG_TYPE_SUCCESS)
                                        .setAnimationEnable(true)
                                        .setTitleText("SUCCESS")
                                        .setContentText("Delete item successfully!")
                                        .setPositiveListener("OK", new PromptDialog.OnPositiveListener() {
                                            @Override
                                            public void onClick(PromptDialog dialog) {
                                                dialog.dismiss();
                                            }
                                        }).show();
                            }
                            else{
                                new PromptDialog(mContext)
                                        .setDialogType(PromptDialog.DIALOG_TYPE_SUCCESS)
                                        .setAnimationEnable(true)
                                        .setTitleText("ERROR")
                                        .setContentText("Unexpected error when trying to delete item!")
                                        .setPositiveListener("OK", new PromptDialog.OnPositiveListener() {
                                            @Override
                                            public void onClick(PromptDialog dialog) {
                                                dialog.dismiss();
                                            }
                                        }).show();
                            }
                            animeList.remove(position);
                            notifyDataSetChanged();
                            db.close();
                        }
                    })
                            .setNegativeListener("CANCEL", new ColorDialog.OnNegativeListener() {
                                @Override
                                public void onClick(ColorDialog dialog) {
                                    dialog.dismiss();
                                }
                            }).show();

                    return false;
                }
            });
        }
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
