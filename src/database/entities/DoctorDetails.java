/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package database.entities;

import java.io.Serializable;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author Sanjay Kumar Jain
 */
@Entity
@Table(name = "doctor_details", uniqueConstraints = {
    @UniqueConstraint(columnNames = {"doctor_id"})
    , @UniqueConstraint(columnNames = {"username"})})
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "DoctorDetails.findAll", query = "SELECT d FROM DoctorDetails d")
    , @NamedQuery(name = "DoctorDetails.findByDoctorId", query = "SELECT d FROM DoctorDetails d WHERE d.doctorId = :doctorId")
    , @NamedQuery(name = "DoctorDetails.findByName", query = "SELECT d FROM DoctorDetails d WHERE d.name = :name")
    , @NamedQuery(name = "DoctorDetails.findByAddress", query = "SELECT d FROM DoctorDetails d WHERE d.address = :address")
    , @NamedQuery(name = "DoctorDetails.findByDepartment", query = "SELECT d FROM DoctorDetails d WHERE d.department = :department")
    , @NamedQuery(name = "DoctorDetails.findByGender", query = "SELECT d FROM DoctorDetails d WHERE d.gender = :gender")
    , @NamedQuery(name = "DoctorDetails.findByUsername", query = "SELECT d FROM DoctorDetails d WHERE d.username = :username")
    , @NamedQuery(name = "DoctorDetails.findByPassword", query = "SELECT d FROM DoctorDetails d WHERE d.password = :password")})
public class DoctorDetails implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @Column(name = "doctor_id", nullable = false)
    private Integer doctorId;
    @Basic(optional = false)
    @Column(nullable = false, length = 45)
    private String name;
    @Basic(optional = false)
    @Column(nullable = false, length = 135)
    private String address;
    @Basic(optional = false)
    @Column(nullable = false, length = 15)
    private String department;
    @Basic(optional = false)
    @Column(nullable = false, length = 15)
    private String gender;
    @Basic(optional = false)
    @Column(nullable = false, length = 26)
    private String username;
    @Basic(optional = false)
    @Column(nullable = false, length = 45)
    private String password;
    @JoinColumn(name = "doctor_id", referencedColumnName = "id", nullable = false, insertable = false, updatable = false)
    @OneToOne(optional = false)
    private AccountDetails accountDetails;

    public DoctorDetails() {
    }

    public DoctorDetails(Integer doctorId) {
        this.doctorId = doctorId;
    }

    public DoctorDetails(Integer doctorId, String name, String address, String department, String gender, String username, String password) {
        this.doctorId = doctorId;
        this.name = name;
        this.address = address;
        this.department = department;
        this.gender = gender;
        this.username = username;
        this.password = password;
    }

    public Integer getDoctorId() {
        return doctorId;
    }

    public void setDoctorId(Integer doctorId) {
        this.doctorId = doctorId;
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

    public String getDepartment() {
        return department;
    }

    public void setDepartment(String department) {
        this.department = department;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
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

    public AccountDetails getAccountDetails() {
        return accountDetails;
    }

    public void setAccountDetails(AccountDetails accountDetails) {
        this.accountDetails = accountDetails;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (doctorId != null ? doctorId.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof DoctorDetails)) {
            return false;
        }
        DoctorDetails other = (DoctorDetails) object;
        if ((this.doctorId == null && other.doctorId != null) || (this.doctorId != null && !this.doctorId.equals(other.doctorId))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "database.entities.DoctorDetails[ doctorId=" + doctorId + " ]";
    }
    
}
