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
public class FeedRequest {
    
    private int actn;
    private int sn;
    private int sv;
    private String sId;
    private String pckId;
    private long utId;

    public int getActn() {
        return actn;
    }

    public void setActn(int actn) {
        this.actn = actn;
    }

    public int getSn() {
        return sn;
    }

    public void setSn(int sn) {
        this.sn = sn;
    }

    public int getSv() {
        return sv;
    }

    public void setSv(int sv) {
        this.sv = sv;
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

    public long getUtId() {
        return utId;
    }

    public void setUtId(long utId) {
        this.utId = utId;
    }
}
