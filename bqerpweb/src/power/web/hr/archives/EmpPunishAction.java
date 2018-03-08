package power.web.hr.archives;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import com.googlecode.jsonplugin.JSONException;
import com.googlecode.jsonplugin.JSONUtil;

import jxl.Cell;
import jxl.Sheet;
import jxl.Workbook;

import power.ear.comm.ejb.PageObject;
import power.ejb.hr.archives.HrCPunish;
import power.ejb.hr.archives.HrCPunishFacadeRemote;
import power.ejb.hr.archives.HrCReward;
import power.ejb.hr.archives.HrCRewardFacadeRemote;
import power.web.comm.AbstractAction;

@SuppressWarnings("serial")
public class  EmpPunishAction  extends AbstractAction{
	private  HrCPunish  punish  ;
	protected  HrCPunishFacadeRemote  remote;
	protected  HrCRewardFacadeRemote remoteA;
	private File xlsFile;
	public File getXlsFile() {
		return xlsFile;
	}
	public void setXlsFile(File xlsFile) {
		this.xlsFile = xlsFile;
	}
	public  EmpPunishAction()
	{
		remote=(HrCPunishFacadeRemote)factory.getFacadeRemote("HrCPunishFacade");
		
	   remoteA=(HrCRewardFacadeRemote)factory.getFacadeRemote("HrCRewardFacade");
		
	}
	public void getPunishList() throws JSONException
	{
		String empId = request.getParameter("empId");
		Object objstart = request.getParameter("start");
		Object objlimit = request.getParameter("limit");
		PageObject obj = new PageObject();
		if (objstart != null && objlimit != null) 
		{
			int start = Integer.parseInt(request.getParameter("start"));
			int limit = Integer.parseInt(request.getParameter("limit"));
			obj = remote.getPunishList(empId,employee.getEnterpriseCode(), start,limit);
		}
		else {
			obj = remote.getPunishList(empId,employee.getEnterpriseCode());
		}
		String str = JSONUtil.serialize(obj);
		write(str);
	}
	public void delPunish()
	{
		String ids=request.getParameter("ids");
		remote.delPunish(ids);
		write("{success:true,msg:'删除成功！'}");
		
	}
	public void addOrUpdatePunish()
	{
		String method=request.getParameter("method");
		String empId=request.getParameter("empId");
		if((method=="add")||(method.equals("add")))
		{
			punish.setEmpId(Long.parseLong(empId));
			punish.setEnterpriseCode(employee.getEnterpriseCode());
//			punish.setIsUse("U");
			punish.setIsUse("Y");//update by sychen 20100830
			remote.save(punish);
			write("{success:true,msg:'增加成功！'}");
			
		}else 
		{
			HrCPunish model = remote.findById(punish.getPunishinfId());
			model.setMemo(punish.getMemo());
			model.setPunishDeadline(punish.getPunishDeadline());
			model.setPunishEndTime(punish.getPunishEndTime());
			model.setPunishExecuteTime(punish.getPunishExecuteTime());
			model.setPunishName(punish.getPunishName());
			model.setPunishReason(punish.getPunishReason());
			model.setPunishTime(punish.getPunishTime());
			model.setPunishUnit(punish.getPunishUnit());
			remote.update(model);
			write("{success:true,msg:'修改成功！'}");
		}
	}
	
