package com.parcelstar.rider.Activity.ReturnParcel;

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

import com.parcelstar.rider.Adaptar.ReturnParcelAdaptar.ReturnParcels;
import com.parcelstar.rider.Model.Return.ReturnListContainer;
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
public class ReturnActivity extends AppCompatActivity {

    Api api;
    ProgressDialog progressDialog;
    RecyclerView recyclerView;
    ConstraintLayout nodatafound;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_return);

        recyclerView = findViewById(R.id.rv_pickup_list);
        nodatafound=findViewById(R.id.nodatafound);
        TextView toolbar = findViewById(R.id.tv_toolbar_title);
        toolbar.setText("Return Parcel List");
        ImageView toolbarBack = findViewById(R.id.tv_back);

        toolbarBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
    }

    @Override
    public void onResume() {
        super.onResume();
        datainitialize();

    }

    private void datainitialize() {
        progressDialog = new ProgressDialog(ReturnActivity.this);
        progressDialog.setMessage("Please Wait......");
        progressDialog.setCancelable(false);

        api = RetrofitClient.get(getApplicationContext()).create(Api.class);
         progressDialog.show();
        api.getReturnparcellist().enqueue(new Callback<ReturnListContainer>() {
            @Override
            public void onResponse(Call<ReturnListContainer> call, Response<ReturnListContainer> response) {
                progressDialog.dismiss();
                if (response.isSuccessful() && response.body() != null) {
                    if(response.body().getParcels().size()>0) {
                        Log.d("tesst", String.valueOf(response.body().getParcels().size()));
                        recyclerView.setHasFixedSize(true);
                        recyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext(),
                                LinearLayoutManager.VERTICAL, false));
                        ReturnParcels adaptar = new ReturnParcels(response.body().getParcels(), getApplicationContext(), ReturnActivity.this, new ReturnParcels.OnItemClickListener() {
                            @Override
                            public void onItemClick(int position) {
                                datainitialize();
                            }
                        });
                        recyclerView.setAdapter(adaptar);
                        recyclerView.setVisibility(View.VISIBLE);
                        nodatafound.setVisibility(View.INVISIBLE);
                        Log.e("ddd", String.valueOf(response.body().getParcels().size()));
                    }
                    else{
                        nodatafound.setVisibility(View.VISIBLE);
                        recyclerView.setVisibility(View.INVISIBLE);
                    }
                }
                else{
                    nodatafound.setVisibility(View.VISIBLE);
                    recyclerView.setVisibility(View.INVISIBLE);
                }
            }

            @Override
            public void onFailure(Call<ReturnListContainer> call, Throwable t) {
                progressDialog.dismiss();
                nodatafound.setVisibility(View.VISIBLE);
            }
        });

    }
}