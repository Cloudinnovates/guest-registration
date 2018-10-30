package com.exadel.guestregistration.models;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import org.springframework.data.annotation.Id;

import java.util.Date;

public class Employee {
    @Id
    private String id;
    @NotNull
    @NotBlank
    private String agentId;
    @NotBlank
    private String position;
    @NotNull
    private Date employment_date;
    @NotNull
    private Date card_issue_date;
    private Date dismissal_date;
    @NotNull
    @NotBlank
    private String cardId;
    @NotNull
    @NotBlank
    private String officeId;


    public Employee() {
        super();
    }

    public Employee(String id, @NotNull @NotBlank String agentId, @NotBlank String position,
                    @NotNull Date employment_date, @NotNull Date card_issue_date, Date dismissal_date,
                    @NotNull @NotBlank String cardId, @NotNull @NotBlank String officeId) {
        this.id = id;
        this.agentId = agentId;
        this.position = position;
        this.employment_date = employment_date;
        this.card_issue_date = card_issue_date;
        this.dismissal_date = dismissal_date;
        this.cardId = cardId;
        this.officeId = officeId;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getAgentId() {
        return agentId;
    }

    public void setAgentId(String agentId) {
        this.agentId = agentId;
    }

    public String getPosition() {
        return position;
    }

    public void setPosition(String position) {
        this.position = position;
    }

    public Date getEmployment_date() {
        return employment_date;
    }

    public void setEmployment_date(Date employment_date) {
        this.employment_date = employment_date;
    }

    public Date getCard_issue_date() {
        return card_issue_date;
    }

    public void setCard_issue_date(Date card_issue_date) {
        this.card_issue_date = card_issue_date;
    }

    public Date getDismissal_date() {
        return dismissal_date;
    }

    public void setDismissal_date(Date dismissal_date) {
        this.dismissal_date = dismissal_date;
    }

    public String getCardId() {
        return cardId;
    }

    public void setCardId(String cardId) {
        this.cardId = cardId;
    }

    public String getOfficeId() {
        return officeId;
    }

    public void setOfficeId(String officeId) {
        this.officeId = officeId;
    }

    @Override
    public String toString() {
        return "Employee{" +
                "id='" + id + '\'' +
                ", agentId='" + agentId + '\'' +
                ", position='" + position + '\'' +
                ", employment_date=" + employment_date +
                ", card_issue_date=" + card_issue_date +
                ", dismissal_date=" + dismissal_date +
                ", cardId='" + cardId + '\'' +
                ", officeId='" + officeId + '\'' +
                '}';
    }
}
