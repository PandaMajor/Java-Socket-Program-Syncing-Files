// The server role (C1)
import java.net.*;
import java.io.*;

public class Server {
    // Initialize socket, server, and input and output streams
    private Socket socket = null;
    private ServerSocket server = null;
    private BufferedReader in = null;
    private PrintWriter out = null;
    private String path = "C:\\Users\\major\\IdeaProjects\\socket programming\\D1copy\\";

    /*  makeFile creates the file with the file name that's passed as an argument
        returns boolean depending on if the file was able to be created
     */
    private boolean makeFile(String filename) throws IOException {
        File file = new File(path + filename);
        if(file.createNewFile()) {
            out.println(filename + " made");
            out.flush();
            return true;
        }
        else{
            return false;
        }
    }

    /*  copyFile copies input from the client as a byte array (of size 256).
        The function receives packets one at a time by acknowledging receiving
        packets by responding with "received". Packets are preceded "content"
        labels to guarantee that they are received in order. This continues until
        the "EOF" is received.
     */
    private void copyFile(File file) throws IOException {
        String line = "";
        OutputStream os = new FileOutputStream(file);
        DataInputStream temp = new DataInputStream(socket.getInputStream());
        line = in.readLine();
        while(!line.equals("EOF")) {
            byte[] b = new byte[256];
            if (line.equals("content")) {
                out.println("received");
                out.flush();
                temp.read(b);
                os.write(b);
            }
            line = in.readLine();   // Check if EOF is reached
        }
    }

    // Constructor with port
    public Server(int port){
        // Start server and wait for connection
        try{
            server = new ServerSocket(port);
            System.out.println("Server started");

            socket = server.accept();
            System.out.println("Client accepted");

            // Create input/output channels
            in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            out =  new PrintWriter(socket.getOutputStream());

            // line is used to read messages sent from client
            String line = in.readLine();

            if(line.equals("D1copy")){              // Upon receiving "D1copy", try to make folder D1copy
                File file = new File(line);
                boolean bool = file.mkdir();
                if(bool){
                    String filename;
                    out.println("D1copy made");     // Acknowledge the creation of the directory
                    out.flush();
                    line = in.readLine();
                    if(line.equals("make F1")){     // Create file F1 and copy information from C2
                        filename = "F1";
                        if(makeFile(filename)){     // Send message acknowledging completion of copying
                            copyFile(new File(path+filename));
                            out.println(filename + " finished");
                            out.flush();
                        }
                        else{
                            System.out.println("File " + filename + " already exists.");
                        }
                    }
                    line = in.readLine();
                    if(line.equals("make F2")){     // Create file F2 and copy information from C2
                        filename = "F2";
                        if(makeFile(filename)){     // Send message acknowledging completion of copying
                            copyFile(new File(path+filename));
                            out.println(filename + " finished");
                            out.flush();
                        }
                        else{
                            System.out.println("File " + filename + " already exists.");
                        }
                    }
                }
                else {
                    // Folder could not be created, so close connection
                    System.out.println("Couldn't create folder");
                    out.close();
                    in.close();
                    socket.close();
                }
            }
            line = in.readLine();
            if(line.equals("end of session")){
                // Close connection
                out.close();
                in.close();
                socket.close();
            }
        }
        catch(IOException i){
            System.out.println(i);
        }
    }

    public static void main(String[] args){
        Server server = new Server(5000);
    }
}