package com.headyassignment.db;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MediatorLiveData;

import com.headyassignment.db.entity.Category;
import com.headyassignment.db.entity.Product;
import com.headyassignment.db.entity.ProductRanking;
import com.headyassignment.db.entity.ProductVariantPOJO;
import com.headyassignment.db.entity.Variant;

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
        return mDatabase.productDao().loadAllProducts();
    }

    public LiveData<Product> getSingleProduct(long id) {
        return mDatabase.productDao().getSingleProduct(id);
    }

    public LiveData<List<Product>> getProductsByCategoryId(long id) {
        return mDatabase.productDao().loadProductsByCategory(id);
    }

    public LiveData<List<Category>> getCategoryByParentId(long id) {
        return mDatabase.categoryDao().loadCategoriesById(id);
    }

    public LiveData<List<ProductRanking>> getRankingWithCount(String ranking) {
        return mDatabase.productRankingDao().loadAllProductRankingByRanking(ranking);
    }

    public LiveData<List<ProductRanking>> getRankingByProduct(long product_id) {
        return mDatabase.productRankingDao().loadAllProductRankingByProduct(product_id);
    }

    public LiveData<List<Variant>> getVariantByProduct(long id) {
        return mDatabase.variantDao().loadAllVariantsByProduct(id);
    }

    public LiveData<List<ProductVariantPOJO>> getVariantWithProducts(List<Long> longs) {
        return mDatabase.variantDao().loadAllVariantsWithProduct(longs);
    }

    public LiveData<List<ProductVariantPOJO>> getVariantForCategory(long id) {
        return mDatabase.variantDao().loadAllVariantsByCategory(id);
    }

    public LiveData<List<ProductVariantPOJO>> getVariantWithProducts() {
        return mDatabase.variantDao().loadAllVariantsWithProduct();
    }
}
