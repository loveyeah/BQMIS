package power.web.hr.salary;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import com.googlecode.jsonplugin.JSONException;
import com.googlecode.jsonplugin.JSONUtil;


import power.ear.comm.ejb.PageObject;
import power.ejb.hr.HrJEmpInfo;
import power.ejb.hr.salary.HrCSalaryType;
import power.ejb.hr.salary.HrCSalaryTypeFacadeRemote;
import power.ejb.hr.salary.HrJSalary;
import power.ejb.hr.salary.HrJSalaryDetail;
import power.ejb.hr.salary.HrJSalaryDetailFacadeRemote;
import power.ejb.hr.salary.HrJSalaryFacadeRemote;
import power.ejb.hr.salary.form.SalaryAmountForm;
import power.web.comm.AbstractAction;
import sun.management.counter.Variability;

@SuppressWarnings("serial")
public class SalaryAmountAction extends AbstractAction
{
	// 工资类别接口
	private HrCSalaryTypeFacadeRemote typeRemote;
	//薪酬主表接口
	private HrJSalaryFacadeRemote salaryRemote;
	//薪酬明细接口
	private HrJSalaryDetailFacadeRemote detailRemote;
	
	public SalaryAmountAction()
	{
		typeRemote = (HrCSalaryTypeFacadeRemote)factory.getFacadeRemote("HrCSalaryTypeFacade");
		salaryRemote = (HrJSalaryFacadeRemote)factory.getFacadeRemote("HrJSalaryFacade");
		detailRemote = (HrJSalaryDetailFacadeRemote)factory.getFacadeRemote("HrJSalaryDetailFacade");
	}
	
