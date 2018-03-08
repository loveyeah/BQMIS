package power.web.run.securityproduction.action.safesuperviseaction;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import jxl.Cell;
import jxl.Workbook;

import org.apache.xmlbeans.impl.xb.xsdschema.Public;

import com.googlecode.jsonplugin.JSONException;
import com.googlecode.jsonplugin.JSONUtil;

import power.ear.comm.ejb.PageObject;
import power.ejb.manage.plan.BpJPlanJobDepMainFacadeRemote;
import power.ejb.run.securityproduction.safesupervise.SpCDynamicCheckDetail;
import power.ejb.run.securityproduction.safesupervise.SpCDynamicCheckDetailFacadeRemote;
import power.ejb.run.securityproduction.safesupervise.SpCDynamicCheckMain;
import power.ejb.run.securityproduction.safesupervise.SpCDynamicCheckMainFacadeRemote;
import power.web.comm.AbstractAction;

@SuppressWarnings("serial")
public class DynamicCheckAction extends AbstractAction {
	private SpCDynamicCheckDetailFacadeRemote detailRemote;
	private SpCDynamicCheckMainFacadeRemote mainRemote;
	protected BpJPlanJobDepMainFacadeRemote mRemote;
	private SpCDynamicCheckDetail detailModel;
	private SpCDynamicCheckMain mainModel;
	
	private File xlsFile;


	public DynamicCheckAction() {
		detailRemote = (SpCDynamicCheckDetailFacadeRemote) factory
				.getFacadeRemote("SpCDynamicCheckDetailFacade");
		mainRemote = (SpCDynamicCheckMainFacadeRemote) factory
				.getFacadeRemote("SpCDynamicCheckMainFacade");
		mRemote = (BpJPlanJobDepMainFacadeRemote) factory
				.getFacadeRemote("BpJPlanJobDepMainFacade");
	}

	public SpCDynamicCheckDetail getDetailModel() {
		return detailModel;
	}

	public void setDetailModel(SpCDynamicCheckDetail detailModel) {
		this.detailModel = detailModel;
	}

	public SpCDynamicCheckMain getMainModel() {
		return mainModel;
	}

	public void setMainModel(SpCDynamicCheckMain mainModel) {
		this.mainModel = mainModel;
	}

	/**
	 * 增加动态检查信息
	 */
	public void saveDynamicCheckInfo() {
		Long mainId = 0l;
		if (mainModel.getMainId() == null) {
			mainModel.setEntryBy(employee.getWorkerCode());
			mainModel.setEnterpriseCode(employee.getEnterpriseCode());
			SpCDynamicCheckMain entity = mainRemote.save(mainModel);
			mainId = entity.getMainId();
		} else {
			mainId = mainModel.getMainId();
		}
		detailModel.setEnterpriseCode(employee.getEnterpriseCode());
		detailModel.setEntryBy(employee.getWorkerCode());
		detailModel.setMainId(mainId);
		detailRemote.save(detailModel);
		write("{success:true,msg:'增加成功！'}");
	}

	/**
	 * 修改动态检查信息
	 */
	public void updateDynamicCheckInfo() {
		String flag = request.getParameter("flag");
		SpCDynamicCheckDetail entity = detailRemote.findById(detailModel
				.getDetailId());
		if (flag.equals("1")) {
			// 动态检查
			entity.setAvoidStep(detailModel.getAvoidStep());
			entity.setDutyBy(detailModel.getDutyBy());
			entity.setDutyDeptCode(detailModel.getDutyDeptCode());
			entity.setExistQuestion(detailModel.getExistQuestion());
			entity.setIssueProerty(detailModel.getIssueProerty());

			entity.setPlanDate(detailModel.getPlanDate());
			entity.setSuperBy(detailModel.getSuperBy());
			entity.setSuperDeptCode(detailModel.getSuperDeptCode());
			entity.setWholeStep(detailModel.getWholeStep());
		}
		if (flag.equals("2")) {
			// 整改计划
			entity.setActualDate(detailModel.getActualDate());
			entity.setNoReason(detailModel.getNoReason());

		}

		detailRemote.update(entity);
		write("{success:true,msg:'修改成功！'}");
	}

	/**
	 * 删除动态检查信息
	 */
	public void deleteDynamicCheckInfo() {
		String ids = request.getParameter("ids");
		detailRemote.delete(ids);
		write("{success:true,msg:'删除成功！'}");

	}

	/**
	 * 查询动态检查信息
	 * 
	 * @throws JSONException
	 */
	public void findDynamicCheckList() throws JSONException {
		PageObject obj = new PageObject();
		Object objstart = request.getParameter("start");
		Object objlimit = request.getParameter("limit");
		String year = request.getParameter("year");
		String season = request.getParameter("season");

		if (objstart != null && objlimit != null) {
			int start = Integer.parseInt(request.getParameter("start"));
			int limit = Integer.parseInt(request.getParameter("limit"));
			obj = detailRemote.findDynamicListForCheck(year, season, employee
					.getEnterpriseCode(), employee.getWorkerCode(), start,
					limit);
		} else {
			obj = detailRemote.findDynamicListForCheck(year, season, employee
					.getEnterpriseCode(), employee.getWorkerCode());
		}
		String str = JSONUtil.serialize(obj);
		write(str);
	}

