package power.ejb.manage.contract.form;

import java.util.Date;

public class ConApproveForm implements java.io.Serializable{
		
		/**
		 * 
		 */
		private static final long serialVersionUID = 1L;
		private Long id;
		private String stepName;
		private String opinion;
		private String caller;
		private String opinionTime;
		//送到日期
		private String comeTime;
		public Long getId() {
			return id;
		}
		public void setId(Long id) {
			this.id = id;
		}
		public String getOpinion() {
			return opinion;
		}
		public void setOpinion(String opinion) {
			this.opinion = opinion;
		}
		public String getCaller() {
			return caller;
		}
		public void setCaller(String caller) {
			this.caller = caller;
		}

		
		public String getOpinionTime() {
			return opinionTime;
		}
		public void setOpinionTime(String opinionTime) {
			this.opinionTime = opinionTime;
		}
		public String getStepName() {
			return stepName;
		}
		public void setStepName(String stepName) {
			this.stepName = stepName;
		}
		public String getComeTime() {
			return comeTime;
		}
		public void setComeTime(String comeTime) {
			this.comeTime = comeTime;
		}

		

	}

