package com.exadel.guestregistration.models;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.util.Date;

@Document(collection = "agent")
public class Agent {
    @Id
    private String id;
    @NotBlank
    private String name;
    @NotBlank
    private String surname;
    @Size(min = 1, max = 20)
    private String phone;
    private String address;
    private String gender;
    private Date date_of_birth;
    private String work_place;

    public Agent() { super(); }

    public Agent(String id, @NotBlank String name, @NotBlank String surname, @Size(min = 1, max = 20)
                 String phone, String address, String gender, Date date_of_birth, String work_place) {
        this.id = id;
        this.name = name;
        this.surname = surname;
        this.phone = phone;
        this.address = address;
        this.gender = gender;
        this.date_of_birth = date_of_birth;
        this.work_place = work_place;
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

    public String getSurname() {
        return surname;
    }

    public void setSurname(String surname) {
        this.surname = surname;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public Date getDate_of_birth() {
        return date_of_birth;
    }

    public void setDate_of_birth(Date date_of_birth) {
        this.date_of_birth = date_of_birth;
    }

    public String getWork_place() {
        return work_place;
    }

    public void setWork_place(String work_place) {
        this.work_place = work_place;
    }

    @Override
    public String toString() {
        return "Agent{" +
                "id='" + id + '\'' +
                ", name='" + name + '\'' +
                ", surname='" + surname + '\'' +
                ", phone='" + phone + '\'' +
                ", address='" + address + '\'' +
                ", gender='" + gender + '\'' +
                ", date_of_birth=" + date_of_birth +
                ", work_place='" + work_place + '\'' +
                '}';
    }
}
