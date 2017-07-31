package com.tm.engagement.domain;

import java.io.Serializable;
import javax.persistence.*;

import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Type;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

import java.util.List;
import java.util.UUID;

@Entity
@Table(name="task_grp")
@JsonInclude(Include.NON_NULL)
public class TaskGroup extends AbstractAuditingEntity implements Serializable {

    private static final long serialVersionUID = -3692035159040791692L;

    @Id
    @Column(name="task_grp_id")
    @Type(type = "uuid-char")
    @GeneratedValue(generator = "uuid")
    @GenericGenerator(name = "uuid", strategy = "uuid2")
	private UUID taskGroupId;

	@Column(name="task_grp_nm")
	private String taskGroupName;

	@Column(name="task_grp_desc")
	private String taskGroupDescription;
	
	//bi-directional many-to-one association to Task
	@OneToMany(fetch = FetchType.LAZY, mappedBy="taskGroup", cascade = CascadeType.ALL)
	private List<Task> taskDetails;

    public UUID getTaskGroupId() {
        return taskGroupId;
    }

    public void setTaskGroupId(UUID taskGroupId) {
        this.taskGroupId = taskGroupId;
    }

    public String getTaskGroupName() {
        return taskGroupName;
    }

    public void setTaskGroupName(String taskGroupName) {
        this.taskGroupName = taskGroupName;
    }

    public String getTaskGroupDescription() {
        return taskGroupDescription;
    }

    public void setTaskGroupDescription(String taskGroupDescription) {
        this.taskGroupDescription = taskGroupDescription;
    }

    public List<Task> getTaskDetails() {
        return taskDetails;
    }

    public void setTaskDetails(List<Task> taskDetails) {
        this.taskDetails = taskDetails;
    }
	
}