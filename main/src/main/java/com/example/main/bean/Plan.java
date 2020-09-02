package com.example.main.bean;

/**
 * Created by 陈泽宇 on 2020/9/2
 * Describe: 预案
 */
public class Plan {

    private String id;
    private String name;
    private String content;//预案内容
    private String categoryId;//事件类型ID
    private String createTime;//创建时间

    private boolean hidden = false;//是否局部隐藏 true 显示 false隐藏

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(String categoryId) {
        this.categoryId = categoryId;
    }

    public String getCreateTime() {
        return createTime;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }

    public boolean isHidden() {
        return hidden;
    }

    public void setHidden(boolean hidden) {
        this.hidden = hidden;
    }
}
