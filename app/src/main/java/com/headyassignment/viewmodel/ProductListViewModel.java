package com.headyassignment.viewmodel;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MediatorLiveData;
import android.support.annotation.NonNull;

import com.headyassignment.MyApplication;
import com.headyassignment.db.entity.Product;

import java.util.List;

public class ProductListViewModel extends AndroidViewModel {

    // MediatorLiveData can observe other LiveData objects and react on their emissions.
    private final MediatorLiveData<List<Product>> mObservableProducts;

    Application application;

    public ProductListViewModel(@NonNull Application application) {
        super(application);

        this.application = application;
        mObservableProducts = new MediatorLiveData<>();
        // set by default null,  until we get data from the database.
        mObservableProducts.setValue(null);
    }

    /**
     * Expose the LiveData Products query so the UI can observe it.
     */
    public LiveData<List<Product>> getProducts(long id) {
        LiveData<List<Product>> products = ((MyApplication) application).getRepository()
                .getProductsByCategoryId(id);

        // observe the changes of the products from the database and forward them
        mObservableProducts.addSource(products, mObservableProducts::setValue);
        return mObservableProducts;
    }
}
