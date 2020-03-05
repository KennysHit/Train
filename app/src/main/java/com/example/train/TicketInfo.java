package com.example.train;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.google.android.material.textfield.TextInputEditText;

import java.util.LinkedList;
import java.util.List;

import cn.bmob.v3.BmobBatch;
import cn.bmob.v3.BmobObject;
import cn.bmob.v3.BmobUser;
import cn.bmob.v3.datatype.BatchResult;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.QueryListListener;

public class TicketInfo extends Fragment {

    private View view;
    private TextView date;
    private TextView station;
    private TextView endstation;
    private TextView starttime;
    private TextView endtime;
    private TextView costtime;
    private TextView departstation;
    private TextView teminalstation;
    private TextView ed_ishave;
    private TextView ed_price;
    private TextView yd_ishave;
    private TextView yd_price;
    private TextView sw_ishave;
    private TextView sw_price;
    private TextView trainno;
    private TextView itemcount;
    private TextInputEditText passenger;
    private TextInputEditText phone;
    private Button clean;
    private Button post;
    private Button additem;
    private Button back;
    private RadioGroup radioGroup;
    private RadioButton radioButton;
    private QueryResult queryResult;
    private int position;
    private String price;

    private User user = BmobUser.getCurrentUser(User.class);
    private List<BmobObject> data;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.ticketinfo_fragment, container, false);
        initView();
        queryResult = (QueryResult) getArguments().getSerializable("queryresult");
        position = getArguments().getInt("position");
        setText();

        additem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!passenger.getText().toString().trim().equals("") && !phone.getText().toString().trim().equals("") ){
                    int id = radioGroup.getCheckedRadioButtonId();
                    if (id!=-1){
                        radioButton = (RadioButton) view.findViewById(id);
                        if(radioButton.getText().toString().trim().equals("二等")){
                            if (ed_ishave.getText().toString().trim().equals("有")){
                                price = ed_price.getText().toString().trim();
                                setData();
                            }else {
                                Toast.makeText(getActivity(), "该类票已售空！", Toast.LENGTH_SHORT).show();
                            }
                        }
                        if(radioButton.getText().toString().trim().equals("一等")){
                            if (yd_ishave.getText().toString().trim().equals("有")){
                                price = yd_price.getText().toString().trim();
                                setData();
                            }else {
                                Toast.makeText(getActivity(), "该类票已售空！", Toast.LENGTH_SHORT).show();
                            }
                        }
                        if(radioButton.getText().toString().trim().equals("商务")){
                            if (sw_ishave.getText().toString().trim().equals("有")){
                                price = sw_price.getText().toString().trim();
                                setData();
                            }else {
                                Toast.makeText(getActivity(), "该类票已售空！", Toast.LENGTH_SHORT).show();
                            }
                        }
                    }else {
                        Toast.makeText(getActivity(), "请选择购票类型！", Toast.LENGTH_SHORT).show();
                    }
                }else {
                    Toast.makeText(getActivity(), "请输入乘客信息！", Toast.LENGTH_SHORT).show();
                }
            }
        });

        clean.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                data.clear();
                itemcount.setText("当前订单数: " + data.size());
            }
        });

        post.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!data.isEmpty()){
                    new BmobBatch().insertBatch(data).doBatch(new QueryListListener<BatchResult>() {

                        @Override
                        public void done(List<BatchResult> o, BmobException e) {
                            if(e==null){
                                data.clear();
                                itemcount.setText("当前订单数: " + data.size());
                                passenger.setText("");
                                phone.setText("");
                                Toast.makeText(getActivity(), o.size() + "张票已预定成功！", Toast.LENGTH_SHORT).show();
                            }else{
                                Toast.makeText(getActivity(), "预定失败！" + e.getMessage(), Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                }else {
                    Toast.makeText(getActivity(), "请添加乘客！", Toast.LENGTH_SHORT).show();
                }
            }
        });

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Fragment query = new Query();
                getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.home, query).commit();
            }
        });

        return view;
    }

    public void initView(){
        data = new LinkedList<BmobObject>();
        date = (TextView) view.findViewById(R.id.ticketinfo_date);
        station = (TextView) view.findViewById(R.id.ticketinfo_station);
        endstation = (TextView) view.findViewById(R.id.ticketinfo_endstation);
        starttime = (TextView) view.findViewById(R.id.ticketinfo_starttime);
        endtime = (TextView) view.findViewById(R.id.ticketinfo_endtime);
        passenger = (TextInputEditText) view.findViewById(R.id.ticketinfo_passenger);
        phone = (TextInputEditText) view.findViewById(R.id.ticketinfo_phone);
        departstation = (TextView) view.findViewById(R.id.ticketinfo_departstation);
        teminalstation = (TextView) view.findViewById(R.id.ticketinfo_teminalstation);
        costtime = (TextView) view.findViewById(R.id.ticketinfo_costtime);
        trainno = (TextView) view.findViewById(R.id.ticketinfo_trainno);
        itemcount = (TextView) view.findViewById(R.id.ticketinfo_itemscount);
        clean = (Button) view.findViewById(R.id.ticketinfo_clean);
        post = (Button) view.findViewById(R.id.ticketinfo_post);
        additem = (Button) view.findViewById(R.id.ticketinfo_additem);
        back = (Button) view.findViewById(R.id.ticketinfo_back);
        radioGroup = (RadioGroup) view.findViewById(R.id.ticketinfo_rg_sittype);
        ed_ishave = (TextView) view.findViewById(R.id.ticketinfo_ed_ishave);
        ed_price = (TextView) view.findViewById(R.id.ticketinfo_ed_price);
        yd_ishave = (TextView) view.findViewById(R.id.ticketinfo_yd_ishave);
        yd_price = (TextView) view.findViewById(R.id.ticketinfo_yd_price);
        sw_ishave = (TextView) view.findViewById(R.id.ticketinfo_sw_ishave);
        sw_price = (TextView) view.findViewById(R.id.ticketinfo_sw_price);
    }

    private void setData(){
        Ticket ticket = new Ticket();
        ticket.setSittype(radioButton.getText().toString().trim());
        ticket.setDate(date.getText().toString().trim());
        ticket.setTrainno(trainno.getText().toString().trim());
        ticket.setStarttime(starttime.getText().toString().trim());
        ticket.setEndtime(endtime.getText().toString().trim());
        ticket.setStation(station.getText().toString().trim());
        ticket.setEndstation(endstation.getText().toString().trim());
        ticket.setDepartstation(departstation.getText().toString().trim());
        ticket.setTerminalstation(teminalstation.getText().toString().trim());
        ticket.setPassenger(passenger.getText().toString().trim());
        ticket.setPhone(phone.getText().toString().trim());
        ticket.setCosttime(costtime.getText().toString().trim());
        ticket.setPrice(price);
        ticket.setBuyer(user.getUsername());
        data.add(ticket);
        itemcount.setText("当前订单数: " + data.size());
        passenger.setText("");
        phone.setText("");
        Toast.makeText(getActivity(), "添加成功！", Toast.LENGTH_SHORT).show();
        System.out.println(ticket.getSittype());
    }

    private void setText(){
        trainno.setText(queryResult.getResult().getTrainInfos().get(position).getTrainno());
        date.setText(queryResult.getResult().getDate());
        starttime.setText(queryResult.getResult().getTrainInfos().get(position).getDeparturetime());
        endtime.setText(queryResult.getResult().getTrainInfos().get(position).getArrivaltime());
        costtime.setText(queryResult.getResult().getTrainInfos().get(position).getCosttime());
        station.setText(queryResult.getResult().getTrainInfos().get(position).getStation());
        endstation.setText(queryResult.getResult().getTrainInfos().get(position).getEndstation());
        departstation.setText(queryResult.getResult().getTrainInfos().get(position).getDepartstation());
        teminalstation.setText(queryResult.getResult().getTrainInfos().get(position).getTerminalstation());
        ed_price.setText(queryResult.getResult().getTrainInfos().get(position).getPriceed());
        yd_price.setText(queryResult.getResult().getTrainInfos().get(position).getPriceyd());
        sw_price.setText(queryResult.getResult().getTrainInfos().get(position).getPricesw());

        if (!queryResult.getResult().getTrainInfos().get(position).getNumed().equals("无"))
            ed_ishave.setText("有");
        else
            ed_ishave.setText("无");

        if (!queryResult.getResult().getTrainInfos().get(position).getNumyd().equals("无"))
            yd_ishave.setText("有");
        else
            yd_ishave.setText("无");

        if (!queryResult.getResult().getTrainInfos().get(position).getNumsw().equals("无"))
            sw_ishave.setText("有");
        else
            sw_ishave.setText("无");
    }
}