	public void findEmpSalaryList()
	{
		String deptIdStr = request.getParameter("deptId");
		String month = request.getParameter("month");
		Long deptId = null;
		if(deptIdStr != null && !deptIdStr.equals(""))
			deptId = Long.parseLong(deptIdStr);
		
		//数据拼写开始
		 String str = "{'data':[";
//		//通过部门id和月份到薪酬主表中查找数据库中是否已有数据
//		boolean isExist = salaryRemote.judgetSalaryExist(deptId, month, employee.getEnterpriseCode());
		// 增加数据
//		str = this.addValue(isExist,deptId, month,str);
		PageObject pg = salaryRemote.findSalayByDept(month, deptId, employee.getEnterpriseCode());
		int typeCount = pg.getTotalCount().intValue();
		List dataList = pg.getList();
		if(dataList != null && dataList.size() > 2)
		{			
			for (int i = 2; i < dataList.size(); i++) {
				str += "{";
				Object[] data = (Object[])dataList.get(i);
				Object[] dataIndex = (Object[]) dataList.get(0);
				for (int j = 0; j <= data.length - 2; j++) {
					String cellValue = "";
					if(data[j] != null)
						cellValue = data[j].toString();
					str += "'" + dataIndex[j].toString() + "':'" + cellValue + "',";
				}			
				if(typeCount > 0)
				{
					Double totalSalary = 0.0;
					for(int z = 4; z <= 4 + typeCount - 1;z++)
					{
						Double salary = 0.0;
						if(data[z] != null && !data[z].equals(""))
							salary = Double.parseDouble(data[z].toString());
						totalSalary += salary;
					}
					str += "'" + dataIndex[data.length - 1].toString() + "':'" + totalSalary + "',";
				}
				if(str.endsWith(","))
				{
					str = str.substring(0,str.length() - 1);
				}
				str += "},";
			}			
		}
		// 数据增加结束	 	 
		 if(str.equals("{'data':[")){
			 str += "]";
		 }else{
			 str = str.substring(0, str.length()-1) + "]";
		 }
		 
//		List<HrCSalaryType> headerList = typeRemote.findUseSalaryTypeList(employee.getEnterpriseCode());
		str += ",'columModel':[new Ext.grid.RowNumberer({header : '序号',width : 32}),";
		str += "{'header' : '薪酬Id','hidden' : true,'width':100,'dataIndex' : 'salaryId','align':'center'},"
			 +"{'header' : '部门Id','hidden' : true,'width':100,'dataIndex' : 'deptId','align':'center'},"
			 +"{'header' : '人员Id','hidden' : true,'width':100,'dataIndex' : 'empId','align':'center'},"
			 +"{'header' : '人员姓名','width':100,'dataIndex' : 'empName','align':'center'},"
			 +"{'header' : '月份','width':100,'dataIndex' : 'month','align':'center'}";	
		if(typeCount > 0)
		{
			if(dataList != null && dataList.size() > 1)
			{
				Object[] header = (Object[])dataList.get(1);
				Object[] dataIndex = (Object[])dataList.get(0);
				for(int i = 4; i <= 4 + typeCount -1; i++)
				{
					System.out.println("aaa " + header[i].toString() + "  bbb");
					str += ",{'header':'" + header[i].toString() + "','width': 100,'dataIndex': '"
						+ dataIndex[i].toString() + "','align':'center'";
					Long typeId = Long.parseLong(dataIndex[i].toString().substring(4));
					HrCSalaryType type = typeRemote.findById(typeId);
					if(type.getIsInput() != null && type.getIsInput().equals("1"))
						str += ",'editor':new Ext.form.NumberField({allowDecimals : true,decimalPrecision : 0})";
					str += "}";
				}
			}				
		}
		
		 str += ",{'header' : '总工资','width':100,'dataIndex' : 'totalSalary','align':'center'}";
		 str += "],'fieldsNames' : [";
		 if(dataList != null && dataList.size() > 0)
		 {
			 Object[] feilds = (Object[])dataList.get(0);
			 for(int i = 0; i<= feilds.length - 1; i++)
			 {
				 str += "{'name':'" + feilds[i] + "'},";
			 }
			 if(str.endsWith(","))
			{
				str = str.substring(0,str.length() - 1);
			}
		 }
		 str += "]}";
		 System.out.println(str);
		 write(str);		  		
	}
	
	
	private String addValue(boolean isExist,Long deptId, String month,
			 String str) {
		// 薪酬主表中存有deptId部门month的数据
		if(isExist)
			;
		// 薪酬主表中未存deptId部门month的数据
		else 
		{
			List<HrJEmpInfo> empList = salaryRemote.findEmpByDeptId(deptId, employee.getEnterpriseCode());
			if(empList != null && empList.size() > 0)
			{
				Iterator<HrJEmpInfo> empIt = empList.iterator();
				while(empIt.hasNext())
				{
					str += "{";
					HrJEmpInfo emp = empIt.next();
					str += "'empId':'" + emp.getEmpId() + "',"
						+ "'empName':'" +emp.getChsName() + "',"
						+ "'deptId':'" + emp.getDeptId() + "',"
						+ "'salaryId':'',"
						+ "'month':'" + month + "'";
					
					// 工资类别列表
					List<HrCSalaryType> typeList = typeRemote.findUseSalaryTypeList(employee.getEnterpriseCode());
					if(typeList != null && typeList.size() > 0)
					{
						Iterator<HrCSalaryType> typeIt = typeList.iterator();
						while(typeIt.hasNext())
						{
							HrCSalaryType type = typeIt.next();
							str += ",'type" + type.getSalaryTypeId() + "':"
							// 判断是计算值还是输入值，若计算则在维护表中计算，若输入，则先不处理  值先用固定值取代
								+ "'1'";
						}
					}
					// 总工资
					str += ",'totalSalary':'" + "999'";
					//数据拼写结束
					str += "},";
				}
			}			
		}
		return str;
	}
	
	/**
	 * 保存薪酬总额及明细薪酬
	 * @throws JSONException 
	 */
	public void saveSalaryAmount() throws JSONException
	{
		String mod = request.getParameter("mod");
		String method = request.getParameter("method");
		
		Object modObj = JSONUtil.deserialize(mod);
		List<Map> modList = (List<Map>)modObj;
		
		List<SalaryAmountForm> arrlist = new ArrayList<SalaryAmountForm>();
		for(Map map : modList)
		{
			SalaryAmountForm temp = this.parseToSalaryAmountForm(map);
			arrlist.add(temp);
		}
		
		salaryRemote.saveSalaryAmountRec(method,arrlist);
	}
	
