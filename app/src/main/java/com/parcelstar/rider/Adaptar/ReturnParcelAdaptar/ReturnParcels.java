package com.parcelstar.rider.Adaptar.ReturnParcelAdaptar;

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

//import com.easyflight.rider.Adaptar.listener.ReturnMenuClickListner;
import com.parcelstar.rider.Model.PickupRequest.PickupRequestAccept;
import com.parcelstar.rider.Model.Return.ReturnParcel;
import com.parcelstar.rider.Network.Api;
import com.parcelstar.rider.Network.RetrofitClient;
import com.parcelstar.rider.R;
import com.parcelstar.rider.Util.DeliveryAlertDialog;
import com.parcelstar.rider.Util.MyAlertDialog;

import org.jetbrains.annotations.NotNull;

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
public class ReturnParcels extends RecyclerView.Adapter<ReturnParcels.Viewholders> {

    public ReturnParcels.OnItemClickListener listener;

    List<ReturnParcel> returnParcellist = new ArrayList<>();
    Context context;
    Activity activity;
    ProgressDialog progressDialog;
    Api api;
    int spinnerPosition = 0;
    DeliveryAlertDialog myAlertDialog;
    public interface OnItemClickListener {
        void onItemClick(int position);
    }



    public ReturnParcels(List<ReturnParcel> returnS, Context context, Activity activity,OnItemClickListener listener) {
        this.returnParcellist = returnS;
        this.context = context;
        this.listener=listener;
        api = RetrofitClient.get(context).create(Api.class);

    }

    @NonNull
    @NotNull
    @Override
    public Viewholders onCreateViewHolder(@NonNull @NotNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.return_model_parcel, parent, false);
        return new ReturnParcels.Viewholders(view);
    }

    @Override
    public void onBindViewHolder(@NonNull @NotNull Viewholders holder, int position) {
        progressDialog = new ProgressDialog(holder.itemView.getContext());
        progressDialog.setMessage("Please Wait......");
        progressDialog.setCancelable(false);
        myAlertDialog = new DeliveryAlertDialog(holder.itemView.getContext());
        Log.d("tesst", String.valueOf(returnParcellist.size()));
        ReturnParcel returnParcel=returnParcellist.get(position);
        holder.Invoice.setText(String.valueOf(returnParcel.getParcelInvoice()));
        holder.CustomerName.setText(String.valueOf(returnParcel.getCustomerName()));
        holder.CustomerPhn.setText(String.valueOf(returnParcel.getCustomerContactNumber()));
        holder.CustomerAddress.setText(String.valueOf(returnParcel.getCustomerAddress()));
        holder.TotalCollectAmount.setText("Collection Amount : " + String.valueOf(returnParcel.getTotalCollectAmount()));
        holder.MerchantName.setText("Merchant Name : " + String.valueOf(returnParcel.getMerchantName()));
        holder.MerchantPhn.setText("Merchant Phone : " + String.valueOf(returnParcel.getMerchantContactNumber()));
        holder.MerchantAddress.setText("Merchant Address : " + String.valueOf(returnParcel.getMerchantAddress()));
        holder.ParcelStatus.setText(String.valueOf(returnParcel.getParcelStatus()));

        holder.MerchantPhn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String call = returnParcellist.get(position).getMerchantContactNumber();
                if (isCallPermissionGranted(context)) {
                    Intent callIntent = new Intent(Intent.ACTION_DIAL);
                    callIntent.setData(Uri.parse("tel:" + call));
                    callIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    context.startActivity(callIntent);
                }


            }
        });

        if (returnParcel.getParcelStatus().equals(new String("Return Branch  Run Start"))) {
            holder.Accept.setVisibility(View.VISIBLE);
            holder.reschedule.setVisibility(View.GONE);
            holder.Reject.setVisibility(View.VISIBLE);
            holder.complete.setVisibility(View.GONE);
            holder.ParcelStatus.setBackground(ContextCompat.getDrawable(context, R.drawable.status_round_orange));


        } else if(returnParcel.getParcelStatus().equals(new String("Return Rider Accept"))) {
            holder.complete.setVisibility(View.VISIBLE);
            holder.Reject.setVisibility(View.GONE);
            holder.Accept.setVisibility(View.GONE);
            holder.reschedule.setVisibility(View.GONE);
            holder.ParcelStatus.setBackground(ContextCompat.getDrawable(context, R.drawable.status_round_green));


        }else if(returnParcel.getParcelStatus().equals(new String("Return Rider Reject"))) {
            holder.complete.setVisibility(View.GONE);
            holder.Reject.setVisibility(View.GONE);
            holder.Accept.setVisibility(View.GONE);
            holder.reschedule.setVisibility(View.GONE);
            holder.ParcelStatus.setBackground(ContextCompat.getDrawable(context, R.drawable.status_round_green));


        }

        holder.Accept.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                progressDialog.show();
                Log.d("tesstt", String.valueOf(returnParcel.getParcelId()));
                api.getreturnaccept(String.valueOf(returnParcel.getParcelId())).enqueue(new Callback<PickupRequestAccept>() {
                    @Override
                    public void onResponse(Call<PickupRequestAccept> call, Response<PickupRequestAccept> response) {
                        Log.d("tesst", "tesssttt");
                        if (response.isSuccessful() && response.body() != null) {
                            progressDialog.dismiss();
                            listener.onItemClick(5);
                        }
                    }

                    @Override
                    public void onFailure(Call<PickupRequestAccept> call, Throwable t) {
                        progressDialog.dismiss();
                    }
                });
            }
        });
        holder.Reject.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MyAlertDialog myAlertDialog = new MyAlertDialog(holder.itemView.getContext());
                myAlertDialog.showConfirmDialog("Complete Request", "Confirm", "");
                myAlertDialog.button.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        myAlertDialog.cancel();
                        progressDialog.show();
                        api.getreturnreject(String.valueOf(returnParcel.getParcelId())).enqueue(new Callback<PickupRequestAccept>() {
                            @Override
                            public void onResponse(Call<PickupRequestAccept> call, Response<PickupRequestAccept> response) {
                                if (response.isSuccessful() && response.body() != null) {
                                    progressDialog.dismiss();
                                    listener.onItemClick(6);
                                }
                            }

                            @Override
                            public void onFailure(Call<PickupRequestAccept> call, Throwable t) {
                                    progressDialog.dismiss();
                            }
                        });
                    }
                });
                myAlertDialog.show();
            }
        });
        holder.complete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MyAlertDialog myAlertDialogo = new MyAlertDialog(holder.itemView.getContext());
                myAlertDialogo.showConfirmDialog("Complete Request", "Confirm", "");
                myAlertDialogo.button.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        String note=" ";
                        note=myAlertDialogo.editText.getText().toString();
                        myAlertDialogo.cancel();
                        progressDialog.show();
                        api.getreturncomplete(String.valueOf(returnParcel.getParcelId()),note).enqueue(new Callback<PickupRequestAccept>() {
                            @Override
                            public void onResponse(Call<PickupRequestAccept> call, Response<PickupRequestAccept> response) {
                                progressDialog.dismiss();
                                if (response.isSuccessful() && response.body() != null) {
                                    progressDialog.dismiss();
                                    listener.onItemClick(6);
                                    Toast.makeText(holder.itemView.getContext(), "Complete Request", Toast.LENGTH_SHORT).show();
                                }

                            }

                            @Override
                            public void onFailure(Call<PickupRequestAccept> call, Throwable t) {
                                progressDialog.dismiss();
                            }
                        });
                    }
                });
                myAlertDialogo.show();
            }
        });


