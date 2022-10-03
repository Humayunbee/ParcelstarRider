package com.parcelstar.rider.Activity.DeliveryParcel;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;


import com.parcelstar.rider.Adaptar.DeliveryParcelListAdaptar.RequestDeliveryAdaptar;
import com.parcelstar.rider.Model.DeliveryParcel.DeliveryContainer;
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
public class DeliveryParcelListActivity extends AppCompatActivity {

    Api api;
    ProgressDialog progressDialog;
    RecyclerView recyclerView;
    ConstraintLayout nodatafound;
    SwipeRefreshLayout swip;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_delivery_parcel_list);

        swip=findViewById(R.id.swip);


        recyclerView = findViewById(R.id.rv_pickup_list);
        progressDialog = new ProgressDialog(DeliveryParcelListActivity.this);
        progressDialog.setMessage("Please Wait......");
        progressDialog.setCancelable(false);
        TextView toolbar = findViewById(R.id.tv_toolbar_title);
        toolbar.setText("Delivery Parcel List");
        ImageView toolbarBack = findViewById(R.id.tv_back);
        nodatafound=findViewById(R.id.nodatafound);
        toolbarBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
       // datainitialize();
    }

    @Override
    public void onResume() {
        super.onResume();
        datainitialize();

    }

    void datainitialize() {
        //Toast.makeText(PickupParcelActivity.this, "tessting", Toast.LENGTH_SHORT).show();



        api = RetrofitClient.get(getApplicationContext()).create(Api.class);
         progressDialog.show();
        api.getDeliveryparcel().enqueue(new Callback<DeliveryContainer>() {
            @Override
            public void onResponse(Call<DeliveryContainer> call, Response<DeliveryContainer> response) {
                progressDialog.dismiss();
                if (response.isSuccessful() && response.body() != null) {
                if(response.body().getParcels().size()>0){
                    recyclerView.setHasFixedSize(true);
                    recyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext(),
                            LinearLayoutManager.VERTICAL, false));
                    RequestDeliveryAdaptar adaptar = new RequestDeliveryAdaptar(response.body().getParcels(), getApplicationContext(), DeliveryParcelListActivity.this, new RequestDeliveryAdaptar.OnItemClickListener() {
                        @Override
                        public void onItemClick(int position) {
                            datainitialize();
                        }
                    });
                    recyclerView.setAdapter(adaptar);
                    Log.e("hhh", String.valueOf(response.body().getParcels().size()));
                    progressDialog.dismiss();
                }
                else{
                    recyclerView.setVisibility(View.INVISIBLE);
                    nodatafound.setVisibility(View.VISIBLE);
                }

                }else{
                    recyclerView.setVisibility(View.INVISIBLE);
                    nodatafound.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void onFailure(Call<DeliveryContainer> call, Throwable t) {
                Log.e("hhh", "failur");
                progressDialog.dismiss();
                nodatafound.setVisibility(View.VISIBLE);
            }
        });

    }
}