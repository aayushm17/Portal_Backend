package com.example.Server.entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "revokedcerts", schema = "certmanager")
public class RevokedCerts {

    @Id

    @Column(name="cn")
    private String cn;
    @Column(name="certname")
    private String certname;
    @Column(name="certtype")
    private String certtype;
    @Column(name="creation_date_time")
    private LocalDateTime dateissued;
    @Column(name="reqname")
    private String reqname;

    @ManyToOne
    @JoinColumn(name = "san", referencedColumnName = "san")
    private com.example.Server.entity.certificate certificate;

    @Column(name="expiration_date_time")
    private String validity;



    @Column(name="Reason")
    private String reason;
    // Foreign key relationship


    // Getters and setters
    public String getCn() {
        return cn;
    }

    public void setCn(String cn) {
        this.cn = cn;
    }

    public String getCertname() {
        return certname;
    }

    public void setCertname(String certname) {
        this.certname = certname;
    }

    public String getCerttype() {
        return certtype;
    }

    public void setCerttype(String certtype) {
        this.certtype = certtype;
    }

    public LocalDateTime getDateissued() {
        return dateissued;
    }

    public void setDateissued(LocalDateTime dateissued) {
        this.dateissued = dateissued;
    }

    public String getReqname() {
        return reqname;
    }

    public void setReqname(String reqname) {
        this.reqname = reqname;
    }

    public String getValidity() {
        return validity;
    }

    public void setValidity(String validity) {
        this.validity = validity;
    }
    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }

    public com.example.Server.entity.certificate getCertificate() {
        return certificate;
    }

    public void setCertificate(certificate certificate) {
        this.certificate = certificate;
    }
}
