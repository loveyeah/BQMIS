package power.ejb.hr.ca;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

@SuppressWarnings("serial")
public class MetaData implements Serializable {
	private List<Map<String, Object>> fields;
	private String root;
	private String id;
	private String totalProperty;
	public List<Map<String, Object>> getFields() {
		return fields;
	}
	public void setFields(List<Map<String, Object>> fields) {
		this.fields = fields;
	}
	public String getRoot() {
		return root;
	}
	public void setRoot(String root) {
		this.root = root;
	}
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getTotalProperty() {
		return totalProperty;
	}
	public void setTotalProperty(String totalProperty) {
		this.totalProperty = totalProperty;
	}

}
