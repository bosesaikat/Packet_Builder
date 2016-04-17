/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.ipvision.attribute.deserialize;

import org.ipvision.attribute.code.Code;
import com.google.gson.Gson;
import java.util.HashMap;

/**
 *
 * @author saikat
 */
public class PacketDeserialize {

    private byte[] packet = null;
    
    private HashMap<String , Object> map ; 

    private int index = 0;
    
    private Gson gson ;
    
    private String jsonResponseString ;
    
    public PacketDeserialize(byte[] packet) {

        this.packet = packet;
        
        this.map = new HashMap<>();
        
        this.gson = new  Gson();
        
        this.jsonResponseString = null;
        
        deSerialize();
    }

    private void deSerialize() {

        int checkByte = (packet[index++] & 0xff);
        map.put("chk_byte", checkByte);

        switch (checkByte) {

            case 0: {

                fullJsonDeserialize();
                
                jsonResponseString  = gson.toJson(map);
                
                System.out.println(jsonResponseString);
                
                break;
            }
            case 1: {

                brokenJsonDeserialize();
                break;
            }
            case 2: {

                fullByteDeserialize();
                break;
            }
            case 3: {
                brokenByteDeserialize();

                break;
            }
            default: {

                System.out.println("Not understandable");
                break;
            }

        }

    }

    private static int getInt(byte[] bytes, int index, int length) {
        switch (length) {
            case 1:
                return (bytes[index++] & 0xFF);
            case 2:
                return (bytes[index++] & 0xFF) << 8 | (bytes[index++] & 0xFF);
            case 4:
                return (bytes[index++] & 0xFF) << 24 | (bytes[index++] & 0xFF) << 16 | (bytes[index++] & 0xFF) << 8 | (bytes[index++] & 0xFF);
        }
        return 0;
    }

    private static long getLong(byte[] bytes, int index, int length) {
        long result = 0;
        for (int i = (length - 1); i > -1; i--) {
            result |= (bytes[index++] & 0xFFL) << (8 * i);
        }
        return result;
    }

    private static String getString(byte[] data_bytes, int index, int length) {
        byte[] str_bytes = new byte[length];
        System.arraycopy(data_bytes, index, str_bytes, 0, length);

        return new String(str_bytes);
    }

    private static byte[] getBytes(byte[] received_bytes, int index, int length) {
        byte[] data_bytes = new byte[length];
        System.arraycopy(received_bytes, index, data_bytes, 0, length);
        return data_bytes;
    }

    private void fullJsonDeserialize() {
        //throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
        
        int attributeCode = ( packet[index++] & 0xff ) << 8 | packet[index++] & 0xff ;
        
        int length  = packet[index++] & 0xff;
        
        int actionCode = getInt(packet, index, length);
        
        map.put("actn", actionCode);
        
        switch(actionCode){
            
            case 20:{
                
                signUpResponseParse();   
                break;
            }
        }
        
        
    }

    private void brokenJsonDeserialize() {
        //throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    private void fullByteDeserialize() {
       // throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    private void brokenByteDeserialize() {
       // throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    private void signUpResponseParse() {
       // throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
       
       while( index<packet.length ){
           
           int attributeCode = ( packet[index++] & 0xff ) << 8 | packet[index++] & 0xff ;
           int length = packet[index++] & 0xff;
           
           switch(attributeCode){
               
               case Code.SESSION_ID : {
                   
                   String value = getString(packet, index, length);
                   map.put("sId", value);
                   index += length;
                   break;
                   
               }
               
               case Code.CLIENT_PACKET_ID :{
                   
                   String value = getString(packet, index, length);
                   map.put("pckId", value);
                   index += length;
                   break;
               }
               
               case Code.IS_EMAIL_VERIFIED :{
                   
                   int value = getInt(packet, index, length);
                   map.put("iev", value);
                   break;
               }
               
               case Code.OFFLINE_SERVER_IP :{
                   
                   String value = getString(packet, index, length);
                   map.put("oIp", value);
                   index += length;
                   break;
               }
               case Code.MOOD :{
                   
                   int value = getInt(packet, index, length);
                   map.put("mood", value);
                   break;
               }
               
               case Code.PROFILE_IMAGE_ID :{
                   
                   int value = getInt(packet, index, length);
                   map.put("prmImId", value);
                   break;
               }
               
               case Code.USER_IDENTITY: {
                   
                   String value = getString(packet, index, length);
                   map.put("uId", value);
                   index += length;
                   break;
               }
               
               case Code.OFFLINE_SERVER_PORT: {
                   
                   int value = getInt(packet, index, length);
                   map.put("oPrt", value);
                   break;
               }
               
               case Code.PROFILE_IMAGE :{
                   
                   String value = getString(packet, index, length);
                   map.put("prIm", value);
                   index += length;
                   break;
                   
               }
               
               case Code.SUCCESS :{
                   
                   boolean value = ((packet[index++] & 0xff) == 0) ? false : true ;
                   map.put("sucs", value);
                   break;
               }
               
               case Code.PASSWORD :{
                   
                   String value = getString(packet, index, length);
                   map.put("usrPwd", value);
                   index += length;
                   break;
               }
               
               case Code.IS_MY_NUMBER_VERIFIED :{
                   
                   String value = getString(packet, index, length);
                   map.put("imnv", value);
                   index += length;
                   break;
               }
               
               case Code.LIVE_STATUS :{
                   
                   int value = getInt(packet, index, length);
                   map.put("lsts", value);
                   break;
               }
               
               case Code.EMAIL_VERIFICATION_CODE :{
                   
                   Double value = Double.parseDouble( getString(packet, index, length) );
                   map.put("emVsn", value);
                   index += length;
                   break;
               }
               
               case Code.LAST_ONLINE_TIME :{
                   
                   long value = getLong(packet, index, length);
                   map.put("lot", value);
                   break;
               }
               
               case Code.PASSWORD_SETED :{
                   
                   boolean value = ((packet[index++] & 0xff) == 0) ? false : true ;
                   map.put("pstd", value);
                   break;
               }
               
               case Code.USER_NAME :{
                   
                   String value = getString(packet, index, length);
                   map.put("fn", value);
                   index += length;
                   break;
               }
               
               case Code.USER_ID :{
                   
                   long value = getLong(packet, index, length);
                   map.put("lot", value);
                   break;
               }
           }
       }
    }

}
