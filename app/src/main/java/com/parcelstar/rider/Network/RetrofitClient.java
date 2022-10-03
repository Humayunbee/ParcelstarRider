package com.parcelstar.rider.Network;

import android.content.Context;

import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
/**
 * Created by Md. Humayun Farid (Android Developer)
 * Create on 01/08/2022
 * Email: Humayunfarid1997@gmail.com
 * Website: www.humayunfarid.com
 * *** Happy Coding ***
 */
public class RetrofitClient  {
    private static Retrofit retrofit = null;
    private static Retrofit retrofitWithDifBase = null;

    private static String BaseUrl="https://parcelstar.com.bd/";
    private static Retrofit retrofitWithNoIntercepter = null;

    private RetrofitClient() {
    }

    public static  Retrofit get(Context context) {

        if (retrofit == null) {
            OkHttpClient.Builder httpClient = new OkHttpClient.Builder().addInterceptor(new QueryParameterInterceptor(context));

            OkHttpClient client = httpClient.build();

            retrofit = new Retrofit.Builder()
                    .baseUrl(BaseUrl)
                    .addConverterFactory(GsonConverterFactory.create())
                    .client(client)
                    .build();
        }
        return retrofit;
    }

    public static Retrofit noInterceptor() {
        if (retrofitWithNoIntercepter == null) {
            retrofitWithNoIntercepter = new Retrofit.Builder()
                    .baseUrl(BaseUrl)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
        }
        return retrofitWithNoIntercepter;
    }
    public static Retrofit difBaseUrle() {
        if (retrofitWithDifBase == null) {
            retrofitWithDifBase = new Retrofit.Builder()
                    .baseUrl("https://parcelstar.com.bd/")
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
        }
        return retrofitWithDifBase;
    }
}
