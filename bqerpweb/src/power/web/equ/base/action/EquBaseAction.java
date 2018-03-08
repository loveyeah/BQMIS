package power.web.equ.base.action;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.naming.NamingException;

import com.googlecode.jsonplugin.JSONException;
import com.googlecode.jsonplugin.JSONUtil;

import power.ear.comm.ejb.Ejb3Factory;
import power.ear.comm.ejb.PageObject;
import power.ejb.comm.TreeNode;
import power.ejb.equ.base.EquCEquipments;
import power.ejb.equ.base.EquCEquipmentsFacadeRemote;
import power.ejb.equ.base.EquCInstallation;
import power.ejb.equ.base.EquCInstallationFacadeRemote;
import power.ejb.equ.base.EquCLocation;
import power.ejb.equ.base.EquCLocationFacadeRemote;
import power.ejb.hr.HrCDept;
import power.ejb.hr.HrCDeptFacadeRemote;
import power.ejb.run.runlog.RunCSpecials;
import power.ejb.run.runlog.RunCSpecialsFacadeRemote;
import power.web.comm.AbstractAction;

public class EquBaseAction extends AbstractAction{
	protected EquCEquipmentsFacadeRemote remote;
	private EquCEquipments equ;
	private String equId;
	private String equNum;
	private String StartSysCode;
	public EquCEquipments getEqu() {
		return equ;
	}

	public void setEqu(EquCEquipments equ) {
		this.equ = equ;
	}
	public String getEquId() {
		return equId;
	}

	public void setEquId(String equId) {
		this.equId = equId;
	}
	public String getEquNum() {
		return equNum;
	}

	public void setEquNum(String equNum) {
		this.equNum = equNum;
	}
	
	public String getStartSysCode() {
		return StartSysCode;
	}

	public void setStartSysCode(String startSysCode) {
		StartSysCode = startSysCode;
	}

	
	/**
	 * 构造函数
	 */
	public EquBaseAction()
	{
		remote=(EquCEquipmentsFacadeRemote) factory.getFacadeRemote("EquCEquipmentsFacade");
	}
	
	/**
	 * 获得设备功能码树(维护时)
	 * @throws JSONException 
	 */
	public void getTreeListByParent() throws JSONException
	{
		String code="";
		code=request.getParameter("id");
		List<TreeNode> arrayList=new ArrayList();
	    List<EquCEquipments> list=	remote.getListByParent(code, employee.getEnterpriseCode());
	    if(list!=null)
	    {
	         for (int i = 0; i < list.size(); i++)
	         {
	    	     EquCEquipments equ= list.get(i);
	    	     TreeNode model=new TreeNode();
	    	     boolean isLeaf =!remote.IfHasChild(equ.getAttributeCode(), employee.getEnterpriseCode());
	    	     model.setId(equ.getAttributeCode());
	    	     model.setText(equ.getAttributeCode()+" "+equ.getEquName());
	    	     model.setLeaf(isLeaf);
	    	     arrayList.add(model);
	    	     
	    	     //boolean isLeaf = (equ.getAttributeCode().length()==16) ? true : false;
	    	    
//	    	     JSONStr.append("{id:'"+equ.getAttributeCode()+"',text:'"+equ.getAttributeCode()+" "+equ.getEquName()+"',leaf:"+isLeaf+"},");
	         }

	         
	    }
	    write(JSONUtil.serialize(arrayList));

	}
	
