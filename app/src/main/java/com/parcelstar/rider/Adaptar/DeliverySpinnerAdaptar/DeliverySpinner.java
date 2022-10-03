package com.parcelstar.rider.Adaptar.DeliverySpinnerAdaptar;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.parcelstar.rider.Adaptar.listener.Clicklistener;
import com.parcelstar.rider.Model.DeliveryParcel.DeliveryStatus;
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
public class DeliverySpinner extends BaseAdapter {

    List<DeliveryStatus> areas=new ArrayList<>();
    LayoutInflater inflter;
    Clicklistener clicklistener;

    public DeliverySpinner(List<DeliveryStatus> areas, Context applicationContext) {
        this.areas = areas;
        this.clicklistener = clicklistener;
        inflter = (LayoutInflater.from(applicationContext));
    }

    @Override
    public int getCount() {
        return areas.size();
    }

    @Override
    public Object getItem(int position) {
        return areas.get(position);
    }

    @Override
    public long getItemId(int position) {
        return areas.get(position).getId();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        convertView = inflter.inflate(R.layout.model_spinner_item, null);
        TextView name=convertView.findViewById(R.id.tv_item);
        name.setText(areas.get(position).getName());

        return convertView;
    }
}
