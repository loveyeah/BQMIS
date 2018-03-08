package power.web.hr.salary;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import jxl.Cell;
import jxl.Sheet;
import jxl.Workbook;

import com.googlecode.jsonplugin.JSONException;
import com.googlecode.jsonplugin.JSONUtil;

import power.ear.comm.ejb.PageObject;
import power.ejb.hr.archives.HrCRewardFacadeRemote;
import power.ejb.hr.labor.HrJLaborSendcard;
import power.ejb.hr.labor.HrJLaborSendcardDetail;
import power.ejb.hr.reward.HrCStationQuantify;
import power.ejb.hr.reward.HrCStationQuantifyFacadeRemote;
import power.ejb.hr.salary.HrCSalaryPersonal;
import power.ejb.hr.salary.HrCSalaryPersonalFacadeRemote;
import power.ejb.hr.salary.form.SalaryPersonalForm;
import power.ejb.manage.plan.trainplan.BpJTrainingSumApproval;
import power.web.comm.AbstractAction;

@SuppressWarnings("serial")
public class SalaryPersonalAction extends AbstractAction{

	private HrCSalaryPersonalFacadeRemote remote;
	protected  HrCRewardFacadeRemote remoteA;
	protected    HrCStationQuantifyFacadeRemote  remoteB;
	private HrCSalaryPersonal personal;
	private File xlsFile;
	public File getXlsFile() {
		return xlsFile;
	}
	public void setXlsFile(File xlsFile) {
		this.xlsFile = xlsFile;
	}
	public SalaryPersonalAction()
	{
		remote = (HrCSalaryPersonalFacadeRemote)factory.getFacadeRemote("HrCSalaryPersonalFacade");
	 remoteA=(HrCRewardFacadeRemote)factory.getFacadeRemote("HrCRewardFacade");
	 remoteB=(HrCStationQuantifyFacadeRemote)factory.getFacadeRemote("HrCStationQuantifyFacade");
	 
	}
	//add by wpzhu 
	public   void getPorprotion() throws JSONException
	{
		String stationId=request.getParameter("stationId");
		HrCStationQuantify  mod=	remoteB.findById(Long.parseLong(stationId));
//		System.out.println("the mod"+JSONUtil.serialize(mod));
		write(JSONUtil.serialize(mod));
	}
	//add by wpzhu   20100708  增加薪酬个人维护的导入
	private static final String[] SARALYPERSONAL_COLUMN_NAMES = {"员工编号","员工名称","所属部门","参加工作时间","入厂时间","初始运龄",
		"保留工资(元)","薪点工资调整","月奖系数","岗位","岗位量化比例","薪点","最近加入运行时间","运龄是否使用","备注"};
	//  薪酬个人维护导入 add by wpzhu
	public  void  importSaralyPersonalInf() throws IOException
	{
		InputStream is = null;
		Workbook workbook = null;
		
		
		// 返回到页面的reponseText
		String str ="";
		String type = request.getParameter("type");
		if ("SaralyPersoalInf".equals(type)) {
			str = this.treatSaralyPersonalXlsFile(is, workbook);
			write(str);
			
		}else{
			write("{success:true,msg:'导入出现异常！'}");
		}
		
	}
	 private String addColumnIndex(int[] column_indexs,Cell[] cells,String type){
	    	if("SaralyPersoalInf".equals(type)){
	    		for(int i=0; i < cells.length; i++)
	    		{
	    			boolean isError = true;
	    			for(int j= 0; j < SARALYPERSONAL_COLUMN_NAMES.length; j++)
	    			{
	    				if(SARALYPERSONAL_COLUMN_NAMES[j].equals(cells[i].getContents())){
	    					column_indexs[i] = j;
	    					isError = false;
	    				}
	    			}
	    			if(isError){
	    				return "{success:true,msg:'"+cells[i].getContents()+"列不是要导入的具体列！'}";
	    			}
	    		}
	    	}
	    	
	    	return null;
	    }
	/**
	 * @param is
	 * @param workbook
	 * @return
	 * @throws IOException
	 */
	private String treatSaralyPersonalXlsFile(InputStream is,Workbook workbook  ) throws IOException{

		String msg = "";
		List<HrCSalaryPersonal> saralyList = new ArrayList<HrCSalaryPersonal>();
		
		try {
			is = new FileInputStream(xlsFile);
			workbook = Workbook.getWorkbook(is);// 得到工作薄
			Sheet sheet = workbook.getSheet(0);// 得到工作薄中的第一个工作表
			int rows = sheet.getRows();// 得到excel的总行数
			if (rows == 0) {
				msg =  "{success:true,msg:'无数据进行导入!'}";
			} else if(rows == 1){
				msg =  "{success:true,msg:'文件除一列头行外，至少还需一行数据!'}";
			}else {
				int[] column_indexs = new int[sheet.getRow(0).length];
				SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
				String columnError = null;
				columnError = addColumnIndex(column_indexs,sheet.getRow(0),"SaralyPersoalInf");
				// 列名是否存在问题
				if(columnError != null)
					return columnError;
				
				for (int i = 1; i < rows; i++) {
					HrCSalaryPersonal saralyPersonal = new HrCSalaryPersonal();
					Cell[] cells = sheet.getRow(i);// i行的所有单元格
					for(int j = 0; j < column_indexs.length; j++){
						 /*
					      0: "员工编号",1:"员工名称",2:"所属部门",3:"参加工作时间",4:"入厂时间",5:"初始
					      运龄",
		                6: "保留工资(元)",7:"薪点工资调整",8:"月奖系数",9:"岗位",10:"岗位量化比例",
		                11:"薪点",12:'最近加入运行时间',13：'运龄是否使用'14:"备注"
					     */
						//  0:员工编号
						if(column_indexs[j] == 0){
							if(cells.length > j && !"".equals(cells[j].getContents())){
								
								if (cells[j].getContents().length() > 20) {
									i++;
									msg = "第" + i + "行员工编号超过20位！";
									break;
								} else   {
									
										Long empId = remoteA.getEmpIdByNewCode(cells[j].getContents(), employee.getEnterpriseCode());
											if (empId == null) {
												i++;
												msg = "第" + i + "行员工编号:"
														+ cells[j].getContents() + "尚未维护！";
												break;
											} else {
												saralyList = remote.findPersonalById(empId);
												saralyPersonal = saralyList.get(0);
												saralyPersonal.setEmpId(empId);
											}	
//									}
								
							}
						}else 
						{
							i++;
							msg = "第" + i + "行人员编码必须填写！";
							break;
						}
						}		
						// 1:员工名称
						else if(column_indexs[j] == 1){
							if(cells.length > j && !"".equals(cells[j].getContents())){
								String empName = "";
								String cellString = cells[j].getContents();
								for (int k = 0; k < cellString.length(); k++) {
									if (!"".equals(cellString
													.substring(k, k + 1))) {
										empName += cellString.substring(k, k + 1);
									}
								}
								
							}else{
								/*i++;
								msg = "第" + i + "行员工名称必须填写！";
								break;*/
								
							}
						}
						// 2:所属部门
						else if(column_indexs[j] == 2){
							if (cells.length > j
									&& !"".equals(cells[j].getContents())) {
								Long deptId=remote.getIdBydeptName(cells[j].getContents(),employee.getEnterpriseCode());
								if (deptId == null) {
									i++;
									msg = "第" + i + "行部门:"
											+ cells[j].getContents() + "尚未维护！";
									break;
								} else {
									saralyPersonal.setDeptId(deptId);
								}	
							}else
							{
								i++;
								msg = "第" + i + "行部门名称必须填写！";
								break;
								
							}
						}
						// 3 参加工作时间
						else if(column_indexs[j] == 3){
							if(cells.length > j ){
								if (!"".equals(cells[j].getContents())) {
									String[] workTime = cells[j].getContents()
									.split("-");
							if (workTime.length != 3||workTime[0].length() != 4) {
								i++;
								msg = "第" + i + "行处参加工作日期格式填写不正确！";
								break;
							}
							if (workTime[1].length() == 1)
								workTime[1] = "0" + workTime[1];
							if (workTime[2].length() == 1)
								workTime[2] = "0" + workTime[2];
							try {
								saralyPersonal.setWorkingFrom(df.parse(workTime[0]
										+ "-" + workTime[1] + "-"
										+ workTime[2]));
							} catch (Exception e1) {
								i++;
								msg = "第" + i + "行参加工作日期格式填写不正确！";
								break;
							}
								}
						}
						}
						//4:入厂时间
						else if (column_indexs[j] == 4) {
							if (cells.length > j
									&& !"".equals(cells[j].getContents())) {
								String[] lastRunTime = cells[j].getContents()
								.split("-");
						if (lastRunTime.length != 3||lastRunTime[0].length() != 4) {
							i++;
							msg = "第" + i + "行处入厂日期格式填写不正确！";
							break;
						}
						if (lastRunTime[1].length() == 1)
							lastRunTime[1] = "0" + lastRunTime[1];
						if (lastRunTime[2].length() == 1)
							lastRunTime[2] = "0" + lastRunTime[2];
						try {
							saralyPersonal.setJoinFrom(df.parse(lastRunTime[0]
									+ "-" + lastRunTime[1] + "-"
									+ lastRunTime[2]));
						} catch (Exception e1) {
							i++;
							msg = "第" + i + "行参加入厂日期格式填写不正确！";
							break;
						}
								 
						}
						}
						// 5:初始运龄
						else if (column_indexs[j] == 5) {
							if (cells.length > j
									&& !"".equals(cells[j].getContents())) {
								saralyPersonal.setRunningAge(Long.parseLong(cells[j].getContents()));//update by sychen 20100723
//								saralyPersonal.setRunningAge(Long.parseLong(cells[5].getContents()));
							}else
							{
								i++;
								msg = "第" + i + "行初始运龄必须填写！";
								break;
							}
							
						}
							// 6 保留工资(元)
						else if (column_indexs[j] == 6) {
							if (cells.length > j
									&& !"".equals(cells[j].getContents())) {
								saralyPersonal.setRemainSalary(Double.parseDouble(cells[j].getContents()));//update by sychen 20100723
//								saralyPersonal.setRemainSalary(Double.parseDouble(cells[6].getContents()));
							}else
							{
								i++;
								msg = "第" + i + "行保留工资必须填写！";
								break;
							}
							
						}
							// 7: 薪点工资调整
						else if (column_indexs[j] == 7) {
							if (cells.length > j
									&& !"".equals(cells[j].getContents())) {
								saralyPersonal.setPointSalaryAdjust(Double.parseDouble(cells[j].getContents()));//update by sychen 20100723
//								saralyPersonal.setPointSalaryAdjust(Double.parseDouble(cells[7].getContents()));
							}else
							{//modify by wpzhu 
								/*i++;
								msg = "第" + i + "行薪点工资调整必须填写！";
								break;*/
							}
							
						}

							// 8:月奖系数
						else if (column_indexs[j] == 8) {
							if (cells.length > j
									&& !"".equals(cells[j].getContents())) {
								saralyPersonal.setMonthAward(Double.parseDouble(cells[j].getContents()));//update by sychen 20100723
//								saralyPersonal.setMonthAward(Double.parseDouble(cells[8].getContents()));
							}else
							{
								i++;
								msg = "第" + i + "行月奖系数必须填写！";
								break;
							}
							
						}	

							// 9:岗位
						else if (column_indexs[j] == 9) {
							if (cells.length > j
									&& !"".equals(cells[j].getContents())) {
								Long stationId=remote.getIdByStationName(cells[j].getContents(),employee.getEnterpriseCode()); 
								if (stationId == null) {
									i++;
									msg = "第" + i + "行岗位:"
											+ cells[j].getContents() + "尚未维护！";
									break;
								} else {
									saralyPersonal.setQuentityId(stationId);
								}	
							}
								
							else
							{
								i++;
								msg = "第" + i + "行岗位名称必须填写！";
								break;
							}
							
						}	
							// 10:岗位量化比例
						else if (column_indexs[j] == 10) {
							if (cells.length > j
									&& !"".equals(cells[j].getContents())) {
								
							
							}else
							{
								/*i++;
								msg = "第" + i + "行更为名称必须填写！";
								break;*/
							}
							
						}
							//11: 薪点
						else if (column_indexs[j] == 11) {
							if (cells.length > j
									&& !"".equals(cells[j].getContents())) {
								
								saralyPersonal.setSalaryPoint(Double.parseDouble(cells[j].getContents()));
								
								
							}else
							{
								i++;
								msg = "第" + i + "行薪点必须填写！";
								break;
							}
							
						}
							//12:最近加入运行时间
						else if (column_indexs[j] == 12) {
							if (cells.length > j
									&& !"".equals(cells[j].getContents())) {
								
								String[] lastRunTime = cells[j].getContents()
								.split("-");
						if (lastRunTime.length != 3||lastRunTime[0].length() != 4) {
							i++;
							msg = "第" + i + "行处最近加入运行时间格式填写不正确！";
							break;
						}
						if (lastRunTime[1].length() == 1)
							lastRunTime[1] = "0" + lastRunTime[1];
						if (lastRunTime[2].length() == 1)
							lastRunTime[2] = "0" + lastRunTime[2];
						try {
							saralyPersonal.setLastJoinRuntime(df.parse(lastRunTime[0]
									+ "-" + lastRunTime[1] + "-"
									+ lastRunTime[2]));
						} catch (Exception e1) {
							i++;
							msg = "第" + i + "行最近加入运行时间格式填写不正确！";
							break;
						}
							}
						}
							// 13运龄是否使用
						else if (column_indexs[j] == 13) {
							if (cells.length > j
									&& !"".equals(cells[j].getContents())) {
								if(cells[j].getContents().trim().equals("是"))
										{
								saralyPersonal.setRunAgeFlag("1");
										}else if(cells[j].getContents().toString().equals("否"))
										{
											saralyPersonal.setRunAgeFlag("2");
										}
								
								
							}else
							{
								i++;
								msg = "第" + i + "行运龄是否使用必须填写！";
								break;
							}
							
						}
							// 14. 备注
						else if (column_indexs[j] == 14) {
							if (cells.length > j
									&& !"".equals(cells[j].getContents())) {
								
								saralyPersonal.setMemo((cells[j].getContents()));
								
								
							}else
							{//modify by  wpzhu 
								/*i++;
								msg = "第" + i + "行备注必须填写！";
								break;*/
							}
							
						}
							
							
							
							
							
							
					}
								
								
								
					
					
							
						
					
				    saralyPersonal.setIsUse("Y");
					saralyPersonal.setEnterpriseCode(employee.getEnterpriseCode());
					saralyList.add(saralyPersonal);
					
					
							}
						
			}
			
				
			
			
		
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			workbook.close();
			is.close();
		}
		if (msg.equals("")) {
			remote.insertSaralyPersonal(saralyList);
			return "{success:true,msg:'导入成功！'}";
		} else {
			return "{success:true,msg:'数据填写存在问题：" + msg + "'}";
		}

	}

	
	
	
	
	
	
