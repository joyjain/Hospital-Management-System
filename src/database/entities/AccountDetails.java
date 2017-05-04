/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package database.entities;

import java.io.Serializable;
import javax.persistence.Basic;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author Joy Jain
 */
@Entity
@Table(name = "account_details", uniqueConstraints = {
    @UniqueConstraint(columnNames = {"id"})
    , @UniqueConstraint(columnNames = {"username"})})
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "AccountDetails.findAll", query = "SELECT a FROM AccountDetails a")
    , @NamedQuery(name = "AccountDetails.findById", query = "SELECT a FROM AccountDetails a WHERE a.id = :id")
    , @NamedQuery(name = "AccountDetails.findByUsername", query = "SELECT a FROM AccountDetails a WHERE a.username = :username")
    , @NamedQuery(name = "AccountDetails.findByPassword", query = "SELECT a FROM AccountDetails a WHERE a.password = :password")
    , @NamedQuery(name = "AccountDetails.findByType", query = "SELECT a FROM AccountDetails a WHERE a.type = :type")})
public class AccountDetails implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(nullable = false)
    private Integer id;
    @Basic(optional = false)
    @Column(nullable = false, length = 26)
    private String username;
    @Basic(optional = false)
    @Column(nullable = false, length = 45)
    private String password;
    @Basic(optional = false)
    @Column(nullable = false, length = 20)
    private String type;
    @OneToOne(cascade = CascadeType.ALL, mappedBy = "accountDetails")
    private DoctorDetails doctorDetails;
    @OneToOne(cascade = CascadeType.ALL, mappedBy = "accountDetails")
    private PatientDetails patientDetails;

    public AccountDetails() {
    }

    public AccountDetails(Integer id) {
        this.id = id;
    }

    public AccountDetails(Integer id, String username, String password, String type) {
        this.id = id;
        this.username = username;
        this.password = password;
        this.type = type;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public DoctorDetails getDoctorDetails() {
        return doctorDetails;
    }

    public void setDoctorDetails(DoctorDetails doctorDetails) {
        this.doctorDetails = doctorDetails;
    }

    public PatientDetails getPatientDetails() {
        return patientDetails;
    }

    public void setPatientDetails(PatientDetails patientDetails) {
        this.patientDetails = patientDetails;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (id != null ? id.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof AccountDetails)) {
            return false;
        }
        AccountDetails other = (AccountDetails) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "database.entities.AccountDetails[ id=" + id + " ]";
    }
    
}