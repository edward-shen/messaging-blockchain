package network;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.lang.ClassNotFoundException;
import java.net.ServerSocket;
import java.net.Socket;


/*

example is

    network.Server server = new network.Server(8000);
    server.start();

    Thread.sleep(30000);

    server.stop();
 */


public class Server implements Runnable {
  private int port;
  private ServerSocket server;
  private Thread t;
  private String threadName = "server";

  public Server(int port) {
    this.port = port;
    try {
      server = new ServerSocket(this.port);
    } catch (IOException e) {
      System.out.println(e.getMessage());
    }
  }

  public void start() {
    if (t == null) {
      t = new Thread(this, threadName);
      t.start();
    }
  }

  public void stop() {
    System.out.println("Shutting down Socket server!!");
    //close the ServerSocket object
    try {
      this.server.close();
    } catch (IOException e) {
      System.out.println(e.getMessage());
    }
  }

  public void run() {

    //create the socket server object
    //keep listens indefinitely until receives 'exit' call or program terminates
    try {
      while (true) {
        System.out.println("Waiting for client request");
        Socket socket = this.server.accept();
        ObjectInputStream ois = new ObjectInputStream(socket.getInputStream());
        String message = (String) ois.readObject();
        System.out.println("Message Received: " + message + " from " + socket.getInetAddress());
        ObjectOutputStream oos = new ObjectOutputStream(socket.getOutputStream());
        oos.writeObject("Message Recieved. Your IP is" + socket.getInetAddress());
        ois.close();
        oos.close();
        socket.close();
        //terminate the server if client sends exit request
      }

    } catch (IOException e) {
      System.out.println(e.getMessage());
    } catch (ClassNotFoundException e) {
      System.out.println(e.getMessage());
    }

  }
}