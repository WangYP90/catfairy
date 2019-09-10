package com.tj24.appmanager.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import com.tj24.appmanager.R;

public class UserEditActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.app_activity_user_edit);
    }

    public static void actionStart(Context context){
        Intent i = new Intent(context,UserEditActivity.class);
        context.startActivity(i);
    }
}
