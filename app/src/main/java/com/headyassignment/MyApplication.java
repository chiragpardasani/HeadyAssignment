package com.headyassignment;

import android.app.Application;

import com.headyassignment.api.APIHelper;
import com.headyassignment.db.AppDatabase;
import com.headyassignment.db.DataRepository;

public class MyApplication extends Application {

    private AppExecutors mAppExecutors;
    APIHelper apiHelper;

    @Override
    public void onCreate() {
        super.onCreate();
        apiHelper = new APIHelper();
        mAppExecutors = new AppExecutors();
    }

    public APIHelper getApiHelper() {
        return apiHelper;
    }

    public AppDatabase getDatabase() {
        return AppDatabase.getInstance(this, mAppExecutors);
    }

    public AppExecutors getmAppExecutors() {
        return mAppExecutors;
    }

    public DataRepository getRepository() {
        return DataRepository.getInstance(getDatabase());
    }
}
