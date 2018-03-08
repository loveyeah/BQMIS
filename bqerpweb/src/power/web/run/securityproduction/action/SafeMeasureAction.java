package power.web.run.securityproduction.action;

import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.mail.search.FromStringTerm;

import power.ear.comm.ejb.PageObject;
import power.ejb.comm.TreeNode;
import power.ejb.run.securityproduction.SpJAntiAccident;
import power.ejb.run.securityproduction.SpJAntiAccidentCheckupFacadeRemote;
import power.ejb.run.securityproduction.SpJAntiAccidentDetails;
import power.ejb.run.securityproduction.SpJAntiAccidentDetailsFacadeRemote;
import power.ejb.run.securityproduction.SpJAntiAccidentFacadeRemote;
import power.ejb.run.securityproduction.form.CheckupForm;
import power.ejb.run.securityproduction.form.SpJAntiAccidentForm;
import power.web.comm.AbstractAction;

import com.googlecode.jsonplugin.JSONException;
import com.googlecode.jsonplugin.JSONUtil;

@SuppressWarnings("serial")
public class SafeMeasureAction extends AbstractAction {
	private String node;
	private String id;
	private SpJAntiAccident model;
	private SpJAntiAccidentFacadeRemote remote;
	private SpJAntiAccidentDetailsFacadeRemote detailRemote;

	public SafeMeasureAction() {
		remote = (SpJAntiAccidentFacadeRemote) factory
				.getFacadeRemote("SpJAntiAccidentFacade");
		detailRemote = (SpJAntiAccidentDetailsFacadeRemote) factory
				.getFacadeRemote("SpJAntiAccidentDetailsFacade");
		
	}

	public String getNode() {
		return node;
	}

	public void setNode(String node) {
		this.node = node;
	}

	public SpJAntiAccident getModel() {
		return model;
	}

	public void setModel(SpJAntiAccident model) {
		this.model = model;
	}

	// 查找反事故措施条例
	@SuppressWarnings("unchecked")
	public void findByParentCode() {
		String check = request.getParameter("check");
		List<SpJAntiAccidentForm> list = remote.findByParentCode(check,node,employee.getWorkerCode());
		if (list != null) {
			Iterator it = list.iterator();
			List<TreeNode> arr = new ArrayList<TreeNode>();
			while (it.hasNext()) {
				TreeNode model = new TreeNode();
				SpJAntiAccidentForm form = (SpJAntiAccidentForm) it.next();
				model.setId(form.getMeasureCode());
				model.setText(form.getMeasureCode() + " "
						+ form.getMeasureName());
				 //modify by liuyi 090917 将编码为四位的换成树枝节点
				model
						.setLeaf(form.getMeasureCode().length() > 0 ? true
								: false);
//				model
//				.setLeaf(form.getMeasureCode().length() > 4 ? true
//						: false);
				// 
				model.setCode(form.getSpecialCode() + "-" + form.getSpecialName()
						+ "," + form.getFdDutyLeader() + "-"+ form.getFdDutyLeaderName()
						+ "," + form.getFdManager() + "-"+ form.getFdManagerName()
						+ "," + form.getFdTechnologyBy() + "-"+ form.getFdTechnologyName()
						+ "," + form.getFdSuperviseBy() + "-"+ form.getFdSuperviseName()
						+ "," + form.getDtDutyLeader() + "-"+ form.getDtDutyLeaderName()
						+ "," + form.getDtManager() + "-"+ form.getDtManagerName()
						+ "," + form.getDtTechnologyBy() + "-"+ form.getDtTechnologyName()
						+ "," + form.getDtSuperviseBy() + "-"+ form.getDtSuperviseName());
				// 
				
				model.setDescription(form.getEntryBy() + "-" + form.getEntryName()
						+ "," + form.getEntryDept() + "-" + form.getEntryDeptName()
						+ "," + form.getEntryDateString());
				// 备注
				model.setHref(form.getMemo());
				arr.add(model);
			}
			try {
				write(JSONUtil.serialize(arr));
				//System.out.println(JSONUtil.serialize(arr).toString());
			} catch (JSONException e) {
				e.printStackTrace();
			}
		}
	}

