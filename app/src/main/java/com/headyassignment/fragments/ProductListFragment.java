package com.headyassignment.fragments;


import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.headyassignment.R;
import com.headyassignment.activities.MainActivity;
import com.headyassignment.adapter.ProductAdapter;
import com.headyassignment.db.entity.Product;
import com.headyassignment.viewmodel.ProductListViewModel;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * A simple {@link Fragment} subclass.
 */
public class ProductListFragment extends Fragment {

    public static final String EXTRA_ID = "extra_id";
    public static final String EXTRA_NAME = "extra_name";

    @BindView(R.id.fragProdList_recyclerView)
    RecyclerView recyclerView;

    List<Product> globalProducts;
    ProductAdapter productAdapter;

    long id;
    String name = "";

    public ProductListFragment() {
        // Required empty public constructor
    }

    public static ProductListFragment newInstance(long id, String name) {
        ProductListFragment productListFragment = new ProductListFragment();
        Bundle bundle = new Bundle();
        bundle.putLong(EXTRA_ID, id);
        bundle.putString(EXTRA_NAME, name);
        productListFragment.setArguments(bundle);
        return productListFragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_product_list, container, false);
        ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        id = getArguments().getLong(EXTRA_ID);
        name = getArguments().getString(EXTRA_NAME);

        ((MainActivity) getActivity()).setTitle(name);

        globalProducts = new ArrayList<>();
        productAdapter = new ProductAdapter(getActivity(), globalProducts);
        recyclerView.setAdapter(productAdapter);
        recyclerView.setLayoutManager(new GridLayoutManager(getActivity(), 2));

        final ProductListViewModel viewModel =
                ViewModelProviders.of(this).get(ProductListViewModel.class);
        subscribeUi(viewModel);
    }

    private void subscribeUi(ProductListViewModel viewModel) {
        // Update the list when the data changes
        viewModel.getProducts(id).observe(this, new Observer<List<Product>>() {
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
