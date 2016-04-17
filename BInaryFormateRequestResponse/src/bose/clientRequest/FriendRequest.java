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
public class FriendRequest {
    
    private String uId;
    private String sId;
    private String pckId;
    private int actn;

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

    public int getActn() {
        return actn;
    }

    public void setActn(int actn) {
        this.actn = actn;
    }
}
