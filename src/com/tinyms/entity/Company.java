package com.tinyms.entity;

import javax.persistence.*;
import java.math.BigDecimal;
import java.sql.Timestamp;

/**
 * Created by tinyms on 14-2-5.
 */
@Entity
public class Company {
    private int id;
    private String name;
    private String email;
    private String phone;
    private String address;
    private String appKey;
    private String businessCategory;
    private String clientType;
    private BigDecimal cost;
    private Timestamp costTime;

    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
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
    @Column(name = "Phone")
    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    @Basic
    @Column(name = "Address")
    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    @Basic
    @Column(name = "AppKey")
    public String getAppKey() {
        return appKey;
    }

    public void setAppKey(String appKey) {
        this.appKey = appKey;
    }

    @Basic
    @Column(name = "BusinessCategory")
    public String getBusinessCategory() {
        return businessCategory;
    }

    public void setBusinessCategory(String businessCategory) {
        this.businessCategory = businessCategory;
    }

    @Basic
    @Column(name = "ClientType")
    public String getClientType() {
        return clientType;
    }

    public void setClientType(String clientType) {
        this.clientType = clientType;
    }

    @Basic
    @Column(name = "Cost")
    public BigDecimal getCost() {
        return cost;
    }

    public void setCost(BigDecimal cost) {
        this.cost = cost;
    }

    @Basic
    @Column(name = "CostTime")
    public Timestamp getCostTime() {
        return costTime;
    }

    public void setCostTime(Timestamp costTime) {
        this.costTime = costTime;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Company company = (Company) o;

        if (id != company.id) return false;
        if (address != null ? !address.equals(company.address) : company.address != null) return false;
        if (appKey != null ? !appKey.equals(company.appKey) : company.appKey != null) return false;
        if (businessCategory != null ? !businessCategory.equals(company.businessCategory) : company.businessCategory != null)
            return false;
        if (clientType != null ? !clientType.equals(company.clientType) : company.clientType != null) return false;
        if (cost != null ? !cost.equals(company.cost) : company.cost != null) return false;
        if (costTime != null ? !costTime.equals(company.costTime) : company.costTime != null) return false;
        if (email != null ? !email.equals(company.email) : company.email != null) return false;
        if (name != null ? !name.equals(company.name) : company.name != null) return false;
        if (phone != null ? !phone.equals(company.phone) : company.phone != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = id;
        result = 31 * result + (name != null ? name.hashCode() : 0);
        result = 31 * result + (email != null ? email.hashCode() : 0);
        result = 31 * result + (phone != null ? phone.hashCode() : 0);
        result = 31 * result + (address != null ? address.hashCode() : 0);
        result = 31 * result + (appKey != null ? appKey.hashCode() : 0);
        result = 31 * result + (businessCategory != null ? businessCategory.hashCode() : 0);
        result = 31 * result + (clientType != null ? clientType.hashCode() : 0);
        result = 31 * result + (cost != null ? cost.hashCode() : 0);
        result = 31 * result + (costTime != null ? costTime.hashCode() : 0);
        return result;
    }
}
