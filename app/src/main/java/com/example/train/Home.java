package com.example.train;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.navigation.NavigationView;

import cn.bmob.v3.BmobUser;

public class Home extends AppCompatActivity {

    private NavigationView navigationView;
    private View headView;
    private TextView username;
    private Button logout;
    private User user = BmobUser.getCurrentUser(User.class);  //获得当前已登录的用户

    @Override
    protected void onCreate ( Bundle savedInstanceState ) {
        super.onCreate ( savedInstanceState );
        setContentView ( R.layout.home_layout);

        initView();

        Fragment query = new Query();
        getSupportFragmentManager().beginTransaction().replace(R.id.home, query).commit();  //替换掉home_layout里的home组件

        username.setText ( user.getUsername () );  //拿到侧滑框的用户名
        System.out.println ( user.getObjectId () );

        logout.setOnClickListener ( new View.OnClickListener ( ) {
            @Override
            public void onClick ( View view ) {
                user.logOut();   //清除缓存用户对象
                user = BmobUser.getCurrentUser(user.getClass ()); // 现在的currentUser是null了
                Toast.makeText ( Home.this, "退出登录！",Toast.LENGTH_SHORT ).show ();
                Intent intent = new Intent (  );
                intent.setClass ( Home.this, Login.class );
                startActivity ( intent );
                finish ();
            }
        } );

        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch ( item.getItemId () ){
                    case R.id.mtickets:
                        Fragment myticketslist = new Mytickets();
                        getSupportFragmentManager().beginTransaction().replace(R.id.home, myticketslist).commit();
                        break;

                }
                //在这里处理item的点击事件
                return true;
            }
        });
    }

    private void initView(){

        navigationView = ( NavigationView ) findViewById ( R.id.navigation );
        headView = navigationView.getHeaderView ( 0 );
        username = (TextView ) headView.findViewById ( R.id.nav_username );
        logout = (Button ) headView.findViewById ( R.id.nav_logout );

    }

}
