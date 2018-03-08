package power.web.hr.archives;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.googlecode.jsonplugin.JSONException;
import com.googlecode.jsonplugin.JSONUtil;

import jxl.Cell;
import jxl.Sheet;
import jxl.Workbook;

import power.ear.comm.ejb.PageObject;
import power.ejb.hr.archives.HrCReward;
import power.ejb.hr.archives.HrCRewardFacadeRemote;
import power.web.comm.AbstractAction;

@SuppressWarnings("serial")
public class  EmpRewardAction  extends AbstractAction{
	private  HrCReward  reward  ;
	protected  HrCRewardFacadeRemote  remote;
	private File xlsFile;
	public File getXlsFile() {
		return xlsFile;
	}
	public void setXlsFile(File xlsFile) {
		this.xlsFile = xlsFile;
	}
	public  EmpRewardAction()
	{
		remote=(HrCRewardFacadeRemote)factory.getFacadeRemote("HrCRewardFacade");
	}
	public void getRewardList() throws JSONException
	{
		String empId = request.getParameter("empId");
		Object objstart = request.getParameter("start");
		Object objlimit = request.getParameter("limit");
		PageObject obj = new PageObject();
		if (objstart != null && objlimit != null) 
		{
			int start = Integer.parseInt(request.getParameter("start"));
			int limit = Integer.parseInt(request.getParameter("limit"));
			obj = remote.getRewardList(empId,employee.getEnterpriseCode(), start,limit);
		}
		else {
			obj = remote.getRewardList(empId,employee.getEnterpriseCode());
		}
		if(obj!=null)
		{
		String str = JSONUtil.serialize(obj);
		write(str);
		}else
		{
			write("{'list':'[]','totalCount':'0'}");
		}
	}
	public void delReward()
	{
		String ids=request.getParameter("ids");
		remote.delReward(ids);
		write("{success:true,msg:'删除成功！'}");
		
	}
	public void addOrUpdateReward()
	{
		String method=request.getParameter("method");
		String empId=request.getParameter("empId");
		
		if((method=="add")||(method.equals("add")))
		{
			reward.setEmpId(Long.parseLong(empId));
			reward.setEnterpriseCode(employee.getEnterpriseCode());
//			reward.setIsUse("U");
			reward.setIsUse("Y");//update by sychen 20100831
			remote.save(reward);
			write("{success:true,msg:'增加成功！'}");
			
		}else 
		{
			HrCReward model = remote.findById(reward.getRewardId());
			model.setRewardLeveal(reward.getRewardLeveal());
			model.setRewardName(reward.getRewardName());
			model.setRewardReason(reward.getRewardReason());
			model.setRewardTime(reward.getRewardTime());
			model.setRewardType(reward.getRewardType());
			model.setRewardUnit(reward.getRewardUnit());
			remote.update(model);
			write("{success:true,msg:'修改成功！'}");
		}
	}
	 private static final String[] REWARDINFO_COLUMN_NAMES = {"人员编码","人员姓名","获得奖励名称","获得时间","获奖类别","获奖级别"
	    	,"获奖颁布单位","获得奖励原因"};
	    
