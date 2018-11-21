package com.wallet.parser.model;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * Inmmutable class to model the parser parameters
 * Created by Liodegar on 11/20/2018.
 */
public final class ParserParam implements Serializable {

    public static final DateTimeFormatter DATE_LOG_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd.HH:mm:ss");

    private static final long serialVersionUID = -2657488248675523789L;

    private final LocalDateTime startDate;

    private final ParserDuration duration;

    private final int threshold;

    private final String accessLog;

    public ParserParam(LocalDateTime startDate, ParserDuration duration, int threshold, String accessLog) {
        this.startDate = startDate;
        this.duration = duration;
        this.threshold = threshold;
        this.accessLog = accessLog;
    }

    public LocalDateTime getStartDate() {
        return startDate;
    }

    public ParserDuration getDuration() {
        return duration;
    }

    public int getThreshold() {
        return threshold;
    }


    public String getAccessLog() {
        return accessLog;
    }

    public boolean isValid() {
        return (startDate != null && duration != null && threshold != 0 && accessLog != null);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        ParserParam that = (ParserParam) o;

        if (threshold != that.threshold) return false;
        if (startDate != null ? !startDate.equals(that.startDate) : that.startDate != null) return false;
        if (duration != that.duration) return false;
        return !(accessLog != null ? !accessLog.equals(that.accessLog) : that.accessLog != null);

    }

    @Override
    public int hashCode() {
        int result = startDate != null ? startDate.hashCode() : 0;
        result = 31 * result + (duration != null ? duration.hashCode() : 0);
        result = 31 * result + threshold;
        result = 31 * result + (accessLog != null ? accessLog.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("ParserParam{");
        sb.append("startDate=").append(startDate);
        sb.append(", duration=").append(duration);
        sb.append(", threshold=").append(threshold);
        sb.append(", accessLog='").append(accessLog).append('\'');
        sb.append('}');
        return sb.toString();
    }
}