	/**
	 * 员工调整
	 */
	public void empChangeRecord()
	{
		remote.empChangeRecord(employee.getEnterpriseCode());
		write("{success:true,msg:'操作成功！'}");
	}
	
	/**
	 * 工龄调整
	 */
	public void runAgeChangeRecord()
	{
		String ids  = request.getParameter("ids");
		remote.runAgeChangeRecord(ids);
		write("{success:true,msg:'操作成功！'}");
	}
	
	/**
	 * 修改薪酬记录
	 * @throws ParseException 
	 */
	public void updateSalaryPersonalRecord() throws ParseException
	{
		String  lastJoinRuntime=request.getParameter("lastJoinRuntime") ;
		HrCSalaryPersonal model = remote.findById(personal.getSalaryPersonalId());
		model.setJoinFrom(personal.getJoinFrom());
		model.setMemo(personal.getMemo());
		model.setMonthAward(personal.getMonthAward());
		model.setPointSalaryAdjust(personal.getPointSalaryAdjust());
		model.setRemainSalary(personal.getRemainSalary());
		model.setRunningAge(personal.getRunningAge());
		model.setWorkingFrom(personal.getWorkingFrom());
		model.setSalaryPoint(personal.getSalaryPoint());//add by  wpzhu 
		model.setQuentityId(personal.getQuentityId());
		model.setRunAgeFlag(personal.getRunAgeFlag());
		SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
		if(lastJoinRuntime!=null&&!lastJoinRuntime.equals(""))
		{
		model.setLastJoinRuntime(df.parse(lastJoinRuntime));
		}
		
		remote.update(model);
		write("{success:true,msg:'修改成功！'}");
	}
	
