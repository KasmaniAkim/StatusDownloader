package com.HashTagApps.WATool.adapter;


import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.HashTagApps.WATool.Interface.RSSItemClickListener;
import com.HashTagApps.WATool.R;
import com.HashTagApps.WATool.activity.RSSContent;
import com.HashTagApps.WATool.activity.RSSFeedActivity;
import com.HashTagApps.WATool.model.RSSObject;
import com.bumptech.glide.Glide;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;


public class RSSFeedAdapter extends RecyclerView.Adapter<RSSFeedAdapter.FeedViewHolder> {

    private RSSObject rssObject;
    private Context context;
    private LayoutInflater layoutInflater;

    public RSSFeedAdapter(RSSObject rssObject, Context context) {
        this.rssObject = rssObject;
        this.context = context;
        layoutInflater = LayoutInflater.from(context);
    }

    @NonNull
    @Override
    public FeedViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = layoutInflater.inflate(R.layout.row,parent,false);
        return new FeedViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull FeedViewHolder holder, int position) {
        holder.txtTitle.setText(rssObject.getItems().get(holder.getAdapterPosition()).getTitle());
        holder.txtPubDate.setText(rssObject.getItems().get(holder.getAdapterPosition()).getPubDate());
        Glide.with(context).load(rssObject.getItems().get(holder.getAdapterPosition()).getThumbnail()).centerCrop().into(holder.thumbnail_image);

        holder.setRssItemClickListener((view, position1, isLongClick) -> {
            if (RSSFeedActivity.isNetworkAvailable(context)) {
                Intent intent = new Intent(context, RSSContent.class);
                intent.putExtra("position", position1);
                context.startActivity(intent);
            } else {
                Toast.makeText(context, "Internet Connection Required", Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public int getItemCount() {
        return rssObject.items.size();
    }

    class FeedViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener,View.OnLongClickListener{

        TextView txtTitle,txtPubDate;
        private RSSItemClickListener rssItemClickListener;
        ImageView thumbnail_image;

        public FeedViewHolder(@NonNull View itemView) {
            super(itemView);
            txtTitle=itemView.findViewById(R.id.textTitle);
            txtPubDate=itemView.findViewById(R.id.textPublishDate);
            thumbnail_image=itemView.findViewById(R.id.thumbnail);

            itemView.setOnClickListener(this);
            itemView.setOnLongClickListener(this);
        }

        public void setRssItemClickListener(RSSItemClickListener rssItemClickListener) {
            this.rssItemClickListener = rssItemClickListener;
        }

        @Override
        public void onClick(View view) {
            rssItemClickListener.onClick(view,getAdapterPosition(),false);

        }

        @Override
        public boolean onLongClick(View view) {
            rssItemClickListener.onClick(view,getAdapterPosition(),true);
            return true;
        }
    }
}
