package power.web.hr.train;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.persistence.Id;

import com.googlecode.jsonplugin.JSONException;
import com.googlecode.jsonplugin.JSONUtil;
import com.sun.org.apache.commons.digester.ExtendedBaseRules;

import power.ear.comm.ejb.Ejb3Factory;
import power.ear.comm.ejb.PageObject;
import power.ejb.comm.TreeNode;
import power.ejb.hr.form.EmpInfo;
import power.ejb.hr.labor.HrCLaborMaterial;
import power.ejb.hr.train.HrCCertificateType;
import power.ejb.hr.train.HrCTrainSort;
import power.ejb.hr.train.HrJOuttrain;
import power.ejb.hr.train.HrJOuttrainDetail;
import power.ejb.hr.train.TrainSortMaint;
import power.ejb.hr.train.form.OutTrainForm;
import power.ejb.run.runlog.RunCSpecials;
import power.web.comm.AbstractAction;

@SuppressWarnings("serial")
public class TrainSortAction extends AbstractAction {
	private TrainSortMaint maintRemote;
	private HrJOuttrain outtrain;
	private int start;
	private int limit;

	public TrainSortAction() {
		maintRemote = (TrainSortMaint) factory
				.getFacadeRemote("TrainSortMaintImpl");
	}

	/**
	 * 费用类别维护
	 */
	public void saveTrainSortInfo() throws JSONException {
		String addStr = request.getParameter("isAdd");
		String updateStr = request.getParameter("isUpdate");
		String ids = request.getParameter("ids");
		List<Map> addMapList = (List<Map>) JSONUtil.deserialize(addStr);
		List<Map> updateMapList = (List<Map>) JSONUtil.deserialize(updateStr);
		List<HrCTrainSort> addList = new ArrayList<HrCTrainSort>();
		List<HrCTrainSort> updateList = new ArrayList<HrCTrainSort>();

		for (Map map : addMapList) {
			HrCTrainSort temp = this.parseTrainFeeSortInstance(map);
			addList.add(temp);
		}
		for (Map map : updateMapList) {
			HrCTrainSort temp = this.parseTrainFeeSortInstance(map);
			updateList.add(temp);
		}
		if (addList.size() > 0 || updateList.size() > 0 || ids != null) {
			maintRemote.saveTrainSort(addList, updateList, ids);
		}
		write("{success:true,msg:'数据保存修改成功！'}");
	}

	/**
	 * 将一映射转化为一费用类别对象
	 * 
	 * @param map
	 * @return
	 */
	public HrCTrainSort parseTrainFeeSortInstance(Map map) {
		HrCTrainSort temp = new HrCTrainSort();

		Long feeSortId = null;
		String feeSortName = null;
		Long orderBy = null;
		String isUse = "Y";
		String enterpriseCode = employee.getEnterpriseCode();

		if (map.get("feeSortId") != null)
			feeSortId = Long.parseLong(map.get("feeSortId").toString());
		if (map.get("feeSortName") != null)
			feeSortName = map.get("feeSortName").toString();
		if (map.get("orderBy") != null)
			orderBy = Long.parseLong(map.get("orderBy").toString());

		temp.setFeeSortId(feeSortId);
		temp.setFeeSortName(feeSortName);
		temp.setOrderBy(orderBy);
		temp.setIsUse(isUse);
		temp.setEnterpriseCode(enterpriseCode);
		return temp;
	}

	public void findTrainSortList() throws JSONException {
		String start = request.getParameter("start");
		String limit = request.getParameter("limit");
		PageObject pg = null;
		if (start != null && limit != null)
			pg = maintRemote.findTrainSortList(employee.getEnterpriseCode(),
					Integer.parseInt(start), Integer.parseInt(limit));
		else
			pg = maintRemote.findTrainSortList(employee.getEnterpriseCode());
		write(JSONUtil.serialize(pg));
	}

	/**
	 * 职工外出培训登记
	 * 
	 * @return
	 */
	public void saveOuttrainInfo() {
		String trainStartdate = request.getParameter("trainStartdate");
		String trainEnddate = request.getParameter("trainEnddate");
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		if (trainStartdate != null && !trainStartdate.equals(""))
			try {
				outtrain.setTrainStartdate(sdf.parse(trainStartdate));
			} catch (ParseException e) {
				e.printStackTrace();
			}
		if (trainEnddate != null && !trainEnddate.equals(""))
			try {
				outtrain.setTrainEnddate(sdf.parse(trainEnddate));
			} catch (ParseException e) {
				e.printStackTrace();
			}
		outtrain.setEnterpriseCode(employee.getEnterpriseCode());
		HrJOuttrain model = maintRemote.saveOuttrain(outtrain);
		write("{success:true,msg:'增加成功！',id:" + model.getTrainId() + "}");

	}

