package power.ejb.report.form;

import power.ejb.report.JxlReportsRight;

	public class JxlReportsRightForm implements java.io.Serializable{
		private JxlReportsRight  model;
		private String workerName;
		public JxlReportsRight getModel() {
			return model;
		}
		public void setModel(JxlReportsRight model) {
			this.model = model;
		}
		public String getWorkerName() {
			return workerName;
		}
		public void setWorkerName(String workerName) {
			this.workerName = workerName;
		}
	}

