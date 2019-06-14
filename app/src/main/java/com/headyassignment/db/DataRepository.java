package com.headyassignment.db;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MediatorLiveData;

import com.headyassignment.db.entity.Category;
import com.headyassignment.db.entity.Product;

import java.util.List;

/**
 * Repository handling the work with products and comments.
 */
public class DataRepository {

    private MediatorLiveData<List<Product>> mObservableProducts;

    private MediatorLiveData<List<Category>> mObservableCategories;

    private static DataRepository sInstance;

    private final AppDatabase mDatabase;

    private DataRepository(final AppDatabase database) {
        mDatabase = database;

        mObservableProducts = new MediatorLiveData<>();
        mObservableCategories = new MediatorLiveData<>();

        mObservableProducts.addSource(mDatabase.productDao().loadAllProducts(),
                productEntities -> {
                    mObservableProducts.postValue(productEntities);
                });

        mObservableCategories.addSource(mDatabase.categoryDao().loadAllCategories(),
                categories -> {
                    mObservableCategories.postValue(categories);
                });
    }

    public static DataRepository getInstance(final AppDatabase database) {
        if (sInstance == null) {
            synchronized (DataRepository.class) {
                if (sInstance == null) {
                    sInstance = new DataRepository(database);
                }
            }
        }
        return sInstance;
    }

    /**
     * Get the list of products from the database and get notified when the data changes.
     */
    public LiveData<List<Product>> getProducts() {
        return mObservableProducts;
    }

    /**
     * Get the list of categories from the database and get notified when the data changes.
     */
    public LiveData<List<Category>> getCategories() {
        return mObservableCategories;
    }
}
