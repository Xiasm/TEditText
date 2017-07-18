package com.xsm.tedittext;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import com.xsm.library.TEditText;
import com.xsm.library.TObject;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private ArrayList<String> mStrings;
    private int i = 0;
    private TEditText mTEditText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mTEditText = (TEditText) findViewById(R.id.edittext);
        findViewById(R.id.topic_button).setOnClickListener(this);

        mStrings = new ArrayList<>();
        mStrings.add("鬼畜神曲");
        mStrings.add("洛阳的天气");
        mStrings.add("麦田收割机");
        mStrings.add("人工智能手机");
        mStrings.add("大汽车");
    }

    @Override
    public void onClick(View v) {
        //话题对象，可继承此类实现特定的业务逻辑
        TObject object = new TObject();
        //匹配规则
        object.setObjectRule("#");
        //话题内容
        object.setObjectText(mStrings.get(i));
        mTEditText.setObject(object);
        i++;
        if (i > 4)
            i = 0;
    }
}
