/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package adam.project.ents;

import java.io.Serializable;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

/**
 *
 * @author adamt
 */
@Entity(name = "FoulList")
public class FoulCodes implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }
    
    private String foulCode;

    public String getFoulCode() {
        return foulCode;
    }

    public void setFoulCode(String foulCode) {
        this.foulCode = foulCode;
    }
    
    private String foulName;

    public String getFoulName() {
        return foulName;
    }

    public void setFoulName(String foulName) {
        this.foulName = foulName;
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
        if (!(object instanceof FoulCodes)) {
            return false;
        }
        FoulCodes other = (FoulCodes) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "tutorial.learnprogramming.form4.ent.NewFoulCodes[ id=" + id + " ]";
    }
    
}
