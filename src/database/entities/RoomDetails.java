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
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author Sanjay Kumar Jain
 */
@Entity
@Table(name = "room_details", uniqueConstraints = {
    @UniqueConstraint(columnNames = {"room_no"})})
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "RoomDetails.findAll", query = "SELECT r FROM RoomDetails r")
    , @NamedQuery(name = "RoomDetails.findByRoomNo", query = "SELECT r FROM RoomDetails r WHERE r.roomNo = :roomNo")
    , @NamedQuery(name = "RoomDetails.findByRoomType", query = "SELECT r FROM RoomDetails r WHERE r.roomType = :roomType")
    , @NamedQuery(name = "RoomDetails.findByStatus", query = "SELECT r FROM RoomDetails r WHERE r.status = :status")})
public class RoomDetails implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @Column(name = "room_no", nullable = false, length = 45)
    private String roomNo;
    @Basic(optional = false)
    @Column(name = "room_type", nullable = false, length = 15)
    private String roomType;
    @Basic(optional = false)
    @Column(nullable = false, length = 15)
    private String status;

    public RoomDetails() {
    }

    public RoomDetails(String roomNo) {
        this.roomNo = roomNo;
    }

    public RoomDetails(String roomNo, String roomType, String status) {
        this.roomNo = roomNo;
        this.roomType = roomType;
        this.status = status;
    }

    public String getRoomNo() {
        return roomNo;
    }

    public void setRoomNo(String roomNo) {
        this.roomNo = roomNo;
    }

    public String getRoomType() {
        return roomType;
    }

    public void setRoomType(String roomType) {
        this.roomType = roomType;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (roomNo != null ? roomNo.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof RoomDetails)) {
            return false;
        }
        RoomDetails other = (RoomDetails) object;
        if ((this.roomNo == null && other.roomNo != null) || (this.roomNo != null && !this.roomNo.equals(other.roomNo))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "database.entities.RoomDetails[ roomNo=" + roomNo + " ]";
    }
    
}
