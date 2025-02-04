package Coursework;

public class SmartRoom {
    // initialises all variables that will need to be declared
    private String roomName;
    private int roomID;

    public SmartRoom(String roomName, int roomID) {// declares all initialised variables
        this.roomName = roomName;
        this.roomID = roomID;
    }

    public String getRoomName() {// retrieves roomName

        return roomName;
    }

    public void setRoomName(String roomName) {// declares roomName

        this.roomName = roomName;
    }

    public int getRoomID() {// retrieves roomID

        return roomID;
    }

    public void setRoomID(int roomID) {// declares roomID

        this.roomID = roomID;
    }

    @Override
    public String toString() {// outputs data on rooms that can contain plugs

        return "\n" + roomID + " - " +
                roomName;
    }
}
