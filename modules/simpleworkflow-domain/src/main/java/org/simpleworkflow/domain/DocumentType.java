package org.simpleworkflow.domain;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Entity
@Table(name="DOC_TYPE")
public class DocumentType extends AbstractEntity {
	private static final long serialVersionUID = 1L;

	@Column(name = "TYPE_NAME", nullable = false)
	private String name;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "APPROVE_TYPE_ID", nullable = true)
	private ApproveType approve;
	
	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("DocType [id=");
		builder.append(id);
		builder.append(", name=");
		builder.append(name);
		builder.append("]");
		return builder.toString();
	}


}
