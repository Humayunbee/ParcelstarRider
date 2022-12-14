package com.parcelstar.rider.Activity.PickupParcel;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

//import com.easyflight.rider.Adaptar.PickupParcelAdaptar.PickUpParcelAdaptar;
import com.parcelstar.rider.Adaptar.PickupParcelAdaptar.RequestParcelAdaptar;
import com.parcelstar.rider.Model.PickUpParcel.PickupParcel;
import com.parcelstar.rider.Network.Api;
import com.parcelstar.rider.Network.RetrofitClient;
import com.parcelstar.rider.R;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
/**
 * Created by Md. Humayun Farid (Android Developer)
 * Create on 01/08/2022
 * Email: Humayunfarid1997@gmail.com
 * Website: www.humayunfarid.com
 * *** Happy Coding ***
 */
public class PickupParcelActivity extends AppCompatActivity {

    Api api;
    ProgressDialog progressDialog;
    RecyclerView recyclerView;
    ConstraintLayout nodatafound;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pickup_parcel);

        recyclerView = findViewById(R.id.rv_pickup_list);
        progressDialog = new ProgressDialog(PickupParcelActivity.this);
        progressDialog.setMessage("Please Wait......");
        progressDialog.setCancelable(false);
        TextView toolbar = findViewById(R.id.tv_toolbar_title);
        toolbar.setText("Pickup Parcel List");
        ImageView toolbarBack =  findViewById(R.id.tv_back);
        nodatafound=findViewById(R.id.nodatafound);
        toolbarBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });


//        Toast.makeText(this, "restart", Toast.LENGTH_SHORT).show();
       datainitialize();
    }
    @Override
    public void onResume(){
        super.onResume();
       datainitialize();

    }



    void datainitialize(){
         //Toast.makeText(PickupParcelActivity.this, "tessting", Toast.LENGTH_SHORT).show();


        progressDialog.show();

        api = RetrofitClient.get(getApplicationContext()).create(Api.class);

        api.getparcel().enqueue(new Callback<PickupParcel>() {
            @Override
            public void onResponse(Call<PickupParcel> call, Response<PickupParcel> response) {
                progressDialog.dismiss();
                if (response.isSuccessful() && response.body() != null) {
                    if(response.body().getParcels().size()>0) {

                        // Toast.makeText(PickupParcelActivity.this, "tessting", Toast.LENGTH_SHORT).show();
                        recyclerView.setHasFixedSize(true);
                        recyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext(),
                                LinearLayoutManager.VERTICAL, false));
                        RequestParcelAdaptar adaptar = new RequestParcelAdaptar(response.body().getParcels(),
                                getApplicationContext(),PickupParcelActivity.this, new RequestParcelAdaptar.OnItemClickListener() {
                            @Override
                            public void onItemClick(int position) {
                                datainitialize();
                                // Toast.makeText(PickupParcelActivity.this, "tessting", Toast.LENGTH_SHORT).show();
                            }
                        });
                        recyclerView.setAdapter(adaptar);
                        recyclerView.setVisibility(View.VISIBLE);
                        nodatafound.setVisibility(View.INVISIBLE);

                        Log.e("ddd", String.valueOf(response.body().getParcels().size()));
                    }else{
                        recyclerView.setVisibility(View.INVISIBLE);
                        nodatafound.setVisibility(View.VISIBLE);;
                    }
                }
                else{
                    recyclerView.setVisibility(View.INVISIBLE);
                    nodatafound.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void onFailure(Call<PickupParcel> call, Throwable t) {
                progressDialog.dismiss();
                nodatafound.setVisibility(View.VISIBLE);
            }
        });

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }
}