package power.web.hr.labor;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import jxl.Cell;
import jxl.Sheet;
import jxl.Workbook;

import power.ear.comm.DataChangeException;
import power.ear.comm.ejb.PageObject;
import power.ejb.hr.labor.HrJLaborTempe;
import power.ejb.hr.labor.HrJLaborTempeDetail;
import power.ejb.hr.labor.HrJLaborTempeManage;
import power.web.comm.AbstractAction;

import com.googlecode.jsonplugin.JSONException;
import com.googlecode.jsonplugin.JSONUtil;
import com.opensymphony.workflow.service.WorkflowService;

@SuppressWarnings("serial")
public class HrJLaborTempeAction extends AbstractAction {
	protected HrJLaborTempeManage tRemote;

	private String workflowType;
	private String actionId;
	private String approveText;
	private String nextRoles;
	private String eventIdentify;
	private String stepId;
	
	private File xlsFile;
	
	private static final String[] LABORTEMPE_COLUMN_NAMES = { "部门","实有人数", "高温标准人数", 
		"高温标准",  "中温标准人数" ,"中温标准", "低温标准人数", "低温标准", "备注"};
	
	/**
	 * 
	 * 构造方法
	 * 
	 */
	@SuppressWarnings("unchecked")
	public HrJLaborTempeAction() {
		tRemote = (HrJLaborTempeManage) factory
				.getFacadeRemote("HrJLaborTempeManageImpl");
	}
	//add by wpzhu 20100716  查询最近日期的待审批信息
	public void getTempeApproveInfo() {
		String str = tRemote.getTempeApproveInfo(employee.getEnterpriseCode());
		write(str);
	}
	public HrJLaborTempe saveLaborTempeMain() throws ParseException {

		String tempeMonth = request.getParameter("tempeMonth");
		String workFlowType = request.getParameter("workFlowType");
		String flag = request.getParameter("flag");
		String costItem = request.getParameter("costItem");
		HrJLaborTempe model = new HrJLaborTempe();
		
		model.setTempeMonth(tempeMonth);
		model.setCostItem(costItem);
		model.setEntryBy(employee.getWorkerCode());
		model.setEntryDate(new Date());
		if(flag!=null&&flag.equals("approveAdd")){

			model.setTempeState("1");
		}
		else {

			model.setTempeState("0");
		}
		model.setIsUse("Y");
		model.setEnterpriseCode(employee.getEnterpriseCode());

		HrJLaborTempe baseInfo = tRemote.save(model,flag,workFlowType,employee.getWorkerCode());
		return baseInfo;

	}
	
	public void updateLaborTempeMain(String mainId) throws ParseException {

		HrJLaborTempe model = tRemote.findByMainId(Long.parseLong(mainId));

		model.setEntryBy(employee.getWorkerCode());
		model.setEntryDate(new Date());
		tRemote.update(model);
	}
	
