package com.tm.engagement.domain;

import java.io.Serializable;
import java.util.List;
import java.util.UUID;

import javax.persistence.*;

import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Type;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

@Entity
@Table(name="task")
@JsonInclude(Include.NON_NULL)
public class Task extends AbstractAuditingEntity implements Serializable {

    private static final long serialVersionUID = -503432677996505158L;

    @Id
	@Column(name="task_id")
    @Type(type = "uuid-char")
    @GeneratedValue(generator = "uuid")
    @GenericGenerator(name = "uuid", strategy = "uuid2")
	private UUID taskId;

	@Column(name="task_desc")
	private String taskDescription;

	@Column(name="task_nm")
	private String taskName;
	
	@ManyToOne(cascade = {CascadeType.ALL})
	@JoinColumn(name="task_grp_id")
	private TaskGroup taskGroup;
	
	@Transient
	private List<String> taskNames;

    public UUID getTaskId() {
        return taskId;
    }

    public void setTaskId(UUID taskId) {
        this.taskId = taskId;
    }

    public String getTaskDescription() {
        return taskDescription;
    }

    public void setTaskDescription(String taskDescription) {
        this.taskDescription = taskDescription;
    }

    public String getTaskName() {
        return taskName;
    }

    public void setTaskName(String taskName) {
        this.taskName = taskName;
    }

    public TaskGroup getTaskGroup() {
        return taskGroup;
    }

    public void setTaskGroup(TaskGroup taskGroup) {
        this.taskGroup = taskGroup;
    }

    public List<String> getTaskNames() {
        return taskNames;
    }

    public void setTaskNames(List<String> taskNames) {
        this.taskNames = taskNames;
    }
    
}