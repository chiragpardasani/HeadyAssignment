package com.headyassignment.viewmodel;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MediatorLiveData;
import android.support.annotation.NonNull;

import com.headyassignment.MyApplication;
import com.headyassignment.db.entity.Product;

import java.util.List;

public class ProductViewModel extends AndroidViewModel {

    // MediatorLiveData can observe other LiveData objects and react on their emissions.
    private final MediatorLiveData<List<Product>> mObservableProducts;

    public ProductViewModel(@NonNull Application application) {
        super(application);

        mObservableProducts = new MediatorLiveData<>();
        // set by default null,  until we get data from the database.
        mObservableProducts.setValue(null);

        LiveData<List<Product>> products = ((MyApplication) application).getRepository()
                .getProducts();

        // observe the changes of the products from the database and forward them
        mObservableProducts.addSource(products, mObservableProducts::setValue);
    }

    /**
     * Expose the LiveData Products query so the UI can observe it.
     */
    public LiveData<List<Product>> getProducts() {
        return mObservableProducts;
    }
}
