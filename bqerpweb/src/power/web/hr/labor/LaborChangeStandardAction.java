package power.web.hr.labor;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import com.googlecode.jsonplugin.JSONException;
import com.googlecode.jsonplugin.JSONUtil;

import power.ear.comm.ejb.PageObject;
import power.ejb.hr.HrCLbgzbmFacadeRemote;
import power.ejb.hr.labor.HrCLaborBean;
import power.ejb.hr.labor.HrJLaborChange;
import power.ejb.hr.labor.HrJLaborStandard;
import power.ejb.hr.labor.LaborInfoMaint;
import power.ejb.manage.plan.itemplan.BpCItemplanTopic;
import power.web.comm.AbstractAction;

public class LaborChangeStandardAction extends AbstractAction{
	private LaborInfoMaint maintRemote;
	private HrJLaborChange changeInfo;
	private HrJLaborStandard standard;
	
	private Long lbWorkId;
	public LaborChangeStandardAction()
	{
		maintRemote=(LaborInfoMaint)factory.getFacadeRemote("LaborInfoMaintImpl");
	}
	
	/**
	 * 劳保工种变更维护
	 * @throws JSONException 
	 * @throws ParseException 
	 */
	
	
	public void getAllWorkInfo() throws JSONException{
	
		HrCLbgzbmFacadeRemote a = (HrCLbgzbmFacadeRemote)
			factory.getFacadeRemote("HrCLbgzbmFacade");
		PageObject obj = a.findAllLbgzbms(employee.getEnterpriseCode());
		String str = JSONUtil.serialize(obj);
     	write(str);
	}
	
	
	@SuppressWarnings("unchecked")
	public void saveLaborChangeInfo() throws JSONException, ParseException
	{
		try {
			String str = request.getParameter("updateData"); 
			String workcode=request.getParameter("empId");
			Object obj = JSONUtil.deserialize(str);
			List<Map> list = (List<Map>) obj;
			List<HrJLaborChange> postlist = new ArrayList<HrJLaborChange>();
			DateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			String changeDate=null;
			String startDate=null;
			for (Map data : list) {
	
				HrJLaborChange model = new HrJLaborChange();
				System.out.println(data.get("newLbWorkId"));
				if(data.get("laborChangeId")!=null && !data.get("laborChangeId").equals("")){
					model.setLaborChangeId(Long.valueOf(data.get("laborChangeId").toString()));
				}
				
				model.setWorkCode(workcode);
				
				if(data.get("changeDate") !=null && !data.get("changeDate").equals("")){
					changeDate = data.get("changeDate").toString();
					model.setChangeDate(format.parse(changeDate));
				}
				if (data.get("startDate") != null && !data.get("startDate").equals("")){
					startDate=data.get("startDate").toString();
				    model.setStartDate(format.parse(startDate));
				}
				model.setEnterpriseCode(employee.getEnterpriseCode());
				model.setIsUse("Y");
				if (data.get("newLbWorkId") != null && !data.get("newLbWorkId").equals("")){
				    model.setNewLbWorkId(Long.valueOf(data.get("newLbWorkId").toString()));
				}else{
					model.setNewLbWorkId(-1L);
				}
					
				if (data.get("oldLbWorkId") != null && !data.get("oldLbWorkId").equals("")){
					 model.setOldLbWorkId(Long.valueOf(data.get("oldLbWorkId").toString()));
				}else{
					model.setOldLbWorkId(-1L);
				}
				

				postlist.add(model);
			}
			
			maintRemote.saveLaborChange(postlist);
			write("{success:true'}");
		} catch (RuntimeException e) {
			e.printStackTrace();
			write("{success:false'}");
		
	}

}

	
	
	public void deleteLaborChangeInfo()
	{
		String Ids = request.getParameter("ids");
		maintRemote.deleteLaborChange(Ids);
		write("{success:true}");
	}
	
	public void findLaborChangeList() throws JSONException
	{
	String empId1=request.getParameter("empId");
		

		PageObject  pgobj = maintRemote.findLaborChangeList(empId1,employee.getEnterpriseCode());
		
	
	   List<HrCLaborBean> beanList=new ArrayList<HrCLaborBean>();
	
	   List itList=pgobj.getList();
	   Iterator it=itList.iterator();
	   while(it.hasNext()){
		   HrCLaborBean bean=new HrCLaborBean();
		   Object[] obj=(Object[])it.next();
		   bean.setChangeDate(changeNullToString(obj[1]));
		   bean.setStartDate(changeNullToString(obj[2]));
		   bean.setOldLbWorkId(changeNullToString(obj[9]));
		   bean.setOldLbWorkName(changeNullToString(obj[3]));
		   bean.setNewLbWorkId(changeNullToString(obj[8]));
		   bean.setNewLbWorkName(changeNullToString(obj[4]));
		   bean.setLaborChangeId(changeNullToString(obj[0]));
		   
		   beanList.add(bean);
	   }
	   
		write(JSONUtil.serialize(beanList));
	}
	
