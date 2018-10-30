package com.example.sean.bydmarket;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;

public class AppSAdapter extends RecyclerView.Adapter<AppSAdapter.ViewHolder> {
    private Context mContext;
    private List<AppS> mList = null;

    public AppSAdapter(List<AppS> list, Context context) {   //构造函数
        mList = list;
        mContext = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_item, parent, false); //加载布局
        final ViewHolder holder = new ViewHolder(view);

        holder.appSView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int position = holder.getAdapterPosition();
                AppS appS = mList.get(position);
                String appName = appS.getName();
                ((MainActivity) mContext).send(appName);  //强转
            }
        });

        holder.appIconImag.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int position = holder.getAdapterPosition();
                AppS appS = mList.get(position);
                String appName = appS.getName();
                ((MainActivity) mContext).send(appName);  //强转
            }
        });
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        AppS appS = mList.get(position);
        holder.appIconImag.setImageResource(appS.getImageId());
        holder.appNameTV.setText(appS.getName());
        holder.resultTV.setText(appS.getResult());
        holder.idTV.setText(appS.getId() + "");
    }

    @Override
    public int getItemCount() {
        return mList.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {  //ViewHolder是一个内部类，缓存用，提高效率
        View appSView;
        ImageView appIconImag;
        TextView idTV;
        TextView appNameTV;
        TextView resultTV;

        public ViewHolder(View view) {
            super(view);
            appSView = view;
            appIconImag = (ImageView) view.findViewById(R.id.appIcon);
            appNameTV = (TextView) view.findViewById(R.id.appName);
            resultTV = (TextView) view.findViewById(R.id.result);
            idTV = (TextView) view.findViewById(R.id.id);
        }
    }

}
