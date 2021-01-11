package com.appdsn.fastdemo.bean;


import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Generated;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.Property;
import org.greenrobot.greendao.annotation.Transient;

@Entity
public class User {
    @Id
    private Long id;
    @Property(nameInDb = "sex")
    private Integer memberSex;//性别
    private String nickname;//昵称
    private String phone;//手机号

    @Transient
    private String memberDetailAddr;//用户的详细地址

    @Generated(hash = 84337579)
    public User(Long id, Integer memberSex, String nickname, String phone) {
        this.id = id;
        this.memberSex = memberSex;
        this.nickname = nickname;
        this.phone = phone;
    }

    @Generated(hash = 586692638)
    public User() {
    }

    public Long getId() {
        return this.id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Integer getMemberSex() {
        return this.memberSex;
    }

    public void setMemberSex(Integer memberSex) {
        this.memberSex = memberSex;
    }

    public String getNickname() {
        return this.nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public String getPhone() {
        return this.phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }
}