/*        holder.Accept.setOnClickListener(new View.OnClickListener() {
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
      *//*  holder.reschedule.setOnClickListener(new View.OnClickListener() {
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
        });*//*
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


        *//*        myAlertDialog.spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
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
                });*//*


            }
        });


       holder.optionMenu.setOnClickListener(new DeliveryRequestMenuClickListner(this, listener,holder, delivery.get(position),context));*/
    }

    @Override
    public int getItemCount() {
        return returnParcellist.size();
    }

    public class Viewholders extends RecyclerView.ViewHolder {
        TextView Invoice, CustomerName, CustomerPhn, CustomerAddress, TotalCollectAmount, MerchantName,MerchantPhn,MerchantAddress, ParcelStatus,
        Accept,Reject,reschedule,complete;
        public Viewholders(@NonNull @NotNull View itemView) {
            super(itemView);

            Invoice = itemView.findViewById(R.id.tv_invoice);
            CustomerName = itemView.findViewById(R.id.tv_customer_name);
            CustomerPhn = itemView.findViewById(R.id.tv_phone);
            CustomerAddress = itemView.findViewById(R.id.tv_address);
            TotalCollectAmount = itemView.findViewById(R.id.tv_amount);
            MerchantName = itemView.findViewById(R.id.tv_merchant_name);
            MerchantAddress = itemView.findViewById(R.id.tv_merchant_address);
            MerchantPhn = itemView.findViewById(R.id.tv_merchant_no);
            ParcelStatus = itemView.findViewById(R.id.tv_status);
           // MerchantAddress = itemView.findViewById(R.id.tv_merchant_address);
            Accept=itemView.findViewById(R.id.txt_accept);
            Reject=itemView.findViewById(R.id.txt_reject);
            reschedule=itemView.findViewById(R.id.txt_re);
            complete=itemView.findViewById(R.id.txt_complete);
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
