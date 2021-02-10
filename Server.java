// The server role
import java.net.*;
import java.io.*;

public class Server {
    //initialize socket and input stream
    private Socket socket = null;
    private ServerSocket server = null;
    private BufferedReader in = null;
    private PrintWriter out = null;

    // constructor with port
    public Server(int port){
        // start server and wait for connection
        try{
            server = new ServerSocket(port);
            System.out.println("Server started");

            socket = server.accept();
            System.out.println("Client accepted");

            // create input/output channels
            in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            out =  new PrintWriter(socket.getOutputStream());

            String line = in.readLine();
            if(line.equals("D1copy")){
                File file = new File(line);
                boolean bool = file.mkdir();
                if(bool){
                    System.out.println("Directory created successfully");
                    out.println("D1copy made");
                    out.flush();
                    line = in.readLine();
                    if(line.equals("make F1")){
                        file = new File("C:\\Users\\major\\IdeaProjects\\socket programming\\D1copy\\F1");
                        if(file.createNewFile()){
                            out.println("F1 made");
                            out.flush();
                            OutputStream os = new FileOutputStream(file);
                            while(!line.equals("EOF")) {
                                System.out.println("hello");
                                os.write(in.read());
                                System.out.println("bye");
                                line = in.readLine();
                            }
                        }
                        else{
                            System.out.println("error");
                        }
                    }
                }
                else {
                    System.out.println("Couldn't create file");
                }
            }

            // close connection
            out.close();
            in.close();
            socket.close();
        }
        catch(IOException i){
            System.out.println(i);
        }
    }

    public static void main(String[] args){
        Server server = new Server(5000);
    }
}