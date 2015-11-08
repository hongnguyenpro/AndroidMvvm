/*
 * Copyright (C) 2015 Brian Lee (@hiBrianLee)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.hibrianlee.mvvmapp.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

import com.hibrianlee.mvvmapp.MvvmApplication;
import com.hibrianlee.mvvmapp.inject.ActivityComponent;
import com.hibrianlee.mvvmapp.inject.ActivityModule;
import com.hibrianlee.mvvmapp.inject.AppComponent;
import com.hibrianlee.mvvmapp.inject.DaggerActivityComponent;
import com.hibrianlee.mvvmapp.viewmodel.ViewModel;

public abstract class ViewModelActivity extends AppCompatActivity {

    private static final String EXTRA_VIEW_MODEL_STATE = "viewModelState";

    private ActivityComponent activityComponent;
    private ViewModel viewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        AppComponent appComponent = ((MvvmApplication) getApplication()).getAppComponent();
        appComponent.inject(this);

        activityComponent = DaggerActivityComponent.builder()
                .appComponent(appComponent)
                .activityModule(new ActivityModule(this))
                .build();

        ViewModel.State savedViewModelState = null;
        if (savedInstanceState != null) {
            savedViewModelState = savedInstanceState.getParcelable(EXTRA_VIEW_MODEL_STATE);
        }
        viewModel = createViewModel(savedViewModelState);
    }

    @Nullable
    protected ViewModel createViewModel(@Nullable ViewModel.State savedViewModelState) {
        return null;
    }

    @Override
    protected void onStart() {
        super.onStart();
        if (viewModel != null) {
            viewModel.onStart();
        }
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        if (viewModel != null) {
            outState.putParcelable(EXTRA_VIEW_MODEL_STATE, viewModel.getInstanceState());
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (viewModel != null) {
            viewModel.onStop();
        }
    }

    public final ActivityComponent getActivityComponent() {
        return activityComponent;
    }
}
