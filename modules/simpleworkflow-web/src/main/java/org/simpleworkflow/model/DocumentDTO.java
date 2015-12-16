package org.simpleworkflow.model;

import org.joda.time.LocalDate;


public class DocumentDTO extends AbstractDTO{

  private static final long serialVersionUID = 1L;

  private String no;
  private LocalDate date;
  private Boolean approved = false;
  private Long docTypeId;
  private String docTypeName;
  
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
  
  public static long getSerialversionuid() {
    return serialVersionUID;
  }
 

}
