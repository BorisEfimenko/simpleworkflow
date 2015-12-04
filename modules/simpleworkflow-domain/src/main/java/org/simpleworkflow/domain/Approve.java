package org.simpleworkflow.domain;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Entity
@Table(name = "APPROVE")
public class Approve extends AbstractEntity {
	private static final long serialVersionUID = 1L;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "APPROVE_TYPE_ID", nullable = false)
	private ApproveType approveType;

	@Column(name = "BEGIN_DATE")	
	private Date beginDate;
	
	@Column(name = "END_DATE")
	private Date endDate;

	@Column(name = "PROCESS_DEFINITION_KEY")
	private String processDefinitionKey;
	
	@Column(name = "PROCESS_DEFINITION_VERSION")
	private String processDefinitionVersion;
}
