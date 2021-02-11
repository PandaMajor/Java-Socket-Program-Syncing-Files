// The client Role (C2)
import java.net.Socket;
import java.io.*;
import java.util.Arrays;

public class Client {
    // Initialize socket, input and output output streams
    private Socket socket = null;
    private BufferedReader in = null;
    private PrintStream out = null;
    private String path = "C:\\Users\\major\\IdeaProjects\\socket programming\\D1\\";

    /*  fileCopy reads content from a file into a byte array (of size 256)
        send messages to ensure that content is received in correct order,
        "content" before the actual information is sent, "received" to send
        information. Send "EOF" message to indicate that end of file is reached.
     */
    private void fileCopy(FileInputStream f) throws IOException {
        byte[] b = new byte[256];               // byte array used to limit message size
        String line;
        boolean eof = (f.read(b) == -1);
        while(!eof){                            // Repeat until EOF is reached
            out.println("content");
            out.flush();
            System.out.println("content");
            line = in.readLine();
            if(line.equals("received")){
                out.write(b);
            }
            System.out.println((Arrays.toString(b)));
            Arrays.fill(b, (byte) 0);           // Reset the array to 0s
            eof = (f.read(b) == -1);
            if(eof){
                out.println("EOF");
                System.out.println("EOF");
            }
        }
    }

    // Constructor to initialize ip address and port
    public Client(String address, int port) {
        try{
            // Create a connection
            socket = new Socket(address, port);
            System.out.print("Connected");

            // Create input/output channels
            in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            out = new PrintStream(socket.getOutputStream());

            String line = "";

            // Tell C1 (server) to make D1copy
            out.println("D1copy");
            out.flush();
            line = in.readLine();
            // Upon receiving "D1copy made", send message to create F1
            if(line.equals("D1copy made")){
                out.println("make F1");
                out.flush();
                line = in.readLine();
                // Upon receiving "F1 made", send message to prepare copying F1
                if(line.equals("F1 made")){
                    FileInputStream f = new FileInputStream(path+"F1.txt");
                    fileCopy(f);
                    line = in.readLine();
                    // Upon receiving "F1 finished", send message to create F2
                    if(line.equals("F1 finished")){
                        f = new FileInputStream(path+"F2.txt");
                        out.println("make F2");
                        out.flush();
                        line = in.readLine();
                        // Upon receiving "F2 made", send message to prepare copying F2
                        if(line.equals("F2 made")){
                            fileCopy(f);
                            line = in.readLine();
                            // Upon receiving "F2 finished", send message to signal end of session
                            if(line.equals("F2 finished")){
                                out.println("end of session");
                                out.flush();
                                // close connection
                                in.close();
                                out.close();
                                socket.close();
                            }
                        }
                    }
                }
            }
        }
        catch(IOException i) {
            System.out.println(i);
        }
    }

    public static void main(String[] args) {
        Client client = new Client("127.0.0.1", 5000);
    }
}
