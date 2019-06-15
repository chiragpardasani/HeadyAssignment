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

    Application application;

    public ProductViewModel(@NonNull Application application) {
        super(application);

        this.application = application;
        mObservableProducts = new MediatorLiveData<>();
        // set by default null,  until we get data from the database.
        mObservableProducts.setValue(null);
    }

    /**
     * Expose the LiveData Products query so the UI can observe it.
     */
    public LiveData<List<Product>> getProducts(long id, List<Long> longs) {
        LiveData<List<Product>> products;
        if (id > 0) {
            products = ((MyApplication) application).getRepository()
                    .getProductsByCategoryId(id);
        } else {
            if (longs != null) {
                products = ((MyApplication) application).getRepository()
                        .getProductsByIds(longs);
            } else {
                products = ((MyApplication) application).getRepository()
                        .getProducts();
            }

        }

        // observe the changes of the products from the database and forward them
        mObservableProducts.addSource(products, mObservableProducts::setValue);
        return mObservableProducts;
    }
}
