package com.headyassignment.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.headyassignment.R;
import com.headyassignment.fragments.CategoryListFragment;

import java.util.List;

public class CategoryAdapter extends RecyclerView.Adapter<CategoryAdapter.CategoryViewHolder> {

    public static final int TYPE_EXPAND = 1;
    public static final int TYPE_COLLAPSE = 2;
    public static final int TYPE_SUB_CATEGORY = 3;

    Context context;
    List<CategoryListFragment.LocalCategory> localCategories;
    OnItemClickListener onItemClickListener;
    LayoutInflater layoutInflater;

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }

    public CategoryAdapter(Context context, List<CategoryListFragment.LocalCategory> localCategories) {
        this.context = context;
        this.localCategories = localCategories;
        layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @NonNull
    @Override
    public CategoryViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_category, viewGroup, false);
        CategoryViewHolder categoryViewHolder = new CategoryViewHolder(view);
        return categoryViewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull CategoryViewHolder holder, int i) {
        CategoryListFragment.LocalCategory localCategory = localCategories.get(i);
        holder.txtTitle.setText(localCategory.getName());

        if (localCategory.isOpened()) {
            holder.img.setImageDrawable(context.getResources().getDrawable(R.mipmap.ic_remove));
            holder.dynamicLinear.removeAllViews();
            if (localCategory.getCategories() != null) {
                for (CategoryListFragment.LocalCategory localCategory1 : localCategory.getCategories()) {
                    inflate(layoutInflater, localCategory1, holder.dynamicLinear);
                }
            }
        } else {
            holder.dynamicLinear.removeAllViews();
            holder.img.setImageDrawable(context.getResources().getDrawable(R.mipmap.ic_add));
        }

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (localCategory.isOpened()) {
                    onItemClickListener.onItemClick(localCategory, TYPE_COLLAPSE);
                } else {
                    onItemClickListener.onItemClick(localCategory, TYPE_EXPAND);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return localCategories.size();
    }

    public class CategoryViewHolder extends RecyclerView.ViewHolder {

        TextView txtTitle;
        LinearLayout dynamicLinear;
        ImageView img;

        public CategoryViewHolder(@NonNull View itemView) {
            super(itemView);

            txtTitle = itemView.findViewById(R.id.itemCategory_txtName);
            dynamicLinear = itemView.findViewById(R.id.itemCategory_linear);
            img = itemView.findViewById(R.id.itemCategory_img);
        }
    }

    public void inflate(LayoutInflater layoutInflater, CategoryListFragment.LocalCategory localCategory, LinearLayout linearLayout) {
        View view = layoutInflater.inflate(R.layout.item_sub_category, null);
        TextView textView = view.findViewById(R.id.itemSubCategory_txtName);
        textView.setText(localCategory.getName());

        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onItemClickListener.onItemClick(localCategory, TYPE_SUB_CATEGORY);
            }
        });

        linearLayout.addView(view);
    }

    public interface OnItemClickListener {
        void onItemClick(CategoryListFragment.LocalCategory localCategory, int type);
    }
}
