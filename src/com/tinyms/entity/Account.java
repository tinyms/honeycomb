package com.tinyms.entity;

import javax.persistence.*;
import java.sql.Timestamp;

/**
 * Created by tinyms on 13-12-27.
 */
@Entity
public class Account {
    private long id;
    private String loginId;
    private String loginPwd;
    private String name;
    private String email;
    private Timestamp createTime;
    private Timestamp logonTime;
    private int status;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    @Basic
    @Column(name = "LoginID")
    public String getLoginId() {
        return loginId;
    }

    public void setLoginId(String loginId) {
        this.loginId = loginId;
    }

    @Basic
    @Column(name = "LoginPwd")
    public String getLoginPwd() {
        return loginPwd;
    }

    public void setLoginPwd(String loginPwd) {
        this.loginPwd = loginPwd;
    }

    @Basic
    @Column(name = "Name")
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Basic
    @Column(name = "Email")
    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    @Basic
    @Column(name = "CreateTime")
    public Timestamp getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Timestamp createTime) {
        this.createTime = createTime;
    }

    @Basic
    @Column(name = "LogonTime")
    public Timestamp getLogonTime() {
        return logonTime;
    }

    public void setLogonTime(Timestamp logonTime) {
        this.logonTime = logonTime;
    }

    @Basic
    @Column(name = "Status")
    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Account account = (Account) o;

        if (id != account.id) return false;
        if (status != account.status) return false;
        if (createTime != null ? !createTime.equals(account.createTime) : account.createTime != null) return false;
        if (email != null ? !email.equals(account.email) : account.email != null) return false;
        if (loginId != null ? !loginId.equals(account.loginId) : account.loginId != null) return false;
        if (loginPwd != null ? !loginPwd.equals(account.loginPwd) : account.loginPwd != null) return false;
        if (logonTime != null ? !logonTime.equals(account.logonTime) : account.logonTime != null) return false;
        if (name != null ? !name.equals(account.name) : account.name != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = (int) (id ^ (id >>> 32));
        result = 31 * result + (loginId != null ? loginId.hashCode() : 0);
        result = 31 * result + (loginPwd != null ? loginPwd.hashCode() : 0);
        result = 31 * result + (name != null ? name.hashCode() : 0);
        result = 31 * result + (email != null ? email.hashCode() : 0);
        result = 31 * result + (createTime != null ? createTime.hashCode() : 0);
        result = 31 * result + (logonTime != null ? logonTime.hashCode() : 0);
        result = 31 * result + status;
        return result;
    }
}
