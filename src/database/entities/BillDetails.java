/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package database.entities;

import java.io.Serializable;
import java.util.Date;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.UniqueConstraint;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author Joy Jain
 */
@Entity
@Table(name = "bill_details", uniqueConstraints = {
    @UniqueConstraint(columnNames = {"patient_id"})
    , @UniqueConstraint(columnNames = {"bill_no"})})
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "BillDetails.findAll", query = "SELECT b FROM BillDetails b")
    , @NamedQuery(name = "BillDetails.findByBillNo", query = "SELECT b FROM BillDetails b WHERE b.billNo = :billNo")
    , @NamedQuery(name = "BillDetails.findByDate", query = "SELECT b FROM BillDetails b WHERE b.date = :date")
    , @NamedQuery(name = "BillDetails.findByDoctorCharge", query = "SELECT b FROM BillDetails b WHERE b.doctorCharge = :doctorCharge")
    , @NamedQuery(name = "BillDetails.findByMedicineCharge", query = "SELECT b FROM BillDetails b WHERE b.medicineCharge = :medicineCharge")
    , @NamedQuery(name = "BillDetails.findByRoomCharge", query = "SELECT b FROM BillDetails b WHERE b.roomCharge = :roomCharge")
    , @NamedQuery(name = "BillDetails.findByOperationCharge", query = "SELECT b FROM BillDetails b WHERE b.operationCharge = :operationCharge")
    , @NamedQuery(name = "BillDetails.findByNoOfDays", query = "SELECT b FROM BillDetails b WHERE b.noOfDays = :noOfDays")
    , @NamedQuery(name = "BillDetails.findByOtherCharges", query = "SELECT b FROM BillDetails b WHERE b.otherCharges = :otherCharges")
    , @NamedQuery(name = "BillDetails.findByTotalAmount", query = "SELECT b FROM BillDetails b WHERE b.totalAmount = :totalAmount")})
public class BillDetails implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @Column(name = "bill_no", nullable = false, length = 45)
    private String billNo;
    @Basic(optional = false)
    @Column(nullable = false)
    @Temporal(TemporalType.TIMESTAMP)
    private Date date;
    @Basic(optional = false)
    @Column(name = "doctor_charge", nullable = false)
    private int doctorCharge;
    @Basic(optional = false)
    @Column(name = "medicine_charge", nullable = false)
    private int medicineCharge;
    @Basic(optional = false)
    @Column(name = "room_charge", nullable = false)
    private int roomCharge;
    @Column(name = "operation_charge")
    private Integer operationCharge;
    @Column(name = "no_of_days")
    private Integer noOfDays;
    @Column(name = "other_charges")
    private Integer otherCharges;
    @Basic(optional = false)
    @Column(name = "total_amount", nullable = false)
    private int totalAmount;
    @JoinColumn(name = "patient_id", referencedColumnName = "patient_id", nullable = false)
    @OneToOne(optional = false)
    private PatientDetails patientId;

    public BillDetails() {
    }

    public BillDetails(String billNo) {
        this.billNo = billNo;
    }

    public BillDetails(String billNo, Date date, int doctorCharge, int medicineCharge, int roomCharge, int totalAmount) {
        this.billNo = billNo;
        this.date = date;
        this.doctorCharge = doctorCharge;
        this.medicineCharge = medicineCharge;
        this.roomCharge = roomCharge;
        this.totalAmount = totalAmount;
    }

    public String getBillNo() {
        return billNo;
    }

    public void setBillNo(String billNo) {
        this.billNo = billNo;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public int getDoctorCharge() {
        return doctorCharge;
    }

    public void setDoctorCharge(int doctorCharge) {
        this.doctorCharge = doctorCharge;
    }

    public int getMedicineCharge() {
        return medicineCharge;
    }

    public void setMedicineCharge(int medicineCharge) {
        this.medicineCharge = medicineCharge;
    }

    public int getRoomCharge() {
        return roomCharge;
    }

    public void setRoomCharge(int roomCharge) {
        this.roomCharge = roomCharge;
    }

    public Integer getOperationCharge() {
        return operationCharge;
    }

    public void setOperationCharge(Integer operationCharge) {
        this.operationCharge = operationCharge;
    }

    public Integer getNoOfDays() {
        return noOfDays;
    }

    public void setNoOfDays(Integer noOfDays) {
        this.noOfDays = noOfDays;
    }

    public Integer getOtherCharges() {
        return otherCharges;
    }

    public void setOtherCharges(Integer otherCharges) {
        this.otherCharges = otherCharges;
    }

    public int getTotalAmount() {
        return totalAmount;
    }

    public void setTotalAmount(int totalAmount) {
        this.totalAmount = totalAmount;
    }

    public PatientDetails getPatientId() {
        return patientId;
    }

    public void setPatientId(PatientDetails patientId) {
        this.patientId = patientId;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (billNo != null ? billNo.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof BillDetails)) {
            return false;
        }
        BillDetails other = (BillDetails) object;
        if ((this.billNo == null && other.billNo != null) || (this.billNo != null && !this.billNo.equals(other.billNo))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "database.entities.BillDetails[ billNo=" + billNo + " ]";
    }
    
}
