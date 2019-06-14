package com.headyassignment.db;

/**
 * Created by Abhijit on 20-09-2018.
 */

import android.arch.persistence.db.SupportSQLiteDatabase;
import android.arch.persistence.room.Database;
import android.arch.persistence.room.Room;
import android.arch.persistence.room.RoomDatabase;
import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.VisibleForTesting;

import com.headyassignment.AppExecutors;
import com.headyassignment.db.dao.CategoryDao;
import com.headyassignment.db.dao.ProductDao;
import com.headyassignment.db.dao.VariantDao;
import com.headyassignment.db.entity.Category;
import com.headyassignment.db.entity.Product;
import com.headyassignment.db.entity.Variant;

import java.util.List;

@Database(entities = {Product.class, Category.class, Variant.class}, version = 1)
public abstract class AppDatabase extends RoomDatabase {

    private static AppDatabase sInstance;

    public abstract ProductDao productDao();

    public abstract CategoryDao categoryDao();

    public abstract VariantDao variantDao();

    @VisibleForTesting
    public static final String DATABASE_NAME = "heady-assignment-db";


    public static AppDatabase getInstance(final Context context, final AppExecutors executors) {
        if (sInstance == null) {
            synchronized (AppDatabase.class) {
                if (sInstance == null) {
                    sInstance = buildDatabase(context.getApplicationContext());
                }
            }
        }
        return sInstance;
    }

    /**
     * Build the database. {@link Builder#build()} only sets up the database configuration and
     * creates a new instance of the database.
     * The SQLite database is only created when it's accessed for the first time.
     */
    private static AppDatabase buildDatabase(final Context appContext) {
        return Room.databaseBuilder(appContext, AppDatabase.class, DATABASE_NAME)
                .fallbackToDestructiveMigration()
                .addCallback(new Callback() {
                    @Override
                    public void onCreate(@NonNull SupportSQLiteDatabase db) {
                        super.onCreate(db);
                    }
                }).build();
    }

    public void insertProducts(final AppDatabase database, AppExecutors appExecutors, final List<Product> products) {
        appExecutors.diskIO().execute(() -> {
            database.runInTransaction(() -> {
                database.productDao().insertAll(products);
            });
        });
    }

    public void insertCategories(final AppDatabase database, AppExecutors appExecutors, final List<Category> categories) {
        appExecutors.diskIO().execute(() -> {
            database.runInTransaction(() -> {
                database.categoryDao().insertAll(categories);
            });
        });
    }

    public void insertVariants(final AppDatabase database, AppExecutors appExecutors, final List<Variant> variants) {
        appExecutors.diskIO().execute(() -> {
            database.runInTransaction(() -> {
                database.variantDao().insertAll(variants);
            });
        });
    }
}