	/**
	 * 将一个映射转成SalaryAmountForm对象
	 * @param map
	 * @return
	 */
	public SalaryAmountForm parseToSalaryAmountForm(Map map)
	{
		SalaryAmountForm temp = new SalaryAmountForm();
		// 薪酬主表
		HrJSalary salary = new HrJSalary();
		// 薪酬明细表列表
		List<HrJSalaryDetail> detailList = new ArrayList<HrJSalaryDetail>();
		//薪酬主表中的数据
		Long salaryId = null;
		Long empId = null;
		Date salaryMonth = null;
		Double totalSalary = null;
		Double adjust = null;
		String modifyBy = employee.getWorkerCode();
		Date modifyDate = new Date();
		String isUse = "Y";
		String enterpriseCode = employee.getEnterpriseCode();
		
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM");
		
		if(map.get("salaryId") != null && !map.get("salaryId").toString().equals(""))
			salaryId = Long.parseLong(map.get("salaryId").toString());
		if(map.get("empId") != null && !map.get("empId").toString().equals("empId"))
			empId = Long.parseLong(map.get("empId").toString());
		if(map.get("salaryMonth") != null && !map.get("salaryMonth").toString().equals(""))
			try {
				salaryMonth = sdf.parse(map.get("salaryMonth").toString());
			} catch (ParseException e) {
				e.printStackTrace();
			}
		if(map.get("totalSalary") != null && !map.get("totalSalary").toString().equals(""))
			totalSalary = Double.parseDouble(map.get("totalSalary").toString());
		
		// 薪酬主表对象设置属性值
		salary.setSalaryId(salaryId);
		salary.setEmpId(empId);
		salary.setSalaryMonth(salaryMonth);
		salary.setTotalSalary(totalSalary);
		salary.setAdjust(adjust);
		salary.setModifyBy(modifyBy);
		salary.setModifyDate(modifyDate);
		salary.setIsUse(isUse);
		salary.setEnterpriseCode(enterpriseCode);
		
		temp.setSalary(salary);
		
		// 薪酬明细表
		Long salaryDetailId = null;
		Long salaryTypeId = null;
		Double salaryMoney = null;
		String memo = null;
		
		// 类别组成的字符串
		String typeStr = "";
		// 类别对应金额组成的字符串
		String moneyStr = "";
		if (map.get("salaryTypeId") != null
				&& !map.get("salaryTypeId").toString().equals("")) {
			typeStr = map.get("salaryTypeId").toString();
			typeStr = typeStr.substring(1,typeStr.length() -1);
			System.out.println(typeStr);
			
			moneyStr = map.get("salaryMoney").toString();
			moneyStr = moneyStr.substring(1,moneyStr.length() -1);
			System.out.println(moneyStr);
		}
		String[] typeArr = typeStr.split(",");
		String[] moneyArr = moneyStr.split(",");
		int arrNum = typeArr.length;
		for(int i = 0 ;i < arrNum ; i++)
		{
			HrJSalaryDetail detail = new HrJSalaryDetail();
			detail.setSalaryId(salaryId);
			detail.setSalaryTypeId(Long.parseLong(typeArr[i].trim()));
			if(moneyArr[i] != null && !moneyArr[i].equals(""))
				detail.setSalaryMoney(Double.parseDouble(moneyArr[i].trim()));
			else 
				detail.setSalaryMoney(0.0);
			detail.setMemo(memo);
			detail.setModifyBy(modifyBy);
			detail.setModifyDate(modifyDate);
			detail.setEnterpriseCode(enterpriseCode);
			
			detailList.add(detail);
		}
		
		temp.setDetailList(detailList);
		return temp;
	}
	
}