package com.example.timeclock.adapters;

import static com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions.withCrossFade;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.timeclock.R;
import com.example.timeclock.models.User;

import java.util.ArrayList;
import java.util.List;

public class UserListAdapter extends RecyclerView.Adapter<UserListAdapter.ViewHolder> {
    private List<User> users = new ArrayList<>();
    private final Context context;

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public final ImageView userPhotoView;
        public final TextView userNameView;
        public final TextView userTypeView;

        public ViewHolder(View view) {
            super(view);

            userPhotoView = view.findViewById(R.id.user_picture);
            userNameView = view.findViewById(R.id.user_name);
            userTypeView = view.findViewById(R.id.user_type);
        }
    }

    public UserListAdapter(Context context) {
        this.context = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_user, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public int getItemCount() {
        return users.size();
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        User user = users.get(position);

        if (user.getName() != null)
            holder.userNameView.setText(user.getName());

        if (user.getUserType() != null) {
            holder.userTypeView.setText(user.getUserType().name());
            if (user.getUserType() == User.UserType.administrator)
                holder.userTypeView.setTextColor(ContextCompat.getColor(context, R.color.purple_700));
            else
                holder.userTypeView.setTextColor(ContextCompat.getColor(context, R.color.teal_700));
        }

        if (user.getImage() != null)
            Glide.with(context).load(user.getImage()).transition(withCrossFade())
                    .into(holder.userPhotoView);
        //future enhancement - load an error symbol if image load error occurs

        //future enhancement -- add onclick behavior so admin can see a user in detail and make edits
    }

    public void setResults(List<User> users) {
        this.users = users;
        notifyDataSetChanged(); //ignore warning as all data does change
    }
}
