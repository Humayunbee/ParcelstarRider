package com.parcelstar.rider.Adaptar.PaidAmount;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.parcelstar.rider.Model.PaidAmount.PaidAmountModel;
import com.parcelstar.rider.R;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;
/**
 * Created by Md. Humayun Farid (Android Developer)
 * Create on 01/08/2022
 * Email: Humayunfarid1997@gmail.com
 * Website: www.humayunfarid.com
 * *** Happy Coding ***
 */
public class PaidAmountAdaptar extends RecyclerView.Adapter<PaidAmountAdaptar.Viewholders> {

    List<PaidAmountModel> paid = new ArrayList<>();
    Context context;

    public PaidAmountAdaptar(List<PaidAmountModel> paid, Context context) {
        this.paid = paid;
        this.context = context;
    }

    @NonNull
    @NotNull
    @Override
    public PaidAmountAdaptar.Viewholders onCreateViewHolder(@NonNull @NotNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.collection_model_parcel, parent, false);
        return new PaidAmountAdaptar.Viewholders(view);
    }

    @Override
    public void onBindViewHolder(@NonNull @NotNull PaidAmountAdaptar.Viewholders holder, int position) {
        holder.Invoice.setText(String.valueOf(paid.get(position).getParcelInvoice()));
        holder.CustomerName.setText(String.valueOf(paid.get(position).getCustomerName()));
        holder.CustomerPhn.setText(String.valueOf(paid.get(position).getCustomerContactNumber()));
        holder.CustomerAddress.setText(String.valueOf(paid.get(position).getCustomerAddress()));
        holder.TotalCollectAmount.setText("Collection Amount :" + String.valueOf(paid.get(position).getCollectAmount()));
        holder.MerchantName.setText("Merchant Name :" + String.valueOf(paid.get(position).getMerchantName()));
        holder.MerchantAddress.setText("Merchant Name :" + String.valueOf(paid.get(position).getMerchantAddress()));
        holder.MerchantNo.setText("Merchant Name :" + String.valueOf(paid.get(position).getMerchantContactNumber()));
        holder.ParcelStatus.setText(String.valueOf(paid.get(position).getParcelStatus()));
       // holder.nav.setVisibility(View.GONE);
    }

    @Override
    public int getItemCount() {
        return paid.size();
    }

    public class Viewholders extends RecyclerView.ViewHolder {

        TextView Invoice, CustomerName, CustomerPhn, CustomerAddress, TotalCollectAmount, MerchantName,MerchantAddress,MerchantNo, ParcelStatus;
      //  ImageView nav;

        public Viewholders(@NonNull @NotNull View itemView) {
            super(itemView);


            Invoice = itemView.findViewById(R.id.tv_invoice);
            CustomerName = itemView.findViewById(R.id.tv_customer_name);
            CustomerPhn = itemView.findViewById(R.id.tv_phone);
            CustomerAddress = itemView.findViewById(R.id.tv_address);
            TotalCollectAmount = itemView.findViewById(R.id.tv_amount);
            MerchantName = itemView.findViewById(R.id.tv_merchant_name);
            MerchantAddress = itemView.findViewById(R.id.tv_merchant_address);
            MerchantNo = itemView.findViewById(R.id.tv_merchant_no);
            ParcelStatus = itemView.findViewById(R.id.tv_status);
           // nav = itemView.findViewById(R.id.iv_menu);
        }
    }
}
