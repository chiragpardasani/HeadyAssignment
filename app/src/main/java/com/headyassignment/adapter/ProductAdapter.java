package com.headyassignment.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.headyassignment.R;
import com.headyassignment.db.entity.ProductVariantPOJO;
import com.headyassignment.utils.AppUtils;

import java.util.Date;
import java.util.List;

public class ProductAdapter extends RecyclerView.Adapter<ProductAdapter.ProductViewHolder> {

    Context context;
    List<ProductVariantPOJO> products;
    private OnItemClickListener mOnItemClickListener;

    public ProductAdapter(Context context, List<ProductVariantPOJO> products) {
        this.context = context;
        this.products = products;
    }

    public void setmOnItemClickListener(OnItemClickListener mOnItemClickListener) {
        this.mOnItemClickListener = mOnItemClickListener;
    }

    @NonNull
    @Override
    public ProductViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_product, viewGroup, false);
        ProductViewHolder productViewHolder = new ProductViewHolder(view);
        return productViewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ProductViewHolder holder, int i) {
        ProductVariantPOJO product = products.get(i);
        Date date = AppUtils.parseStringDate(product.getDate_added(), AppUtils.TEMPLATE_STANDARD_DATE_AND_TIME_TIMEZONE_MILLI);
        holder.txtDate.setText("Created on : " + AppUtils.getStandardDate(date, "dd MMM yyyy"));
        holder.txtTitle.setText(product.getName());
        holder.txtPrice.setText("Rs : " + product.getPrice());
        holder.txtAvailableIn.setText("Size: " + product.getSize() + ", Color: " + product.getColor());

        if (product.getCount() > 0) {
            holder.txtCount.setVisibility(View.VISIBLE);
            holder.txtCount.setText("Ranking : " + product.getCount());
        } else {
            holder.txtCount.setVisibility(View.GONE);
        }

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mOnItemClickListener.onItemClick(product, i);
            }
        });
    }

    @Override
    public int getItemCount() {
        return products.size();
    }

    public class ProductViewHolder extends RecyclerView.ViewHolder {

        TextView txtDate;
        TextView txtTitle;
        TextView txtPrice;
        TextView txtAvailableIn;
        TextView txtCount;

        public ProductViewHolder(@NonNull View itemView) {
            super(itemView);

            txtDate = itemView.findViewById(R.id.itemProduct_txtDate);
            txtTitle = itemView.findViewById(R.id.itemProduct_txtTitle);
            txtPrice = itemView.findViewById(R.id.itemProduct_txtPrice);
            txtAvailableIn = itemView.findViewById(R.id.itemProduct_txtAvailableIn);
            txtCount = itemView.findViewById(R.id.itemProduct_txtCountText);
        }
    }

    public interface OnItemClickListener {
        void onItemClick(ProductVariantPOJO obj, int position);
    }
}
