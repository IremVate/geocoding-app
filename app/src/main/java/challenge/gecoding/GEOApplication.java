package challenge.gecoding;

import java.util.ArrayList;

import challenge.gecoding.models.CityModel;

public class GEOApplication {

    private static GEOApplication instance = new GEOApplication();

    public static GEOApplication getInstance() {
        return instance;
    }


    private ArrayList<CityModel> cityListFromFile;

    public ArrayList<CityModel> getCityListFromFile() {
        return cityListFromFile;
    }

    public void setCityListFromFile(ArrayList<CityModel> cityListFromFile) {
        this.cityListFromFile = cityListFromFile;
    }
}
