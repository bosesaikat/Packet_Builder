package org.ipvision.byteBuilder;

import org.ipvision.attribute.code.Code;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Queue;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
/**
 *
 * @author saikat
 */
public class ByteBuilder {

    /**
     * byte size of the packet
     */
    public static int PACKET_SIZE_MAX = 2000;

    /**
     * packet header size
     */
    public static int HEADER_SIZE;

    /**
     * broken packet header size
     */
    public int SUB_HEADER_SIZE = 8;

    /**
     * No of bytes to store data size
     */
    public static int CURRENT_PACKET_DATA_LENGTH_SIZE = 2;

    /**
     * header size + broken packet header size + data size store
     */
    public static int TOTAL_HEADER_SIZE;

    public static int CHECK_BYTE_SIZE = 1;

    public int PACKET_SIZE = PACKET_SIZE_MAX - SUB_HEADER_SIZE - CHECK_BYTE_SIZE;

    byte[] header = null;

    byte[] send_bytes = null;

    private Queue<byte[]> packets;

    /**
     * header start index
     *
     */
    int index = CURRENT_PACKET_DATA_LENGTH_SIZE;

    public ByteBuilder() {
        this.packets = new LinkedList<byte[]>() ;
    }

    public ByteBuilder setHeader(byte[] header) {

        this.header = header;

        HEADER_SIZE = header.length;

        send_bytes = new byte[PACKET_SIZE];
        index = addHeader(this.header, send_bytes);

        return this;
    }

    public int getHeaderSize() {

        return HEADER_SIZE;

    }

    public ByteBuilder addInt(int code, int value, int size) {
        /**
         * 2 bytes attribute code + 1 byte length
         */
        int attributeLength = 3 + size;

        if (index + attributeLength >= PACKET_SIZE) {

            int dataLength = index - CURRENT_PACKET_DATA_LENGTH_SIZE;

            index = addTotalDatabytesLength(dataLength, send_bytes);

            packets.add(send_bytes);

            index = CURRENT_PACKET_DATA_LENGTH_SIZE;
            send_bytes = new byte[PACKET_SIZE];
            index = addHeader(this.header, send_bytes);
        }

        index = ByteBuilder.intToByte(code, value, size, send_bytes, index);
        return this;
    }

    public ByteBuilder addLong(int code, long value, int size) {
        /**
         * 2 bytes attribute code + 1 byte length
         */
        int attributeLength = 3 + size;

        if (index + attributeLength >= PACKET_SIZE) {

            int dataLength = index - CURRENT_PACKET_DATA_LENGTH_SIZE;

            index = addTotalDatabytesLength(dataLength, send_bytes);

            packets.add(send_bytes);

            index = CURRENT_PACKET_DATA_LENGTH_SIZE;
            send_bytes = new byte[PACKET_SIZE];
            index = addHeader(this.header, send_bytes);
        }

        index = ByteBuilder.longToByte(code, value, size, send_bytes, index);
        return this;
    }

    public static int addTotalDatabytesLength(int dataLength, byte[] send_bytes) {

        int i = 0;

       // send_bytes[i++] = (byte) (Code.DATA >> 8);
       // send_bytes[i++] = (byte) (Code.DATA);

       // send_bytes[i++] = (byte) CURRENT_PACKET_DATA_LENGTH_SIZE;

        send_bytes[i++] = (byte) (dataLength >> 8);
        send_bytes[i++] = (byte) (dataLength);

        return i;
    }

    public ByteBuilder addBool(int code, boolean value) {

        int attributeLength = 4;

        if (index + attributeLength >= PACKET_SIZE) {

            int dataLength = index - CURRENT_PACKET_DATA_LENGTH_SIZE;

            index = addTotalDatabytesLength(dataLength, send_bytes);

            packets.add(send_bytes);

            index = CURRENT_PACKET_DATA_LENGTH_SIZE;

            send_bytes = new byte[PACKET_SIZE];
            index = addHeader(this.header, send_bytes);
        }

        index = ByteBuilder.boolToByte(code, value, send_bytes, index);
        return this;
    }

    public ByteBuilder addDouble(int code, double value) {

        int attributeLength = 3 + String.valueOf(value).getBytes().length;

        if (index + attributeLength >= PACKET_SIZE) {

            int dataLength = index - CURRENT_PACKET_DATA_LENGTH_SIZE;

            index = addTotalDatabytesLength(dataLength, send_bytes);

            packets.add(send_bytes);

            index = CURRENT_PACKET_DATA_LENGTH_SIZE;
            send_bytes = new byte[PACKET_SIZE];
            index = addHeader(this.header, send_bytes);
        }

        String strValue = String.valueOf(value);
        index = ByteBuilder.stringToByte(code, strValue, send_bytes, index);
        return this;
    }

