/**
　* Copyright ustcsoft.com
　* All right reserved.
　*/
package power.web.hr.ca.leave.yearPlan.action;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

@SuppressWarnings("serial")
public class MetaData implements Serializable {
    private List<Map<String, Object>> fields;
    private List<Map<String, Object>> rows;
    private String root;
    private String id;
    private String totalProperty;
    
	public List<Map<String, Object>> getFields() {
        return fields;
    }
    public void setFields(List<Map<String, Object>> fields) {
        this.fields = fields;
    }
        public List<Map<String, Object>> getRows() {
            return rows;
        }
        public void setRows(List<Map<String, Object>> rows) {
            this.rows = rows;
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
