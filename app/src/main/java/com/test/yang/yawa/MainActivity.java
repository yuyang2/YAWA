package com.test.yang.yawa;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.test.yang.yawa.Weather.WeatherDetailsActivity;
import com.test.yang.yawa.Weather.WeatherInfo;
import com.test.yang.yawa.Weather.WeatherListAdapter;
import com.test.yang.yawa.util.RequestHelper;
import com.test.yang.yawa.util.RequestQueueSingleton;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;

public class MainActivity extends Activity {
    @InjectView(R.id.city_edit_text)
    TextView cityTextView;
    @InjectView(R.id.weather_list)
    ListView weatherListView;

    private ProgressDialog progressDialog;
    private static final String TAG = "MainActivity";
    private static final String JSON_LIST_KEY = "list";
    private static final String JSON_TEMP_KEY = "temp";
    private static final String JSON_MIN_KEY = "min";
    private static final String JSON_MAX_KEY = "max";
    private static final String JSON_WEATHER_KEY = "weather";
    private static final String JSON_DESCRIPTION_KEY = "description";
    private static final String JSON_ICON_KEY = "icon";
    private static final String JSON_CITY_KEY = "city";
    private static final String JSON_NAME_KEY = "name";
    private List<WeatherInfo> weatherInfoList = new ArrayList<>();
    private WeatherListAdapter weatherListAdapter;

    public static final String EXTRA_CITY = "EXTRA_CITY";
    public static final String EXTRA_DATE = "EXTRA_DATE";
    public static final String EXTRA_DESCRIPTION = "EXTRA_DESCRIPTION";
    public static final String EXTRA_MIN_TEMP = "EXTRA_MIN_TEMP";
    public static final String EXTRA_MAX_TEMP = "EXTRA_MAX_TEMP";
    public static final String EXTRA_ICON = "EXTRA_ICON";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.inject(this);

        weatherListAdapter = new WeatherListAdapter(this, weatherInfoList);
        weatherListView.setAdapter(weatherListAdapter);

        weatherListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                final Intent intent = new Intent(getApplicationContext(), WeatherDetailsActivity.class);
                final WeatherInfo weatherInfo = (WeatherInfo) parent.getItemAtPosition(position);
                intent.putExtra(EXTRA_CITY, weatherInfo.getCity());
                intent.putExtra(EXTRA_DATE, weatherInfo.getDate());
                intent.putExtra(EXTRA_DESCRIPTION, weatherInfo.getDescription());
                intent.putExtra(EXTRA_MIN_TEMP, weatherInfo.getMinTemp());
                intent.putExtra(EXTRA_MAX_TEMP, weatherInfo.getMaxTemp());
                intent.putExtra(EXTRA_ICON, weatherInfo.getIcon());
                startActivity(intent);
            }
        });
    }

    @Override
    protected void onStop() {
        super.onStop();
        RequestQueueSingleton.getInstance(this).cancelPendingRequests(TAG);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        hideProgressDialog(); // avoid leaked window
    }

    @OnClick(R.id.show_weather_btn)
    public void showWeather() {
        if (TextUtils.isEmpty(getCity())) {
            Toast.makeText(getApplicationContext(), getString(R.string.city_empty_text), Toast.LENGTH_SHORT).show();
            return;
        }

        showProgressDialog(getString(R.string.show_weather_progress_text));
        weatherListAdapter.clear();

        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, String.format(RequestHelper.getApiUrl(), getCity()), null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                Log.d(TAG, response.toString());

                JSONArray jsonArray = new JSONArray();
                try {
                    jsonArray = response.getJSONArray(JSON_LIST_KEY);
                } catch (JSONException e) {
                    Log.w(TAG, e.getMessage());
                }

                JSONObject jsonObject;
                JSONObject tempObject;
                JSONArray weatherArray;
                for (int i = 0; i < jsonArray.length(); i++) {
                    try {
                        jsonObject = jsonArray.getJSONObject(i);
                        tempObject = jsonObject.getJSONObject(JSON_TEMP_KEY);
                        weatherArray = jsonObject.getJSONArray(JSON_WEATHER_KEY);
                        weatherInfoList.add(new WeatherInfo(getDate(i + 1), response.getJSONObject(JSON_CITY_KEY).getString(JSON_NAME_KEY), tempObject.getString(JSON_MIN_KEY), tempObject.getString(JSON_MAX_KEY), weatherArray.getJSONObject(0).getString(JSON_DESCRIPTION_KEY), weatherArray.getJSONObject(0).getString(JSON_ICON_KEY)));
                    } catch (JSONException e) {
                        Log.w(TAG, e.getMessage());
                    }
                }

                hideProgressDialog();
                weatherListAdapter.notifyDataSetChanged();
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e(TAG, "Error: " + error.getMessage());
                hideProgressDialog();
                Toast.makeText(getApplicationContext(), getString(R.string.show_weather_error_text), Toast.LENGTH_SHORT).show();
            }
        });

        RequestQueueSingleton.getInstance(this).addToRequestQueue(request, TAG);
    }

    private void showProgressDialog(final String message) {
        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage(message);
        progressDialog.show();
    }

    private void hideProgressDialog() {
        if (progressDialog != null) {
            progressDialog.dismiss();
        }
    }

    private String getDate(int next) {
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.DAY_OF_YEAR, next);
        return new SimpleDateFormat("dd/MM/yyyy").format(calendar.getTime());
    }

    private String getCity() {
        return cityTextView.getText().toString().trim();
    }

}
