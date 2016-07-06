package com.qtfreet.musicuu.ui.adapter;

import android.content.Context;
import android.os.Handler;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.qtfreet.musicuu.R;
import com.qtfreet.musicuu.model.resultBean;
import com.qtfreet.musicuu.ui.OnMusicClickListener;
import com.qtfreet.musicuu.ui.activity.SearchActivity;
import com.squareup.picasso.Picasso;


import java.util.List;

/**
 * Created by simonla on 2016/5/14.
 * Have a good day.
 */
public class SongDetailAdapter extends RecyclerView.Adapter<SongDetailAdapter.ViewHolder> {
    private OnMusicClickListener onItemClickListener;
    private List<resultBean> mSongs;
    private Context mContext;
    public static final String TAG = "SongDetailAdapter";
    public void setOnMusicClickListener(OnMusicClickListener listener) {
        this.onItemClickListener = listener;
    }
    public SongDetailAdapter(Context context, List<resultBean>
            songs) {
        mSongs = songs;
        mContext = context;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.
                from(parent.getContext()).
                inflate(R.layout.item_music_list, parent, false));
    }



    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {
        final resultBean song = mSongs.get(position);
        String url = song.getPicUrl();
        final String download = song.getHqUrl();
        holder.mImageView.setTag(url);
        Picasso.with(mContext).load(url).into(holder.mImageView);
        holder.mSongName.setText(song.getSongName());
        holder.mSinger.setText(song.getArtist());
        holder.mPopupMenu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mContext instanceof SearchActivity) {
                    ((SearchActivity) mContext).popupMenuClick(v,position);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return mSongs.size();
    }


    class ViewHolder extends RecyclerView.ViewHolder {
        public ImageView mImageView;
        public RelativeLayout mRelativeLayout;
        public TextView mSongName;
        public TextView mSinger;
        public TextView mPopupMenu;

        public ViewHolder(View itemView) {
            super(itemView);
            mRelativeLayout = (RelativeLayout) itemView.findViewById(R.id.rl_item_main);
            mImageView = (ImageView) itemView.findViewById(R.id.iv_item_music);
            mPopupMenu = (TextView) itemView.findViewById(R.id.tv_popup_menu);
            mSongName = (TextView) itemView.findViewById(R.id.tv_item_song_name);
            mSinger = (TextView) itemView.findViewById(R.id.tv_item_singer);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    onItemClickListener.Music(v, getAdapterPosition());
                }
            });
        }
    }


}