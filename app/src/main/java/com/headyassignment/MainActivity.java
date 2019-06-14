package com.headyassignment;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.headyassignment.adapter.ProductAdapter;
import com.headyassignment.api.APIHelper;
import com.headyassignment.db.entity.Product;
import com.headyassignment.utils.DataProcessor;
import com.headyassignment.utils.Response;
import com.headyassignment.viewmodel.ProductViewModel;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import okhttp3.ResponseBody;

public class MainActivity extends AppCompatActivity {

    @BindView(R.id.actMain_recyclerView)
    RecyclerView recyclerView;

    List<Product> globalProducts;
    ProductAdapter productAdapter;

    APIHelper apiHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        apiHelper = ((MyApplication) getApplicationContext()).getApiHelper();

        globalProducts = new ArrayList<>();
        productAdapter = new ProductAdapter(this, globalProducts);

        recyclerView.setAdapter(productAdapter);
        recyclerView.setLayoutManager(new GridLayoutManager(this, 2));
        final ProductViewModel viewModel =
                ViewModelProviders.of(this).get(ProductViewModel.class);

        subscribeUi(viewModel);

        apiHelper.getJson(new APIHelper.ICallback() {
            @Override
            public void onSuccess(Response body) {
                if (body != null) {
                    DataProcessor dataProcessor = new DataProcessor(MainActivity.this);
                    dataProcessor.processData(body);
                }
            }

            @Override
            public void onFailure(ResponseBody responseBody) {

            }
        });
    }

    private void subscribeUi(ProductViewModel viewModel) {
        // Update the list when the data changes
        viewModel.getProducts().observe(this, new Observer<List<Product>>() {
            @Override
            public void onChanged(@Nullable List<Product> products) {
                if (products != null) {
                    globalProducts.clear();
                    globalProducts.addAll(products);
                    productAdapter.notifyDataSetChanged();
                } else {
                    // TODO: 14-06-2019 Empty list handling
                }
            }
        });
    }
}
