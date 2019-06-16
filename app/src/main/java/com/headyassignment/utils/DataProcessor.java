package com.headyassignment.utils;

import android.content.Context;

import com.headyassignment.MyApplication;
import com.headyassignment.db.AppDatabase;
import com.headyassignment.db.entity.Category;
import com.headyassignment.db.entity.Product;
import com.headyassignment.db.entity.ProductRanking;
import com.headyassignment.db.entity.Variant;

import java.util.ArrayList;
import java.util.List;

public class DataProcessor {

    private static final String TAG = "DataProcessor";

    Context context;

    List<Category> categories;
    List<Product> products;
    List<Variant> variants;
    List<ProductRanking> productRankings;

    AppDatabase appDatabase;

    public DataProcessor(Context context) {
        this.context = context;
        categories = new ArrayList<>();
        products = new ArrayList<>();
        variants = new ArrayList<>();
        productRankings = new ArrayList<>();
        appDatabase = ((MyApplication) context.getApplicationContext()).getDatabase();
    }

    public void processData(Response response) {
        List<Response.ResponseCategory> responseCategories = response.getCategories();
        List<Response.ResponseRanking> responseRankings = response.getRankings();

        for (Response.ResponseCategory responseCategory : responseCategories) {
            Category category = new Category();
            category.setId(responseCategory.getId());
            category.setName(responseCategory.getName());
            category.setChild_categories(responseCategory.getChild_categories());
            categories.add(category);

            if (responseCategory.getProducts() != null) {
                for (Response.ResponseProduct responseProduct : responseCategory.getProducts()) {
                    Product product = new Product();
                    product.setId(responseProduct.getId());
                    product.setCategory_id(responseCategory.getId());
                    product.setName(responseProduct.getName());
                    product.setDate_added(responseProduct.getDate_added());
                    product.setTax(responseProduct.getTax());
                    products.add(product);

                    if (responseProduct.getVariants() != null) {
                        for (Response.ResponseVariant responseVariant : responseProduct.getVariants()) {
                            Variant variant = new Variant();
                            variant.setId(responseVariant.getId());
                            variant.setColor(responseVariant.getColor());
                            variant.setPrice(responseVariant.getPrice());
                            variant.setSize(responseVariant.getSize());
                            variant.setProduct_id(responseProduct.getId());
                            variants.add(variant);
                        }
                    }
                }
            }
        }

        // For inserting parent id once we have all the categories ready
        for (Category category : categories) {
            for (long id : category.getChild_categories()) {
                Category categoryChild = findCategoryFromId(id);
                categoryChild.setParent_id(category.getId());
            }
        }

        for (Response.ResponseRanking responseRankingProduct : response.getRankings()) {

            for (Response.ResponseRankingProduct rankingProduct : responseRankingProduct.getProducts()) {
                ProductRanking productRanking = new ProductRanking();
                productRanking.setRanking(responseRankingProduct.getRanking());
                productRanking.setProduct_id(rankingProduct.getId());
                if (rankingProduct.getOrder_count() > 0) {
                    productRanking.setCount(rankingProduct.getOrder_count());
                }

                if (rankingProduct.getView_count() > 0) {
                    productRanking.setCount(rankingProduct.getView_count());
                }

                if (rankingProduct.getShares() > 0) {
                    productRanking.setCount(rankingProduct.getShares());
                }

                productRankings.add(productRanking);
            }
        }

        saveModelsToDb();
    }

    /**
     * Save list to database
     */
    private void saveModelsToDb() {
        appDatabase.insertProducts(appDatabase, ((MyApplication) context.getApplicationContext()).getmAppExecutors(), products);
        appDatabase.insertCategories(appDatabase, ((MyApplication) context.getApplicationContext()).getmAppExecutors(), categories);
        appDatabase.insertVariants(appDatabase, ((MyApplication) context.getApplicationContext()).getmAppExecutors(), variants);
        appDatabase.deleteAll(appDatabase, ((MyApplication) context.getApplicationContext()).getmAppExecutors());
        appDatabase.insertProductRanking(appDatabase, ((MyApplication) context.getApplicationContext()).getmAppExecutors(), productRankings);
    }

    /**
     * Find category from id
     *
     * @param id
     * @return
     */
    private Category findCategoryFromId(long id) {
        for (Category category : categories) {
            if (category.getId() == id) {
                return category;
            }
        }
        return null;
    }
}
