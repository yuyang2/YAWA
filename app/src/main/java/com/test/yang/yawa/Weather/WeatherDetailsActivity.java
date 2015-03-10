package com.test.yang.yawa.Weather;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

import com.android.volley.toolbox.NetworkImageView;
import com.test.yang.yawa.MainActivity;
import com.test.yang.yawa.R;
import com.test.yang.yawa.util.RequestHelper;
import com.test.yang.yawa.util.RequestQueueSingleton;

import butterknife.ButterKnife;
import butterknife.InjectView;

public class WeatherDetailsActivity extends Activity {
    @InjectView(R.id.details_city_text)
    TextView cityTextView;
    @InjectView(R.id.details_date_text)
    TextView dateTextView;
    @InjectView(R.id.details_description_text)
    TextView descriptionTextView;
    @InjectView(R.id.details_max_temp)
    TextView maxTempTextView;
    @InjectView(R.id.details_min_temp)
    TextView minTempTextView;
    @InjectView(R.id.details_icon_image)
    NetworkImageView iconImageView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_weather_details);
        ButterKnife.inject(this);

        final Intent intent = getIntent();
        cityTextView.setText(intent.getStringExtra(MainActivity.EXTRA_CITY));
        dateTextView.setText(intent.getStringExtra(MainActivity.EXTRA_DATE));
        descriptionTextView.setText(intent.getStringExtra(MainActivity.EXTRA_DESCRIPTION));
        maxTempTextView.setText(String.format(getString(R.string.max_temp_text), intent.getStringExtra(MainActivity.EXTRA_MAX_TEMP)));
        minTempTextView.setText(String.format(getString(R.string.min_temp_text), intent.getStringExtra(MainActivity.EXTRA_MIN_TEMP)));
        iconImageView.setImageUrl(String.format(RequestHelper.getIconUrl(), intent.getStringExtra(MainActivity.EXTRA_ICON)), RequestQueueSingleton.getInstance(this).getImageLoader());
    }

}
