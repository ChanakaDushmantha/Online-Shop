package lk.chanaka.dushmantha.groceryonline.Shop;


import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.viewpager.widget.PagerAdapter;

import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.squareup.picasso.Picasso;

import java.util.List;

import lk.chanaka.dushmantha.groceryonline.R;
import lk.chanaka.dushmantha.groceryonline.Shop.Comments.CommentsDialog;

public class ShopAdapter extends PagerAdapter {

    private List<Shop> shops;
    private Context context;
    private static ClickListener clickListener;

    public ShopAdapter(List<Shop> shops, Context context) {
        this.shops = shops;
        this.context = context;
    }

    public void setOnItemClickListener(ClickListener clickListener) {
        ShopAdapter.clickListener = clickListener;
    }

    @Override
    public int getCount() {
        return shops.size();
    }

    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object o) {
        return view.equals(o);
    }

    @NonNull
    @Override
    public Object instantiateItem(@NonNull ViewGroup container, int position) {
        View view = LayoutInflater.from(context).inflate(
                R.layout.view_pager_shop,
                container, false
        );

        ImageView shopThumb = view.findViewById(R.id.mealThumb);
        TextView shopName = view.findViewById(R.id.shopName);
        TextView rating = view.findViewById(R.id.rating);
        TextView comments = view.findViewById(R.id.comments);

        String strMealThumb = shops.get(position).getImage_url();
        if (!strMealThumb.equals("null")) {
            Picasso.get().load(strMealThumb).into(shopThumb);
        }

        String strMealName = shops.get(position).getName();
        shopName.setText(strMealName);
        rating.setText(shops.get(position).getRating());

        view.setOnClickListener(v -> clickListener.onClick(v, position));
        comments.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentTransaction ft = ((ShopActivity)context).getSupportFragmentManager().beginTransaction();
                Fragment prev = ((ShopActivity)context).getSupportFragmentManager().findFragmentByTag("dialog");
                if (prev != null) {

                    ft.remove(prev);
                }
                ft.addToBackStack(null);
                BottomSheetDialogFragment dialogFragment = new CommentsDialog();

                Bundle args = new Bundle();
                args.putString("ShopId", shops.get(position).getShopId());

                dialogFragment.setArguments(args);
                dialogFragment.show(ft, "dialog");
            }
        });

        container.addView(view, 0);
        return view;
    }

    @Override
    public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
        container.removeView((View)object);
    }

    public interface ClickListener {
        void onClick(View v, int position);
    }
}