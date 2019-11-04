package com.example.linshuer.xiangshuo.bean;

/**
 * Created by Linshuer on 2018/6/10.
 */

public class User {

    private String username,password,headimage,nickname,sex,address,autopraph,theme;

    public User(String username,String password,String headimage,String nickname,String sex,String address,String autopraph,String theme){
        this.username =username;
        this.password=password;
        this.headimage=headimage;
        this.nickname =nickname;
        this.sex =sex;
        this.address=address;
        this.autopraph=autopraph;
        this.theme=theme;
    }
    public User(){
        this.username ="";
        this.password="";
        this.headimage="linshuer_headimage";
        this.nickname ="shuer";
        this.sex ="男";
        this.address="";
        this.autopraph="";
        this.theme="蓝色";
    }

    public String getTheme() {
        return theme;
    }

    public void setTheme(String theme) {
        this.theme = theme;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getHeadimage() {
        return headimage;
    }

    public void setHeadimage(String headimage) {
        this.headimage = headimage;
    }


    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public String getSex() {
        return sex;
    }

    public void setSex(String sex) {
        this.sex = sex;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getAutopraph() {
        return autopraph;
    }

    public void setAutopraph(String autopraph) {
        this.autopraph = autopraph;
    }

    @Override
    public String toString() {
        return "User [username=" + username + ", password=" + password + ", headimage="
                + headimage + ", nickname=" + nickname + ", sex=" + sex + ", address=" + address + ", autopraph="
                + autopraph + ", theme="+ theme+"]";
    }
}
