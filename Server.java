

// A Java program for a Server
import java.net.*;
import java.io.*;
 
public class Server
{
    //initialize socket and input stream
    private Socket          socket   = null;
    private ServerSocket    server   = null;
    private DataInputStream network_input =  null;
    private DataOutputStream network_output = null;
    BufferedReader consoleInput = null;
        //Commands Specific to Client
        public void send(String toSend)
        {
            System.out.println("Message Sending:" + toSend);
            message = "";
            try {
                network_output.writeUTF(toSend.replace(Command.COMMAND_NAME_SEND, ""));
            }
            catch (IOException i) {
                System.out.println(i);
            }
        }
    
        public void endConnection()
        {
            // close the connection
            continueInputLoop = false;
            try {
                data_input.close();
                network_output.close();
                socket.close();
            }
            catch (IOException i) {
                System.out.println(i);
            }
            System.out.println("!Ending Connection!");
        }
    

    // constructor with port
    public Server(int port)
    {
        // starts server and waits for a connection
        try
        {
            server = new ServerSocket(port);
            System.out.println("Server started");
 
            System.out.println("Waiting for a client to join ...");
 
            socket = server.accept();
            System.out.println("!Client accepted!");
 
            // takes input from the client socket
            network_input = new DataInputStream(
                new BufferedInputStream(socket.getInputStream()));
            consoleInput = new BufferedReader(new InputStreamReader(System.in));

            // sends output to the socket
            network_output = new DataOutputStream(
                socket.getOutputStream());
 
            String line = "";
 
            // reads message from client until "Over" is sent
            while (!line.equals("Over"))
            {
                try
                {
                    line = network_input.readUTF();
                    System.out.println(line);
 
                }
                catch(IOException i)
                {
                    System.out.println(i);
                }
            }
            System.out.println("Closing connection");
 
            // close connection
            socket.close();
            network_input.close();
        }
        catch(IOException i)
        {
            System.out.println(i);
        }
    }
 
    public static void main(String args[])
    {
        Server server = new Server(5000);
    }
}
