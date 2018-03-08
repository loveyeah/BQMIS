package power.ejb.productiontec.relayProtection.form;

import power.ejb.productiontec.relayProtection.PtJdbhCDzxmwh;

import com.sun.corba.se.spi.ior.iiop.JavaCodebaseComponent;

public class ProtectProjectForm implements java.io.Serializable
{
	private PtJdbhCDzxmwh pjcd;
	private String protectTypeName;
	public PtJdbhCDzxmwh getPjcd() {
		return pjcd;
	}
	public void setPjcd(PtJdbhCDzxmwh pjcd) {
		this.pjcd = pjcd;
	}
	public String getProtectTypeName() {
		return protectTypeName;
	}
	public void setProtectTypeName(String protectTypeName) {
		this.protectTypeName = protectTypeName;
	}
	
}