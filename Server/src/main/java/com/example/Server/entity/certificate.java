package com.example.Server.entity;
import com.example.Server.entity.RevokedCerts;
import jakarta.persistence.*;
import java.time.LocalDateTime;
import java.util.Set;
@Entity
@Table(name = "issuedcert", schema = "certmanager")
public class certificate {



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
    @Column(name="san")
    private String san;
    @Column(name="expiration_date_time")
    private String validity;
    @OneToMany(mappedBy = "certificate")
    private Set<RevokedCerts> revokedCerts;

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

    public String getSan() {
        return san;
    }

    public void setSan(String san) {
        this.san = san;
    }

    public String getValidity() {
        return validity;
    }

    public void setValidity(String validity) {
        this.validity = validity;
    }
}

