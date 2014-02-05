package com.tinyms.entity;

import javax.persistence.*;

/**
 * Created by tinyms on 14-2-5.
 */
@Entity
public class CallOrder {
    private int id;
    private int callNo;
    private String category;
    private Company company;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    @ManyToOne(cascade = {CascadeType.REMOVE, CascadeType.REFRESH}, optional = true)
    @JoinColumn(name = "CompanyId")
    public Company getCompany() {
        return company;
    }

    public void setCompany(Company company) {
        this.company = company;
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

        CallOrder callOrder = (CallOrder) o;

        if (callNo != callOrder.callNo) return false;
        if (id != callOrder.id) return false;
        if (category != null ? !category.equals(callOrder.category) : callOrder.category != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = id;
        result = 31 * result + callNo;
        result = 31 * result + (category != null ? category.hashCode() : 0);
        return result;
    }
}
