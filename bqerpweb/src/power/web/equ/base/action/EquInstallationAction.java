package power.web.equ.base.action;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import com.googlecode.jsonplugin.JSONException;
import com.googlecode.jsonplugin.JSONUtil;

import power.ejb.comm.TreeNode;
import power.ejb.equ.base.EquCEquipments;
import power.ejb.equ.base.EquCEquipmentsFacadeRemote;
import power.ejb.equ.base.EquCInstallation;
import power.ejb.equ.base.EquCInstallationFacadeRemote;
import power.ejb.hr.HrJEmpInfo;
import power.ejb.hr.HrJEmpInfoFacadeRemote;
import power.web.comm.AbstractAction;

public class EquInstallationAction extends AbstractAction{

	private EquCInstallationFacadeRemote remote;
	private EquCInstallation equInstall;
	private String installId;
	private String equNum;
	private String StartSysCode;
	private String classStructureId;
	public String getClassStructureId() {
		return classStructureId;
	}
	public void setClassStructureId(String classStructureId) {
		this.classStructureId = classStructureId;
	}
	public EquCInstallation getEquInstall() {
		return equInstall;
	}
	public void setEquInstall(EquCInstallation equInstall) {
		this.equInstall = equInstall;
	}
	public String getInstallId() {
		return installId;
	}
	public void setInstallId(String installId) {
		this.installId = installId;
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
	public EquInstallationAction()
	{
		remote=(EquCInstallationFacadeRemote)factory.getFacadeRemote("EquCInstallationFacade");
	}
	
	
	/**
	 * 获得设备安装点树
	 * @throws JSONException 
	 */
	public void getInstallTreeList() throws JSONException
	{
		String code="";
		code=request.getParameter("id");
		if(!code.equals("root"))
		{
			code="+"+code.substring(1,code.length());
		}
		List<TreeNode> arrayList=new ArrayList();
		List<EquCInstallation> list=remote.getListByParent(code,employee.getEnterpriseCode());
		if(list!=null)
	    {
	         for (int i = 0; i < list.size(); i++)
	         {
	        	 EquCInstallation model=list.get(i);
	        	 TreeNode node=new TreeNode();
	        	 boolean isLeaf=!remote.ifHasChild(model.getInstallationCode(), employee.getEnterpriseCode());
	        	node.setId(model.getInstallationCode());
	        	node.setText(model.getInstallationCode()+" "+model.getInstallationDesc());
	        	node.setLeaf(isLeaf);
	        	 arrayList.add(node);
	        	 // boolean isLeaf = (model.getInstallationCode().length()>8) ? true : false;
	        	// JSONStr.append("{id:'"+model.getInstallationCode()+"',text:'"+model.getInstallationCode()+" "+model.getInstallationDesc()+"',leaf:"+isLeaf+"},");
	         }
	    
	    }
		write(JSONUtil.serialize(arrayList));
	
	}
	
	/**
	 * 通过安装点码获得一条设备安装点信息
	 * @throws JSONException
	 */
	public void getInstallByCode() throws JSONException
	{
		HrJEmpInfoFacadeRemote empRemote=(HrJEmpInfoFacadeRemote)factory.getFacadeRemote("HrJEmpInfoFacade");
		String code="";
		code=request.getParameter("id");
		EquCInstallation model=remote.findByCode(code, employee.getEnterpriseCode());
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
	 * 增加或修改一条安装点信息
	 * @throws ParseException
	 */
	public void addOrUpdateInstallCode() throws ParseException
	{
		String methode=request.getParameter("method");
		if(methode.equals("add"))
		{
			addInstalCode();
		}
		if(methode.equals("update"))
		{
			updateInstallCode();
		}
	}
	
	/**
	 * 增加一条安装点信息
	 * @throws ParseException
	 */
	public void addInstalCode() throws ParseException
	{
		if(equInstall.getInstallationCode()==null)
		{
			equInstall.setInstallationCode(equInstall.getFatherCode()+equNum);
		}
		else
		{
			if(equInstall.getFatherCode().length()==2)
			{
				equInstall.setInstallationCode(StartSysCode+equInstall.getInstallationCode());
			}
			if(!equNum.equals(""))
			{
				equInstall.setInstallationCode(equInstall.getInstallationCode()+equNum);
			}
		
			if(!equInstall.getFatherCode().equals("root"))
			{
				
				if(equInstall.getFatherCode().length()==8)
				{
					equInstall.setInstallationCode(equInstall.getFatherCode()+"·"+equInstall.getInstallationCode());
				}
				else
				{
				equInstall.setInstallationCode(equInstall.getFatherCode()+equInstall.getInstallationCode());
				}
			}
			
			
			
			
		}
		equInstall.setEnterpriseCode(employee.getEnterpriseCode());
		SimpleDateFormat sf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	    String mydate = sf.format(new java.util.Date());
	    equInstall.setChangeDate(sf.parse(mydate));
	    equInstall.setChangeBy("999999");
	    int i=remote.save(equInstall);
		if(i != -1)
		{
			write("{success:true,id:'"+equInstall.getInstallationCode()+"',msg:'增加成功！'}");
		}
		else
		{
			write("{success:true,id:'-1',msg:'增加失败:编码重复！'}");
		}
	}
	
	/**
	 * 修改一条安装点信息
	 */
	public void updateInstallCode()
	{
		EquCInstallation model=remote.findById(Long.parseLong(installId));
		model.setChangeBy(employee.getWorkerCode());
		model.setChangeDate(new java.util.Date());
		model.setInstallationDesc(equInstall.getInstallationDesc());
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
	 * 通过安装点码或id删除一条安装点信息
	 */
	public void deleteInstallCode()
	{
		String method=request.getParameter("method");
		 if(method.equals("code"))
		 {
			 deleteInstallByCode();
		 }
		 if(method.equals("id"))
		 {
			 deleteInstallById();
		 }
	}
	
	/**
	 * 通过安装点码删除安装点信息
	 */
	public void deleteInstallByCode()
	{
		String code="";
		code=request.getParameter("id");
		EquCInstallation model=remote.findByCode(code, employee.getEnterpriseCode());
		if(remote.delete(model.getId()))
		{
			this.updateEquForDelete(model.getInstallationCode(), employee.getEnterpriseCode());
			 write("{success:true,msg:'删除成功！'}");
		}
		else
		{
			write("{success:true,msg:'有子节点存在，不能删除！'}");
		}
	}
	
	/**
	 * 通过id删除一条安装点信息
	 */
	public void deleteInstallById()
	{
		EquCInstallation model=remote.findById(Long.parseLong(installId));
		if(remote.delete(Long.parseLong(installId)))
		{
			this.updateEquForDelete(model.getInstallationCode(), employee.getEnterpriseCode());
			 write("{success:true,msg:'删除成功！'}");
		}
		else
		{
			write("{success:true,msg:'有子节点存在，不能删除！'}");
		}
		
	}
	
	/**
	 * 批量增加设备与安装点对应信息（即修改设备表的安装点字段为installCode）
	 */
	public void addEquRInstall()
	{
		EquCEquipmentsFacadeRemote	equremote=(EquCEquipmentsFacadeRemote) factory.getFacadeRemote("EquCEquipmentsFacade");
		 String codes= request.getParameter("codes");
		 String installCode=request.getParameter("installCode");
	     String [] equcodes= codes.split(",");
	     EquCEquipments model=new EquCEquipments();
	     for(int i=0;i<equcodes.length;i++)
			{
				if(!equcodes[i].equals(""))
				{
					model=equremote.findByCode(equcodes[i], employee.getEnterpriseCode());
					if(model!=null)
					{
						model.setInstallationCode(installCode);
						equremote.update(model);
						
					}
				}
			}
	 	write("{success:true,msg:'增加成功！'}");
	}
	
	/**
	 * 批量删除设备与安装点对应信息（即修改设备表的安装点字段为空）
	 */
	public void deleteEquRInstall()
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
						model.setInstallationCode("");
						equremote.update(model);
						
					}
				}
			}
	     write("{success:true,msg:'删除成功！'}");
	}
	
	
	/**
	 * （删除一条安装点信息时）修改安装点对应的设备的安装点字段为空
	 * @param installCode
	 * @param enterpriseCode
	 */
	public void updateEquForDelete(String installCode,String enterpriseCode)
	{
		EquCEquipmentsFacadeRemote	equremote=(EquCEquipmentsFacadeRemote) factory.getFacadeRemote("EquCEquipmentsFacade");
		equremote.deleteInstallationCode(installCode, enterpriseCode);
	
	}
	
	
	
	
	
	
	
}
