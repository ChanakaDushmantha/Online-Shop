package lk.chanaka.dushmantha.groceryonline.Shop.Comments;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;

import java.util.List;

import lk.chanaka.dushmantha.groceryonline.R;

public class CommentsAdapter extends RecyclerView.Adapter<CommentsAdapter.ViewHolder> {
    private LayoutInflater inflater;
    private List<Comments> comments;

    CommentsAdapter(Context ctx, List<Comments> comments){
        this.inflater = LayoutInflater.from(ctx);
        this.comments = comments;
    }

    @NonNull
    @Override
    public CommentsAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.comment_item,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CommentsAdapter.ViewHolder holder, int position) {
        holder.userName.setText(comments.get(position).getUserName());
        holder.time.setText(comments.get(position).getTime());
        holder.ratingBar.setRating(comments.get(position).getRating());
        holder.comment.setText(comments.get(position).getComment());
        String image = comments.get(position).getImage_url();
        System.out.println(image+" image");
        if (!image.isEmpty()) {
            if (!image.equals("null")) {
                Picasso.get().load(image).into(holder.coverImage);
            }
        }
    }

    @Override
    public int getItemCount() {
        return comments.size();
    }

    public class ViewHolder extends  RecyclerView.ViewHolder{
        TextView userName, time, comment;
        ImageView coverImage;
        RatingBar ratingBar;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            userName = itemView.findViewById(R.id.custName);
            time = itemView.findViewById(R.id.time);
            ratingBar = itemView.findViewById(R.id.custRatingBar);
            comment = itemView.findViewById(R.id.comment);
            coverImage = itemView.findViewById(R.id.coverImage);

            // handle onClick

            /*itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(inflater.getContext(), Item.class);
                    intent.putExtra("ID", itemId);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    inflater.getContext().startActivity(intent);
                }
            });*/
        }
    }
}
