/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package bose.PacketBuilderV2;

import bose.AttributeInfo.AttributesInfo;
import bose.ByteUtil.ByteUtil;
import bose.Header.Header;
import bose.Header.HeaderCode;
import com.sun.javafx.font.freetype.HBGlyphLayout;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
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

    //  private int PACKET_HEADER_SIZE = Header.PACKET_HEADER_SIZE;
    public static final int PACKET_SIZE = 9000;
    public static int PACKET_HEADER_SIZE;

    public ArrayList<byte[]> packets;

    public PacketBuilderV2(ArrayList<AttributesInfo> attributes) {

        this.attributes = attributes;
        //this.byteUtil = new ByteUtil();
        this.packets = new ArrayList<byte[]>();

        setHeader();

    }

    private void setHeader() {

        this.header = new Header();
        this.header.setSessionID("A07XE");
        this.header.setPacketID(1);
        this.header.setServerPacketID(1);
        this.header.setActionID(1);
        this.header.setAcknowledgement(10L);
        this.header.setDataLength(0);
       // this.header.setSequenceNo(0);

    }

    public void addAttributeInByte() throws IllegalArgumentException, IllegalAccessException, NoSuchMethodException, InvocationTargetException {

        dataLimitCalc();
        
        System.out.println("Header Size " + PACKET_HEADER_SIZE);

        int sequenceNo = 0;
        
        final int LIM = PACKET_SIZE - PACKET_HEADER_SIZE;

        byte[] bytes = new byte[LIM];

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

                if (countByte == LIM) {
                    // packets[packetIndex++][0] = bytes;
                    packets.add(bytes);
                    countByte = 0;
                    bytes = new byte[LIM];
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
            while ((startIndex < values.length)) {

                //Object[] values = attributes.get(i).getDataArray();
                int remainingBytes = values.length - startIndex;

                int endIndex = endIndex(LIM - countByte - 1, remainingBytes);
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

                if (countByte == LIM) {
                    // packets[packetIndex++][0] = bytes;
                    this.packets.add(bytes);
                    countByte = 0;
                    bytes = new byte[LIM];
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
        return (currentDataSizeByByte < length) ? currentDataSizeByByte - 3 : length;
    }

    public ArrayList<byte[]> getByte() throws IllegalArgumentException, IllegalAccessException, NoSuchMethodException, InvocationTargetException {

        for (int i = 0; i < this.packets.size(); i++) {

            int headerByteCount = 0;

            byte[] attrByte = this.packets.get(i);
            int totalDataByteSize = this.packets.get(i).length;
            int totalCurrentPacketSize = totalDataByteSize + PACKET_HEADER_SIZE;

            byte[] bytes = new byte[PACKET_HEADER_SIZE];
            byte[] packetBytes = new byte[totalCurrentPacketSize];

            Field[] fields = header.getClass().getDeclaredFields();

            
            int fieldIndex = 0;    
            for (Field field : fields) {

                Class cls = (Class) field.getType();
                int headerDataSize = 0;
                Object headerValue = 0;
                int headerCode = 0;
                String methodNameEnd = field.getName().replaceFirst( 
                  String.valueOf( field.getName().charAt(0) ), String.valueOf( field.getName().toUpperCase().charAt(0) ) );
          
                Method m = header.getClass().getDeclaredMethod("get" + methodNameEnd );
                
                if (ByteUtil.isInteger(cls)) {
                    //  System.out.println("hit");
                    if (field.getName().equalsIgnoreCase("dataLength")) {
                        
                        headerDataSize = 2;
                        headerValue = totalDataByteSize;
                    
                    } else {
                    
                        headerDataSize = 4;
                        headerValue = m.invoke(header);
                    
                    }
                } else if (ByteUtil.isBoolean(cls)) {
                    
                    headerDataSize = 1;
                    headerValue = m.invoke(header);
                
                } else if (ByteUtil.isByte(cls)) {
                
                    headerDataSize = 1;
                    headerValue = m.invoke(header);
                    
                } else if (ByteUtil.isCharacter(cls)) {
                
                    headerDataSize = 1;
                    headerValue = m.invoke(header);
                    
                } else if (ByteUtil.isString(cls)) {
                    
                    headerDataSize = m.invoke(header).toString().getBytes().length;
                    headerValue = m.invoke(header);
                    
                } else if (ByteUtil.isDouble(cls)) {
                    
                    headerDataSize = String.valueOf( m.invoke(header) ).getBytes().length;
                    headerValue = m.invoke(header);
                    
                } else if (ByteUtil.isFloat(cls)) {
                    
                    headerDataSize = String.valueOf( m.invoke(header) ).length();
                    headerValue = m.invoke(header);
                    
                } else if (ByteUtil.isLong(cls)) {
                    
                    headerDataSize = 8;
                    headerValue = m.invoke(header);
                }
                
                switch(fieldIndex){
                    
                    case HeaderCode.SESSION_ID:
                        headerCode = HeaderCode.SESSION_ID;
                        break;
                    case HeaderCode.PACKET_ID:
                        headerCode = HeaderCode.PACKET_ID;
                        break;
                    case HeaderCode.SERVER_PACKET_ID:{
                        
                        headerCode = HeaderCode.SERVER_PACKET_ID ;
                        headerValue = (int)headerValue + i;
                        break;
                    }
                        
                    case HeaderCode.ACTION_ID:
                        headerCode = HeaderCode.ACTION_ID;
                        break;
                    case HeaderCode.ACKNOWLEDGEMENT:
                        headerCode = HeaderCode.ACKNOWLEDGEMENT;
                        break;
                    case HeaderCode.DATA:
                        headerCode = HeaderCode.DATA;
                        break;
                }
                
               // System.out.println(headerCode + " " + headerDataSize + " " + headerValue + " " + cls);
                byte[] currentHeaderByte = ByteUtil.getBytes(headerCode, headerDataSize, headerValue, cls);
               // System.out.println(currentHeaderByte.length + " " + bytes.length + " " + headerByteCount);
                System.arraycopy(currentHeaderByte, 0, bytes, headerByteCount, currentHeaderByte.length);
                headerByteCount += headerDataSize + 3;
                fieldIndex ++;
            }

           /* int sessionId = this.header.getSessionID();
            byte[] currentHeaderByte = ByteUtil.getBytes(1, headerDataSize, sessionId, Integer.class);
            System.arraycopy(currentHeaderByte, 0, bytes, headerByteCount, currentHeaderByte.length);
            headerByteCount += headerDataSize + 3;

            int packetId = this.header.getPacketID();
            currentHeaderByte = ByteUtil.getBytes(2, headerDataSize, packetId, Integer.class);
            System.arraycopy(currentHeaderByte, 0, bytes, headerByteCount, currentHeaderByte.length);
            headerByteCount += headerDataSize + 3;

            int serverPacketId = this.header.getServerPacketID() + i;
            currentHeaderByte = ByteUtil.getBytes(3, headerDataSize, serverPacketId, Integer.class);
            System.arraycopy(currentHeaderByte, 0, bytes, headerByteCount, currentHeaderByte.length);
            headerByteCount += headerDataSize + 3;

            int actionId = this.header.getActionID();
            currentHeaderByte = ByteUtil.getBytes(4, headerDataSize, actionId, Integer.class);
            System.arraycopy(currentHeaderByte, 0, bytes, headerByteCount, currentHeaderByte.length);
            headerByteCount += headerDataSize + 3;

            currentHeaderByte = ByteUtil.getBytes(5, 2, totalDataByteSize, Integer.class);
            System.arraycopy(currentHeaderByte, 0, bytes, headerByteCount, currentHeaderByte.length);
            headerByteCount += 2 + 3;
            */
            System.arraycopy(bytes, 0, packetBytes, 0, bytes.length);

            //packetBytes[headerByteCount++] = (byte) (totalDataByteSize >> 8);
            //packetBytes[headerByteCount++] = (byte) (totalDataByteSize >> 0);
            System.arraycopy(attrByte, 0, packetBytes, headerByteCount, attrByte.length);

            this.packets.set(i, packetBytes);

        }

        return this.packets;
    }

    private void dataLimitCalc() throws IllegalArgumentException, IllegalAccessException, NoSuchMethodException, InvocationTargetException {
        //throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.

        Field[] headerFields = header.getClass().getDeclaredFields();
       // Method[] methods = header.getClass().getMethods();
        int headerBytes = 0;

        for (Field field : headerFields) {

            Class cls = (Class) field.getAnnotatedType().getType();
            //  System.out.println(cls);

            int headerCodeByte = 2;
            int headerValueLength = 1;
            
            String methodNameEnd = field.getName().replaceFirst( 
                  String.valueOf( field.getName().charAt(0) ), String.valueOf( field.getName().toUpperCase().charAt(0) ) );
          
            Method m = header.getClass().getDeclaredMethod("get" + methodNameEnd );
         // System.err.println( m.invoke(header));

            if (ByteUtil.isInteger(cls)) {
                //  System.out.println("hit");
                if (field.getName().equalsIgnoreCase("dataLength")) {
                   
                    headerBytes += 2;
                    
                }
                else {
                    
                    headerBytes += 4;
                }
            } else if (ByteUtil.isBoolean(cls)) {
                
                headerBytes += 1;
                
            } else if (ByteUtil.isByte(cls)) {
                
                headerBytes += 1;
                
            } else if (ByteUtil.isCharacter(cls)) {
                
                headerBytes += 1;
                
            } else if (ByteUtil.isString(cls)) {
                
                String value = (String) m.invoke(header);
                headerBytes += value.getBytes().length ;
                //headerBytes += field.get(header).toString().getBytes().length;
            } else if (ByteUtil.isDouble(cls)) {
                
                Double value = (Double)m.invoke(header);
                headerBytes += String.valueOf( value ).getBytes().length;
                
            } else if (ByteUtil.isFloat(cls)) {
                
                float value = (float)m.invoke(header);
                headerBytes += String.valueOf( value ).length();
                
            } else if (ByteUtil.isLong(cls)) {
                
                headerBytes += 8;
            }

            //System.out.println( headerBytes );
            headerBytes += headerCodeByte + headerValueLength;

        }

        PACKET_HEADER_SIZE = headerBytes;
    }
}