    public ByteBuilder addFloat(int code, float value) {

        int attributeLength = 3 + String.valueOf(value).getBytes().length;

        if (index + attributeLength >= PACKET_SIZE) {

            int dataLength = index - CURRENT_PACKET_DATA_LENGTH_SIZE;

            index = addTotalDatabytesLength(dataLength, send_bytes);

            packets.add(send_bytes);

            index = CURRENT_PACKET_DATA_LENGTH_SIZE;
            send_bytes = new byte[PACKET_SIZE];
            index = addHeader(this.header, send_bytes);
        }

        String strValue = String.valueOf(value);
        index = ByteBuilder.stringToByte(code, strValue, send_bytes, index);
        return this;
    }

    public ByteBuilder addString(int code, String value) {

        int attributeLength = 3 + value.getBytes().length;

        if (index + attributeLength >= PACKET_SIZE) {

            int dataLength = index - CURRENT_PACKET_DATA_LENGTH_SIZE;

            index = addTotalDatabytesLength(dataLength, send_bytes);

            packets.add(send_bytes);

            index = CURRENT_PACKET_DATA_LENGTH_SIZE;
            send_bytes = new byte[PACKET_SIZE];
            index = addHeader(this.header, send_bytes);
        }

        index = ByteBuilder.stringToByte(code, value, send_bytes, index);
        return this;
    }

    public ByteBuilder addBigString(int code, String value) {

        int attributeLength = 3 + value.getBytes().length;

        if (index + attributeLength >= PACKET_SIZE) {

            int dataLength = index - CURRENT_PACKET_DATA_LENGTH_SIZE;

            index = addTotalDatabytesLength(dataLength, send_bytes);

            packets.add(send_bytes);

            index = CURRENT_PACKET_DATA_LENGTH_SIZE;
            send_bytes = new byte[PACKET_SIZE];
            index = addHeader(this.header, send_bytes);
        }

        index = ByteBuilder.stringToByte(code, value, send_bytes, index);
        return this;
    }

    public ByteBuilder addByte(int code, byte[] values) {

        int attributeLength = 4 + values.length;

        if (index + attributeLength >= PACKET_SIZE) {

            int dataLength = index - CURRENT_PACKET_DATA_LENGTH_SIZE;

            index = addTotalDatabytesLength(dataLength, send_bytes);

            packets.add(send_bytes);

            index = CURRENT_PACKET_DATA_LENGTH_SIZE;
            send_bytes = new byte[PACKET_SIZE];
            index = addHeader(this.header, send_bytes);
        }

        index = ByteBuilder.addBytes(code, values, 0, send_bytes, index, values.length);
        return this;
    }

    public ArrayList<byte[]> build() {

        int dataLength = index - CURRENT_PACKET_DATA_LENGTH_SIZE;

        index = addTotalDatabytesLength(dataLength, send_bytes);

        packets.add(send_bytes);
        
  

        int totalPackets = packets.size();

        ArrayList<byte[]> returnPackets = new ArrayList<>();

        int checkByte;

        if (totalPackets == 1) {

            checkByte = 2;

            byte[] data = packets.poll();

            dataLength = (data[0] & 0xFF) << 8 | (data[1] & 0xFF);

            send_bytes = new byte[CHECK_BYTE_SIZE + dataLength];

            send_bytes[0] = (byte) checkByte;

            System.arraycopy(data, CURRENT_PACKET_DATA_LENGTH_SIZE, send_bytes, 1, dataLength);

            returnPackets.add(send_bytes);
        } else {
            
            checkByte = 3;

            int subHeaderLength = 1;

            int subHeaderCodeByte = 1 ;
            
            int sequenceNo = 1;

            if (totalPackets > 255) {
                subHeaderLength = 2;
            }
            
            SUB_HEADER_SIZE = 2 * ( subHeaderCodeByte + 1 + subHeaderLength);

            while(!packets.isEmpty()) {
                
                byte[] currentPacket = packets.poll();
                
                int subHeaderIndex = 0;
                
                dataLength = ( currentPacket[0] & 0xFF) << 8 | ( currentPacket[1] & 0xFF);
                
                send_bytes = new byte[ CHECK_BYTE_SIZE + SUB_HEADER_SIZE + dataLength];
                
                send_bytes[ subHeaderIndex++ ] = (byte) checkByte;
                
                send_bytes[subHeaderIndex++] = (byte) Code.SEQUENCE;

                send_bytes[subHeaderIndex++] = (byte) subHeaderLength;

                if (totalPackets > 255) {

                    send_bytes[subHeaderIndex++] = (byte) (sequenceNo >> 8);
                    send_bytes[subHeaderIndex++] = (byte) (sequenceNo);
                } else {
                    send_bytes[subHeaderIndex++] = (byte) sequenceNo;
                }

                sequenceNo++;

                send_bytes[subHeaderIndex++] = (byte) Code.TOTAL_PACKETS;
                send_bytes[subHeaderIndex++] = (byte) subHeaderLength;

                if (totalPackets > 255) {

                    send_bytes[subHeaderIndex++] = (byte) (totalPackets >> 8);
                    send_bytes[subHeaderIndex++] = (byte) (totalPackets);
                } else {
                    send_bytes[subHeaderIndex++] = (byte) totalPackets;
                }
                
                System.arraycopy(currentPacket, CURRENT_PACKET_DATA_LENGTH_SIZE, send_bytes, subHeaderIndex, dataLength);
                
                returnPackets.add(send_bytes);

            }

        }

        return returnPackets;
    }