	public void updateOuttrainInfo() {

		String trainStartdate = request.getParameter("trainStartdate");
		String trainEnddate = request.getParameter("trainEnddate");
		String trainId = request.getParameter("trainId");
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		if (trainStartdate != null && !trainStartdate.equals(""))
			try {
				outtrain.setTrainStartdate(sdf.parse(trainStartdate));
			} catch (ParseException e) {
				e.printStackTrace();
			}
		if (trainEnddate != null && !trainEnddate.equals(""))
			try {
				outtrain.setTrainEnddate(sdf.parse(trainEnddate));
			} catch (ParseException e) {
				e.printStackTrace();
			}
		HrJOuttrain entity = maintRemote.findById(Long.parseLong(trainId));
		//add by wpzhu 20100702
		entity.setTrainInsititution(outtrain.getTrainInsititution());
		entity.setIsReceived(outtrain.getIsReceived());
		
		entity.setStationId(outtrain.getStationId());
		entity.setTechnologyTitlesTypeId(outtrain.getTechnologyTitlesTypeId());
		entity.setNowStationId(outtrain.getNowStationId());
		entity.setTrainSource(outtrain.getTrainSource());
		entity.setTrainSite(outtrain.getTrainSite());
		entity.setTrainContent(outtrain.getTrainContent());
		entity.setTrainStartdate(outtrain.getTrainStartdate());
		entity.setTrainEnddate(outtrain.getTrainEnddate());
		entity.setTrainCharacter(outtrain.getTrainCharacter());
		entity.setCertificateSort(outtrain.getCertificateSort());
		entity.setCertificateNo(outtrain.getCertificateNo());
		entity.setCertificateStartTime(outtrain.getCertificateStartTime());
		entity.setCertificateEndTime(outtrain.getCertificateEndTime());
		HrJOuttrain model = maintRemote.updateOuttrain(entity);
		write("{success:true,msg:'修改成功！',id:" + model.getTrainId() + "}");

	}

	public void findOutTrainList() throws JSONException {
		String trainStartdate = request.getParameter("trainStartdate");
		String trainEnddate = request.getParameter("trainEnddate");
		String start = request.getParameter("start");
		String limit = request.getParameter("limit");

		String certificateStartTime = request.getParameter("certificateStartTime");
		String certificateEndTime = request.getParameter("certificateEndTime");
		String personName = request.getParameter("personName");
		String deptName = request.getParameter("deptName");
		String certificateType = request.getParameter("certificateType");
		PageObject pg = null;
		if (start != null && limit != null)
			pg = maintRemote.findOutTrainList(trainStartdate, trainEnddate,
					certificateStartTime, certificateEndTime, personName,
					deptName, certificateType, employee.getEnterpriseCode(),
					Integer.parseInt(start), Integer.parseInt(limit));
		else
			pg = maintRemote.findOutTrainList(trainStartdate, trainEnddate,
					certificateStartTime, certificateEndTime, personName,
					deptName, certificateType, employee.getEnterpriseCode());
//		 System.out.println(JSONUtil.serialize(pg));
		if(pg!=null){
			write(JSONUtil.serialize(pg));
		}else{
			write("{list:[]}");
		}
		
		
	}

	public void getEmployeeInfo() throws JSONException {
		String workerCode = request.getParameter("workerCode");
		TrainSortMaint bll = (TrainSortMaint) Ejb3Factory.getInstance()
				.getFacadeRemote("TrainSortMaintImpl");

		EmpInfo empInfo = bll.getEmpInfoInfo(workerCode);
		write(JSONUtil.serialize(empInfo));

	}

	public void findStationIdList() throws JSONException {
		String start = request.getParameter("start");
		String limit = request.getParameter("limit");
		PageObject pg = null;
		if (start != null && limit != null)
			pg = maintRemote.findStationIdList(employee.getEnterpriseCode(),
					Integer.parseInt(start), Integer.parseInt(limit));
		else
			pg = maintRemote.findStationIdList(employee.getEnterpriseCode());
		write(JSONUtil.serialize(pg));
	}

	public void findTechnologyTitlesType() throws JSONException {
		String start = request.getParameter("start");
		String limit = request.getParameter("limit");
		PageObject pg = null;
		if (start != null && limit != null)
			pg = maintRemote.findTechnologyTitlesType(Integer.parseInt(start),
					Integer.parseInt(limit));
		else
			pg = maintRemote.findTechnologyTitlesType();
		write(JSONUtil.serialize(pg));
	}

	public void getOuttrainInfo() throws JSONException {
		String trainId = request.getParameter("trainId");
		TrainSortMaint bll = (TrainSortMaint) Ejb3Factory.getInstance()
				.getFacadeRemote("TrainSortMaintImpl");

		OutTrainForm dataForm = bll.getOuttrainInfo(Long.parseLong(trainId),
				employee.getEnterpriseCode());
		write(JSONUtil.serialize(dataForm));

	}

