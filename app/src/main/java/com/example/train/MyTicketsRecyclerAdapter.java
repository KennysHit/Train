package com.example.train;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class MyTicketsRecyclerAdapter extends RecyclerView.Adapter<MyTicketsRecyclerAdapter.MyTicketsViewHolder> {
    private List<Ticket> data;
    private Context context;
    private OnItemClickListener listener;

    public MyTicketsRecyclerAdapter( Context context , List<Ticket> data) {
        this.data = data;
        this.context = context;
    }

    public interface OnItemClickListener{
        void onClick(int position);
    }

    public void setOnItemClickListener ( OnItemClickListener listener ) {
        this.listener = listener;
    }


    @NonNull
    @Override
    public MyTicketsViewHolder onCreateViewHolder ( @NonNull ViewGroup parent , int viewType ) {
        View container = LayoutInflater.from ( parent.getContext () ).inflate (
                R.layout.mticketview, parent, false );
        return new MyTicketsViewHolder(container);
    }//将单个列表项界面与TickeViewHolder类绑定


    @Override
    public void onBindViewHolder ( @NonNull MyTicketsViewHolder holder , final int position ) {
        holder.bind ( this.context, position );
        holder.itemView.setOnClickListener ( new View.OnClickListener ( ) {
            @Override
            public void onClick ( View view ) {
                if ( listener != null ){
                    listener.onClick ( position );
                }
            }
        } );

    } //将每一项数据通过ViewHolder内部类绑定到指定的UI界面（ticket_layout），并且可以设置点击监听事件接口


    @Override
    /**
     * 返回列表项目数量
     */
    public int getItemCount () {
        return data.size ();
    } //设置列表项目数量

    /**
     * 绑定单个项目UI界面的内部类
     */
    public class MyTicketsViewHolder extends RecyclerView.ViewHolder{

        private TextView trainno;
        private TextView isStart;
        private TextView isEnd;
        private TextView station;
        private TextView endstation;
        private TextView departuretime;
        private TextView costtime;
        private TextView arrivaltime;
        private TextView passenger;
        private TextView buytime;
        private TextView date;

        /**
         * 构造方法
         * @param view
         */
        public MyTicketsViewHolder ( @NonNull View view ) {
            super ( view );
            trainno = (TextView) view.findViewById ( R.id.mticketview_trainno);
            isStart = (TextView) view.findViewById ( R.id.mticketview_isstart);
            isEnd = (TextView) view.findViewById ( R.id.mticketview_isend);
            station = (TextView) view.findViewById ( R.id.mticketview_station);
            endstation = (TextView) view.findViewById ( R.id.mticketview_endstation);
            departuretime = (TextView) view.findViewById ( R.id.mticketview_starttime);
            arrivaltime = (TextView) view.findViewById ( R.id.mticketview_endtime);
            costtime = (TextView) view.findViewById ( R.id.mticketview_costtime);
            date = (TextView) view.findViewById ( R.id.mticketview_date);
            passenger = (TextView) view.findViewById(R.id.mticketview_passenger);
            buytime = (TextView) view.findViewById(R.id.mticketview_buytime);
        }

        /**
         * 设置列表项UI界面数据
         * @param context
         */
        public void bind ( Context context, int i ){
            trainno.setText(data.get(i).getTrainno());
            if (data.get(i).getStation().equals(data.get(i).getDepartstation())) {
                isStart.setText("始");
            } else {
                isStart.setText("过");
            }
            if (data.get(i).getEndstation().equals(data.get(i).getTerminalstation())) {
                isEnd.setText("终");
            } else {
                isEnd.setText("过");
            }
            date.setText(data.get(i).getDate());
            station.setText(data.get(i).getStation());
            endstation.setText(data.get(i).getEndstation());
            departuretime.setText(data.get(i).getStarttime());
            arrivaltime.setText(data.get(i).getEndtime());
            costtime.setText(data.get(i).getCosttime());
            passenger.setText("乘客: " + data.get(i).getPassenger());
            buytime.setText(data.get(i).getUpdatedAt());
        }

    }

}
