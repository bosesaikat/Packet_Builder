/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package bose.PacketBuilderV2;

import bose.AttributeInfo.AttributesInfo;
import bose.ByteUtil.ByteUtil;
import bose.Header.Header;
import java.util.ArrayList;
import java.util.Date;

/**
 *
 * @author saikat
 */
public class PacketBuilderV2 {
    
    private Header header ;
    private ArrayList<AttributesInfo> attributes;
    private ByteUtil byteUtil;
    private static final int LIM = 128000;
    private static final int PACKET_HEADER_SIZE = 16;
    private ArrayList<byte[]> packets;
    
    public PacketBuilderV2(Header header, ArrayList<AttributesInfo> attributes) {
        
        this.header = header;
        this.attributes = attributes;
        //this.byteUtil = new ByteUtil();
        
      
    }
    
    public void addAttributeInByte(){
        
       this.packets = new ArrayList<byte[]>();
       
       byte[] bytes = new byte[LIM - PACKET_HEADER_SIZE];
       
       ArrayList<Integer> largeAttributesIndex = new ArrayList<Integer>();
       //System.out.println(largeAttributes);
       int countByte = 0;
       for(int i=0 ; i<this.attributes.size() ; i++){
           
           int attrId = this.attributes.get(i).getID();
         //  String name = attributes.get(i).getName();
           Object value = this.attributes.get(i).getData();
           Class type = this.attributes.get(i).getType();
           int size = this.attributes.get(i).getSize();
           if(size>255 || ByteUtil.isByte(type)){
               largeAttributesIndex.add(i);
           }
           else {
               int noOfDataByte = size + 3;
           
               byte[] currentByte = ByteUtil.getBytes(attrId, size , value, type);
          
               System.arraycopy(currentByte, 0, bytes, countByte, currentByte.length);
               countByte += noOfDataByte;
           }
      
          
       }
      
    
       
       for(int i = 0 ; i< largeAttributesIndex.size() ; i++){
           
           byte[] currrentByte = null;
           //int attrId = attributes.get(i).getID();
           AttributesInfo attr = this.attributes.get( largeAttributesIndex.get(i) );
           int attrId = attr.getID();
         //  String name = attributes.get(i).getName();
           Class type = attr.getType();
           System.out.println("\n"+type);
           
           if( ByteUtil.isByte(type) ){
               
               byte[] values = attr.getDataArray();
              // for(int j = 0 ; j< values.length; j++)
                   // System.out.println("byte "+values[j]);
               
               /*for(byte b : values){
                   System.out.print(b+" ");
               }*/
               System.out.println("");
               
               while(values.length!= 0){
                   
                   //Object[] values = attributes.get(i).getDataArray();
                   int length = values.length;
                   int endIndex = this.endIndex(countByte, length);
                   //System.out.println("End Index "+ endIndex);
                   byte[] tempValues = new byte[endIndex];
                   System.arraycopy(values, 0, tempValues, 0, endIndex );
                   //currrentByte = null;
                   currrentByte = ByteUtil.getBytes(attrId, endIndex, tempValues, type);
                   System.out.println(currrentByte.length);
                   for(int j = 0 ; j <currrentByte.length; j++)
                       System.out.println(currrentByte[j]);
                   int noOfDataByte = endIndex + 4;
                   
                   System.arraycopy(currrentByte , 0, bytes, countByte, currrentByte.length);
                   countByte += noOfDataByte;
                   System.out.println("Count Bytes "+ countByte);
                   if(countByte == LIM - PACKET_HEADER_SIZE){
                      // packets[packetIndex++][0] = bytes;
                      packets.add(bytes);
                      countByte = 0;
                   }
                   
                   
                   tempValues = new byte[length-endIndex];
                   System.arraycopy(values, endIndex, tempValues, 0, values.length-endIndex);
                   values = tempValues;
                   
               }
           }    
           
           else if(ByteUtil.isString(type)){
               
               Object value = attr.getData();
               String strValue  = String.valueOf(value);
               
               while(strValue.length()!= 0 || strValue.equals(null)){
                   
                   
                   int length = strValue.length();
                   int endIndex = this.endIndex(countByte, length);
                   
                   String tempString = "";
                   tempString = strValue.substring(0, endIndex);
                   System.out.println(tempString);
                   
                   currrentByte = ByteUtil.getBytes(attrId, endIndex, tempString, type);
                   int noOfDataByte = endIndex + 4;
                   
                   System.arraycopy(currrentByte , 0, bytes, countByte, currrentByte.length);
                   countByte += noOfDataByte;
                   
                   if(countByte == LIM - PACKET_HEADER_SIZE){
                   
                      packets.add(bytes);
                      countByte = 0;
                      bytes = null;
                   }
                   
                   strValue = strValue.substring(endIndex, strValue.length());
                   
                  
                   
               }
               
           }
           
           
       }
       
       packets.add(bytes);
       System.out.println("packet size "+packets.size());
      for(int i = 0 ; i < countByte ; i++ ){
           System.out.print(bytes[i]+" ");
       }
             
    }
    
    
    private int endIndex(int currentDataSizeByByte, int length) {
		// TODO Auto-generated method stub
		if(currentDataSizeByByte<length)
			return currentDataSizeByByte-3;
		else 
			return length;
    }


   /* private ArrayList<AttributesInfo> excludeLargeAttributes(ArrayList<AttributesInfo> attributes) {
        //throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
        ArrayList<AttributesInfo> largeAttributes = new ArrayList<AttributesInfo>();
        int size = attributes.size();
        for(int i=0;i<size;i++){
            
            AttributesInfo currentAttribute = attributes.get(i);
            //Object value = currentAttribute.getData();
            Class type  = currentAttribute.getType(); 
            System.out.println(currentAttribute + " " +  type);
            
           if( ByteUtil.isByteArray(type ) ){
           //if( currentAttribute.getData() instanceof Integer.class)
                largeAttributes.add( currentAttribute );
                attributes.remove( currentAttribute );
               // System.out.println(currentAttribute + " " + currentAttribute.getClass());
                size--;
            }
            
            else if( ByteUtil.isString( currentAttribute.getType() ) && currentAttribute.getSize() > 255){
                largeAttributes.add( currentAttribute );
                attributes.remove( currentAttribute );
                size--;
            }
        }
        
        return largeAttributes;
    }*/
}
