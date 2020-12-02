package com.lilanz.wificonnect.adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.lilanz.wificonnect.R;
import com.lilanz.wificonnect.beans.SongBean;
import com.lilanz.wificonnect.controls.AppDataControl;
import com.lilanz.wificonnect.utils.StringUtil;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class SongAdapter extends RecyclerView.Adapter<SongAdapter.SongHolder> {

    private Context context;
    private List<SongBean> beanList;

    // 1.修改对象
    public SongAdapter(Context context, List<SongBean> list) {
        this.context = context;
        this.beanList = list;
        if (beanList == null) {
            beanList = new ArrayList<>();
        }
    }

    public int getPlayingIndex() {
        if (AppDataControl.isPlaying && !StringUtil.isEmpty(AppDataControl.playingPath)) {
            int size = beanList.size();
            for (int i = 0; i < size; i++) {
                if (AppDataControl.playingPath.equals(beanList.get(i).path)) {
                    return i;
                }
            }
        }
        return 0;
    }

    @NonNull
    @Override
    public SongHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        // 2.修改布局
        View view = LayoutInflater.from(context).inflate(R.layout.item_song, viewGroup, false);
        return new SongHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull SongHolder holder, final int i) {
        // 3.设置界面数据
        SongBean bean = beanList.get(i);
        holder.tvOrder.setText(i + 1 + "");
        int index = bean.name.lastIndexOf(".");
        holder.tvName.setText(bean.name.substring(0, index));
        holder.tvSinger.setText(bean.singer);
        holder.tvDuring.setText(String.format("%02d:%02d", bean.duration / 60000, bean.duration / 1000 % 60));

        if (i % 2 == 0) {
            holder.itemView.setBackgroundResource(R.drawable.shape_box_white);
        } else {
            holder.itemView.setBackgroundResource(R.drawable.shape_box_gray);
        }

        if (AppDataControl.playingPath != null && AppDataControl.playingPath.equals(bean.path)) {
            holder.itemView.setBackgroundResource(R.drawable.shape_box_green);
        }

        if (listener != null) {
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    AppDataControl.isPlaying = true;
                    AppDataControl.playingPath = beanList.get(i).path;
                    listener.onItemClick(beanList.get(i));
                    notifyDataSetChanged();
                }
            });
        }
    }

    @Override
    public int getItemCount() {
        return beanList.size();
    }

    public class SongHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.tv_order)
        TextView tvOrder;
        @BindView(R.id.tv_name)
        TextView tvName;
        @BindView(R.id.tv_singer)
        TextView tvSinger;
        @BindView(R.id.tv_during)
        TextView tvDuring;

        public SongHolder(@NonNull View view) {
            super(view);
            ButterKnife.bind(this, view);
            tvOrder.setTextSize(15);
            tvName.setTextSize(15);
            tvSinger.setTextSize(15);
            tvDuring.setTextSize(15);
            // 4.查找界面控件
        }
    }

    private OnItemClickListener listener;

    public void setListener(OnItemClickListener listener) {
        this.listener = listener;
    }

    public interface OnItemClickListener {
        public void onItemClick(SongBean bean);
    }
}
