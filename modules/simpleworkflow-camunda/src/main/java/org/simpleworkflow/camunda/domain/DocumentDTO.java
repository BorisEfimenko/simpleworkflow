package org.simpleworkflow.camunda.domain;

import org.joda.time.LocalDate;


public class DocumentDTO extends AbstractDTO{
  private static final long serialVersionUID = -4747355066722575444L;
  private String no;
  private LocalDate date;
  private Boolean approved = false;
  private Long docTypeId;
  private String docTypeName;

  public DocumentDTO() {
    super();
  }

  public DocumentDTO(Long id, Integer version, String no, LocalDate date, Boolean approved, Long docTypeId, String docTypeName) {
    super();
    this.id=id;
    this.version=version;
    this.no = no;
    this.date = date;
    this.approved = approved;
    this.docTypeId = docTypeId;
    this.docTypeName = docTypeName;    
  }

  public String getNo() {
    return no;
  }
  
  public void setNo(String no) {
    this.no = no;
  }
  
  public LocalDate getDate() {
    return date;
  }
  
  public void setDate(LocalDate date) {
    this.date = date;
  }
  
  public Boolean getApproved() {
    return approved;
  }
  
  public void setApproved(Boolean approved) {
    this.approved = approved;
  }
  
  public Long getDocTypeId() {
    return docTypeId;
  }
  
  public void setDocTypeId(Long docTypeId) {
    this.docTypeId = docTypeId;
  }
  
  public String getDocTypeName() {
    return docTypeName;
  }
  
  public void setDocTypeName(String docTypeName) {
    this.docTypeName = docTypeName;
  }

  @Override
  public int hashCode() {
    final int prime = 31;
    int result = super.hashCode();
    result = prime * result + ((approved == null) ? 0 : approved.hashCode());
    result = prime * result + ((date == null) ? 0 : date.hashCode());
    result = prime * result + ((docTypeId == null) ? 0 : docTypeId.hashCode());
    result = prime * result + ((docTypeName == null) ? 0 : docTypeName.hashCode());
    result = prime * result + ((no == null) ? 0 : no.hashCode());
    return result;
  }

  @Override
  public boolean equals(Object obj) {
    if (this == obj)
      return true;
    if (!super.equals(obj))
      return false;
    if (getClass() != obj.getClass())
      return false;
    DocumentDTO other = (DocumentDTO) obj;
    if (approved == null) {
      if (other.approved != null)
        return false;
    } else if (!approved.equals(other.approved))
      return false;
    if (date == null) {
      if (other.date != null)
        return false;
    } else if (!date.equals(other.date))
      return false;
    if (docTypeId == null) {
      if (other.docTypeId != null)
        return false;
    } else if (!docTypeId.equals(other.docTypeId))
      return false;
    if (docTypeName == null) {
      if (other.docTypeName != null)
        return false;
    } else if (!docTypeName.equals(other.docTypeName))
      return false;
    if (no == null) {
      if (other.no != null)
        return false;
    } else if (!no.equals(other.no))
      return false;
    return true;
  }

  @Override
  public String toString() {
    StringBuilder builder = new StringBuilder();
    builder.append("DocumentDTO [id=");
    builder.append(id);
    builder.append(", version=");
    builder.append(version);    
    builder.append(", no=");
    builder.append(no);
    builder.append(", date=");
    builder.append(date);
    builder.append(", approved=");
    builder.append(approved);
    builder.append(", docTypeId=");
    builder.append(docTypeId);
    builder.append(", docTypeName=");
    builder.append(docTypeName);
    builder.append("]");
    return builder.toString();
  }

 

}
