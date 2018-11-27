package com.example.zhangkang.autoservicetest;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.example.anotation.BindView;
import com.example.anotation.BindViewActivity;
import com.example.anotation.ViewClick;
import com.example.anotation.ViewClicks;
import com.example.apt.BindViewHelper;

import self.zhangkang.base.BaseActivity;

@BindViewActivity
public class MainActivity extends BaseActivity {
    @BindView(R.id.button1)
    Button mButton1;
    @BindView(R.id.button2)
    Button mButton2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        BindViewHelper.bind(this);
    }

    @ViewClicks({R.id.button1, R.id.button2})
    public void onClkick(View view) {
        switch (view.getId()) {
            case R.id.button1:
                Toast.makeText(this, "点击第一个按钮", Toast.LENGTH_SHORT).show();
                break;
            case R.id.button2:
                Toast.makeText(this, "点击第二个按钮", Toast.LENGTH_SHORT).show();
                break;
            default:
                break;
        }
    }

    @ViewClick(R.id.button3)
    public void onClick3(View view) {
        Toast.makeText(this, "点击第三个按钮", Toast.LENGTH_SHORT).show();
    }

    @ViewClick(R.id.button4)
    public void onClick4(View view) {
        Toast.makeText(this, "点击第四个按钮", Toast.LENGTH_SHORT).show();
    }

    @ViewClick(R.id.button5)
    public void onClick5(View view) {
        Toast.makeText(this, "点击第五个按钮", Toast.LENGTH_SHORT).show();
    }

    @ViewClick(R.id.button6)
    public void onClick6(View view) {
        Toast.makeText(this, "点击第六个按钮", Toast.LENGTH_SHORT).show();
    }
}
