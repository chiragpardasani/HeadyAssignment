package com.headyassignment.activities;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.widget.TextView;

import com.headyassignment.R;
import com.headyassignment.adapter.VariantAdapter;
import com.headyassignment.db.entity.Product;
import com.headyassignment.utils.AppUtils;
import com.headyassignment.viewmodel.ProductViewModel;
import com.headyassignment.viewmodel.VariantViewModel;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ProductDetailActivity extends AppCompatActivity {

    public static final String EXTRA_ID = "extra_id";
    public static final String EXTRA_NAME = "extra_name";

    @BindView(R.id.toolbar)
    Toolbar toolbar;

    @BindView(R.id.actProdDetail_txtTitle)
    TextView txtTitle;

    @BindView(R.id.actProdDetail_txtCreatedOn)
    TextView txtCreatedOn;

    @BindView(R.id.actProdDetail_txtPrice)
    TextView txtPrice;

    @BindView(R.id.actProdDetail_txtTax)
    TextView txtTax;

    @BindView(R.id.actProdDetail_recyclerView)
    RecyclerView recyclerView;

    List<Variant> globalVariants;

    VariantAdapter variantAdapter;

    long id;
    String name;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_detail);
        ButterKnife.bind(this);

        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Product Detail");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        id = getIntent().getLongExtra(EXTRA_ID, 0);
        name = getIntent().getStringExtra(EXTRA_NAME);

        globalVariants = new ArrayList<>();

        variantAdapter = new VariantAdapter(this, globalVariants);

        recyclerView.setAdapter(variantAdapter);
        recyclerView.setLayoutManager((new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)));

        txtTitle.setText(name);

        variantAdapter.setmOnItemClickListener(new VariantAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(Variant obj, int type) {
                for (Variant variant : globalVariants) {
                    if (variant.getId() == obj.getId()) {
                        variant.setSelected(true);
                        txtPrice.setText("Rs " + variant.getPrice());
                    } else {
                        variant.setSelected(false);
                    }
                }

                variantAdapter.notifyDataSetChanged();
            }
        });

        final VariantViewModel viewModel =
                ViewModelProviders.of(this).get(VariantViewModel.class);
        subscribe(viewModel);

        final ProductViewModel productViewModel =
                ViewModelProviders.of(this).get(ProductViewModel.class);
        subscribe(productViewModel);
    }

    private void subscribe(VariantViewModel viewModel) {
        viewModel.getRankingWithCount(id).observe(this, new Observer<List<com.headyassignment.db.entity.Variant>>() {
            @Override
            public void onChanged(@Nullable List<com.headyassignment.db.entity.Variant> variants) {
                if (variants != null) {
                    globalVariants.clear();
                    globalVariants.clear();
                    int i = 1;
                    for (com.headyassignment.db.entity.Variant variant : variants) {
                        Variant variantColor = new Variant();
                        variantColor.setId(variant.getId());
                        variantColor.setColor(variant.getColor());
                        variantColor.setSize(variant.getSize());
                        variantColor.setPrice(variant.getPrice());
                        if (i == 1) {
                            variantColor.setSelected(true);
                            txtPrice.setText("Rs " + variant.getPrice());
                        } else {
                            variantColor.setSelected(false);
                        }
                        i++;
                        globalVariants.add(variantColor);
                    }

                    variantAdapter.notifyDataSetChanged();
                }
            }
        });
    }

    public void subscribe(ProductViewModel productViewModel) {
        productViewModel.getSingleProduct(id).observe(this, new Observer<Product>() {
            @Override
            public void onChanged(@Nullable Product product) {
                if (product != null) {
                    txtTax.setText(" + " + product.getTax().getValue() + "% " + product.getTax().getName());

                    Date date = AppUtils.parseStringDate(product.getDate_added(), AppUtils.TEMPLATE_STANDARD_DATE_AND_TIME_TIMEZONE_MILLI);
                    txtCreatedOn.setText("Created on : " + AppUtils.getStandardDate(date, "dd MMM yyyy"));
                }
            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home)
            onBackPressed();
        return super.onOptionsItemSelected(item);
    }

    public class Variant {
        long id;
        String color;
        String size;
        String price;
        boolean isSelected;

        public String getColor() {
            return color;
        }

        public void setColor(String color) {
            this.color = color;
        }

        public String getSize() {
            return size;
        }

        public void setSize(String size) {
            this.size = size;
        }

        public String getPrice() {
            return price;
        }

        public void setPrice(String price) {
            this.price = price;
        }

        public boolean isSelected() {
            return isSelected;
        }

        public void setSelected(boolean selected) {
            isSelected = selected;
        }

        public long getId() {
            return id;
        }

        public void setId(long id) {
            this.id = id;
        }
    }
}
