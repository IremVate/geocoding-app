package challenge.gecoding;


import android.arch.lifecycle.Observer;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.SearchView;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.support.v7.widget.LinearLayoutManager;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import challenge.gecoding.adapter.GeoCodingAdapter;
import challenge.gecoding.databinding.FragmentGeocodingBinding;
import challenge.gecoding.models.CityModel;
import challenge.gecoding.viewmodels.GeoCodingViewModel;

public class GeoCodingFragment extends Fragment implements SearchView.OnQueryTextListener {

    private GeoCodingAdapter adapter;

    private GeoCodingViewModel geoCodingVM;

    private LinearLayoutManager layoutManager;

    private FragmentGeocodingBinding geocodingBinding;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        geocodingBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_geocoding, container, false);

        layoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false);

        geoCodingVM = new GeoCodingViewModel(getContext());

        geocodingBinding.searchView.setOnQueryTextListener(this);

        setUpAdapter();

        return geocodingBinding.getRoot();
    }

    private void setUpAdapter(){

        geoCodingVM.citiesMLD.observe(this, new Observer<ArrayList<CityModel>>() {
            @Override
            public void onChanged(@Nullable ArrayList<CityModel> cityModels) {
                geocodingBinding.recyclerViewLocations.setLayoutManager(layoutManager);

                adapter = new GeoCodingAdapter(getContext(), cityModels);

                geocodingBinding.recyclerViewLocations.setAdapter(adapter);
            }
        });

    }

    @Override
    public boolean onQueryTextSubmit(String query) {
        return false;
    }

    @Override
    public boolean onQueryTextChange(String newText) {
        adapter.getFilter().filter(newText);
        return false;
    }
}
