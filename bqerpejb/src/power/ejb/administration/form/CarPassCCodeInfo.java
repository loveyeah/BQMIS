package power.ejb.administration.form;

import java.io.Serializable;

public class CarPassCCodeInfo implements Serializable {
	private int id;
	private String passCode;
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getPassCode() {
		return passCode;
	}
	public void setPassCode(String passCode) {
		this.passCode = passCode;
	}
	
}
