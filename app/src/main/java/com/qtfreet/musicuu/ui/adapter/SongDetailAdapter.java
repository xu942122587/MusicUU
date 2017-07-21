package com.qtfreet.musicuu.ui.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.blankj.utilcode.util.ConvertUtils;
import com.qtfreet.musicuu.R;
import com.qtfreet.musicuu.model.OnMusicClickListener;
import com.qtfreet.musicuu.musicApi.MusicService.SongResult;
import com.squareup.picasso.Picasso;
import com.yanzhenjie.recyclerview.swipe.SwipeMenuAdapter;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;


public class SongDetailAdapter extends SwipeMenuAdapter<SongDetailAdapter.ViewHolder> {
    private OnMusicClickListener onItemClickListener;
    private List<SongResult> mSongs;
    private Context mContext;

    public void setOnMusicClickListener(OnMusicClickListener listener) {
        this.onItemClickListener = listener;
    }

    public SongDetailAdapter(Context context, List<SongResult>
            songs) {
        mSongs = songs;
        mContext = context;
    }

    @Override
    public View onCreateContentView(ViewGroup parent, int viewType) {
        return LayoutInflater.
                from(parent.getContext()).
                inflate(R.layout.item_music_list, parent, false);
    }

    @Override
    public ViewHolder onCompatCreateViewHolder(View realContentView, int viewType) {
        return new ViewHolder(realContentView);
    }


    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
        final SongResult song = mSongs.get(position);
        String url = song.getPicUrl();
        holder.mImageView.setTag(url);
        int size = ConvertUtils.dp2px(90);
        if (url != null) {
            if (!url.isEmpty()) {
                Picasso.with(mContext).load(url).resize(size, size).centerCrop().into(holder.mImageView);
            }
        }
        holder.mSongName.setText(song.getSongName());
        holder.mSinger.setText(song.getArtistName() + "-" + song.getAlbumName());
    }


    @Override
    public int getItemCount() {
        return mSongs.size();
    }


    class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        @Bind(R.id.rl_item_main)
        RelativeLayout mRelativeLayout;
        @Bind(R.id.iv_item_music)
        ImageView mImageView;
        @Bind(R.id.tv_item_song_name)
        TextView mSongName;
        @Bind(R.id.tv_item_singer)
        TextView mSinger;


        ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            if (onItemClickListener != null) {
                onItemClickListener.Music(view, getAdapterPosition());
            }
        }
    }


}