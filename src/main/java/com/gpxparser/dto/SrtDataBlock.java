package com.gpxparser.dto;

import java.math.BigDecimal;
import java.util.Date;

/**
 * User: dDima <a href="mailto:schetinin.d@gmail.com"/>schetinin.d@gmail.com</a>
 * Date: 9/10/2016
 * Time: 9:11 PM
 * To change this template use File | Settings | File Templates.
 */
public class SrtDataBlock {

    private Integer blockNumber;

    private BigDecimal longitude;

    private BigDecimal latitude;

    private BigDecimal gpsAltitude; // altitude over the sea level (Kiev, Teremki 190-200m, Left Bank of the River up to 100m)

    private BigDecimal elevation; // barometric elevation value in meters

    private Date time;

    public Integer getBlockNumber() {
        return blockNumber;
    }

    public void setBlockNumber(Integer blockNumber) {
        this.blockNumber = blockNumber;
    }

    public BigDecimal getLongitude() {
        return longitude;
    }

    public void setLongitude(BigDecimal longitude) {
        this.longitude = longitude;
    }

    public BigDecimal getLatitude() {
        return latitude;
    }

    public void setLatitude(BigDecimal latitude) {
        this.latitude = latitude;
    }

    public BigDecimal getGpsAltitude() {
        return gpsAltitude;
    }

    public void setGpsAltitude(BigDecimal gpsAltitude) {
        this.gpsAltitude = gpsAltitude;
    }

    public BigDecimal getElevation() {
        return elevation;
    }

    public void setElevation(BigDecimal elevation) {
        this.elevation = elevation;
    }

    public Date getTime() {
        return time;
    }

    public void setTime(Date time) {
        this.time = time;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        SrtDataBlock that = (SrtDataBlock) o;

        if (blockNumber != null ? !blockNumber.equals(that.blockNumber) : that.blockNumber != null) return false;
        if (longitude != null ? !longitude.equals(that.longitude) : that.longitude != null) return false;
        if (latitude != null ? !latitude.equals(that.latitude) : that.latitude != null) return false;
        if (gpsAltitude != null ? !gpsAltitude.equals(that.gpsAltitude) : that.gpsAltitude != null) return false;
        if (elevation != null ? !elevation.equals(that.elevation) : that.elevation != null) return false;
        return time != null ? time.equals(that.time) : that.time == null;

    }

    @Override
    public int hashCode() {
        int result = blockNumber != null ? blockNumber.hashCode() : 0;
        result = 31 * result + (longitude != null ? longitude.hashCode() : 0);
        result = 31 * result + (latitude != null ? latitude.hashCode() : 0);
        result = 31 * result + (gpsAltitude != null ? gpsAltitude.hashCode() : 0);
        result = 31 * result + (elevation != null ? elevation.hashCode() : 0);
        result = 31 * result + (time != null ? time.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "SrtDataBlock{" +
                "blockNumber=" + blockNumber +
                ", longitude=" + longitude +
                ", latitude=" + latitude +
                ", gpsAltitude=" + gpsAltitude +
                ", elevation=" + elevation +
                ", time=" + time +
                '}';
    }
}
