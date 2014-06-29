package cn.timeoff.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

@Entity
public class User {
	
    @Id
    @GeneratedValue(strategy=GenerationType.AUTO)
    private long id;
    
    @ManyToOne
    @JoinColumn(name="cooperation_id")
    private Cooperation cooperation;

    @Column(nullable=false, length=128)
    private String username;

	@Column(nullable=false, length=512, unique=true)
    private String email;

    @Column(nullable=false, length=512)
    private String password;

    @Column
    private String firstName;

    @Column
    private String lastName;
    
    public User() {
	}

    public User(String username, String email) {
		this.username = username;
		this.email = email;
	}

	public Cooperation getCooperation() {
		return cooperation;
	}

	public void setCooperation(Cooperation cooperation) {
		this.cooperation = cooperation;
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

	@Override
    public String toString() {
        return String.format(
            "User[id=%s, firstName='%s', lastName='%s', cooperation='%s']",
            username, firstName, lastName, cooperation);
    }

}
