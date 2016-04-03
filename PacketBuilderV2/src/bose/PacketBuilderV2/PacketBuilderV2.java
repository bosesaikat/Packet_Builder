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

    private Header header;
    private ArrayList<AttributesInfo> attributes;
    private ByteUtil byteUtil;
    
    private final int PACKET_HEADER_SIZE = Header.PACKET_HEADER_SIZE;
    private final int LIM = Header.PACKET_SIZE - PACKET_HEADER_SIZE ;
    public ArrayList<byte[]> packets;

    public PacketBuilderV2(ArrayList<AttributesInfo> attributes) {

        this.attributes = attributes;
        //this.byteUtil = new ByteUtil();
        this.packets = new ArrayList<byte[]>();
        
        setHeader();

    }

    private void setHeader() {
        this.header = new Header();
        this.header.setSessionID(1);
        this.header.setPacketID(1);
        this.header.setServerPacketID(1);
        this.header.setActionID(1);

    }

    public void addAttributeInByte() {

        

        byte[] bytes = new byte[ LIM ];

        ArrayList<Integer> largeAttributesIndex = new ArrayList<Integer>();
        //System.out.println(largeAttributes);
        int countByte = 0;
        for (int i = 0; i < this.attributes.size(); i++) {

            int attrId = this.attributes.get(i).getID();
            //  String name = attributes.get(i).getName();
            Object value = this.attributes.get(i).getData();
            Class type = this.attributes.get(i).getType();
            int size = this.attributes.get(i).getSize();
            
            if (size > 255 || ByteUtil.isByte(type)) {
                largeAttributesIndex.add(i);
            } else {
                int noOfDataByte = size + 3;

                byte[] currentByte = ByteUtil.getBytes(attrId, size, value, type);

                System.arraycopy(currentByte, 0, bytes, countByte, currentByte.length);

                if (countByte == LIM ) {
                    // packets[packetIndex++][0] = bytes;
                    packets.add(bytes);
                    countByte = 0;
                    bytes = new byte[LIM] ;
                   // bytes = null;
                } else {
                    countByte += noOfDataByte;
                }
            }

        }
        
      //  System.err.println("count byte before large attributes " + countByte);

        for (int i = 0; i < largeAttributesIndex.size(); i++) {

            byte[] currrentByte = null;
            //int attrId = attributes.get(i).getID();
            AttributesInfo attr = this.attributes.get(largeAttributesIndex.get(i));
            int attrId = attr.getID();
            //  String name = attributes.get(i).getName();
            Class type = attr.getType();
            //  System.out.println("\n"+type);
            byte[] values = null;

            if (ByteUtil.isByte(type)) {

                values = attr.getDataArray();
            } else if (ByteUtil.isString(type)) {

                Object value = attr.getData();
                String strValue = String.valueOf(value);
                values = strValue.getBytes();

            }
            
            int startIndex = 0;
            
          //  System.out.println("Values len " + values.length );
            
            while ( ( startIndex < values.length ) ) {

                //Object[] values = attributes.get(i).getDataArray();
                int remainingBytes = values.length - startIndex;
                
                int endIndex = endIndex( LIM - countByte -1 , remainingBytes );
              //  System.out.println("Start Index " + startIndex + " End Index "+ endIndex);
                byte[] tempValues = new byte[endIndex];
                System.arraycopy(values, startIndex, tempValues, 0, endIndex);
                //currrentByte = null;
                currrentByte = ByteUtil.getBytes(attrId, endIndex, tempValues, byte.class);
              //  System.out.println(currrentByte.length);
                // for(int j = 0 ; j <currrentByte.length; j++)
                //     System.out.println(currrentByte[j]);
                int noOfDataByte = endIndex + 4;
               // System.out.println("count byte " + countByte);
              //  System.out.println("current byte len "+ currrentByte.length + " bytes len " + bytes.length + " count byte " + countByte);
                System.arraycopy(currrentByte, 0, bytes, countByte, currrentByte.length);
                countByte += noOfDataByte;
               // System.err.println("Count Bytes "+ countByte);
                
               
                
                if (countByte == LIM ) {
                    // packets[packetIndex++][0] = bytes;
                    this.packets.add(bytes);
                    countByte = 0;
                    bytes = new byte[LIM] ;
                }

                startIndex += endIndex;
                    

            }

        }

        //   System.out.println("Count Bytes " + countByte );
    
        
        byte[] actualBytes = new byte[countByte];
        System.arraycopy(bytes, 0, actualBytes, 0, countByte);
        this.packets.add(actualBytes);

     //   ;

    }

    private int endIndex(int currentDataSizeByByte, int length) {
        // TODO Auto-generated method stub
        if (currentDataSizeByByte < length) {
            return currentDataSizeByByte - 3;
        } else {
            return length;
        }
    }

    public ArrayList<byte[]> getByte() {

        
        for (int i = 0; i < this.packets.size(); i++) {
            
            int headerByteCount = 0;
            
            byte[] attrByte = this.packets.get(i);
            int totalDataByteSize = this.packets.get(i).length;
            int totalCurrentPacketSize = totalDataByteSize + PACKET_HEADER_SIZE;

            byte[] bytes = new byte[PACKET_HEADER_SIZE];
            byte[] packetBytes = new byte[totalCurrentPacketSize];

            int sessionId = this.header.getSessionID();
            int packetId = this.header.getPacketID() + i ;
            int serverPacketId = this.header.getServerPacketID() + i;
            int actionId = this.header.getActionID();

            int headerDataSize = 4;

            byte[] currentHeaderByte = ByteUtil.getBytes(1, headerDataSize, sessionId, Integer.class);
        //    System.out.println("current header byte len "+ currentHeaderByte.length + " bytes len " + bytes.length + " count byte " + headerByteCount);
            System.arraycopy(currentHeaderByte, 0, bytes, headerByteCount, currentHeaderByte.length);
            headerByteCount += headerDataSize + 3;

            currentHeaderByte = ByteUtil.getBytes(2, headerDataSize, packetId, Integer.class);
            System.arraycopy(currentHeaderByte, 0, bytes, headerByteCount, currentHeaderByte.length);
            headerByteCount += headerDataSize + 3;

            currentHeaderByte = ByteUtil.getBytes(3, headerDataSize, serverPacketId, Integer.class);
            System.arraycopy(currentHeaderByte, 0, bytes, headerByteCount, currentHeaderByte.length);
            headerByteCount += headerDataSize + 3;

            currentHeaderByte = ByteUtil.getBytes(4, headerDataSize, actionId, Integer.class);
            System.arraycopy(currentHeaderByte, 0, bytes, headerByteCount, currentHeaderByte.length);
            headerByteCount += headerDataSize + 3;

            System.arraycopy(bytes, 0, packetBytes, 0, bytes.length);
            System.arraycopy(attrByte, 0, packetBytes, headerByteCount, attrByte.length);

            this.packets.set(i, packetBytes);

        }

        return this.packets;
    }
}
