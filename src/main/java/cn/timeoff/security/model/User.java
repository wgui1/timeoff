package cn.timeoff.security.model;

import java.sql.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.validation.constraints.Pattern;

/**
 * User account information, support DaoAuthenticationManager
 * It is always used even it is not used for DaoAuthentication, in that case,
 * It save just user name and as a bridge connect to employee
 * @author gawain
 *
 */
@Entity
public class User {
	
    @Id
    @GeneratedValue(strategy=GenerationType.AUTO)
    private long id;
    
    @ManyToOne
    @JoinColumn(name="domain_id")
    private Domain domain;

	@Column(nullable=false, length=128, unique=true)
    private String username;

	@Pattern(regexp="[a-z0-9!#$%&'*+/=?^_`{|}~-]+(?:\\."
	        +"[a-z0-9!#$%&'*+/=?^_`{|}~-]+)*@"
	        +"(?:[a-z0-9](?:[a-z0-9-]*[a-z0-9])?\\.)+[a-z0-9](?:[a-z0-9-]*[a-z0-9])?",
	        message="{invalid.email}")
	@Column(nullable=false, length=512)
    private String email;

    @Column(nullable=false, length=512)
    private String password;

    @Column()
    private Boolean enabled = true;

	@Column
    private String firstName;

    @Column
    private String lastName;
    
    @Column
    private Date onboardDate;
    
    @ManyToOne
    @JoinColumn(name="manager_id")
    private User manager;

    public User() {
	}

    public User(Domain domain, String username, String email, String password) {
		this.domain = domain;
		this.username = username;
		this.email = email;
		this.password = password;
	}

    public long getId() {
		return id;
	}

    public Boolean getEnabled() {
		return enabled;
	}

	public void setEnabled(Boolean enabled) {
		this.enabled = enabled;
	}

	public Domain getDomain() {
        return domain;
    }

    public void setDomain(Domain domain) {
        this.domain = domain;
    }

    public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public String getLastName() {
		return lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}
	
	public Date getOnboardDate() {
		return onboardDate;
	}

	public void setOnboardDate(Date onboardDate) {
		this.onboardDate = onboardDate;
	}

	public User getManager() {
		return manager;
	}

	public void setManager(User manager) {
		this.manager = manager;
	}

	@Override
    public String toString() {
        return String.format(
            "User[username=%s, firstName='%s', lastName='%s']",
            username, firstName, lastName);
    }
}
