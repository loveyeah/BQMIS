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
public class EquCWoToolsAdd implements java.io.Serializable {
	// Fields
	private EquCWoTools tool;
	private String typename;
	
	public String getTypename() {
		return typename;
	}
	public void setTypename(String typename) {
		this.typename = typename;
	}
	public EquCWoTools getTool() {
		return tool;
	}
	public void setTool(EquCWoTools tool) {
		this.tool = tool;
	}



}