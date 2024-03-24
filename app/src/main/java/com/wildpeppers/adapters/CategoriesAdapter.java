package com.wildpeppers.adapters;

import android.content.Context;
import android.content.Intent;
//import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.InterstitialAd;

import com.wildpeppers.R;
import com.wildpeppers.activities.CategorizedWallpapersActivity;
import com.wildpeppers.models.CategoriesModel;

import java.util.List;

public class CategoriesAdapter extends RecyclerView.Adapter<CategoriesAdapter.CategoryViewHolder> {

    private Context mCtx;
    private List<CategoriesModel> categoryList;

    private InterstitialAd mInterstitialAd;

    public CategoriesAdapter(Context mCtx, List<CategoriesModel> categoryList) {
        this.mCtx = mCtx;
        this.categoryList = categoryList;

        mInterstitialAd = new InterstitialAd(mCtx);
        mInterstitialAd.setAdUnitId("ca-app-pub-6677140660293215/9762844227");
        mInterstitialAd.loadAd(new AdRequest.Builder().build());
    }

    @Override
    public CategoryViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mCtx).inflate(R.layout.layout_item_category, parent, false);
        return new CategoryViewHolder(view);
    }

    @Override
    public void onBindViewHolder(CategoryViewHolder holder, int position) {
        CategoriesModel c = categoryList.get(position);
        //holder.textView.setText(c.name);
        Glide.with(mCtx)
                .load(c.thumb)
                .into(holder.imageView);
    }

    @Override
    public int getItemCount() {
        return categoryList.size();
    }

    class CategoryViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        //TextView textView;
        ImageView imageView;

        public CategoryViewHolder(View itemView) {
            super(itemView);

            //textView = itemView.findViewById(R.id.text_view_cat_name);
            imageView = itemView.findViewById(R.id.image_view);

            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {

            if (mInterstitialAd.isLoaded()) {
                mInterstitialAd.show();
            } else {
                mInterstitialAd.loadAd(new AdRequest.Builder().build());
            }

            int p = getAdapterPosition();
            CategoriesModel c = categoryList.get(p);

            Intent intent = new Intent(mCtx, CategorizedWallpapersActivity.class);
            intent.putExtra("category", c.name); //chestia asta deschide categoriile in functie de nume

            mCtx.startActivity(intent);
        }
    }
}
