package com.parcelstar.rider.Adaptar.PickupParcelAdaptar;

import android.Manifest;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

//import com.easyflight.rider.Adaptar.listener.RequestMenuClickListener;
import com.parcelstar.rider.Model.PickUpParcel.Parcel;
import com.parcelstar.rider.Model.PickupRequest.PickupRequestAccept;
import com.parcelstar.rider.Network.Api;
import com.parcelstar.rider.Network.RetrofitClient;
import com.parcelstar.rider.R;
import com.parcelstar.rider.Util.MyAlertDialog;

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
public class RequestParcelAdaptar extends RecyclerView.Adapter<RequestParcelAdaptar.Viewholders> {

    List<Parcel> pickup = new ArrayList<>();
    Context context;
    public OnItemClickListener listener;
    ProgressDialog progressDialog;
    Api api;
    Activity activity;
    public interface OnItemClickListener {
        void onItemClick(int position);
    }

    public RequestParcelAdaptar(List<Parcel> pickup, Context context, Activity activity,RequestParcelAdaptar.OnItemClickListener listener) {
        this.pickup = pickup;
        this.context = context;
        this.listener = listener;
        this.activity=activity;
        api = RetrofitClient.get(context).create(Api.class);

    }

    @Override
    public RequestParcelAdaptar.Viewholders onCreateViewHolder(@NonNull @NotNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.pickup_model_parcel, parent, false);

        return new RequestParcelAdaptar.Viewholders(view);
    }

    @Override
    public void onBindViewHolder(@NonNull @NotNull RequestParcelAdaptar.Viewholders holder, int position ) {
        progressDialog = new ProgressDialog(holder.itemView.getContext());
        progressDialog.setMessage("Please Wait......");
        progressDialog.setCancelable(false);
        Parcel parcel=pickup.get(position);
        holder.Invoice.setText(String.valueOf(pickup.get(position).getParcelInvoice()));
        holder.CustomerName.setText(String.valueOf(pickup.get(position).getCustomerName()));
        holder.CustomerPhn.setText(String.valueOf(pickup.get(position).getCustomerContactNumber()));
        holder.CustomerAddress.setText(String.valueOf(pickup.get(position).getCustomerAddress()));
        holder.TotalCollectAmount.setText("Collection Amount : " + String.valueOf(pickup.get(position).getTotalCollectAmount()));
        holder.MerchantName.setText("Merchant Name : " + String.valueOf(pickup.get(position).getMerchantName()));
        holder.MerchantPhn.setText("Merchant Phone : " + String.valueOf(pickup.get(position).getMerchantContactNumber()));
        holder.MerchantAddress.setText("Merchant Address : " + String.valueOf(pickup.get(position).getMerchantAddress()));
        holder.ParcelStatus.setText(String.valueOf(pickup.get(position).getParcelStatus()));
        Log.d("tesst", parcel.getParcelStatus());


        holder.MerchantPhn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String call = pickup.get(position).getMerchantContactNumber();
                if (isCallPermissionGranted(context)) {
                    Intent callIntent = new Intent(Intent.ACTION_DIAL);
                    callIntent.setData(Uri.parse("tel:" + call));
                    callIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    context.startActivity(callIntent);
                }


            }
        });

        if (parcel.getParcelStatus().equals(new String("Pickup Run Create"))) {
            holder.Accept.setVisibility(View.VISIBLE);
            holder.reschedule.setVisibility(View.VISIBLE);
            holder.Reject.setVisibility(View.VISIBLE);
            holder.complete.setVisibility(View.GONE);
            holder.ParcelStatus.setBackground(ContextCompat.getDrawable(context, R.drawable.status_round_orange));


        } else if(parcel.getParcelStatus().equals(new String("Pickup Run Accept Rider"))) {
            holder.complete.setVisibility(View.VISIBLE);
            holder.Reject.setVisibility(View.GONE);
            holder.Accept.setVisibility(View.GONE);
            holder.reschedule.setVisibility(View.VISIBLE);
            holder.ParcelStatus.setBackground(ContextCompat.getDrawable(context, R.drawable.status_round_green));


        }

        holder.Accept.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                progressDialog.show();
                api.getrequestacc(String.valueOf(parcel.getParcelId())).enqueue(new Callback<PickupRequestAccept>() {
                    @Override
                    public void onResponse(Call<PickupRequestAccept> call, Response<PickupRequestAccept> response) {
                        if (response.isSuccessful() && response.body() != null) {
                            progressDialog.dismiss();
                            Toast.makeText(context, "Request Accept", Toast.LENGTH_SHORT).show();
                            listener.onItemClick(5);
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
            }
        });
        holder.Reject.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                progressDialog.show();
                api.getrequestreject(String.valueOf(parcel.getParcelId())).enqueue(new Callback<PickupRequestAccept>() {
                    @Override
                    public void onResponse(Call<PickupRequestAccept> call, Response<PickupRequestAccept> response) {
                        if (response.isSuccessful() && response.body() != null) {
                            progressDialog.dismiss();
                            Toast.makeText(context, "Request Reject", Toast.LENGTH_SHORT).show();
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
            }
        });
        holder.reschedule.setOnClickListener(new View.OnClickListener() {
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
        });
        holder.complete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MyAlertDialog myAlertDialog = new MyAlertDialog(holder.itemView.getContext());
                myAlertDialog.showConfirmDialog("Complete Request", "Confirm", "");
                myAlertDialog.button.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        myAlertDialog.cancel();
                        progressDialog.show();
                        String note=" ";
                        note=myAlertDialog.editText.getText().toString();
                        Log.d("testt", note);
                        api.getpickcomplete(String.valueOf(parcel.getParcelId())).enqueue(new Callback<PickupRequestAccept>() {
                            @Override
                            public void onResponse(Call<PickupRequestAccept> call, Response<PickupRequestAccept> response) {
                                if (response.isSuccessful() && response.body() != null) {
                                    progressDialog.dismiss();
                                    listener.onItemClick(6);
                                    Toast.makeText(holder.itemView.getContext(), "Complete Request", Toast.LENGTH_SHORT).show();
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
            }
        });
    }

    @Override
    public int getItemCount() {
        return pickup.size();
    }

    public class Viewholders extends RecyclerView.ViewHolder {

        TextView Invoice, CustomerName,MerchantPhn,MerchantAddress,
                CustomerPhn, CustomerAddress, TotalCollectAmount,
                MerchantName, ParcelStatus,
                Accept,Reject,reschedule,complete;
       // ImageView optionMenu;


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

            //optionMenu = itemView.findViewById(R.id.iv_menu);
        }

        /*public ImageView getoption() {
            return this.optionMenu;
        }*/
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
