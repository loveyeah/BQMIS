package power.web.hr.salary;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import power.ear.comm.ejb.PageObject;
import power.ejb.hr.salary.HrJSalaryWage;
import power.ejb.hr.salary.HrJSalaryWageFacadeRemote;
import power.web.comm.AbstractAction;

import com.googlecode.jsonplugin.JSONException;
import com.googlecode.jsonplugin.JSONUtil;
@SuppressWarnings("serial")
public class SalaryWageAction extends AbstractAction
{
	private HrJSalaryWageFacadeRemote remote;
	
	public SalaryWageAction()
	{
		remote = (HrJSalaryWageFacadeRemote)factory.getFacadeRemote("HrJSalaryWageFacade");
	}
	
	
	public void calculateSalary() throws Exception
	{
		String flag = request.getParameter("flag");
		String deptId = request.getParameter("deptId");
		String yearMonth = request.getParameter("yearMonth");
		remote.calculateSalary(flag, deptId, yearMonth, employee.getEnterpriseCode());
	}
	
	
	
	public void getBasicPrimiumAndAward() throws JSONException
	{
		PageObject pg = new PageObject();
		String deptId = request.getParameter("deptId");
		String yearMonth = request.getParameter("yearMonth");
		
		String start = request.getParameter("start");
		String limit = request.getParameter("limit");
		if(start != null && limit != null)
			pg = remote.getBasicPrimiumAndAward(deptId, yearMonth, employee.getEnterpriseCode(),
					Integer.parseInt(start),Integer.parseInt(limit));
		else
			pg = remote.getBasicPrimiumAndAward(deptId, yearMonth, employee.getEnterpriseCode());
		write(JSONUtil.serialize(pg));
	}
	
	//判断历史工资不可修改 add by sychen 20100805 
	public void checkOldSalaryModify() {
		String deptId=request.getParameter("deptId");
		String str = remote.checkOldSalaryModify(deptId,employee.getEnterpriseCode());
		write(str);
	}
	
