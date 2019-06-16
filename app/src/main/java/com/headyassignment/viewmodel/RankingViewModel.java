package com.headyassignment.viewmodel;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MediatorLiveData;
import android.support.annotation.NonNull;

import com.headyassignment.MyApplication;
import com.headyassignment.db.entity.ProductRanking;

import java.util.List;

public class RankingViewModel extends AndroidViewModel {

    // MediatorLiveData can observe other LiveData objects and react on their emissions.
    private final MediatorLiveData<List<ProductRanking>> mObservableRanking;

    Application application;

    public RankingViewModel(@NonNull Application application) {
        super(application);

        this.application = application;
        mObservableRanking = new MediatorLiveData<>();
        // set by default null,  until we get data from the database.
        mObservableRanking.setValue(null);
    }

    /**
     * Expose the LiveData ProductRankings query so the UI can observe it.
     */
    public LiveData<List<ProductRanking>> getRankingWithCount(String ranking) {
        LiveData<List<ProductRanking>> products = ((MyApplication) application).getRepository()
                .getRankingWithCount(ranking);
        // observe the changes of the products from the database and forward them
        mObservableRanking.addSource(products, mObservableRanking::setValue);
        return mObservableRanking;
    }

    /**
     * Expose the LiveData ProductRankings query so the UI can observe it.
     */
    public LiveData<List<ProductRanking>> getRankingByProduct(long product_id) {
        LiveData<List<ProductRanking>> products = ((MyApplication) application).getRepository()
                .getRankingByProduct(product_id);
        // observe the changes of the products from the database and forward them
        mObservableRanking.addSource(products, mObservableRanking::setValue);
        return mObservableRanking;
    }
}
