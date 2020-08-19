package com.example.main.bean;

/**
 * Created by czy on 2020/8/18 23:06.
 * describe:
 */
public class ImageList {

    private String uid;
    private String url;

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    /**
     * Created by czy on 2020/8/9 20:13.
     * describe:
     */
    public static class People {

        private String id;
        private String name;
        private String tel;
        private String address;


        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getTel() {
            return tel;
        }

        public void setTel(String tel) {
            this.tel = tel;
        }

        public String getAddress() {
            return address;
        }

        public void setAddress(String address) {
            this.address = address;
        }

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }
    }
}
