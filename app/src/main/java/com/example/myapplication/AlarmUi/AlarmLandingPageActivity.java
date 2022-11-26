package com.example.myapplication.AlarmUi;

import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.myapplication.R;

public final class AlarmLandingPageActivity extends AppCompatActivity {

    Button button1;
    Button button2;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            getWindow().setFlags(
                    WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS,
                    WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS
            );
        }

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_alarm_landing_page);

        button1  = (Button) findViewById(R.id.load_main_activity_btn);
        button2 = (Button) findViewById(R.id.dismiss_btn);

        //alarm main page 이동
        button1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(AlarmLandingPageActivity.this, AlarmActivity.class);
                startActivity(intent);
                finish();

                Toast myToast = Toast.makeText(getApplicationContext(),"알람 설정 페이지로 이동합니다.", Toast.LENGTH_SHORT);
                myToast.show();

            }
        });

        //디바이스 배경화면으로 이동
        button2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Intent.ACTION_MAIN);
                intent.addCategory(Intent.CATEGORY_HOME);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);

                Toast myToast = Toast.makeText(getApplicationContext(),"알람이 해제되었습니다.", Toast.LENGTH_SHORT);
                myToast.show();

            }
        });


    }

    public static Intent launchIntent(Context context) {
        final Intent i = new Intent(context, AlarmLandingPageActivity.class);
        i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        return i;
    }

}