	/**
	 * 查找符合条件的薪酬记录
	 * @throws JSONException
	 */
	public void findSalaryPersonalList() throws JSONException
	{
		String fuzzy = request.getParameter("fuzzy");
		//add by ypan 20100728
		String deptId = request.getParameter("deptId");
		Object objstart = request.getParameter("start");
		Object objlimit = request.getParameter("limit");
		PageObject object = new PageObject();
		if (objstart != null && objlimit != null) {
			int start = Integer.parseInt(request.getParameter("start"));
			int limit = Integer.parseInt(request.getParameter("limit"));
			//modify by ypan 20100728
			object = remote.findSalaryPersonalList(deptId,fuzzy, employee.getEnterpriseCode(), start,limit);
		} else {
			object = remote.findSalaryPersonalList(deptId,fuzzy, employee.getEnterpriseCode());
		}
		String strOutput = "";
		if (object == null || object.getList() == null) {
			strOutput = "{\"list\":[],\"totalCount\":0}";
		} else {
			strOutput = JSONUtil.serialize(object);
		}
		write(strOutput);
	}
	
	/**
	 * 取是否运行岗位，如果是，运龄可填，否则运龄不可输入
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	public void getRunStationList() throws Exception {

		String empCode = request.getParameter("empCode");
		SalaryPersonalForm obj = remote.getRunStationList(empCode);
		List lists = null;
		String returnStr = "{model:" + JSONUtil.serialize(obj) + ",list:"
				+ JSONUtil.serialize(lists) + "}";
		write(returnStr);
	}

	public HrCSalaryPersonal getPersonal() {
		return personal;
	}

	public void setPersonal(HrCSalaryPersonal personal) {
		this.personal = personal;
	}
}