	/**
	 * 费用明细
	 * 
	 * @return
	 */
	public void saveOuttrainDetailInfo() throws JSONException {
		String addStr = request.getParameter("isAdd");
		String updateStr = request.getParameter("isUpdate");
		String ids = request.getParameter("ids");
		String trainId = request.getParameter("trainId");
		// System.out.println(addStr);
		List<Map> addMapList = (List<Map>) JSONUtil.deserialize(addStr);
		List<Map> updateMapList = (List<Map>) JSONUtil.deserialize(updateStr);
		List<HrJOuttrainDetail> addList = new ArrayList<HrJOuttrainDetail>();
		List<HrJOuttrainDetail> updateList = new ArrayList<HrJOuttrainDetail>();

		for (Map map : addMapList) {
			HrJOuttrainDetail temp = this.parseOuttrainDetailInstance(map);
			addList.add(temp);
		}
		for (Map map : updateMapList) {
			HrJOuttrainDetail temp = this.parseOuttrainDetailInstance(map);
			updateList.add(temp);
		}
		if (addList.size() > 0 || updateList.size() > 0 || ids != null) {
			maintRemote.saveOuttrainDetail(addList, updateList, ids);
		}
		write("{success:true,msg:'数据保存修改成功！'}");
	}

	/**
	 * 将一映射转化为一费用明细对象
	 * 
	 * @param map
	 * @return
	 */
	public HrJOuttrainDetail parseOuttrainDetailInstance(Map map) {
		HrJOuttrainDetail temp = new HrJOuttrainDetail();

		Long feeId = null;
		Long feeSortId = null;
		Double actualFee = null;
		String feeDept = null;
		String memo = null;
		String isUse = "Y";
		String enterpriseCode = employee.getEnterpriseCode();

		if (map.get("feeId") != null)
			feeId = Long.parseLong(map.get("feeId").toString());
		if (map.get("feeSortId") != null)
			feeSortId = Long.parseLong(map.get("feeSortId").toString());
		if (map.get("actualFee") != null)
			actualFee = Double.parseDouble(map.get("actualFee").toString());
		if (map.get("feeDept") != null)
			feeDept = map.get("feeDept").toString();
		if (map.get("memo") != null)
			memo = map.get("memo").toString();

		temp.setFeeId(feeId);
		temp.setFeeSortId(feeSortId);
		temp.setActualFee(actualFee);
		temp.setFeeDept(feeDept);
		temp.setMemo(memo);
		temp.setIsUse(isUse);
		temp.setEnterpriseCode(enterpriseCode);
		temp.setTrainId(Long.parseLong(map.get("trainId").toString()));
		return temp;
	}

	public void findOuttrainDetailList() throws JSONException {
		String trainId = request.getParameter("trainId");
		List result = maintRemote.findOuttrainDetailList(Long
				.parseLong(trainId), employee.getEnterpriseCode());
		if (result != null && result.size() > 0) {
			// System.out.println(JSONUtil.serialize(result));
			write(JSONUtil.serialize(result));
		} else {
			write("[]");
		}
	}

	/**
	 * *******************************证书类别维护 bjxu
	 * 0622********************************************
	 */
	public void saveCertificate() {
		String name = request.getParameter("name");
		String memo = request.getParameter("memo");
		String pid = request.getParameter("pid");
		HrCCertificateType model = new HrCCertificateType();
		model.setName(name);
		model.setMemo(memo);
		model.setEnterpriseCode(employee.getEnterpriseCode());
		Long Pid;
		if (pid == null || "".equals(pid))
			Pid = 0l;
		else {
			Pid = Long.parseLong(pid);
		}
		model.setPId(Pid);
		maintRemote.saveCertificateType(model);
		write("{success:true}");
	}

	public void updateCertificate() {
		String name = request.getParameter("name");
		String memo = request.getParameter("memo");
		String id = request.getParameter("id");
		HrCCertificateType model = maintRemote.findByCertificateTypeId(Long
				.parseLong(id));
		model.setName(name);
		model.setMemo(memo);
		maintRemote.updateCertificateType(model);
		write("{success:true}");

	}

	public void delCertificate() {

		String id = request.getParameter("id");
		if (maintRemote.findByPid(Long.parseLong(id))) {
			HrCCertificateType model = maintRemote.findByCertificateTypeId(Long
					.parseLong(id));
			maintRemote.deleteCertificateType(model);
			write("{success:true}");
		} else {
			write("{failure:true}");
		}
	}

	public void listCertificate() throws JSONException {
		String pid = request.getParameter("pid");
		try {
			List<TreeNode> list = maintRemote.findTreeCertificateTypeList(Long
					.parseLong(pid));
			write(JSONUtil.serialize(list));
		} catch (Exception exc) {
			exc.printStackTrace();
			write("[]");
		}

	}

	// end

	public int getStart() {
		return start;
	}

	public void setStart(int start) {
		this.start = start;
	}

	public int getLimit() {
		return limit;
	}

	public void setLimit(int limit) {
		this.limit = limit;
	}

	public HrJOuttrain getOuttrain() {
		return outtrain;
	}

	public void setOuttrain(HrJOuttrain outtrain) {
		this.outtrain = outtrain;
	}

}