	/**
	 * 查询整改计划信息列表
	 * 
	 * @throws JSONException
	 */
	public void findDynamicPlanList() throws JSONException {
		PageObject obj = new PageObject();
		Object objstart = request.getParameter("start");
		Object objlimit = request.getParameter("limit");
		String year = request.getParameter("year");
		String season = request.getParameter("season");

		if (objstart != null && objlimit != null) {
			int start = Integer.parseInt(request.getParameter("start"));
			int limit = Integer.parseInt(request.getParameter("limit"));
			obj = detailRemote.findDynamicListForPlan(year, season, employee
					.getEnterpriseCode(), employee.getWorkerCode(), start,
					limit);
		} else {
			obj = detailRemote.findDynamicListForPlan(year, season, employee
					.getEnterpriseCode(), employee.getWorkerCode());
		}
		String str = JSONUtil.serialize(obj);
		write(str);
	}

	/**
	 * 查询整改完成情况统计信息列表
	 * 
	 * @throws JSONException
	 */
	public void findDynamicQueryList() throws JSONException {
		PageObject obj = new PageObject();
		Object objstart = request.getParameter("start");
		Object objlimit = request.getParameter("limit");
		String year = request.getParameter("year");
		String season = request.getParameter("season");
		String existQuestion = request.getParameter("existQuestion");
		String isFinish = request.getParameter("isFinish");
		if (objstart != null && objlimit != null) {
			int start = Integer.parseInt(request.getParameter("start"));
			int limit = Integer.parseInt(request.getParameter("limit"));
			obj = detailRemote
					.findDynamicListForQuery(year, season, employee
							.getEnterpriseCode(), existQuestion, isFinish,
							start, limit);
		} else {
			obj = detailRemote.findDynamicListForQuery(year, season, employee
					.getEnterpriseCode(), existQuestion, isFinish);
		}
		if (obj != null && obj.getTotalCount() > 0) {
			String str = JSONUtil.serialize(obj);
			write(str);
		} else {
			write("{\"list\":[],\"totalCount\":0}");
		}
	}

	/**
	 * 根据部门ID获取一级部门名称
	 * 
	 * @throws JSONException
	 *             add by ltong 20100429
	 */
	public void getdeptcode() throws JSONException {
		String deptId = request.getParameter("deptId");
		String deptCode = mRemote.getManagerDept(Long.parseLong(deptId));
		write(JSONUtil.serialize(deptCode));
	}

	/**
	 * 问题汇总查询
	 * 
	 * @throws JSONException
	 *             add by ltong 20100510
	 */
	public void findMatterGather() throws JSONException {
		PageObject obj = new PageObject();
		Object objstart = request.getParameter("start");
		Object objlimit = request.getParameter("limit");
		String monthDate = request.getParameter("monthDate");
		String problemKind = request.getParameter("problemKind");

		if (objstart != null && objlimit != null) {
			int start = Integer.parseInt(request.getParameter("start"));
			int limit = Integer.parseInt(request.getParameter("limit"));
			obj = detailRemote.queryDynamicGatherList(monthDate, employee
					.getEnterpriseCode(), problemKind, start, limit);
		} else {
			obj = detailRemote.queryDynamicGatherList(monthDate, employee
					.getEnterpriseCode(), problemKind);
		}
		String str = JSONUtil.serialize(obj);
		write(str);
	}

	/**
	 * 问题汇总明细查询
	 * 
	 * @throws JSONException
	 *             add by ltong 20100510
	 */
	public void findMatterGatherQuery() throws JSONException {
		PageObject obj = new PageObject();
		Object objstart = request.getParameter("start");
		Object objlimit = request.getParameter("limit");
		String monthDate = request.getParameter("monthDate");
		String problemKind = request.getParameter("problemKind");
		String deptCode = request.getParameter("deptCode");
		if (objstart != null && objlimit != null) {
			int start = Integer.parseInt(request.getParameter("start"));
			int limit = Integer.parseInt(request.getParameter("limit"));
			obj = detailRemote.findDynamicDetailForGather(monthDate, employee
					.getEnterpriseCode(), problemKind, deptCode, start, limit);
		} else {
			obj = detailRemote.findDynamicDetailForGather(monthDate, employee
					.getEnterpriseCode(), problemKind, deptCode);
		}
		String str = JSONUtil.serialize(obj);
		write(str);
	}
	
