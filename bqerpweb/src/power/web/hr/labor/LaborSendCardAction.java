package power.web.hr.labor;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import jxl.Cell;
import jxl.Sheet;
import jxl.Workbook;

import com.googlecode.jsonplugin.JSONException;
import com.googlecode.jsonplugin.JSONUtil;
import com.opensymphony.workflow.service.WorkflowService;
import com.sun.org.apache.xerces.internal.impl.xpath.regex.ParseException;
import power.ear.comm.ejb.PageObject;
import power.ejb.hr.archives.HrCPunish;
import power.ejb.hr.labor.HrJLaborSendcard;
import power.ejb.hr.labor.HrJLaborSendcardDetail;
import power.ejb.hr.labor.HrJLaborSendcardDetailFacadeRemote;
import power.ejb.hr.labor.HrJLaborSendcardFacadeRemote;
import power.ejb.manage.plan.BpJPlanRepairDep;
import power.web.comm.AbstractAction;
import power.web.comm.PostMessage;

public class LaborSendCardAction extends AbstractAction {
	private HrJLaborSendcardFacadeRemote remoteA;
	private HrJLaborSendcardDetailFacadeRemote remoteB;
	private File xlsFile;
	public File getXlsFile() {
		return xlsFile;
	}
	public void setXlsFile(File xlsFile) {
		this.xlsFile = xlsFile;
	}
	public LaborSendCardAction() {
		remoteA = (HrJLaborSendcardFacadeRemote) factory
				.getFacadeRemote("HrJLaborSendcardFacade");
		remoteB = (HrJLaborSendcardDetailFacadeRemote) factory
				.getFacadeRemote("HrJLaborSendcardDetailFacade");
	}
	private static final String[] SENDCARDNFO_COLUMN_NAMES = {"部门","实有人数","发卡标准","金额","签名","备注"};
	public void copyData()
	{
		String sendYear = request.getParameter("sendYear");
		String sendKind = request.getParameter("sendKind");
		String costItem = request.getParameter("costItem");
		
		HrJLaborSendcard  model=new  HrJLaborSendcard();
			model.setSendKind(sendKind);
			model.setSendYear(sendYear);
			model.setCostItem(costItem);
			model.setEnterpriseCode(employee.getEnterpriseCode());
			model.setEntryDate(new Date());
			model.setEntryBy(employee.getWorkerCode());
			model.setSendState("0");
			model.setIsUse("Y");
		
			// update by sychen 20100712
		Long  mainId  =remoteB.findMaxYear(sendYear,sendKind,employee.getEnterpriseCode());
//		Long  mainId  =remoteB.findMaxYear(sendKind,employee.getEnterpriseCode());//得到最近日期数据的主表id
		if(mainId.equals("0"))
		{
			write("{success:true,msg:'主表中没有数据可以调用'}");
		}else
		{
			Long ID=	remoteA.saveDetail(model,employee.getEnterpriseCode(),employee.getWorkerCode());
			//通过这个主表id找到所有明细记录并插入作为当前所选日期的数据
			  remoteB.findByMainIdandSave(mainId,ID,employee.getWorkerCode(),employee.getEnterpriseCode());
		}
		
		
		write("{success:true,msg:'调用去年数据成功！'}");
	}
	public void  sendCardReport()//上报
	{
		String mainId = request.getParameter("mainId");
			String approveText = request.getParameter("approveText");
			String nextRoles = request.getParameter("nextRoles");
			String flowCode = request.getParameter("flowCode");
			String actionId = request.getParameter("actionId");
			remoteA.sendCardReport(Long.parseLong(mainId),
					Long.parseLong(actionId), employee.getWorkerCode(),
					approveText, nextRoles, flowCode);

			write("{success:true,msg:'上报成功！'}");

		}

