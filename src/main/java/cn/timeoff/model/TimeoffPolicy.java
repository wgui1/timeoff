package cn.timeoff.model;

import java.sql.Timestamp;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;

@Entity
public class TimeoffPolicy {

    @Id
    @GeneratedValue(strategy=GenerationType.AUTO)
    private long id;

    @CreatedBy
    private String createdBy;

    @CreatedDate
    private Timestamp createdDate;

    private int renewal;
    private int carryOver;
    private int accuralInterval;
    private int accuralBy;
    private int accuralLimit;
    private boolean active;
    private boolean balanceRequired;
    private boolean autoApproval;
    private boolean requestEanbled;

    public TimeoffPolicy() {
        super();
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    public int getRenewal() {
        return renewal;
    }

    public void setRenewal(int renewal) {
        this.renewal = renewal;
    }

    public int getCarryOver() {
        return carryOver;
    }

    public void setCarryOver(int carryOver) {
        this.carryOver = carryOver;
    }

    public int getAccuralInterval() {
        return accuralInterval;
    }

    public void setAccuralInterval(int accuralInterval) {
        this.accuralInterval = accuralInterval;
    }

    public int getAccuralBy() {
        return accuralBy;
    }

    public void setAccuralBy(int accuralBy) {
        this.accuralBy = accuralBy;
    }

    public int getAccuralLimit() {
        return accuralLimit;
    }

    public void setAccuralLimit(int accuralLimit) {
        this.accuralLimit = accuralLimit;
    }

    public boolean isBalanceRequired() {
        return balanceRequired;
    }

    public void setBalanceRequired(boolean balanceRequired) {
        this.balanceRequired = balanceRequired;
    }

    public boolean isAutoApproval() {
        return autoApproval;
    }

    public void setAutoApproval(boolean autoApproval) {
        this.autoApproval = autoApproval;
    }

    public boolean isRequestEanbled() {
        return requestEanbled;
    }

    public void setRequestEanbled(boolean requestEanbled) {
        this.requestEanbled = requestEanbled;
    }

}