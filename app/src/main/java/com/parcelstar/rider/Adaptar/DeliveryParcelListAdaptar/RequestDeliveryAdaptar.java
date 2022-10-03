package com.parcelstar.rider.Adaptar.DeliveryParcelListAdaptar;

import android.Manifest;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

//import com.easyflight.rider.Adaptar.listener.DeliveryRequestMenuClickListner;
import com.parcelstar.rider.Model.DeliveryParcel.DeliveryParcel;
import com.parcelstar.rider.Model.PickupRequest.PickupRequestAccept;
import com.parcelstar.rider.Network.Api;
import com.parcelstar.rider.Network.RetrofitClient;
import com.parcelstar.rider.R;
import com.parcelstar.rider.Util.DeliveryAlertDialog;
import com.google.gson.JsonObject;

import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

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
public class RequestDeliveryAdaptar extends RecyclerView.Adapter<RequestDeliveryAdaptar.Viewholders> {

    List<DeliveryParcel> delivery = new ArrayList<>();
    Context context;
    public OnItemClickListener listener;
    ProgressDialog progressDialog;
    Api api;
    Activity activity;
    int spinnerPosition = 0;
    DeliveryAlertDialog myAlertDialog;
    public interface OnItemClickListener {
        void onItemClick(int position);
    }

    public RequestDeliveryAdaptar(List<DeliveryParcel> delivery, Context context, Activity activity, OnItemClickListener listener
    ) {
        this.delivery = delivery;
        this.context = context;
        this.listener = listener;
        this.activity=activity;
        api = RetrofitClient.get(context).create(Api.class);
    }

