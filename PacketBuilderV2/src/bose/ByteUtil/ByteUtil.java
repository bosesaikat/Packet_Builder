/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package bose.ByteUtil;

import bose.AttributeInfo.AttributesInfo;
import java.util.Arrays;

/**
 *
 * @author saikat
 */
public class ByteUtil {

    public static byte[] getBytes(int attrId, int noOfDataBytes, byte values[], Class type){
        
        byte[] bytes = null;
        
       /* for(int i = 0 ; i< values.length; i++)
            System.out.println("byte "+values[i]);*/
        
        if(isByte(type)){
            
           bytes = getAttributeHeader(attrId, noOfDataBytes, bytes, type);
           System.arraycopy(values, 0, bytes, 4, values.length);
        }
       // System.out.println(bytes.length);
        return bytes;
    }
    
    public static byte[] getBytes(int attrId , int noOfDataBytes ,  Object value, Class type) {
        
//        int startIndex = 0;
      /*  AttributesInfo attribute = AttributeInfoProvider.getInstance().getAttributeByName(name);
        if(attribute == null){
            System.out.println("ByteUtil: " + name + " is not found.");
            return null;
        }*/
      
        //int attrId = attribute.getID();
        //int noOfDataBytes = attribute.getSize();
        
        
//        int noOfHeaderBytes = 2;
//        int noOfDataLengthBytes = 1;
        
        
        byte[] bytes = null;
//        byte[] bytes = new byte[ noOfHeaderBytes + noOfDataLengthBytes + noOfDataBytes ];
//        
//        //add attribute header bytes
//        bytes[ startIndex ++ ] = (byte) (attrId >> 8);
//        bytes[ startIndex ++ ] = (byte) (attrId);
//        bytes[ startIndex ++ ] = (byte) noOfDataBytes;
        
        if (isInteger(type)) {
//            System.out.println("Integer/Long: " + name);
//            System.out.println("Name: " + name);
//            System.out.println("val: " + value);
//            int noOfTotalBytes = noOfHeaderBytes + noOfDataLengthBytes + noOfDataBytes;
            bytes = getAttributeHeader(attrId, noOfDataBytes, bytes, type);
            decimalToBytes((int)value, noOfDataBytes, bytes);
            
        } else if (isLong(type)) {
//            System.out.println("Integer/Long: " + name);
//            System.out.println("Name: " + name);
//            System.out.println("val: " + value);
//            int noOfTotalBytes = noOfHeaderBytes + noOfDataLengthBytes + noOfDataBytes;
            bytes = getAttributeHeader(attrId, noOfDataBytes, bytes, type);
            decimalToBytes((long)value, noOfDataBytes, bytes);
            
        } else if (isString(type)) {
//            System.out.println("String: " + type);
            String data = (String)value;
          //  System.out.println("String byte len "+ data.getBytes().length);
           // noOfDataBytes = data.getBytes().length;
           // System.out.println("no of data bytes "+ noOfDataBytes);
            bytes = getAttributeHeader(attrId, noOfDataBytes, bytes, type);
            
          /*  for( byte b : bytes)
                System.out.print(b + " ");
          */  
            
            stringToBytes(data, bytes);
            
        } else if (isByte(type)) {
            //noOfDataBytes = 1;
            bytes = getAttributeHeader(attrId, noOfDataBytes, bytes, type);
           // System.out.println("Byte: " + type);
            
        } else if (isBoolean(type)) {
//            System.out.println("Boolean: " + type);
//            System.out.println("val: " + (boolean)value);
            noOfDataBytes = 1;
            bytes = getAttributeHeader(attrId, noOfDataBytes, bytes, type);
            booleanToBytes((boolean)value, bytes);
            
        } else if (isCharacter(type)) {
//            System.out.println("Character: " + type);
            noOfDataBytes = 1;
            bytes = getAttributeHeader(attrId, noOfDataBytes, bytes, type);
            stringToBytes(Character.toString((char)value), bytes);
            
        } else if (isFloat(type)) {
//            System.out.println("Float/Double: " + type);
            float temp = (float) value;
            String data = String.valueOf(temp);
            noOfDataBytes = data.length();
            bytes = getAttributeHeader(attrId, noOfDataBytes, bytes, type);
            
            stringToBytes(data, bytes);
            
        } else if (isDouble(type)) {
            
            double temp = (double) value;
            String data = String.valueOf(temp);
            noOfDataBytes = data.length();
            bytes = getAttributeHeader(attrId, noOfDataBytes, bytes, type);
            
            stringToBytes(data, bytes);
            
            //System.out.println("Float/Double: " + type);
        } else if (type.isArray()) {
            System.out.println("Array: " + type);
//                System.out.println("Array type: " + ((ParameterizedType) attr.getGenericType()).getActualTypeArguments()[0]);
//                System.out.println("Array type: " + attr.getClass());
            Class cl = type.getComponentType();
            if (cl.isPrimitive()) {
            }

        } /*else if (type.equals(List.class) || type.equals(ArrayList.class)) {
            System.out.println("List: " + type);
            //System.out.println("List type: " + ((ParameterizedType) attr.getGenericType()).getActualTypeArguments()[0]);
        }*/ else {
            System.out.println("Other: " + type);
        }
        
        
        
        return bytes;
    }

