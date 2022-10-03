
package com.parcelstar.rider.Model.Return;

import java.util.List;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
/**
 * Created by Md. Humayun Farid (Android Developer)
 * Create on 01/08/2022
 * Email: Humayunfarid1997@gmail.com
 * Website: www.humayunfarid.com
 * *** Happy Coding ***
 */
public class ReturnListContainer {

    @SerializedName("success")
    @Expose
    private Integer success;
    @SerializedName("message")
    @Expose
    private String message;
    @SerializedName("parcels")
    @Expose
    private List<ReturnParcel> parcels = null;

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

    public List<ReturnParcel> getParcels() {
        return parcels;
    }

    public void setParcels(List<ReturnParcel> parcels) {
        this.parcels = parcels;
    }

}
