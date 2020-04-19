package com.p7.framework.netty.commu;

import java.util.Date;

/**
 * @author Yangzhen
 * @Description
 * @date 2020-04-16 13:24
 **/
public class Header {

    private String from;

    private String to;

    private Date date;

    public Header() {
    }

    public Header(String from, String to, Date date) {
        this.from = from;
        this.to = to;
        this.date = date;
    }

    public String getFrom() {
        return from;
    }

    public void setFrom(String from) {
        this.from = from;
    }

    public String getTo() {
        return to;
    }

    public void setTo(String to) {
        this.to = to;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    @Override
    public String toString() {
        return "Header{" + "from='" + from + '\'' + ", to='" + to + '\'' + ", date=" + date + '}';
    }
}