    @NonNull
    @NotNull
    @Override
    public Viewholders onCreateViewHolder(@NonNull @NotNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.delivery_model_parcel, parent, false);
        return new RequestDeliveryAdaptar.Viewholders(view);
    }

    @Override
    public void onBindViewHolder(@NonNull @NotNull Viewholders holder, int position) {
        progressDialog = new ProgressDialog(holder.itemView.getContext());
        progressDialog.setMessage("Please Wait......");
        progressDialog.setCancelable(false);
        myAlertDialog = new DeliveryAlertDialog(holder.itemView.getContext());
        DeliveryParcel deliveryParcel=delivery.get(position);
        holder.Invoice.setText(String.valueOf(delivery.get(position).getParcelInvoice()));
        holder.CustomerName.setText(String.valueOf(delivery.get(position).getCustomerName()));
        holder.CustomerPhn.setText(String.valueOf(delivery.get(position).getCustomerContactNumber()));
        holder.CustomerAddress.setText(String.valueOf(delivery.get(position).getCustomerAddress()));
        holder.TotalCollectAmount.setText("Collection Amount : " + String.valueOf(delivery.get(position).getCollectAmount()));
        holder.MerchantName.setText("Merchant Name : " + String.valueOf(delivery.get(position).getMerchantName()));
        holder.MerchantPhn.setText("Merchant Phone : " + String.valueOf(delivery.get(position).getMerchantContactNumber()));
        holder.MerchantAddress.setText("Merchant Address : " + String.valueOf(delivery.get(position).getMerchantAddress()));
        holder.ParcelStatus.setText(String.valueOf(delivery.get(position).getParcelStatus()));


        holder.CustomerPhn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String call = delivery.get(position).getCustomerContactNumber();
                if (isCallPermissionGranted(context)) {
                    Intent callIntent = new Intent(Intent.ACTION_CALL);
                    callIntent.setData(Uri.parse("tel:" + call));
                    callIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    context.startActivity(callIntent);
                }


            }
        });

        if (deliveryParcel.getParcelStatus().equals(new String("Delivery Run Start"))) {
            holder.Accept.setVisibility(View.VISIBLE);
            holder.reschedule.setVisibility(View.GONE);
            holder.Reject.setVisibility(View.VISIBLE);
            holder.complete.setVisibility(View.GONE);
            holder.ParcelStatus.setBackground(ContextCompat.getDrawable(context, R.drawable.status_round_orange));


        } else if(deliveryParcel.getParcelStatus().equals(new String("Delivery Run Rider Accept"))) {
            holder.complete.setVisibility(View.VISIBLE);
            holder.Reject.setVisibility(View.GONE);
            holder.Accept.setVisibility(View.GONE);
            holder.reschedule.setVisibility(View.GONE);
            holder.ParcelStatus.setBackground(ContextCompat.getDrawable(context, R.drawable.status_round_green));


        }

        holder.Accept.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                progressDialog.show();

                api.getdeliveryaccept(String.valueOf(deliveryParcel.getParcelId())).enqueue(new Callback<PickupRequestAccept>() {
                    @Override
                    public void onResponse(Call<PickupRequestAccept> call, Response<PickupRequestAccept> response) {
                        if (response.isSuccessful() && response.body() != null) {
                            progressDialog.dismiss();
                            Toast.makeText(holder.itemView.getContext(), "Request Accept", Toast.LENGTH_SHORT).show();
                            listener.onItemClick(5);
                        } else {
                            progressDialog.dismiss();
                            Log.d("tesst",response.message().toString());
//                                    try {
//                                        Log.e("deli", String.valueOf(response.errorBody().string()));
//                                    } catch (IOException e) {
//                                        e.printStackTrace();
//                                    }
                        }

                    }

                    @Override
                    public void onFailure(Call<PickupRequestAccept> call, Throwable t) {

                    }
                });
            }
        });
        holder.Reject.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                progressDialog.show();
                api.getdeliveryreject(String.valueOf(deliveryParcel.getParcelId())).enqueue(new Callback<PickupRequestAccept>() {
                    @Override
                    public void onResponse(Call<PickupRequestAccept> call, Response<PickupRequestAccept> response) {
                        if (response.isSuccessful() && response.body() != null) {
                            progressDialog.dismiss();
                            Toast.makeText(holder.itemView.getContext(), "Request Reject", Toast.LENGTH_SHORT).show();
                            listener.onItemClick(5);
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
      /*  holder.reschedule.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MyAlertDialog myAlertDialogone = new MyAlertDialog(holder.itemView.getContext());
                myAlertDialogone.showConfirmDialog("Complete Request", "Confirm", "");
                myAlertDialogone.button.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        String note=" ";
                        note=myAlertDialogone.editText.getText().toString();
                        myAlertDialogone.cancel();
                        progressDialog.show();
                        api.getreschedule(String.valueOf(parcel.getParcelId()),note).enqueue(new Callback<PickupRequestAccept>() {
                            @Override
                            public void onResponse(Call<PickupRequestAccept> call, Response<PickupRequestAccept> response) {
                                if (response.isSuccessful() && response.body() != null) {
                                    //  Log.e("res","dsdfdgdfdsfgdf");
                                    progressDialog.dismiss();
                                    listener.onItemClick(6);
                                    Toast.makeText(holder.itemView.getContext(), "Complete Rescheduel", Toast.LENGTH_SHORT).show();
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
            }
        });*/
        holder.complete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                myAlertDialog.showConfirmDialog("Complete Request", "Confirm", "");
                myAlertDialog.button.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        sendDatatoServerBYConfirm(deliveryParcel, spinnerPosition);


                    }
                });


                myAlertDialog.spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                        if (i == 1) {
                            myAlertDialog.LnConfirmAmount.setVisibility(View.VISIBLE);
                            myAlertDialog.LnCode.setVisibility(View.VISIBLE);
                            myAlertDialog.LnDate.setVisibility(View.GONE);
                            myAlertDialog.LnNote.setVisibility(View.GONE);
                            myAlertDialog.ConfirmAmount.setText(String.valueOf(deliveryParcel.getCollectAmount()));
                            myAlertDialog.ConfirmAmount.setEnabled(false);

                            myAlertDialog.SendOtp.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    myAlertDialog.sendOtp(String.valueOf(deliveryParcel.getParcelId().toString()));
                                }
                            });
                            spinnerPosition = 1;
                            //Toast.makeText(viewHolders.itemView.getContext(), "okkkkkkkkk", Toast.LENGTH_SHORT).show();
                        } else if (i == 2) {
                            myAlertDialog.LnConfirmAmount.setVisibility(View.VISIBLE);
                            myAlertDialog.LnCode.setVisibility(View.VISIBLE);
                            myAlertDialog.LnNote.setVisibility(View.VISIBLE);
                            myAlertDialog.LnDate.setVisibility(View.GONE);
                            myAlertDialog.ConfirmAmount.setText("");
                            myAlertDialog.ConfirmAmount.setEnabled(true);
                        } else if (i == 3) {
                            myAlertDialog.LnDate.setVisibility(View.VISIBLE);
                            myAlertDialog.LnNote.setVisibility(View.VISIBLE);
                            myAlertDialog.LnConfirmAmount.setVisibility(View.GONE);
                            myAlertDialog.LnCode.setVisibility(View.GONE);


                        } else if (i == 4) {
                            myAlertDialog.LnNote.setVisibility(View.VISIBLE);
                            myAlertDialog.LnConfirmAmount.setVisibility(View.GONE);
                            myAlertDialog.LnCode.setVisibility(View.GONE);
                            myAlertDialog.LnDate.setVisibility(View.GONE);
                        }

                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> adapterView) {

                    }
                });

                myAlertDialog.show();
            }
        });


       // holder.optionMenu.setOnClickListener(new DeliveryRequestMenuClickListner(this, listener,holder, delivery.get(position),context));
    }

    @Override
    public int getItemCount() {
        return delivery.size();
    }

    public class Viewholders extends RecyclerView.ViewHolder {
        TextView Invoice, CustomerName,MerchantPhn,MerchantAddress, CustomerPhn, CustomerAddress, TotalCollectAmount, MerchantName, ParcelStatus,
                Accept,Reject,reschedule,complete;
        public Viewholders(@NonNull @NotNull View itemView) {
            super(itemView);

            Invoice = itemView.findViewById(R.id.tv_invoice);
            CustomerName = itemView.findViewById(R.id.tv_customer_name);
            CustomerPhn = itemView.findViewById(R.id.tv_phone);
            CustomerAddress = itemView.findViewById(R.id.tv_address);
            TotalCollectAmount = itemView.findViewById(R.id.tv_amount);
            MerchantName = itemView.findViewById(R.id.tv_merchant_name);
            MerchantPhn = itemView.findViewById(R.id.tv_merchant_no);
            ParcelStatus = itemView.findViewById(R.id.tv_status);
            MerchantAddress = itemView.findViewById(R.id.tv_merchant_address);
            Accept=itemView.findViewById(R.id.txt_accept);
            Reject=itemView.findViewById(R.id.txt_reject);
            reschedule=itemView.findViewById(R.id.txt_re);
            complete=itemView.findViewById(R.id.txt_complete);
        }


    }
    public void sendDatatoServerBYConfirm(DeliveryParcel parcel, int position) {
        Log.e("tesst", "inner senddataServer");
        int ppsitions = myAlertDialog.spinner.getSelectedItemPosition();
        if (ppsitions == 0) {
            Toast.makeText(context, "Please Select Delivery Type", Toast.LENGTH_SHORT).show();
        } else if (ppsitions == 1) {

            if (!TextUtils.isEmpty(myAlertDialog.EnterCode.getText().toString())) {
                myAlertDialog.cancel();
                progressDialog.show();
                api.confirmDelivery(
                        String.valueOf(parcel.getParcelId()),
                        "21",
                        String.valueOf(parcel.getCollectAmount()),
                        myAlertDialog.EnterCode.getText().toString(),
                        "",
                        ""

                ).enqueue(new Callback<JsonObject>() {
                    @Override
                    public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                        progressDialog.dismiss();
                        if (response.isSuccessful() && response.body() != null) {
                            Toast.makeText(context, "Successfully submitted", Toast.LENGTH_SHORT).show();
                            listener.onItemClick(5);
                           // Log.e("tesst", response.body().getAsString().toString());
                            myAlertDialog.cancel();
                        } else {

                            try {
                                Log.e("tesst", response.errorBody().string());
                                Toast.makeText(context,"Code or Collection Amount doesn't Match", Toast.LENGTH_LONG).show();
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        }
                    }

                    @Override
                    public void onFailure(Call<JsonObject> call, Throwable t) {
                        progressDialog.dismiss();
                        Toast.makeText(context, "Something wrong", Toast.LENGTH_SHORT).show();
                    }
                });
            } else {
                myAlertDialog.EnterCode.setError("Enter your OTP Code");
                myAlertDialog.EnterCode.requestFocus();
            }

        } else if (ppsitions == 2) {
            if (!TextUtils.isEmpty(myAlertDialog.EnterCode.getText().toString())) {
                if (!TextUtils.isEmpty(myAlertDialog.ConfirmAmount.getText().toString())) {
                    myAlertDialog.cancel();
                    progressDialog.show();
                    api.confirmDelivery(
                            String.valueOf(parcel.getParcelId()),
                            "22",
                            String.valueOf(parcel.getCollectAmount()),
                            myAlertDialog.EnterCode.getText().toString(),
                            "",
                            myAlertDialog.ENote.getText().toString()

                    ).enqueue(new Callback<JsonObject>() {
                        @Override
                        public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                            progressDialog.dismiss();
                            if (response.isSuccessful() && response.body() != null) {
                                Toast.makeText(context, "Successfully submitted", Toast.LENGTH_SHORT).show();
                               // Log.e("tesst", response.body().getAsString().toString());
                                myAlertDialog.cancel();
                                listener.onItemClick(5);
                            } else {
                                try {
                                    Toast.makeText(context, "Parcel Code not Matching", Toast.LENGTH_SHORT).show();
                                    Log.e("tesst", response.errorBody().string());
                                } catch (IOException e) {
                                    e.printStackTrace();
                                }
                            }
                        }

                        @Override
                        public void onFailure(Call<JsonObject> call, Throwable t) {
                            progressDialog.dismiss();
                            Toast.makeText(context, "Something wrong", Toast.LENGTH_SHORT).show();
                        }
                    });
                } else {
                    myAlertDialog.ConfirmAmount.setError("Enter Amount");
                    myAlertDialog.ConfirmAmount.requestFocus();
                }
            } else {
                myAlertDialog.EnterCode.setError("Enter your OTP Code");
                myAlertDialog.EnterCode.requestFocus();
            }

        } else if (ppsitions == 3) {
            if (!TextUtils.isEmpty(myAlertDialog.Edate.getText().toString())) {
                myAlertDialog.cancel();
                progressDialog.show();

                api.confirmDelivery(
                        String.valueOf(parcel.getParcelId()),
                        "23",
                        myAlertDialog.ConfirmAmount.getText().toString(),
                        " ",
                        myAlertDialog.Edate.getText().toString(),
                        myAlertDialog.ENote.getText().toString()

                ).enqueue(new Callback<JsonObject>() {
                    @Override
                    public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                        progressDialog.dismiss();
                        if (response.isSuccessful() && response.body() != null) {
                            Toast.makeText(context, "Successfully submitted", Toast.LENGTH_SHORT).show();
                            // Log.e("tesst", response.body().getAsString().toString());
                            myAlertDialog.cancel();
                            listener.onItemClick(5);
                        } else {
                            try {
                                Log.e("tesst", response.errorBody().string());
                                Toast.makeText(context, response.errorBody().string(), Toast.LENGTH_SHORT).show();
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        }
                    }

                    @Override
                    public void onFailure(Call<JsonObject> call, Throwable t) {
                        progressDialog.dismiss();
                        Toast.makeText(context, "Something wrong", Toast.LENGTH_SHORT).show();
                    }
                });

            } else {
                myAlertDialog.Edate.setError("Select Date!");
                myAlertDialog.Edate.requestFocus();
            }


        } else if (ppsitions == 4) {
            if (!TextUtils.isEmpty(myAlertDialog.ENote.getText().toString())) {

                myAlertDialog.cancel();
                progressDialog.show();
                api.confirmDelivery(
                        String.valueOf(parcel.getParcelId()),
                        "24",
                        "",
                        " ",
                        " ",
                        myAlertDialog.ENote.getText().toString()

                ).enqueue(new Callback<JsonObject>() {
                    @Override
                    public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                        progressDialog.dismiss();
                        if (response.isSuccessful() && response.body() != null) {
                            Toast.makeText(context, "Successfully submitted", Toast.LENGTH_SHORT).show();
                            //Log.e("tesst",response.body().getAsString().toString());
                            myAlertDialog.cancel();
                            listener.onItemClick(5);
                        } else {
                            try {
                                Log.e("tesst", response.errorBody().string());
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        }
                    }

                    @Override
                    public void onFailure(Call<JsonObject> call, Throwable t) {
                        progressDialog.dismiss();
                        Toast.makeText(context, "Something wrong", Toast.LENGTH_SHORT).show();
                    }
                });

            } else {
                myAlertDialog.ENote.setError("Enter Note Please");
                myAlertDialog.ENote.requestFocus();
            }

        }


    }



    public boolean isCallPermissionGranted(Context context1) {
        if (Build.VERSION.SDK_INT >= 23) {
            if (ActivityCompat.checkSelfPermission(context1, android.Manifest.permission.CALL_PHONE)
                    == PackageManager.PERMISSION_GRANTED) {
                return true;
            } else {
                ActivityCompat.requestPermissions(activity, new String[]{Manifest.permission.CALL_PHONE}, 1);
                return false;
            }
        } else {
            return true;
        }
    }

}
