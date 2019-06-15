package com.headyassignment.activities;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.widget.TextView;

import com.headyassignment.R;
import com.headyassignment.adapter.VariantAdapter;

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

    @BindView(R.id.actProdDetail_recyclerViewColors)
    RecyclerView recyclerViewColors;

    @BindView(R.id.actProdDetail_recyclerViewSizes)
    RecyclerView recyclerViewSizes;

    List<Variant> variantSizes;
    List<Variant> variantColors;

    VariantAdapter variantAdapterSizes;
    VariantAdapter variantAdapterColors;

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

        txtTitle.setText(name);
    }

    public class Variant {
        public static final int TYPE_SIZE = 1;
        public static final int TYPE_COLOR = 2;

        int type;
        String color;
        String size;
        String price;
        boolean isSelected;

        public int getType() {
            return type;
        }

        public void setType(int type) {
            this.type = type;
        }

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
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home)
            onBackPressed();
        return super.onOptionsItemSelected(item);
    }
}
