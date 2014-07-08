package cn.timeoff.security.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;;

@Entity
@Table(name = "authority",
       uniqueConstraints = {
         @UniqueConstraint(columnNames = {"user_id", "authority"} )
} )
public class Authority {
    @Id
    @GeneratedValue(strategy=GenerationType.AUTO)
    private long id;

    @ManyToOne
    @JoinColumn(name="user_id")
    private User user;
    
	@Column(length=50, nullable=false)
    private String authority;

    public Authority() {
    }

    public Authority(String authority) {
    	this.authority = authority;
    }

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	public String getAuthority() {
		return authority;
	}

	public void setAuthority(String authority) {
		this.authority = authority;
	}
}
