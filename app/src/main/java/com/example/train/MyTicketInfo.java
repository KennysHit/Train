package com.example.train;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.UpdateListener;

public class MyTicketInfo extends Fragment {

    private View view;
    private TextView date;
    private TextView station;
    private TextView endstation;
    private TextView starttime;
    private TextView endtime;
    private TextView price;
    private TextView costtime;
    private TextView departstation;
    private TextView teminalstation;
    private TextView trainno;
    private TextView passenger;
    private TextView phone;
    private TextView buyer;
    private TextView buytime;
    private Button giveback;
    private Button back;
    private Ticket ticket;
    private TextView sittype;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.myticketinfo_fragment, container, false);
        initView();

        ticket = (Ticket) getArguments().getSerializable("mticket");
        date.setText(ticket.getDate());
        station.setText(ticket.getStation());
        endstation.setText(ticket.getEndstation());
        departstation.setText(ticket.getDepartstation());
        teminalstation.setText(ticket.getTerminalstation());
        starttime.setText(ticket.getStarttime());
        endtime.setText(ticket.getEndtime());
        passenger.setText("乘客姓名： " + ticket.getPassenger());
        phone.setText("乘客电话:  " + ticket.getPhone());
        costtime.setText(ticket.getCosttime());
        price.setText(ticket.getPrice());
        trainno.setText(ticket.getTrainno());
        buyer.setText("购票人: " + ticket.getBuyer());
        buytime.setText("购票日期: " + ticket.getUpdatedAt());
        sittype.setText(ticket.getSittype() + "座");

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Fragment mtickets = new Mytickets();
                getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.home, mtickets).commit();
            }
        });

        giveback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ticket.setObjectId(ticket.getObjectId());
                ticket.delete(new UpdateListener() {
                    @Override
                    public void done(BmobException e) {
                        if(e==null){
                            Toast.makeText ( getActivity(), "退票成功！", Toast.LENGTH_SHORT ).show ();
                            Fragment mtickets = new Mytickets();
                            getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.home, mtickets).commit();
                        }else{
                            Toast.makeText ( getActivity(), "退票失败！" + e.getMessage(), Toast.LENGTH_SHORT ).show ();
                        }
                    }
                });
            }
        });  //BMOB内置函数

        return view;
    }

    public void initView(){
        date = (TextView) view.findViewById(R.id.mticketinfo_date);
        station = (TextView) view.findViewById(R.id.mticketinfo_station);
        endstation = (TextView) view.findViewById(R.id.mticketinfo_endstation);
        starttime = (TextView) view.findViewById(R.id.mticketinfo_starttime);
        endtime = (TextView) view.findViewById(R.id.mticketinfo_endtime);
        passenger = (TextView) view.findViewById(R.id.mticketinfo_passenger);
        phone = (TextView) view.findViewById(R.id.mticketinfo_phone);
        departstation = (TextView) view.findViewById(R.id.mticketinfo_departstation);
        teminalstation = (TextView) view.findViewById(R.id.mticketinfo_teminalstation);
        costtime = (TextView) view.findViewById(R.id.mticketinfo_costtime);
        trainno = (TextView) view.findViewById(R.id.mticketinfo_trainno);
        back = (Button) view.findViewById(R.id.mticketinfo_back);
        giveback = (Button) view.findViewById(R.id.mticketinfo_giveback);
        buyer = (TextView) view.findViewById(R.id.mticketinfo_buyer);
        buytime = (TextView) view.findViewById(R.id.mticketinfo_buytime);
        price = (TextView) view.findViewById(R.id.mticketinfo_price);
        sittype = (TextView) view.findViewById(R.id.miticketinfo_sittype);
    }
}
