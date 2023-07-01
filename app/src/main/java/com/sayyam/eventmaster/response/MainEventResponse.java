package com.sayyam.eventmaster.response;

import com.sayyam.eventmaster.model.MainEventModel;

public class MainEventResponse {

    public int status;

    public MainEventModel getData() {
        return data;
    }

    public void setData(MainEventModel data) {
        this.data = data;
    }

    public MainEventModel data;

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }
}