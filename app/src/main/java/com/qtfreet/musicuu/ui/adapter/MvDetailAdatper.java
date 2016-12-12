package com.qtfreet.musicuu.ui.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;


import com.qtfreet.musicuu.R;
import com.qtfreet.musicuu.model.Bean.YinyueTai.MvBean;
import com.qtfreet.musicuu.model.OnVideoClickListener;
import com.qtfreet.musicuu.ui.view.RadioImageView;
import com.squareup.picasso.Picasso;


import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;


public class MvDetailAdatper extends RecyclerView.Adapter<MvDetailAdatper.VideoViewHodler> {

    private Context mContext;
    private List<MvBean.DataBean> mvBean;

    private OnVideoClickListener onVideoClickListener;

    public MvDetailAdatper(Context mContext, List<MvBean.DataBean> imageInfos) {
        this.mContext = mContext;
        this.mvBean = imageInfos;
    }

    @Override
    public VideoViewHodler onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.searchmv_layout, parent, false);

        return new VideoViewHodler(view);
    }

    @Override
    public void onBindViewHolder(VideoViewHodler holder, int position) {

        loadImage(holder, mvBean.get(position));
    }

    private void loadImage(VideoViewHodler holder, MvBean.DataBean Info) {
        String postImg = Info.getAlbumImg();
        String recImg = Info.getRecommendPic();
        String pic;
        if (recImg == null) {
            pic = postImg;
        } else {
            pic = recImg;
        }
        if (!TextUtils.isEmpty(pic)) {
            Picasso.with(mContext).load(pic).into(holder.iv);
        } else {
            //    Picasso.with(mContext).load(R.mipmap.icon).fit().into(holder.iv);

        }
        holder.des.setText(Info.getTitle());

    }

    @Override
    public int getItemCount() {
        return mvBean.size();
    }

    class VideoViewHodler extends RecyclerView.ViewHolder {
        @Bind(R.id.des)
        TextView des;
        @Bind(R.id.iv_gril)
        RadioImageView iv;


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
