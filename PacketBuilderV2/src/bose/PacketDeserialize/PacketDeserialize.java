/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package bose.PacketDeserialize;

import bose.Header.Header;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author saikat
 */
public class PacketDeserialize {
    
    
    
    public static void deSerialize(ArrayList<byte[]> packet){
     
        for(byte[] b : packet){
         
            System.out.println("packet size " + b.length );
            int index = headerDeSerialize(b);
            
          //  System.out.println("index " + index);
            
            while(index < b.length){
                int attrID = ByteToDataConverter.getInteger(b, index, 2);
                index +=2;
                
                int length ; 
                if(attrID == 5 || attrID == 6){
                    
                    length  = ByteToDataConverter.getInteger(b, index, 2);
                    index += 2;
                }
                else {
                    
                    length  = ByteToDataConverter.getInteger(b, index, 1);
                    index ++;

                }
                
               // System.out.println("len " + length);
                
                Object attrValue = null;
                switch(attrID){
                    
                    case 0:{
                        
                        attrValue = ByteToDataConverter.getInteger(b, index, length);
                        break ; 
                        
                    }
                    case 1:{
                        
                        attrValue = ByteToDataConverter.getLong(b, index, length);
                        attrValue = new Date((long) attrValue);
                        break ;
                    }
                    case 2:{
                        
                        attrValue = ByteToDataConverter.getBoolean(b, index, length);
                        break;
                    }
                    case 3:{
                        
                        attrValue = ByteToDataConverter.getString(b, index, length);
                        break;
                    }
                    case 4:{
                        
                        attrValue = ByteToDataConverter.getDouble(b, index, length);
                        break;
                    }
                    case 5:{
                        
                        attrValue = ByteToDataConverter.getString(b, index, length);
                        break;
                    }
                    case 6:{
                        
                        //System.out.println("big string len in packet " + length);
                        attrValue = ByteToDataConverter.getByte(b, index, length);
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
                  
                    System.out.print("attribute id " + attrID +" length " + length + "attribute values");
                    
                    for(byte byteData : byteDataArray)
                        System.out.print(" " + byteData );
                    System.out.println();
               }
               else
                 System.out.println("attribute id " + attrID +" length " + length + " attribute value " + attrValue);
            }
        }
    }
    
    private static int headerDeSerialize( byte[] b ){
        
        int index = 0;
        
       // for(int i = 0 ; i < Header.PACKET_HEADER_SIZE; i++)
       //     System.out.print(b[i] + " ");
       
            
            while(index < Header.PACKET_HEADER_SIZE){
                
                int headerID = ByteToDataConverter.getInteger(b, index, 2);
                index +=2;
              //  System.out.println(b[index]);
                int length  = ByteToDataConverter.getInteger(b, index, 1);
               // System.out.println("length " + length);
                index++;
                
                int headerValue = ByteToDataConverter.getInteger(b, index, length);
                index += length;
                
                System.out.println("Header ID "+ headerID + " Header Value " + headerValue);
            }
        
        return index;
    }
    
}
