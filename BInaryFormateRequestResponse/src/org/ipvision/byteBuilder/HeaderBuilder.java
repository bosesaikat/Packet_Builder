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
public class HeaderBuilder {

    public static int HEADER_SIZE;

    byte[] header_bytes = null;

    int index = 0;

    public HeaderBuilder(int size) {

        HEADER_SIZE = size;
        this.header_bytes = new byte[HEADER_SIZE];

    }

    public int getHeaderSize() {

        return HEADER_SIZE;

    }

    public HeaderBuilder addInt(int code, int value, int size) {

        index = HeaderBuilder.intToByte(code, value, size, header_bytes, index);
        return this;
    }

    public HeaderBuilder addLong(int code, long value, int size) {
        //throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
        index = ListByteBuilder.longToByte(code, value, size, header_bytes, index);
        return this;
    }
    public static int addTotalDatabytesLength(int dataLength, byte[] send_bytes) {

        int i = HEADER_SIZE;
        send_bytes[i++] = (byte) (dataLength >> 8);
        send_bytes[i++] = (byte) (dataLength);

        return i;
    }

    public HeaderBuilder addBool(int code, boolean value) {

        int attributeLength = 4;

        index = HeaderBuilder.boolToByte(code, value, header_bytes, index);
        return this;
    }

    public HeaderBuilder addDouble(int code, double value) {

        int attributeLength = 3 + String.valueOf(value).getBytes().length;

        String strValue = String.valueOf(value);
        index = HeaderBuilder.stringToByte(code, strValue, header_bytes, index);
        return this;
    }

    public HeaderBuilder addFloat(int code, float value) {

        int attributeLength = 3 + String.valueOf(value).getBytes().length;

        String strValue = String.valueOf(value);
        index = HeaderBuilder.stringToByte(code, strValue, header_bytes, index);
        return this;
    }

    public HeaderBuilder addString(int code, String value) {

        int attributeLength = 3 + value.getBytes().length;

        index = HeaderBuilder.stringToByte(code, value, header_bytes, index);
        return this;
    }

    public HeaderBuilder addBigString(int code, String value) {

        int attributeLength = 3 + value.getBytes().length;

        index = HeaderBuilder.stringToByte(code, value, header_bytes, index);
        return this;
    }

    public HeaderBuilder addByte(int code, byte[] values) {

        int attributeLength = 4 + values.length;

        index = HeaderBuilder.addBytes(code, values, 0, header_bytes, index, values.length);
        return this;
    }

    public byte[] getHeader() {

        return this.header_bytes;
    }

    public static int intToByte(int attribute, Integer int_value, int length, byte[] send_bytes, int index) {

        if (int_value == null || int_value.intValue() == 0) {
            return index;
        }

        int value = int_value.intValue();

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

    public static int longToByte(int attribute, Long long_value, int length, byte[] send_bytes, int index) {

        if (long_value == null || long_value.longValue() == 0) {
            return index;
        }
        long value = long_value.longValue();

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

    public static int stringToByte(int attribute, String value, byte[] send_bytes, int index) {

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

    public static int addBytes(int attribute, byte[] data_bytes, int src_pos, byte[] send_bytes, int index, int length) {

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

    public static int boolToByte(int attribute, boolean bool_value, byte[] send_bytes, int index) {

        send_bytes[index++] = (byte) (attribute >> 8);
        send_bytes[index++] = (byte) (attribute);

        int length = 1;
        send_bytes[index++] = (byte) length;

        send_bytes[index++] = (byte) (bool_value == true ? 1 : 0);

        return index;

    }

}
