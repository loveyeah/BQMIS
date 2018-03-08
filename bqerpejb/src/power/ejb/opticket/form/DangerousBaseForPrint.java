package power.ejb.opticket.form;

import java.util.List;

import power.ejb.opticket.bussiness.RunJOpMeasures;


public class DangerousBaseForPrint implements java.io.Serializable{
	/**
	 * 操作票基本信息
	 */
	private OpticketBaseForPrint model;
	/**
	 * 危险控制措施信息列表
	 */
	private List<RunJOpMeasures> list;
	public OpticketBaseForPrint getModel() {
		return model;
	}
	public void setModel(OpticketBaseForPrint model) {
		this.model = model;
	}
	public List<RunJOpMeasures> getList() {
		return list;
	}
	public void setList(List<RunJOpMeasures> list) {
		this.list = list;
	}
	

}
