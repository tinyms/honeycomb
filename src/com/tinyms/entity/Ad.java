package com.tinyms.entity;

import javax.persistence.*;
import java.sql.Time;

/**
 * Created by tinyms on 14-2-5.
 */

@Entity
public class Ad {
    private int id;
    private Time validStartTime;
    private Time validEndTime;
    private String imgPath;
    private int playNum;
    private String kind;
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
    @Column(name = "ValidStartTime")
    public Time getValidStartTime() {
        return validStartTime;
    }

    public void setValidStartTime(Time validStartTime) {
        this.validStartTime = validStartTime;
    }

    @Basic
    @Column(name = "ValidEndTime")
    public Time getValidEndTime() {
        return validEndTime;
    }

    public void setValidEndTime(Time validEndTime) {
        this.validEndTime = validEndTime;
    }

    @Basic
    @Column(name = "ImgPath")
    public String getImgPath() {
        return imgPath;
    }

    public void setImgPath(String imgPath) {
        this.imgPath = imgPath;
    }

    @Basic
    @Column(name = "PlayNum")
    public int getPlayNum() {
        return playNum;
    }

    public void setPlayNum(int playNum) {
        this.playNum = playNum;
    }

    @Basic
    @Column(name = "Kind")
    public String getKind() {
        return kind;
    }

    public void setKind(String kind) {
        this.kind = kind;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Ad ad = (Ad) o;

        if (id != ad.id) return false;
        if (playNum != ad.playNum) return false;
        if (imgPath != null ? !imgPath.equals(ad.imgPath) : ad.imgPath != null) return false;
        if (kind != null ? !kind.equals(ad.kind) : ad.kind != null) return false;
        if (validEndTime != null ? !validEndTime.equals(ad.validEndTime) : ad.validEndTime != null) return false;
        if (validStartTime != null ? !validStartTime.equals(ad.validStartTime) : ad.validStartTime != null)
            return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = id;
        result = 31 * result + (validStartTime != null ? validStartTime.hashCode() : 0);
        result = 31 * result + (validEndTime != null ? validEndTime.hashCode() : 0);
        result = 31 * result + (imgPath != null ? imgPath.hashCode() : 0);
        result = 31 * result + playNum;
        result = 31 * result + (kind != null ? kind.hashCode() : 0);
        return result;
    }
}
