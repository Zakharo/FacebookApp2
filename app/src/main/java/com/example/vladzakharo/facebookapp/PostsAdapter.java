package com.example.vladzakharo.facebookapp;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

/**
 * Created by Vlad Zakharo on 30.04.2016.
 */
public class PostsAdapter extends RecyclerView.Adapter<PostsAdapter.MyViewHolder> {

    private List<Post> postsList;

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView tvMessage;
        public TextView tvName;
        public TextView tvCaption;
        public TextView tvDescription;

        public MyViewHolder(View view){
            super(view);
            tvMessage = (TextView) view.findViewById(R.id.text1);
            tvName = (TextView) view.findViewById(R.id.text2);
            tvCaption = (TextView) view.findViewById(R.id.text3);
            tvDescription = (TextView) view.findViewById(R.id.text4);
        }
    }
    public PostsAdapter(List<Post> postsList){
        this.postsList = postsList;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.list_item, parent, false);

        return new MyViewHolder(itemView);
    }
    @Override
    public void onBindViewHolder(MyViewHolder holder, int position){
        Post post = postsList.get(position);
        holder.tvMessage.setText(post.getMessage());
        holder.tvName.setText(post.getName());
        holder.tvCaption.setText(post.getCaption());
        holder.tvDescription.setText(post.getDescription());
    }

    @Override
    public int getItemCount(){
        return postsList.size();
    }
}