	public void sendCardApprove() {
		
		String entryId = request.getParameter("entryId");
		String workerCode = request.getParameter("workerCode");
		String actionId = request.getParameter("actionId");
		String approveText = request.getParameter("approveText");
		String nextRoles = request.getParameter("nextRoles");
		String mainId = request.getParameter("mainId");
		String[] mainIDS=mainId.split(",");
		for(int i=0;i<mainIDS.length;i++)
		{
		if(mainIDS[i]!=null&&!mainIDS[i].equals(""))
		{
			remoteA.sendCardApprove(Long.parseLong(mainIDS[i]), Long
					.parseLong(actionId), Long.parseLong(entryId),
					workerCode, approveText, nextRoles);
		
		}
		}
		write("{success:true,msg:'审批成功！'}");
	}
	public  void  importSendCardInf() throws IOException
	{
		InputStream is = null;
		Workbook workbook = null;
		String sendYear = request.getParameter("sendYear");
		String sendKind = request.getParameter("sendKind");
		String costItem = request.getParameter("costItem");
		HrJLaborSendcard  mainModel=remoteA.findMainInfo(sendYear, sendKind, employee.getEnterpriseCode());
		HrJLaborSendcard model=new HrJLaborSendcard();
		if(mainModel==null)
		{
			model.setCostItem(costItem);
			model.setEnterpriseCode(employee.getEnterpriseCode());
			model.setEntryBy(employee.getWorkerCode());
			model.setEntryDate(new Date());
			model.setSendKind(sendKind);
			model.setIsUse("Y");
			model.setSendState("0");
			model.setSendYear(sendYear);
			
		}else
		{
			HrJLaborSendcard  mod=remoteA.findById(mainModel.getSendcardId());
			mod.setCostItem(costItem);
			mod.setEntryBy(employee.getWorkerCode());
			mod.setEntryDate(new Date());
			model=mod;
			
		}
		
		// 返回到页面的reponseText
		String str ="";
		String type = request.getParameter("type");
		if ("sendCardInf".equals(type)) {
			str = this.treatSendCardXlsFile(is, workbook,model);
			write(str);
			
		}else{
			write("{success:true,msg:'导入出现异常！'}");
		}
		
	}
	 private String addColumnIndex(int[] column_indexs,Cell[] cells,String type){
	    	if("sendCardInf".equals(type)){
	    		for(int i=0; i < cells.length; i++)
	    		{
	    			boolean isError = true;
	    			for(int j= 0; j < SENDCARDNFO_COLUMN_NAMES.length; j++)
	    			{
	    				if(SENDCARDNFO_COLUMN_NAMES[j].equals(cells[i].getContents().trim())){
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
	private String treatSendCardXlsFile(InputStream is,Workbook workbook,HrJLaborSendcard  mainModel) throws IOException{

		String msg = "";
		List<HrJLaborSendcardDetail> detailList = new ArrayList<HrJLaborSendcardDetail>();
		
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
				columnError = addColumnIndex(column_indexs,sheet.getRow(0),"sendCardInf");
				// 列名是否存在问题
				if(columnError != null)
					return columnError;
				
				for (int i = 1; i < rows; i++) {
					HrJLaborSendcardDetail sendCard = new HrJLaborSendcardDetail();
					Cell[] cells = sheet.getRow(i);// i行的所有单元格
					for(int j = 0; j < column_indexs.length; j++){
						 /*
					       0: "部门",1:"实有人数",2:"发卡标准",3:"金额",4:"签名",5:"备注"
					     */
						//  0:部门
						if(column_indexs[j] == 0){
							if(cells.length > j && !"".equals(cells[j].getContents())){
								String deptName = "";
								String cellString = cells[j].getContents();
								for (int k = 0; k < cellString.length(); k++) {
									if (!"".equals(cellString
													.substring(k, k + 1))) {
										deptName += cellString.substring(k, k + 1);
									}
								}
								sendCard.setDeptName(deptName);
							}else{
								i++;
								msg = "第" + i + "行部门名称必须填写！";
								break;
								
							}
						}		
						// 1:实有人数
						else if(column_indexs[j] == 1){
							if(cells.length > j && !"".equals(cells[j].getContents())){
								
								sendCard.setFactNum(Long.parseLong(cells[1].getContents()));
							}else{
								i++;
								msg = "第" + i + "行实有人数必须填写！";
								break;
								
							}
						}
						// 2:发卡标准
						else if(column_indexs[j] == 2){
							if (cells.length > j
									&& !"".equals(cells[j].getContents())) {
								sendCard.setSendStandard(Double.parseDouble((cells[2].getContents())));
							}else
							{
								i++;
								msg = "第" + i + "行发卡标准必须填写！";
								break;
								
							}
						}
						// 3:金额
						else if(column_indexs[j] == 3){
							if(cells.length > j ){
								if (!"".equals(cells[j].getContents())) {
//									sendCard.s((cells[3].getContents()));
								}else
								{
										/*i++;
										msg = "第" + i + "行金额必须填写！";
										break;*/
									}
								}
							
						}
						//4:签名
						else if (column_indexs[j] == 4) {
							if (cells.length > j
									&& !"".equals(cells[j].getContents())) {
								sendCard.setSignName((cells[4].getContents()));
							}else{
								
									i++;
									msg = "第" + i + "行签名必须填写！";
									break;
							}
								 
						}
						// 5:备注
						else if (column_indexs[j] == 5) {
							if (cells.length > j
									&& !"".equals(cells[j].getContents())) {
								sendCard.setMemo(cells[5].getContents());
							}else
							{
								i++;
								msg = "第" + i + "行备注必须填写！";
								break;
							}
							
						}
						
					}
					sendCard.setModifyBy(employee.getWorkerCode());
					sendCard.setModifyDate(new Date());
					sendCard.setIsUse("Y");
					sendCard.setEnterpriseCode(employee.getEnterpriseCode());
					detailList.add(sendCard);
					
					
				
			}
			
			}
		
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			workbook.close();
			is.close();
		}
		if (msg.equals("")) {
			remoteB.insertSendcardDetail(mainModel, detailList);
			return "{success:true,msg:'导入成功！'}";
		} else {
			return "{success:true,msg:'数据填写存在问题：" + msg + "'}";
		}

	}
  public   void  	getSendCardApproveInfo() throws JSONException
  {
	     String entryIds="";
	     String sendYear = request.getParameter("sendYear");
		 String sendKind = request.getParameter("sendKind");
		 WorkflowService workflowService = new com.opensymphony.workflow.service.WorkflowServiceImpl();

			entryIds = workflowService.getAvailableWorkflow(
					new String[] { "bqSendCard" }, employee.getWorkerCode());
			List  list=remoteA.getApprovelist(sendYear,sendKind,entryIds,employee.getEnterpriseCode());
			System.out.println("the list"+JSONUtil.serialize(list));
			write(JSONUtil.serialize(list));
  }
	 public void getTbarStatus()
	  {
		 String sendYear = request.getParameter("sendYear");
		 String sendKind = request.getParameter("sendKind");
		 String str = "";
		str = remoteA.getSendCardStatus(sendYear,sendKind, employee.getWorkerCode(),employee.getEnterpriseCode());

		write(str);
	}
    public void getSendCardList() throws JSONException
    {
    	PageObject obj=new PageObject();
    	String start="";
    	String limit="";
    	String flag = request.getParameter("flag");
    	String sendYear = request.getParameter("sendYear");
		String sendKind = request.getParameter("sendKind");
    	Object  sta=request.getParameter("start");
    	Object  lim=request.getParameter("limit");
    	if(sta!=null&&lim!=null)
    	{
    		 start=sta.toString();
    		 limit=lim.toString();
    		 obj=remoteB.findSendcardDetailList(flag,sendYear, sendKind, Integer.parseInt(start),Integer.parseInt(limit));
    	}else
    	{
    		obj=remoteB.findSendcardDetailList(flag,sendYear, sendKind);
    	}
    	if (obj != null) {
			write(JSONUtil.serialize(obj));
		} else {
			write("{totalCount : 0,list :[]}");
		  }
    	
    }
    public void delSendCard()
    {
    	String ids=request.getParameter("ids");
    	remoteB.delete(ids);
    	write("{success:true,msg:'删除成功！'}");
    }
	public void saveSendCard() throws JSONException, ParseException,
			java.text.ParseException {
		String flag=request.getParameter("flag");
		String sendYear = request.getParameter("sendYear");
		String sendKind = request.getParameter("sendKind");
		String costItem=request.getParameter("costItem");
		String str = request.getParameter("isUpdate");
		Object object = JSONUtil.deserialize(str);
		List<Map> list = (List<Map>) object;
		List<HrJLaborSendcardDetail> addList = null;
		List<HrJLaborSendcardDetail> updateList = null;
		
		
		//
		if (list != null && list.size() > 0) {
			// 明细信息list
			addList = new ArrayList<HrJLaborSendcardDetail>();
			updateList = new ArrayList<HrJLaborSendcardDetail>();
			
			
			
			for (Map data : list) {
				
				String detailId = null;
				String sendcardId = null;
				String deptName = null;
				String factNum = null;
				String sendStandard = null;
				String signName = null;
				String memo = null;
			/*	String sendkind=null;
				String sendyear=null;
				*/

				if (data.get("detailId") != null&&!data.get("detailId").equals(""))
					detailId = data.get("detailId").toString();
				if (data.get("sendcardId") != null&&!data.get("sendcardId").equals(""))
					sendcardId = data.get("sendcardId").toString();

				if (data.get("deptName") != null&&!data.get("deptName").equals(""))
					deptName = data.get("deptName").toString();
				if (data.get("factNum") != null&&!data.get("factNum").equals(""))
					factNum = data.get("factNum").toString();
				if (data.get("sendStandard") != null&&!data.get("sendStandard").equals(""))
					sendStandard = data.get("sendStandard").toString();
				if (data.get("signName") != null&&!data.get("signName").equals(""))
					signName = data.get("signName").toString();
				if (data.get("memo") != null&&!data.get("memo").equals(""))
					memo = data.get("memo").toString();
				
				
				/*if (data.get("sendYear") != null&&!data.get("sendYear").equals(""))
					sendyear = data.get("sendYear").toString();
				if (data.get("sendKind") != null&&!data.get("sendKind").equals(""))
					sendkind = data.get("sendKind").toString();
				*/
				
				
				
				
				//-------------------------
				
			

				HrJLaborSendcardDetail model = new HrJLaborSendcardDetail();
				if (detailId == null) {
					if(deptName!=null)
					{
					model.setDeptName(deptName);
					}
					if (factNum != null )
						model.setFactNum(Long.parseLong(factNum));
					if (sendStandard != null)
						model.setSendStandard(Double.parseDouble(sendStandard));

					if (memo != null)
						model.setMemo(memo);
					if(signName!=null)
					{
						model.setSignName(signName);
					}
					model.setIsUse("Y");
					model.setModifyBy(employee.getWorkerCode());
					model.setModifyDate(new Date());
					model.setEnterpriseCode(employee.getEnterpriseCode());
					addList.add(model);
				} else {
					model = remoteB.findById(Long.parseLong(detailId));
					if(deptName!=null)
					{
					model.setDeptName(deptName);
					}
					if (factNum != null )
						model.setFactNum(Long.parseLong(factNum));
					if (sendStandard != null)
						model.setSendStandard(Double.parseDouble(sendStandard));

					if (memo != null)
						model.setMemo(memo);
					if(signName!=null)
					{
						model.setSignName(signName);
					}
					model.setIsUse("Y");
					model.setModifyBy(employee.getWorkerCode());
					model.setModifyDate(new Date());
					model.setEnterpriseCode(employee.getEnterpriseCode());
					updateList.add(model);
				}
			}
		}
			remoteB.saveMainAndDetail( flag,sendYear,sendKind,costItem,employee.getEnterpriseCode(),employee.getWorkerCode(),addList,updateList);
			write("{success:true,msg:'操作成功！'}");

		}
	

}