package Coursework;

import java.util.Scanner;

public class ConsoleHelper {
    public void out(String info) {System.out.println(info);}// print string data

    public int getInt(String sentence) {// store integer data
        Scanner in = new Scanner(System.in);
        out(sentence);
        return in.nextInt();
    }

    public String getString(String sentence){// store string data
        Scanner input = new Scanner(System.in);
        out(sentence);
        return input.nextLine();
    }

    public void populate(SmartHome office){
        for (int i = 0; i < office.getSizeRoom(); i++) {// loops through 'rooms' array, gets roomName and roomID and
            // calls addRoom() to pass them to the SmartHome class

            String roomName = getString("please provide a name for your room");
            int roomID = i + 1;
            office.addRoom(roomName, roomID);
        }
        for (int i = 0; i < office.getSizePlug(); i++) {// loops through 'plugs' array, displays rooms available for
            // plugs, gets plugRoomID, status, deviceID and plugID and calls addPlug() to pass them to the SmartHome
            // class

            out("Enter plug information below");
            out(office.toString2());
            out("Using the above list, please select the room for this\nplug (integer only)");
            int plugRoomID = getInt("");
            out("AVAILABLE DEVICE LIST OPTIONS\nThese are standard devices that can be attached to\nthe smart plug:");
            out("1 - Lamp\n2 - TV\n3 - Computer\n4 - Phone Recharger\n5 - Heater");
            out("Using the above list, please select the device to attach\nto the smart plug (integer only)");
            boolean status = false;
            int deviceID = getInt("");
            int plugID = i + 1;
            office.addPlug(plugRoomID, status, plugID, deviceID);
        }
    }
    public void menuOptions(SmartHome office) {// gets lightStatus, calls either turnAllPlugsOn() or turnAllPlugsOff()
        // depending on the value of lightStatus and finally displays plug data

        out("HOUSE LEVEL OPTIONS");
        out("1 - Switch all plugs off");
        out("2 - Switch all plugs on");
        out("Select an option");

        int lightStatus = getInt("");

        if (lightStatus == 2){
            office.turnAllPlugsOn();
        }
        else if (lightStatus == 1){
            office.turnAllPlugsOff();
        }
        out(office.toString());
    }
    public void menuOptions2(SmartHome office){// displays rooms, gets roomOfPlug then displays data for that room
        // gets roomOption and then calls either allPlugsOffInRoom, allPlugsOnInRoom or togglePlug depending on
        // the value of roomOption, to pass roomOfPlug to the SmartHome class, finally data of plugs and rooms are
        // displayed

        out(office.toString2());
        out("Please select room (integer only)");

        int roomOfPlug = getInt("");
        out(office.toString3(roomOfPlug));

        out("ROOM LEVEL OPTIONS");
        out("1 - Switch all plugs off in room\n2 - Switch all plugs on in room\n3 - Select a plug in the room and toggle its on/off status\n");

        int roomOption = getInt("");

        if (roomOption == 1){
            office.allPlugsOffInRoom(roomOfPlug);
        }
        else if(roomOption == 2){
            office.allPlugsOnInRoom(roomOfPlug);
        }
        else if (roomOption == 3){
            office.togglePlug();
        }
    }
    public void menuOptions3(SmartHome office){// gets plugSelect and plugOption, then depending on the value of
        // plugOption calls turnPlugOff, turnPlugOn, changeAttachedDevice or changePlugRoom, then data of plugs and
        // rooms are displayed

        out("Please select plug (integer only)");
        int plugSelect = getInt("");
        out("PLUG LEVEL OPTIONS\n1 - Switch plug off\n2 - Switch plug on\n3 - Change attached device\n4 - Move plug to different room\n");
        out("Select an option");
        int plugOption = getInt("");
        if (plugOption == 1){
            office.turnPlugOff(plugSelect);
        }
        else if (plugOption == 2){
            office.turnPlugOn(plugSelect);
        }
        else if (plugOption == 3){
            changeAttachedDevice(office, plugSelect);
        }
        else if (plugOption == 4){
            changePlugRoom(office, plugSelect);
        }
        out(office.toString());
        out(office.toString2());
    }
    public void changeAttachedDevice(SmartHome office, int plugSelect){// gets newDeviceID and calls
        // changeAttachedDevice to pass it into the SmartHome class

        out("AVAILABLE DEVICE LIST OPTIONS\nThese are standard devices attached to the smart plug,\nunless otherwise stated");
        out("1-Lamp\n2-TV\n3-Computer\n4-Phone Recharger\n5-Heater\n");
        out("Enter device to attach to smart plug (integer only)");

        int newDeviceID = getInt("");

        office.changeAttachedDevice(newDeviceID, plugSelect);
    }
    public void changePlugRoom(SmartHome office, int plugSelect){// gets newRoomID and calls changePlugRoom to pass
        // it into the SmartHome class

        out(office.toString2());
        out("Please select room for device from list (integer only)");
        int newRoomID = getInt("");

        office.changePlugRoom(plugSelect, newRoomID);
    }
    public void dashboardMenu(SmartHome office){// gets dashboardOption and then depending on the value of
        // dashboardOptions passes it on into one of menuOptions, menuOptions2, menuOptions3 or systemLevelOptions when
        // they are called determined by a switch function

        out("                                                  -------------------------------DASHBOARD--------------------------------                              ");
        out(office.toStringDashboard());
        out("                                                  ------------------------------MENU OPTIONS------------------------------                              ");
        out("                                                  --------------------------please select option--------------------------                              ");
        out("1 - house level options\n2 - room level options\n3 - plug level options\n4 - system level options");
        int dashboardOptions = getInt("");

        switch(dashboardOptions){
            case 1:
                menuOptions(office);
                break;
            case 2:
                menuOptions2(office);
                break;
            case 3:
                menuOptions3(office);
                break;
            case 4:
                systemLevelOptions(office);
                break;
        }

    }
    public void systemLevelOptions(SmartHome office){// gets newDeviceName and then calls the newDevice() function to
        // pass it on into the SmartHome class, gets status, plugRoomID, plugID, deviceID and calls the newPlugs()
        // function to pass them on into the SmartHome class, gets roomName and calls the newRooms() function to pass
        // them on into the SmartHome class

        out("system level options\n1 - add new device\n2 - add new SmartPlug\n3 - add new room\nselect an option");

        int systemLevelOption = getInt("");

        if (systemLevelOption == 1) {

            out("Add new device");
            String newDeviceName = getString("Please enter name for new device:");
            out(office.newDevice(newDeviceName));
        }
        else if (systemLevelOption == 2) {

            out("Add new plug");
            boolean status = false;
            out(office.toString2());
            out("Which room would you like the plug to be in");
            int plugRoomID = getInt("");
            int plugID = office.getSizePlug() + 2;
            out(office.toStringDevices());
            out("Which device would you like to be connected to the new plug (integer only)");
            int deviceID = getInt("");
            office.newPlugs(plugRoomID, status, plugID, deviceID);
        }
        else if (systemLevelOption == 3) {

            out("Add new room");
            String roomName = getString("Please enter name of new room");
            office.newRooms(roomName);
        }
    }
}
