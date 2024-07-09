
// A Java program for a Server
import java.net.*;
import java.io.*;

public class Server implements CommandInterface{
    // initialize socket and input stream
    private Socket socket = null;
    private ServerSocket server = null;
    private DataInputStream network_input = null;
    private DataOutputStream network_output = null;
    BufferedReader consoleInput = null;
    private String message = "";
    private static boolean continueInputLoop = false;
    public static String user = "Server:";
    public boolean shouldEndConnection = false;
    private String connectedUserName = "";

    // Commands Specific to Client
    public void send(String toSend) {
        System.out.println("Message Sending: -> " + toSend);
        message = "";
        try {
            network_output.writeUTF(toSend.replace(Command.COMMAND_NAME_SEND, ""));
            continueInputLoop = false;
        } catch (IOException i) {
            System.out.println(i);
        }
    }

    public void endConnection() {
        // close the connection
        shouldEndConnection = true;
        try {
            consoleInput.close();
            network_input.close();
            network_output.close();
            socket.close();
        } catch (IOException i) {
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
            network_input = new DataInputStream(new BufferedInputStream(socket.getInputStream()));
            
            //User Input
            consoleInput = new BufferedReader(new InputStreamReader(System.in));

            // sends output to the socket
            network_output = new DataOutputStream(socket.getOutputStream());
 
            String line = "";
            boolean firstPacketRecived = false;
            while (!shouldEndConnection) {
                while (!continueInputLoop)
                {
                    if(firstPacketRecived)
                    {
                        System.out.println("Attempting to Read Incomming Data from " + connectedUserName);
                        try
                        {
                            line = network_input.readUTF();
                            if(line.length() > 0){System.out.println("---------------------\n"+"  RECIVED -> "+line + "\n---------------------");}else{System.out.print("No Data Found -> Moving to Input");}
                            continueInputLoop  = true;
                        }
                        catch(IOException i)
                        {
                            System.out.println(i);
                        }
                    }
                    else
                    {
                        System.out.println("Got Client Data");
                        try
                        {
                            line = network_input.readUTF();
                            connectedUserName = line;
                            continueInputLoop  = true;
                            firstPacketRecived = true;
                        }
                        catch(IOException i)
                        {
                            System.out.println(i);
                        }
                    }
                }
                while(continueInputLoop)
                {
                    try {
                        System.out.println("Enter a message or command:");
                        message = user + message.replace(user, "") + consoleInput.readLine();
                        checkCommand(message);
                    }
                    catch (IOException i) {
                        System.out.println(i);
                    }
                
                }   
            }
        }
        catch(IOException i)
        {
            System.out.println(i);
        }
    }

    public void checkCommand(String cmd) {
        if (cmd.toUpperCase().contains(Command.COMMAND_NAME_SEND.toUpperCase())) {
            send(cmd);
        }
        if (cmd.toUpperCase().contains(Command.COMMAND_NAME_END.toUpperCase())) {
            endConnection();
        }
    }

    public static void main(String args[]) {
        Server server = new Server(5000);
    }
}
