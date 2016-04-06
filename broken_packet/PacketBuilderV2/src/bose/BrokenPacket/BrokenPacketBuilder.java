/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package bose.BrokenPacket;

import bose.AttributeInfo.AttributesInfo;
import bose.ByteUtil.ByteUtil;
import bose.Header.Header;
import bose.Header.HeaderCode;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Random;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author saikat
 */
public class BrokenPacketBuilder {
    
    private ArrayList<AttributesInfo> attributes;
    
    private byte[] allData = null ;
    
    public static final int BROKEN_PACKET_SIZE = 2000 ;
    
    public static int BROKEN_PACKET_HEADER_SIZE;
    
    private Header header;
    
    public BrokenPacketBuilder( ArrayList<AttributesInfo> attributes ){
        
        this.attributes = attributes ;
        
        setHeader();
    }
    
     private void setHeader() {

        this.header = new Header();
        this.header.setSessionID("A07XE");
        this.header.setPacketID( new Random().nextInt() % 1000);
        this.header.setServerPacketID(1);
        this.header.setActionID(1);
        this.header.setAcknowledgement(1L);
        
       // this.header.setSequenceNo(0);

    }
    public void addAttributes(){
        
        
        ArrayList<Integer> largeAttributesIndex = new ArrayList<Integer>();
        //System.out.println(largeAttributes);
        int countByte = 0;
        
        for (int i = 0; i < this.attributes.size(); i++) {
            
            AttributesInfo attributesInfo = this.attributes.get(i) ; 

            int attrId = attributesInfo.getID();
            Class type = attributesInfo.getType();
            int size = attributesInfo.getSize();
            
            Object value = null;
            
            byte[] byteValues = null;
            
            byte[] currentDataBytes = null;
            
            if( ByteUtil.isByte(type)){
                
                byteValues = attributesInfo.getDataArray();
                currentDataBytes = ByteUtil.getBytes(attrId, size, byteValues, type);
                               
            }
            
            else{
                
                value = attributesInfo.getData();
                currentDataBytes = ByteUtil.getBytes(attrId, size, value, type);
                
            }
            
             /* for(byte b : currentDataBytes){
            
                 System.out.print(b +" ");
              }
              System.out.println();
            */
            int mergeByteLength  = countByte + currentDataBytes.length ;
            byte[] mergeBytes = new  byte[ mergeByteLength ];
            
            if(allData!=null)
                System.arraycopy(allData, 0, mergeBytes, 0, allData.length);
            
            System.arraycopy(currentDataBytes, 0 , mergeBytes , countByte , currentDataBytes.length);
            allData = mergeBytes;
            countByte += currentDataBytes.length;

        }
    
       System.out.println(allData.length);
        
        for(byte b : allData ){
            
            System.out.print(b +" ");
        }
        System.out.println();
        
    }
    
