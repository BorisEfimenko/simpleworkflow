package org.simpleworkflow.domain;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import org.hibernate.annotations.Type;
import org.joda.time.LocalDate;

@Entity
@Table(name = "APPROVE")
public class Approve extends AbstractEntity {
	private static final long serialVersionUID = 1L;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "APPROVE_TYPE_ID", nullable = false)
	private ApproveType approveType;
	
	@Column(name = "BEGIN_DATE")	
	@Type(type = "org.jadira.usertype.dateandtime.joda.PersistentLocalDate")
	private LocalDate beginDate;
		
	@Column(name = "END_DATE")
	@Type(type = "org.jadira.usertype.dateandtime.joda.PersistentLocalDate")
	private LocalDate endDate;

	@Column(name = "PROCESS_DEFINITION_KEY")
	private String processDefinitionKey;
	
	@Column(name = "PROCESS_DEFINITION_VERSION")
	private String processDefinitionVersion;
	
	 public ApproveType getApproveType() {
	    return approveType;
	  }

	  public void setApproveType(ApproveType approveType) {
	    this.approveType = approveType;
	  }

	  public LocalDate getBeginDate() {
	    return beginDate;
	  }

	  public void setBeginDate(LocalDate beginDate) {
	    this.beginDate = beginDate;
	  }

	  public LocalDate getEndDate() {
	    return endDate;
	  }

	  public void setEndDate(LocalDate endDate) {
	    this.endDate = endDate;
	  }

	  public String getProcessDefinitionKey() {
	    return processDefinitionKey;
	  }

	  public void setProcessDefinitionKey(String processDefinitionKey) {
	    this.processDefinitionKey = processDefinitionKey;
	  }

	  public String getProcessDefinitionVersion() {
	    return processDefinitionVersion;
	  }

	  public void setProcessDefinitionVersion(String processDefinitionVersion) {
	    this.processDefinitionVersion = processDefinitionVersion;
	  }

    @Override
    public String toString() {
      StringBuilder builder = new StringBuilder();
      builder.append("Approve [id=");
      builder.append(this.getId());
      builder.append(", beginDate=");
      builder.append(beginDate);
      builder.append(", endDate=");
      builder.append(endDate);
      builder.append(", processDefinitionKey=");
      builder.append(processDefinitionKey);
      builder.append(", processDefinitionVersion=");
      builder.append(processDefinitionVersion);
      builder.append("]");      
      return builder.toString();
    }


}
