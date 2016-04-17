/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package bose.clientRequest;

/**
 *
 * @author saikat
 */
public class CallFriend {
    
    private String fndId;
    private int actn;
    private String uId;
    private String sId;
    private String pckId;
    private String callID;
    private int calT;

    public String getFndId() {
        return fndId;
    }

    public void setFndId(String fndId) {
        this.fndId = fndId;
    }

    public int getActn() {
        return actn;
    }

    public void setActn(int actn) {
        this.actn = actn;
    }

    public String getuId() {
        return uId;
    }

    public void setuId(String uId) {
        this.uId = uId;
    }

    public String getsId() {
        return sId;
    }

    public void setsId(String sId) {
        this.sId = sId;
    }

    public String getPckId() {
        return pckId;
    }

    public void setPckId(String pckId) {
        this.pckId = pckId;
    }

    public String getCallID() {
        return callID;
    }

    public void setCallId(String callID) {
        this.callID = callID;
    }

    public int getCallType() {
        return calT;
    }

    public void setCallType(int calT) {
        this.calT = calT;
    }
    
}