    public ArrayList<byte[]> getByte(){
        
        try {
            
            
            BROKEN_PACKET_HEADER_SIZE = headerSizeCalc(header);
            
            int checkByte = 1 ;
            
            int totalBytes = allData.length + BROKEN_PACKET_HEADER_SIZE;
            
            ArrayList<byte[]> packet = makePacket(header, allData , totalBytes , checkByte) ;
            
            return packet;
            
        } catch (IllegalArgumentException ex) {
            Logger.getLogger(BrokenPacketBuilder.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            Logger.getLogger(BrokenPacketBuilder.class.getName()).log(Level.SEVERE, null, ex);
        } catch (NoSuchMethodException ex) {
            Logger.getLogger(BrokenPacketBuilder.class.getName()).log(Level.SEVERE, null, ex);
        } catch (InvocationTargetException ex) {
            Logger.getLogger(BrokenPacketBuilder.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        return null;
    }
    
    private int headerSizeCalc( Object obj ) throws IllegalArgumentException, IllegalAccessException, NoSuchMethodException, InvocationTargetException {
        //throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.

        Field[] headerFields = obj.getClass().getDeclaredFields();
       // Method[] methods = header.getClass().getMethods();
        int headerBytes = 0;

        for (Field field : headerFields) {

            Class cls = (Class) field.getAnnotatedType().getType();
            //  System.out.println(cls);

            int headerCodeByte = 2;
            int headerValueLength = 1;
            
            String methodNameEnd = field.getName().replaceFirst( 
                  String.valueOf( field.getName().charAt(0) ), String.valueOf( field.getName().toUpperCase().charAt(0) ) );
          
            Method m = obj.getClass().getDeclaredMethod("get" + methodNameEnd );
         // System.err.println( m.invoke(header));

            if (ByteUtil.isInteger(cls)) {
         
                    
                    headerBytes += 4;
               
            } else if (ByteUtil.isBoolean(cls)) {
                
                headerBytes += 1;
                
            } else if (ByteUtil.isByte(cls)) {
                
                headerBytes += 1;
                
            } else if (ByteUtil.isCharacter(cls)) {
                
                headerBytes += 1;
                
            } else if (ByteUtil.isString(cls)) {
                
                String value = (String) m.invoke( obj );
                headerBytes += value.getBytes().length ;
                //headerBytes += field.get(header).toString().getBytes().length;
            } else if (ByteUtil.isDouble(cls)) {
                
                Double value = (Double)m.invoke( obj );
                headerBytes += String.valueOf( value ).getBytes().length;
                
            } else if (ByteUtil.isFloat(cls)) {
                
                float value = (float)m.invoke( obj );
                headerBytes += String.valueOf( value ).length();
                
            } else if (ByteUtil.isLong(cls)) {
                
                headerBytes += 8;
            }

            //System.out.println( headerBytes );
            headerBytes += headerCodeByte + headerValueLength;

        }

        return headerBytes;
    }

    private ArrayList<byte[]> makePacket(Header obj, byte[] allData, int length, int checkByte) throws NoSuchMethodException, IllegalAccessException, IllegalArgumentException, InvocationTargetException {
       // throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
       
       ArrayList<byte[]> madePacket = new ArrayList<byte[]>();
       
       int totalPacket = length / BROKEN_PACKET_SIZE;
       
       if( ( length - totalPacket * BROKEN_PACKET_SIZE) > 0 ){
       
           totalPacket ++ ;
       }
       
       byte[] headerBytes = new byte[ BROKEN_PACKET_HEADER_SIZE ];
       
       headerBytes = addHeader(header , allData.length);
       
       
       int currentDataIndex = 0; 
       int sequenceNo = 0;
       int currentPacketNo = 0;
       int remainingPacketNo = 0;
     
       currentPacketNo ++ ;
     
       while(currentDataIndex < allData.length){
           
           int index = 0;
           
           byte[] currentPacket = new byte[BROKEN_PACKET_SIZE];
           
          
               
               currentPacket[ index++ ] = (byte) checkByte;
               
               System.arraycopy(headerBytes, 0, currentPacket, index, BROKEN_PACKET_HEADER_SIZE);
               index += BROKEN_PACKET_HEADER_SIZE;
               
               
               remainingPacketNo  = totalPacket - currentPacketNo ;
               
               byte[] subPacketHeader = null;
               
               if(sequenceNo > 255)
                   subPacketHeader = ByteUtil.getBytes(HeaderCode.SEQUENCE_NO, 2, sequenceNo, int.class);
               else
                   subPacketHeader = ByteUtil.getBytes(HeaderCode.SEQUENCE_NO, 1, sequenceNo, int.class);
               
               System.arraycopy(subPacketHeader, 0, currentPacket, index, subPacketHeader.length);
               sequenceNo++ ;
               
               index += subPacketHeader.length;
               
               if(totalPacket > 255)
                   subPacketHeader = ByteUtil.getBytes(HeaderCode.TOTAL_PACKET, 2, totalPacket , int.class);
               else
                   subPacketHeader = ByteUtil.getBytes(HeaderCode.TOTAL_PACKET, 1, totalPacket , int.class);
               
               System.arraycopy(subPacketHeader, 0, currentPacket, index, subPacketHeader.length);
               
               index += subPacketHeader.length;
               
               if(currentPacketNo > 255)
                   subPacketHeader = ByteUtil.getBytes(HeaderCode.CURRENT_PACKET_NO, 2, currentPacketNo++ , int.class);
               else
                   subPacketHeader = ByteUtil.getBytes(HeaderCode.CURRENT_PACKET_NO, 1, currentPacketNo++ , int.class);
               
               System.arraycopy(subPacketHeader, 0, currentPacket, index, subPacketHeader.length);
               
               index += subPacketHeader.length;
               
               if(remainingPacketNo > 255)
                   subPacketHeader = ByteUtil.getBytes(HeaderCode.REMAINING_PACKET_NO, 2, remainingPacketNo , int.class);
               else
                   subPacketHeader = ByteUtil.getBytes(HeaderCode.REMAINING_PACKET_NO, 1, remainingPacketNo , int.class);
               
               System.arraycopy(subPacketHeader, 0, currentPacket, index, subPacketHeader.length);
               
               index += subPacketHeader.length;
               
               int currentPacketDataLength  = BROKEN_PACKET_SIZE - index ;
               
               if( ( allData.length - currentDataIndex ) < currentPacketDataLength){
                   
                   currentPacketDataLength = allData.length - currentDataIndex ;
                   
                   byte[] tempArray = new byte[currentPacketDataLength + index ];
                   
                   System.arraycopy(currentPacket, 0, tempArray , 0 , index);
                   
                   currentPacket = tempArray;
               }
               
            //   System.out.println("src index " + currentDataIndex + " dst index " + index + " copy len " +currentPacketDataLength + " dst len " + currentPacket.length );
               System.arraycopy(allData, currentDataIndex , currentPacket , index, currentPacketDataLength);
    
               currentDataIndex += currentPacketDataLength ; 
               
               //System.out.println(currentDataIndex + " " + currentPacket.length);
               madePacket.add(currentPacket);
               
           
           
       }
       
       return madePacket;
    }
    
    

    private byte[] addHeader(Header obj , int dataLength) throws NoSuchMethodException, IllegalAccessException, IllegalArgumentException, InvocationTargetException {
      //  throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
        
      
      //System.out.println(obj.getClass());
        
        byte[] bytes = new byte[BROKEN_PACKET_HEADER_SIZE] ;
        int headerByteCount = 0;
        Field[] fields = obj.getClass().getDeclaredFields();

        int fieldIndex = 0;

        for (Field field : fields) {

            Class cls = (Class) field.getType();
            int headerDataSize = 0;
            Object headerValue = 0;
            int headerCode = 0;

            String methodNameEnd = field.getName().replaceFirst(
                    String.valueOf(field.getName().charAt(0)), String.valueOf(field.getName().toUpperCase().charAt(0)));

            Method m = obj.getClass().getDeclaredMethod("get" + methodNameEnd);
            
            //System.out.println(m.getName());

            if (ByteUtil.isInteger(cls)) {
                //  System.out.println("hit");
                 headerDataSize = 4;
                 headerValue = m.invoke(obj);

                
            } else if (ByteUtil.isBoolean(cls)) {

                headerDataSize = 1;
                headerValue = m.invoke(obj);

            } else if (ByteUtil.isByte(cls)) {

                headerDataSize = 1;
                headerValue = m.invoke(obj);

            } else if (ByteUtil.isCharacter(cls)) {

                headerDataSize = 1;
                headerValue = m.invoke(obj);

            } else if (ByteUtil.isString(cls)) {

                headerDataSize = m.invoke(obj).toString().getBytes().length;
                headerValue = m.invoke(obj);

            } else if (ByteUtil.isDouble(cls)) {

                headerDataSize = String.valueOf(m.invoke(obj)).getBytes().length;
                headerValue = m.invoke(obj);

            } else if (ByteUtil.isFloat(cls)) {

                headerDataSize = String.valueOf(m.invoke(obj)).length();
                headerValue = m.invoke(obj);

            } else if (ByteUtil.isLong(cls)) {

                headerDataSize = 8;
                headerValue = m.invoke(header);
            }

            switch (fieldIndex) {

                case HeaderCode.SESSION_ID:
                    headerCode = HeaderCode.SESSION_ID;
                    break;
                case HeaderCode.PACKET_ID:
                    headerCode = HeaderCode.PACKET_ID;
                    break;
                case HeaderCode.SERVER_PACKET_ID: {

                    headerCode = HeaderCode.SERVER_PACKET_ID;
                    headerValue = (int) headerValue;
                    obj.setServerPacketID((int)(headerCode) + 1);
                    break;
                }

                case HeaderCode.ACTION_ID:
                    headerCode = HeaderCode.ACTION_ID;
                    break;
                case HeaderCode.ACKNOWLEDGEMENT:
                    headerCode = HeaderCode.ACKNOWLEDGEMENT;
                    break;
       
            }

            // System.out.println(headerCode + " " + headerDataSize + " " + headerValue + " " + cls);
            byte[] currentHeaderByte = ByteUtil.getBytes(headerCode, headerDataSize, headerValue, cls);
            // System.out.println(currentHeaderByte.length + " " + bytes.length + " " + headerByteCount);
            System.arraycopy(currentHeaderByte, 0, bytes, headerByteCount, currentHeaderByte.length);
            headerByteCount += headerDataSize + 3;
            fieldIndex++;
        }

        return bytes;
    }
    
   

}
