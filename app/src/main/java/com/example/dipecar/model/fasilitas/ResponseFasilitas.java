package com.example.dipecar.model.fasilitas;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.example.dipecar.model.model_mobil.DataCars;

import java.util.List;


public class ResponseFasilitas {
    @SerializedName("status")
    @Expose
    private Boolean status;
    @SerializedName("message")
    @Expose
    private String message;
    @SerializedName("data")
    @Expose
    private List<DataFasilitas> data = null;

    public Boolean getStatus() {
        return status;
    }

    public void setStatus(Boolean status) {
        this.status = status;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public List<DataFasilitas> getData() {
        return data;
    }

    public void setData(List<DataFasilitas> data) {
        this.data = data;
    }
}
