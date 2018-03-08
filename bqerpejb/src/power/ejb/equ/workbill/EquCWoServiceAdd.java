package power.ejb.equ.workbill;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * EquCWoService entity.
 * 
 * @author MyEclipse Persistence Tools
 */
@SuppressWarnings("serial")
public class EquCWoServiceAdd implements java.io.Serializable {
	// Fields
	private EquCWoService service;
	private String typename;
	
	public EquCWoService getService() {
		return service;
	}
	public void setService(EquCWoService service) {
		this.service = service;
	}
	public String getTypename() {
		return typename;
	}
	public void setTypename(String typename) {
		this.typename = typename;
	}



}