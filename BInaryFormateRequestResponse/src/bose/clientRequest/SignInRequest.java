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
public class SignInRequest {
    private int actn;
    private String did;
    private int dvc;
    private boolean isb;
    private int lt;
    private String pckId;
    private int pen;
    private long uId;
    private String usrPw;
    private int vsn;

    public int getActn() {
        return actn;
    }

    public void setActn(int actn) {
        this.actn = actn;
    }

    public String getDid() {
        return did;
    }

    public void setDid(String did) {
        this.did = did;
    }

    public int getDvc() {
        return dvc;
    }

    public void setDvc(int dvc) {
        this.dvc = dvc;
    }

    public boolean isIsb() {
        return isb;
    }

    public void setIsb(boolean isb) {
        this.isb = isb;
    }

    public int getLt() {
        return lt;
    }

    public void setLt(int lt) {
        this.lt = lt;
    }

    public String getPckId() {
        return pckId;
    }

    public void setPckId(String pckId) {
        this.pckId = pckId;
    }

    public int getPen() {
        return pen;
    }

    public void setPen(int pen) {
        this.pen = pen;
    }

    public long getuId() {
        return uId;
    }

    public void setuId(long uId) {
        this.uId = uId;
    }

    public String getUsrPw() {
        return usrPw;
    }

    public void setUsrPw(String usrPw) {
        this.usrPw = usrPw;
    }

    public int getVsn() {
        return vsn;
    }

    public void setVsn(int vsn) {
        this.vsn = vsn;
    }
    
    
    
    
}
