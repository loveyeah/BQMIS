package power.web.hr.labor;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.googlecode.jsonplugin.JSONException;
import com.googlecode.jsonplugin.JSONUtil;

import jxl.Cell;
import jxl.Sheet;
import jxl.Workbook;

import power.ear.comm.ejb.PageObject;
import power.ejb.hr.archives.HrCPunish;
import power.ejb.hr.labor.HrCSsMain;
import power.ejb.hr.labor.HrCSsMainFacadeRemote;
import power.ejb.hr.labor.HrJSsHealth;
import power.ejb.hr.labor.HrJSsHealthFacadeRemote;
import power.ejb.hr.labor.HrJSsOldageFacadeRemote;
import power.ejb.hr.labor.HrJSsOldage;
import power.ejb.hr.labor.HrJSsYearcount;
import power.ejb.hr.labor.HrJSsYearcountFacadeRemote;
import power.web.comm.AbstractAction;
import power.web.comm.UploadFileAbstractAction;

@SuppressWarnings("serial")
public class LaborManageAction extends UploadFileAbstractAction  {

	private File xlsFile;
	private HrJSsOldageFacadeRemote oldRemote;
	private HrJSsHealthFacadeRemote healthRemote;
	private HrJSsYearcountFacadeRemote yearRemote;
	private HrCSsMainFacadeRemote mainRemote;
	private HrJSsOldage oldage;
	private HrJSsHealth health;
	private HrJSsYearcount yearCount;
	private File annex;

	public HrJSsOldage getOldage() {
		return oldage;
	}

	public void setOldage(HrJSsOldage oldage) {
		this.oldage = oldage;
	}

	public LaborManageAction() {
		oldRemote = (HrJSsOldageFacadeRemote) factory
				.getFacadeRemote("HrJSsOldageFacade");
		healthRemote = (HrJSsHealthFacadeRemote) factory
				.getFacadeRemote("HrJSsHealthFacade");
		yearRemote = (HrJSsYearcountFacadeRemote) factory
				.getFacadeRemote("HrJSsYearcountFacade");
		mainRemote = (HrCSsMainFacadeRemote) factory
				.getFacadeRemote("HrCSsMainFacade");
	}
  
