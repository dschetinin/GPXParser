package com.gpxparser.jpa;

import javax.persistence.*;
import java.util.Date;

/**
 * User: dschetinin <a href="mailto:schetinin.d@gmail.com"/>schetinin.d@gmail.com</a>
 * Date: 11/29/2016
 * Time: 4:49 PM
 * To change this template use File | Settings | File Templates.
 */
@Entity
@Table(name = "gpx_tracks_history", schema = "gpxparser", catalog = "")
public class GpxTracksHistoryEntity {
    private int trackId;
    private String gpxData;
    private String fileName;
    private Date dateCreated;
    private Date dateUpdated;

    @Id
    @Column(name = "track_id", nullable = false)
    public int getTrackId() {
        return trackId;
    }

    public void setTrackId(int trackId) {
        this.trackId = trackId;
    }

    @Basic
    @Column(name = "file_name", nullable = true)
    public String getFileName() {
        return fileName;
    }

    public void setFileName(String gpxData) {
        this.fileName = gpxData;
    }

    @Basic
    @Column(name = "gpx_data", nullable = true)
    public String getGpxData() {
        return gpxData;
    }

    public void setGpxData(String gpxData) {
        this.gpxData = gpxData;
    }

    @Basic
    @Column(name = "date_created", nullable = false)
    public Date getDateCreated() {
        return dateCreated;
    }

    public void setDateCreated(Date dateCreated) {
        this.dateCreated = dateCreated;
    }

    @Basic
    @Column(name = "date_updated", nullable = true)
    public Date getDateUpdated() {
        return dateUpdated;
    }

    public void setDateUpdated(Date dateUpdated) {
        this.dateUpdated = dateUpdated;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        GpxTracksHistoryEntity that = (GpxTracksHistoryEntity) o;

        if (trackId != that.trackId) return false;
        if (gpxData != null ? !gpxData.equals(that.gpxData) : that.gpxData != null) return false;
        if (dateCreated != null ? !dateCreated.equals(that.dateCreated) : that.dateCreated != null) return false;
        if (dateUpdated != null ? !dateUpdated.equals(that.dateUpdated) : that.dateUpdated != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = trackId;
        result = 31 * result + (gpxData != null ? gpxData.hashCode() : 0);
        result = 31 * result + (dateCreated != null ? dateCreated.hashCode() : 0);
        result = 31 * result + (dateUpdated != null ? dateUpdated.hashCode() : 0);
        return result;
    }
}
