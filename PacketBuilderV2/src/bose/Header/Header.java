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
    
    public static final int PACKET_SIZE = 9000;
    public static final int PACKET_HEADER_SIZE = 28;
    private int sessionID;
    private int packetID;
    private int serverPacketID;
    private int actionID;

    public void setSessionID(int sessionID) {
        this.sessionID = sessionID;
    }

    public int getSessionID() {
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
