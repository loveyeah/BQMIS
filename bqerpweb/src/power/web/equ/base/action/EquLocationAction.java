package power.web.equ.base.action;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import com.googlecode.jsonplugin.JSONException;
import com.googlecode.jsonplugin.JSONUtil;

import java.text.ParseException;
import java.text.SimpleDateFormat;

import power.ear.comm.ejb.PageObject;
import power.ejb.comm.TreeNode;
import power.ejb.equ.base.EquCEquipments;
import power.ejb.equ.base.EquCEquipmentsFacadeRemote;
import power.ejb.equ.base.EquCLocation;
import power.ejb.equ.base.EquCLocationFacadeRemote;
import power.ejb.hr.HrJEmpInfo;
import power.ejb.hr.HrJEmpInfoFacadeRemote;
//import power.ejb.equ.base.EquRLocationFacadeRemote;
import power.web.comm.AbstractAction;

public class EquLocationAction extends AbstractAction{
	private EquCLocationFacadeRemote remote;
	private EquCLocation equLoc;
	private String locationId;
	private String classStructureId;
	private String equNum;
	private String StartSysCode;
	public String getStartSysCode() {
		return StartSysCode;
	}
	public void setStartSysCode(String startSysCode) {
		StartSysCode = startSysCode;
	}
	public EquCLocation getEquLoc() {
		return equLoc;
	}
	public void setEquLoc(EquCLocation equLoc) {
		this.equLoc = equLoc;
	}
	public String getLocationId() {
		return locationId;
	}
	public void setLocationId(String locationId) {
		this.locationId = locationId;
	}
	public String getClassStructureId() {
		return classStructureId;
	}
	public void setClassStructureId(String classStructureId) {
		this.classStructureId = classStructureId;
	}
	public String getEquNum() {
		return equNum;
	}
	public void setEquNum(String equNum) {
		this.equNum = equNum;
	}
	
	public EquLocationAction()
	{
		remote=(EquCLocationFacadeRemote) factory.getFacadeRemote("EquCLocationFacade");
	}
	
	/**
	 * 获得位置树
	 * @throws JSONException 
	 */
	public void getLocationTreeList() throws JSONException
	{
		String code="";
		code=request.getParameter("id");
		if(!code.equals("root"))
		{
			code="+"+code.substring(1,code.length());
		}
		List<TreeNode> arrayList=new ArrayList();

		List<EquCLocation> list=remote.getListByParent(code, employee.getEnterpriseCode());
		if(list!=null)
	    {
	         for (int i = 0; i < list.size(); i++)
	         {
	        	 EquCLocation model=list.get(i);
	        	 boolean isLeaf=!remote.ifHasChild(model.getLocationCode(), employee.getEnterpriseCode());
	        	 TreeNode node=new TreeNode();
	        	 node.setId(model.getLocationCode());
	        	 node.setText(model.getLocationCode()+" "+model.getLocationDesc());
	        	 node.setLeaf(isLeaf);
	        	 arrayList.add(node);
	        //	 boolean isLeaf = (model.getLocationCode().length()>8) ? true : false;
	     
	         }
	    
	    }
		write(JSONUtil.serialize(arrayList));
	
	}
	
	/**
	 * 通过位置码获得一条位置信息
	 * @throws JSONException
	 */
	public void getLocationByCode() throws JSONException
	{
		HrJEmpInfoFacadeRemote empRemote=(HrJEmpInfoFacadeRemote)factory.getFacadeRemote("HrJEmpInfoFacade");
		String code="";
		code=request.getParameter("id");
		EquCLocation model=remote.findByCode(code, employee.getEnterpriseCode());
		 String workName="";
		 if(model!=null)
		 {
			List<HrJEmpInfo> list= empRemote.findByEmpCode(model.getChangeBy());
			if(list!=null&&list.size()>0)
			{
				workName=list.get(0).getChsName();
			}
		 }
			String str=JSONUtil.serialize(model);
		 write("{success: true,workName:'"+workName+"',data:"+str+"}");
	}
	
	/**
	 * 增加或修改一条位置信息
	 * @throws ParseException
	 */
	public void addOrUpdateLocationCode() throws ParseException
	{
		String methode=request.getParameter("method");
		if(methode.equals("add"))
		{
			addLocationCode();
		}
		if(methode.equals("update"))
		{
			updateLocationCode();
		}
	}
	
