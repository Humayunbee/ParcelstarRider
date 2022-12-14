/*
package com.easyflight.rider.Adaptar.listener;

import android.app.ProgressDialog;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.widget.PopupMenu;


import com.easyflight.rider.Model.PickUpParcel.Parcel;
import com.easyflight.rider.Model.PickupRequest.PickupRequestAccept;
import com.easyflight.rider.Network.Api;
import com.easyflight.rider.Network.RetrofitClient;
import com.easyflight.rider.R;
import com.easyflight.rider.Util.MyAlertDialog;

import java.io.IOException;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class PercelMenuClickListener implements View.OnClickListener {

    PickUpParcelAdaptar percelListAdapter;
    PickUpParcelAdaptar.OnItemClickListener listener;
    PickUpParcelAdaptar.Viewholders viewHolders;
    Parcel percel;
    ProgressDialog progressDialog;
    Api api;


    public PercelMenuClickListener(PickUpParcelAdaptar percelListAdapter,
                                   PickUpParcelAdaptar.Viewholders viewHolders,
                                   Parcel percel, PickUpParcelAdaptar.OnItemClickListener listner) {
        this.percelListAdapter = percelListAdapter;
        this.viewHolders = viewHolders;
        this.percel = percel;
        this.listener = listner;

        api = RetrofitClient.get(viewHolders.itemView.getContext()).create(Api.class);
        progressDialog = new ProgressDialog(viewHolders.itemView.getContext());
        progressDialog.setMessage("Please Wait......");
        progressDialog.setCancelable(false);
    }




    @Override
    public void onClick(View view) {
        //Log.e("ffff", percel.getParcelStatus());
        PopupMenu popupMenu = new PopupMenu(viewHolders.itemView.getContext(), viewHolders.getoption());
        popupMenu.getMenuInflater().inflate(R.menu.parcel_pickup_menu, popupMenu.getMenu());

        popupMenu.getMenu().findItem(R.id.m_request_complete).setVisible(true);
        if (percel.getParcelStatus().equals(new String("Pickup Run Create"))) {
            popupMenu.getMenu().findItem(R.id.m_request_accept).setVisible(true);
           // Log.e("request", "dkdkdkd");
        } else {
            popupMenu.getMenu().findItem(R.id.m_request_accept).setVisible(false);
          //  Log.e("request", "hghhhg");
        }
        if (percel.getParcelStatus().equals(new String("Pickup Run Accept Rider"))) {
            popupMenu.getMenu().findItem(R.id.m_request_reject).setVisible(false);
           // Log.e("request", "dkdkdkd");
        } else {
            popupMenu.getMenu().findItem(R.id.m_request_reject).setVisible(true);
            //Log.e("request", "hghhhg");
        }
        if (percel.getParcelStatus().equals(new String("Pickup Run Accept Rider"))) {
            popupMenu.getMenu().findItem(R.id.m_request_complete).setVisible(true);
        } else {
            popupMenu.getMenu().findItem(R.id.m_request_complete).setVisible(false);
           // Log.e("request", "hghhhg");
        }
        if (percel.getParcelStatus().equals(new String("Pickup Run Accept Rider"))) {
            popupMenu.getMenu().findItem(R.id.m_request_reschedule).setVisible(true);
        } else {
            popupMenu.getMenu().findItem(R.id.m_request_complete).setVisible(false);
           // Log.e("request", "hghhhg");
        }
        if (percel.getParcelStatus().equals(new String("Pickup Run Create"))){
            popupMenu.getMenu().findItem(R.id.m_request_complete).setVisible(false);
        }else {
            popupMenu.getMenu().findItem(R.id.m_request_complete).setVisible(true);
            //Log.e("request", "hghhhg");
        }



        popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.m_request_accept:
                        progressDialog.show();
                        api.getrequestacc(String.valueOf(percel.getParcelId())).enqueue(new Callback<PickupRequestAccept>() {
                            @Override
                            public void onResponse(Call<PickupRequestAccept> call, Response<PickupRequestAccept> response) {
                                if (response.isSuccessful() && response.body() != null) {
                                    progressDialog.dismiss();
                                    Toast.makeText(viewHolders.itemView.getContext(), "Request Accept", Toast.LENGTH_SHORT).show();
                                    listener.onItemClick(6);
                                } else {
                                    progressDialog.dismiss();
                                    // Toast.makeText(viewHolders.itemView.getContext(), "Request Accept", Toast.LENGTH_SHORT).show();
                                    try {
                                        Log.e("accp", String.valueOf(response.errorBody().string()));
                                    } catch (IOException e) {
                                        e.printStackTrace();
                                    }
                                }
                            }

                            @Override
                            public void onFailure(Call<PickupRequestAccept> call, Throwable t) {

                            }
                        });

                       // listener.onItemClick(5);
                        return true;
                    case R.id.m_request_reject:
                        progressDialog.show();
                        api.getrequestreject(String.valueOf(percel.getParcelId())).enqueue(new Callback<PickupRequestAccept>() {
                            @Override
                            public void onResponse(Call<PickupRequestAccept> call, Response<PickupRequestAccept> response) {
                                if (response.isSuccessful() && response.body() != null) {
                                    progressDialog.dismiss();
                                    Toast.makeText(viewHolders.itemView.getContext(), "Request Reject", Toast.LENGTH_SHORT).show();
                                    listener.onItemClick(6);
                                } else {
                                    progressDialog.dismiss();
                                    // Toast.makeText(viewHolders.itemView.getContext(), "Request reject", Toast.LENGTH_SHORT).show();
                                    try {
                                        Log.e("acc", String.valueOf(response.errorBody().string()));
                                    } catch (IOException e) {
                                        e.printStackTrace();
                                    }
                                }
                            }

                            @Override
                            public void onFailure(Call<PickupRequestAccept> call, Throwable t) {

                            }
                        });

                        //Toast.makeText(viewHolders.itemView.getContext(), "Hold Parcel", Toast.LENGTH_SHORT).show();
                       // listener.onItemClick(6);
                        return true;
                    case R.id.m_request_complete:

                        MyAlertDialog myAlertDialog = new MyAlertDialog(viewHolders.itemView.getContext());
                        myAlertDialog.showConfirmDialog("Complete Request", "Confirm", "");
                        myAlertDialog.button.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                myAlertDialog.cancel();
                                progressDialog.show();
                                api.getpickcomplete(String.valueOf(percel.getParcelId())).enqueue(new Callback<PickupRequestAccept>() {
                                    @Override
                                    public void onResponse(Call<PickupRequestAccept> call, Response<PickupRequestAccept> response) {
                                        if (response.isSuccessful() && response.body() != null) {
                                            progressDialog.dismiss();
                                            listener.onItemClick(6);
                                            Toast.makeText(viewHolders.itemView.getContext(), "Complete Request", Toast.LENGTH_SHORT).show();
                                        } else {
                                            progressDialog.dismiss();
                                        }

                                    }

                                    @Override
                                    public void onFailure(Call<PickupRequestAccept> call, Throwable t) {

                                    }
                                });
                            }
                        });
                        myAlertDialog.show();

                        return true;

                    case R.id.m_request_reschedule:

                        MyAlertDialog myAlertDialogone = new MyAlertDialog(viewHolders.itemView.getContext());
                        myAlertDialogone.showConfirmDialog("Complete Request", "Confirm", "");
                        myAlertDialogone.button.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                myAlertDialogone.cancel();
                                progressDialog.show();
                                String note=" ";
                                note=myAlertDialogone.editText.getText().toString();
                                api.getreschedule(String.valueOf(percel.getParcelId()),note).enqueue(new Callback<PickupRequestAccept>() {
                                    @Override
                                    public void onResponse(Call<PickupRequestAccept> call, Response<PickupRequestAccept> response) {
                                        if (response.isSuccessful() && response.body() != null) {
                                           // Log.e("res","dsdfdgdfdsfgdf");
                                            progressDialog.dismiss();
                                            listener.onItemClick(6);
                                            Toast.makeText(viewHolders.itemView.getContext(), "Complete Rescheduel", Toast.LENGTH_SHORT).show();
                                        } else {
                                            progressDialog.dismiss();
                                        }
                                    }

                                    @Override
                                    public void onFailure(Call<PickupRequestAccept> call, Throwable t) {

                                    }
                                });

                            }
                        });

                        myAlertDialogone.show();


                     // Toast.makeText(viewHolders.itemView.getContext(), "rescheule done", Toast.LENGTH_SHORT).show();
                        return true;
                    default:
                        return false;
                }

            }
        });
        popupMenu.show();
    }
}
*/