	public void getAnnexVlaue()
	{
		String str="";
		String strYear = request.getParameter("strYear");// 年度
		String yearType = request.getParameter("yearType");// 年度类型
		String insuranceType = request.getParameter("insuranceType");// 保险类型
		
		Long mainId = oldRemote
		.findMainID(strYear, yearType, insuranceType);
		HrCSsMain main = new HrCSsMain();
		if(mainId!=null)
		{
		main=mainRemote.findById(mainId);
		 str=main.getAnnex();
		 if(!"".equals(str)&&str!=null)
		 {
		 write(str);
		 }else
		 {
			 write("");
		 }
		
		}
		
	}
	public void uploadfile()
	{
		String filePath = request.getParameter("filePath");
		String strYear = request.getParameter("mainYear");// 年度
		String yearType = request.getParameter("yearType");// 年度类型
		String insuranceType = request.getParameter("insuranceType");// 保险类型
		
		String Temp="";
		if (!filePath.equals("")) {
			String result = filePath.substring(filePath.lastIndexOf("\\") + 1);
			String fileName = result.replaceAll(" ", "");
			String[] filetemp = fileName.split("\\.");
			if (filetemp[1].equals("txt")) {
				filetemp[1] = ".doc";
				fileName = filetemp[0] + filetemp[1];
			}
			 Temp = uploadFile(annex, fileName, "project");
			
		}
		
		
		Long mainId = oldRemote
		.findMainID(strYear, yearType, insuranceType);
		HrCSsMain main = new HrCSsMain();
		if(mainId!=null)
		{
		main=mainRemote.findById(mainId);
		main.setAnnex(Temp);
		mainRemote.update(main);
		write("{success:true,msg:'"+Temp+"'}");
		}
		
	}
	/**
	 * 社保管理查询列表 add by drdu 20100607
	 * 
	 * @throws JSONException
	 */
	public void findLaborManageList() throws JSONException {
		String strYear = request.getParameter("strYear");// 年度
		String yearType = request.getParameter("yearType");// 年度类型
		String insuranceType = request.getParameter("insuranceType");// 保险类型
		String deptName = request.getParameter("deptName");// 部门
		String workName = request.getParameter("workName");// 姓名

		@SuppressWarnings("unused")
		Object objstart = request.getParameter("start");
		@SuppressWarnings("unused")
		Object objlimit = request.getParameter("limit");
		PageObject obj = new PageObject();
		if (objstart != null && objlimit != null) {
			int start = Integer.parseInt(request.getParameter("start"));
			int limit = Integer.parseInt(request.getParameter("limit"));

			if (insuranceType != null && insuranceType.equals("0")) {
				obj = healthRemote.findHealthList(strYear, yearType,
						insuranceType, deptName, workName, employee
								.getEnterpriseCode(), start, limit);
			} else if ((insuranceType != null && insuranceType.equals("1"))
					|| (insuranceType != null && insuranceType.equals("3"))
					|| (insuranceType != null && insuranceType.equals("4"))
					|| (insuranceType != null && insuranceType.equals("5"))) {
				obj = oldRemote.findOldageList(strYear, yearType,
						insuranceType, deptName, workName, employee
								.getEnterpriseCode(), start, limit);
			} else if (insuranceType != null && insuranceType.equals("2")) {
				obj = yearRemote.findYearcountList(strYear, yearType,
						insuranceType, deptName, workName, employee
								.getEnterpriseCode(), start, limit);
			}
		} else {
			if (insuranceType != null && insuranceType.equals("0")) {
				obj = healthRemote.findHealthList(strYear, yearType,
						insuranceType, deptName, workName, employee
								.getEnterpriseCode());
			} else if ((insuranceType != null && insuranceType.equals("1"))
					|| (insuranceType != null && insuranceType.equals("3"))
					|| (insuranceType != null && insuranceType.equals("4"))
					|| (insuranceType != null && insuranceType.equals("5"))) {
				obj = oldRemote.findOldageList(strYear, yearType,
						insuranceType, deptName, workName, employee
								.getEnterpriseCode());
			} else if (insuranceType != null && insuranceType.equals("2")) {
				obj = yearRemote.findYearcountList(strYear, yearType,
						insuranceType, deptName, workName, employee
								.getEnterpriseCode());
			}
		}
		
		
		
		write(JSONUtil.serialize(obj));
	}

	String method = "";
	String strYear = "";
	String yearType = "";
	String insuranceType = "";

	public void addOrUpdateMange() {
		method = request.getParameter("method");
		strYear = request.getParameter("strYear");// 年度
		yearType = request.getParameter("yearType");// 年度类型
		insuranceType = request.getParameter("insuranceType");// 保险类型
		if ((insuranceType == "1" || insuranceType.equals("1"))
				|| (insuranceType == "3" || insuranceType.equals("3"))
				|| (insuranceType == "4" || insuranceType.equals("4"))
				|| (insuranceType == "5" || insuranceType.equals("5"))) {
			this.addOrUpdateOldage();
		} else if (insuranceType == "2" || insuranceType.equals("2")) {
			this.addOrUpdateYearCount();
		} else if (insuranceType == "0" || insuranceType.equals("0")) {
			this.addOrUpdateHeath();
		}

	}

