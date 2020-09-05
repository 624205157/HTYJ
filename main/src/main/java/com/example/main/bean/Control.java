package com.example.main.bean;

import java.util.List;

/**
 * Created by 陈泽宇 on 2020/9/3
 * Describe: json表单
 */
public class Control{
    private String id;
    private String label;//中文描述
    private String type;//控件类型 input、textarea、radio、checkbox、date、upload
    private boolean required;//是否必填
    private String placeholder;//输入框提示文字
    private List<Option> options;//候选项（仅radio、checkbox、select有）


    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public boolean isRequired() {
        return required;
    }

    public void setRequired(boolean required) {
        this.required = required;
    }

    public String getPlaceholder() {
        return placeholder;
    }

    public void setPlaceholder(String placeholder) {
        this.placeholder = placeholder;
    }

    public List<Option> getOptions() {
        return options;
    }

    public void setOptions(List<Option> options) {
        this.options = options;
    }
}
