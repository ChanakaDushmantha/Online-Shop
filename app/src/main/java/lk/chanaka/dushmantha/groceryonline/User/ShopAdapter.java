package lk.chanaka.dushmantha.groceryonline.User;


import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.viewpager.widget.PagerAdapter;

import com.squareup.picasso.Picasso;

import java.util.List;

import lk.chanaka.dushmantha.groceryonline.R;

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

        ImageView mealThumb = view.findViewById(R.id.mealThumb);
        TextView mealName = view.findViewById(R.id.shopName);

        String strMealThumb = shops.get(position).getImage_url();
        if (!strMealThumb.equals("null")) {
            Picasso.get().load(strMealThumb).into(mealThumb);
        }

        String strMealName = shops.get(position).getName();
        mealName.setText(strMealName);

        view.setOnClickListener(v -> clickListener.onClick(v, position));

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