package com.example.main.bean;

import android.widget.TextView;

/**
 * Created by czy on 2020/9/5 17:06.
 * describe: 表单json
 */
public class Dynamic {
    private TextView view;
    private TextView labelView;
    private Control control;

    public Dynamic(Control control,TextView labelView,TextView view){
        this.control = control;
        this.view = view;
        this.labelView = labelView;
    }


    public TextView getView() {
        return view;
    }

    public void setView(TextView view) {
        this.view = view;
    }


    public Control getControl() {
        return control;
    }

    public void setControl(Control control) {
        this.control = control;
    }

    public TextView getLabelView() {
        return labelView;
    }

    public void setLabelView(TextView labelView) {
        this.labelView = labelView;
    }
}
