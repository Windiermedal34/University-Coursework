package Coursework;

public class SmartPlug {
    // initialises all variables that will need to be declared
    private String deviceName;
    private int deviceID;
    private int plugRoomID;
    private int plugID;
    private boolean status;
    private String plugRoomName;

    public SmartPlug(String deviceName, int deviceID, int plugRoomID, int plugID, boolean status, String plugRoomName) {
        // declares all initialised variables
        this.deviceName = deviceName;
        this.deviceID = deviceID;
        this.plugRoomID = plugRoomID;
        this.plugID = plugID;
        this.status = status;
        this.plugRoomName = plugRoomName;
    }


    public String getPlugRoomName() {// retrieves plugRoomName

        return plugRoomName;
    }

    public void setPlugRoomName(String plugRoomName) {// declares plugRoomName

        this.plugRoomName = plugRoomName;
    }

    public int getDeviceID() {// retrieves deviceID

        return deviceID;
    }

    public void setDeviceID(int deviceID) {// declares deviceID

        this.deviceID = deviceID;
    }

    public String getDeviceName() {// retrieves deviceName

        return deviceName;
    }

    public void setDeviceName(String deviceName) {// declares deviceName

        this.deviceName = deviceName;
    }

    public int getPlugRoomID() {// retrieves plugRoomID

        return plugRoomID;
    }

    public void setPlugRoomID(int plugRoomID) {// declares plugRoomID

        this.plugRoomID = plugRoomID;
    }

    public int getPlugID() {// retrieves plugID

        return plugID;
    }

    public void setPlugID(int plugID) {// declares PlugID

        this.plugID = plugID;
    }

    public boolean getStatus() {// retrieves status

        return status;
    }

    public void setStatus(boolean status) {// declares status

        this.status = status;
    }

    @Override
    public String toString() {// outputs plug/device data

        return "device: " + deviceName +
                ", room: " + plugRoomName +
                ", plug: " + plugID +
                ", status: " + status + "\n";
    }
    public String toStringDashboard() {// outputs room data

        return "ROOM: " + plugRoomID +
                "\n SmartPlug |attached to: " + deviceName +
                "                 |room: " + plugRoomName +
                "|ID: " + plugID + "|status: " + status + "|\n";
    }
    public String toStringPlug(){// outputs SmartPlug data

        return "Smart Plug: " +
                "attached to: " + deviceName +
                " - " + "room: " + plugRoomName +
                " - " + "ID: " + plugID +
                " - " + "status: " + status + "\n";
    }
    public String toStringDevices(){// outputs device data

        return "\n" + deviceID +
                " - " + deviceName;
    }
    public String plugPrintout(int roomOfPlug){// initialises printOut, checks the roomID is correct, sets printOut as
        // equal to the output of toStringPlug before outputting printOut

        String printOut = "Printout";
        if (getPlugRoomID()==roomOfPlug){
            printOut = toStringPlug();
        }
        return printOut;
    }
    public void turnPlugsOff(){// sets status to false
        setStatus(false);
    }
    public void turnPlugsOn(){// sets status to true
        setStatus(true);
    }
    public void togglePlug(){// inverts the status

        setStatus(!getStatus());
    }
    public void changeAttachedDevice(int newDeviceID, String newDeviceName) {// sets new deviceName and deviceID for
        // that plug

        setDeviceName(newDeviceName);
        setDeviceID(newDeviceID);
    }
    public void changePlugRoom(String newPlugRoomName, int newPlugRoomID){// sets new plugRoomID and plugRoomName for
        // that plug

        setPlugRoomID(newPlugRoomID);
        setPlugRoomName(newPlugRoomName);
    }
}
