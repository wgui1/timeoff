package cn.timeoff.security.model;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

@Entity
@Table(name = "employee",
       uniqueConstraints = {
         @UniqueConstraint(columnNames = {"user_id", "cooperation_id"} )
} )
public class Employee {
	
    @Id
    @GeneratedValue(strategy=GenerationType.AUTO)
    private long id;

    @ManyToOne
    @JoinColumn(name="user_id")
    private User user;

    @ManyToOne
    @JoinColumn(name="cooperation_id")
    private Cooperation cooperation;

	public Employee(User user, Cooperation cooperation) {
		super();
		this.user = user;
		this.cooperation = cooperation;
	}

	public Employee() {
		super();
	}

}