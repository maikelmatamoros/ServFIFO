/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package serverjava;

import Server.Clients;
import Server.ServerF;
import java.io.IOException;
import java.net.ServerSocket;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author jordan
 */
public class ServerJava {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        try {
            System.out.println("Server Start in port:9000");
            ServerSocket mainServer = new ServerSocket(9000);
            Clients c = new Clients();
            Thread h = new Thread(new Runnable() {
                @Override
                public void run() {
                    
                    while (true) {
                       
                        if (c.empty()==false) {
                            System.err.println("Pos nel papa");
                            new ServerF(c.get(), c).init();
                        }
                        try {
                            Thread.sleep(1000);
                        } catch (InterruptedException ex) {
                            Logger.getLogger(ServerJava.class.getName()).log(Level.SEVERE, null, ex);
                        }//try-cath
                    }
                }
            });
            
            h.start();
            
            do {
                c.push(mainServer.accept());
                System.err.println("Pasa");
                try {
                    Thread.sleep(100);
                } catch (InterruptedException ex) {
                    Logger.getLogger(ServerJava.class.getName()).log(Level.SEVERE, null, ex);
                }
            } while (true);
        } catch (IOException ex) {
            Logger.getLogger(ServerJava.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

}
