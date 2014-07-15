package cn.timeoff.security.model;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

@Entity
public class GroupMember {
    @Id
    @GeneratedValue(strategy=GenerationType.AUTO)
    private long id;

    @ManyToOne
    @JoinColumn(name="group_id")
    private Group group;

	@ManyToOne
    @JoinColumn(name="employee_id")
    private Employee employee;

    public GroupMember() {
	}

    public GroupMember(Group group, Employee employee) {
    	this.group = group;
    	this.employee = employee;
	}

	public long getId() {
		return id;
	}

    public Group getGroup() {
		return group;
	}

	public void setGroup(Group group) {
		this.group = group;
	}

	public Employee getEmployee() {
		return employee;
	}

	public void setEmployee(Employee employee) {
		this.employee = employee;
	}

}
