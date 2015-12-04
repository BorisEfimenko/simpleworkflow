package org.simpleworkflow.domain;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Entity
@Table(name="DOCUMENT")
public class Document  extends AbstractEntity {
	private static final long serialVersionUID = 1L;

	@Column(name = "DOCUMENT_NO", nullable = false)
	private String no;
	
	@Column(name = "DOCUMENT_DATE", nullable = false)
	private Date date;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "DOC_TYPE_ID")
	private DocType docType;

    @Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("Document [id=");
		builder.append(id);
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
