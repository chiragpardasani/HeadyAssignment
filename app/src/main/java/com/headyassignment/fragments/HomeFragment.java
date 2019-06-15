package com.headyassignment.fragments;


import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;

import com.headyassignment.MyApplication;
import com.headyassignment.R;
import com.headyassignment.activities.MainActivity;
import com.headyassignment.adapter.ProductAdapter;
import com.headyassignment.api.APIHelper;
import com.headyassignment.db.entity.Product;
import com.headyassignment.db.entity.ProductRanking;
import com.headyassignment.utils.DataProcessor;
import com.headyassignment.utils.Response;
import com.headyassignment.viewmodel.ProductViewModel;
import com.headyassignment.viewmodel.RankingViewModel;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import okhttp3.ResponseBody;

/**
 * A simple {@link Fragment} subclass.
 */
public class HomeFragment extends Fragment {

    public static final String TYPE_VIEW_ALL = "View All";

    @BindView(R.id.fragHome_recyclerView)
    RecyclerView recyclerView;

    @BindView(R.id.fragHome_edtSearch)
    EditText edtSearch;

    @BindView(R.id.fragHome_spinner)
    Spinner spinner;

    List<Product> globalProducts;
    List<ProductRanking> globalProductRankings;
    ProductAdapter productAdapter;

    APIHelper apiHelper;
    RankingViewModel viewModelRanking;
    ProductViewModel viewModel;

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
        recyclerView.setLayoutManager(new GridLayoutManager(getActivity(), 2));

        spinner.setAdapter(createTypeAdapter());
        viewModel =
                ViewModelProviders.of(this).get(ProductViewModel.class);
        subscribeUi(viewModel);

        viewModelRanking =
                ViewModelProviders.of(this).get(RankingViewModel.class);
        subscribeUiForIds();
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

        new GetGroupByCategories().execute();

        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String type = String.valueOf(parent.getItemAtPosition(position));
                if (!type.equalsIgnoreCase(TYPE_VIEW_ALL)) {
                    getRankingWithCount(type);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    private void subscribeUi(ProductViewModel viewModel) {
        // Update the list when the data changes
        viewModel.getProducts(0, null).observe(this, new Observer<List<Product>>() {
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

    private void subscribeUiForIds() {
        // Update the list when the data changes
        viewModel.getProducts(0, longs).observe(this, new Observer<List<Product>>() {
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

                viewModel.getProducts(0, longs);
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
}
