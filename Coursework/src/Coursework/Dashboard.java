package Coursework;

public class Dashboard {

    public static void main(String[] args) {

        // build ConsoleHelper object
        ConsoleHelper console = new ConsoleHelper();

        // gets the size of the rooms array
        int howManyRooms = console.getInt("How many rooms are there in this property?");
        // gets the size of the plugs array
        int howManyPlugs = console.getInt("How many plugs do you want to place in this property?");

        // build SmartHome object
        SmartHome office = new SmartHome(howManyPlugs, howManyRooms);

        // populate SmartHome
        console.populate(office);

        while (true) {

            // calls dashboard with dashboardMenu
            console.dashboardMenu(office);

        }
    }
}