	@SuppressWarnings("unchecked")
	public void saveLaborTempe() throws Exception {
		try {
			String tempeMonth = request.getParameter("tempeMonth");
			String str = request.getParameter("isUpdate");
			String deleteIds = request.getParameter("isDelete");
			
			HrJLaborTempe baseInfo = new HrJLaborTempe();
			String mainId = tRemote.getHrJLaborTempeMainId(tempeMonth,employee.getWorkerCode(),employee.getEnterpriseCode());
			if (mainId.equals("")||mainId==null) {
				
				baseInfo = saveLaborTempeMain();
			}
			else{
				updateLaborTempeMain(mainId);
			}
			
			Object obj = JSONUtil.deserialize(str);

			List<HrJLaborTempeDetail> addList = new ArrayList<HrJLaborTempeDetail>();
			List<HrJLaborTempeDetail> updateList = new ArrayList<HrJLaborTempeDetail>();

			List<Map> list = (List<Map>) obj;
			String tempeId = null;
			for (Map data : list) {

				String tempeDetailId = null;
				String deptName = null;
				String factNum = null;
				String highTempeNum = null;
				String highTempeStandard = null;
				String midTempeNum=null;
				String midTempeStandard=null;
				String lowTempeNum = null;
				String lowTempeStandard=null;
				String memo=null;

				tempeId = baseInfo.getTempeId() == null ? mainId : baseInfo.getTempeId()
				.toString();

				if (data.get("tempeDetailId") != null) {
					tempeDetailId = data.get("tempeDetailId").toString();
				}
				if (data.get("deptName") != null) {
					deptName = data.get("deptName").toString();
				}
				if (data.get("factNum") != null) {
					factNum = data.get("factNum").toString();
				}
				if (data.get("highTempeNum") != null) {
					highTempeNum = data.get("highTempeNum").toString();
				}
				if (data.get("highTempeStandard") != null) {
					highTempeStandard = data.get("highTempeStandard").toString();
				}

				if (data.get("midTempeNum") != null) {
					midTempeNum = data.get("midTempeNum").toString();
				}
				if (data.get("midTempeStandard") != null) {
					midTempeStandard = data.get("midTempeStandard").toString();
				}
				if (data.get("lowTempeNum") != null) {
					lowTempeNum = data.get("lowTempeNum").toString();
				}

				if (data.get("lowTempeStandard") != null) {
					lowTempeStandard = data.get("lowTempeStandard").toString();
				}
				if (data.get("memo") != null) {
					memo = data.get("memo").toString();
				}

				HrJLaborTempeDetail model = new HrJLaborTempeDetail();

				 
				if (tempeDetailId == null) {
					model.setTempeId(Long.parseLong(tempeId));
					model.setDeptName(deptName);
					if(factNum!=null&&!factNum.equals("")){
						model.setFactNum(Long.parseLong(factNum));
					}
					if(highTempeNum!=null&&!highTempeNum.equals("")){
						model.setHighTempeNum(Long.parseLong(highTempeNum));
					}
					if(highTempeStandard!=null&&!highTempeStandard.equals("")){
					    model.setHighTempeStandard(Double.parseDouble(highTempeStandard));
					}
					if(midTempeNum!=null&&!midTempeNum.equals("")){
					    model.setMidTempeNum(Long.parseLong(midTempeNum));
					}
					if(midTempeStandard!=null&&!midTempeStandard.equals("")){
					    model.setMidTempeStandard(Double.parseDouble(midTempeStandard));
					}
					if(lowTempeNum!=null&&!lowTempeNum.equals("")){
					    model.setLowTempeNum(Long.parseLong(lowTempeNum));
					}

					if(lowTempeStandard!=null&&!lowTempeStandard.equals("")){
					    model.setLowTempeStandard(Double.parseDouble(lowTempeStandard));
					}
					model.setMemo(memo);
					model.setModifyBy(employee.getWorkerCode());
					model.setModifyDate(new Date());
					model.setEnterpriseCode(employee.getEnterpriseCode());
					model.setIsUse("Y");
					addList.add(model);

				} else {
					model = tRemote.findByDetailId(Long.parseLong(tempeDetailId));
					model.setDeptName(deptName);
					if(factNum!=null&&!factNum.equals("")){
						model.setFactNum(Long.parseLong(factNum));
					}
					if(highTempeNum!=null&&!highTempeNum.equals("")){
						model.setHighTempeNum(Long.parseLong(highTempeNum));
					}
					if(highTempeStandard!=null&&!highTempeStandard.equals("")){
					    model.setHighTempeStandard(Double.parseDouble(highTempeStandard));
					}
					if(midTempeNum!=null&&!midTempeNum.equals("")){
					    model.setMidTempeNum(Long.parseLong(midTempeNum));
					}
					if(midTempeStandard!=null&&!midTempeStandard.equals("")){
					    model.setMidTempeStandard(Double.parseDouble(midTempeStandard));
					}
					if(lowTempeNum!=null&&!lowTempeNum.equals("")){
					    model.setLowTempeNum(Long.parseLong(lowTempeNum));
					}

					if(lowTempeStandard!=null&&!lowTempeStandard.equals("")){
					    model.setLowTempeStandard(Double.parseDouble(lowTempeStandard));
					}
					model.setMemo(memo);
					model.setModifyBy(employee.getWorkerCode());
					model.setModifyDate(new Date());

					updateList.add(model);
				}
			}

			if (addList.size() > 0)
				tRemote.saveDetail(addList);

			if (updateList.size() > 0)

				tRemote.updateDetail(updateList);

			if (deleteIds != null && !deleteIds.trim().equals(""))
				tRemote.deleteDetail(deleteIds);
			
			 write("{success:true,msg:'保存成功！',obj:" + tempeId + "}");

		} catch (Exception exc) {
			write("{success:false,msg:'操作失败'}");
			throw exc;
		}
	}
	
	public void reportHrJLaborTempe() {

		String tempeId = request.getParameter("tempeId");
		String approveText = request.getParameter("approveText");
		String nextRoles = request.getParameter("nextRoles");

		String nextRolePs = request.getParameter("nextRolePs");
		String workflowType = request.getParameter("workflowType");
		String actionId = request.getParameter("actionId");

		tRemote.reportHrJLaborTempe(Long.parseLong(tempeId), Long
				.parseLong(actionId), employee.getWorkerCode(), approveText,
				nextRoles, nextRolePs, workflowType);
		
		write("{success:true,msg:'上报成功！'}");

	}	
	
