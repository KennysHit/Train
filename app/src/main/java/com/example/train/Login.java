package com.example.train;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.material.textfield.TextInputEditText;

import cn.bmob.v3.Bmob;
import cn.bmob.v3.BmobUser;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.SaveListener;

public class Login extends AppCompatActivity {

    private TextInputEditText loginUsername;
    private TextInputEditText loginPassword;
    private Button login;
    private Button register;

    @Override
    protected void onCreate ( Bundle savedInstanceState ) {
        super.onCreate ( savedInstanceState );
        setContentView ( R.layout.login_layout );
        initView();
        Bmob.initialize ( this, "494cd47a0866a92f426c701b836534e0" );    //绑定BMOB云服务器

        if ( BmobUser.isLogin () ){
            Toast.makeText ( this,"已登录！",Toast.LENGTH_SHORT ).show ();
            Intent intent = new Intent (  );
            intent.setClass ( Login.this, Home.class );
            startActivity ( intent );
            finish ();
        }else{
            Toast.makeText ( this,"请登录！",Toast.LENGTH_SHORT ).show ();
        }     //判断用户是否登录

        login.setOnClickListener ( new View.OnClickListener ( ) {
            @Override
            public void onClick ( View view ) {
                String username = loginUsername.getText ().toString ().trim ();   //trim---去除字符串首尾空格
                String password = loginPassword.getText ().toString ().trim ();
                if(!username.isEmpty () && !password.isEmpty ()){

                    User user = new User ();
                    user.setUsername ( username );
                    user.setPassword ( password );

                    user.login ( new SaveListener< User > ( ) {

                        @Override
                        public void done ( User user , BmobException e ) {
                            if(e==null){
                                Toast.makeText ( Login.this,"登陆成功！",Toast.LENGTH_SHORT ).show ();
                                Intent intent = new Intent (  );
                                intent.setClass ( Login.this, Home.class );   //intent跳转界面或者传递消息
                                startActivity ( intent );
                                finish (); //杀死当前activity
                            }else {
                                if(e.getErrorCode ()==101) {
                                    Toast.makeText ( Login.this , "用户名或密码错误！" , Toast.LENGTH_SHORT ).show ( );
                                }
                            }
                        }
                    } );
                }else {
                    Toast.makeText ( Login.this ,"用户名或密码为空！" , Toast.LENGTH_SHORT ).show ( );
                }
            }
        } );

        register.setOnClickListener ( new View.OnClickListener ( ) {
            @Override
            public void onClick ( View view ) {
                Intent intent = new Intent (  );
                intent.setClass ( Login.this, Register.class );  //跳转到注册界面
                startActivity ( intent );
                finish ();
            }
        } );

    }
    private void initView(){
        loginUsername = (TextInputEditText ) findViewById ( R.id.login_username );
        loginPassword = (TextInputEditText) findViewById ( R.id.login_password );
        login = (Button) findViewById ( R.id.login_button_login );
        register = (Button) findViewById ( R.id.login_button_register );
    }  //绑定组件
}
