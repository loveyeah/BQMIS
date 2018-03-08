package power.ejb.opticket.form;

import java.util.List;

import power.ejb.opticket.bussiness.RunJOpFinwork;

public class WorkBaseForPrint implements java.io.Serializable{
	/**
	 * 操作票基本信息
	 */
	private OpticketBaseForPrint model;
	/**
	 * 电气倒闸操作后完成的工作信息列表
	 */
	private List<RunJOpFinwork> list;
	public OpticketBaseForPrint getModel() {
		return model;
	}
	public void setModel(OpticketBaseForPrint model) {
		this.model = model;
	}
	public List<RunJOpFinwork> getList() {
		return list;
	}
	public void setList(List<RunJOpFinwork> list) {
		this.list = list;
	}
}