	// 查找反事故措施详细条例
	public void findByMeasureCode() {
		List<SpJAntiAccidentDetails> list = detailRemote.findByCode(id);
		try {
			write(JSONUtil.serialize(list));
		} catch (JSONException e) {
			e.printStackTrace();
		}
	}

	/**
	 * 查找反措列表 by ghzhou 09/10/21
	 */
	public void getMeasurListByCode(){
		List<SpJAntiAccidentForm> list = remote.findByCode(id);
		try{
			write(JSONUtil.serialize(list));
			//System.out.println(JSONUtil.serialize(list).toString());
		}
		catch(JSONException e){
			e.printStackTrace();
		}
	}
	
	// 增加反事故措施
	public void addMeasure() {
		model.setEnterpriseCode(employee.getEnterpriseCode());
		model.setIsUse("Y");
		model.setEntryBy(employee.getWorkerCode());
		model.setEntryDept(employee.getDeptCode());
		model.setEntryDate(new Date());
		if (id.equals("0"))
			remote.save(model, id);
		else{
//			remote.save(model, id.substring(0,2));
			remote.save(model, id);
		}
	}

	// 修改反事故措施
	public void updateMeasure() {
		SpJAntiAccident entity=remote.findById(model.getMeasureCode());
		
		if(model.getSpecialCode() == null || model.getSpecialCode().equals(""))
			model.setSpecialCode(null);
		if(model.getFdDutyLeader() == null || model.getFdDutyLeader().equals(""))
			model.setFdDutyLeader(null);
		if(model.getFdManager() == null || model.getFdManager().equals(""))
			model.setFdManager(null);
		if(model.getFdSuperviseBy() == null || model.getFdSuperviseBy().equals(""))
			model.setFdSuperviseBy(null);
		if(model.getFdTechnologyBy() == null || model.getFdTechnologyBy().equals(""))
			model.setFdTechnologyBy(null);
		if(model.getDtDutyLeader() == null ||model.getDtDutyLeader().equals(""))
			model.setDtDutyLeader(null);
		if(model.getDtManager() == null || model.getDtManager().equals(""))
			model.setDtManager(null);
		if(model.getDtSuperviseBy() == null || model.getDtSuperviseBy().equals(""))
			model.setDtSuperviseBy(null);
		if(model.getDtTechnologyBy() == null || model.getDtTechnologyBy().equals(""))
			model.setDtTechnologyBy(null);
		
		entity.setMeasureName(model.getMeasureName());
		entity.setSpecialCode(model.getSpecialCode());
		
		/*if(model.getDtDutyLeader()!=null)
		{
			String leader=model.getDtDutyLeader();
			String dutyLeader[]=leader.split(",");
		}*/
		entity.setFdDutyLeader(model.getFdDutyLeader());
		
		
		
		
		entity.setFdManager(model.getFdManager());
		entity.setFdSuperviseBy(model.getFdSuperviseBy());
		entity.setFdTechnologyBy(model.getFdTechnologyBy());
		entity.setDtDutyLeader(model.getDtDutyLeader());
		entity.setDtManager(model.getDtManager());
		entity.setDtSuperviseBy(model.getDtSuperviseBy());
		entity.setDtTechnologyBy(model.getDtTechnologyBy());
		entity.setEntryBy(employee.getWorkerCode());
		entity.setEntryDate(new Date());
		entity.setEntryDept(employee.getDeptCode());
		entity.setMemo(model.getMemo());
		remote.update(entity);
	}

	// 删除反事故措施
	public void deleteMeasure() {
		SpJAntiAccident model=remote.findById(id);
		remote.delete(model);
	}

	// 保存反事故措施详细
	public void saveDetailMeasure() throws JSONException {
		String addStr = request.getParameter("addStr");
		String updateStr = request.getParameter("updateStr");
		String deleteStr = request.getParameter("deleteStr");
		List<SpJAntiAccidentDetails> addList = null;
		List<SpJAntiAccidentDetails> updateList = null;
		if (addStr != null) {
			addList = parseContent(addStr);
		}
		if (updateStr != null) {
			updateList = parseContent(updateStr);
		}
		detailRemote.saveModified(addList, updateList, deleteStr,id);
	}

