package power.ejb.hr.labor;

import java.text.ParseException;
import java.util.List;

import javax.ejb.Remote;

import power.ear.comm.ejb.PageObject;
import power.ejb.hr.labor.form.LoborRegisterForm;

@Remote
public interface LaborManage {

	/**
	 * 查询部门劳保用品发放信息
	 * @param strDate
	 * @param enterpriseCode
	 * @return
	 */
	public DynaicQueryClass  findDeptSendList(String year,String season,String enterpriseCode,String[] plantname,String[] plantcount,String[] planID);
	
	/**
	 * 保存部门用品发放记录
	 * @param list
	 */
	public boolean saveDeptSendInfo(String year,String season,List<HrJLaborDeptRegister> addlist,List<HrJLaborDeptRegister>updatelist,String enterprise)throws ParseException;
	 
	
	public PageObject getDeptList(String enterpriseCode);
	
	/**
	 * modify by fyyang 100105
	 * @param deptCode
	 * @param laborMaterialId
	 * @param year
	 * @param season
	 * @param enterpriseCode
	 * @return
	 */
	public HrJLaborDeptRegister findByDeptAndLabor(String deptCode,Long laborMaterialId,String year,String season,String enterpriseCode);
	
	public List findLabor() throws Exception ;
	
	//public List findById(Sting  id) throws Exception;
	
	/**
	 * 劳保用品发放登记查询
	 * add by drdu 091225
	 * @param enterpriseCode
	 * @param deptId
	 * @param sendSeason
	 * @param rowStartIdxAndCount
	 * @return
	 */
	public PageObject findLaborRegisterList(String enterpriseCode,
			String deptId,String sendSeason,final int... rowStartIdxAndCount);
	
	/**
	 * 劳保用品发放登记保存
	 * @param method
	 * @param arrlist
	 */
	public void saveLoborRegisterRecord(String method,
			List<LoborRegisterForm> arrlist);
}
