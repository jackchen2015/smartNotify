/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.hxct.entity;

import java.util.Date;

/**
 *
 * @author 0310002487
 */
public class AlertEntity {
    private int id;
    private String gwname;
    private String gwloc;
    private String gwip;
    private String type;
    private Date sndTime;

    /**
     * @return the id
     */
    public int getId() {
        return id;
    }

    /**
     * @param id the id to set
     */
    public void setId(int id) {
        this.id = id;
    }


    /**
     * @return the sndTime
     */
    public Date getSndTime() {
        return sndTime;
    }

    /**
     * @param sndTime the sndTime to set
     */
    public void setSndTime(Date sndTime) {
        this.sndTime = sndTime;
    }

    /**
     * @return the gwname
     */
    public String getGwname() {
        return gwname;
    }

    /**
     * @param gwname the gwname to set
     */
    public void setGwname(String gwname) {
        this.gwname = gwname;
    }

    /**
     * @return the gwloc
     */
    public String getGwloc() {
        return gwloc;
    }

    /**
     * @param gwloc the gwloc to set
     */
    public void setGwloc(String gwloc) {
        this.gwloc = gwloc;
    }

    /**
     * @return the gwip
     */
    public String getGwip() {
        return gwip;
    }

    /**
     * @param gwip the gwip to set
     */
    public void setGwip(String gwip) {
        this.gwip = gwip;
    }

    /**
     * @return the type
     */
    public String getType() {
        return type;
    }

    /**
     * @param type the type to set
     */
    public void setType(String type) {
        this.type = type;
    }
   
}
