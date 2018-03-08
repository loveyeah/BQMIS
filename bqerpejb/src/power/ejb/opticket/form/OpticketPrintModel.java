package power.ejb.opticket.form;

import java.util.List;

import power.ejb.opticket.RunJOpticketstep;

/**
 * @author sltang
 *
 */
public class OpticketPrintModel implements java.io.Serializable{
	/**
	 * 操作票基本信息
	 */
	private OpticketBaseForPrint model;
	/**
	 * 操作项目列表
	 */
	private List<RunJOpticketstep> list;
	public OpticketBaseForPrint getModel() {
		return model;
	}
	public void setModel(OpticketBaseForPrint model) {
		this.model = model;
	}
	public List<RunJOpticketstep> getList() {
		return list;
	}
	public void setList(List<RunJOpticketstep> list) {
		this.list = list;
	}
}
