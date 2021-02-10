// The client Role
import java.net.Socket;
import java.io.*;

public class Client {
    // initialize socket and input output streams
    private Socket socket = null;
    private BufferedReader in = null;
    private PrintStream out = null;

    // constructor to initialize ip address and port
    public Client(String address, int port) throws IOException {
        // create a connection
        try{
            socket = new Socket(address, port);
            System.out.print("Connected");

            // create input/output channels
            in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            out = new PrintStream(socket.getOutputStream());

            String line = "";

            // Tell C1 (server) to make D1copy
            out.println("D1copy");
            out.flush();
            line = in.readLine();
            if(line.equals("D1copy made")){
                out.println("make F1");
                out.flush();
                line = in.readLine();
                if(line.equals("F1 made")){
                    byte[] b = new byte[256];
                    FileInputStream f = new FileInputStream("C:\\Users\\major\\IdeaProjects\\socket programming\\D1\\F1.txt");
                    while(f.read(b) != -1){
                        if(f.read(b) != -1){
                            out.write(b);
                        }
                        else{
                            out.write(b);
                            out.println("\n EOF");
                        }
                    }
                    // read F1 -> server
                    // server write F1 -> F1'
                }
            }
        }
        catch(IOException i){
            System.out.println(i);
        }

        // close the connection
        try{
            in.close();
            out.close();
            socket.close();
        }
        catch(IOException i){
            System.out.println(i);
        }
    }
    public static void main(String[] args) throws IOException {
        Client client = new Client("127.0.0.1", 5000);
    }
}
