package eu.vranckaert.worktime.model;

import com.j256.ormlite.field.DataType;
import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;
import eu.vranckaert.worktime.enums.ExternalSystems;

import java.io.Serializable;

/**
 * User: DIRK VRANCKAERT
 * Date: 05/02/11
 * Time: 16:52
 */
@DatabaseTable
public class Project implements Serializable {
    @DatabaseField(generatedId = true)
    private Integer id;
    @DatabaseField
    private String name;
    @DatabaseField
    private String comment;
    @DatabaseField(defaultValue = "0")
    private Integer order;
    @DatabaseField(dataType = DataType.BOOLEAN, defaultValue = "false")
    private boolean defaultValue;
    @DatabaseField
    private Long externalId;
    @DatabaseField(dataType = DataType.ENUM_STRING)
    private ExternalSystems externalSystem;
    @DatabaseField
    private String flags;
    @DatabaseField(dataType = DataType.BOOLEAN, defaultValue = "false")
    private boolean finished;

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

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public Integer getOrder() {
        return order;
    }

    public void setOrder(Integer order) {
        this.order = order;
    }

    public boolean isDefaultValue() {
        return defaultValue;
    }

    public void setDefaultValue(boolean defaultValue) {
        this.defaultValue = defaultValue;
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

    public boolean isFinished() {
        return finished;
    }

    public void setFinished(boolean finished) {
        this.finished = finished;
    }
}
