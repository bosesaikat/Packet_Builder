/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package bose.AttributeInfo;

import java.util.ArrayList;

/**
 *
 * @author saikat
 */
public class AttributesInfo {
    private int ID;
    private Object Data;
    private Class<?> Type;
    private String Name;
    private int size;
    private byte[] DataArray; 

    public void setData(Object Data) {
        this.Data = Data;
    }

    public Object getData() {
        return Data;
    }

    public void setID(int ID) {
        this.ID = ID;
    }

    public int getID() {
        return ID;
    }

    public void setType(Class<?> Type) {
        this.Type = Type;
    }

    public Class<?> getType() {
        return Type;
    }

    public void setName(String Name) {
        this.Name = Name;
    }

    public String getName() {
        return Name;
    }

    public void setSize(int size) {
        this.size = size;
    }

    public int getSize() {
        return size;
    }

    public void setDataArray(byte[] DataArray) {
        this.DataArray = DataArray;
    }

    public byte[] getDataArray() {
        return DataArray;
    }
    
    
    
}
