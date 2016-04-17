/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package binaryformatresponse;


import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.util.ArrayList;
import javax.xml.bind.DatatypeConverter;
import org.ipvision.byteBuilder.PacketMaker;
import org.ringid.contacts.ContactDTO;

/**
 *
 * @author saikat
 */
public class Server {

    public static void main(String args[]) {
        DatagramSocket sock = null;

        int port1 = 4000;
        int port2 = 4001;
        try {

            
            sock = new DatagramSocket(port2);

            byte[] buffer = new byte[128000];
            DatagramPacket incoming = new DatagramPacket(buffer, buffer.length);

            echo("Server socket created. Waiting for incoming data...");

            while (true) {

                sock.receive(incoming);
                byte[] data =incoming.getData() ;
                String request;

                if( (data[0] == 1) ){ /** Socket Keep Alive Check **/
                    
                    continue;    
                }
                else {
                    
                    request =new String(data, 0, incoming.getLength());
                }
                    
//                 = new String(data, 0, incoming.getLength());

                System.out.println(request);

                request = request.trim();

                echo(incoming.getAddress().getHostAddress() + " : " + incoming.getPort() + " - " + request);

                // s = "OK : " + s;
                PacketMaker packetMaker = new PacketMaker();
                packetMaker.setRequest(request);

                ArrayList<byte[]> packets;

                if (!request.isEmpty()) {
                    packets = packetMaker.getListOfPackets();
                } else {
                    continue;
                }

                for (byte[] packet : packets) {

                    System.out.println(packet.length + " bytes data has sent");

                    for (int i = 0; i < packet.length; i++) {
                        System.out.print(packet[i] + " ");
                    }
                    System.out.println();
                    
                   // new PacketDeserialize(packet);

                    // String hexString = DatatypeConverter.printHexBinary( packet );
                    //   System.out.println(hexString);
                    DatagramPacket dp = new DatagramPacket(packet, packet.length, incoming.getAddress(), incoming.getPort());
                    sock.send(dp);
                }

                //sock.close();
            }
        } catch (IOException e) {
            System.err.println("IOException " + e);
        }
    }

    //simple function to echo data to terminal
    public static void echo(String msg) {
        System.out.println(msg);
    }
}
