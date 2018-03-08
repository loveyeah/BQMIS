package power.web.comm;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import power.web.comm.XlsConfigBean.Excel.Column;

public class XlsConfigBean {
	// 所有的业务配置信息
	private List<Excel> excels;

	public void addExcel(Excel excel) {
		if (excels == null) {
			excels = new ArrayList<Excel>();
		}
		// 根据ID来增加对应的业务配置信息
		excels.add(excel);
	}

	public List<Excel> getExcels() {
		return excels;
	}

	public Excel getExcel(String id) {
		Excel xls = null;
		for (Iterator<Excel> i = excels.iterator(); i.hasNext();) {
			xls = (Excel) i.next();
			if (id.equals(xls.getId())) {
				break;
			}
		}
		return xls;
	}

	static class Excel {

		private String id;

		private List<Column> columns;
		
		private HiddenColumn hiddenColumn;

		private List<Check> checks;

		public String getId() {
			return id;
		}

		public void setId(String id) {
			this.id = id;
		}

		public List<Check> getChecks() {
			return checks;
		}

		public List<Column> getColumns() {
			return columns;
		}

		public void addColumn(Column column) {
			if (columns == null) {
				columns = new ArrayList<Column>();
			}
			columns.add(column);
		}

		public void addCheck(Check check) {
			if (checks == null) {
				checks = new ArrayList<Check>();
			}
			checks.add(check);
		}

		static class Column {

			private String id;

			private String name;

			private String type;

			private String length;

			private String maxValue;

			private String minValue;

			private String empty;

			private String key;

			private String half;

			private String hidden;

			public String getEmpty() {
				return empty;
			}

			public void setEmpty(String empty) {
				this.empty = empty;
			}

			public String getId() {
				return id;
			}

			public void setId(String id) {
				this.id = id;
			}

			public String getKey() {
				return key;
			}

			public void setKey(String key) {
				this.key = key;
			}

			public String getLength() {
				return length;
			}

			public void setLength(String length) {
				this.length = length;
			}

			public String getMaxValue() {
				return maxValue;
			}

			public void setMaxValue(String maxValue) {
				this.maxValue = maxValue;
			}

			public String getMinValue() {
				return minValue;
			}

			public void setMinValue(String minValue) {
				this.minValue = minValue;
			}

			public String getName() {
				return name;
			}

			public void setName(String name) {
				this.name = name;
			}

			public String getType() {
				return type;
			}

			public void setType(String type) {
				this.type = type;
			}

			public String getHalf() {
				return half;
			}

			public void setHalf(String half) {
				this.half = half;
			}
		}

		static class HiddenColumn {

			private String id;

			public String getId() {
				return id;
			}

			public void setId(String id) {
				this.id = id;
			}
		}

		static class Check {

			private String method;

			private String clazz;

			private String args;

			public String getArgs() {
				return args;
			}

			public void setArgs(String args) {
				this.args = args;
			}

			public String getClazz() {
				return clazz;
			}

			public void setClazz(String clazz) {
				this.clazz = clazz;
			}

			public String getMethod() {
				return method;
			}

			public void setMethod(String method) {
				this.method = method;
			}
		}

		public HiddenColumn getHiddenColumn() {
			return hiddenColumn;
		}

		public void setHiddenColumn(HiddenColumn hiddenColumn) {
			this.hiddenColumn = hiddenColumn;
		}

		public void setChecks(List<Check> checks) {
			this.checks = checks;
		}
	}
}