	/**
	 * 增加位置信息
	 * @throws ParseException
	 */
	public void addLocationCode() throws ParseException
	{
		if(equLoc.getLocationCode()!=null)
		{
		  if(equLoc.getPLocationCode().length()==2)
		  {
		   equLoc.setLocationCode(StartSysCode+equLoc.getLocationCode());	
		   }
		   if(!equNum.equals(""))
		   {
			equLoc.setLocationCode(equLoc.getLocationCode()+equNum);
		   }
		
		  if(!equLoc.getPLocationCode().equals("root"))
		  {
			equLoc.setLocationCode(equLoc.getPLocationCode()+equLoc.getLocationCode());
		   }
		}
		else
		{
			equLoc.setLocationCode(equLoc.getPLocationCode()+equNum);
		}
		equLoc.setEnterpriseCode(employee.getEnterpriseCode());
		SimpleDateFormat sf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	   String mydate = sf.format(new java.util.Date());
		equLoc.setChangeDate(sf.parse(mydate));
		equLoc.setChangeBy(employee.getWorkerCode());
		int i= remote.save(equLoc);
		if(i != -1)
		{
			write("{success:true,id:'"+equLoc.getLocationCode()+"',msg:'增加成功！'}");
		}
		else
		{
			write("{success:true,id:'-1',msg:'增加失败:编码重复！'}");
		}
		
	}
	
	
	/**
	 * 修改位置信息
	 */
	public void updateLocationCode()
	{
		EquCLocation model=remote.findById(Long.parseLong(locationId));
		model.setLocationDesc(equLoc.getLocationDesc());
		//model.setChangeBy(equLoc.getChangeBy());
		model.setChangeBy(employee.getWorkerCode());
		//SimpleDateFormat sf = new SimpleDateFormat("yyyy-MM-dd");
	 //   reportdate = tempDate.format(new java.util.Date());
		 model.setChangeDate(new java.util.Date());
		 model.setGisparam1(equLoc.getGisparam1());
		 model.setGisparam2(equLoc.getGisparam2());
		 model.setGisparam3(equLoc.getGisparam3());
		 if(classStructureId!=null)
		 {
			 if(!classStructureId.equals(""))
			 {
			 model.setClassStructureId(Long.parseLong(classStructureId));
			 }
		 }
		 if(remote.update(model))
	     {
	       write("{success:true,msg:'修改成功！'}");
	     }
	     else
	     {
	        write("{success:true,msg:'修改失败:编码重复！'}");
	     }
	}
	
	/**
	 * 通过位置码或位置id删除位置信息
	 */
	public void deleteLocationCode()
	{
		String method=request.getParameter("method");
		 if(method.equals("code"))
		 {
			 deleteLocationByCode();
		 }
		 if(method.equals("id"))
		 {
			 deleteLocationById();
		 }
		
	}
	
	/**
	 * 通过id删除位置信息
	 */
	public void deleteLocationById()
	{
		EquCLocation model=remote.findById(Long.parseLong(locationId));
	  	if(remote.delete(Long.parseLong(locationId)))
	  	{
	  		updateEquForDelete(model.getLocationCode(),model.getEnterpriseCode());
		  write("{success:true,msg:'删除成功！'}");
	  	}
	  	else
	  	{
	  		write("{success:true,msg:'有子节点存在，不能删除！'}");
	  	}
	}
	
	/**
	 * 通过位置码删除位置信息
	 */
	public void deleteLocationByCode()
	{
		String code="";
		code=request.getParameter("id");
		EquCLocation model=remote.findByCode(code, employee.getEnterpriseCode());
		if(remote.delete(model.getLocationId()))
		{
			updateEquForDelete(code,employee.getEnterpriseCode());
		 write("{success:true,msg:'删除成功！'}");
		}
		else
		{
			write("{success:true,msg:'有子节点存在，不能删除！'}");
		}
	}
	
