package Coursework;

import java.util.Scanner;

public class SmartHome {
    // initialises the plugs array
    private SmartPlug[] plugs;
    // initialises the rooms array
    private SmartRoom[] rooms;
    // initialises plugCurrentIndex
    private int plugCurrentIndex;
    // initialises roomCurrentIndex
    private int roomCurrentIndex;
    // initialises currentIndex
    private int currentIndex;
    // creates deviceList array
    private String[] deviceList = {"Lamp", "TV", "Computer", "Phone Recharger", "Heater"};

    public SmartHome(int howManyPlugs, int howManyRooms){
        plugs = new SmartPlug[howManyPlugs];
        rooms = new SmartRoom[howManyRooms];
        currentIndex = 0;
        plugCurrentIndex = 0;
        roomCurrentIndex = 0;
    }

    public String newDevice(String newDeviceName){
        String[] updatedDevices = new String[getSizeDevices()+1];
        for(currentIndex = 0; currentIndex < getSizeDevices(); currentIndex++){
            updatedDevices[currentIndex] = deviceList[currentIndex];
        }
        updatedDevices[currentIndex] = newDeviceName;
        this.deviceList = updatedDevices;
        return("New device '" + newDeviceName + "' added");
    }
    public void newPlugs(int plugRoomID, boolean status, int plugID, int deviceID){// makes a temporary array with size
        // 1 larger than the original array, then loops through the arrays and copies data from the original array into
        // the temporary array, SmartRoom is called to call the room.getRoomID function to get the new plugRoomName,
        // then all of the new data is passed into SmartPlug, then this new data is added to the end of the temporary
        // array, the temporary array is declared as the original array to confirm the new data

        SmartPlug[] updatedPlugs = new SmartPlug[getSizePlug()+1];
        for (plugCurrentIndex = 0; plugCurrentIndex < getSizePlug(); plugCurrentIndex++){
            updatedPlugs[plugCurrentIndex] = plugs[plugCurrentIndex];
        }
        for (SmartRoom room : rooms) {
            if(room.getRoomID()==plugRoomID) {
                String plugRoomName = room.getRoomName();
                SmartPlug plug = new SmartPlug(deviceList[deviceID - 1], deviceID, plugRoomID, plugID, status, plugRoomName);
                updatedPlugs[plugCurrentIndex] = plug;
                this.plugs = updatedPlugs;
            }
        }
    }
    public void newRooms(String roomName){// makes a temporary array with size 1 larger than the original array, then
        // loops through the arrays and copies data from the original array into the temporary array,
        // roomCurrentIndex + 1 gets roomID, new SmartRoom object is built, then roomID and roomName is passed into
        // SmartPlug, this new data is added to the end of the temporary array, the temporary array is declared as the
        // original array to confirm the new data

        SmartRoom[] updatedRooms = new SmartRoom[getSizeRoom()+1];
        for (roomCurrentIndex = 0; roomCurrentIndex < getSizeRoom(); roomCurrentIndex++){
            updatedRooms[roomCurrentIndex] = rooms[roomCurrentIndex];
        }
        int roomID = roomCurrentIndex + 1;
        SmartRoom room = new SmartRoom(roomName, roomID);
        updatedRooms[roomCurrentIndex] = room;
        this.rooms = updatedRooms;
    }
    public int getSizePlug() {// returns the length of the plugs array
        return plugs.length;
    }
    public int getSizeRoom(){// returns the length of the rooms array
        return rooms.length;
    }
    public int getSizeDevices(){// returns the length of the deviceList array
        return deviceList.length;
    }
    public void addRoom(String roomName, int roomID){// builds SmartRoom object and passes roomID and roomName into the
        // SmartRoom class, adds rooms array data to SmartRoom class and finally increments the index

        SmartRoom room = new SmartRoom(roomName, roomID);
        rooms[roomCurrentIndex] = room;
        roomCurrentIndex++;
    }
    public void addPlug(int plugRoomID, boolean status, int plugID, int deviceID){// gets deviceName by using deviceID
        // as 1 above the index of the deviceList array, it gets plugRoomName by calling the getRoomName method from
        // SmartRoom it then builds the SmartPlug object and passes deviceName, deviceID, plugRoomID, plugID, status and
        // plugRoomName into the SmartPlug class, it then adds data from the plugs array to the SmartPlug class and
        // increments the index

        String deviceName = deviceList[deviceID-1];

        String plugRoomName;

        for (SmartRoom room : rooms) {
            if(room.getRoomID()==plugRoomID) {
                plugRoomName = room.getRoomName();
                SmartPlug plug = new SmartPlug(deviceName, deviceID, plugRoomID, plugID, status, plugRoomName);
                plugs[plugCurrentIndex] = plug;
                plugCurrentIndex++;
            }
        }
    }
    public String toString() {// initialises plugData, calls the SmartPlug class, adds the output of plug.toString()
        // to plugData and then outputs it

        String plugData = "";
        for(SmartPlug plug : plugs){
            plugData += plug.toString() + "";
        }
        return plugData;
    }
    public String toString2() {// initialises roomsAvailable, calls the SmartRoom class, adds the output of the toString
        // method to roomsAvailable and then outputs it

        String roomsAvailable = "rooms available: ";
        for (SmartRoom room : rooms){
            roomsAvailable += room.toString() + "";
        }
        return roomsAvailable;
    }
    public String toStringDevices(){// initialises devices, calls the SmartPlug class, adds the output of the
        // toStringDevices function to devices and then outputs it

        String devices = "Devices available: ";
        for (SmartPlug plug : plugs){
            devices += plug.toStringDevices();
        }
        return devices;
    }
    public void turnAllPlugsOff(){// calls the SmartPlug class and then calls the turnPlugsOff() function

        for (SmartPlug plug : plugs){
            plug.turnPlugsOff();
        }
    }