	/**
	 * 获得设备功能码树（用于选择页面：checkbox是否存在）
	 */
	public void getTreeForSelect()
	{
		String code="";
		String method="";
		code=request.getParameter("id");
		Object myobj=request.getParameter("method");
		if(myobj!=null)
		{
			method=myobj.toString();
		}
	
		StringBuffer JSONStr = new StringBuffer(); 
		JSONStr.append("[");
	    List<EquCEquipments> list=	remote.getListByParent(code, employee.getEnterpriseCode());
	    if(list!=null)
	    {
	         for (int i = 0; i < list.size(); i++)
	         {
	    	     EquCEquipments equ= list.get(i);
	    	    // boolean isLeaf = (equ.getAttributeCode().length()==16) ? true : false;
	    	     boolean isLeaf =!remote.IfHasChild(equ.getAttributeCode(), employee.getEnterpriseCode());
	    	     if(method.equals("many"))
	    	     {
	    	    //选择多项时树带checkbox
	    	     JSONStr.append("{id:'"+equ.getAttributeCode()+"',text:'"+equ.getAttributeCode()+" "+equ.getEquName()+"',leaf:"+isLeaf+",checked:false},");
	         
	    	     }
	    	     else
	    	     {
	    	    	 JSONStr.append("{id:'"+equ.getAttributeCode()+"',text:'"+equ.getAttributeCode()+" "+equ.getEquName()+"',leaf:"+isLeaf+"},");
	    	     }
	         }
	     	if (JSONStr.length() > 1) {
				JSONStr.deleteCharAt(JSONStr.lastIndexOf(","));
			}
	         
	    }
	 //  String str=" [{id:'1',text:'设备',leaf:false}]";
	   JSONStr.append("]");
	    write(JSONStr.toString());
	}
	
	/**
	 * 通过功能码获得一条设备信息
	 * @throws JSONException
	 */
	public void getInfoByCode() throws JSONException
	{
		String code="";
	
		code=request.getParameter("id");
		 equ=new EquCEquipments();
		 equ=remote.findByCode(code, employee.getEnterpriseCode());
		 String str=JSONUtil.serialize(equ);
			String locName="";
			String installName="";
		 if(equ.getLocationCode()!=null)
		 {
			 locName=equ.getLocationCode();
			 EquCLocationFacadeRemote locRemote=(EquCLocationFacadeRemote) factory.getFacadeRemote("EquCLocationFacade");
			 EquCLocation locModel=new EquCLocation();
		     locModel= locRemote.findByCode(equ.getLocationCode(), equ.getEnterpriseCode());
		     if(locModel!=null)
		     {
			  locName=locModel.getLocationDesc();
		     }
		 }
		 if(equ.getInstallationCode()!=null)
		 {
			 installName=equ.getInstallationCode();
			 EquCInstallationFacadeRemote installRemote=(EquCInstallationFacadeRemote)factory.getFacadeRemote("EquCInstallationFacade");
			 EquCInstallation installModel=new EquCInstallation();
		      installModel=installRemote.findByCode(equ.getInstallationCode(), equ.getEnterpriseCode());
		      if(installModel!=null)
		      {
			    installName=installModel.getInstallationDesc();
		       }
		 }
		 
		 write("{success: true,locName:'"+locName+"',installName:'"+installName+"', data:"+str+"}");
		 
		 
		 
	
	}
	
	/**
	 * 增加或修改设备信息
	 */
	
	public void addOrUpdateAttributeCode()
	{
		String methode=request.getParameter("method");
		if(methode.equals("add"))
		{
			addAttributeCode();
		}
		else
		{
			updateAttributeCode();
		}
		
	}
	
	/**
	 * 增加设备信息
	 */
	public void addAttributeCode()
	{
		if(equ.getPAttributeCode().equals("root"))
		{
			equ.setAttributeCode(equ.getAttributeCode()+StartSysCode) ;
		}
		if(!equNum.equals(""))
		{
	     equ.setAttributeCode(equ.getAttributeCode()+equNum);
		}
		if(!equ.getPAttributeCode().equals("root"))
		{
			if(equ.getPAttributeCode().length()==4)
			{
				equ.setAttributeCode(equ.getPAttributeCode().substring(0,2)+equ.getAttributeCode().trim());
				
			}
			else
			{
			equ.setAttributeCode(equ.getPAttributeCode()+equ.getAttributeCode().trim());
			}
		}
		if(equ.getPAttributeCode().length()!=4)
		{
			equ.setPAttributeCode("");
		}
		
		equ.setEnterpriseCode(employee.getEnterpriseCode());
		int i= remote.save(equ);
		if(i != -1)
		{
			write("{success:true,id:'"+equ.getAttributeCode()+"',msg:'增加成功！'}");
		}
		else
		{
			write("{success:true,id:'-1',msg:'增加失败:编码重复！'}");
		}
		
	}
	