	/**
	 * (删除位置信息时)修改位置对应的设备的位置字段为空
	 * @param locationCode
	 * @param enterpriseCode
	 */
	public void updateEquForDelete(String locationCode,String enterpriseCode)
	{
		EquCEquipmentsFacadeRemote	equremote=(EquCEquipmentsFacadeRemote) factory.getFacadeRemote("EquCEquipmentsFacade");
		equremote.deleteLocationCode(locationCode, enterpriseCode);
	
	}
	
//	public void findEquRLocList()
//	{
//		//没用
//		String locationCode="";
//		Object myobj=request.getParameter("code");
//		if(myobj!=null)
//		{
//			locationCode=myobj.toString();
//		}
//		locationCode="+"+locationCode.substring(1,locationCode.length());
//		
//	    String enterpriseCode="sun_ustc";
//	    int start = Integer.parseInt(request.getParameter("start"));
//		int limit = Integer.parseInt(request.getParameter("limit"));
//		EquRLocationFacadeRemote rlocRemote=(EquRLocationFacadeRemote) factory.getFacadeRemote("EquRLocationFacade");
//		PageObject obj=rlocRemote.findEquRLocList(locationCode, enterpriseCode, start,limit);
//		if(obj.getList()!=null)
//		{
//		Iterator it=obj.getList().iterator();
//		StringBuffer JSONStr = new StringBuffer(); 
//		JSONStr.append("{list:[");
//		while(it.hasNext())
//		{
//			Object[] data=(Object[])it.next();
//			//id,equ_name,attribute_code,installation_code,assetnum,location_code
//			if(data[0]!=null)
//			{
//				JSONStr.append("{id:'"+data[0].toString()+"',");
//			}
//			else
//			{
//				JSONStr.append("{id:'',");
//			}
//			if(data[1]!=null)
//			{
//				JSONStr.append("equ_name:'"+data[1].toString()+"',");
//			}
//			else
//			{
//				JSONStr.append("equ_name:'',");
//			}
//			if(data[2]!=null)
//			{
//				JSONStr.append("attribute_code:'"+data[2].toString()+"',");
//			}
//			else
//			{
//				JSONStr.append("attribute_code:'',");
//			}
//			
//			if(data[3]!=null)
//			{
//				JSONStr.append("installation_code:'"+data[3].toString()+"',");
//			}
//			else
//			{
//				JSONStr.append("installation_code:'',");
//			}
//			
//			if(data[4]!=null)
//			{
//				JSONStr.append("assetnum:'"+data[4].toString()+"',");
//			}
//			else
//			{
//				JSONStr.append("assetnum:'',");
//			}
//			
//			if(data[5]!=null)
//			{
//				JSONStr.append("location_code:'"+data[5].toString()+"'},");
//			}
//			else
//			{
//				JSONStr.append("location_code:''},");
//			}
////			JSONStr.append("{id:'"+data[0]+"',equ_name:'"+data[1]+"',attribute_code:'"+data[2]+"',");
////			JSONStr.append("installation_code:'"+data[3]+"',assetnum:'"+data[4]+"',location_code:'"+data[5]+"'},");
//		
//		}
//		if (JSONStr.length() > 7) {
//			JSONStr.deleteCharAt(JSONStr.lastIndexOf(","));
//		}
//		JSONStr.append("],totalCount:'"+obj.getTotalCount()+"'}");
//		String str=JSONStr.toString();
//		write(str);
//		}
//		
//	}
	
	
	/**
	 * 增加设备与位置对应信息
	 */
	public void addEquRLocation()
	{
		EquCEquipmentsFacadeRemote	equremote=(EquCEquipmentsFacadeRemote) factory.getFacadeRemote("EquCEquipmentsFacade");
		 String codes= request.getParameter("codes");
		 String locationCode=request.getParameter("locationCode");
	     String [] equcodes= codes.split(",");
	     EquCEquipments model=new EquCEquipments();
		for(int i=0;i<equcodes.length;i++)
		{
			if(!equcodes[i].equals(""))
			{
				model=equremote.findByCode(equcodes[i], employee.getEnterpriseCode());
				if(model!=null)
				{
					model.setLocationCode(locationCode);
					equremote.update(model);
					
				}
			}
		}
		write("{success:true,msg:'增加成功！'}");
	}
	
	/**
	 * 删除设备与位置对应信息
	 */
	public void deleteEquRLocation()
	{
		EquCEquipmentsFacadeRemote	equremote=(EquCEquipmentsFacadeRemote) factory.getFacadeRemote("EquCEquipmentsFacade");
		 String ids= request.getParameter("ids");
		 String [] equids= ids.split(",");
		 EquCEquipments model=new EquCEquipments();
		 for(int i=0;i<equids.length;i++)
		 {
			 if(!equids[i].equals(""))
			 {
				 model=equremote.findById(Long.parseLong(equids[i]));
				 if(model!=null)
					{
					  model.setLocationCode("");
					  equremote.update(model);
					}
			 }
		 }
		 write("{success:true,msg:'删除成功！'}");
	}
	

	
	
	
}
