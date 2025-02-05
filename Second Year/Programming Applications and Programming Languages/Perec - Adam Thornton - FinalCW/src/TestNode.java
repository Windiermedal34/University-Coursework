import static java.lang.Integer.valueOf;

public class TestNode {

    public static void main(String[] args){

        String line0 = "0,2,1,Do you want to take up a sport?";
        String line2 = "2,3,4,Do you have any injuries that prevents you from doing certain activities?";
        String line21 = "Do you prefer activities that take a longer time over activities that take a shorter time?";

        Node n0 = new Node();
        mapNode(n0, line0);
        Node n1 = new Node();
        mapNode(n1, line2);
        Node n21 = new Node();
        mapNode(n21, line21);

        n0.setYesNode(n1);
        n0.setNoNode(n21);

        System.out.println(n0.getQuestion().toString());
        System.out.println(n0.getYesNode().toString());
        System.out.println(n0.getNoNode().toString());

    }

    public static void mapNode(Node n, String line){
        String[] stringArray = line.split(",");
        n.setID(valueOf(stringArray[0]));
        n.setYesID(valueOf(stringArray[1]));
        n.setNoID(valueOf(stringArray[2]));

        //n.setDescription(stringArray[3]);
        n.setQuestion(stringArray[4]);
    }





}


