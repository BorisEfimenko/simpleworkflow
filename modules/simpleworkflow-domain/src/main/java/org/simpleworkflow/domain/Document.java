package org.simpleworkflow.domain;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Convert;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.simpleworkflow.domain.converter.BooleanToStringConverter;

@Entity
@Table(name = "DOCUMENT")
public class Document extends AbstractEntity {

  private static final long serialVersionUID = 1L;

  @Column(name = "DOCUMENT_NO", nullable = false)
  private String no;

  @Column(name = "DOC_DATE", nullable = false)
  @Temporal(TemporalType.DATE)
  private Date date;

  @Column(name = "APPROVED", columnDefinition="char", length=1, nullable = false)
  @Convert(converter = BooleanToStringConverter.class)
  private Boolean approved = false;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "DOC_TYPE_ID", nullable = false)
  private DocumentType docType;

  public String getNo() {
    return no;
  }

  public void setNo(String no) {
    this.no = no;
  }

  public Date getDate() {
    return date;
  }

  public void setDate(Date date) {
    this.date = date;
  }

  public DocumentType getDocType() {
    return docType;
  }

  public void setDocType(DocumentType docType) {
    this.docType = docType;
  }

  public Boolean isApproved() {
    return approved;
  }

  public void setApproved(Boolean approved) {
    this.approved = approved;
  }

  @Override
  public String toString() {
    StringBuilder builder = new StringBuilder();
    builder.append("Document [id=");
    builder.append(this.getId());
    builder.append(", no=");
    builder.append(no);
    builder.append(", date=");
    builder.append(date);
    builder.append(", docType=");
    builder.append(docType.getName());
    builder.append("]");
    return builder.toString();
  }

}
