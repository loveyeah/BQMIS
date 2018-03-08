package power.ejb.opticket.form;

import java.util.List;

import power.ejb.opticket.bussiness.RunJOpStepcheck;

public class CheckBaseForPrint implements java.io.Serializable{
	/**
	 * 操作票基本信息
	 */
	private OpticketBaseForPrint model;
	/**
	 * 电气倒闸操作前检查的步骤列表
	 */
	private List<RunJOpStepcheck> list;
	
	
	public OpticketBaseForPrint getModel() {
		return model;
	}
	public void setModel(OpticketBaseForPrint model) {
		this.model = model;
	}
	public List<RunJOpStepcheck> getList() {
		return list;
	}
	public void setList(List<RunJOpStepcheck> list) {
		this.list = list;
	}
	
}