	public void addOrUpdateOldage() {

		if ((method == "add") || (method.equals("add"))) {
			Long mainId = oldRemote
					.findMainID(strYear, yearType, insuranceType);
			if (mainId == null) {
				HrCSsMain main = new HrCSsMain();
				main.setMainYear(strYear);
				main.setYearType(yearType);
				main.setInsuranceType(insuranceType);
				main.setImportBy(employee.getWorkerCode());
				main.setImportDate(new Date());
				main.setIsUse("Y");
				main.setEnterpriseCode(employee.getEnterpriseCode());
				mainRemote.save(main);

			} else {
				oldage.setMainId(mainId);
				oldage.setEnterpriseCode(employee.getEnterpriseCode());
				oldage.setIsUse("Y");
				oldRemote.save(oldage);
				write("{success:true,msg:'增加成功！'}");
			}

		} else {
			HrJSsOldage model = oldRemote.findById(oldage.getDetailId());
			model.setBanzuName(oldage.getBanzuName());
			model.setDeductCriterion(oldage.getDeductCriterion());
			model.setDeptName(oldage.getDeptName());
			model.setHalfYearSalary(oldage.getHalfYearSalary());
			model.setMemo(oldage.getMemo());
			model.setMonthBase(oldage.getMonthBase());
			model.setPersonelName(oldage.getPersonelName());
			model.setPersonnelCode(oldage.getPersonnelCode());
			model.setSelfSign(oldage.getSelfSign());
			model.setMonthStandard(oldage.getMonthStandard());
			oldRemote.update(model);
			write("{success:true,msg:'修改成功！'}");
		}

	}

	public void addOrUpdateYearCount() {

		if ((method == "add") || (method.equals("add"))) {
			Long mainId = oldRemote
					.findMainID(strYear, yearType, insuranceType);
			if (mainId == null) {
				HrCSsMain main = new HrCSsMain();
				main.setMainYear(strYear);
				main.setYearType(yearType);
				main.setInsuranceType(insuranceType);
				main.setImportBy(employee.getWorkerCode());
				main.setImportDate(new Date());
				main.setIsUse("Y");
				main.setEnterpriseCode(employee.getEnterpriseCode());
				mainRemote.save(main);

			} else {
				yearCount.setMainId(mainId);
				yearCount.setEnterpriseCode(employee.getEnterpriseCode());
				yearCount.setIsUse("Y");
				yearRemote.save(yearCount);
				write("{success:true,msg:'增加成功！'}");
			}

		} else {
			HrJSsYearcount model = yearRemote.findById(yearCount.getDetailId());
			model.setDeptName(yearCount.getDeptName());
			model.setIdentityCardNumber(yearCount.getIdentityCardNumber());
			model.setMonthEnterAccount(yearCount.getMonthEnterAccount());
			model
					.setMonthPersonnelAccount(yearCount
							.getMonthPersonnelAccount());
			model.setMonthTotal(yearCount.getMonthTotal());
			model.setPaymentMonth(yearCount.getPaymentMonth());
			model.setPersonelName(yearCount.getPersonelName());
			model.setSelfSign(yearCount.getSelfSign());
			model.setYearCountCode(yearCount.getYearCountCode());
			yearRemote.update(model);
			write("{success:true,msg:'修改成功！'}");
		}

	}

	public void addOrUpdateHeath() {

		if ((method == "add") || (method.equals("add"))) {
			Long mainId = oldRemote
					.findMainID(strYear, yearType, insuranceType);
			if (mainId == null) {
				HrCSsMain main = new HrCSsMain();
				main.setMainYear(strYear);
				main.setYearType(yearType);
				main.setInsuranceType(insuranceType);
				main.setImportBy(employee.getWorkerCode());
				main.setImportDate(new Date());
				main.setIsUse("Y");
				main.setEnterpriseCode(employee.getEnterpriseCode());
				mainRemote.save(main);

			} else {
				health.setMainId(mainId);
				health.setEnterpriseCode(employee.getEnterpriseCode());
				health.setIsUse("Y");
				healthRemote.save(health);
				write("{success:true,msg:'增加成功！'}");
			}

		} else {
			HrJSsHealth model = healthRemote.findById(health.getDetailId());
			model.setAccountNum(health.getAccountNum());
			model.setDeptName(health.getDeptName());
			model.setIdentityCardNumber(health.getIdentityCardNumber());
			model.setManualNumber(health.getManualNumber());
			model.setMedicareCardNumber(health.getMedicareCardNumber());
			model.setPersonelName(health.getPersonelName());
			healthRemote.update(model);
			write("{success:true,msg:'修改成功！'}");
		}

	}

