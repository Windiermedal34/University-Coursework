/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package adam.project.ents;

import java.io.Serializable;
import javax.persistence.Embeddable;

/**
 *
 * @author adamt
 */
@Embeddable
public class TimeData implements Serializable {
    private int seconds;

    public int getSeconds() {
        return seconds;
    }

    public void setSeconds(int seconds) {
        this.seconds = seconds;
    }
    
    public String toString(){
        return Integer.toString(seconds);
    }
}
