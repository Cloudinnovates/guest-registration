package com.exadel.guestregistration.models;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;


@Document(collection = "office_register")
public class Office {
    @Id
    private String id;
    @NotNull
    private String name;
    @NotNull
    private String address;
    @NotNull
    @Size(min = 1, max = 20)
    private String phone;
    @NotNull
    private String email;
    private String type;
    @NotNull
    private String manager_name;
    private String manager_surname;
    @NotNull
    private String manager_email;

    public Office(){
        super();
    }

    public Office(String id, String name, String address, String phone, String email, String type,
                  String manager_name, String manager_surname, String manager_email) {
        this.id = id;
        this.name = name;
        this.address = address;
        this.phone = phone;
        this.email = email;
        this.type = type;
        this.manager_name = manager_name;
        this.manager_surname = manager_surname;
        this.manager_email = manager_email;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getManager_name() {
        return manager_name;
    }

    public void setManager_name(String manager_name) {
        this.manager_name = manager_name;
    }

    public String getManager_surname() {
        return manager_surname;
    }

    public void setManager_surname(String manager_surname) {
        this.manager_surname = manager_surname;
    }

    public String getManager_email() {
        return manager_email;
    }

    public void setManager_email(String manager_email) {
        this.manager_email = manager_email;
    }

    @Override
    public String toString() {
        return "Office{" +
                "id='" + id + '\'' +
                ", Name='" + name + '\'' +
                ", address='" + address + '\'' +
                ", phone='" + phone + '\'' +
                ", email='" + email + '\'' +
                ", type='" + type + '\'' +
                ", Manager name='" + manager_name + '\'' +
                ", Manager surname='" + manager_surname + '\'' +
                ", Manager email='" + manager_email + '\'' +
                '}';
    }

}