	public void delLaborManageList() {
		String ids = request.getParameter("id");
		String type = request.getParameter("type");
		if ((type == "1" || type.equals("1"))
				|| (type == "3" || type.equals("3"))
				|| (type == "4" || type.equals("4"))
				|| (type == "5" || type.equals("5"))) {
			oldRemote.delete(ids);
		} else if (type == "0" || type.equals("0")) {
			healthRemote.delete(ids);
		} else if (type == "2" || type.equals("2")) {
			yearRemote.delete(ids);
		}

		write("{success:true,msg:'删除成功！'}");
	}

	// add bu ltong 全部删除
	public void delAllLaborManageList() {
		String strYear = request.getParameter("strYear");// 年度
		String yearType = request.getParameter("yearType");// 年度类型
		String insuranceType = request.getParameter("insuranceType");// 保险类型
		String deptName = request.getParameter("deptName");// 部门
		String workName = request.getParameter("workName");// 姓名
		if ((insuranceType == "1" || insuranceType.equals("1"))
				|| (insuranceType == "3" || insuranceType.equals("3"))
				|| (insuranceType == "4" || insuranceType.equals("4"))
				|| (insuranceType == "5" || insuranceType.equals("5"))) {
			oldRemote.delAllOldageList(strYear, yearType, insuranceType,
					deptName, workName, employee.getEnterpriseCode());
		} else if (insuranceType == "0" || insuranceType.equals("0")) {
			healthRemote.delAllHealthList(strYear, yearType, insuranceType,
					deptName, workName, employee.getEnterpriseCode());
		} else if (insuranceType == "2" || insuranceType.equals("2")) {
			yearRemote.delAllYearcountList(strYear, yearType, insuranceType,
					deptName, workName, employee.getEnterpriseCode());
		}

		write("{success:true,msg:'删除成功！'}");
	}

	public void importLaborManageInfo() throws IOException {
		String mainYear = request.getParameter("mainYear");
		String yearType = request.getParameter("yearType");
		String insuranceType = request.getParameter("insuranceType");
		HrCSsMain main = new HrCSsMain();
		main.setMainYear(mainYear);
		main.setYearType(yearType);
		main.setInsuranceType(insuranceType);
		main.setEnterpriseCode(employee.getEnterpriseCode());
		main.setImportBy(employee.getWorkerCode());
		main.setImportDate(new Date());
		if ("1".equals(insuranceType) || "3".equals(insuranceType)
				|| "4".equals(insuranceType)) {
			this.OldageInsurance(main, insuranceType);
		} else if ("0".equals(insuranceType)) {
			this.healthInsurance(main);
		} else if ("2".equals(insuranceType)) {
			this.yearcountInsurance(main);
		} else if("5".equals(insuranceType))
		{
			this.providentFundImp(main, insuranceType);
		}
	}

