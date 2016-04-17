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
public class UserUpdateRequest {
    private int actn;
    private String did;
    private int dvc;
    private String el;
    private int ispc;
    private long mbl;
    private String mblDc;

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

    public String getEl() {
        return el;
    }

    public void setEl(String el) {
        this.el = el;
    }

    public int getIspc() {
        return ispc;
    }

    public void setIspc(int ispc) {
        this.ispc = ispc;
    }

    public long getMbl() {
        return mbl;
    }

    public void setMbl(long mbl) {
        this.mbl = mbl;
    }

    public String getMblDc() {
        return mblDc;
    }

    public void setMblDc(String mblDc) {
        this.mblDc = mblDc;
    }

    public String getNm() {
        return nm;
    }

    public void setNm(String nm) {
        this.nm = nm;
    }

    public String getPckId() {
        return pckId;
    }

    public void setPckId(String pckId) {
        this.pckId = pckId;
    }

    public String getuId() {
        return uId;
    }

    public void setuId(String uId) {
        this.uId = uId;
    }

    public int getVsn() {
        return vsn;
    }

    public void setVsn(int vsn) {
        this.vsn = vsn;
    }
    private String nm;
    private String pckId;
    private String uId;
    private int vsn;
    
}
