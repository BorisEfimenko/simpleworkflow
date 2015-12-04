package org.simpleworkflow.domain;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Table(name="APPROVE_TYPE")
public class ApproveType extends AbstractEntity {
	private static final long serialVersionUID = 1L;

	@Column(name = "APPROVE_TYPE_NAME", nullable = false)
	private String name;
	
	@Column(name = "DESCRIPTIONS")
	private String descriptions;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getDescriptions() {
		return descriptions;
	}

	public void setDescriptions(String descriptions) {
		this.descriptions = descriptions;
	}

}
