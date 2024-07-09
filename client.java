import java.net.*;
import java.util.Scanner;
import java.util.concurrent.Callable;
import java.util.function.Consumer;
import java.io.*;

public class client implements CommandInterface{
    public String user = "";
    private Socket socket = null;
    private DataInputStream network_input = null;
    private DataOutputStream network_output = null;
    static CommandInterface sidedCommands;  
    private static boolean continueInputLoop = true;
    String message = "";
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
            network_input.close();
            network_output.close();
            socket.close();
        }
        catch (IOException i) {
            System.out.println(i);
        }
        System.out.println("!Ending Connection!");
    }

    public client(String addy, int port)
    {
        Scanner scnr = new Scanner(System.in);
        System.out.println("Enter User:");
        user = scnr.nextLine() + ":";
        // establish a connection
        try {
            socket = new Socket(addy, port);
            System.out.println("Connected Maybe?");
            // takes input from socket
            network_input = new DataInputStream(socket.getInputStream());
            
            // takes input from terminal
            BufferedReader consoleInput = new BufferedReader(new InputStreamReader(System.in));

            // sends output to the socket
            network_output = new DataOutputStream(
                socket.getOutputStream());

            while(continueInputLoop)
            {
                try {
                    message = user + message.replace(user, "") + consoleInput.readLine();
                    checkCommand(message);
                }
                catch (IOException i) {
                    System.out.println(i);
                }
            }
        }
        catch (UnknownHostException u) {
            System.out.println(u + "You done fucked up jimmy");
            return;
        }
        catch (IOException i) {
            System.out.println(i);
            return;
        }
    }

    public void checkCommand(String cmd)
    {
            if(cmd.toUpperCase().contains(Command.COMMAND_NAME_SEND.toUpperCase()))
            {
                send(cmd);
            }
            if(cmd.toUpperCase().contains(Command.COMMAND_NAME_END.toUpperCase()))
            {
                endConnection();
            }
    }
    public static void main(String[] args)
    {
        sidedCommands = new client("127.0.0.1", 5000);
    }
}