	public void saveBasicPrimiumAndAward() throws JSONException {
		String isUpdate = request.getParameter("isUpdate");
		List<Map<String, Object>> updateMapList = (ArrayList<Map<String, Object>>) JSONUtil.deserialize(isUpdate);
		List<HrJSalaryWage> list = new ArrayList<HrJSalaryWage>();
		if (updateMapList != null && updateMapList.size() > 0) {
			for (Map data : updateMapList) {
				HrJSalaryWage model = new HrJSalaryWage();
				model = this.parseSalaryWageInstance(data);
				list.add(model);
			}
		}
		remote.saveBasicPrimiumAndAward(list);
	}
	
	
	/**
	 * 将以映射转为一薪酬统计对象
	 * @param map
	 * @return
	 */
	private HrJSalaryWage parseSalaryWageInstance(Map map)
	{
		HrJSalaryWage temp = new HrJSalaryWage();
		if(map.get("wageId") != null)
			temp.setWageId(Long.parseLong(map.get("wageId").toString()));
		if(map.get("empId") != null)
			temp.setEmpId(Long.parseLong(map.get("empId").toString()));
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM");
		if(map.get("salaryMonth") != null)
			try {
				temp.setSalaryMonth(sdf.parse(map.get("salaryMonth").toString()));
			} catch (ParseException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		
		if(map.get("basisWage") != null) {
			if ("".equals(map.get("basisWage"))) {
				temp.setBasisWage(null);
			} else
			    temp.setBasisWage(Double.parseDouble(map.get("basisWage").toString()));
		}
		if(map.get("ageWage") != null) {
			if ("".equals(map.get("ageWage"))) {
				temp.setAgeWage(null);
			} else
			    temp.setAgeWage(Double.parseDouble(map.get("ageWage").toString()));
		}
		if(map.get("operationWage") != null) {
			if("".equals(map.get("operationWage"))) {
				temp.setOperationWage(null);
			} else
			    temp.setOperationWage(Double.parseDouble(map.get("operationWage").toString()));
		}
		if(map.get("remainWage") != null) {
			if ("".equals(map.get("remainWage"))) {
				temp.setRemainWage(null);
			} else
				temp.setRemainWage(Double.parseDouble(map.get("remainWage").toString()));
		}
		if(map.get("pointWage") != null) {
			if ("".equals(map.get("pointWage"))) {
				temp.setPointWage(null);
			} else
				temp.setPointWage(Double.parseDouble(map.get("pointWage").toString()));
		}
			
		if(map.get("overtimeWage") != null) {
			if ("".equals(map.get("overtimeWage"))) {
				temp.setOvertimeWage(null);
			} else 
				temp.setOvertimeWage(Double.parseDouble(map.get("overtimeWage").toString()));
		}
		if(map.get("deductionWage") != null) {
			if ("".equals(map.get("deductionWage"))) {
				temp.setDeductionWage(null);
			} else
				temp.setDeductionWage(Double.parseDouble(map.get("deductionWage").toString()));
		}
		if(map.get("totalWage") != null) {
			if ("".equals(map.get("totalWage"))) {
				temp.setTotalWage(null);
			} else
				temp.setTotalWage(Double.parseDouble(map.get("totalWage").toString()));
		}
			
		if(map.get("wageMemo") != null)
			temp.setWageMemo(map.get("wageMemo").toString());
		//update by sychen 20100805
		if(map.get("others") != null) {
			if ("".equals(map.get("others"))) {
				temp.setOthers(null);
			} else
				temp.setOthers(Double.parseDouble(map.get("others").toString()));
		}
		if(map.get("individualAwardsOne") != null) {
			if ("".equals(map.get("individualAwardsOne"))) {
				temp.setIndividualAwardsOne(null);
			} else
				temp.setIndividualAwardsOne(Double.parseDouble(map.get("individualAwardsOne").toString()));
		}
		if(map.get("individualAwardsTwo") != null) {
			if ("".equals(map.get("individualAwardsTwo"))) {
				temp.setIndividualAwardsTwo(null);
			} else
				temp.setIndividualAwardsTwo(Double.parseDouble(map.get("individualAwardsTwo").toString()));
		}
		if(map.get("monthAwards") != null) {
			if ("".equals(map.get("monthAwards"))) {
				temp.setMonthAwards(null);
			} else
				temp.setMonthAwards(Double.parseDouble(map.get("monthAwards").toString()));
		}
		if(map.get("bigAwards") != null) {
			if ("".equals(map.get("bigAwards"))) {
				temp.setBigAwards(null);
			} else
				temp.setBigAwards(Double.parseDouble(map.get("bigAwards").toString()));
		}
		if(map.get("totalIncome") != null) {
			if ("".equals(map.get("totalIncome"))) {
				temp.setTotalIncome(null);
			} else
				temp.setTotalIncome(Double.parseDouble(map.get("totalIncome").toString()));
		}
//		if(map.get("monthAwardCut") != null) {
//			if ("".equals(map.get("monthAwardCut"))) {
//				temp.setMonthAwardCut(null);
//			} else
//			    temp.setMonthAwardCut(Double.parseDouble(map.get("monthAwardCut").toString()));
//		}
//		if(map.get("monthAward") != null) {
//			if ("".equals(map.get("monthAward"))) {
//				temp.setMonthAward(null);
//			} else
//				temp.setMonthAward(Double.parseDouble(map.get("monthAward").toString()));
//		}
//		if(map.get("monthAwardMemo") != null) 
//			temp.setMonthAwardMemo(map.get("monthAwardMemo").toString());
//		if(map.get("bigAwardOneCut") != null) {
//			if ("".equals(map.get("bigAwardOneCut"))) {
//				temp.setBigAwardOneCut(null);
//			} else
//				temp.setBigAwardOneCut(Double.parseDouble(map.get("bigAwardOneCut").toString()));
//		}
//		if(map.get("bigAwardOne") != null) {
//			if ("".equals(map.get("bigAwardOne"))) {
//				temp.setBigAwardOne(null);
//			} else
//				temp.setBigAwardOne(Double.parseDouble(map.get("bigAwardOne").toString()));
//		}
//		if(map.get("bigAwardTwoCut") != null) {
//			if ("".equals(map.get("bigAwardTwoCut"))) {
//				temp.setBigAwardTwoCut(null);
//			} else
//				temp.setBigAwardTwoCut(Double.parseDouble(map.get("bigAwardTwoCut").toString()));
//		}
//		if(map.get("bigAwardTwo") != null) {
//			if ("".equals(map.get("bigAwardTwo"))) {
//				temp.setBigAwardTwo(null);
//			} else
//				temp.setBigAwardTwo(Double.parseDouble(map.get("bigAwardTwo").toString()));
//		}
//			
//		if(map.get("bigAwardMemo") != null)
//			temp.setBigAwardMemo(map.get("bigAwardMemo").toString());
		
		String enterpriseCode = employee.getEnterpriseCode();
		temp.setEnterpriseCode(enterpriseCode);
//		
//		if(map.get("coefficient") != null)
//			temp.setCoefficient(Double.parseDouble(map.get("coefficient").toString()));
		
		
		return temp;
	}
	/**
	 * 功能：查询员工工资明细
	 * add by qxjiao 20100806
	 
	 */
	public void getWageInfoDetail(){
		String start = request.getParameter("start");
		String limit = request.getParameter("limit");
		String id = request.getParameter("ids");
		String salary_month = request.getParameter("month");
		System.out.println(start);
		System.out.println(limit);
		System.out.println(id);
		List result = remote.getWageDetail(id,salary_month,Integer.parseInt(start),Integer.parseInt(limit));
		PageObject obj = new PageObject();
		obj.setList(result);
		obj.setTotalCount((long)id.split(",").length);
		try {
			write(JSONUtil.serialize(obj));
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}




