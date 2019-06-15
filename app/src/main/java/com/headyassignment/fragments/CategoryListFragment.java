package com.headyassignment.fragments;


import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.headyassignment.R;
import com.headyassignment.activities.MainActivity;
import com.headyassignment.adapter.CategoryAdapter;
import com.headyassignment.db.entity.Category;
import com.headyassignment.viewmodel.CategoryViewModel;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * A simple {@link Fragment} subclass.
 */
public class CategoryListFragment extends Fragment {

    public static final String EXTRA_ID = "extra_id";

    @BindView(R.id.fragCategoryList_recyclerView)
    RecyclerView recyclerView;

    List<LocalCategory> localCategories;
    CategoryAdapter categoryAdapter;
    long id;
    long selectedParent_id = 0;
    CategoryViewModel viewModel;

    public CategoryListFragment() {
        // Required empty public constructor
    }

    public static CategoryListFragment newInstance(long id) {
        CategoryListFragment categoryListFragment = new CategoryListFragment();
        Bundle bundle = new Bundle();
        bundle.putLong(EXTRA_ID, id);
        categoryListFragment.setArguments(bundle);
        return categoryListFragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_category_list, container, false);
        ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        id = getArguments().getLong(EXTRA_ID);

        localCategories = new ArrayList<>();
        categoryAdapter = new CategoryAdapter(getActivity(), localCategories);
        categoryAdapter.setOnItemClickListener(new CategoryAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(LocalCategory localCategory, int type) {
                switch (type) {
                    case CategoryAdapter.TYPE_EXPAND:
                        selectedParent_id = localCategory.getId();
                        viewModel.setID(selectedParent_id);
                        break;
                    case CategoryAdapter.TYPE_COLLAPSE:
                        localCategory.setOpened(false);
                        localCategory.setCategories(new ArrayList<>());
                        categoryAdapter.notifyDataSetChanged();
                        break;
                    case CategoryAdapter.TYPE_SUB_CATEGORY:
                        ((MainActivity) getActivity()).replaceFragment(ProductListFragment.newInstance(localCategory.getId(), localCategory.getName()), true);
                        break;
                }
            }
        });
        recyclerView.setAdapter(categoryAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
    }

    private void subscribeUi(CategoryViewModel viewModel) {
        // Update the list when the data changes
        viewModel.getCategories(id).observe(this, new Observer<List<Category>>() {
            @Override
            public void onChanged(@Nullable List<Category> categories) {
                if (categories != null) {
                    if (selectedParent_id > 0) {
                        LocalCategory parentLocalCategory = getCategoryId();
                        List<LocalCategory> subCategories = new ArrayList<>();
                        for (Category category : categories) {
                            LocalCategory localCategory = new LocalCategory();
                            localCategory.setId(category.getId());
                            localCategory.setName(category.getName());
                            subCategories.add(localCategory);
                        }
                        if (parentLocalCategory != null) {
                            parentLocalCategory.setOpened(true);
                            parentLocalCategory.setCategories(subCategories);
                        }

                    } else {
                        localCategories.clear();
                        for (Category category : categories) {
                            LocalCategory localCategory = new LocalCategory();
                            localCategory.setId(category.getId());
                            localCategory.setName(category.getName());
                            localCategories.add(localCategory);
                        }
                    }
                    categoryAdapter.notifyDataSetChanged();
                } else {
                    // TODO: 14-06-2019 Empty list handling
                }
            }
        });
    }

    @Override
    public void onPause() {
        super.onPause();
        selectedParent_id = 0;
    }

    @Override
    public void onResume() {
        super.onResume();
        ((MainActivity) getActivity()).setTitle("Categories");
        viewModel = ViewModelProviders.of(this).get(CategoryViewModel.class);
        viewModel.setID(id);
        subscribeUi(viewModel);
    }

    public static class LocalCategory {
        long id;
        String name;
        boolean isOpened;
        List<LocalCategory> categories;

        public boolean isOpened() {
            return isOpened;
        }

        public void setOpened(boolean opened) {
            isOpened = opened;
        }

        public long getId() {
            return id;
        }

        public void setId(long id) {
            this.id = id;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public List<LocalCategory> getCategories() {
            return categories;
        }

        public void setCategories(List<LocalCategory> categories) {
            this.categories = categories;
        }
    }

    public LocalCategory getCategoryId() {
        for (LocalCategory localCategory : localCategories) {
            if (localCategory.getId() == selectedParent_id) {
                return localCategory;
            }
        }

        return null;
    }
}
