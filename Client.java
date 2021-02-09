// The Client Role
import java.net.*;
import java.io.*;

public class Client {
    // initialize socket and input output streams
    private Socket socket = null;
    private DataInputStream input = null;
    private DataOutputStream out = null;

    // constructor to initialize ip address and port
    public Client(String address, int port){
        // create a connection
        try{
            socket = new Socket(address, port);
            System.out.print("Connected");

            // get input from terminal
            input = new DataInputStream(System.in);

            // send output to socket
            out = new DataOutputStream(socket.getOutputStream());
        }
        catch(UnknownHostException u){
            System.out.println(u);
        }
        catch(IOException i){
            System.out.println(i);
        }

        // string to read message from input
        String line = "";

        // read until "Over" is received
        while(!line.equals("Over")){
            try{
                assert input != null;
                line = input.readLine();
                assert out != null;
                out.writeUTF(line);
            }
            catch(IOException i){
                System.out.println(i);
            }
        }
        // close the connection
        try{
            input.close();
            out.close();
            socket.close();
        }
        catch(IOException i){
            System.out.println(i);
        }
    }
    public static void main(String[] args){
        Client client = new Client("127.0.0.1", 5000);
    }
}
