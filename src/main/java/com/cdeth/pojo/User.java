package com.cdeth.pojo;

/**
 * Created by tianlei on 2017/十月/18.
 */
public class User {

    private Integer id;
    private String mobile; //手机号
    private String password;

    //
    private String ethPassword; //eth 密码
    private String address; //eth 地址

    //
    private String createDatetime;
    private String updateDatetime;

    public Integer getId() {
        return id;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getEthPassword() {
        return ethPassword;
    }

    public void setEthPassword(String ethPassword) {
        this.ethPassword = ethPassword;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getCreateDatetime() {
        return createDatetime;
    }

    public void setCreateDatetime(String createDatetime) {
        this.createDatetime = createDatetime;
    }

    public String getUpdateDatetime() {
        return updateDatetime;
    }

    public void setUpdateDatetime(String updateDatetime) {
        this.updateDatetime = updateDatetime;
    }
}
