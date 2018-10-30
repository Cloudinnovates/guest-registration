package com.exadel.guestregistration.models; 

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.validation.constraints.*;
import java.util.Date;

@Document(collection = "cards")
public class Card {
    @Id   
    private String id;
    
    @Indexed(unique=true)
    @Max(9999)
    @Min(0)
    private int cardNo;

    @NotNull
    private Date validFrom;

    @NotNull
    private Date validThru;

    @NotBlank
    private String officeId;

    @NotBlank
    private String cardType;

    @NotNull
    private boolean cardAvailable = true;   

    public Card() {
        super();
    }


    /**
     *
     * @param id
     * @param cardNo
     * @param validFrom
     * @param validThru
     * @param officeId
     * @param cardType
     */
    public Card(String id, @Max(9999) @Min(0) int cardNo, @NotEmpty @NotNull Date validFrom, @NotEmpty @NotNull Date validThru, @NotBlank String officeId, @NotBlank String cardType) {
        this.id = id;
        this.cardNo = cardNo;
        this.validFrom = validFrom;
        this.validThru = validThru;
        this.officeId = officeId;
        this.cardType = cardType;
    }

    public Card(String id, @Max(9999) @Min(0) int cardNo, @NotEmpty @NotNull Date validFrom, @NotEmpty @NotNull Date validThru, @NotBlank String officeId, @NotBlank String cardType, @NotNull boolean cardAvailable) {
        this.id = id;
        this.cardNo = cardNo;
        this.validFrom = validFrom;
        this.validThru = validThru;
        this.officeId = officeId;
        this.cardType = cardType;
        this.cardAvailable = cardAvailable; 
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public int getCardNo() {
        return cardNo;
    }

    public void setCardNo(int cardNo) {
        this.cardNo = cardNo;
    }

    public Date getValidFrom() {
        return validFrom;
    }

    public void setValidFrom(Date validFrom) {
        this.validFrom = validFrom;
    }

    public Date getValidThru() {
        return validThru;
    }

    public void setValidThru(Date validThru) {
        this.validThru = validThru;
    }

    public String getOfficeId() {
        return officeId;
    }

    public void setOfficeId(String officeId) {
        this.officeId = officeId;
    }

    public String getCardType() {
        return cardType;
    }

    public void setCardType(String cardType) {
        this.cardType = cardType;
    }

    public boolean isCardAvailable() {
        return cardAvailable;
    }

    public void setCardAvailable(boolean cardAvailable) {
        this.cardAvailable = cardAvailable;
    }

    @Override
    public String toString() {
        return "{" +
                "\"id\"=\"" + id + '\"' +
                ", \"cardNo\"=" + cardNo +
                ", \"validFrom\"=" + validFrom.getTime() +
                ", \"validThru\"=" + validThru.getTime() +
                ", \"officeId\"=\"" + officeId + '\"' +
                ", \"cardType\"=\"" + cardType + '\"' +
                ", \"cardAvailable\"=" + cardAvailable +
                '}';
    }
}
