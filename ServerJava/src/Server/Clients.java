/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Server;

import java.net.Socket;
import java.util.LinkedList;
import java.util.Queue;

/**
 *
 * @author Maikel
 */
public class Clients {
    private Queue<Socket> clientsQueue;
    private boolean flag;

    public Clients() {
        this.clientsQueue=new LinkedList<>();
        this.flag=false;
    }

    public void push(Socket client){
        this.clientsQueue.add(client);
    }
    
    public Socket get(){
        return this.clientsQueue.poll();
    }
    
    public boolean flag(){
        return this.flag;
    }
    
    public void changeState(boolean state){
        this.flag=state;
    }
    
    public boolean empty(){
        return this.clientsQueue.isEmpty();
    }
    
}
