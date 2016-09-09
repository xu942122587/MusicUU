package com.qtfreet.musicuu.ui.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.qtfreet.musicuu.R;
import com.qtfreet.musicuu.model.Bean.Mango.MangoDetailBean;
import com.qtfreet.musicuu.model.OnVideoClickListener;
import com.qtfreet.musicuu.ui.view.RadioImageView;
import com.squareup.picasso.Picasso;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;


public class MangoDetailAdatper extends RecyclerView.Adapter<MangoDetailAdatper.VideoViewHodler> {

    private Context mContext;
    private List<MangoDetailBean.DataBean> mgBean;

    private OnVideoClickListener onVideoClickListener;

    public MangoDetailAdatper(Context mContext, List<MangoDetailBean.DataBean> Infos) {
        this.mContext = mContext;
        this.mgBean = Infos;
    }

    @Override
    public VideoViewHodler onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.search_mango_layout, parent, false);

        return new VideoViewHodler(view);
    }

    @Override
    public void onBindViewHolder(VideoViewHodler holder, int position) {

        loadImage(holder, mgBean.get(position));
    }

    private void loadImage(VideoViewHodler holder, MangoDetailBean.DataBean Info) {
        String postImg = Info.getImage();
        Picasso.with(mContext).load(postImg).into(holder.iv);
        holder.des.setText(Info.getName());
        holder.video_number.setText(Info.getDesc());
        if (!Info.getIcon().isEmpty()) {
            Picasso.with(mContext).load(Info.getIcon()).into(holder.iv_yuGao);
        }

    }

    @Override
    public int getItemCount() {
        return mgBean.size();
    }

    class VideoViewHodler extends RecyclerView.ViewHolder {
        @Bind(R.id.des)
        TextView des;
        @Bind(R.id.iv_gril)
        RadioImageView iv;
        @Bind(R.id.video_number)
        TextView video_number;
        @Bind(R.id.yuGao)
        ImageView iv_yuGao;


        public VideoViewHodler(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    onVideoClickListener.click(v, getAdapterPosition());
                }
            });
        }
    }

    public void setOnVideoClickListener(OnVideoClickListener listener) {
        this.onVideoClickListener = listener;
    }

}
