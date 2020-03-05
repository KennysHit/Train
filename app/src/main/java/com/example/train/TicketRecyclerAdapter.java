package com.example.train;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class TicketRecyclerAdapter extends RecyclerView.Adapter<TicketRecyclerAdapter.TicketViewHolder> {
    private QueryResult queryResult;
    private Context context;
    private OnItemClickListener listener;

    public TicketRecyclerAdapter( Context context , QueryResult data) {
        this.queryResult = data;
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
    public TicketViewHolder onCreateViewHolder ( @NonNull ViewGroup parent , int viewType ) {
        View container = LayoutInflater.from ( parent.getContext () ).inflate (
                R.layout.ticketview, parent, false );
        return new TicketViewHolder(container);
    }//将单个列表项界面与TickeViewHolder类绑定


    @Override
    public void onBindViewHolder ( @NonNull TicketViewHolder holder , final int position ) {
        holder.bind ( this.context, queryResult, position );
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
        return queryResult.getResult ().getTrainInfos ().size ();
    } //设置列表项目数量

    /**
     * 绑定单个项目UI界面的内部类
     */
    public class TicketViewHolder extends RecyclerView.ViewHolder{

        private TextView trainno;
        private TextView isStart;
        private TextView isEnd;
        private TextView station;
        private TextView endstation;
        private TextView departuretime;
        private TextView costtime;
        private TextView arrivaltime;
        private TextView numsw;
        private TextView numyd;
        private TextView numed;
        private TextView date;


        /**
         * 构造方法
         * @param itemView
         */
        public TicketViewHolder ( @NonNull View itemView ) {
            super ( itemView );
            trainno = (TextView) itemView.findViewById ( R.id.ticketview_trainno);
            isStart = (TextView) itemView.findViewById ( R.id.ticketview_isstart);
            isEnd = (TextView) itemView.findViewById ( R.id.ticketview_isend);
            station = (TextView) itemView.findViewById ( R.id.ticketview_station);
            endstation = (TextView) itemView.findViewById ( R.id.ticketview_endstation);
            departuretime = (TextView) itemView.findViewById ( R.id.ticketview_starttime);
            arrivaltime = (TextView) itemView.findViewById ( R.id.ticketview_endtime);
            costtime = (TextView) itemView.findViewById ( R.id.ticketview_costtime);
            numsw = (TextView) itemView.findViewById ( R.id.ticketview_numsw);
            numyd = (TextView) itemView.findViewById ( R.id.ticketview_numyd);
            numed = (TextView) itemView.findViewById ( R.id.ticketview_numed);
            date = (TextView) itemView.findViewById ( R.id.ticketview_date);
        }

        /**
         * 设置列表项UI界面数据
         * @param context
         * @param queryResult 拿到的数据
         * @param i 单个列表项的角标
         */
        public void bind ( Context context , QueryResult queryResult, int i ){
            trainno.setText(queryResult.getResult().getTrainInfos().get(i).getTrainno());
            if (queryResult.getResult().getTrainInfos().get(i).getStation().equals(queryResult.getResult().getTrainInfos().get(i).getDepartstation())) {
                isStart.setText("始");
            } else {
                isStart.setText("过");
            }
            if (queryResult.getResult().getTrainInfos().get(i).getEndstation().equals(queryResult.getResult().getTrainInfos().get(i).getTerminalstation())) {
                isEnd.setText("终");
            } else {
                isEnd.setText("过");
            }
            date.setText(queryResult.getResult().getDate());
            station.setText(queryResult.getResult().getTrainInfos().get(i).getStation());
            endstation.setText(queryResult.getResult().getTrainInfos().get(i).getEndstation());
            departuretime.setText(queryResult.getResult().getTrainInfos().get(i).getDeparturetime());
            arrivaltime.setText(queryResult.getResult().getTrainInfos().get(i).getArrivaltime());
            costtime.setText(queryResult.getResult().getTrainInfos().get(i).getCosttime());

            if (!queryResult.getResult().getTrainInfos().get(i).getNumed().equals("无"))
                numed.setText("有");
            else
                numed.setText("无");

            if (!queryResult.getResult().getTrainInfos().get(i).getNumyd().equals("无"))
                numyd.setText("有");
            else
                numyd.setText("无");

            if (!queryResult.getResult().getTrainInfos().get(i).getNumsw().equals("无"))
                numsw.setText("有");
            else
                numsw.setText("无");

        }

    }

}
