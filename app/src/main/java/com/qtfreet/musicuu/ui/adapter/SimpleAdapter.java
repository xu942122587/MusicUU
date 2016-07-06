package com.qtfreet.musicuu.ui.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.qtfreet.musicuu.R;

/**
 * Created by qtfreet on 2016/3/30.
 */
public class SimpleAdapter extends BaseAdapter {

    String[] shareStr = null;

    static class ViewHolder {
        public TextView title;

    }

    public SimpleAdapter(Context context, String[] agrs) {
        super();
        this.mContext = context;
        this.shareStr = agrs;
    }

    public ItemClickListener mItemClickListener;

    public void setItemClickListener(ItemClickListener listener) {
        mItemClickListener = listener;
    }

    @Override
    public int getCount() {
        return shareStr.length;
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        final ViewHolder holder;
        if (convertView == null) {
            convertView = View.inflate(mContext, R.layout.list_item, null);
            holder = new ViewHolder();
            holder.title = (TextView) convertView.findViewById(R.id.tv);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        holder.title.setText(shareStr[position]);
        holder.title.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mItemClickListener.onItemClick(position);
            }
        });
        return convertView;
    }

    public interface ItemClickListener {
        void onItemClick(int pos);
    }

    private Context mContext;


}
