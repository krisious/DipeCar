package com.example.dipecar.adapter;

import android.content.Context;
import android.content.Intent;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;

import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.example.dipecar.R;
import com.example.dipecar.admin.ActivityDetailUsers;
import com.example.dipecar.api.client;
import com.example.dipecar.model.model_user.DataUser;
import com.google.gson.Gson;

import com.squareup.picasso.Picasso;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;


public class UsersAdapter extends RecyclerView.Adapter<UsersAdapter.MyViewHolder>{
    private List<DataUser> usersList;
    private Context mContext;
    private int lastPosition=0;
    private View mView;
    public class MyViewHolder extends RecyclerView.ViewHolder {
        private View view;
        private TextView name;
        private TextView email;
        private CircleImageView userImage;
        public MyViewHolder(View view) {
            super(view);
            mView=view;
            name=(TextView)view.findViewById(R.id.name);
            email=(TextView)view.findViewById(R.id.email);
            userImage=(CircleImageView)view.findViewById(R.id.userImage);
            this.view=view;
            mContext=view.getContext();
            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Gson gson=new Gson();
                    String user=gson.toJson(usersList.get(getAdapterPosition()));
                    Intent i=new Intent(mContext, ActivityDetailUsers.class);
                    i.putExtra("user",user);
                    mContext.startActivity(i);
                }
            });
        }

        public void bindItem(DataUser users) {
            name.setText(users.getName());
            email.setText(users.getEmail());
            if (!users.getPhoto().isEmpty())
                Picasso.with(mContext)
                    .load(client.getBaseUrlImage()+ users.getPhoto())
                    .resize(250, 250)
                    .centerCrop()
                    .into(this.userImage);
        }
    }


    public UsersAdapter(List<DataUser> usersList) {
        this.usersList = usersList;
    }


    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView= LayoutInflater.from(parent.getContext()).inflate(R.layout.design_user,parent,false);
        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        holder.bindItem(usersList.get(position));
        setAnimation(mView, position);
    }

    @Override
    public int getItemCount() {
        return usersList.size();
    }

    private void setAnimation(View viewToAnimate,int position) {
        if (position > lastPosition) {
            lastPosition = position;
            Animation animation = AnimationUtils.loadAnimation(mView.getContext(), R.anim.slide_left_to_right);
            viewToAnimate.startAnimation(animation);
        }
    }
}
