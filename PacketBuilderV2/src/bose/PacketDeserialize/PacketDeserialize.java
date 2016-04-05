/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package bose.PacketDeserialize;

import bose.Header.Header;
import bose.Header.HeaderCode;
import bose.PacketBuilderV2.PacketBuilderV2;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author saikat
 */
public class PacketDeserialize {
    
    private Object packetId = null;
    private int previousSequeceNo = -1;
    private ByteToDataConverter byteToDataConverter;
    public PacketDeserialize() {
    
        this.byteToDataConverter = new  ByteToDataConverter();
    }
    
    
    
    Map< Object , byte[]> map = new HashMap< Object, byte[]>();
    
    public  void deSerialize(ArrayList<byte[]> packet){
        
        
     
        for(byte[] b : packet){
         
            System.out.println("datagram size " + b.length );
            int index = headerDeSerialize(b);
            
            System.out.println("index after header " + index );
            int sequenceByteLength  = byteToDataConverter.getInteger(b, index, 1) ;
            index++;
            
            int sequenceNo = byteToDataConverter.getInteger(b, index, sequenceByteLength );
            index += sequenceByteLength ;
            
            System.out.println("Seq no : " + sequenceNo );
            
            byte[] dataArray = map.get(packetId);
            int dataArrayLength = 0;
            if(dataArray == null)
                dataArrayLength = 0;
            else dataArrayLength = dataArray.length ;
            
            if( previousSequeceNo + 1 == sequenceNo ){
                
                byte[] mergeData = new byte[ dataArrayLength + b.length - ( index - 1 ) ];
                
                System.out.println( "merge array len " + mergeData.length + " previous array len " + dataArrayLength + " current array len " + b.length + " index " + index );
                
                System.arraycopy( b, index, mergeData, dataArrayLength, b.length - index );
                
                map.put( packetId, mergeData );
                previousSequeceNo = sequenceNo;
            }
            
          //  System.out.println("index " + index);
            
            while(index < b.length){
                int attrID = byteToDataConverter.getInteger(b, index, 2);
                index +=2;
                
                int length ; 
                if(attrID == 5 || attrID == 6){
                    
                    length  = byteToDataConverter.getInteger(b, index, 2);
                    index += 2;
                }
                else {
                    
                    length  = byteToDataConverter.getInteger(b, index, 1);
                    index ++;

                }
                
               // System.out.println("len " + length);
                
                Object attrValue = null;
                switch(attrID){
                    
                    case 0:{
                        
                        attrValue = byteToDataConverter.getInteger(b, index, length);
                        break ; 
                        
                    }
                    case 1:{
                        
                        attrValue = byteToDataConverter.getLong(b, index, length);
                        attrValue = new Date((long) attrValue);
                        break ;
                    }
                    case 2:{
                        
                        attrValue = byteToDataConverter.getBoolean(b, index, length);
                        break;
                    }
                    case 3:{
                        
                        attrValue = byteToDataConverter.getString(b, index, length);
                        break;
                    }
                    case 4:{
                        
                        attrValue = byteToDataConverter.getDouble(b, index, length);
                        break;
                    }
                    case 5:{
                        
                        attrValue = byteToDataConverter.getString(b, index, length);
                        break;
                    }
                    case 6:{
                        
                        //System.out.println("big string len in packet " + length);
                        attrValue = byteToDataConverter.getByte(b, index, length);
                        break;
                    }
                   
                    
                    
                }
                
                index += length;
                
               // System.out.println("index " + index );
               if(attrID  == 6){
                   
                   byte[] byteDataArray = null; 
                   ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
                    try {
                        
                        ObjectOutputStream objectOutputStream = new  ObjectOutputStream(byteArrayOutputStream);
                        objectOutputStream.writeObject(attrValue);
                        byteDataArray =  byteArrayOutputStream.toByteArray();
                        
                        
                    } catch (IOException ex) {
                        ex.printStackTrace();
                    }
                  
                    System.out.print("attribute id " + attrID +" length " + length + " attribute values ");
                    
                    for(byte byteData : byteDataArray)
                        System.out.print(" " + byteData );
                    System.out.println();
               }
               else
                 System.out.println("attribute id " + attrID +" length " + length + " attribute value " + attrValue);
            }
        }
        
        for( Map.Entry< Object , byte[]> entry : map.entrySet() ){
            //byte[] dataInPacket = entry.getValue();
            System.out.println(entry.getKey() + " " + entry.getValue().length);
        }
    }
    
    private int headerDeSerialize( byte[] b ){
        
        int index = 0;
        
       // for(int i = 0 ; i < Header.PACKET_HEADER_SIZE; i++)
       //     System.out.print(b[i] + " ");
       
            
            while(index < PacketBuilderV2.PACKET_HEADER_SIZE ){
                
                int headerID = byteToDataConverter.getInteger(b, index, 2);
                index +=2;
              //  System.out.println(b[index]);
                int length  = byteToDataConverter.getInteger(b, index, 1);
               // System.out.println("length " + length);
                Object headerValue = null;
                String headerName = "";
                index++;
                
                switch(headerID){
                    
                    case HeaderCode.SESSION_ID:{
                        
                        headerValue = byteToDataConverter.getString(b, index, length);
                        headerName = "Session ID";
                        break;
                    }
                    case HeaderCode.PACKET_ID:{
                        
                        headerValue = byteToDataConverter.getInteger(b, index, length);
                        packetId = headerValue ;
                        headerName = "Packet ID";
                        break;
                    }
                      
                    case HeaderCode.SERVER_PACKET_ID:{
                        
                        headerValue = byteToDataConverter.getInteger(b, index, length);
                        headerName = "Server Packet Id";
                        break;
                    }
                        
                    case HeaderCode.ACTION_ID:{
                        
                        headerValue = byteToDataConverter.getInteger(b, index, length);
                        headerName = "Action Id" ;
                        break;
                    }
                    case HeaderCode.DATA:{
                        
                        headerValue = byteToDataConverter.getInteger(b, index, length);
                        headerName = "Data Bytes" ;
                        break;
                    }
                    
                    case HeaderCode.ACKNOWLEDGEMENT:{
                        
                        headerValue = byteToDataConverter.getLong(b, index, length);
                        headerName = "Acknowledgement" ;
                        break;
                    }
                        
                }
                
                
                //Object headerValue = ByteToDataConverter.getInteger(b, index, length);
                index += length;
                
  
                System.out.println( headerName + " " + headerValue);
            }
            
           // int dataLength =  (b[ index++ ] & 0xff ) << 8 | ( b[ index++ ] & 0xff );
            
          //  System.out.println("data length " + dataLength);
        
        return index;
    }
    
}
