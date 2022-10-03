package com.parcelstar.rider.Model.Dashboard;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
/**
 * Created by Md. Humayun Farid (Android Developer)
 * Create on 01/08/2022
 * Email: Humayunfarid1997@gmail.com
 * Website: www.humayunfarid.com
 * *** Happy Coding ***
 */
public class DashBordDataContainer {
    @SerializedName("success")
    @Expose
    private Integer success;
    @SerializedName("message")
    @Expose
    private String message;

    @SerializedName("dashboard_count")
    @Expose
    private DashBoardData dashBoardData;

    public DashBoardData getDashBoardData() {
        return dashBoardData;
    }

    public void setDashBoardData(DashBoardData dashBoardData) {
        this.dashBoardData = dashBoardData;
    }

    public Integer getSuccess() {
        return success;
    }

    public void setSuccess(Integer success) {
        this.success = success;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
