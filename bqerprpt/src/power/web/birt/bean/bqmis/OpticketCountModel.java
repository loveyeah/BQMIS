package power.web.birt.bean.bqmis;

import java.util.List;

import power.ejb.opticket.stat.RunJOpticketStat;
import power.ejb.opticket.stat.RunJOpticketStatDetail;

public class OpticketCountModel {
	
	private RunJOpticketStat baseModel;
	
	private List<RunJOpticketStatDetail> deptOnelist;
	
	private List<RunJOpticketStatDetail> deptTwolist;
	
	private List<RunJOpticketStatDetail> deptThreelist;
	
	private List<RunJOpticketStatDetail> deptFourlist;
	
	private List<RunJOpticketStatDetail> deptFivelist;

	public RunJOpticketStat getBaseModel() {
		return baseModel;
	}

	public void setBaseModel(RunJOpticketStat baseModel) {
		this.baseModel = baseModel;
	}

	public List<RunJOpticketStatDetail> getDeptOnelist() {
		return deptOnelist;
	}

	public void setDeptOnelist(List<RunJOpticketStatDetail> deptOnelist) {
		this.deptOnelist = deptOnelist;
	}

	public List<RunJOpticketStatDetail> getDeptTwolist() {
		return deptTwolist;
	}

	public void setDeptTwolist(List<RunJOpticketStatDetail> deptTwolist) {
		this.deptTwolist = deptTwolist;
	}

	public List<RunJOpticketStatDetail> getDeptThreelist() {
		return deptThreelist;
	}

	public void setDeptThreelist(List<RunJOpticketStatDetail> deptThreelist) {
		this.deptThreelist = deptThreelist;
	}

	public List<RunJOpticketStatDetail> getDeptFourlist() {
		return deptFourlist;
	}

	public void setDeptFourlist(List<RunJOpticketStatDetail> deptFourlist) {
		this.deptFourlist = deptFourlist;
	}

	public List<RunJOpticketStatDetail> getDeptFivelist() {
		return deptFivelist;
	}

	public void setDeptFivelist(List<RunJOpticketStatDetail> deptFivelist) {
		this.deptFivelist = deptFivelist;
	}
	

}
