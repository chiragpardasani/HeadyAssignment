package com.headyassignment.fragments;


import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import com.headyassignment.MyApplication;
import com.headyassignment.R;
import com.headyassignment.activities.MainActivity;
import com.headyassignment.activities.ProductDetailActivity;
import com.headyassignment.adapter.ProductAdapter;
import com.headyassignment.api.APIHelper;
import com.headyassignment.db.entity.ProductRanking;
import com.headyassignment.db.entity.ProductVariantPOJO;
import com.headyassignment.utils.DataProcessor;
import com.headyassignment.utils.Response;
import com.headyassignment.viewmodel.RankingViewModel;
import com.headyassignment.viewmodel.VariantViewModel;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import okhttp3.ResponseBody;

/**
 * A simple {@link Fragment} subclass.
 */
public class HomeFragment extends Fragment {

    public static final String TYPE_VIEW_ALL = "All Prodcuts";

    @BindView(R.id.fragHome_recyclerView)
    RecyclerView recyclerView;

    @BindView(R.id.fragHome_spinner)
    Spinner spinner;

    List<ProductVariantPOJO> globalProducts;
    List<ProductRanking> globalProductRankings;
    ProductAdapter productAdapter;

    APIHelper apiHelper;
    RankingViewModel viewModelRanking;
    VariantViewModel variantViewModel;

    List<Long> longs;

    public HomeFragment() {
        // Required empty public constructor
    }

    public static HomeFragment newInstance() {
        return new HomeFragment();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_home, container, false);
        ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        apiHelper = ((MyApplication) getActivity().getApplicationContext()).getApiHelper();

        globalProducts = new ArrayList<>();
        globalProductRankings = new ArrayList<>();
        longs = new ArrayList<>();
        productAdapter = new ProductAdapter(getActivity(), globalProducts);

        recyclerView.setAdapter(productAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        spinner.setAdapter(createTypeAdapter());
        viewModelRanking =
                ViewModelProviders.of(this).get(RankingViewModel.class);

        variantViewModel =
                ViewModelProviders.of(this).get(VariantViewModel.class);
        subscribeForVariant();

        // Server call for fetching gson
        serverCallForGson();

        // Get categories by database
        new GetGroupByCategories().execute();

        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String type = String.valueOf(parent.getItemAtPosition(position));
                if (!type.equalsIgnoreCase(TYPE_VIEW_ALL)) {
                    getRankingWithCount(type);
                } else {
                    variantViewModel.getProductWithVariant(new ArrayList<>());
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        productAdapter.setmOnItemClickListener(new ProductAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(ProductVariantPOJO obj, int position) {
                Intent intent = new Intent(getActivity(), ProductDetailActivity.class);
                intent.putExtra(ProductDetailActivity.EXTRA_ID, obj.getId());
                intent.putExtra(ProductDetailActivity.EXTRA_NAME, obj.getName());
                startActivity(intent);
            }
        });
    }

    private void subscribeForVariant() {
        variantViewModel.getProductWithVariant(longs).observe(getActivity(), new Observer<List<ProductVariantPOJO>>() {
            @Override
            public void onChanged(@Nullable List<ProductVariantPOJO> productVariantPOJOS) {
                if (productVariantPOJOS != null) {
                    globalProducts.clear();
                    globalProducts.addAll(productVariantPOJOS);
                    productAdapter.notifyDataSetChanged();
                } else {
                    // TODO: 14-06-2019 Empty list handling
                }
            }
        });
    }

    private void getRankingWithCount(String ranking) {
        viewModelRanking.getRankingWithCount(ranking).observe(this, new Observer<List<ProductRanking>>() {
            @Override
            public void onChanged(@Nullable List<ProductRanking> productRankings) {
                if (productRankings != null) {
                    longs.clear();
                    for (ProductRanking productRanking : productRankings) {
                        longs.add(productRanking.getProduct_id());
                    }
                }
                variantViewModel.getProductWithVariant(longs);
            }
        });
    }

    private ArrayAdapter<String> createTypeAdapter() {
        List<String> strType = new ArrayList<>();
        strType.add(TYPE_VIEW_ALL);

        for (ProductRanking productRanking : globalProductRankings) {
            strType.add(productRanking.getRanking());
        }
        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<>(
                getActivity(), android.R.layout.simple_spinner_dropdown_item, strType);
        return arrayAdapter;
    }


    @Override
    public void onResume() {
        super.onResume();

        ((MainActivity) getActivity()).setTitle("Home");
    }

    /**
     * Get personal use task
     */
    private class GetGroupByCategories extends AsyncTask<Void, Void, List<ProductRanking>> {

        @Override
        protected List<ProductRanking> doInBackground(Void... voids) {
            return ((MyApplication) getActivity().getApplicationContext()).getDatabase().productRankingDao().loadAllProductRankingGroupBy();
        }

        @Override
        protected void onPostExecute(List<ProductRanking> productRankings) {
            super.onPostExecute(productRankings);
            if (productRankings != null) {
                globalProductRankings.clear();
                globalProductRankings.addAll(productRankings);

                spinner.setAdapter(createTypeAdapter());
            } else {
                // TODO: 14-06-2019 Empty list handling
            }
        }
    }

    /**
     * Server call for getting json using retrofit
     * Assuming that data will change daily keeping this call on every load.
     */
    public void serverCallForGson() {
        apiHelper.getJson(new APIHelper.ICallback() {
            @Override
            public void onSuccess(Response body) {
                if (body != null) {
                    DataProcessor dataProcessor = new DataProcessor(getActivity());
                    dataProcessor.processData(body);
                }
            }

            @Override
            public void onFailure(ResponseBody responseBody) {

            }
        });
    }
}