	public void OldageInsurance(HrCSsMain main, String insuranceType)
			throws IOException {
		InputStream is = null;
		Workbook workbook = null;
		String msg = "";
		List<HrJSsOldage> Laborlist = new ArrayList<HrJSsOldage>();
		try {
			is = new FileInputStream(xlsFile);
			workbook = Workbook.getWorkbook(is);// 得到工作薄
			Sheet sheet = workbook.getSheet(0);// 得到工作薄中的第一个工作表
			int rows = sheet.getRows();// 得到excel的总行数
			if (rows <= 1) {
				write("{success:true,msg:'无导入数据！'}");
			} else {
				for (int i = 1; i < rows; i++) {
					HrJSsOldage oldage = new HrJSsOldage();
					Cell[] cells = sheet.getRow(i);// i行的所有单元格
					// 第一列 序号为空跳出循环
					if ("".equals(cells[0].getContents())) {
						break;
					}
					// 第二列 个人编号personnel_code
					if (cells.length <= 1 || "".equals(cells[1].getContents())) {
						i++;
						msg = "第" + i + "行第二列，个人编号必须填写！";
						break;
					} else {
						oldage.setPersonnelCode(cells[1].getContents());
					}
					// 第三列 姓名personel_name
					if (cells.length <= 2 || "".equals(cells[2].getContents())) {
						i++;
						msg = "第" + i + "行第三列，姓名必须填写！";
						break;
					} else {
						// 去除空格
						String name = "";
						String cellString = cells[2].getContents();
						for (int ii = 0; ii < cellString.length(); ii++) {
							if (!"".equals(cellString.substring(ii, ii + 1))) {
								name += cellString.substring(ii, ii + 1);
							}
						}
						oldage.setPersonelName(cells[2].getContents());
					}
					// 第四列 部门dept_name
					if (cells.length <= 3 || "".equals(cells[3].getContents())) {
						i++;
						msg = "第" + i + "行第四列，部门必须填写！";
						break;
					} else {
						oldage.setDeptName(cells[3].getContents());
					}
					// 第五列 班组banzu_name
					if (cells.length <= 4 || "".equals(cells[4].getContents())) {
						i++;
						msg = "第" + i + "行第五列，班组必须填写！";
						break;
					} else {
						oldage.setBanzuName(cells[4].getContents());
					}
					// 第六列 本人上年实际月工资收入half_year_salary
					if (cells.length <= 5 || "".equals(cells[5].getContents())) {
						i++;
						msg = "第" + i + "行第六列，本人上年实际月工资收入必须填写！";
						break;
					} else {
						oldage.setHalfYearSalary(Double.parseDouble(cells[5]
								.getContents()));
					}
					// 第七列 月缴费基数month_base
					if (cells.length <= 6 || "".equals(cells[6].getContents())) {
						i++;
						msg = "第" + i + "行第七列，月缴费基数必须填写！";
						break;
					} else {
						oldage.setMonthBase(Double.parseDouble(cells[6]
								.getContents()));
					}
					// 第八列 个人扣款月报标准deduct_criterion
					if (cells.length <= 7 || "".equals(cells[7].getContents())) {
						i++;
						if ("1".equals(insuranceType)) {
							oldage
									.setDeductCriterion((Double
											.parseDouble(cells[6].getContents())) * 0.08);
						}
						if ("3".equals(insuranceType)) {
							oldage
									.setDeductCriterion((Double
											.parseDouble(cells[6].getContents())) * 0.01);

						}
						if ("4".equals(insuranceType)) {
							oldage
									.setDeductCriterion((Double
											.parseDouble(cells[6].getContents())) * 0.02);

						}
						if ("5".equals(insuranceType)) {
							oldage
									.setDeductCriterion((Double
											.parseDouble(cells[6].getContents())) * 0.08);

						}
					} else {
						oldage.setDeductCriterion(Double.parseDouble(cells[7]
								.getContents()));
					}
					// 第九列 职工本人签字self_sign
					if (cells.length >= 9) {
						oldage.setSelfSign(cells[8].getContents());
					}
					// 第十列 备注memo
					if (cells.length >= 10) {
						oldage.setMemo(cells[9].getContents());
					}
					oldage.setIsUse("Y");
					oldage.setEnterpriseCode(employee.getEnterpriseCode());
					Laborlist.add(oldage);
				}
				if ("".equals(msg)) {
					oldRemote.saveOrUpdateOldageInfo(main, Laborlist);
					write("{success:true,msg:'导入成功！'}");
				} else {
					write("{success:true,msg:'数据填写存在问题：" + msg + "'}");
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			workbook.close();
			is.close();
		}
	}
	
	
	public void providentFundImp(HrCSsMain main, String insuranceType)throws IOException 
	{
		InputStream is = null;
		Workbook workbook = null;
		String msg = "";
		List<HrJSsOldage> Laborlist = new ArrayList<HrJSsOldage>();
		try {
			is = new FileInputStream(xlsFile);
			workbook = Workbook.getWorkbook(is);// 得到工作薄
			Sheet sheet = workbook.getSheet(0);// 得到工作薄中的第一个工作表
			int rows = sheet.getRows();// 得到excel的总行数
			if (rows <= 1) {
				write("{success:true,msg:'无导入数据！'}");
			} else {
				for (int i = 1; i < rows; i++) {
					HrJSsOldage oldage = new HrJSsOldage();
					Cell[] cells = sheet.getRow(i);// i行的所有单元格
					// 第一列 序号为空跳出循环
					if (cells.length<1||"".equals(cells[0].getContents())) {
						break;
					}
					// 第二列 个人编号personnel_code
					if (cells.length <= 1 || "".equals(cells[1].getContents())) {
						i++;
						msg = "第" + i + "行第二列，公积金个人账户必须填写！";
						break;
					} else {
						oldage.setPersonnelCode(cells[1].getContents());
					}
					// 第三列 姓名personel_name
					if (cells.length <= 2 || "".equals(cells[2].getContents())) {
						i++;
						msg = "第" + i + "行第三列，姓名必须填写！";
						break;
					} else {
						// 去除空格
						String name = "";
						String cellString = cells[2].getContents();
						for (int ii = 0; ii < cellString.length(); ii++) {
							if (!"".equals(cellString.substring(ii, ii + 1))) {
								name += cellString.substring(ii, ii + 1);
							}
						}
						oldage.setPersonelName(cells[2].getContents());
					}
					// 第四列 部门dept_name
					if (cells.length <= 3 || "".equals(cells[3].getContents())) {
						i++;
						msg = "第" + i + "行第四列，部门必须填写！";
						break;
					} else {
						oldage.setDeptName(cells[3].getContents());
					}
					// 第五列 班组banzu_name
//					if (cells.length <= 4 || "".equals(cells[4].getContents())) {
//						i++;
//						msg = "第" + i + "行第五列，班组必须填写！";
//						break;
//					} else {
						oldage.setBanzuName(cells[4].getContents());
					//}
					// 第六列 本人上年实际月工资收入half_year_salary
					if (cells.length <= 5 || "".equals(cells[5].getContents())) {
						i++;
						msg = "第" + i + "行第六列，工资必须填写！";
						break;
					} else {
						oldage.setHalfYearSalary(Double.parseDouble(cells[5]
								.getContents()));
					}

					// 第七列 个人扣款月报标准deduct_criterion
					if (cells.length <= 6 || "".equals(cells[6].getContents())) {
						i++;
						oldage
									.setDeductCriterion((Double
											.parseDouble(cells[5].getContents())) * 0.08);

						
					} else {
						oldage.setDeductCriterion(Double.parseDouble(cells[6]
								.getContents()));
					}
					// 第八列 企业月标准MONTH_STANDARD
					if (cells.length <= 7 || "".equals(cells[7].getContents())) {
						i++;
						msg = "第" + i + "行第七列，企业月标准必须填写！";
						break;
					} else {
						oldage.setMonthStandard(Double.parseDouble(cells[7]
								.getContents()));
					}
					// 第九列 备注memo 
					if (cells.length >= 10) {
						oldage.setMemo(cells[9].getContents());
					}
					// 第十列 职工本人签字self_sign
					if (cells.length >= 11) {
						oldage.setSelfSign(cells[10].getContents());
					}
					oldage.setIsUse("Y");
					oldage.setEnterpriseCode(employee.getEnterpriseCode());
					Laborlist.add(oldage);
				}
				if ("".equals(msg)) {
					oldRemote.saveOrUpdateOldageInfo(main, Laborlist);
					write("{success:true,msg:'导入成功！'}");
				} else {
					write("{success:true,msg:'数据填写存在问题：" + msg + "'}");
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			workbook.close();
			is.close();
		}
	}

	public void healthInsurance(HrCSsMain main) throws IOException {
		InputStream is = null;
		Workbook workbook = null;
		String msg = "";
		List<HrJSsHealth> Laborlist = new ArrayList<HrJSsHealth>();
		try {
			is = new FileInputStream(xlsFile);
			workbook = Workbook.getWorkbook(is);// 得到工作薄
			Sheet sheet = workbook.getSheet(0);// 得到工作薄中的第一个工作表
			int rows = sheet.getRows();// 得到excel的总行数
			if (rows <= 1) {
				write("{success:true,msg:'无导入数据！'}");
			} else {
				for (int i = 1; i < rows - 1; i++) {
					HrJSsHealth health = new HrJSsHealth();
					Cell[] cells = sheet.getRow(i);
					// 第一列 序号为空跳出循环
					if ("".equals(cells[0].getContents())) {
						break;
					}
					// 第二列 部门dept_name
					if (cells.length <= 1 || "".equals(cells[1].getContents())) {
						i++;
						msg = "第" + i + "行第二列，部门必须填写！";
						break;
					} else {
						health.setDeptName(cells[1].getContents());
					}
					// 第三列 姓名personel_name
					if (cells.length <= 2 || "".equals(cells[2].getContents())) {
						i++;
						msg = "第" + i + "行第三列，姓名必须填写！";
						break;
					} else {
						health.setPersonelName(cells[2].getContents());
					}
					// 第四列 手册号码manual_number
					if (cells.length <= 3 || "".equals(cells[3].getContents())) {
						i++;
						msg = "第" + i + "行第四列，手册号码必须填写！";
						break;
					} else {
						health.setManualNumber(cells[3].getContents());
					}
					// 第五列 医保卡号medicare_card_number
					if (cells.length <= 4 || "".equals(cells[4].getContents())) {
						i++;
						msg = "第" + i + "行第五列，医保卡号必须填写！";
						break;
					} else {
						health.setMedicareCardNumber(cells[4].getContents());
					}
					// 第六列 身份证号identity_card_number
					/*if (cells.length <= 5 || "".equals(cells[5].getContents())) {
						i++;
						msg = "第" + i + "行第六列，身份证号必须填写！";
						break;
					} else {
						health.setIdentityCardNumber(cells[5].getContents());
					}*/
					health.setIdentityCardNumber(cells[5].getContents());
					// 第七列 合计account_num
					if (cells.length <= 6 || "".equals(cells[6].getContents())) {
						i++;
						msg = "第" + i + "行第七列，合计必须填写！";
						break;
					} else {
						health.setAccountNum(Double.parseDouble(cells[6]
								.getContents()));
					}
					health.setIsUse("Y");
					health.setEnterpriseCode(employee.getEnterpriseCode());
					Laborlist.add(health);
				}
				if ("".equals(msg)) {
					healthRemote.saveOrUpdateHealthInfo(main, Laborlist);
					write("{success:true,msg:'导入成功！'}");
				} else {
					write("{success:true,msg:'数据填写存在问题：" + msg + "'}");
				}
			}

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			workbook.close();
			is.close();
		}

	}

	public void yearcountInsurance(HrCSsMain main) throws IOException {
		InputStream is = null;
		Workbook workbook = null;
		String msg = "";
		List<HrJSsYearcount> Laborlist = new ArrayList<HrJSsYearcount>();
		try {
			is = new FileInputStream(xlsFile);
			workbook = Workbook.getWorkbook(is);
			Sheet sheet = workbook.getSheet(0);
			int rows = sheet.getRows();
			if (rows <= 1) {
				write("{success:true,msg:'无导入数据！'}");
			} else {
				for (int i = 1; i <= rows - 1; i++) {
					HrJSsYearcount yearcount = new HrJSsYearcount();
					Cell[] cells = sheet.getRow(i);
					// 第一列 序号为空跳出循环
					if ("".equals(cells[0].getContents())) {
						break;
					}
					// 第二列 年金帐号year_count_code
					if (cells.length <= 1 || "".equals(cells[1].getContents())) {
						i++;
						msg = "第" + i + "行第二列，年金帐号必须填写！";
						break;
					} else {
						yearcount.setYearCountCode(cells[1].getContents());
					}
					// 第三列 身份证号identity_card_number
					if (cells.length <= 2 || "".equals(cells[2].getContents())) {
						i++;
						msg = "第" + i + "行第三列，身份证号必须填写！";
						break;
					} else {
						yearcount.setIdentityCardNumber(cells[2].getContents());
					}
					// 第四列 部门dept_name
					if (cells.length <= 3 || "".equals(cells[3].getContents())) {
						i++;
						msg = "第" + i + "行第四列，部门必须填写！";
						break;
					} else {
						yearcount.setDeptName(cells[3].getContents());
					}
					// 第五列 姓名personel_name
					if (cells.length <= 4 || "".equals(cells[4].getContents())) {
						i++;
						msg = "第" + i + "行第五列，姓名必须填写！";
						break;
					} else {
						yearcount.setPersonelName(cells[4].getContents());
					}
					// 第六列 月企业缴费month_enter_account
					if (cells.length <= 5 || "".equals(cells[5].getContents())) {
						i++;
						msg = "第" + i + "行第六列，月企业缴费必须填写！";
						break;
					} else {
						yearcount.setMonthEnterAccount(Double
								.parseDouble(cells[5].getContents()));
					}
					// 第七列 月个人缴费month_personnel_account
					if (cells.length <= 6 || "".equals(cells[6].getContents())) {
						i++;
						msg = "第" + i + "行第七列，月个人缴费必须填写！";
						break;
					} else {
						yearcount.setMonthPersonnelAccount(Double
								.parseDouble(cells[6].getContents()));
					}
					// 第八列 月缴费合计month_total
					if (cells.length <= 7 || "".equals(cells[7].getContents())) {
						i++;
						msg = "第" + i + "行第八列，月缴费合计必须填写！";
						break;
					} else {
						yearcount.setMonthTotal(Double.parseDouble(cells[7]
								.getContents()));
					}
					// 第九列 缴费月payment_month
					if (cells.length <= 8 || "".equals(cells[8].getContents())) {
						i++;
						msg = "第" + i + "行第九列，缴费月必须填写！";
						break;
					} else {
						yearcount.setPaymentMonth(cells[8].getContents());
					}
					// 第十列 本人签字self_sign
					if (cells.length >= 10) {
						yearcount.setSelfSign(cells[9].getContents());
					}
					yearcount.setIsUse("Y");
					yearcount.setEnterpriseCode(employee.getEnterpriseCode());
					Laborlist.add(yearcount);
				}
				if ("".equals(msg)) {
					yearRemote.saveOrUpdateYearcountInfo(main, Laborlist);
					write("{success:true,msg:'导入成功！'}");
				} else {
					write("{success:true,msg:'数据填写存在问题：" + msg + "'}");
				}
			}

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			workbook.close();
			is.close();
		}

	}

	public File getXlsFile() {
		return xlsFile;
	}

	public void setXlsFile(File xlsFile) {
		this.xlsFile = xlsFile;
	}

	public HrJSsHealth getHealth() {
		return health;
	}

	public void setHealth(HrJSsHealth health) {
		this.health = health;
	}

	public HrJSsYearcount getYearCount() {
		return yearCount;
	}

	public void setYearCount(HrJSsYearcount yearCount) {
		this.yearCount = yearCount;
	}

	public File getAnnex() {
		return annex;
	}

	public void setAnnex(File annex) {
		this.annex = annex;
	}

	
	
}