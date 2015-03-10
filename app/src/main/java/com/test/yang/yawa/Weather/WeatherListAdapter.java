package com.test.yang.yawa.Weather;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.android.volley.toolbox.NetworkImageView;
import com.test.yang.yawa.R;
import com.test.yang.yawa.util.RequestHelper;
import com.test.yang.yawa.util.RequestQueueSingleton;

import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;

public class WeatherListAdapter extends BaseAdapter {
    private Context context;
    private LayoutInflater layoutInflater;
    private List<WeatherInfo> weatherInfoList;

    public WeatherListAdapter(Context context, List<WeatherInfo> weatherInfoList) {
        this.context = context;
        this.weatherInfoList = weatherInfoList;
        layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {
        return weatherInfoList.size();
    }

    @Override
    public Object getItem(int position) {
        return weatherInfoList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    public void clear() {
        weatherInfoList.clear();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        WeatherViewHolder viewHolder;
        if (convertView == null) {
            convertView = layoutInflater.inflate(R.layout.weather_list_row, parent, false);
            viewHolder = new WeatherViewHolder(convertView);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (WeatherViewHolder) convertView.getTag();
        }

        WeatherInfo weatherInfo = weatherInfoList.get(position);

        viewHolder.iconImageView.setImageUrl(String.format(RequestHelper.getIconUrl(), weatherInfo.getIcon()), RequestQueueSingleton.getInstance(context).getImageLoader());
        viewHolder.dateTextView.setText(weatherInfo.getDate());
        viewHolder.descriptionTextView.setText(weatherInfo.getDescription());
        viewHolder.tempTextView.setText(weatherInfo.getMaxTemp() + " / " + weatherInfo.getMinTemp());

        return convertView;
    }

    static class WeatherViewHolder {
        @InjectView(R.id.icon_image)
        NetworkImageView iconImageView;
        @InjectView(R.id.date_text)
        TextView dateTextView;
        @InjectView(R.id.description_text)
        TextView descriptionTextView;
        @InjectView(R.id.temp_text)
        TextView tempTextView;

        public WeatherViewHolder(View view) {
            ButterKnife.inject(this, view);
        }
    }

}
