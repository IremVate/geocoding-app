package challenge.gecoding.details;

import android.Manifest;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.databinding.DataBindingUtil;
import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import challenge.gecoding.AppConstants;
import challenge.gecoding.GEOApplication;
import challenge.gecoding.MainActivity;
import challenge.gecoding.R;
import challenge.gecoding.commands.SaveCityActions;
import challenge.gecoding.databinding.ActivityCityDetailsBinding;
import challenge.gecoding.models.CityModel;

public class CityDetailsActivity extends AppCompatActivity implements OnMapReadyCallback, GoogleMap.OnMapClickListener, SaveCityActions {

    private GoogleMap mMap;

    private CityModel cityModel;

    private ArrayList<CityModel> allCities;

    private boolean isButtonEnabled = false;

    private ActivityCityDetailsBinding cityDetailsBinding;

    private static final int LOCATION_PERMISSION_REQUEST_CODE = 1;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        cityDetailsBinding = DataBindingUtil.setContentView(this, R.layout.activity_city_details);

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);

        mapFragment.getMapAsync(this);

        cityModel = getIntent().getParcelableExtra("clickedCity") != null ? (CityModel) getIntent().getParcelableExtra("clickedCity") : new CityModel();

        cityDetailsBinding.setCityData(cityModel);
        cityDetailsBinding.setActions(this);
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        mMap.setOnMapClickListener(this);

        enableMyLocationIfPermitted();

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case LOCATION_PERMISSION_REQUEST_CODE: {
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    enableMyLocationIfPermitted();
                }
                else {
                    getLocationFromAddress(cityModel.getLatitude(), cityModel.getLangitude());

                }
                return;
            }

        }
    }

    @Override
    public void onMapClick(LatLng latLng) {

        mMap.clear();

        isButtonEnabled = true;

        MarkerOptions markerOptions = new MarkerOptions();
        markerOptions.position(latLng);

        String street = "";

        Geocoder geocoder = new Geocoder(this, Locale.getDefault());
        try {
            List<Address> addresses = geocoder.getFromLocation(latLng.latitude, latLng.longitude, 1);
            if (addresses != null && addresses.size() > 0) {
                android.location.Address returnedAddress = addresses.get(0);
                street = returnedAddress.getAddressLine(0);

                cityModel.setCity(returnedAddress.getAdminArea());
                cityModel.setCityAscii(returnedAddress.getAdminArea());
                cityModel.setLatitude(String.valueOf(returnedAddress.getLatitude()));
                cityModel.setLangitude(String.valueOf(returnedAddress.getLongitude()));
                cityModel.setPopulation(returnedAddress.getPostalCode());
                cityModel.setCountry(returnedAddress.getCountryName());
                cityModel.setIso2(returnedAddress.getCountryCode());
                cityModel.setIso3(returnedAddress.getCountryCode());
                cityModel.setProvince(returnedAddress.getSubAdminArea() != null ? returnedAddress.getSubAdminArea()  : returnedAddress.getAdminArea());

            }
        }
        catch (IOException e) {
            e.printStackTrace();
        }

        cityDetailsBinding.editTextCity.setText(street);
        markerOptions.title(street);

        mMap.addMarker(markerOptions).showInfoWindow();
        mMap.moveCamera(CameraUpdateFactory.newLatLng(latLng));
    }


    @Override
    public void saveCity() {

        if(!isButtonEnabled){
            Toast.makeText(this, getResources().getString(R.string.save_city_warning), Toast.LENGTH_SHORT).show();
        }
        else {
            openAlert();
        }
    }

    private void enableMyLocationIfPermitted() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_FINE_LOCATION}, LOCATION_PERMISSION_REQUEST_CODE);
        }
        else if (mMap != null) {
            getLocationFromAddress(cityModel.getLatitude(), cityModel.getLangitude());
        }
    }

    public void getLocationFromAddress(String lat, String lang) {

        mMap.clear();

        LatLng latLng = new LatLng(Double.valueOf(lat), Double.valueOf(lang));

        mMap.addMarker(new MarkerOptions().position(latLng).title(cityModel.getCity() + ", " + cityModel.getProvince() + ", " + cityModel.getCountry())).showInfoWindow();;

        mMap.moveCamera(CameraUpdateFactory.newLatLng(latLng));
        mMap.animateCamera(CameraUpdateFactory.zoomTo(15.0f));

    }

    private void openAlert(){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage(getResources().getString(R.string.save_city_dialog_title))
                .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {

                public void onClick(DialogInterface dialog, int id) {
                         Toast.makeText(CityDetailsActivity.this, getResources().getString(R.string.save_city_message_success), Toast.LENGTH_SHORT).show();

                         handleCityList();

                         finish();

                         Intent intent = new Intent(CityDetailsActivity.this, MainActivity.class);
                         intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                         startActivity(intent);
                    }
                })
                .setNegativeButton(android.R.string.cancel, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                    }
                });

        builder.show();
    }

    private void handleCityList(){
        allCities = GEOApplication.getInstance().getCityListFromFile() != null ? GEOApplication.getInstance().getCityListFromFile() : new ArrayList<CityModel>();

        allCities.add(cityModel);
    }

}
