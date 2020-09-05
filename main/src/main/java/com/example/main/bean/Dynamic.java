package com.example.main.bean;

import android.widget.TextView;

/**
 * Created by czy on 2020/9/5 17:06.
 * describe: 表单json
 */
public class Dynamic {
    private String id;
    private TextView view;

    public Dynamic(String id,TextView view){
        this.id = id;
        this.view = view;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public TextView getView() {
        return view;
    }

    public void setView(TextView view) {
        this.view = view;
    }
}
