/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.ipvision.byteBuilder;

/**
 *
 * @author saikat
 */
public class BrokenHeaderBuilder {
    
    public static int BROKEN_HEADER_SIZE;

    byte[] header_bytes = null;

    int index = 0;

    public BrokenHeaderBuilder(int size) {

        BROKEN_HEADER_SIZE = size;
        this.header_bytes = new byte[BROKEN_HEADER_SIZE];

    }

    public int getHeaderSize() {

        return BROKEN_HEADER_SIZE;

    }

    public BrokenHeaderBuilder addInt(int code, int value, int size) {

        index = BrokenHeaderBuilder.intToByte(code, value, size, header_bytes, index);
        return this;
    }

    public BrokenHeaderBuilder addLong(int code, long value, int size) {
        //throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
        index = BrokenHeaderBuilder.longToByte(code, value, size, header_bytes, index);
        return this;
    }
    public static int addTotalDatabytesLength(int dataLength, byte[] send_bytes) {

        int i = BROKEN_HEADER_SIZE;
        send_bytes[i++] = (byte) (dataLength >> 8);
        send_bytes[i++] = (byte) (dataLength);

        return i;
    }

    public BrokenHeaderBuilder addBool(int code, boolean value) {

    

        index = BrokenHeaderBuilder.boolToByte(code, value, header_bytes, index);
        return this;
    }

    public BrokenHeaderBuilder addDouble(int code, double value) {

       

        String strValue = String.valueOf(value);
        index = BrokenHeaderBuilder.stringToByte(code, strValue, header_bytes, index);
        return this;
    }

    public BrokenHeaderBuilder addFloat(int code, float value) {

        

        String strValue = String.valueOf(value);
        index = BrokenHeaderBuilder.stringToByte(code, strValue, header_bytes, index);
        return this;
    }

    public BrokenHeaderBuilder addString(int code, String value) {

        

        index = BrokenHeaderBuilder.stringToByte(code, value, header_bytes, index);
        return this;
    }

    public BrokenHeaderBuilder addBigString(int code, String value) {

       

        index = BrokenHeaderBuilder.stringToByte(code, value, header_bytes, index);
        return this;
    }

    public BrokenHeaderBuilder addByte(int code, byte[] values) {

        index = BrokenHeaderBuilder.addBytes(code, values, 0, header_bytes, index, values.length);
        return this;
    }

    public byte[] getBrokenHeader() {

        return this.header_bytes;
    }

    public static int intToByte(int attribute, Integer int_value, int length, byte[] send_bytes, int index) {

        if (int_value == null ) {
            return index;
        }

        int value = int_value.intValue();

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
       
        //System.out.println(" index "+index);
       
        return index;
    }

    public static int longToByte(int attribute, Long long_value, int length, byte[] send_bytes, int index) {

        if (long_value == null ) {
            return index;
        }
        long value = long_value.longValue();

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

    public static int stringToByte(int attribute, String value, byte[] send_bytes, int index) {

        if (value == null || value.length() == 0) {
            return index;
        }

        send_bytes[index++] = (byte) attribute;

        byte[] id_bytes = value.getBytes();
        int length = id_bytes.length;

        if (length > 255) {
            send_bytes[index++] = (byte) (length >> 8);
            send_bytes[index++] = (byte) length;
        } else {
            send_bytes[index++] = (byte) length;
        }

       // System.out.println("source len " + id_bytes.length + " dest len " + send_bytes.length + " source start index " + 0 + " dest start index " + index + " no of item copy " + length);
        System.arraycopy(id_bytes, 0, send_bytes, index, length);
        index += length;

        return index;
    }

    public static int addBytes(int attribute, byte[] data_bytes, int src_pos, byte[] send_bytes, int index, int length) {

        if (data_bytes == null || data_bytes.length == 0) {
            return index;
        }

        send_bytes[index++] = (byte) attribute;

        if (length > 255) {
            send_bytes[index++] = (byte) (length >> 8);
            send_bytes[index++] = (byte) (length);

        } else {
            send_bytes[index++] = (byte) (length);
        }

        System.arraycopy(data_bytes, src_pos, send_bytes, index, length);
        index += length;
        return index;
    }

    public static int boolToByte(int attribute, boolean bool_value, byte[] send_bytes, int index) {

        
        send_bytes[index++] = (byte) (attribute);

        int length = 1;
        send_bytes[index++] = (byte) length;

        send_bytes[index++] = (byte) (bool_value == true ? 1 : 0);

        return index;

    }
}
