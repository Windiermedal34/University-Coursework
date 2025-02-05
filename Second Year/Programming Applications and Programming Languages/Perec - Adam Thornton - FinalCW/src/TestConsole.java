


import java.io.FileNotFoundException;

public class TestConsole {

    static NodeCollection NodeCollection;
    public static void main(String[] args) throws FileNotFoundException {

        NodeMap map = new NodeMap();

        Console c = new Console(map);


        try {
            NodeCollection = new NodeCollection();
        } catch (
                FileNotFoundException e) {
            print(e.getMessage());
            return;
        }
    }

    public static void print(String  info){System.out.println(info);}

}
