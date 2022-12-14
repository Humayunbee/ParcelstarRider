package com.parcelstar.rider.Adaptar.DashboardAdaptar;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;
import com.parcelstar.rider.Model.Dashboard.DashBoardHelperModel;
import com.parcelstar.rider.R;

import java.util.ArrayList;
import java.util.List;
/**
 * Created by Md. Humayun Farid (Android Developer)
 * Create on 01/08/2022
 * Email: Humayunfarid1997@gmail.com
 * Website: www.humayunfarid.com
 * *** Happy Coding ***
 */
public class DashBoardAdaptar extends RecyclerView.Adapter<DashBoardAdaptar.ViewHolders> {

    List<DashBoardHelperModel> dashBoardData = new ArrayList<>();
    Context context;

    public DashBoardAdaptar(List<DashBoardHelperModel> dashBoardData, Context context) {
        this.dashBoardData = dashBoardData;
        this.context = context;
    }


    @Override
    public ViewHolders onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.model_dashboard, parent, false);
        return new DashBoardAdaptar.ViewHolders(view);
    }

    @Override
    public void onBindViewHolder(ViewHolders holder, int position) {
        holder.title.setText(dashBoardData.get(position).getTitle());
        holder.value.setText(String.format("%1$,.0f",dashBoardData.get(position).getValue()));
    }

    @Override
    public int getItemCount() {
        return dashBoardData.size();
    }

    public class ViewHolders extends RecyclerView.ViewHolder {

        TextView value, title;
        LinearLayout parentLayout;
        public ViewHolders(View itemView) {
            super(itemView);
//            parentLayout=itemView.findViewById(R.id.parent);
            value = itemView.findViewById(R.id.tv_counter);
            title = itemView.findViewById(R.id.tv_title);
        }
    }
}
