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
@Table(name = "patient_details", uniqueConstraints = {
    @UniqueConstraint(columnNames = {"patient_id"})
    , @UniqueConstraint(columnNames = {"username"})})
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "PatientDetails.findAll", query = "SELECT p FROM PatientDetails p")
    , @NamedQuery(name = "PatientDetails.findByPatientId", query = "SELECT p FROM PatientDetails p WHERE p.patientId = :patientId")
    , @NamedQuery(name = "PatientDetails.findByName", query = "SELECT p FROM PatientDetails p WHERE p.name = :name")
    , @NamedQuery(name = "PatientDetails.findByAge", query = "SELECT p FROM PatientDetails p WHERE p.age = :age")
    , @NamedQuery(name = "PatientDetails.findByWeight", query = "SELECT p FROM PatientDetails p WHERE p.weight = :weight")
    , @NamedQuery(name = "PatientDetails.findByGender", query = "SELECT p FROM PatientDetails p WHERE p.gender = :gender")
    , @NamedQuery(name = "PatientDetails.findByAddress", query = "SELECT p FROM PatientDetails p WHERE p.address = :address")
    , @NamedQuery(name = "PatientDetails.findByContactNo", query = "SELECT p FROM PatientDetails p WHERE p.contactNo = :contactNo")
    , @NamedQuery(name = "PatientDetails.findByDoctorId", query = "SELECT p FROM PatientDetails p WHERE p.doctorId = :doctorId")
    , @NamedQuery(name = "PatientDetails.findByUsername", query = "SELECT p FROM PatientDetails p WHERE p.username = :username")
    , @NamedQuery(name = "PatientDetails.findByPassword", query = "SELECT p FROM PatientDetails p WHERE p.password = :password")
    , @NamedQuery(name = "PatientDetails.findByDisease", query = "SELECT p FROM PatientDetails p WHERE p.disease = :disease")
    , @NamedQuery(name = "PatientDetails.findByRoomNo", query = "SELECT p FROM PatientDetails p WHERE p.roomNo = :roomNo")})
public class PatientDetails implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @Column(name = "patient_id", nullable = false)
    private Integer patientId;
    @Basic(optional = false)
    @Column(nullable = false, length = 45)
    private String name;
    @Basic(optional = false)
    @Column(nullable = false)
    private int age;
    @Basic(optional = false)
    @Column(nullable = false)
    private int weight;
    @Basic(optional = false)
    @Column(nullable = false, length = 15)
    private String gender;
    @Basic(optional = false)
    @Column(nullable = false, length = 135)
    private String address;
    @Basic(optional = false)
    @Column(name = "contact_no", nullable = false)
    private int contactNo;
    @Basic(optional = false)
    @Column(name = "doctor_id", nullable = false)
    private int doctorId;
    @Basic(optional = false)
    @Column(nullable = false, length = 26)
    private String username;
    @Basic(optional = false)
    @Column(nullable = false, length = 45)
    private String password;
    @Basic(optional = false)
    @Column(nullable = false, length = 45)
    private String disease;
    @Basic(optional = false)
    @Column(name = "room_no", nullable = false, length = 45)
    private String roomNo;
    @OneToOne(cascade = CascadeType.ALL, mappedBy = "patientId")
    private BillDetails billDetails;
    @JoinColumn(name = "patient_id", referencedColumnName = "id", nullable = false, insertable = false, updatable = false)
    @OneToOne(optional = false)
    private AccountDetails accountDetails;

    public PatientDetails() {
    }

    public PatientDetails(Integer patientId) {
        this.patientId = patientId;
    }

    public PatientDetails(Integer patientId, String name, int age, int weight, String gender, String address, int contactNo, int doctorId, String username, String password, String disease, String roomNo) {
        this.patientId = patientId;
        this.name = name;
        this.age = age;
        this.weight = weight;
        this.gender = gender;
        this.address = address;
        this.contactNo = contactNo;
        this.doctorId = doctorId;
        this.username = username;
        this.password = password;
        this.disease = disease;
        this.roomNo = roomNo;
    }

    public Integer getPatientId() {
        return patientId;
    }

    public void setPatientId(Integer patientId) {
        this.patientId = patientId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public int getWeight() {
        return weight;
    }

    public void setWeight(int weight) {
        this.weight = weight;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public int getContactNo() {
        return contactNo;
    }

    public void setContactNo(int contactNo) {
        this.contactNo = contactNo;
    }

    public int getDoctorId() {
        return doctorId;
    }

    public void setDoctorId(int doctorId) {
        this.doctorId = doctorId;
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

    public String getDisease() {
        return disease;
    }

    public void setDisease(String disease) {
        this.disease = disease;
    }

    public String getRoomNo() {
        return roomNo;
    }

    public void setRoomNo(String roomNo) {
        this.roomNo = roomNo;
    }

    public BillDetails getBillDetails() {
        return billDetails;
    }

    public void setBillDetails(BillDetails billDetails) {
        this.billDetails = billDetails;
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
        hash += (patientId != null ? patientId.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof PatientDetails)) {
            return false;
        }
        PatientDetails other = (PatientDetails) object;
        if ((this.patientId == null && other.patientId != null) || (this.patientId != null && !this.patientId.equals(other.patientId))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "database.entities.PatientDetails[ patientId=" + patientId + " ]";
    }
    
}
