package retrofit;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.trello.rxlifecycle2.components.RxActivity;

import java.util.List;
import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import retrofit.Utils.LogInterceptor;
import retrofit.Utils.MyObserver;
import retrofit.bean.Bean;
import retrofit.bean.Demo;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MainActivity extends RxActivity implements View.OnClickListener {
    private static final String TAG = "MainActivity";
    private TextView tv_retrofit;
    private MyObserver<Demo> myObserver;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_retrofit);
        findViewById(R.id.btn_0).setOnClickListener(this);
        findViewById(R.id.btn_1).setOnClickListener(this);
        findViewById(R.id.btn_2).setOnClickListener(this);
        tv_retrofit = findViewById(R.id.tv_retrofit);
    }
    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btn_0:
                getRetrofit();
                break;
            case R.id.btn_1:
                getData();
                break;
            case R.id.btn_2:
                getDatas();
                break;
        }
    }
    private void getRetrofit() {
        OkHttpClient client = new OkHttpClient().newBuilder()
                .readTimeout(Constans.DEFAULT_TIME, TimeUnit.SECONDS)//Set the read timeout
                .connectTimeout(Constans.DEFAULT_TIME, TimeUnit.SECONDS)//Set request timeout
                .writeTimeout(Constans.DEFAULT_TIME,TimeUnit.SECONDS)//Set write timeout
                .addInterceptor(new LogInterceptor())//Add print blocker
                .retryOnConnectionFailure(true)//There was an error in the settings and reconnection
                .build();

        Retrofit retrofit = new Retrofit.Builder()
                .client(client)
                .baseUrl(Constans.BaseUrl)
                //Add GSON parsing: return data to GSON type
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        ApiUrl api = retrofit.create(ApiUrl.class);
        Call<Bean> demo = api.getRetrofit();
        demo.enqueue(new Callback<Bean>() {
            @Override
            public void onResponse(Call<Bean> call, Response<Bean> response) {
                Log.e(TAG, "Request success information: "+response.body().toString());
                tv_retrofit.setText(response.body().toString());
            }

            @Override
            public void onFailure(Call<Bean> call, Throwable t) {
                Log.e(TAG, "Request failed message " +t.getMessage());
                tv_retrofit.setText(t.getMessage());
            }
        });

    }

    private void getDatas() {
        RequestUtils.getDemoList(this, new MyObserver<List<Demo>>(this) {
            @Override
            public void onSuccess(List<Demo> result) {
                for (Demo demo:result){
                    Log.e(TAG, "onSuccess: "+demo.toString() );
                }
                tv_retrofit.setText(result.toString());
            }
            @Override
            public void onFailure(Throwable e, String errorMsg) {
                tv_retrofit.setText(errorMsg);
            }
        });
    }

    private void getData() {
        myObserver = new MyObserver<Demo>(this) {
            @Override
            public void onSuccess(Demo result) {
                tv_retrofit.setText(result.toString());
            }
            @Override
            public void onFailure(Throwable e, String errorMsg) {
                tv_retrofit.setText(errorMsg);
            }
        };
        RequestUtils.getDemo(this,myObserver);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        myObserver.cancleRequest();
    }
}
