package com.example.weather;

import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.bottomsheet.BottomSheetDialog;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.concurrent.ExecutionException;

public class MainActivity extends AppCompatActivity {

    String city = "Bishkek";

    String key = "e01d5bfc830fdca8fd827f3cfdcd5317";

    public class DownloadJSON extends AsyncTask<String, Void, String> {
        @Override
        protected String doInBackground(String... strings) {

            String result = "";

            URL url;

            HttpURLConnection httpURLConnection;

            InputStream inputStream;

            InputStreamReader inputStreamReader;

            try {

                url = new URL(strings[0]);

                httpURLConnection = (HttpURLConnection) url.openConnection();

                inputStream = httpURLConnection.getInputStream();

                inputStreamReader = new InputStreamReader(inputStream);

                int data = inputStreamReader.read();

                while(data != -1) {

                    result += (char) data;

                    data = inputStreamReader.read();

                }

            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }

            return result;
        }
    }

    public class DownloadImage extends AsyncTask<String, Void, Bitmap> {

        @Override
        protected Bitmap doInBackground(String... strings) {
            Bitmap bitmap = null;

            URL url;

            HttpURLConnection httpURLConnection;

            InputStream inputStream;

            try {
                Log.i("LINK",strings[0]);
                url = new URL(strings[0]);

                httpURLConnection = (HttpURLConnection) url.openConnection();

                inputStream = httpURLConnection.getInputStream();

                bitmap = BitmapFactory.decodeStream(inputStream);


            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }

            return bitmap;
        }
    }

    TextView txtDate, txtCity, txtWeather, txtTemp, txtMax, txtMin, txtHumidity, txtPressure, txtWind, txtSunset, txtSunrise;
    ImageView imageIcon, picture;
    EditText etCity;
    Button slideUp;
    BottomSheetDialog dialog;

    public void loading(View view) {

        city = etCity.getText().toString();

        String url = "https://api.openweathermap.org/data/2.5/weather?q=" + city + "&units=metric&appid=" + key;

        DownloadJSON downloadJSON = new DownloadJSON();

        try {
            String result = downloadJSON.execute(url).get();

            JSONObject jsonObject = new JSONObject(result);

            String temp = jsonObject.getJSONObject("main").getString("temp") + "°";

            double temperature = jsonObject.getJSONObject("main").getDouble("temp");

            String humidity = jsonObject.getJSONObject("main").getString("humidity") + "%";

            String tempMin = jsonObject.getJSONObject("main").getString("temp_min") + "°";

            String tempMax = jsonObject.getJSONObject("main").getString("temp_max") + "°";

            String pressure = jsonObject.getJSONObject("main").getString("pressure");

            String windSpeed = jsonObject.getJSONObject("wind").getString("speed") + "km/h";

            String weather = jsonObject.getJSONArray("weather").getJSONObject(0).getString("main");

            Long sunrise = jsonObject.getJSONObject("sys").getLong("sunrise");

            Long sunset = jsonObject.getJSONObject("sys").getLong("sunset");

            Long time = jsonObject.getLong("dt");

            String sTime = new SimpleDateFormat("dd-M-yyyy hh:mm:ss", Locale.ENGLISH)
                    .format(new Date(time * 1000));

            String sunriseTime = new SimpleDateFormat("hh:mm", Locale.ENGLISH)
                    .format(new Date(sunrise * 1000)) + " AM";

            String sunsetTime = new SimpleDateFormat("hh:mm", Locale.ENGLISH)
                    .format(new Date(sunset * 1000)) + " PM";

            txtTemp.setText(temp);
            txtHumidity.setText(humidity);
            txtMin.setText(tempMin);
            txtMax.setText(tempMax);
            txtPressure.setText(pressure);
            txtWind.setText(windSpeed);
            txtWeather.setText(weather);
            txtDate.setText(sTime);
            txtCity.setText(city);
            txtSunrise.setText(sunriseTime);
            txtSunset.setText(sunsetTime);

            Calendar calendar = Calendar.getInstance();
            int currentTime = calendar.get(Calendar.HOUR_OF_DAY);

            if(currentTime < 20 && currentTime > 5){
            }
            else {
                picture.setImageResource(R.drawable.night);
            }

            if(temperature < 10){
                picture.setImageResource(R.drawable.windy);
            }
            else {
                picture.setImageResource(R.drawable.sunny);
            }

//            String nameIcon = "10d";
//
//            nameIcon = jsonObject.getJSONArray("weather").getJSONObject(0).getString("icon");
//
//            DownloadImage downloadImage = new DownloadImage();
//
//            String urlIcon = "https://openweathermap.org/img/wn/"+ nameIcon +"@2x.png";
//
//            Bitmap bitmap = downloadImage.execute(urlIcon).get();
//
//            imageIcon.setImageBitmap(bitmap);

        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        txtDate = findViewById(R.id.date);
        txtCity = findViewById(R.id.city);
        txtWeather = findViewById(R.id.weather);
        txtTemp = findViewById(R.id.temp);
        txtMax = findViewById(R.id.max);
        txtMin = findViewById(R.id.min);
        txtHumidity = findViewById(R.id.humidity);
        txtPressure = findViewById(R.id.pressure);
        txtWind = findViewById(R.id.wind);
        txtSunrise = findViewById(R.id.sunrise);
        txtSunset = findViewById(R.id.sunset);
        imageIcon = findViewById(R.id.imgWeather);
        slideUp = findViewById(R.id.slideUp);
        etCity = findViewById(R.id.et_city);
        picture = findViewById(R.id.picture);

        slideUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                loading(view);
            }
        });

//        dialog = new BottomSheetDialog(this);
//        createDialog();
//
//        slideUp.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                dialog.show();
//            }
//        });
//
//        dialog.getWindow().clearFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);

    }

//    private void createDialog() {
//        View view = getLayoutInflater().inflate(R.layout.bottom_sheet, null, false);
//
//        Button btnFindCity = findViewById(R.id.findCity);
//        EditText etCity = findViewById(R.id.et_city);
//
//        btnFindCity.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                dialog.dismiss();
//                loading(view, etCity);
//            }
//        });
//
//        dialog.setContentView(view);
//    }
}