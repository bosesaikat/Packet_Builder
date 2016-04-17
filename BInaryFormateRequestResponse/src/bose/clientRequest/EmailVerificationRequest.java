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
public class EmailVerificationRequest {
  
    private int actn;
    private String did;
    private int dvc;
    
    
    private String pckId;
    
    private long uId;
    
    private int vsn;
    private String el;
    private String mblDc;
    private String evc ;

    public String getEvc() {
        return evc;
    }

    public void setEvc(String evc) {
        this.evc = evc;
    }

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

    public String getPckId() {
        return pckId;
    }

    public void setPckId(String pckId) {
        this.pckId = pckId;
    }

    public long getuId() {
        return uId;
    }

    public void setuId(long uId) {
        this.uId = uId;
    }

    public int getVsn() {
        return vsn;
    }

    public void setVsn(int vsn) {
        this.vsn = vsn;
    }

    public String getEl() {
        return el;
    }

    public void setEl(String el) {
        this.el = el;
    }

    public String getMblDc() {
        return mblDc;
    }

    public void setMblDc(String mblDc) {
        this.mblDc = mblDc;
    }
    
    
    
}
