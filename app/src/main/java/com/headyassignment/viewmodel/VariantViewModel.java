package com.headyassignment.viewmodel;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MediatorLiveData;
import android.support.annotation.NonNull;

import com.headyassignment.MyApplication;
import com.headyassignment.db.entity.ProductVariantPOJO;
import com.headyassignment.db.entity.Variant;

import java.util.List;

public class VariantViewModel extends AndroidViewModel {

    // MediatorLiveData can observe other LiveData objects and react on their emissions.
    private final MediatorLiveData<List<Variant>> mObservableVariant;

    private final MediatorLiveData<List<ProductVariantPOJO>> mObservableProductVariantPOJOs;

    Application application;

    public VariantViewModel(@NonNull Application application) {
        super(application);

        this.application = application;
        mObservableVariant = new MediatorLiveData<>();
        mObservableProductVariantPOJOs = new MediatorLiveData<>();
        // set by default null,  until we get data from the database.
        mObservableVariant.setValue(null);
        mObservableProductVariantPOJOs.setValue(null);
    }

    /**
     * Expose the LiveData Vriants query so the UI can observe it.
     */
    public LiveData<List<Variant>> getRankingWithCount(long id) {
        LiveData<List<Variant>> products = ((MyApplication) application).getRepository()
                .getVariantByProduct(id);
        // observe the changes of the products from the database and forward them
        mObservableVariant.addSource(products, mObservableVariant::setValue);
        return mObservableVariant;
    }

    /**
     * Expose the LiveData Vriants query so the UI can observe it.
     */
    public LiveData<List<ProductVariantPOJO>> getProductWithVariant(List<Long> longs) {
        LiveData<List<ProductVariantPOJO>> products;
        if (longs.size() > 0) {
            products = ((MyApplication) application).getRepository()
                    .getVariantWithProducts(longs);
        } else {
            products = ((MyApplication) application).getRepository()
                    .getVariantWithProducts();
        }
        // observe the changes of the products from the database and forward them
        mObservableProductVariantPOJOs.addSource(products, mObservableProductVariantPOJOs::setValue);
        return mObservableProductVariantPOJOs;
    }

    /**
     * Expose the LiveData Vriants query so the UI can observe it.
     */
    public LiveData<List<ProductVariantPOJO>> getProductByCategoryId(long id) {
        LiveData<List<ProductVariantPOJO>> products;

        products = ((MyApplication) application).getRepository()
                .getVariantForCategory(id);

        // observe the changes of the products from the database and forward them
        mObservableProductVariantPOJOs.addSource(products, mObservableProductVariantPOJOs::setValue);
        return mObservableProductVariantPOJOs;
    }
}
