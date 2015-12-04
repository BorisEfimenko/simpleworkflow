package org.simpleworkflow.domain;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;

import org.springframework.format.annotation.DateTimeFormat;

@Entity
public class Report extends AbstractEntity {
	private static final long serialVersionUID = 1L;

	public String getName() {
		return name;
	}



	public void setName(String name) {
		this.name = name;
	}

	public Date getDate() {
		return date;
	}

	public void setDate(Date date) {
		this.date = date;
	}

	@Column(name="name", nullable = false)
        String name;
	
	@DateTimeFormat(pattern = "DD.MM.YY")
	@Column(name="report_date", nullable = false)
	private Date date ;    

    @Override
	public String toString() {
		return "Report [name=" + name + ", date=" + date + "]";
	}

}