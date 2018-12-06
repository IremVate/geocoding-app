package challenge.gecoding.adapter;

import android.content.Context;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Parcelable;
import android.support.annotation.NonNull;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import challenge.gecoding.R;
import challenge.gecoding.databinding.ItemCityBinding;
import challenge.gecoding.details.CityDetailsActivity;
import challenge.gecoding.models.CityModel;

public class GeoCodingAdapter extends RecyclerView.Adapter<GeoCodingAdapter.ViewHolder> implements Filterable {

    private Context context;

    private CustomFilter mFilter;

    private ItemCityBinding cityBinding;

    private ArrayList<CityModel> mFilterList;

    private ArrayList<CityModel> unFilteredList;
    private ArrayList<CityModel> cityList;

    public GeoCodingAdapter(Context context, ArrayList<CityModel> cityList) {
        this.context = context;
        this.cityList = cityList;

        this.mFilterList = cityList;
        this.unFilteredList = cityList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());

        cityBinding = DataBindingUtil.inflate(inflater, R.layout.item_city, parent, false);

        return new ViewHolder(cityBinding.getRoot());
    }

    @Override
    public void onBindViewHolder(@NonNull GeoCodingAdapter.ViewHolder holder, int position) {

        final CityModel currentModel = cityList.get(position);

        holder.textCity.setText(currentModel.getCity());
        holder.textCountry.setText(String.format(context.getResources().getString(R.string.city_item_country), currentModel.getCountry()));
        holder.textProvince.setText(String.format(context.getResources().getString(R.string.city_item_province), currentModel.getProvince()));
        holder.textPopulation.setText(String.format(context.getResources().getString(R.string.city_item_population), currentModel.getPopulation()));
        holder.textIso2.setText(String.format(context.getResources().getString(R.string.city_item_iso2), currentModel.getIso2()));
        holder.textIso3.setText(String.format(context.getResources().getString(R.string.city_item_iso3), currentModel.getIso3()));

        holder.rootCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, CityDetailsActivity.class);
                intent.putExtra("clickedCity", (Parcelable) currentModel);
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return cityList.size();
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public int getItemViewType(int position) {
        return position;
    }

    @Override
    public Filter getFilter() {
        if (mFilter == null) {
            mFilter = new CustomFilter();
        }

        return mFilter;
    }

    class CustomFilter extends Filter {

        @Override
        protected FilterResults performFiltering(CharSequence constraint) {

            FilterResults results = new FilterResults();

            if (constraint != null && constraint.length() > 0) {
                constraint = constraint.toString().toUpperCase();

                ArrayList<CityModel> filters = new ArrayList<CityModel>();

                for (int i = 0; i < mFilterList.size(); i++) {
                    if (mFilterList.get(i).getCity().toUpperCase().startsWith(constraint.toString())) {
                        CityModel city = new CityModel();
                        city = mFilterList.get(i);
                        filters.add(city);
                    }
                }

                results.count = filters.size();
                results.values = filters;

            }
            else {

                results.count = unFilteredList.size();
                results.values = unFilteredList;

            }
            return results;
        }

        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {

            cityList = (ArrayList<CityModel>) results.values;

            notifyDataSetChanged();

        }
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        private TextView textCity;
        private TextView textCountry;
        private TextView textProvince;
        private TextView textPopulation;
        private TextView textIso2;
        private TextView textIso3;

        private CardView rootCard;

        public ViewHolder(View itemView) {
            super(itemView);

            textCity = itemView.findViewById(R.id.text_city);
            textCountry = itemView.findViewById(R.id.text_country);
            textProvince = itemView.findViewById(R.id.text_province);
            textIso2 = itemView.findViewById(R.id.text_iso2);
            textIso3 = itemView.findViewById(R.id.text_iso3);
            textPopulation = itemView.findViewById(R.id.text_population);

            rootCard = itemView.findViewById(R.id.card_container);
        }
    }
}
