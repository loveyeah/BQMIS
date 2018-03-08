package power.ejb.equ.standardpackage;

@SuppressWarnings("serial")
public class EquCStandardEquForm  implements java.io.Serializable {
	
	private EquCStandardEqu model;
	private String equName;

	public String getEquName() {
		return equName;
	}
	public void setEquName(String equName) {
		this.equName = equName;
	}
	public EquCStandardEqu getModel() {
		return model;
	}
	public void setModel(EquCStandardEqu model) {
		this.model = model;
	}
}
