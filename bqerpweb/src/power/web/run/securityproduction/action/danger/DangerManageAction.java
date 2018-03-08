package power.web.run.securityproduction.action.danger;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Map;

import jxl.Cell;
import jxl.Sheet;
import jxl.Workbook;
import power.ear.comm.ejb.PageObject;
import power.ejb.run.securityproduction.danger.SpJDangerDeptRegister;
import power.ejb.run.securityproduction.danger.SpJDangerDeptRegisterFacadeRemote;
import power.ejb.run.securityproduction.danger.SpJDangerDeptValue;
import power.ejb.run.securityproduction.danger.SpJDangerDeptValueFacadeRemote;
import power.web.comm.AbstractAction;

import com.googlecode.jsonplugin.JSONException;
import com.googlecode.jsonplugin.JSONUtil;

public class DangerManageAction extends AbstractAction{
	private Integer start;
	private Integer limit;
	private File xlsFile;
	private SpJDangerDeptValueFacadeRemote remote;
	private SpJDangerDeptRegisterFacadeRemote registerRemote;
	private static final String[] BIGDANGERDEPTVALUE_COLUMN_NAMES = { "危险源名称", "完成时间",
		"评估部门", "责任人", "备注"};
	public DangerManageAction(){
		remote=(SpJDangerDeptValueFacadeRemote)factory.getFacadeRemote("SpJDangerDeptValueFacade");
		registerRemote=(SpJDangerDeptRegisterFacadeRemote)factory.getFacadeRemote("SpJDangerDeptRegisterFacade");
	}
	/**
	 * 通过录入部门Id
	 * 查询L值B2值列表
	 */
	public void findDangerManageList(){
		
		String id = request.getParameter("dangerId");
		long dangerId = Long.parseLong(id);
	//	System.out.println("dangerId--------------------"+dangerId);
		List<SpJDangerDeptValue> list=remote.findByDangerId(dangerId,employee.getEnterpriseCode());
	//	System.out.println(list.size());
		if (list.size()==0) {//判断在该企业编码下，录入部门Id的L值B2值记录是否存在
			list.clear();//不存在，new新对象返回list
			SpJDangerDeptValue modelL=new SpJDangerDeptValue();
			modelL.setDangerId(dangerId);
			modelL.setScoreSort("1");
			list.add(modelL);
			SpJDangerDeptValue modelB2=new SpJDangerDeptValue();
			modelB2.setDangerId(dangerId);
			modelB2.setScoreSort("2");
			list.add(modelB2);
		}
		if(list.size()==1){
			
		}
		try {
			PageObject obj=new PageObject();
			obj.setList(list);
			obj.setTotalCount(Long.parseLong(list.size()+""));
	//		System.out.println(JSONUtil.serialize(obj));
			write(JSONUtil.serialize(obj));
		} catch (JSONException e) {
			e.printStackTrace();
		}
	}
	/**
	 * 保存L值B2值信息
	 * 判断是否已存在
	 * 是，更新
	 * 否，保存
	 * @throws JSONException 
	 */
	public void saveDangerDeptValue() throws JSONException
	{
		SpJDangerDeptValue model;
		String str = request.getParameter("isUpdate");
		Object object = JSONUtil.deserialize(str);
	//	System.out.println("==================="+str);
		List<Map> list = (List<Map>) object;
		if (list!=null&&list.size()>0) {
		for (Map map : list) {
			model=new SpJDangerDeptValue();
			if (map.get("id")!=null) {
				model.setId(Long.parseLong(map.get("id").toString()));
			}
			model.setDangerId(Long.parseLong(map.get("dangerId").toString()));
			if (map.get("score1")!=null) {
				model.setScore1(map.get("score1").toString());
			}
			if (map.get("score2")!=null) {
				model.setScore2(map.get("score2").toString());
			}
			if (map.get("score3")!=null) {
				model.setScore3(map.get("score3").toString());
			}
			if (map.get("score4")!=null) {
				model.setScore4(map.get("score4").toString());
			}
			if (map.get("score5")!=null) {
				model.setScore5(map.get("score5").toString());
			}
			if (map.get("score6")!=null) {
				model.setScore6(map.get("score6").toString());
			}
			if (map.get("score7")!=null) {
				model.setScore7(map.get("score7").toString());
			}
			if (map.get("score8")!=null) {
				model.setScore8(map.get("score8").toString());
			}
			if (map.get("score9")!=null) {
				model.setScore9(map.get("score9").toString());
			}
			if (map.get("score10")!=null) {
				model.setScore10(map.get("score10").toString());
			}
			model.setIsUse("Y");
			model.setScoreSort(map.get("scoreSort").toString());
			model.setLastModifiedBy(employee.getWorkerCode());
			Calendar c=Calendar.getInstance();
			model.setLastModifiedDate(c.getTime());
			model.setEnterpriseCode(employee.getEnterpriseCode());
			if (model.getId()!=null) {//Id不为空，数据库中已保存，执行更新
				remote.update(model);
			}else{
				remote.save(model);
			}
		}
	//	System.out.println("成功");
		write("{success:true,msg:'操作成功！'}");
		}
	}
	