	private static final String[] PUNISHINFO_COLUMN_NAMES = {"人员编码","处罚名称","处罚时间","给予处罚单位","处罚原因"
    	,"处罚起始时间","处罚终止时间","处罚执行期限","备注"};
    
public  void  importPunishInf() throws IOException
{
	InputStream is = null;
	Workbook workbook = null;
	
	// 返回到页面的reponseText
	String str ="";
	String type = request.getParameter("type");
	if ("punishInf".equals(type)) {
		str = this.treatRewardXlsFile(is, workbook);
		write(str);
		
	}else{
		write("{success:true,msg:'导入出现异常！'}");
	}
	
}
 private String addColumnIndex(int[] column_indexs,Cell[] cells,String type){
    	if("punishInf".equals(type)){
    		for(int i=0; i < cells.length; i++)
    		{
    			boolean isError = true;
    			for(int j= 0; j < PUNISHINFO_COLUMN_NAMES.length; j++)
    			{
    				if(PUNISHINFO_COLUMN_NAMES[j].equals(cells[i].getContents())){
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
private String treatRewardXlsFile(InputStream is,Workbook workbook) throws IOException{

	String msg = "";
	List<HrCPunish> punishList = new ArrayList<HrCPunish>();
	
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
			columnError = addColumnIndex(column_indexs,sheet.getRow(0),"punishInf");
			// 列名是否存在问题
			if(columnError != null)
				return columnError;
			
			for (int i = 1; i < rows; i++) {
				HrCPunish punish = new HrCPunish();
				Cell[] cells = sheet.getRow(i);// i行的所有单元格
				for(int j = 0; j < column_indexs.length; j++){
					 /*
				        0: "人员编码",1:"处罚名称",2:"处罚时间",3:"给予处罚单位",4:"处罚原因"
    	               ,5:"处罚起始时间",6:"处罚终止时间",7:"处罚执行期限",8:"备注"
				     */
					//  0:人员编码
					if(column_indexs[j] == 0){
						if(cells.length > j && !"".equals(cells[j].getContents())){
							if (cells[j].getContents().length() > 20) {
								i++;
								msg = "第" + i + "行人员编码超过20位！";
								break;
							} else   {
								
									Long empId = remoteA.getEmpIdByNewCode(cells[j].getContents(), employee.getEnterpriseCode());
										if (empId == null) {
											i++;
											msg = "第" + i + "行人员编码:"
													+ cells[j].getContents() + "尚未维护！";
											break;
										} else {
											punish.setEmpId(empId);
										}	
//								}
							
						}
					}else 
					{
						i++;
						msg = "第" + i + "行人员编码必须填写！";
						break;
					}
					}		
					// 1:处罚名称列
					else if(column_indexs[j] == 1){
						if(cells.length > j && !"".equals(cells[j].getContents())){
							String punishName = "";
							String cellString = cells[j].getContents();
							for (int k = 0; k < cellString.length(); k++) {
								if (!"".equals(cellString
												.substring(k, k + 1))) {
									punishName += cellString.substring(k, k + 1);
								}
							}
							punish.setPunishName(punishName);
						}else{
							i++;
							msg = "第" + i + "行处罚名称必须填写！";
							break;
							
						}
					}
					// 2:处罚时间
					else if(column_indexs[j] == 2){
						if (cells.length > j
								&& !"".equals(cells[j].getContents())) {
							String[] punishTime = cells[j].getContents()
									.split("-");
							if (punishTime.length != 3||punishTime[0].length() != 4) {
								i++;
								msg = "第" + i + "行处罚日期格式填写不正确！";
								break;
							}
//							System.out.println("the punish"+cells[j].getContents());
							if (punishTime[1].length() == 1)
								punishTime[1] = "0" + punishTime[1];
							if (punishTime[2].length() == 1)
								punishTime[2] = "0" + punishTime[2];
							try {
								punish.setPunishTime(df.parse(punishTime[0]
										+ "-" + punishTime[1] + "-"
										+ punishTime[2]));
							} catch (Exception e1) {
								i++;
								msg = "第" + i + "行处罚日期格式填写不正确！";
								break;
							}
						}
					}
					// 3:给予处罚单位
					else if(column_indexs[j] == 3){
						if(cells.length > j ){
							if (!"".equals(cells[j].getContents())) {
								punish.setPunishUnit((cells[3].getContents()));
							}else
							{
									i++;
									msg = "第" + i + "行给予处罚单位必须填写！";
									break;
								}
							}
						
					}
					//4:处罚原因
					else if (column_indexs[j] == 4) {
						if (cells.length > j
								&& !"".equals(cells[j].getContents())) {
							punish.setPunishReason((cells[4].getContents()));
						}else{
							
								i++;
								msg = "第" + i + "行处罚原因必须填写！";
								break;
						}
							 
					}
					// 5:处罚起始时间
					else if (column_indexs[j] == 5) {
						if (cells.length > j
								&& !"".equals(cells[j].getContents())) {
							String[] punishExcuteTime = cells[j].getContents()
									.split("-");
							if (punishExcuteTime.length != 3||punishExcuteTime[0].length() != 4) {
								i++;
								msg = "第" + i + "行处罚起始日期格式填写不正确！";
								break;
							}
							if (punishExcuteTime[1].length() == 1)
								punishExcuteTime[1] = "0" + punishExcuteTime[1];
							if (punishExcuteTime[2].length() == 1)
								punishExcuteTime[2] = "0" + punishExcuteTime[2];
							try {
								punish.setPunishExecuteTime(df.parse(punishExcuteTime[0]
										+ "-" + punishExcuteTime[1] + "-"
										+ punishExcuteTime[2]));
							} catch (Exception e1) {
								i++;
								msg = "第" + i + "行处罚起始日期格式填写不正确！";
								break;
							}
						}
						
					}
					// 6:处罚终止时间
					else if (column_indexs[j] == 6) {
						if (cells.length > j
								&& !"".equals(cells[j].getContents())) {
							String[] punishEndTime = cells[j].getContents()
									.split("-");
							if (punishEndTime.length != 3||punishEndTime[0].length() != 4) {
								i++;
								msg = "第" + i + "行处罚终止日期格式填写不正确！";
								break;
							}
							if (punishEndTime[1].length() == 1)
								punishEndTime[1] = "0" + punishEndTime[1];
							if (punishEndTime[2].length() == 1)
								punishEndTime[2] = "0" + punishEndTime[2];
							try {
								punish.setPunishEndTime(df.parse(punishEndTime[0]
										+ "-" + punishEndTime[1] + "-"
										+ punishEndTime[2]));
							} catch (Exception e1) {
								i++;
								msg = "第" + i + "行处罚终止日期格式填写不正确！";
								break;
							}
						}
					}else if (column_indexs[j] == 7) {
						if (cells.length > j
								&& !"".equals(cells[j].getContents())) {
							punish.setPunishDeadline(cells[7].getContents());
						}else{
							
								i++;
								msg = "第" + i + "行处罚执行期限必须填写！";
								break;
						}
							 
					}else if (column_indexs[j] == 8) {
						if (cells.length > j
								&& !"".equals(cells[j].getContents())) {
							punish.setMemo((cells[8].getContents()));
						}else{
//								i++;
//								msg = "第" + i + "行备注必须填写！";
//								break;
						}
							 
					}
				}
			
//				punish.setIsUse("U");
				punish.setIsUse("Y");//update by sychen 20100830
				punish.setEnterpriseCode(employee.getEnterpriseCode());
				punishList.add(punish);
				
			}
		
		}
	} catch (Exception e) {
		e.printStackTrace();
	} finally {
		workbook.close();
		is.close();
	}
	if (msg.equals("")) {
		remote.saveOrUpdatePunish(punishList);
		return "{success:true,msg:'导入成功！'}";
	} else {
		return "{success:true,msg:'数据填写存在问题：" + msg + "'}";
	}

}


	public HrCPunish getPunish() {
		return punish;
	}
	public void setPunish(HrCPunish punish) {
		this.punish = punish;
	}
	

}