package power.ejb.manage.contract.form;

import java.util.Date;

@SuppressWarnings("serial")
public class ConDocForm implements java.io.Serializable{
	private Long conDocId;
	private Long keyId;
	private String docType;
	private String docName;
	private String docContent;
	private String docMemo;
	private String oriFileName;
	private String oriFileExt;
	private String lastModifiedBy;
	private String lastModifiedDate;
	private String enterpriseCode;
	private String isUse;
	private String lastModifiedName;
	private Long conModifyId;
	
	private String oriFile;
	public Long getConDocId() {
		return conDocId;
	}
	public void setConDocId(Long conDocId) {
		this.conDocId = conDocId;
	}
	public Long getKeyId() {
		return keyId;
	}
	public void setKeyId(Long keyId) {
		this.keyId = keyId;
	}
	public String getDocType() {
		return docType;
	}
	public void setDocType(String docType) {
		this.docType = docType;
	}
	public String getDocName() {
		return docName;
	}
	public void setDocName(String docName) {
		this.docName = docName;
	}
	
	public String getDocMemo() {
		return docMemo;
	}
	public void setDocMemo(String docMemo) {
		this.docMemo = docMemo;
	}
	public String getOriFileName() {
		return oriFileName;
	}
	public void setOriFileName(String oriFileName) {
		this.oriFileName = oriFileName;
	}
	public String getOriFileExt() {
		return oriFileExt;
	}
	public void setOriFileExt(String oriFileExt) {
		this.oriFileExt = oriFileExt;
	}
	public String getLastModifiedBy() {
		return lastModifiedBy;
	}
	public void setLastModifiedBy(String lastModifiedBy) {
		this.lastModifiedBy = lastModifiedBy;
	}
	public String getLastModifiedDate() {
		return lastModifiedDate;
	}
	public void setLastModifiedDate(String lastModifiedDate) {
		this.lastModifiedDate = lastModifiedDate;
	}
	public String getEnterpriseCode() {
		return enterpriseCode;
	}
	public void setEnterpriseCode(String enterpriseCode) {
		this.enterpriseCode = enterpriseCode;
	}
	public String getIsUse() {
		return isUse;
	}
	public void setIsUse(String isUse) {
		this.isUse = isUse;
	}
	public String getLastModifiedName() {
		return lastModifiedName;
	}
	public void setLastModifiedName(String lastModifiedName) {
		this.lastModifiedName = lastModifiedName;
	}
	public Long getConModifyId() {
		return conModifyId;
	}
	public void setConModifyId(Long conModifyId) {
		this.conModifyId = conModifyId;
	}
	public String getOriFile() {
		return oriFile;
	}
	public void setOriFile(String oriFile) {
		this.oriFile = oriFile;
	}
	public String getDocContent() {
		return docContent;
	}
	public void setDocContent(String docContent) {
		this.docContent = docContent;
	}
}
