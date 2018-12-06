package challenge.gecoding.models;

import android.os.Parcel;
import android.os.Parcelable;

public class CityModel implements Parcelable {

    private String city;
    private String cityAscii;
    private String latitude;
    private String langitude;
    private String population;
    private String country;
    private String iso2;
    private String iso3;
    private String province;

    public CityModel() {
    }

    public CityModel(String city, String cityAscii, String latitude, String langitude, String population, String country, String iso2, String iso3, String province) {
        this.city = city;
        this.cityAscii = cityAscii;
        this.latitude = latitude;
        this.langitude = langitude;
        this.population = population;
        this.country = country;
        this.iso2 = iso2;
        this.iso3 = iso3;
        this.province = province;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getCityAscii() {
        return cityAscii;
    }

    public void setCityAscii(String cityAscii) {
        this.cityAscii = cityAscii;
    }

    public String getLatitude() {
        return latitude;
    }

    public void setLatitude(String latitude) {
        this.latitude = latitude;
    }

    public String getLangitude() {
        return langitude;
    }

    public void setLangitude(String langitude) {
        this.langitude = langitude;
    }

    public String getPopulation() {
        return population;
    }

    public void setPopulation(String population) {
        this.population = population;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getIso2() {
        return iso2;
    }

    public void setIso2(String iso2) {
        this.iso2 = iso2;
    }

    public String getIso3() {
        return iso3;
    }

    public void setIso3(String iso3) {
        this.iso3 = iso3;
    }

    public String getProvince() {
        return province;
    }

    public void setProvince(String province) {
        this.province = province;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.city);
        dest.writeString(this.cityAscii);
        dest.writeString(this.latitude);
        dest.writeString(this.langitude);
        dest.writeString(this.population);
        dest.writeString(this.country);
        dest.writeString(this.iso2);
        dest.writeString(this.iso3);
        dest.writeString(this.province);
    }

    protected CityModel(Parcel in) {
        this.city = in.readString();
        this.cityAscii = in.readString();
        this.latitude = in.readString();
        this.langitude = in.readString();
        this.population = in.readString();
        this.country = in.readString();
        this.iso2 = in.readString();
        this.iso3 = in.readString();
        this.province = in.readString();
    }

    public static final Parcelable.Creator<CityModel> CREATOR = new Parcelable.Creator<CityModel>() {
        @Override
        public CityModel createFromParcel(Parcel source) {
            return new CityModel(source);
        }

        @Override
        public CityModel[] newArray(int size) {
            return new CityModel[size];
        }
    };
}
