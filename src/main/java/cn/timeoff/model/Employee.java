package cn.timeoff.model;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;

import cn.timeoff.security.model.User;

@Entity
public class Employee {

    @Id
    @GeneratedValue(strategy=GenerationType.AUTO)
    private long id;

    @ManyToOne
    @JoinColumn(name="cooperation_id")
    private Cooperation cooperation;

    @ManyToOne
    @JoinColumn(name="organization_id")
    private Organization organization;
    
    @OneToOne
    @JoinColumn(name="user_id")
    private User user;

}