	 public void getHrJLaborTempeStatus()
	  {
		String tempeMonth = request.getParameter("tempeMonth");
		String str = "";
		str = tRemote.getHrJLaborTempeStatus(tempeMonth, employee.getWorkerCode(),employee.getEnterpriseCode());

		write(str);
	}
	 
		public void getHrJLaborTempeList() throws JSONException {
			
			String start = request.getParameter("start");
			String limit = request.getParameter("limit");
			String flag = request.getParameter("flag");
			String tempeMonth = request.getParameter("tempeMonth");
			String entryIds = "";
			WorkflowService workflowService = new com.opensymphony.workflow.service.WorkflowServiceImpl();

			entryIds = workflowService.getAvailableWorkflow(
					new String[] { "bqHrJLaborTempeApprove" }, employee.getWorkerCode());
			PageObject pg = new PageObject();
			if (start != null && limit != null)
				pg = tRemote.getHrJLaborTempeList(flag, tempeMonth,entryIds,employee.getWorkerCode() ,employee.getEnterpriseCode(),
					Integer.parseInt(start), Integer.parseInt(limit));
			else
				pg = tRemote.getHrJLaborTempeList(flag, tempeMonth,entryIds,employee.getWorkerCode(),employee.getEnterpriseCode());

			if (pg.getTotalCount() > 0) {
				write(JSONUtil.serialize(pg));
			} else {
				write("{totalCount : 0,list :[]}");
			}
		}
	 

		public void hrJLaborTempeApprove() {
		try {
			String mainId = request.getParameter("tempeId");
			String[] mainIds = mainId.split(",");
			for (int i = 0; i < mainIds.length; i++) {
				if (!mainIds[i].equals("")) {
					String tempeId = mainIds[i];
					HrJLaborTempe obj = tRemote.hrJLaborTempeApprove(tempeId,
							workflowType, employee.getWorkerCode(), actionId,
							eventIdentify, approveText, nextRoles, stepId,
							employee.getEnterpriseCode());
					write("{success:true,msg:'操作成功！',obj:"
							+ JSONUtil.serialize(obj) + "}");
				}
			}

		} catch (Exception e) {
			write("{failure:true,msg:'" + e.getMessage() + "'}");
		}
	}
		
		@SuppressWarnings("unchecked")
		public void getLaborTempeInfo() throws JSONException {
			String tempeMonth = request.getParameter("tempeMonth");
			String entryIds = "";
			WorkflowService workflowService = new com.opensymphony.workflow.service.WorkflowServiceImpl();

			entryIds = workflowService.getAvailableWorkflow(
					new String[] { "bqHrJLaborTempeApprove" }, employee.getWorkerCode());
			List list = null;
			list = tRemote.getLaborTempeInfo(tempeMonth, entryIds,employee.getEnterpriseCode());
			write(JSONUtil.serialize(list));
		}
		
		private String addColumnIndex(int[] column_indexs, Cell[] cells) {
			for (int i = 0; i < cells.length; i++) {
				boolean isError = true;

				for (int j = 0; j < LABORTEMPE_COLUMN_NAMES.length; j++) {
					if (LABORTEMPE_COLUMN_NAMES[j].equals(cells[i]
							.getContents().trim())) {
						column_indexs[i] = j;
						isError = false;
					}
				}
			
				if (isError) {
					return "{success:true,msg:'" + cells[i].getContents().trim()
							+ "列不是要导入的具体列！'}";
				}
			}

			return null;
		}

    public void importLaborTempeInfo() throws IOException,DataChangeException {
	   InputStream is = null;
	   Workbook workbook = null;
    	// 返回到页面的reponseText
	   String str = "";
		String tempeMonth = request.getParameter("tempeMonth");
		
		  str = this.treatLaborTempeXlsFile(is, workbook,tempeMonth);
		  write(str);
	
    }
  
