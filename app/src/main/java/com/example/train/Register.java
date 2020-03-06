package com.example.train;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.material.textfield.TextInputEditText;

import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.SaveListener;

public class Register extends AppCompatActivity {

    private TextInputEditText registerUsername;
    private TextInputEditText registerPassword;
    private TextInputEditText registerPassword2;
    private Button register;
    private Button back;

    @Override
    protected void onCreate ( Bundle savedInstanceState ) {
        super.onCreate ( savedInstanceState );
        setContentView ( R.layout.register_layout );
        initView();

        register.setOnClickListener ( new View.OnClickListener ( ) {
            @Override
            public void onClick ( View view ) {
                String username = registerUsername.getText ().toString ().trim ();
                String password = registerPassword.getText ().toString ().trim ();
                String password2 = registerPassword2.getText ().toString ().trim ();
                if ( !username.isEmpty () && !password.isEmpty () && !password2.isEmpty () ) {
                    if ( password.equals ( password2 ) ) {

                        User user = new User ();
                        user.setUsername ( username );
                        user.setPassword ( password );

                        user.signUp ( new SaveListener< User > ( ) {
                            @Override
                            public void done ( User user , BmobException e ) {    //BMOB的内置函数
                                if(e==null){
                                    Toast.makeText ( Register.this , "注册成功！" , Toast.LENGTH_SHORT ).show ( );
                                    Intent intent = new Intent (  );
                                    intent.setClass ( Register.this, Home.class );
                                    startActivity ( intent );
                                    finish ();
                                }else{
                                    Toast.makeText ( Register.this , "注册失败，错误："+e.getMessage () , Toast.LENGTH_SHORT ).show ( );
                                    Log.e ( "error:",e.getMessage () );  //Toast--界面提示；log--终端信息打印
                                }
                            }
                        } );
                    }else{
                        Toast.makeText ( Register.this , "请确认两次密码输入相同！" , Toast.LENGTH_SHORT ).show ( );
                        registerPassword.setText ( "" );
                        registerPassword2.setText ( "" );
                    }
                }else {
                    Toast.makeText ( Register.this , "请将注册信息填写完整！" , Toast.LENGTH_SHORT ).show ( );
                }
            }
        } );

        back.setOnClickListener ( new View.OnClickListener ( ) {
            @Override
            public void onClick ( View view ) {
                Intent intent = new Intent (  );
                intent.setClass ( Register.this, Login.class );
                startActivity ( intent );
                finish ();
            }
        } );
    }

    private void initView(){
        registerUsername = (TextInputEditText ) findViewById ( R.id.register_username );
        registerPassword = (TextInputEditText) findViewById ( R.id.register_password );
        registerPassword2 = (TextInputEditText) findViewById ( R.id.register_password2 );
        back = (Button) findViewById ( R.id.register_button_back );
        register = (Button) findViewById ( R.id.register_button_register );
    }

}
