package com.example.train;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.TimeZone;

import static com.example.train.Demo.calcAuthorization;
import static com.example.train.Demo.urlencode;

public class Query extends Fragment {

    private View view;
    public static String secretID  = "AKIDAZ8334x6lhLNqb6Ipei6lvkTiylbR3tuCcNm";
    public static String secretKey = "f3pzpDV20P3qsih4XsJC1vGsO2k3Xt59XqY0kmuo";
    public static String source = "market";
    private String result = "";
    private QueryResult query_result;
    private TicketRecyclerAdapter ticketRecyclerAdapter;
    private Handler handler;  //消息处理对象
    private RecyclerView ticketList;
    private TextView startInfo;
    private TextView endInfo;
    private Button dateButton;
    private TextView dateInfo;
    private Button queryButton;
    private Calendar calendar = Calendar.getInstance ();
    private int myYear = calendar.get ( Calendar.YEAR );
    private int myMonth = calendar.get ( Calendar.MONTH );
    private int myDay = calendar.get ( Calendar.DAY_OF_MONTH );

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.query_fragment, container, false);

        initView();

        queryButton.setOnClickListener ( new View.OnClickListener ( ) {
            @Override
            public void onClick ( View view ) {
                /**
                 * Android8.0之后服务器请求不能放在主线程
                 */
                new Thread(new Runnable(){
                    @Override
                    public void run() {
                        String start = startInfo.getText ().toString ().trim ();
                        String end = endInfo.getText ().toString ().trim ();
                        String date = dateInfo.getText ().toString ().trim ();
                        Log.i ("info:" , start+"  "+end+"  "+date );
                        result = getInfo (start, end, date); //拿到查询结果的字符串类型的json对象
                        Log.i ( "result:", result );
                        //从全局池中返回一个message实例，避免多次创建message（如new Message）
                        Message msg = Message.obtain();
                        if(analysisJson ( result )!=null){
                            msg.obj = analysisJson ( result );  //拿到解析后的json对象
                            msg.what = 1;   //消息的标志
                            handler.sendMessage(msg);
                        }else {
                            msg.what = 2;
                            handler.sendMessage(msg);
                        }

                    }
                }).start ();

            }
        } );

        dateButton.setOnClickListener ( new View.OnClickListener ( ) {
            @Override
            public void onClick ( View view ) {
                DatePickerDialog datePickerDialog = new DatePickerDialog ( getActivity() , new DatePickerDialog.OnDateSetListener ( ) {
                    @Override
                    public void onDateSet (DatePicker datePicker , int year , int month , int day ) {
                        myYear = year;
                        myMonth = month + 1;
                        myDay = day;
                        dateInfo.setText ( myYear+"-"+myMonth+"-"+myDay );
                    }
                }, myYear, myMonth, myDay);
                datePickerDialog.show ();
            }
        } );

        return view;
    }

    private void initView(){

        startInfo = (TextView) view.findViewById ( R.id.start_info );
        endInfo = (TextView) view.findViewById ( R.id.end_info );
        dateButton = (Button ) view.findViewById ( R.id.date_btn );
        dateInfo = (TextView) view.findViewById ( R.id.date_info );
        queryButton = (Button) view.findViewById ( R.id.query_btn );
        ticketList = (RecyclerView) view.findViewById ( R.id.ticket_list );

        handler = new Handler(){
            @Override
            public void handleMessage( Message msg) {
                super.handleMessage(msg);
                switch (msg.what) {      //判断标志位
                    case 1:
                        /**
                         获取数据，更新UI
                         */
                        System.out.println ( "msg.obj:" + msg.obj );
                        query_result = (QueryResult ) msg.obj;
                        ticketRecyclerAdapter = new TicketRecyclerAdapter ( getActivity(), query_result );
                        ticketList.setLayoutManager ( new LinearLayoutManager( getActivity() ) ); //设置列表样式
                        ticketList.setAdapter ( ticketRecyclerAdapter );
                        ticketRecyclerAdapter.setOnItemClickListener ( new TicketRecyclerAdapter.OnItemClickListener ( ) {
                            @Override
                            public void onClick ( int position ) {
                                Fragment ticketinfo = new TicketInfo();
                                Bundle args = new Bundle();
                                args.putInt("position", position);
                                args.putSerializable("queryresult", query_result);
                                ticketinfo.setArguments(args);
                                getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.home, ticketinfo).commit();
                            }
                        } );
                        break;
                    case 2:
                        Toast.makeText ( getActivity(), "查询结果为空！", Toast.LENGTH_SHORT ).show ();
                }
            }
        };
    }

    /**
     * @param start 出发站
     * @param end 终点站
     * @param date 出发时间
     * @return 字符串类型的json对象
     */
    private String getInfo(String start, String end, String date){
        String result = "";
        Calendar cd = Calendar.getInstance();
        SimpleDateFormat sdf = new SimpleDateFormat ("EEE, dd MMM yyyy HH:mm:ss 'GMT'", Locale.US);
        sdf.setTimeZone( TimeZone.getTimeZone("GMT"));
        String datetime = sdf.format(cd.getTime());
        // 签名
        String auth = null;
        try {
            auth = calcAuthorization(source, secretID, secretKey, datetime);
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace ( );
        } catch (InvalidKeyException e) {
            e.printStackTrace ( );
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace ( );
        }
        // 请求方法
        String method = "GET";
        // 请求头
        Map<String, String> headers = new HashMap<String, String>();
        headers.put("X-Source", source);
        headers.put("X-Date", datetime);
        headers.put("Authorization", auth);

        // 查询参数
        Map<String, String> queryParams = new HashMap<String, String>();
        queryParams.put("date", date);
        queryParams.put("end", end);
        queryParams.put("start", start);
        // body参数
        Map<String, String> bodyParams = new HashMap<String, String>();

        // url参数拼接
        String url = "https://service-mptf1093-1257101137.ap-shanghai.apigateway.myqcloud.com/release/train/ticket";
        if (!queryParams.isEmpty()) {
            try {
                url += "?" + urlencode(queryParams);
                System.out.println ( url );
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace ( );
            }
        }

        BufferedReader in = null;
        try {
            URL realUrl = new URL (url);
            HttpURLConnection conn = ( HttpURLConnection ) realUrl.openConnection();
            conn.setConnectTimeout(5000);
            conn.setReadTimeout(5000);
            conn.setRequestMethod(method);

            // request headers
            for (Map.Entry<String, String> entry : headers.entrySet()) {
                conn.setRequestProperty(entry.getKey(), entry.getValue());
            }

            // request body
            Map<String, Boolean> methods = new HashMap<> ();
            methods.put("POST", true);
            methods.put("PUT", true);
            methods.put("PATCH", true);
            Boolean hasBody = methods.get(method);
            if (hasBody != null) {
                conn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");

                conn.setDoOutput(true);
                DataOutputStream out = new DataOutputStream (conn.getOutputStream());
                out.writeBytes(urlencode(bodyParams));
                out.flush();
                out.close();
            }

            // 定义 BufferedReader输入流来读取URL的响应
            in = new BufferedReader (new InputStreamReader(conn.getInputStream()));
            String line;
            while ((line = in.readLine()) != null) {
                result += line;
            }

            System.out.println(result);
        } catch (Exception e) {
            System.out.println(e);
            e.printStackTrace();
        } finally {
            try {
                if (in != null) {
                    in.close();
                }
            } catch (Exception e2) {
                e2.printStackTrace();
            }
        }
        return result;
    }

    /**
     * 将拿到的字符串类型的json对象解析为对应json对象的javaBean对象
     * @param jsonData 拿到的字符串类型的json对象
     * @return 解析后的javabean对象
     */
    private QueryResult analysisJson( String jsonData){
        try {
            JSONObject jsonQr = new JSONObject ( jsonData );
            JSONObject jsonQi = new JSONObject ( jsonQr.getString ( "result" ) );
            JSONArray jsonti = new JSONArray ( jsonQi.getString ( "list" ) );

            QueryResult queryResult = new QueryResult ();
            QueryInfo queryInfo = new QueryInfo ();

            //解析QueryResult
            queryResult.setStatus ( (Integer.valueOf ( jsonQr.getString("status") )));
            queryResult.setMsg ( jsonQr.getString ( "msg" ) );
            queryResult.setResult ( queryInfo );
            //解析QueryInfo
            queryInfo.setStart ( jsonQi.getString ( "start" ) );
            queryInfo.setEnd ( jsonQi.getString ( "end" ) );
            queryInfo.setDate ( jsonQi.getString ( "date" ) );


            Log.i ( "log:", String.valueOf ( queryResult.getStatus () ) );
            Log.i ( "log:", queryResult.getMsg () );
            Log.i ("log:", queryResult.getResult ().getStart ());
            Log.i ("log:", queryResult.getResult ().getEnd ());
            Log.i ("log:", queryResult.getResult ().getDate ());
            //解析TrainInfo
            queryInfo.setTrainInfos ( new ArrayList< TrainInfo >(  ) );
            for(int i=0; i<jsonti.length (); i++){
                JSONObject jsonObject = jsonti.getJSONObject ( i );

                TrainInfo trainInfo = new TrainInfo ();

                trainInfo.setTrainno ( jsonObject.getString ( "trainno" ) );
                System.out.println (jsonObject.getString ( "trainno" ));
                trainInfo.setType ( jsonObject.getString ( "type" ) );
                trainInfo.setTypename ( jsonObject.getString ( "typename" ) );
                trainInfo.setStation ( jsonObject.getString ( "station" ) );
                trainInfo.setEndstation ( jsonObject.getString ( "endstation" ) );
                trainInfo.setDeparturetime ( jsonObject.getString ( "departuretime" ) );
                trainInfo.setArrivaltime ( jsonObject.getString ( "arrivaltime" ) );
                trainInfo.setCosttime ( jsonObject.getString ( "costtime" ) );
                trainInfo.setTrainno12306 ( jsonObject.getString ( "trainno12306" ) );
                trainInfo.setDistance ( jsonObject.getString ( "distance" ) );
                trainInfo.setDay ( jsonObject.getString ( "day" ) );
                trainInfo.setIsend ( jsonObject.getString ( "isend" ) );
                trainInfo.setSequenceno ( jsonObject.getString ( "sequenceno" ) );
                trainInfo.setStationcode ( jsonObject.getString ( "stationcode" ) );
                trainInfo.setEndstationcode ( jsonObject.getString ( "endstationcode" ) );
                trainInfo.setDepartstation ( jsonObject.getString ( "departstation" ) );
                trainInfo.setTerminalstation ( jsonObject.getString ( "terminalstation" ) );
                trainInfo.setDepartstationcode ( jsonObject.getString ( "departstationcode" ) );
                trainInfo.setTerminalstationcode ( jsonObject.getString ( "terminalstationcode" ) );
                trainInfo.setStartdate ( jsonObject.getString ( "startdate" ) );
                trainInfo.setCanbay ( jsonObject.getString ( "canbay" ) );
                trainInfo.setPriceed ( jsonObject.getString ( "priceed" ) );
                trainInfo.setNumed ( jsonObject.getString ( "numed" ) );
                trainInfo.setPriceyd ( jsonObject.getString ( "priceyd" )  );
                trainInfo.setNumyd ( jsonObject.getString ( "numyd" ) );
                trainInfo.setPricesw ( jsonObject.getString ( "pricesw" )  );
                trainInfo.setNumsw ( jsonObject.getString ( "numsw" ) );
                trainInfo.setPricetd ( jsonObject.getString ( "pricetd" )  );
                trainInfo.setNumtd ( jsonObject.getString ( "numtd" ) );
                trainInfo.setPricerz ( jsonObject.getString ( "pricerz" )  );
                trainInfo.setNumrz ( jsonObject.getString ( "numrz" ) );
                trainInfo.setPriceyz ( jsonObject.getString ( "priceyz" )  );
                trainInfo.setNumyz ( jsonObject.getString ( "numyz" ) );
                trainInfo.setPricegr1 ( jsonObject.getString ( "pricegr1" )  );
                trainInfo.setNumgr1 ( jsonObject.getString ( "numgr1" ) );
                trainInfo.setPricegr2 ( jsonObject.getString ( "pricegr2" ) );
                trainInfo.setNumgr2 ( jsonObject.getString ( "numgr2" ) );
                trainInfo.setPricerw1 ( jsonObject.getString ( "pricerw1" ) );
                trainInfo.setNumrw1 ( jsonObject.getString ( "numrw1" ) );
                trainInfo.setPricerw2 ( jsonObject.getString ( "pricerw2" ) );
                trainInfo.setNumrw2 ( jsonObject.getString ( "numrw2" ) );
                trainInfo.setPriceyw1 ( jsonObject.getString ( "priceyw1" )  );
                trainInfo.setNumyw1 ( jsonObject.getString ( "numyw1" ) );
                trainInfo.setPriceyw2 ( jsonObject.getString ( "priceyw2" ) );
                trainInfo.setNumyw2 ( jsonObject.getString ( "numyw2" ) );
                trainInfo.setPriceyw3 ( jsonObject.getString ( "priceyw3" ) );
                trainInfo.setNumyw3 ( jsonObject.getString ( "numyw3" ) );
                trainInfo.setPricewz ( jsonObject.getString ( "pricewz" ) );
                trainInfo.setNumwz ( jsonObject.getString ( "numwz" ) );
                trainInfo.setPriceqt ( jsonObject.getString ( "priceqt" ) );
                trainInfo.setNumqt ( jsonObject.getString ( "numqt" ) );
                trainInfo.setPricedw ( jsonObject.getString ( "pricedw" ) );
                trainInfo.setNumdw ( jsonObject.getString ( "numdw" ) );
                trainInfo.setPricedw1 ( jsonObject.getString ( "pricedw1" ) );
                trainInfo.setNumdw1 ( jsonObject.getString ( "numdw1" ) );

                if (trainInfo.getType().equals("G"))
                    queryInfo.getTrainInfos ().add ( trainInfo );



            }
            return  queryResult;
        } catch (JSONException e) {
            e.printStackTrace ( );
            return null;
        }

    }

    private String analysisJavaBean(QueryInfo queryInfo){
        Gson gson = new Gson();
        //toJson方法参数即一个javabean。返回值即一个json字符串
        String json = gson.toJson(queryInfo);
        return json;
    }

}
