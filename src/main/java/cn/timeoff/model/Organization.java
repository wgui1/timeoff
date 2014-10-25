package cn.timeoff.model;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Collection;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;

import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.annotation.LastModifiedDate;

import cn.timeoff.core.NoValueSetError;

@Entity
public class Organization {
    @Id
    @GeneratedValue(strategy=GenerationType.AUTO)
    private long id;

    @ManyToOne
    @JoinColumn(name="cooperation_id")
    private Cooperation cooperation;

    @ManyToOne(optional=true)
    @JoinColumn(name="organization_id", nullable=true)
    private Organization upperLevel;
    
    @OneToMany(mappedBy="upperLevel")
    private Collection<Organization> subOrganizations = new ArrayList<Organization>();

    @OneToOne(mappedBy="organization", optional=false)
    private TimeoffSetting timeoffSetting;

    private String name;
    
    private boolean isTopLevel = false;

    @LastModifiedBy
    private String modifiedBy;

    @LastModifiedDate
    private Timestamp lastModifiedTime;

    public Organization() {
        super();
    }

    public Organization(Cooperation cooperation, String name) {
        super();
        this.cooperation = cooperation;
        this.name = name;
    }

    public Organization(Cooperation cooperation, String name, Organization upperLevel) {
        super();
        this.cooperation = cooperation;
        this.upperLevel = upperLevel;
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public Cooperation getCooperation() {
        return cooperation;
    }

    public void setCooperation(Cooperation cooperation) {
        this.cooperation = cooperation;
    }

    public Organization getUpperLevel() {
        return upperLevel;
    }

    public void setUpperLevel(Organization upperLevel) {
        this.upperLevel = upperLevel;
    }
    
    public TimeoffSetting getTimeoffSetting() {
        return timeoffSetting;
    }

    public void setTimeoffSetting(TimeoffSetting timeoffSetting) {
        this.timeoffSetting = timeoffSetting;
    }
    
    public TimeoffSetting getTimeoffSettingRecursive() throws NoValueSetError{
    	TimeoffSetting s = getTimeoffSetting();
    	if (s == null && !isTopLevel()) {
    		s = getUpperLevel().getTimeoffSettingRecursive();
    	}
    	if (s == null) {
    		throw new NoValueSetError("No timeoff setting found");
    	}
    	return s;
    }

	public Collection<Organization> getSubOrganizations() {
		return subOrganizations;
	}

	public void setSubOrganizations(Collection<Organization> subOrganizations) {
		this.subOrganizations = subOrganizations;
	}

	public void setTopLevel(boolean isTopLevel) {
		this.isTopLevel = isTopLevel;
	}

	public boolean isTopLevel() {
		return isTopLevel;
	}

	public String getModifiedBy() {
		return modifiedBy;
	}

	public Timestamp getLastModifiedTime() {
		return lastModifiedTime;
	}

}