    public static byte[] getAttributeHeader(int attrId, int dataLength, byte[] bytes, Class type){
      //  System.out.println("attrId "+ attrId + " dataLen " + dataLength);
        int noOfHeaderBytes = 2;
        int noOfDataLengthBytes;
        
        if( dataLength >= 256 || isByte(type) )
            noOfDataLengthBytes = 2;
        else
            noOfDataLengthBytes = 1;

        
        int noOfTotalBytes = noOfHeaderBytes + dataLength + noOfDataLengthBytes;
        bytes = new byte[ noOfTotalBytes ];
        int startIndex = 0;
        //add attribute header bytes
        bytes[ startIndex ++ ] = (byte) (attrId >> 8);
        bytes[ startIndex ++ ] = (byte) (attrId);
        
        if(dataLength >= 256 || isByte(type) ) {
            bytes[ startIndex ++] = (byte) ((dataLength >> 8) & 0xff);
            bytes[ startIndex ++ ] = (byte) ((dataLength >> 0) & 0xff);
          //  System.out.println(bytes[startIndex-2] + " "+ bytes[startIndex-1]);
           // System.out.println( (bytes[startIndex-2] & 0xff) << 8 | (bytes[startIndex-1] & 0xff) );
        }
        
        else            
            bytes[ startIndex ++] = (byte) (dataLength);
       
        //System.out.println(bytes[startIndex-1]);
        
        return bytes;
    }
    
    public static boolean isInteger(Class clz) {
        return clz.equals(Integer.class)
                || clz.equals(int.class);
    }
    public static boolean isLong(Class clz) {
        return clz.equals(Long.class)
                || clz.equals(long.class);
    }

    public static boolean isFloat(Class clz) {
        return clz.equals(Float.class)
                || clz.equals(float.class);
    }
    public static boolean isDouble(Class clz) {
        return clz.equals(Double.class)
                || clz.equals(double.class);
    }

    public static boolean isCharacter(Class clz) {
        return clz.equals(Character.class)
                || clz.equals(char.class);
    }

    public static boolean isString(Class clz) {
        return clz.equals(String.class);
    }

    public static boolean isBoolean(Class clz) {
        return clz.equals(Boolean.class)
                || clz.equals(boolean.class);
    }

    public static boolean isByte(Class clz) {
        return clz.equals(Byte.class)
                || clz.equals(byte.class);
    }
    
     
    public static boolean isByteArray(Class clz) {
        return clz.equals( Byte[].class )
                ||clz.equals(byte[].class);
    }
     
    public static void decimalToBytes(long value, int length, byte[] dest_bytes) {
        int startIndex = 3;
        int noOfByteShift = (length - 1) * 8;

        if(noOfByteShift < 0){
            return;
        }
        
        for(int i = noOfByteShift; i >= 0; i -= 8){
            dest_bytes [ startIndex ++ ] = (byte) (value >> noOfByteShift);
            noOfByteShift -= 8;
        }
    }
    
    public static void fractionToBytes(long value, int length, byte[] dest_bytes) {
        int startIndex = 3;
        int noOfByteShift = (length - 1) * 8;

        if(noOfByteShift < 0){
            return;
        }
        
        for(int i = noOfByteShift; i >= 0; i -= 8){
            dest_bytes [ startIndex ++ ] = (byte) (value >> noOfByteShift);
            noOfByteShift -= 8;
        }
    }
    
    public static void stringToBytes(String value, byte[] dest_bytes) {
    
        int startIndex ;
        byte[] str_bytes = value.getBytes();
        int length = str_bytes.length;
        
        startIndex = (length < 255 ) ? 3 : 4; 
            
        //dest_bytes[ startIndex ] = (byte) length;
       //System.out.println("source byte array len " +str_bytes.length);
      // System.out.println("destination byte array len "+dest_bytes.length);

        System.arraycopy(str_bytes, 0, dest_bytes, startIndex, length);
        
    }
    
    public static void booleanToBytes(boolean value, byte[] dest_bytes) {
    
        int startIndex = 3;
        dest_bytes[ startIndex ] = (byte)(value == true ? 1 : 0);
    }

   

}