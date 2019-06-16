package com.headyassignment.viewmodel;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MediatorLiveData;
import android.support.annotation.NonNull;

import com.headyassignment.MyApplication;
import com.headyassignment.db.entity.Product;

public class ProductViewModel extends AndroidViewModel {

    // MediatorLiveData can observe other LiveData objects and react on their emissions.
    private final MediatorLiveData<Product> mObservableProduct;

    Application application;

    public ProductViewModel(@NonNull Application application) {
        super(application);

        this.application = application;
        mObservableProduct = new MediatorLiveData<>();
        // set by default null,  until we get data from the database.
        mObservableProduct.setValue(null);
    }

    /**
     * Get single product by id
     *
     * @param id
     * @return
     */
    public LiveData<Product> getSingleProduct(long id) {
        LiveData<Product> product = ((MyApplication) application).getRepository()
                .getSingleProduct(id);

        // observe the changes of the products from the database and forward them
        mObservableProduct.addSource(product, mObservableProduct::setValue);
        return mObservableProduct;
    }
}
