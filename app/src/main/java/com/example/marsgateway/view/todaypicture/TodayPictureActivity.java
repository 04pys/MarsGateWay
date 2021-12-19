package com.example.marsgateway.view.todaypicture;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.app.Activity;
import android.graphics.Paint;
import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.webkit.WebView;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.marsgateway.R;
import com.example.marsgateway.databinding.ActivityMainBinding;
import com.example.marsgateway.databinding.ActivityTodayPictureBinding;
import com.example.marsgateway.util.SecretKeyClass;
import com.example.marsgateway.data.api.NasaService;
import com.example.marsgateway.model.PictureData;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class TodayPictureActivity extends AppCompatActivity {

    PictureData pictureData;

    private static final String CHECK_USER_DATE = "FIRST_MEET";
    private static final String TAG = "MainActivity";
    private static final String CHECK_EVNET_POPUP = "CHECK_EVNET_POPUP";
    private static final String CHECK_EVNET_POPUP_NO = "CHECK_EVNET_POPUP_NO";
    private static final String CHECK_EVNET_POPUP_YES = "CHECK_EVNET_POPUP_YES";
    private String event_popup_result;
    private ActivityTodayPictureBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_today_picture);

        // setup instance
        binding = ActivityTodayPictureBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        binding.explanationTv.setMovementMethod(new ScrollingMovementMethod());

        binding.pictureImg.setVisibility(View.INVISIBLE);
        binding.pictureVideo.setVisibility(View.INVISIBLE);

        setSupportActionBar(binding.toolbar);
        binding.toolbar.setTitle("Today's Astronomical picture");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_baseline_arrow_back_24);

        binding.quitBtn.setPaintFlags(binding.quitBtn.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://api.nasa.gov/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        retrofit.create(NasaService.class);
        NasaService service = retrofit.create(NasaService.class);

        String api_key = new SecretKeyClass().api_key;
        Call<PictureData> call = service.getPicture(api_key);
        call.enqueue(new Callback<PictureData>() {
            @Override
            public void onResponse(Call<PictureData> call, Response<PictureData> response) {
                if (response.isSuccessful()) {
                    PictureData pictureData = response.body();
                    TodayPictureActivity.this.pictureData = pictureData;
                    if (pictureData.media_type.equals("video")) {
                        Log.d(TAG, "onResponse: success");
                        binding.titleTv.setText(pictureData.title);
                        binding.explanationTv.setText(pictureData.explanation);
                        Activity activity = TodayPictureActivity.this;
                        binding.pictureVideo.loadUrl(pictureData.url);
                        binding.pictureVideo.setVisibility(View.VISIBLE);


                    } else {
                        binding.titleTv.setText(pictureData.title);
                        binding.explanationTv.setText(pictureData.explanation);
                        Activity activity = TodayPictureActivity.this;
                        if (activity.isFinishing())
                            return;
                        Glide.with(activity)
                                .load(pictureData.url)
                                .into(binding.pictureImg);
                        binding.pictureImg.setVisibility(View.VISIBLE);
                        Log.d(TAG, "media_type: " + pictureData.media_type);
                    }
                } else {
                    Log.e("error", String.valueOf(response.code()));
                }

            }

            @Override
            public void onFailure(Call<PictureData> call, Throwable t) {
                t.printStackTrace();
            }
        });

        binding.quitBtn.setOnClickListener(view -> {
            getApplication().getSharedPreferences("event_popup", MODE_PRIVATE).edit().putString(CHECK_EVNET_POPUP, CHECK_EVNET_POPUP_YES).apply();
            finish();
        });

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home: { //toolbar의 back키 눌렀을 때 동작
                finish();
                return true;
            }
        }
        return super.onOptionsItemSelected(item);
    }
    // EVENT POP

    // start NewHttpGetRequest.class
    // end NewHttpGetRequest.class

}