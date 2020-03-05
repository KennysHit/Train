package com.example.train;

import java.io.Serializable;
import java.util.List;

public class QueryInfo implements Serializable {

    private String start; //"成都"
    private String end; //"简阳"
    private String date; //"2020-04-15"
    private List<TrainInfo> trainInfos;

    public String getStart () {
        return start;
    }

    public String getEnd () {
        return end;
    }

    public String getDate () {
        return date;
    }

    public List< TrainInfo > getTrainInfos () {
        return trainInfos;
    }

    public void setStart ( String start ) {
        this.start = start;
    }

    public void setEnd ( String end ) {
        this.end = end;
    }

    public void setDate ( String date ) {
        this.date = date;
    }

    public void setTrainInfos ( List< TrainInfo > trainInfos ) {
        this.trainInfos = trainInfos;
    }
}