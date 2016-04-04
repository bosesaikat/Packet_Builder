/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package bose.Header;

/**
 *
 * @author saikat
 */
public class Header {
    
   // public static final int PACKET_SIZE = 9000;
  //  public static final int PACKET_HEADER_SIZE = 33;
    private String sessionID;
    private int packetID;
    private int serverPacketID;
    private int actionID;
    private long acknowledgement;
    private int dataLength;
   // private int sequenceNo ;

   // public int getSequenceNo() {
   //     return sequenceNo;
   // }

   // public void setSequenceNo(int sequenceNo) {
  //      this.sequenceNo = sequenceNo;
 //   }
    
    public long getAcknowledgement() {
        return acknowledgement;
    }

    public void setAcknowledgement(long acknowledgement) {
        this.acknowledgement = acknowledgement;
    }
    
     
    

    public void setDataLength(int dataLength) {
        this.dataLength = dataLength;
    }

    public int getDataLength() {
        return dataLength;
    }
    
    
    public void setSessionID(String sessionID) {
        this.sessionID = sessionID;
    }

    public String getSessionID() {
        return sessionID;
    }

    public void setActionID(int actionID) {
        this.actionID = actionID;
    }

    public int getActionID() {
        return actionID;
    }

    public void setPacketID(int packetID) {
        this.packetID = packetID;
    }

    public int getPacketID() {
        return packetID;
    }

    public void setServerPacketID(int serverPacketID) {
        this.serverPacketID = serverPacketID;
    }

    public int getServerPacketID() {
        return serverPacketID;
    }
    
    
}
