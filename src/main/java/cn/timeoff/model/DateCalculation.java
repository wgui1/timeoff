package cn.timeoff.model;

import java.sql.Timestamp;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
public class DateCalculation {

    public enum YearCutoffTime {
    	Formal,
    	Calendar,
    	FromOnboard, 
    }

    @Id
    @GeneratedValue(strategy=GenerationType.AUTO)
    private long id;
    

    private YearCutoffTime year_cutoff;

    private Timestamp datetime;

}