	private String treatLaborTempeXlsFile(InputStream is, Workbook workbook,String tempeMonth )
	throws IOException {

		String msg = "";
		List<HrJLaborTempeDetail> laborTempeList = new ArrayList<HrJLaborTempeDetail>();

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
				String columnError = null;
				columnError = addColumnIndex(column_indexs, sheet.getRow(0));
				// 列名是否存在问题
				if (columnError != null)
					return columnError;

				for (int i = 1; i < rows; i++) {
					HrJLaborTempeDetail laborTempeEntity = new HrJLaborTempeDetail();
					Cell[] cells = sheet.getRow(i);// i行的所有单元格
					for (int j = 0; j < column_indexs.length; j++) {
						/* 0:部门,1:实有人数,2: 高温标准人数, 3:高温标准, 4:中温标准人数 ,5:中温标准, 
						 * 6:低温标准人数,7: 低温标准,8: 备注
						 */
						String tempeId =tRemote.getHrJLaborTempeMainId(tempeMonth,employee.getWorkerCode(),employee.getEnterpriseCode());
						if(tempeId==null || tempeId.equals("")){

							HrJLaborTempe baseInfo = new HrJLaborTempe();
							baseInfo = saveLaborTempeMain();
							tempeId =baseInfo.getTempeId().toString();
						}
							laborTempeEntity.setTempeId(Long.parseLong(tempeId));
					
						// 0:部门
						 if (column_indexs[j] == 0) {
							if (cells.length > j	&& !"".equals(cells[j].getContents().trim())) {
								laborTempeEntity.setDeptName(cells[j].getContents().trim());
							} 
						}
						// 1:实有人数
						else if (column_indexs[j] == 1) {
							if (cells.length > j&& !"".equals(cells[j].getContents().trim())) {
								laborTempeEntity.setFactNum(Long.parseLong(cells[j].getContents().trim()));
							} 

						}
						// 2:高温标准人数
						else if (column_indexs[j] == 2) {
							if (cells.length > j&& !"".equals(cells[j].getContents().trim())) {
								laborTempeEntity.setHighTempeNum(Long.parseLong(cells[j]	.getContents().trim()));
							}
						}
						// 3:高温标准
						else if (column_indexs[j] == 3) {
							if (cells.length > j&& !"".equals(cells[j].getContents().trim())) {
								laborTempeEntity.setHighTempeStandard(Double.parseDouble(cells[j]	.getContents().trim()));
							} 
						}
						// 4:中温标准人数
						else if (column_indexs[j] ==4) {
							if (cells.length > j&& !"".equals(cells[j].getContents().trim())) {
								laborTempeEntity.setMidTempeNum(Long.parseLong(cells[j]	.getContents().trim()));
							} 
						}
						 //	5:中温标准
						 else if (column_indexs[j] ==5) {
							if (cells.length > j&& !"".equals(cells[j].getContents().trim())) {
								laborTempeEntity.setMidTempeStandard(Double.parseDouble(cells[j]	.getContents().trim()));
							} 
						}
						 //	6:低温标准人数
						 else if (column_indexs[j] == 6) {
							if (cells.length > j&& !"".equals(cells[j].getContents().trim())) {
								laborTempeEntity.setLowTempeNum(Long.parseLong(cells[j]	.getContents().trim()));
							} 
						}
						 //	7:低温标准
						 else if (column_indexs[j] == 7) {
							if (cells.length > j&& !"".equals(cells[j].getContents().trim())) {
								laborTempeEntity.setLowTempeStandard(Double.parseDouble(cells[j]	.getContents().trim()));
							}
						}
						 //	8:备注
						 else if (column_indexs[j] == 8) {
							if (cells.length > j&& !"".equals(cells[j].getContents().trim())) {
								laborTempeEntity.setMemo(cells[j]	.getContents().trim());
							} 
						}
					}

					if (msg.equals("")) {
						if (laborTempeEntity.getTempeId() == null) {
							msg = "主表ID必须存在，请检查!";
							break;
						}
					}
					laborTempeEntity.setModifyBy(employee.getWorkerCode());
					laborTempeEntity.setModifyDate(new Date());
					laborTempeEntity.setEnterpriseCode(employee.getEnterpriseCode());
					laborTempeEntity.setIsUse("Y");

					laborTempeList.add(laborTempeEntity);
				}

			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			workbook.close();
			is.close();
		}

		if (msg.equals("")) {
			tRemote.importLaborTempeRecords(laborTempeList,employee.getEnterpriseCode());
			return "{success:true,msg:'导入成功！'}";
		} else {
			return "{success:true,msg:'数据填写存在问题：" + msg + "'}";
		}

	}
    
    
		public String getWorkflowType() {
			return workflowType;
		}


		public void setWorkflowType(String workflowType) {
			this.workflowType = workflowType;
		}


		public String getActionId() {
			return actionId;
		}


		public void setActionId(String actionId) {
			this.actionId = actionId;
		}


		public String getApproveText() {
			return approveText;
		}


		public void setApproveText(String approveText) {
			this.approveText = approveText;
		}


		public String getNextRoles() {
			return nextRoles;
		}


		public void setNextRoles(String nextRoles) {
			this.nextRoles = nextRoles;
		}


		public String getEventIdentify() {
			return eventIdentify;
		}


		public void setEventIdentify(String eventIdentify) {
			this.eventIdentify = eventIdentify;
		}


		public String getStepId() {
			return stepId;
		}


		public void setStepId(String stepId) {
			this.stepId = stepId;
		}


		public File getXlsFile() {
			return xlsFile;
		}


		public void setXlsFile(File xlsFile) {
			this.xlsFile = xlsFile;
		}
	
}