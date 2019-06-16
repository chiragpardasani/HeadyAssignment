package com.headyassignment.viewmodel;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MediatorLiveData;
import android.support.annotation.NonNull;

import com.headyassignment.MyApplication;
import com.headyassignment.db.entity.Category;

import java.util.List;

public class CategoryViewModel extends AndroidViewModel {

    // MediatorLiveData can observe other LiveData objects and react on their emissions.
    private final MediatorLiveData<List<Category>> mObservableCategories;
    Application application;

    public CategoryViewModel(@NonNull Application application) {
        super(application);

        mObservableCategories = new MediatorLiveData<>();
        // set by default null,  until we get data from the database.
        mObservableCategories.setValue(null);
        this.application = application;
    }

    /**
     * Expose the LiveData Categories query so the UI can observe it.
     */
    public LiveData<List<Category>> getCategories(long id) {
        LiveData<List<Category>> categories = ((MyApplication) application).getRepository()
                .getCategoryByParentId(id);

        // observe the changes of the products from the database and forward them
        mObservableCategories.addSource(categories, mObservableCategories::setValue);

        return mObservableCategories;
    }
}
