package eu.vranckaert.worktime.model;

import com.j256.ormlite.field.DataType;
import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;
import eu.vranckaert.worktime.enums.ExternalSystems;

import java.io.Serializable;
import java.util.Date;

/**
 * User: DIRK VRANCKAERT
 * Date: 05/02/11
 * Time: 16:50
 */
@DatabaseTable
public class TimeRegistration implements Serializable {
    @DatabaseField(generatedId = true, columnName = "id")
    private Integer id;
    @DatabaseField(columnName = "startTime")
    private Date startTime;
    @DatabaseField(columnName = "endTime")
    private Date endTime;
    @DatabaseField(columnName = "comment")
    private String comment;
    @DatabaseField(foreign = true, columnName = "taskId")
    private Task task;
    @DatabaseField
    private Long externalId;
    @DatabaseField(dataType = DataType.ENUM_STRING)
    private ExternalSystems externalSystem;
    @DatabaseField
    private String flags;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Date getStartTime() {
        return startTime;
    }

    public void setStartTime(Date startTime) {
        this.startTime = startTime;
    }

    public Date getEndTime() {
        return endTime;
    }

    public void setEndTime(Date endTime) {
        this.endTime = endTime;
    }

    public Task getTask() {
        return task;
    }

    public void setTask(Task task) {
        this.task = task;
    }

    public boolean isOngoingTimeRegistration() {
        if (endTime == null) {
            return true;
        }
        return false;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public Long getExternalId() {
        return externalId;
    }

    public void setExternalId(Long externalId) {
        this.externalId = externalId;
    }

    public ExternalSystems getExternalSystem() {
        return externalSystem;
    }

    public void setExternalSystem(ExternalSystems externalSystem) {
        this.externalSystem = externalSystem;
    }

    public String getFlags() {
        return flags;
    }

    public void setFlags(String flags) {
        this.flags = flags;
    }
}
