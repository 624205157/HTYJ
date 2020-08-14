package com.example.main.activity;

import android.view.View;
import android.widget.TextView;

import com.example.commonlib.base.BaseActivity;
import com.example.main.R;

/**
 * Created by czy on 2020/8/13 20:04.
 * describe:
 */
public abstract class RightTitleActivity extends BaseActivity {

    protected void rightTitle(String text,RightClickListener listener){
        TextView textView = findViewById(R.id.right_tv);
        textView.setText(text);
        textView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.callBack();
            }
        });
    }


    protected interface RightClickListener{
        void callBack();
    }
}
