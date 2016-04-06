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
public class SubHeader {
    
    public int sequenceNo;
    public int currentPacketNo;
    public int totalPacket;
    public int remainingPacket;
    

    public void setCurrentPacketNo(int currentPacketNo) {
        this.currentPacketNo = currentPacketNo;
    }

    public int getCurrentPacketNo() {
        return currentPacketNo;
    }

    public void setRemainingPacket(int remainingPacket) {
        this.remainingPacket = remainingPacket;
    }

    public int getRemainingPacket() {
        return remainingPacket;
    }

    public void setSequenceNo(int sequenceNo) {
        this.sequenceNo = sequenceNo;
    }

    public int getSequenceNo() {
        return sequenceNo;
    }
    
    
    
    
}