	@SuppressWarnings("unchecked")
	private List<SpJAntiAccidentDetails> parseContent(String records)
			throws JSONException {
		List<SpJAntiAccidentDetails> result = new ArrayList<SpJAntiAccidentDetails>();
		Object object = JSONUtil.deserialize(records);
		List list = (List) object;
		int intLen = list.size();
		for (int i = 0; i < intLen; i++) {
			Map map = (Map) list.get(i);
			SpJAntiAccidentDetails m = this.parsemodel(map);
			result.add(m);
		}
		return result;
	}

	@SuppressWarnings("unchecked")
	private SpJAntiAccidentDetails parsemodel(Map map) {
		SpJAntiAccidentDetails m = new SpJAntiAccidentDetails();
		Object detailsId = map.get("detailsId");
		Object measureCode = map.get("measureCode");
		Object content = map.get("content");
		if (detailsId != null && !"".equals(detailsId.toString())) {
			m.setDetailsId(Long.parseLong(detailsId.toString()));
		}
		if (measureCode != null) {
			m.setMeasureCode(measureCode.toString());
		}
		if (content != null) {
			m.setContent(content.toString());
		}
		return m;
	}
	/**
	 * add by fyyang 090920
	 * 查询excel导出所需数据
	 */
	public void getListForExcel()
	{
		List<SpJAntiAccidentForm> list = remote.findAllAntiAccidentByParent(employee.getDeptCode(), employee.getEnterpriseCode());
	  //-----------------------------------------------------------------------  
		String accidentCodes="";
	    String accidentNames="";
	    for(SpJAntiAccidentForm model: list)
	    {
	    	if(model.getMeasureCode().length()==2)
	    	{
	    		if(accidentCodes.equals("")) 
	    		{
	    			accidentCodes=model.getMeasureCode();
	    			accidentNames=model.getMeasureName();
	    		}
	    		else 
	    		{
	    			accidentCodes=accidentCodes+","+model.getMeasureCode();
	    			accidentNames=accidentNames+","+model.getMeasureName();
	    		}
	    	}
	    }
	    //编码为2位的节点编码及名称
	    if(accidentCodes.equals(""))
	    {
	    	return ;
	    }
	    String [] code2s=accidentCodes.split(",");
	    String [] name2s=accidentNames.split(",");
	    
	  //------------------------------------------------------------------------------  
	    String count2s="";
	    String c4s="";
	    String c4names="";
	    for(int j=0;j<code2s.length;j++)
	    {
	    	int num=0;
	    	String tempC4s="";
	    	String tempC4Names="";
	    	for(SpJAntiAccidentForm model: list)
	    	{
	    		if(model.getMeasureCode().length()==4&&model.getMeasureCode().substring(0, 2).equals(code2s[j]))
	    		{
	    			num++;
	    			if(tempC4s.equals(""))
	    			{
	    				tempC4s=model.getMeasureCode();
	    				tempC4Names=model.getMeasureName();
	    			}
	    			else
	    			{
	    				tempC4s=tempC4s+";"+model.getMeasureCode();
	    				tempC4Names=tempC4Names+";"+model.getMeasureName();
	    			}
	    		}
	    	}
	    	if(count2s=="") count2s=num+"";
	    	else
	    	{
	    		count2s=count2s+","+num;
	    	}
	    	if(c4s=="") 
	    	{
	    		c4s=tempC4s;
	    		c4names=tempC4Names;
	    	}
	    	else
	    	{
	    		c4s=c4s+","+tempC4s;
	    		c4names=c4names+","+tempC4Names;
	    	}
	    }
	    
	    //编码为2位子节点的个数
	    String [] num2s=count2s.split(",");
	  //编码为4位的节点编码及名称
	    String [] code4s=c4s.split(",");
	    String [] name4s=c4names.split(",");
	    //--------------------------------------------------------------------------------

	    //---------------------------------------------------------------------------------
	    String strResult="data:[";
	    String html="";
	    for(int i=0;i<code2s.length;i++)
	    {
	    	int line=i+1;
	    	html+="{\"tb\":\"<table border=1>";
	    	if(i==0) html+="<tr><th  colspan=11>二十五项反措动责任落实分解表</th></tr>";
	    	html+="<tr><th colspan=11 align='left'>"+line+"、"+name2s[i]+"</th></tr>";
	    	html+="<tr><th colspan=11 align='left'>填写单位："+employee.getDeptName()+"</th></tr>";
	    	html+="<tr><th rowspan=3>项目</th><th rowspan=3>序号</th><th rowspan=3> 应完成的工作</th><th colspan=8>责任落实情况</th></tr>";
	    	html+="<tr><th colspan=4>发电企业</th><th colspan=4>大唐陕西发电公司</th></tr>";
	    	html+="<tr><th>责任领导</th><th>管理责任人</th><th>技术责任人</th><th>监督责任人</th><th>责任领导</th><th>管理责任人</th><th>技术责任人</th><th>监督责任人</th></tr>";

	    	String [] code4=code4s[i].split(";");
	    	String [] name4=name4s[i].split(";");
	         for(int k=0;k<code4.length;k++)
	         {
	        	 String tr="";
	        	 int detailCount=0;
	        	 
	        	 String c6s="";
	        	 String n6s="";
	 	    	  for(SpJAntiAccidentForm model: list)
	 		    	{
	 		    		if(model.getMeasureCode().length()==6&&model.getMeasureCode().substring(0, 4).equals(code4[k]))
	 		    		{
	 		    			if(c6s.equals(""))
	 		    			{
	 		    				c6s=model.getMeasureCode();
	 		    				n6s=model.getMeasureName();
	 		    			}
	 		    			else
	 		    			{
	 		    				c6s=c6s+","+model.getMeasureCode();
	 		    				n6s=n6s+","+model.getMeasureName();
	 		    			}
	 		    			
	 		    		}
	 		    	}
	 	    	String [] code6s=c6s.split(",");
	 	    	String [] name6s=n6s.split(",");
	 	    	int no=0;
	 	    	for(int m=0;m<code6s.length;m++)
	 	    	{
	 	    		SpJAntiAccidentForm detailMan=new SpJAntiAccidentForm();
	 	    		for(SpJAntiAccidentForm model: list)
	 	    		{
	 	    		 if(model.getMeasureCode().equals(code6s[m]))
	 	    		 {
	 	    			detailMan=model;
	 	    			break;
	 	    		 }
	 	    		}
	 	    		
	 	    		 tr+="<tr><td></td><td colspan=8>"+name6s[m]+"<td></tr>";
	 	    		List<SpJAntiAccidentDetails> detailList = detailRemote.findByCode(code6s[m]);
	 	    		detailCount+=detailList.size();
	 	    		for(SpJAntiAccidentDetails detailModel:detailList)
		         	{
	 	    			no++;
	 	    			tr+="<tr><td>"+no+"</td><td>"+detailModel.getContent()+"</td><td>"+detailMan.getFdDutyLeaderName()+"</td><td>"+detailMan.getFdManagerName()+"</td><td>"+detailMan.getFdTechnologyName()+"</td><td>"+detailMan.getFdSuperviseName()+"</td>";
		         	    tr+="<td>"+detailMan.getDtDutyLeaderName()+"</td><td>"+detailMan.getDtManagerName()+"</td><td>"+detailMan.getDtTechnologyName()+"</td><td>"+detailMan.getDtSuperviseName()+"</td></tr>";
		         	}
	 	    	}
	 	    	detailCount=detailCount+code6s.length+1;
	 	    	String headtr="<tr><td rowspan='"+detailCount+"'>"+name4[k]+"</td><td colspan=10></td></tr>";
	 	    	tr=headtr+tr;
	 	    	html+=tr;
	         	
	         }
	         
	         html+="</table>\"},";
	         
	    }
	    
	    strResult+=html;
	    strResult=strResult.substring(0, strResult.length()-1)+"]";

	  // strResult="data:[{\"tb\": \"<table border=1></table>\"}]";
	   write("{"+strResult+"}");
	
	}

	//删除反事故措施详细
	public void deleteDetail(){
		String ids=request.getParameter("ids");
		detailRemote.delete(ids);
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}
}
