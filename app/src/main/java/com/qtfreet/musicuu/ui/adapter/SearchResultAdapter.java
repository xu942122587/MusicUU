package com.qtfreet.musicuu.ui.adapter;

import java.util.List;


import android.content.Context;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.qtfreet.musicuu.R;
import com.qtfreet.musicuu.model.DownListener;
import com.qtfreet.musicuu.model.resultBean;
import com.squareup.picasso.Picasso;

import me.drakeet.uiview.UIButton;

/**
 * liteplayer by loader
 *
 * @author qibin
 */
public class SearchResultAdapter extends BaseAdapter {
    private List<resultBean> mSearchResult;
    private Context context;
    private DownListener mListener;

    public SearchResultAdapter(Context mContext, List<resultBean> searchResult) {
        mSearchResult = searchResult;
        this.context = mContext;
    }


    public void setDownloadListener(DownListener listener) {
        this.mListener = listener;
    }

    @Override
    public int getCount() {
        return mSearchResult.size();
    }

    @Override
    public Object getItem(int position) {
        return mSearchResult.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        final ViewHolder holder;
        if (convertView == null) {
            convertView = View.inflate(context, R.layout.search_result_item, null);
            holder = new ViewHolder();
            holder.title = (TextView) convertView.findViewById(R.id.tv_search_result_title);
            holder.artist = (TextView) convertView.findViewById(R.id.tv_search_result_artist);
            holder.album = (TextView) convertView.findViewById(R.id.tv_search_result_album);
            holder.pic = (ImageView) convertView.findViewById(R.id.pic);
            holder.btn_down = (UIButton) convertView.findViewById(R.id.btn_down);
            holder.imageView = (ImageView) convertView.findViewById(R.id.exist_mv);


            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        String artist = mSearchResult.get(position).getArtist();
        String album = mSearchResult.get(position).getAlbum();
        String picurl = mSearchResult.get(position).getPicUrl();
        holder.title.setText(mSearchResult.get(position).getSongName());

        if (!TextUtils.isEmpty(artist)) holder.artist.setText(artist);
        else holder.artist.setText("未知艺术家");
        if(!mSearchResult.get(position).getVideoUrl().equals("")){
            holder.imageView.setVisibility(View.VISIBLE);
        }

        if (!TextUtils.isEmpty(album)) holder.album.setText(album);
        else holder.album.setText("未知专辑");
        if (!TextUtils.isEmpty(picurl)) {
            Picasso.with(context).load(picurl).resize(150, 150).centerCrop().into(holder.pic);
        }
        final String SongId =  mSearchResult.get(position).getSongId();
        final String SongName  = mSearchResult.get(position).getSongName();
        final String Artist = mSearchResult.get(position).getArtist();
        final String SqUrl = mSearchResult.get(position).getSqUrl();
        final String HqUrl =  mSearchResult.get(position).getHqUrl();
        final String LqUrl =  mSearchResult.get(position).getLqUrl();
        final String VideoUrl = mSearchResult.get(position).getVideoUrl();
        holder.btn_down.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!SqUrl.equals("")){
                    mListener.Download(v, position, SongId, SongName, Artist,  SqUrl, VideoUrl, holder.btn_down);
                }else if(!HqUrl.equals("")){
                    mListener.Download(v, position, SongId, SongName, Artist,  HqUrl, VideoUrl, holder.btn_down);
                }else if(!LqUrl.equals("")){
                    mListener.Download(v, position, SongId, SongName, Artist,  LqUrl, VideoUrl, holder.btn_down);
                }


            }
        });
        return convertView;
    }

    static class ViewHolder {
        public TextView title;
        public TextView artist;
        public TextView album;
        public ImageView pic;
        public UIButton btn_down;
        public ImageView imageView;
    }
}
