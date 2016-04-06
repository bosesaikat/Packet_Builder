/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package bose.PacketBuilderV2;

import bose.AttributeInfo.AttributesInfo;
import bose.Header.Header;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.Random;


/**
 *
 * @author saikat
 */
public class Main {
    public static void main(String[] args) throws FileNotFoundException, IOException {
       ArrayList<AttributesInfo> attributesInfoList = new ArrayList<AttributesInfo>();
       Header header = new Header();
       
       
       for(int i=0 ; i<7 ; i++){
         AttributesInfo attributesInfo = new AttributesInfo();
         switch(i){
             case 0:{
                attributesInfo.setID(i);
                attributesInfo.setType(Integer.class);
                attributesInfo.setName("userID");
                attributesInfo.setData(100);
                attributesInfo.setSize(4);
                break;
             }
             case 1:{
                attributesInfo.setID(i);
                attributesInfo.setType(Long.class);
                attributesInfo.setName("userStatusDate");
                attributesInfo.setData(new Date().getTime());
                attributesInfo.setSize(8);
                break;
             }
             case 2:{
                attributesInfo.setID(i);
                attributesInfo.setType(Boolean.class);
                attributesInfo.setName("isValid");
                attributesInfo.setData(false);
                attributesInfo.setSize(1);
                break;
             }
             case 3:{
                attributesInfo.setID(i);
                attributesInfo.setType(String.class);
                attributesInfo.setName("userName");
                attributesInfo.setData("Saikat");
                attributesInfo.setSize( attributesInfo.getData().toString().length() );
                break;
             }
             case 4:{
                attributesInfo.setID(i);
                attributesInfo.setType(Double.class);
                attributesInfo.setName("userRating");
                attributesInfo.setData(124.45);
                attributesInfo.setSize(8);
                break;
             }
             case 5:{
                attributesInfo.setID(i);
                attributesInfo.setType(String.class);
                attributesInfo.setName("userStatus");
                
                File file = new File("/home/bose/input.txt");
		FileReader fr = new FileReader(file);
		BufferedReader br = null;
		String sCurrentLine="";

		try {
			br = new BufferedReader(fr);

			while ((br.readLine()) != null) {
				sCurrentLine+=br.readLine();
			}

		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			br.close();
			fr.close();			
		}
                attributesInfo.setData(sCurrentLine);
                attributesInfo.setSize( attributesInfo.getData().toString().length() );
                break;
             }
             case 6:{
                attributesInfo.setID(i);
                attributesInfo.setType(byte.class);
                attributesInfo.setName("userCode");
                byte[] data = new byte[10];
                new Random().nextBytes(data);
               
                attributesInfo.setDataArray(data);
                
                /*for (int j = 0 ; j<attributesInfo.getDataArray().length;j++){
                    System.out.print("byte "+attributesInfo.getDataArray()[j]+" ");
                }*/
                attributesInfo.setSize( data.length );
             }
         }
         attributesInfoList.add(attributesInfo);
       }
       

       PacketBuilderV2 packetBuilderV2 = new PacketBuilderV2(header, attributesInfoList);
       
       packetBuilderV2.addAttributeInByte();
    }
}
