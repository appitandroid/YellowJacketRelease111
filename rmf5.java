package com.ritchieengineering.yellowjacket.storage.model;

import android.util.Log;

import com.j256.ormlite.field.DatabaseField;

import org.joda.time.DateTime;

import java.io.Serializable;
import java.util.Comparator;
import java.util.Date;

/**
 * Entity class for Session History.
 *
 * @author Kenton Watson
 */
public class SessionHistory implements Serializable, Comparable<SessionHistory>{

    @DatabaseField(id = true)
    private Integer id;

    @DatabaseField
    private String screenshotPath;

    @DatabaseField(foreign = true, foreignAutoCreate = true, foreignAutoRefresh = true)
    private Location location;

    @DatabaseField(foreign = true, foreignAutoCreate = true, foreignAutoRefresh = true)
    private Equipment equipment;

    private Date dateTime;

    private String sessionType;

    public SessionHistory() {}

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getScreenshotPath() {
        return screenshotPath;
    }

    public void setScreenshotPath(String screenshotPath) {
        this.screenshotPath = screenshotPath;
    }

    public Location getLocation() {
        return location;
    }

    public void setLocation(Location location) {
        this.location = location;
    }

    public Equipment getEquipment() {
        return equipment;
    }

    public void setEquipment(Equipment equipment) {
        this.equipment = equipment;
    }

    /**
     * Gets dateTime.
     *
     * @return Value of dateTime.
     */
    public Date getDateTime() {
        return dateTime;
    }

    /**
     * Sets new dateTime.
     *
     * @param dateTime New value of dateTime.
     */
    public void setDateTime(Date dateTime) {
        this.dateTime = dateTime;
        Log.i("SessionHistory", "DateTime Value:"+this.dateTime);
    }

    /**
     * Sets new sessionType.
     *
     * @param sessionType New value of sessionType.
     */
    public void setSessionType(String sessionType) {
        this.sessionType = sessionType;
    }

    /**
     * Gets sessionType.
     *
     * @return Value of sessionType.
     */
    public String getSessionType() {
        return sessionType;
    }

    private static Comparator<SessionHistory> companyComparator = new Comparator<SessionHistory>() {
        @Override
        public int compare(SessionHistory lhs, SessionHistory rhs) {
            //Sort alphabetical by company name, fallback on first/last
            String str1 = lhs.getLocation().getSortingName();

            String str2 = rhs.getLocation().getSortingName();
            int res = String.CASE_INSENSITIVE_ORDER
                    .compare(str1, str2);

            return res;
        }
    };

    private int compareCompany(SessionHistory compareTo) {
        //Sort alphabetical by company name, fallback on first/last
        String str1 = this.getLocation().getSortingName();

        String str2 = compareTo.getLocation().getSortingName();
        int res = String.CASE_INSENSITIVE_ORDER
                .compare(str1, str2);

        return res;
    }

    public static Comparator<SessionHistory> timestampComparator = new Comparator<SessionHistory>() {
        @Override
        public int compare(SessionHistory lhs, SessionHistory rhs) {
            DateTime time1 = new DateTime(lhs.getDateTime());
            DateTime time2 = new DateTime(rhs.getDateTime());

            int res = 0;
            if (time1.isBefore(time2)) {
                res = 1;
            } else if (time1.isAfter(time2)) {
                res = -1;
            }
            return res;
        }
    };

    private int compareTimestamp(SessionHistory compareTo) {
        DateTime time1 = new DateTime(this.getDateTime());
        DateTime time2 = new DateTime(compareTo.getDateTime());

        int res = 0;
        if (time1.isBefore(time2)) {
            res = 1;
        } else if (time1.isAfter(time2)) {
            res = -1;
        }
        return res;
    }

    @Override
    public int compareTo(SessionHistory another) {
        int res = compareCompany(another);
        if (res == 0) {
            res = -compareTimestamp(another);
        }
        return res;
    }
}