	public String changeNullToString(Object obj){
		if(obj!=null){
			return obj.toString();
		}
		return "";
	}

	

	/**
	 * 劳保用品领用标准维护
	 * @throws JSONException 
	 */
	
	public void saveLaborStandard() throws JSONException
	{
		String addStr = request.getParameter("isAdd");
		String updateStr = request.getParameter("isUpdate");
		String ids = request.getParameter("ids");
		List<Map> addMapList = (List<Map>)JSONUtil.deserialize(addStr);
		List<Map> updateMapList = (List<Map>) JSONUtil.deserialize(updateStr);
		List<HrJLaborStandard> addList = new ArrayList<HrJLaborStandard>();
		List<HrJLaborStandard> updateList = new ArrayList<HrJLaborStandard>();
		
		for(Map map : addMapList)
		{
			HrJLaborStandard temp = this.parseLaborStandardInstance(map);
			addList.add(temp);
		}
		for (Map map : updateMapList) {
			HrJLaborStandard temp = this.parseLaborStandardInstance(map);
			updateList.add(temp);
		}
		if(addList.size() > 0|| updateList.size() > 0  ||ids != null)
		{
			maintRemote.saveLaborStandard(addList, updateList,ids);
		}
		write("{success:true,msg:'数据保存修改成功！'}");
	
	}
	
	
	/**
	 * 将一映射转化为一劳保用品对象
	 * @param map
	 * @return
	 */
	public HrJLaborStandard parseLaborStandardInstance(Map map)
	{
		HrJLaborStandard temp = new HrJLaborStandard();
		
		Long laborStandardId = null;
		Long lbWorkId = null;
		Long laborMaterialId = null;
		Long spacingMonth = null;
		Long materialNum = null;
		String sendKind = null;
		String isUse = "Y";
		String enterpriseCode = employee.getEnterpriseCode();
		
		if(map.get("laborStandardId") != null)
			laborStandardId = Long.parseLong(map.get("laborStandardId").toString());
		if(map.get("lbWorkId") != null)
			lbWorkId = Long.parseLong(map.get("lbWorkId").toString());
		if(map.get("laborMaterialId") != null)
			laborMaterialId = Long.parseLong(map.get("laborMaterialId").toString());
		if(map.get("spacingMonth") != null)
			spacingMonth = Long.parseLong(map.get("spacingMonth").toString());
		if(map.get("materialNum") != null)
			materialNum = Long.parseLong(map.get("materialNum").toString());
		if(map.get("sendKind") != null)
			sendKind = map.get("sendKind").toString();
			
		temp.setLaborStandardId(laborStandardId);
		temp.setLbWorkId(lbWorkId);
		temp.setLaborMaterialId(laborMaterialId);
		temp.setSpacingMonth(spacingMonth);
		temp.setMaterialNum(materialNum);
		temp.setSendKind(sendKind);
		temp.setIsUse(isUse);
		temp.setEnterpriseCode(enterpriseCode);
		return temp;
	}
	
	public void findLaborStandardList() throws JSONException
	{
		String start = request.getParameter("start");
		String limit = request.getParameter("limit");
		PageObject pg = null;
		if(start != null && limit != null)
			pg = maintRemote.findLaborStandardList(this.getLbWorkId(),employee.getEnterpriseCode(),Integer.parseInt(start),
					Integer.parseInt(limit));
		else 
			pg = maintRemote.findLaborStandardList(this.getLbWorkId(),employee.getEnterpriseCode());
		
		write(JSONUtil.serialize(pg));
	
	}
	
	public void findNoSelectLaborStandardList() throws JSONException
	{
		String start = request.getParameter("start");
		String limit = request.getParameter("limit");
		PageObject pg = null;
		if(start != null && limit != null)
			pg = maintRemote.findNoSelectLaborStandardList(this.getLbWorkId(),employee.getEnterpriseCode(),Integer.parseInt(start),
					Integer.parseInt(limit));
		else 
			pg = maintRemote.findNoSelectLaborStandardList(this.getLbWorkId(),employee.getEnterpriseCode());
		
		write(JSONUtil.serialize(pg));
			
	}
	
	public HrJLaborChange getChangeInfo() {
		return changeInfo;
	}

	public void setChangeInfo(HrJLaborChange changeInfo) {
		this.changeInfo = changeInfo;
	}

	public HrJLaborStandard getStandard() {
		return standard;
	}

	public void setStandard(HrJLaborStandard standard) {
		this.standard = standard;
	}

	public Long getLbWorkId() {
		return lbWorkId;
	}

	public void setLbWorkId(Long lbWorkId) {
		this.lbWorkId = lbWorkId;
	}

	
	
	

	
}
