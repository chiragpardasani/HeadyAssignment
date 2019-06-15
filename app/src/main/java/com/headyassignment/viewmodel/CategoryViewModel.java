package com.headyassignment.viewmodel;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MediatorLiveData;
import android.arch.lifecycle.ViewModel;
import android.arch.lifecycle.ViewModelProvider;
import android.support.annotation.NonNull;

import com.headyassignment.MyApplication;
import com.headyassignment.db.entity.Category;

import java.util.List;

public class CategoryViewModel extends AndroidViewModel {

    // MediatorLiveData can observe other LiveData objects and react on their emissions.
    private final MediatorLiveData<List<Category>> mObservableCategories;
    long Id;
    Application application;

    public CategoryViewModel(@NonNull Application application, long id) {
        super(application);

        mObservableCategories = new MediatorLiveData<>();
        // set by default null,  until we get data from the database.
        mObservableCategories.setValue(null);
        Id = id;
        this.application = application;
    }

    /**
     * Expose the LiveData Categories query so the UI can observe it.
     */
    public LiveData<List<Category>> getCategories() {

        LiveData<List<Category>> categories = ((MyApplication) application).getRepository()
                .getCategoryByParentId(Id);

        // observe the changes of the products from the database and forward them
        mObservableCategories.addSource(categories, mObservableCategories::setValue);

        return mObservableCategories;
    }

    public void setID(long id) {
        Id = id;

        LiveData<List<Category>> categories = ((MyApplication) application).getRepository()
                .getCategoryByParentId(Id);

        // observe the changes of the products from the database and forward them
        mObservableCategories.addSource(categories, mObservableCategories::setValue);
    }

    /**
     * A creator is used to inject the assestNo into the ViewModel
     * <p>
     * This creator is to showcase how to inject dependencies into ViewModels. It's not
     * actually necessary in this case, as the product ID can be passed in a public method.
     */
    public static class Factory extends ViewModelProvider.NewInstanceFactory {

        @NonNull
        private final Application mApplication;

        private final long id;

        public Factory(@NonNull Application application, long id) {
            mApplication = application;
            this.id = id;
        }

        @Override
        public <T extends ViewModel> T create(Class<T> modelClass) {
            //noinspection unchecked
            return (T) new CategoryViewModel(mApplication, id);
        }
    }
}
