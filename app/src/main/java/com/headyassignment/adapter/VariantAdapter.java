package com.headyassignment.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.headyassignment.R;
import com.headyassignment.activities.ProductDetailActivity;

import java.util.List;

public class VariantAdapter extends RecyclerView.Adapter<VariantAdapter.VariantViewHolder> {

    public static final int TYPE_COLOR = 1;
    public static final int TYPE_SIZE = 2;

    Context context;
    List<ProductDetailActivity.Variant> variants;
    private OnItemClickListener mOnItemClickListener;

    public VariantAdapter(Context context, List<ProductDetailActivity.Variant> variants) {
        this.context = context;
        this.variants = variants;
    }

    public void setmOnItemClickListener(OnItemClickListener mOnItemClickListener) {
        this.mOnItemClickListener = mOnItemClickListener;
    }

    @NonNull
    @Override
    public VariantViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_variant, viewGroup, false);
        VariantViewHolder productViewHolder = new VariantViewHolder(view);
        return productViewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull VariantViewHolder holder, int i) {
        ProductDetailActivity.Variant variant = variants.get(i);
        if (variant.getType() == ProductDetailActivity.Variant.TYPE_COLOR) {
            holder.txtTitle.setText(variant.getColor());
        } else {
            holder.txtTitle.setText(variant.getSize());
        }

        if (variant.isSelected()) {
            holder.cardView.setCardBackgroundColor(context.getResources().getColor(R.color.viewBgLine));
        } else {
            holder.cardView.setCardBackgroundColor(context.getResources().getColor(R.color.app_white));
        }
    }

    @Override
    public int getItemCount() {
        return variants.size();
    }

    public class VariantViewHolder extends RecyclerView.ViewHolder {

        TextView txtTitle;
        CardView cardView;

        public VariantViewHolder(@NonNull View itemView) {
            super(itemView);

            txtTitle = itemView.findViewById(R.id.itemVariant_txtTitle);
            cardView = itemView.findViewById(R.id.itemVariant_card);
        }
    }

    public interface OnItemClickListener {
        void onItemClick(ProductDetailActivity.Variant obj, int type);
    }
}
