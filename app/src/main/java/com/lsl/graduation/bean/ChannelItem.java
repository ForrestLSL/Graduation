package com.lsl.graduation.bean;

import java.io.Serializable;

/**
 * Created by Forrest on 16/4/13.
 */
public class ChannelItem implements Serializable{
    private Integer id;
    private String name;
    private Integer orderId;
    private Integer selected;
    public ChannelItem(){}
    /***
     *  构造器
     * @param id
     * @param name
     * @param orderId
     * @param selected
     */
    public ChannelItem(Integer id, String name, Integer orderId, Integer selected) {
        this.id = Integer.valueOf(id);
        this.name = name;
        this.orderId = Integer.valueOf(orderId);
        this.selected = Integer.valueOf(selected);
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getOrderId() {
        return orderId;
    }

    public void setOrderId(Integer orderId) {
        this.orderId = orderId;
    }

    public Integer getSelected() {
        return selected;
    }

    public void setSelected(Integer selected) {
        this.selected = selected;
    }

    @Override
    public String toString() {
        return "ChannelItem [id=" + this.id + ", name=" + this.name
                + ", selected=" + this.selected + "]";
    }
}
