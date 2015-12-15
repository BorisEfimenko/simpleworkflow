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
	
  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "APPROVE_TYPE_ID", nullable = true)
  private ApproveType approveType;
  
  
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
  
  public ApproveType getApproveType() {
    return approveType;
  }

  
  public void setApproveType(ApproveType approveType) {
    this.approveType = approveType;
  }

  @Override
  public String toString() {
    StringBuilder builder = new StringBuilder();
    builder.append("DocType [id=");
    builder.append(this.getId());
    builder.append(", name=");
    builder.append(name);
    builder.append("]");
    return builder.toString();
  }


}
