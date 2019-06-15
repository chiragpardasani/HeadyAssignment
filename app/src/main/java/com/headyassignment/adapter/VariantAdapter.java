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
        holder.txtColor.setText(variant.getColor());
        holder.txtSize.setText(variant.getSize());

        if (variant.isSelected()) {
            holder.cardView.setCardBackgroundColor(context.getResources().getColor(R.color.viewBgLine));
        } else {
            holder.cardView.setCardBackgroundColor(context.getResources().getColor(R.color.app_white));
        }

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mOnItemClickListener.onItemClick(variant, i);
            }
        });
    }

    @Override
    public int getItemCount() {
        return variants.size();
    }

    public class VariantViewHolder extends RecyclerView.ViewHolder {

        TextView txtSize;
        TextView txtColor;
        CardView cardView;

        public VariantViewHolder(@NonNull View itemView) {
            super(itemView);

            txtSize = itemView.findViewById(R.id.itemVariant_txtSize);
            txtColor = itemView.findViewById(R.id.itemVariant_txtColor);
            cardView = itemView.findViewById(R.id.itemVariant_card);
        }
    }

    public interface OnItemClickListener {
        void onItemClick(ProductDetailActivity.Variant obj, int type);
    }
}
