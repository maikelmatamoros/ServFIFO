/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Server;

import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.DataOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.Socket;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.activation.MimetypesFileTypeMap;

/**
 *
 * @author jordan
 */
public class ServerF {

    private Socket clientSocket;
    private Clients c;

    public ServerF(Socket socket,Clients c) {
        this.clientSocket = socket;
        this.c=c;
    }
    
    public void init() {

        try {
            System.out.println("Nuevo cliente conectado");
            BufferedReader in = new BufferedReader(
                    new InputStreamReader(clientSocket.getInputStream()));
            BufferedWriter out = new BufferedWriter(
                    new OutputStreamWriter(clientSocket.getOutputStream()));

            String s;
            while ((s = in.readLine()) != null) {
                System.out.println(s);
                if (s.isEmpty()) {
                    break;
                } else {
                    if (s.equals("GET / HTTP/1.1")) {
                        sendRoot(out);
                    } else if (s.equals("GET /getText HTTP/1.1")) {
                        sendFile(clientSocket, "text.txt");
                    } else if (s.equals("GET /getImg HTTP/1.1")) {
                        sendFile(clientSocket, "img.jpg");
                    } else if (s.equals("GET /getTextLarge HTTP/1.1")) {
                        sendFile(clientSocket, "textLarge.txt");
                    } else if (s.equals("GET /getImgLarge HTTP/1.1")) {
                        sendFile(clientSocket, "imgLarge.jpg");
                    }
                }
            }

            System.out.println("Conexión terminada");
            out.close();
            in.close();
            clientSocket.close();
            c.changeState(false);
        } 
        catch (IOException ex) {
            Logger.getLogger(ServerF.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

    private void sendRoot(BufferedWriter out) throws IOException {
        out.write("HTTP/1.0 200 OK\r\n");
        out.write("Content-Type: text/html\r\n");
        out.write("Content-Length: 59\r\n");
        out.write("\r\n");
        out.write("<TITLE>Servidor FIFO</TITLE>");
        out.write("<h1>Ejecutar:</h1>");
        out.write("<P>/getText</P>");
        out.write("<P>/getImg</P>");
        out.write("<P>/getTextLarge</P>");
        out.write("<P>/getImgLarge</P>");
    }//getRoot

    private void sendFile(Socket clientSocket, String path) throws FileNotFoundException, IOException {
        File file = new File(path);
        String mime = new MimetypesFileTypeMap().getContentType(file);
        FileInputStream archivo = new FileInputStream(file);
        int longitud = archivo.available();
        byte[] datos = new byte[longitud];
        archivo.read(datos);
        archivo.close();

        String header = "HTTP/1.0 200 OK\n"
                + "Allow: GET\n"
                + "MIME-Version: 1.0\n"
                + "Server : Basic HTTP Server\n"
                + "Content-Type: " + mime + "\n"
                + "Content-Disposition: attachment; filename=\"" + file.getName() + "\"\n"
                + "Content-length: " + longitud
                + "\n\n";

        DataOutputStream clientStream = new DataOutputStream(
                new BufferedOutputStream(clientSocket.getOutputStream())) {
        };
        clientStream.writeBytes(header);
        clientStream.write(datos);

        clientStream.flush();
        clientStream.close();
    }//sendFile

}//class