    public void turnAllPlugsOn() {// calls the SmartPlug class and then calls the turnAllPlugsOn function

        for (SmartPlug plug : plugs) {
            plug.turnPlugsOn();
        }
    }

    public void allPlugsOffInRoom(int roomOfPlug){// calls the SmartPlug class, then checks the plugID is correct and
        // calls the turnPlugsOffInRoom function

        for (SmartPlug plug : plugs) {
            if (plug.getPlugRoomID()==roomOfPlug){
                plug.turnPlugsOff();
            }
        }
    }
    public void allPlugsOnInRoom(int roomOfPlug){// calls the SmartPlug class, then checks the plugRoomID is correct
        // and calls the turnPLugsOnInRoom function

        for (SmartPlug plug : plugs) {
            if (plug.getPlugRoomID()==roomOfPlug){
                plug.turnPlugsOn();
            }
        }
    }
    public void togglePlug(){// builds Scanner object, gets plugSelectID, calls the SmartPlug class and
        // checks the plugID is correct then it calls the togglePlug function

        Scanner in = new Scanner(System.in);
        System.out.println("Enter ID of plug you want to toggle (integer)");
        int plugSelectID;
        System.out.println("");
        plugSelectID = in.nextInt();
        for (SmartPlug plug : plugs) {
            if (plug.getPlugID()==plugSelectID) {
                plug.togglePlug();
            }
        }
    }
    public String toString3(int roomOfPlug){// initialises printout, calls the SmartPlug class, calls the plugPrintout
        // function and passes roomOfPlug into the SmartPlug class before it adds the output to printout and then outputs
        // it

        String printout = "";
        for(SmartPlug plug : plugs) {
            if (plug.getPlugRoomID() == roomOfPlug) {
                printout += plug.plugPrintout(roomOfPlug) + "";
            }
        }
        return printout;
    }
    public void turnPlugOff(int plugSelect){// calls the SmartPlug class, checks the plugID is correct and calls the
        // turnPlugOff function

        for (SmartPlug plug : plugs){
            if (plug.getPlugID()==(plugSelect)) {
                plug.turnPlugsOff();
            }
        }
    }
    public void turnPlugOn(int plugSelect){// calls the SmartPlug class, checks the plugID is correct and calls the
        // turnPlugOn function

        for (SmartPlug plug : plugs){
            if (plug.getPlugID()==(plugSelect)) {
                plug.turnPlugsOn();
            }
        }
    }
    public void changeAttachedDevice(int newDeviceID, int plugSelect){// gets newDeviceName by using newDeviceID as 1
        // above the index of the deviceList array, then calls the SmartPlug class, checks the plugID is correct and
        // calls changeAttachedDevice and passes newDeviceID and newDeviceName into the SmartPlug class

        String newDeviceName = deviceList[newDeviceID - 1];
        for (SmartPlug plug : plugs){
            if (plug.getPlugID()==plugSelect){
                plug.changeAttachedDevice(newDeviceID, newDeviceName);
            }
        }
    }
    public void changePlugRoom(int plugSelect, int newPlugRoom){// initialises newPlugRoomName, then calls SmartRoom
        // and checks newPlugRoom against getRoomID, then it calls getRoomName to get newPlugRoomName,
        // then SmartPlug is called, then plugSelect is checked against getPlugID, then changePlugRoom is
        // called and newPlugRoomName and newPlugRoom are passed with it into SmartPlug

        String newPlugRoomName;
        for (SmartRoom room : rooms) {
            if (room.getRoomID() == newPlugRoom) {
                newPlugRoomName = room.getRoomName();
                for (SmartPlug plug : plugs) {
                    if (plug.getPlugID() == plugSelect) {
                        plug.changePlugRoom(newPlugRoomName, newPlugRoom);
                    }
                }
            }
        }
    }
    public String toStringDashboard(){// initialises dashboard, calls the SmartPlug class, calls toStringDashboard()
        // adds the output to dashbaord and then outputs it

        String dashboard = "";
        for(SmartPlug plug : plugs){
            dashboard += plug.toStringDashboard() + "";
        }
        return dashboard;
    }
}
