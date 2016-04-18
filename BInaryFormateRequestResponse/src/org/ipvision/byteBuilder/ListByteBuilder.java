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
public class ListByteBuilder {

    public static int SIZE = 128000;

    byte[] bytes = new byte[SIZE];

    int index = 0;

    public ListByteBuilder addInt(int code, int value, int size) {

        index = ListByteBuilder.intToByte(code, value, size, bytes, index);
        return this;
    }

    public ListByteBuilder addLong(int code, long value, int size) {
        //throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
        index = ListByteBuilder.longToByte(code, value, size, bytes, index);
        return this;
    }

    public static int addTotalDatabytesLength(int dataLength, byte[] send_bytes) {

        int i = SIZE;
        send_bytes[i++] = (byte) (dataLength >> 8);
        send_bytes[i++] = (byte) (dataLength);

        return i;
    }

    public ListByteBuilder addBool(int code, boolean value) {

        index = ListByteBuilder.boolToByte(code, value, bytes, index);
        return this;
    }

    public ListByteBuilder addDouble(int code, double value) {

        String strValue = String.valueOf(value);
        index = ListByteBuilder.stringToByte(code, strValue, bytes, index);
        return this;
    }

    public ListByteBuilder addFloat(int code, float value) {

        String strValue = String.valueOf(value);
        index = ListByteBuilder.stringToByte(code, strValue, bytes, index);
        return this;
    }

    public ListByteBuilder addString(int code, String value) {

        index = ListByteBuilder.stringToByte(code, value, bytes, index);
        return this;
    }

    public ListByteBuilder addBigString(int code, String value) {

        index = ListByteBuilder.stringToByte(code, value, bytes, index);
        return this;
    }

    public ListByteBuilder addByte(int code, byte[] values) {

        index = ListByteBuilder.addBytes(code, values, 0, bytes, index, values.length);
        return this;
    }

    public byte[] getListBytes() {

        byte[] finalBytes = new byte[index];
        System.arraycopy(this.bytes, 0, finalBytes, 0, index);
        return finalBytes;
    }

    public static int intToByte(int attribute, Integer int_value, int length, byte[] bytes, int index) {

        if (int_value == null ) {
            return index;
        }

        int value = int_value.intValue();

        bytes[index++] = (byte) (attribute >> 8);
        bytes[index++] = (byte) attribute;

        bytes[index++] = (byte) length;

        switch (length) {
            case 1: {
                bytes[index++] = (byte) (value);
                break;
            }
            case 2: {
                bytes[index++] = (byte) (value >> 8);
                bytes[index++] = (byte) (value);
                break;
            }
            case 4: {
                bytes[index++] = (byte) (value >> 24);
                bytes[index++] = (byte) (value >> 16);
                bytes[index++] = (byte) (value >> 8);
                bytes[index++] = (byte) (value);
                break;
            }
        }

        return index;
    }

    public static int longToByte(int attribute, Long long_value, int length, byte[] bytes, int index) {

        if (long_value == null) {
            return index;
        }
        long value = long_value.longValue();

        bytes[index++] = (byte) (attribute >> 8);
        bytes[index++] = (byte) attribute;
        bytes[index++] = (byte) length;

        switch (length) {
            case 6: {
                bytes[index++] = (byte) (value >> 40);
                bytes[index++] = (byte) (value >> 32);
                bytes[index++] = (byte) (value >> 24);
                bytes[index++] = (byte) (value >> 16);
                bytes[index++] = (byte) (value >> 8);
                bytes[index++] = (byte) (value);
                break;
            }
            case 8: {
                bytes[index++] = (byte) (value >> 56);
                bytes[index++] = (byte) (value >> 48);
                bytes[index++] = (byte) (value >> 40);
                bytes[index++] = (byte) (value >> 32);
                bytes[index++] = (byte) (value >> 24);
                bytes[index++] = (byte) (value >> 16);
                bytes[index++] = (byte) (value >> 8);
                bytes[index++] = (byte) (value);
                break;
            }
        }

        return index;
    }

    public static int stringToByte(int attribute, String value, byte[] bytes, int index) {

        if (value == null || value.length() == 0) {
            return index;
        }

        bytes[index++] = (byte) (attribute >> 8);
        bytes[index++] = (byte) attribute;

        byte[] id_bytes = value.getBytes();
        int length = id_bytes.length;

        if (length > 255) {
            bytes[index++] = (byte) (length >> 8);
            bytes[index++] = (byte) length;
        } else {
            bytes[index++] = (byte) length;
        }

        System.arraycopy(id_bytes, 0, bytes, index, length);
        index += length;

        return index;
    }

    public static int addBytes(int attribute, byte[] data_bytes, int src_pos, byte[] bytes, int index, int length) {

        if (data_bytes == null || data_bytes.length == 0) {
            return index;
        }

        bytes[index++] = (byte) (attribute >> 8);
        bytes[index++] = (byte) attribute;

        bytes[index++] = (byte) (length >> 8);
        bytes[index++] = (byte) (length);

        System.arraycopy(data_bytes, src_pos, bytes, index, length);
        index += length;
        return index;
    }

    public static int boolToByte(int attribute, boolean bool_value, byte[] bytes, int index) {

        bytes[index++] = (byte) (attribute >> 8);
        bytes[index++] = (byte) (attribute);

        int length = 1;
        bytes[index++] = (byte) length;

        bytes[index++] = (byte) (bool_value == true ? 1 : 0);

        return index;

    }

}