    private static int intToByte(int attribute, int value, int length, byte[] send_bytes, int index) {


        send_bytes[index++] = (byte) (attribute >> 8);
        send_bytes[index++] = (byte) attribute;

        send_bytes[index++] = (byte) length;

        switch (length) {
            case 1: {
                send_bytes[index++] = (byte) (value);
                break;
            }
            case 2: {
                send_bytes[index++] = (byte) (value >> 8);
                send_bytes[index++] = (byte) (value);
                break;
            }
            case 4: {
                send_bytes[index++] = (byte) (value >> 24);
                send_bytes[index++] = (byte) (value >> 16);
                send_bytes[index++] = (byte) (value >> 8);
                send_bytes[index++] = (byte) (value);
                break;
            }
        }

        return index;
    }

    private static int longToByte(int attribute, long value, int length, byte[] send_bytes, int index) {


        send_bytes[index++] = (byte) (attribute >> 8);
        send_bytes[index++] = (byte) attribute;
        send_bytes[index++] = (byte) length;

        switch (length) {
            case 6: {
                send_bytes[index++] = (byte) (value >> 40);
                send_bytes[index++] = (byte) (value >> 32);
                send_bytes[index++] = (byte) (value >> 24);
                send_bytes[index++] = (byte) (value >> 16);
                send_bytes[index++] = (byte) (value >> 8);
                send_bytes[index++] = (byte) (value);
                break;
            }
            case 8: {
                send_bytes[index++] = (byte) (value >> 56);
                send_bytes[index++] = (byte) (value >> 48);
                send_bytes[index++] = (byte) (value >> 40);
                send_bytes[index++] = (byte) (value >> 32);
                send_bytes[index++] = (byte) (value >> 24);
                send_bytes[index++] = (byte) (value >> 16);
                send_bytes[index++] = (byte) (value >> 8);
                send_bytes[index++] = (byte) (value);
                break;
            }
        }

        return index;
    }

    private static int stringToByte(int attribute, String value, byte[] send_bytes, int index) {

        if (value == null || value.length() == 0) {
            return index;
        }

        send_bytes[index++] = (byte) (attribute >> 8);
        send_bytes[index++] = (byte) attribute;

        byte[] id_bytes = value.getBytes();
        int length = id_bytes.length;

        if (length > 255) {
            send_bytes[index++] = (byte) (length >> 8);
            send_bytes[index++] = (byte) length;
        } else {
            send_bytes[index++] = (byte) length;
        }

        System.arraycopy(id_bytes, 0, send_bytes, index, length);
        index += length;

        return index;
    }

    private static int addBytes(int attribute, byte[] data_bytes, int src_pos, byte[] send_bytes, int index, int length) {

        if (data_bytes == null || data_bytes.length == 0) {
            return index;
        }

        send_bytes[index++] = (byte) (attribute >> 8);
        send_bytes[index++] = (byte) attribute;

        send_bytes[index++] = (byte) (length >> 8);
        send_bytes[index++] = (byte) (length);

        System.arraycopy(data_bytes, src_pos, send_bytes, index, length);
        index += length;
        return index;
    }

    private static int boolToByte(int attribute, boolean bool_value, byte[] send_bytes, int index) {

        send_bytes[index++] = (byte) (attribute >> 8);
        send_bytes[index++] = (byte) (attribute);

        int length = 1;
        send_bytes[index++] = (byte) length;

        send_bytes[index++] = (byte) (bool_value == true ? 1 : 0);

        return index;

    }

    private int addHeader(byte[] header, byte[] send_bytes) {
        // throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.

        System.arraycopy(header, 0, send_bytes, index, HEADER_SIZE);

        TOTAL_HEADER_SIZE = HEADER_SIZE + CURRENT_PACKET_DATA_LENGTH_SIZE;

        index += HEADER_SIZE;

        return index;
    }
}
