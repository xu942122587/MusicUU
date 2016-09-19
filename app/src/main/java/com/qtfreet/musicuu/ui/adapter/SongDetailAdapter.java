package com.qtfreet.musicuu.ui.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.daimajia.swipe.SwipeLayout;
import com.qtfreet.musicuu.R;
import com.qtfreet.musicuu.model.Bean.MusicUU.resultBean;
import com.qtfreet.musicuu.model.OnMusicClickListener;
import com.squareup.picasso.Picasso;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;


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
    public void onBindViewHolder(final ViewHolder holder, final int position) {
        // holder.swipe.setShowMode(SwipeLayout.ShowMode.LayDown);

        final resultBean song = mSongs.get(position);
        String url = song.getPicUrl();
        final String mv = song.getMvUrl();
        holder.mImageView.setTag(url);
        Picasso.with(mContext).load(url).into(holder.mImageView);
        holder.mSongName.setText(song.getSongName());
        holder.mSinger.setText(song.getArtist());
        // holder.swipe.setClickToClose(true);
        holder.down_music.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onItemClickListener.downLoadMusic(view, position);
                holder.swipe.close();

            }
        });
        holder.play_mv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onItemClickListener.playMV(view, position);
                holder.swipe.close();
            }
        });
        holder.swipe.getSurfaceView().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onItemClickListener.Music(view, position);
            }
        });

        holder.swipe.addSwipeListener(new SwipeLayout.SwipeListener() {
            @Override
            public void onStartOpen(SwipeLayout layout) {
                // Log.e("qtfreet0000", "1111111111");
                if (mv.isEmpty()) {
                    holder.play_mv.setVisibility(View.INVISIBLE);
                }
            }

            @Override
            public void onOpen(SwipeLayout layout) {

            }

            @Override
            public void onStartClose(SwipeLayout layout) {

            }

            @Override
            public void onClose(SwipeLayout layout) {

            }

            @Override
            public void onUpdate(SwipeLayout layout, int leftOffset, int topOffset) {

            }

            @Override
            public void onHandRelease(SwipeLayout layout, float xvel, float yvel) {

            }
        });
    }


    @Override
    public int getItemCount() {
        return mSongs.size();
    }


    class ViewHolder extends RecyclerView.ViewHolder {

        @Bind(R.id.rl_item_main)
        RelativeLayout mRelativeLayout;
        @Bind(R.id.iv_item_music)
        ImageView mImageView;
        @Bind(R.id.tv_item_song_name)
        TextView mSongName;
        @Bind(R.id.tv_item_singer)
        TextView mSinger;
        @Bind(R.id.swipe)
        SwipeLayout swipe;
        @Bind(R.id.play_mv)
        ImageView play_mv;
        @Bind(R.id.down_music)
        ImageView down_music;


        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }


}