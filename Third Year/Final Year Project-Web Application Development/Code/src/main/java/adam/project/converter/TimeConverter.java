/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package adam.project.converter;

import adam.project.ents.TimeData;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.FacesConverter;

/**
 *
 * @author adamt
 */
@FacesConverter("adam.project.converter.TimeConverter")
public class TimeConverter implements Converter {

    @Override
    public Object getAsObject(FacesContext fc, UIComponent uic, String time) {
        String[] splitTime;
        if (time.contains("'")) {
            splitTime = time.split("'");
        } else if (time.contains(".")) {
            splitTime = time.split(".");
        } else if (time.contains(";")) {
            splitTime = time.split(";");
        } else if (time.contains(":")) {
            splitTime = time.split(":");
        } else if (time.contains("-")) {
            splitTime = time.split("-");
        } else if (time.contains("_")) {
            splitTime = time.split("_");
        } else{
            splitTime = time.split(" ");
        }
        String min = splitTime[0];
        String secs = splitTime[1];
        int mins = Integer.parseInt(min);
        int sec = Integer.parseInt(secs);
        int minsToSecs = mins * 60;
        sec = sec + minsToSecs;
        TimeData data = new TimeData();
        data.setSeconds(sec);
        return data;
    }

    @Override
    public String getAsString(FacesContext fc, UIComponent uic, Object t) {
        TimeData data = (TimeData) t;
        String seconds = data.toString();
        int secs = Integer.parseInt(seconds);
        int remainingSeconds = secs % 60;
        secs = secs - remainingSeconds;
        int minutes = secs / 60;
        String mins = Integer.toString(minutes);
        String secsRemaining = Integer.toString(remainingSeconds);
        String time = mins + "'" + secsRemaining;
        return time;
    }
}
