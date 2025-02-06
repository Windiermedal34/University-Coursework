/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package adam.project.ents;

import java.io.Serializable;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;

/**
 *
 * @author adamt
 */
@Entity(name="foul")
public class Foul implements Serializable{
    
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
    
    private String foulName;

    public String getFoulName() {
        return foulName;
    }

    public void setFoulName(String foulName) {
        this.foulName = foulName;
    }
    
    private String quarter;

    public String getQuarter() {
        return quarter;
    }

    public void setQuarter(String quarter) {
        this.quarter = quarter;
    }
    
    @Embedded
    private TimeData timeLeftInQtrAsSeconds;

    public TimeData getTime() {
        return timeLeftInQtrAsSeconds;
    }

    public void setTime(TimeData timeLeftInQtrAsSeconds) {
        this.timeLeftInQtrAsSeconds = timeLeftInQtrAsSeconds;
    }
    
    private String player;

    public String getPlayer() {
        return player;
    }

    public void setPlayer(String player) {
        this.player = player;
    }
    
    private String homeOrAway;

    public String getHomeOrAway() {
        return homeOrAway;
    }

    public void setHomeOrAway(String homeOrAway) {
        this.homeOrAway = homeOrAway;
    }
    
    private String officials;

    public String getOfficials() {
        return officials;
    }

    public void setOfficials(String official1) {
        this.officials = official1;
    }
    
    private String decision;

    public String getDecision() {
        return decision;
    }

    public void setDecision(String decision) {
        this.decision = decision;
    }
    
    private String position;

    public String getPosition() {
        return position;
    }

    public void setPosition(String position) {
        this.position = position;
    }
    
    @ManyToOne
    private Game game;

    public Game getGame() {
        return game;
    }

    public void setGame(Game game) {
        this.game = game;
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
        if (!(object instanceof Foul)) {
            return false;
        }
        Foul other = (Foul) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "adam.project.ent.Foul[ id=" + id + " ]";
    }
    
}