	/**
	 * add by fyyang 20100514 导入
	 */
	public void importMatterInfo()
	{
		
		String strYear=request.getParameter("quarterDate");
		String checkKind=request.getParameter("quarterBoxName");
		InputStream is;
		 SpCDynamicCheckMain mainEntity= new SpCDynamicCheckMain();
		 List<SpCDynamicCheckDetail> detailList= new ArrayList<SpCDynamicCheckDetail>();
		//主表信息
		 mainEntity.setYear(strYear);
		 mainEntity.setSeason(checkKind);
		 mainEntity.setEntryBy(employee.getWorkerCode());
		 mainEntity.setEnterpriseCode(employee.getEnterpriseCode());
		 String dutyNames="";
		 String superNames="";
		 String dutyCr="";
		 String superCr="";
		 String msg="";
		try {
			is = new FileInputStream(xlsFile);
			jxl.Workbook wb = Workbook.getWorkbook(is); // 得到工作薄
			jxl.Sheet st = wb.getSheet(0);// 得到工作薄中的第一个工作表 
			int rsRows = st.getRows(); // 得到excel的总行数
             System.out.println(rsRows);
			   if(rsRows<=1)
			   {
				   write("{success:true,msg:'无数据进行导入！'}");
			   }
			   else
			   {
			   for (int i = 1; i < rsRows; i++) {
				SpCDynamicCheckDetail detailEntity=new SpCDynamicCheckDetail();
				
//				yuan 存在问题  整改措施 整改前防范措施  整改责任人  整改监督人  问题性质  计划完成时间
//
//				new 存在问题  整改计划  计划整改时间  责任人  监督人  问题性质
				if("".equals(st.getCell(3, i).getContents()))
				{
					i++;
					msg="第"+i+"行整改责任人必须填写！’";
					break;
				}
				if("".equals(st.getCell(4, i).getContents()))
				{
					i++;
					msg="第"+i+"行整改监督人必须填写！’";
					break;
				}
				if(("一般性质").equals(st.getCell(5, i).getContents()))
				{
					detailEntity.setIssueProerty("1");
				}
				else if(("重大性质").equals(st.getCell(5, i).getContents()))
				{
					detailEntity.setIssueProerty("2");
				}
				else
				{
					i++;
					msg="第"+i+"行问题性质填写有误，请填写‘一般性质’或‘重大性质’";
					break;
				}
				
				if("".equals(st.getCell(2, i).getContents()))
				{
					i++;
					msg="第"+i+"行计划完成时间必须填写！’";
					break;
				}
				String [] strDate=st.getCell(2, i).getContents().split("/");
				if(strDate.length!=3)
				{
					i++;
					msg="第"+i+"行计划完成时间格式填写不正确！’";
					break;
				}
				if(strDate[1].length()==1) strDate[1]="0"+strDate[1];
				if(strDate[2].length()==1) strDate[2]="0"+strDate[2];
				SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
				try 
				{
				detailEntity.setPlanDate(df.parse(strDate[0]+"-"+strDate[1]+"-"+strDate[2]));
				}
				catch(Exception e1)
				{
					i++;
					msg="第"+i+"行计划完成时间格式填写不正确！’";
					break;
				}
			
				detailEntity.setExistQuestion(st.getCell(0, i).getContents());
				detailEntity.setWholeStep(st.getCell(1, i).getContents());
			//	detailEntity.setAvoidStep(st.getCell(2, i).getContents());
				detailEntity.setAvoidStep("");
				detailEntity.setDutyBy(st.getCell(3, i).getContents());
				detailEntity.setSuperBy(st.getCell(4, i).getContents());
				if(dutyNames.equals("")) 
					{
					 dutyNames="'"+st.getCell(3, i).getContents()+"'";
					 dutyCr=i+",3";
					}
				else 
				{
					if((dutyNames).indexOf("'"+st.getCell(3, i).getContents()+"'")==-1)
					{
						dutyNames+=",'"+st.getCell(3, i).getContents()+"'";
						 dutyCr+=";"+i+",3";
						
					}
				}
				if(superNames.equals("")) 
					{
					superNames="'"+st.getCell(4, i).getContents()+"'";
					superCr=i+",4";
					}
				else 
					{
					if((superNames).indexOf("'"+st.getCell(4, i).getContents()+"'")==-1)
					{
					superNames+=",'"+st.getCell(4, i).getContents()+"'";
					superCr+=";"+i+",4";
					}
					}
				detailEntity.setEntryBy(employee.getWorkerCode());
				detailEntity.setEnterpriseCode(employee.getEnterpriseCode());
				
				
				
				detailList.add(detailEntity);
			}
			wb.close();// 关闭工作薄
			is.close();// 关闭输入流  
			if(msg.equals(""))
			{
			msg=detailRemote.checkInputManName(dutyNames, superNames, dutyCr, superCr);
			}
			if(msg.equals(""))
			{
			 detailRemote.importInfo(mainEntity, detailList);
			 write("{success:true,msg:'导入成功！'}");
			}
			else
			{
				write("{success:true,msg:'数据填写存在问题："+msg+"'}");
			}
			   }
			 
		} catch (Exception e1) {
			e1.printStackTrace();
		} 
	}

	public File getXlsFile() {
		return xlsFile;
	}

	public void setXlsFile(File xlsFile) {
		this.xlsFile = xlsFile;
	}
}
