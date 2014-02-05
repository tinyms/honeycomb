package com.tinyms.entity;

import javax.persistence.*;

/**
 * Created by tinyms on 14-2-5.
 */
@Entity
public class Call {
    private int id;
    private int companyId;
    private int callNo;
    private String category;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    @Basic
    @Column(name = "CompanyId")
    public int getCompanyId() {
        return companyId;
    }

    public void setCompanyId(int companyId) {
        this.companyId = companyId;
    }

    @Basic
    @Column(name = "CallNo")
    public int getCallNo() {
        return callNo;
    }

    public void setCallNo(int callNo) {
        this.callNo = callNo;
    }

    @Basic
    @Column(name = "Category")
    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Call call = (Call) o;

        if (callNo != call.callNo) return false;
        if (companyId != call.companyId) return false;
        if (id != call.id) return false;
        if (category != null ? !category.equals(call.category) : call.category != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = id;
        result = 31 * result + companyId;
        result = 31 * result + callNo;
        result = 31 * result + (category != null ? category.hashCode() : 0);
        return result;
    }
}
