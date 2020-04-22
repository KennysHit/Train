package com.example.train;

import java.io.Serializable;

public class QueryResult implements Serializable {

    private int status; //0
    private String msg; //"ok"
    private QueryInfo result; //

    public int getStatus () {
        return status;
    }

    public String getMsg () {
        return msg;
    }

    public QueryInfo getResult () {
        return result;
    }

    public void setStatus ( int status ) {
        this.status = status;
    }

    public void setMsg ( String msg ) {
        this.msg = msg;
    }

    public void setResult ( QueryInfo result ) {
        this.result = result;
    }
}