	/**
	 * 通过年度查询重大危险源落实部门信息
	 */
	public void findDeptRegisterListByDangerYear(){
		String Year=request.getParameter("rewardYear");
		String status=request.getParameter("status");
		String deptCode=null;
		String choose=request.getParameter("choose");
		if ("1".equals(choose)) {
			deptCode=employee.getDeptCode();
		}
		List list=registerRemote.findByDangerYear(Year,status,deptCode, employee.getEnterpriseCode(), start,limit);
		Long count=registerRemote.getTotalCount(Year,status,deptCode, employee.getEnterpriseCode());
		PageObject pageObject=new PageObject();
		pageObject.setList(list);
		pageObject.setTotalCount(count);
		try {
			write(JSONUtil.serialize(pageObject));
		} catch (JSONException e) {
			e.printStackTrace();
		}
	}
	/**
	 * 保存重大危险源落实部门信息
	 * @throws JSONException
	 * @throws ParseException 
	 * @throws ParseException
	 */
	public void saveDangerDeptRegister()throws JSONException, ParseException{
		SpJDangerDeptRegister ddr;
		SimpleDateFormat format; 
		String str = request.getParameter("isUpdate");
		Object object = JSONUtil.deserialize(str);
		List<Map> list = (List<Map>) object;
		if (list!=null&&list.size()>0) {
			for (Map map : list) {
				if (map.get("dangerId")!=null) {
					ddr=registerRemote.findById(Long.parseLong(map.get("dangerId").toString()));
					ddr.setDangerId(Long.parseLong(map.get("dangerId").toString()));
				}else{
					ddr=new SpJDangerDeptRegister();
				}
				if (map.get("dangerName")!=null) {
					ddr.setDangerName(map.get("dangerName").toString());
				}
				if (map.get("orderBy")!=null&&!map.get("orderBy").equals("")) {
					System.out.println("---"+map.get("orderBy").toString()+"+++");
					ddr.setOrderBy(Long.parseLong(map.get("orderBy").toString()));
				}
				if (map.get("memo")!=null) {
					ddr.setMemo(map.get("memo").toString());
				}
				if (map.get("status")!=null) {
					ddr.setStatus(map.get("status").toString());
				}
				if (map.get("workFlowNo")!=null) {
					ddr.setWorkFlowNo(Long.parseLong(map.get("workFlowNo").toString()));
				}
				if (map.get("assessDept")!=null) {
					ddr.setAssessDept(map.get("assessDept").toString());
				}
				if (map.get("chargeBy")!=null) {
					ddr.setChargeBy(map.get("chargeBy").toString());
				}
				if (map.get("finishDate")!=null) {
					String date=map.get("finishDate").toString();
					format = new SimpleDateFormat("yyyy-MM-dd");
					ddr.setFinishDate(format.parse(date));
					//ddr.setDangerYear(date.substring(0,4));
				}
				System.out.println(map.get("dangerYear")==null);
				System.out.println("214314:"+map.get("dangerYear").toString());
				if(map.get("dangerYear")!=null){
					ddr.setDangerYear(map.get("dangerYear").toString());
				}
				if (map.get("isUse")!=null) {
					ddr.setIsUse(map.get("isUse").toString());
				}else{
					ddr.setIsUse("Y");
				}
					ddr.setLastModifiedBy(employee.getWorkerCode());
					Date date=new Date();
					ddr.setLastModifiedDate(date);
					ddr.setEnterpriseCode(employee.getEnterpriseCode());
				if (ddr.getDangerId()!=null) {
					registerRemote.update(ddr);
				}else if(ddr.getDangerId()==null&&"Y".equals(ddr.getIsUse())){
					registerRemote.save(ddr);
				}
			}
		}
		write("{success:true,msg:'操作成功！'}");
	}
	/**
	 * 
	 * @param column_indexs
	 * @param cells
	 * @param type
	 * @return
	 */
	private String addColumnIndex(int[] column_indexs, Cell[] cells) {
		for (int i = 0; i < cells.length; i++) {
			boolean isError = true;
			
				for (int j = 0; j < BIGDANGERDEPTVALUE_COLUMN_NAMES.length; j++) {
					if (BIGDANGERDEPTVALUE_COLUMN_NAMES[j].equals(cells[i].getContents())) {
						column_indexs[i] = j;
						isError = false;
					}
			}
		
				if (isError) {
				return "{success:true,msg:'" + cells[i].getContents()
						+ "列不是要导入的具体列！'}";
			}
		}
		return null;
		}
	/**
	 * 导入重大危险源落实部门信息
	 * @throws IOException
	 */
	public void importBigDangerDeptValue() throws IOException{
		InputStream is=null;
		Workbook workbook=null;
		SpJDangerDeptRegister ddr=null;
		String msg = "";
		String dutyNames="";
		String deptNames="";
		String dutyCr="";
		String deptCr="";
		String dangerNames="";
		String dangerYear=request.getParameter("dangerYear");
		List<SpJDangerDeptRegister> ddrList = new ArrayList<SpJDangerDeptRegister>();
		try {
			is = new FileInputStream(xlsFile);
			workbook = Workbook.getWorkbook(is);// 得到工作薄
			Sheet sheet = workbook.getSheet(0);// 得到工作薄中的第一个工作表
			int rows = sheet.getRows();// 得到excel的总行数
			
			if (rows == 0) {
				msg = "{success:true,msg:'无数据进行导入!'}";
			} else if (rows == 1) {
				msg = "{success:true,msg:'文件除一列头行外，至少还需一行数据!'}";
			} else {
				int[] column_indexs = new int[sheet.getRow(0).length];
				SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
				String columnError = null;
				columnError = addColumnIndex(column_indexs, sheet.getRow(0)
						);
				// 列名是否存在问题
//				if (columnError != null)
//					return columnError;
				outer:
				for (int i = 1; i < rows; i++) {
					ddr = new SpJDangerDeptRegister();
					Cell[] cells = sheet.getRow(i);// i行的所有单元格
					// if(cells.length > column_indexs.length){
					// i++;
					// msg = "第" + i + "行的数据超过列头行！";
					// break;
					// }
					for (int j = 0; j < column_indexs.length; j++) {
						// 1:危险源名称
						 	if (column_indexs[j] == 0) {
							if (cells.length > j
									&& !"".equals(cells[j].getContents())) {
								if (cells[j].getContents().length() > 20) {
									i++;
									msg+= "第" + i + "行危险源名称超过20位！";
									//break;
								} else {
									ddr.setDangerName(cells[j].getContents());
								}
							}else{
									//msg+= "第" + i + "行危险源名称为空！";
									continue outer;
									
							}
						}
						// 2：完成时间
						else if (column_indexs[j] == 1) {
							if (cells.length > j
									&& !"".equals(cells[j].getContents())) {
								String[] partPositiveDateArr = cells[j]
										.getContents().split("-");
								if (partPositiveDateArr.length != 3
										|| partPositiveDateArr[0].length() != 4) {
									i++;
									msg+= "第" + i + "行完成时间格式填写不正确！";
									break;
								}
								if (partPositiveDateArr[1].length() == 1)
									partPositiveDateArr[1] = "0"
											+ partPositiveDateArr[1];
								if (partPositiveDateArr[2].length() == 1)
									partPositiveDateArr[2] = "0"
											+ partPositiveDateArr[2];
								try {
									ddr.setFinishDate(df
											.parse(partPositiveDateArr[0] + "-"
													+ partPositiveDateArr[1]
													+ "-"
													+ partPositiveDateArr[2]));
									//ddr.setDangerYear(partPositiveDateArr[0]);
								} catch (Exception e1) {
									i++;
									msg+= "第" + i + "行完成时间格式填写不正确！";
								}
							}
						}
							// 3:部门名称
							else if (column_indexs[j] == 2) {
								if (cells.length > j&& !"".equals(cells[j].getContents())) {
									if (!"".equals(cells[j].getContents())) {
										ddr.setAssessDept(cells[j].getContents());
									}
								}else{
									msg+= "第" + i + "行部门名称为空！";
								}
							}
						 // 4:负责人名称
							else if (column_indexs[j] == 3) {
								if (cells.length > j
										&& !"".equals(cells[j].getContents())) {
									ddr.setChargeBy(cells[j].getContents());
								}else{
									//msg+= "第" + i + "行负责人名称为空！";
								}
							}
					// 5:备注
					if (column_indexs[j] == 4) {
						if (cells.length > j
								&& !"".equals(cells[j].getContents())) {
							ddr.setMemo(cells[j].getContents());
						}
					}
					}
					ddr.setDangerYear(dangerYear);
					ddr.setIsUse("Y");
					ddr.setEnterpriseCode(employee.getEnterpriseCode());
					ddr.setStatus("0");
					ddr.setLastModifiedDate(new java.util.Date());
					ddr.setLastModifiedBy(employee.getWorkerCode());
					ddrList.add(ddr);
					String temp=sheet.getCell(3, i).getContents();
					if(dutyNames.equals(""))//负责人姓名
						{	
							
							if (temp!=null&&!temp.equals("")) {
								dutyNames="'"+temp+"'";
								dutyCr=i+",3";
							}
							
						}
						else 
						{
							if(temp!=null&&!temp.equals("")&&(dutyNames).indexOf("'"+temp+"'")==-1)
							{
								dutyNames+=",'"+temp+"'";
								dutyCr+=";"+i+",3";
							}
						}
					if(deptNames.equals(""))//部门名称
						{
							deptNames="'"+sheet.getCell(2, i).getContents()+"'";
							deptCr=i+",2";
							}
						else 
						{
							if((deptNames).indexOf("'"+sheet.getCell(2, i).getContents()+"'")==-1)
							{
								deptNames+=",'"+sheet.getCell(2, i).getContents()+"'";
								deptCr+=";"+i+",2";
							}
						}
					if(dangerNames.equals("")) //危险源名称
					{
						dangerNames="'"+sheet.getCell(0, i).getContents()+"'";
					}
					else 
					{
						if((dangerNames).indexOf("'"+sheet.getCell(0, i).getContents()+"'")==-1)
						{
							dangerNames+=",'"+sheet.getCell(0, i).getContents()+"'";
						}
					}
					
			}
				if(msg.equals(""))
				{
				if (dutyNames!=null&&!dutyNames.equals("")) {
					msg=registerRemote.checkInputManName(dutyNames, dutyCr);
				}
				msg+=registerRemote.checkInputDeptName(deptNames, deptCr);
				}
				if(msg.equals(""))
				{
				registerRemote.importDangerDeptValue(dangerNames, ddrList);
				write("{success:true,msg:'导入成功！'}");
				}
				else
				{
					write("{success:true,msg:'数据填写存在问题："+msg+"'}");
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			workbook.close();
			is.close();
		}
	}
	/**
	 * 分发各部门
	 */
	public void sendTodept(){
		String str = request.getParameter("ids");
		if (str!=null&&str!="") {
			registerRemote.changeStatus("1", str, employee.getEnterpriseCode());
			write("{success:true,msg:'操作成功！'}");
		}
	}
	/**
	 * 签字
	 */
	public void deptChargeBy(){
		String str = request.getParameter("ids");
		if (str!=null&&str!="") {
			registerRemote.changeStatus("2", str, employee.getEnterpriseCode());
			write("{success:true,msg:'操作成功！'}");
		}
	}
	/**
	 * 重大危险源落实部门录入页面
	 * 添加，导入按钮是否可用
	 * add by kzhang 20100811
	 */
	public void checkIsEditable(){
		String year=request.getParameter("rewardYear");
		long x=registerRemote.checkIsEditable(year, employee.getEnterpriseCode());
		write(x+"");
	}
	/**
	 * 删除重大危险源落实部门信息
	 * add by kzhang 20100811
	 */
	public void deleteBigDangerDeptRegistion(){
		String str = request.getParameter("ids");
		registerRemote.delete(str);
		write("{success:true,msg:'操作成功！'}");
	}
	public Integer getStart() {
		return start;
	}
	public void setStart(Integer start) {
		this.start = start;
	}
	public Integer getLimit() {
		return limit;
	}
	public void setLimit(Integer limit) {
		this.limit = limit;
	}
	public File getXlsFile() {
		return xlsFile;
	}
	public void setXlsFile(File xlsFile) {
		this.xlsFile = xlsFile;
	}
	
	
	
	
}
