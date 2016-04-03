/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package bose.PacketDeserialize;

/**
 *
 * @author saikat
 */
public class ByteToDataConverter {
    
    public static int getInteger(byte[] byteData , int index, int length) {
        
        try{
         int val=0;
      
         for(int i=1 ; i<=length ; i++){
          //   System.out.println( byteData[index] );
          
             val = val | ( ( byteData[index++] & 0xff ) << (length-i)*8 );
         }
         return val;   
        }
        catch(Exception e){
            e.printStackTrace();
        }
        return 0;
    }
    
        
    public static long getLong(byte[] byteData , int index, int length){
        
        long val=0;
      
        for(int i=1;i<=length;i++){
            
            val = val | ( ( byteData[index++] & 0xffL ) << (length-i)*8 );
        }
        return val;
    }
    
        
    public static String getString(byte[] byteData , int index, int length){
       
        byte[] str_bytes = new byte[length];
        
        System.arraycopy(byteData, index, str_bytes, 0, length);

        return new String(str_bytes);
    }
    
        
    public static boolean getBoolean(byte[] byteData , int index, int length){
        
        return  (( byteData[index++] & 0x01)!=0);
    }
    
        
    public static double getDouble(byte[] byteData , int index, int length){
        
        String strValue = getString(byteData, index, length);
        return Double.parseDouble(strValue);
        
    }
    
        
    public static byte[] getByte(byte[] byteData , int index, int length){
        
        byte[] data = new byte[length];
        System.arraycopy(byteData, index, data, 0, length);
        return  data;
    }
    
        
    public static float getFloat(byte[] byteData , int index, int length){
        return 0;
    }
}
