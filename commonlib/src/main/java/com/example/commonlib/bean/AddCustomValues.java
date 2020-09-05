package com.example.commonlib.bean;

/**
 * 新增用户返回参数
 * Created by liyanjun on 2017/6/22.
 */

public class AddCustomValues {
    protected String paramDesc;//参数描述
    protected String paramValue;//参数值
    protected String paramGroup;//排序

    public AddCustomValues() {
    }

    public AddCustomValues(String paramDesc, String paramValue) {
        this.paramDesc = paramDesc;
        this.paramValue = paramValue;
    }

    public String getParamDesc() {
        return paramDesc;
    }

    public void setParamDesc(String paramDesc) {
        this.paramDesc = paramDesc;
    }

    public String getParamValue() {
        return paramValue;
    }

    public void setParamValue(String paramValue) {
        this.paramValue = paramValue;
    }

    public String getParamGroup() {
        return paramGroup;
    }

    public void setParamGroup(String paramGroup) {
        this.paramGroup = paramGroup;
    }

    @Override
    public String toString() {
        return "参数值：" + paramValue + "\t\t参数描述：" + paramDesc;
    }

}