	/**
	 * 修改设备信息
	 */
	public void updateAttributeCode()
	{
		
		EquCEquipments model=new EquCEquipments();
		model=remote.findById(Long.parseLong(equId));
		model.setEquName(equ.getEquName());
		model.setLocationCode(equ.getLocationCode());
		model.setInstallationCode(equ.getInstallationCode());
		model.setBugCode(equ.getBugCode());
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
	 * 通过设备功能码或id删除设备信息
	 */
	public void deleteAttributeCode()
	{
		String methode=request.getParameter("method");
		if(methode.equals("id"))
		{
			deleteAttributeCodeById();
		}
		if(methode.equals("code"))
		{
			deleteAttributeBycode();
		}
		
		
	}
	
	/**
	 * 根据设备id删除设备信息
	 */
	public void deleteAttributeCodeById()
	{
		EquCEquipments model=new EquCEquipments();
		model=remote.findById(Long.parseLong(equId));
		model.setIsUse("N");
		remote.update(model);
		 write("{success:true,msg:'删除成功！'}");
	}
	
	/**
	 * 根据设备功能码删除设备信息
	 */
	public void deleteAttributeBycode()
	{
		EquCEquipments model=new EquCEquipments();
		String code=request.getParameter("id");
		model=remote.findByCode(code, employee.getEnterpriseCode());
		model.setIsUse("N");
		remote.update(model);
	    write("{success:true,msg:'删除成功！'}");
	}
	
	
	/**
	 * 模糊查询设备信息列表（通过设备功能码或设备名称）
	 * fuzzy 设备功能码或设备名称
	 * @throws JSONException
	 */
	public void findEquListByFuzzy() throws JSONException
	{
		String fuzzy="";
		Object myobj=request.getParameter("fuzzy");
		if(myobj!=null)
		{
			fuzzy=myobj.toString();
		}
	    String enterpriseCode=employee.getEnterpriseCode();
	    int start = Integer.parseInt(request.getParameter("start"));
		int limit = Integer.parseInt(request.getParameter("limit"));
		PageObject obj=remote.findListByNameOrCode(fuzzy, enterpriseCode, start,limit);
		String str=JSONUtil.serialize(obj);
		write(str);
	}
	
	/**
	 * 通过位置码获得设备信息列表（即获得某个位置对应的设备列表）
	 * @throws JSONException
	 */
	public void  findListByLocationCode() throws JSONException
	{
		String code="";
		Object myobj=request.getParameter("code");
		if(myobj!=null)
		{
			code=myobj.toString();
		}
	    String enterpriseCode=employee.getEnterpriseCode();
	    int start = Integer.parseInt(request.getParameter("start"));
		int limit = Integer.parseInt(request.getParameter("limit"));
		PageObject obj=remote.findListByLocationCode(code, enterpriseCode, start,limit);
		String str=JSONUtil.serialize(obj);
		write(str);
	}
	
	/**
	 * 通过安装点码获得设备信息列表（即获得某个安装点对应的设备列表）
	 * @throws JSONException
	 */
	public void findListByInstallationCode() throws JSONException
	{
		String code="";
		Object myobj=request.getParameter("code");
		if(myobj!=null)
		{
			code=myobj.toString();
		}
	    String enterpriseCode=employee.getEnterpriseCode();
	    int start = Integer.parseInt(request.getParameter("start"));
		int limit = Integer.parseInt(request.getParameter("limit"));
		PageObject obj=remote.findListByInstallCode(code, enterpriseCode, start, limit);
		String str=JSONUtil.serialize(obj);
		write(str);
	}
	
	//----------------设备基本信息填写----------------------------
	/**
	 * 获得专业列表
	 */
	public void findSpecialList() throws JSONException
	{
		RunCSpecialsFacadeRemote specialRemote=(RunCSpecialsFacadeRemote)factory.getFacadeRemote("RunCSpecialsFacade");
		List<RunCSpecials> list=specialRemote.findByType("1", employee.getEnterpriseCode());
		 String str=JSONUtil.serialize(list);
		write(str);
	
	}
	
	/**
	 * 获得部门树
	 * @throws Exception
	 */
	public void findDeptList() throws Exception
	{
		String pdeptId=request.getParameter("id");
		HrCDeptFacadeRemote deptRemote=(HrCDeptFacadeRemote)factory.getFacadeRemote("HrCDeptFacade");
		List<HrCDept> deptl=deptRemote.findByPdeptId(Long.parseLong(pdeptId));
		write(toDeptTreeJsonStr(deptl));
	}
	private boolean isLeafdept(Long pid) throws NamingException {
		HrCDeptFacadeRemote bll = (HrCDeptFacadeRemote) Ejb3Factory
				.getInstance().getFacadeRemote("HrCDeptFacade");
		List<HrCDept> ld = bll.findByPdeptId(pid);
		if (ld != null && ld.size() > 0)
			return false;
		return true;
	}
	private String toDeptTreeJsonStr(List<HrCDept> list) throws Exception{
		StringBuffer JSONStr = new StringBuffer(); 
		JSONStr.append("[");
		String icon="";
		for(int i=0;i<list.size();i++){
			HrCDept dept=list.get(i);
			if(isLeafdept(dept.getDeptId())){
				icon="file";
			}else{
				icon="folder";
			}
			JSONStr.append("{\"text\":\"" + dept.getDeptName()+ 
					"\",\"id\":\"" + dept.getDeptId() + 
					"\",\"pcode\":\"" +dept.getPdeptCode()+
					"\",\"leaf\":" + isLeafdept(dept.getDeptId())+
					",\"cls\":\"" + icon+ "\"},");	
		}
		if (JSONStr.length() > 1) {
			JSONStr.deleteCharAt(JSONStr.lastIndexOf(","));
		}
		JSONStr.append("]"); 
		return JSONStr.toString();
	}
	
	public void getBaseInfoByCode() throws JSONException
	{
		String code=request.getParameter("code");
		EquCEquipments model=remote.findByCode(code, employee.getEnterpriseCode());
		String pEquName="";
		String deptName="";

		if(model!=null)
		{
			if(model.getPAttributeCode()!=null)
			{
				if(model.getPAttributeCode().equals("root"))
				{
					pEquName="设备树";
				}
				else
				{
					EquCEquipments pmodel=remote.findByCode(model.getPAttributeCode(), employee.getEnterpriseCode());
					if(pmodel!=null)
					{
						if(pmodel.getEquName()!=null)
						{
							pEquName=pmodel.getEquName();
						}
					}
				}
				
			}
			if(model.getBelongTeam()!=null)
			{
				HrCDeptFacadeRemote bll = (HrCDeptFacadeRemote) Ejb3Factory
				.getInstance().getFacadeRemote("HrCDeptFacade");
		            HrCDept deptModel = bll.findById(Long.parseLong(model.getBelongTeam()));
		            if(deptModel!=null)
		            {
		            	deptName=deptModel.getDeptName();
		            }
			}
		}
		String str=JSONUtil.serialize(model);
		 write("{success: true,pEquName:'"+pEquName+"',deptName:'"+deptName+"',data:"+str+"}");
	}
	
	public void saveEquBaseInfo()
	{
		EquCEquipments model=remote.findByCode(equ.getAttributeCode(), employee.getEnterpriseCode());
		model.setAssetnum(equ.getAssetnum());
		model.setAssetType(equ.getAssetType());
		model.setBelongProfession(equ.getBelongProfession());
		model.setBelongTeam(equ.getBelongTeam());
		model.setChangeBy(employee.getWorkerCode());
		model.setChangeDate(new java.util.Date());
		model.setChargeBy(equ.getChargeBy());
		model.setDesignlife(equ.getDesignlife());
		model.setDisabled(equ.getDisabled());//是否在用
		model.setInstalldate(equ.getInstalldate());
		model.setPriority(equ.getPriority());
		model.setPurchaseprice(equ.getPurchaseprice());
		model.setVendor(equ.getVendor());
		model.setWarrantyexpdate(equ.getWarrantyexpdate());
		model.setManufacturer(equ.getManufacturer());
		remote.update(model);
		write("{success:true,msg:'修改成功！'}");
	}
	
}
