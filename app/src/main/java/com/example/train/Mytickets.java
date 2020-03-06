package com.example.train;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.BmobUser;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;

public class Mytickets extends Fragment {

    private View view;
    private Button back;
    private RecyclerView mticketslist;
    private MyTicketsRecyclerAdapter myTicketsRecyclerAdapter;
    private List<Ticket> data;
    private User user = BmobUser.getCurrentUser(User.class);
    private Handler handler;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.mtickets_fragment, container, false);
        initView();
        initData();

        handler = new Handler(){
            @Override
            public void handleMessage( Message msg) {
                super.handleMessage(msg);
                switch (msg.what) {      //判断标志位
                    case 1:
                        /**
                         获取数据，更新UI
                         */
                        data = (List<Ticket>) msg.obj;
                        myTicketsRecyclerAdapter = new MyTicketsRecyclerAdapter( getActivity(), data );
                        mticketslist.setLayoutManager ( new LinearLayoutManager( getActivity() ) ); //设置列表样式
                        mticketslist.setAdapter ( myTicketsRecyclerAdapter );
                        myTicketsRecyclerAdapter.setOnItemClickListener ( new MyTicketsRecyclerAdapter.OnItemClickListener ( ) {
                            @Override
                            public void onClick ( int position ) {
                                Fragment mticketinfo = new MyTicketInfo();
                                Bundle args = new Bundle();
                                args.putSerializable("mticket", data.get(position));
                                mticketinfo.setArguments(args);
                                getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.home, mticketinfo).commit();
                            }
                        } );
                        break;
                    case 2:
                        Toast.makeText ( getActivity(), "查询结果为空！", Toast.LENGTH_SHORT ).show ();
                }
            }
        };

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Fragment query = new Query();
                getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.home, query).commit();
            }
        });


        return view;
    }

    private void initView(){
        mticketslist = (RecyclerView) view.findViewById(R.id.mtickets_list);
        back = (Button) view.findViewById(R.id.mtickets_back);
    }

    private void initData(){
        BmobQuery<Ticket> query = new BmobQuery<Ticket>();
        query.addWhereEqualTo("buyer", user.getUsername());  //设置查询条件，购买人为当前用户的用户民
        query.setLimit(50);   //一次性查询的数目上限
        query.findObjects(new FindListener<Ticket>() {
            @Override
            public void done(List<Ticket> object, BmobException e) {
                if(e==null){
                    Message msg = Message.obtain();
                    msg.obj = object;
                    msg.what = 1;   //消息的标志
                    handler.sendMessage(msg);
                }else{
                    Toast.makeText(getActivity(), "查询失败" + e.getMessage(), Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}
