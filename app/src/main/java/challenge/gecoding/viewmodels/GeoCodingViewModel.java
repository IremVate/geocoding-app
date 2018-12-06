package challenge.gecoding.viewmodels;

import android.arch.lifecycle.MutableLiveData;
import android.content.Context;
import android.databinding.BaseObservable;
import android.databinding.Bindable;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;

import challenge.gecoding.AppConstants;
import challenge.gecoding.GEOApplication;
import challenge.gecoding.models.CityModel;

public class GeoCodingViewModel extends BaseObservable {

    private Context context;

    public MutableLiveData<ArrayList<CityModel>> citiesMLD = new MutableLiveData<>();

    public GeoCodingViewModel(Context context) {
        this.context = context;

        callData();
    }

    private void callData(){
        if(GEOApplication.getInstance().getCityListFromFile() != null){
           fromLocalDate();
        }
        else {
            readFromCSV();
        }
    }

    public ArrayList<CityModel> readFromCSV() {
        ArrayList<CityModel> cityModels = new ArrayList<>();

        StringBuilder returnString = new StringBuilder();
        InputStream fIn = null;
        InputStreamReader isr = null;
        BufferedReader input = null;
        try {
            fIn = context.getResources().getAssets().open(AppConstants.CSV_FILENAME, Context.MODE_WORLD_READABLE);
            isr = new InputStreamReader(fIn);
            input = new BufferedReader(isr);
            String line = "";

            int iteration = 0;
            while ((line = input.readLine()) != null) {

                if(iteration == 0) {
                    iteration++;
                    continue;
                }
                String[] row = line.split(";");

                String city = row[0];
                String cityAscii = row[1];
                String lat = row[2];
                String lang = row[3];
                String pop = row[4];
                String country = row[5];
                String iso2 = row[6];
                String iso3 = row[7];
                String province = row[8];

                cityModels.add(new CityModel(city, cityAscii, lat, lang, pop, country, iso2, iso3, province));
            }

        }
        catch (Exception e) {
            e.getMessage();
        }
        finally {
            try {
                if (isr != null)
                    isr.close();
                if (fIn != null)
                    fIn.close();
                if (input != null)
                    input.close();
            } catch (Exception e2) {
                e2.getMessage();
            }
        }

        citiesMLD.postValue(cityModels);

        GEOApplication.getInstance().setCityListFromFile(cityModels);

        return cityModels;
    }

    public void fromLocalDate(){
        citiesMLD.postValue(GEOApplication.getInstance().getCityListFromFile());
    }
}
