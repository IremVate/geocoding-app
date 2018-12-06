package challenge.gecoding;

import android.databinding.DataBindingUtil;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import challenge.gecoding.databinding.ActivityMainBinding;
import challenge.gecoding.models.CityModel;

public class MainActivity extends AppCompatActivity {

    private ActivityMainBinding mainBinding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mainBinding = DataBindingUtil.setContentView(this, R.layout.activity_main);

        showFragmentAdd(new GeoCodingFragment(), null);

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }

    public void showFragmentAdd(Fragment newFragment, Bundle args) {
        if (newFragment != null) {
            newFragment.setArguments(args);
            getSupportFragmentManager()
                    .beginTransaction()
                    .add(R.id.frame, newFragment)
                    .addToBackStack(null)
                    .commit();
        }
    }

}
