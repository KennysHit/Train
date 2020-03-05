package com.example.train;

import cn.bmob.v3.BmobObject;

public class Ticket extends BmobObject {
    private String departstation;
    private String endstation;
    private String endtime;
    private String passenger;
    private String buyer;
    private String station;
    private String costtime;
    private String phone;
    private String price;
    private String starttime;
    private String terminalstation;
    private String trainno;
    private String sittype;
    private String date;

    public Ticket() {
        this.setTableName("Ticket");
    }

    public String getDepartstation() {
        return departstation;
    }

    public String getEndstation() {
        return endstation;
    }

    public String getEndtime() {
        return endtime;
    }

    public String getPassenger() {
        return passenger;
    }

    public String getBuyer() {
        return buyer;
    }

    public String getStation() {
        return station;
    }

    public String getCosttime() {
        return costtime;
    }

    public String getPhone() {
        return phone;
    }

    public String getPrice() {
        return price;
    }

    public String getStarttime() {
        return starttime;
    }

    public String getTerminalstation() {
        return terminalstation;
    }

    public String getTrainno() {
        return trainno;
    }

    public String getSittype() {
        return sittype;
    }

    public String getDate() {
        return date;
    }

    public void setDepartstation(String departstation) {
        this.departstation = departstation;
    }

    public void setEndstation(String endstation) {
        this.endstation = endstation;
    }

    public void setEndtime(String endtime) {
        this.endtime = endtime;
    }

    public void setPassenger(String passenger) {
        this.passenger = passenger;
    }

    public void setBuyer(String buyer) {
        this.buyer = buyer;
    }

    public void setStation(String station) {
        this.station = station;
    }

    public void setCosttime(String costtime) {
        this.costtime = costtime;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public void setStarttime(String starttime) {
        this.starttime = starttime;
    }

    public void setTerminalstation(String terminalstation) {
        this.terminalstation = terminalstation;
    }

    public void setTrainno(String trainno) {
        this.trainno = trainno;
    }

    public void setSittype(String sittype) {
        this.sittype = sittype;
    }

    public void setDate(String date) {
        this.date = date;
    }
}