	public  void  importRewardInf() throws IOException
	{
		InputStream is = null;
		Workbook workbook = null;
		
		// 返回到页面的reponseText
		String str ="";
		String type = request.getParameter("type");
		if ("rewardInf".equals(type)) {
			str = this.treatRewardXlsFile(is, workbook);
			write(str);
			
		}else{
			write("{success:true,msg:'导入出现异常！'}");
		}
		
	}
	 private String addColumnIndex(int[] column_indexs,Cell[] cells,String type){
	    	if("rewardInf".equals(type)){
	    		for(int i=0; i < cells.length; i++)
	    		{
	    			boolean isError = true;
	    			for(int j= 0; j < REWARDINFO_COLUMN_NAMES.length; j++)
	    			{
	    				if(REWARDINFO_COLUMN_NAMES[j].equals(cells[i].getContents())){
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
    	List<HrCReward> rewardList = new ArrayList<HrCReward>();
		
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
				columnError = addColumnIndex(column_indexs,sheet.getRow(0),"rewardInf");
				// 列名是否存在问题
				if(columnError != null)
					return columnError;
				
				for (int i = 1; i < rows; i++) {
					HrCReward reward = new HrCReward();
					Cell[] cells = sheet.getRow(i);// i行的所有单元格
					for(int j = 0; j < column_indexs.length; j++){
						 /*
					     * 0: "人员编码",1:"获得奖励名称",2:"获得时间",3:"获奖类别",4:"获奖级别"
	    	               ,5:"获奖颁布单位",6:"获得奖励原因"
					     */
						//  0:人员编码
						if(column_indexs[j] == 0){
							if(cells.length > j && !"".equals(cells[j].getContents())){
								if (cells[j].getContents().length() > 20) {
									i++;
									msg = "第" + i + "行人员编码超过20位！";
									break;
								} else   {
									
										Long empId = remote.getEmpIdByNewCode(cells[j].getContents(), employee.getEnterpriseCode());
											if (empId == null) {
												i++;
												msg = "第" + i + "行人员编码:"
														+ cells[j].getContents() + "尚未维护！";
												break;
											} else {
												reward.setEmpId(empId);
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
						// 1:奖励名称列
						else if(column_indexs[j] == 2){
							if(cells.length > j && !"".equals(cells[j].getContents())){
								String rewardName = "";
								String cellString = cells[j].getContents();
								for (int ii = 0; ii < cellString.length(); ii++) {
									if (!"".equals(cellString
													.substring(ii, ii + 1))) {
										rewardName += cellString.substring(ii, ii + 1);
									}
								}
								reward.setRewardName(rewardName);
							}else{
								i++;
								msg = "第" + i + "行奖励名称必须填写！";
								break;
								
							}
						}
						// 2:获得时间
						else if(column_indexs[j] == 3){
							if (cells.length > j
									&& !"".equals(cells[j].getContents())) {
								String split = new String();
								if(cells[j].getContents().contains("-")){
									split = "-";
								}else if(cells[j].getContents().contains("/"))
								{
									split = "/";
								}
								String[] rewardTime = cells[j].getContents()
										.split(split);
								if (rewardTime.length != 3||rewardTime[0].length() != 4) {
									i++;
									msg = "第" + i + "行出生日期格式填写不正确！";
									break;
								}
								if (rewardTime[1].length() == 1)
									rewardTime[1] = "0" + rewardTime[1];
								if (rewardTime[2].length() == 1)
									rewardTime[2] = "0" + rewardTime[2];
								try {
									reward.setRewardTime(df.parse(rewardTime[0]
											+ "-" + rewardTime[1] + "-"
											+ rewardTime[2]));
								} catch (Exception e1) {
									i++;
									msg = "第" + i + "行获奖日期格式填写不正确！";
									break;
								}
							}
						}
						// 3:获奖类别
						else if(column_indexs[j] == 4){
							if(cells.length > j ){
								if (!"".equals(cells[j].getContents())) {
									reward.setRewardType((cells[3].getContents()));
								}else
								{
										i++;
										msg = "第" + i + "行获奖类别填写不正确！";
										break;
									}
								}
							
						}
						//4:获奖级别
						else if (column_indexs[j] == 5) {
							if (cells.length > j
									&& !"".equals(cells[j].getContents())) {
								reward.setRewardLeveal((cells[4].getContents()));
							}else{
								
									i++;
									msg = "第" + i + "行，获奖级别必须填写！";
									break;
							}
								 
						}
						// 5:获奖颁布单位
						else if (column_indexs[j] == 6) {
							if (cells.length > j
									&& !"".equals(cells[j].getContents())) {
								reward.setRewardUnit(cells[5].getContents());
							}else{

								i++;
								msg = "第" + i + "行，获奖颁布单位必须填写！";
								break;
							} 
							
						}
						// 6:获奖原因
						else if (column_indexs[j] == 7) {
							if (cells.length > j
									&& !"".equals(cells[j].getContents())) {
								reward.setRewardReason(cells[j].getContents());
							}else
							{
								i++;
//								msg = "第" + i + "行，获奖原因必须填写！";
								reward.setRewardReason(cells[j].getContents());
							}
							
						}
						
						}
				
//					reward.setIsUse("U");
					reward.setIsUse("Y");//update by sychen 20100831
					reward.setEnterpriseCode(employee.getEnterpriseCode());
					rewardList.add(reward);
					
				}
			
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			workbook.close();
			is.close();
		}
		
		if (msg.equals("")) {
			remote.saveOrUpdateReward(rewardList);
			return "{success:true,msg:'导入成功！'}";
		} else {
			return "{success:true,msg:'数据填写存在问题：" + msg + "'}";
		}
	
    }

	public HrCReward getReward() {
		return reward;
	}
	public void setReward(HrCReward reward) {
		this.reward = reward;
	}

}