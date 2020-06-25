package com.grenader.example.pcfsimple.server.model;

import com.grenader.example.pcfsimple.server.dao.AttributeEncryptor;

import javax.persistence.Convert;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import java.util.Objects;

@Entity
public class CreditCard {
    @Id
    @GeneratedValue
    private Long id;

    @Convert(converter = AttributeEncryptor.class)
    private String name;

    @Convert(converter = AttributeEncryptor.class)
    private String number;

    @Convert(converter = AttributeEncryptor.class)
    private String expiration;

    private String cvv;

    public CreditCard() {
    }

    public CreditCard(String name,
                      String number,
                      String expiration,
                      String cvv) {
        this.name = name;
        this.number = number;
        this.expiration = expiration;
        this.cvv = cvv;
    }


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public String getExpiration() {
        return expiration;
    }

    public void setExpiration(String expire) {
        this.expiration = expire;
    }

    public String getCvv() {
        return cvv;
    }

    public void setCvv(String cvs) {
        this.cvv = cvs;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        CreditCard that = (CreditCard) o;
        return Objects.equals(name, that.name) &&
                Objects.equals(number, that.number) &&
                Objects.equals(expiration, that.expiration) &&
                Objects.equals(cvv, that.cvv);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, number, expiration, cvv);
    }

    @Override
    public String toString() {
        return "CreditCard{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", number='" + number + '\'' +
                ", expiration='" + expiration + '\'' +
                ", cvv='" + cvv + '\'' +
                '}';
    }
}
