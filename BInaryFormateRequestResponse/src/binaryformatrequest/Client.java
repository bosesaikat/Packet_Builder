/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package binaryformatrequest;

import bose.clientRequest.*;
import com.google.gson.Gson;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import javax.xml.bind.DatatypeConverter;

/**
 *
 * @author saikat
 */
public class Client {

    public static void main(String args[]) {
        DatagramSocket sock = null;
        int port = 4001;

        SignInRequest signInRequest = new SignInRequest();

        signInRequest.setActn(20);
        signInRequest.setDid("asdfajksgflas");
        signInRequest.setDvc(1);
        signInRequest.setIsb(false);
        signInRequest.setLt(1);
        signInRequest.setPckId("FRaqbC814605276398611346");
        signInRequest.setPen(3);
        signInRequest.setuId(2110065096);
        signInRequest.setUsrPw("123456");
        signInRequest.setVsn(140);

        EmailVerificationRequest emailVerificationRequest = new EmailVerificationRequest();

        emailVerificationRequest.setActn(220);
        emailVerificationRequest.setDid("fbd2e455ea3b2631");
        emailVerificationRequest.setDvc(1);
        emailVerificationRequest.setEl("ringid975@gmail.com");
        emailVerificationRequest.setMblDc("%2B880");
        emailVerificationRequest.setPckId("JbhBtur1460798771017038968");
        emailVerificationRequest.setuId(2110077604);
        emailVerificationRequest.setVsn(139);
        emailVerificationRequest.setEvc("5572");
        
        

        UserUpdateRequest userUpdateRequest = new UserUpdateRequest();

        String updateReq = " {\"actn\": 126, \"did\": \"fbd2e455ea3b2631\", \"dvc\": 2, \"el\": \"ringid975@gmail.com\", \"ispc\": 0, \"mbl\": \"1515632757\", \"mblDc\": \"+880\", \"nm\": \"Neymar\", \"pckId\": \"1MLsQn31460865908693546503\", \"uId\": \"2110077639\", \"usrPw\": \"123456\", \"vsn\": \"141\"}";
        userUpdateRequest = new Gson().fromJson(updateReq, UserUpdateRequest.class);
        //String s;

        String addFriendReq = "{\"uId\":\"2110010016\",\"actn\":127,\"pckId\":\"14435194282922110010002\",\"sId\":\"89722799736374062110067045\"}";
        
        
        //BufferedReader cin = new BufferedReader(new InputStreamReader(System.in));
        
        String delFriendReq = "{\"uId\":\"2110067045\",\"actn\":128,\"pckId\":\"77f1844bd0408\",\"sId\":\"89728677953455352110067046\"}";
        
        String acceptFriendReq = "{\"utId\":52559,\"actn\":129,\"pckId\":\"77f1844c66fe0\",\"sId\":\"89728677953455352110067046\"}";
        
        FriendRequest friendRequest = new Gson().fromJson(acceptFriendReq,FriendRequest.class);
        
        
        
        
        String callBlockReq = "{\"actn\":82,\"sn\":6,\"sv\":0,\"utId\":52349,\"pckId\":\"77f1844c72da8\",\"sId\":\"95699514561334112110067046\"}";
        
        FeedRequest feedRequest = new Gson().fromJson(callBlockReq, FeedRequest.class);
        
        
        String callFriendReq = "{\"fndId\":\"2110067045\",\"actn\":174,\"pckId\":\"1459682413067045         \",\"callID\":\"jCRGryWt14608826592710249        \",\"sId\":\"44884286691693142110067045\",\"calT\":1}";
        
        CallFriend callFriendRequest = new Gson().fromJson(callFriendReq, CallFriend.class);
        
        
        String storeContacts = "{\"sId\":\"45724655907464252110067046\",\"actn\":284,\"pckId\":\"77f1844c90000            \",\"cnLst\":[{\"cnn\":\"Naseef    \",\"cnv\":\"01625137811   \"}                                      ]}\n" +
"							{\"sId\":\"45732757211012832110067045\",\"actn\":284,\"pckId\":\"jCRGryWt14597672731422526\",\"cnLst\":[{\"cnn\":\"Naseef vai\",\"cnv\":\"+8801625137811\"},{\"cnn\":\"Tuhin\",\"cnv\":\"+880181213341\"}]}";
        
        String userShortDetails = "{\"actn\":204,\"utId\":22061, \"pckId\" : \"SA1XvpFV14608963620641743\", \"sId\" : \"51843873356338382110067046\"}";

        try {
            sock = new DatagramSocket();

            InetAddress host = InetAddress.getByName("192.168.8.231");

            Gson gson = new Gson();
            String jsonString = gson.toJson(callFriendRequest);
            System.out.println(jsonString);

            //byte[] b = jsonString.getBytes();
            
            byte[] b= userShortDetails.getBytes();

            DatagramPacket dp = new DatagramPacket(b, b.length, host, port);
            sock.send(dp);

            while (true) {

                //   echo("Enter message to send : ");
                //  s = (String) cin.readLine();
                //  b = s.getBytes();
                //  dp = new DatagramPacket(b, b.length, host, port);
                byte[] buffer = new byte[128000];
                DatagramPacket reply = new DatagramPacket(buffer, buffer.length);

                sock.receive(reply);

                byte[] data = reply.getData();
                if (data == null) {
                    sock.close();
                }
                // System.out.println(data.length);
                //s = new String(data, 0, reply.getLength());

                int packetLength = reply.getLength();
                byte[] packet = new byte[packetLength];

                System.arraycopy(data, 0, packet, 0, packetLength);

                echo(reply.getAddress().getHostAddress() + " : " + reply.getPort() + " - " + packetLength + " bytes data has received");

                showPacket(packet);

            }
        } catch (IOException e) {
            System.err.println("IOException " + e);
        }
    }

    private static void echo(String msg) {
        System.out.println(msg);
    }

    private static void showPacket(byte[] packet) {
        //throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.

        for (byte b : packet) {
            System.out.print(b + " ");
        }
        System.out.println();
        //String hexString  = DatatypeConverter.printHexBinary(packet);
        // System.out.println(hexString);
    }
}
