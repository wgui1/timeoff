package cn.timeoff.security.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

@Entity
public class GroupAuthority {
    @Id
    @GeneratedValue(strategy=GenerationType.AUTO)
    private long id;

    @ManyToOne
    @JoinColumn(name="group_id")
    private Group group;

	@Column(length=50, nullable=false)
    private String authority;

    public GroupAuthority() {
	}

    public GroupAuthority(Group group, String authority) {
    	this.group = group;
    	this.authority = authority;
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

	public String getAuthority() {
		return authority;
	}

	public void setAuthority(String authority) {
		this.authority = authority;
	}
}
