package power.web.equ.base.action;

import com.googlecode.jsonplugin.JSONException;
import com.googlecode.jsonplugin.JSONUtil;

import power.ear.comm.ejb.PageObject;
import power.ejb.equ.base.EquCClass;
import power.ejb.equ.base.EquCClassFacadeRemote;
import power.web.comm.AbstractAction;

public class EquClassAction extends AbstractAction{
	private static final long serialVersionUID = 1L;
	protected EquCClassFacadeRemote remote;
	private String equClassId;
	private EquCClass equCclass;
	public String getEquClassId() {
		return equClassId;
	}
	public void setEquClassId(String equClassId) {
		this.equClassId = equClassId;
	}
	public EquCClass getEquCclass() {
		return equCclass;
	}
	public void setEquCclass(EquCClass equCclass) {
		this.equCclass = equCclass;
	}
	
	/**
	 * 构造函数
	 */
	public EquClassAction()
	{
		remote=(EquCClassFacadeRemote) factory.getFacadeRemote("EquCClassFacade");
	}
	
	/**
	 * 根据id获得（系统/设备/部件）编码信息
	 * @throws JSONException
	 */
	public void findClassInfo() throws JSONException
	{
		EquCClass model=remote.findById(Long.parseLong(equClassId));
		String str=JSONUtil.serialize(model);
		 write(str);
	}
	
	/**
	 * 查找系统码、设备码或部件码列表
	 * @throws JSONException
	 */
	public void findClassForSelect() throws JSONException
	{
		String method=request.getParameter("method");
		if(method.equals("sys"))
		{
			findSysListForSelect();
		}
		else if(method.equals("equ"))
		{
			findEquListForSelect();
		}
		else if(method.equals("part"))
		{
			findPartListForSelect();
		}
	}
	
	/**
	 * 获得系统编码列表（根据编码或名称模糊查询）
	 * @throws JSONException
	 */
	public void findSysCodeList() throws JSONException
	{
		findClassList("1");
	}
	
	
	/**
	 * 获得供选择的系统编码信息
	 * @throws JSONException
	 */
	public void findSysListForSelect() throws JSONException
	{
		String code=request.getParameter("code");
		String mycode="";
		int length=0;
		if(code.equals("1"))
		{
			//获得所有的一位系统编码
		  mycode="%";
		  length=1;
		}
		else if(code.equals("all"))
		{
			//获得所有的三位系统编码
			mycode="%";
			length=3;
		}
		else
		{
			//获得以code开头的三位系统编码
			mycode=code;
			length=3;
		}
		getClassListForSelect(mycode,"1",length);
	}
	
	/**
	 * 获得供选择的设备编码列表（根据编码或名称模糊查询）
	 * @throws JSONException
	 */
	public void findEquCodeList() throws JSONException 
	{
		findClassList("2");
	}
	
	/**
	 * 获得供选择的2位的所有设备编码
	 * @throws JSONException
	 */
	public void findEquListForSelect() throws JSONException 
	{
		getClassListForSelect("%","2",2);
	}
	
	/**
	 * 获得部件编码列表（根据编码或名称模糊查询）
	 * @throws JSONException
	 */
	public void findPartCodeList() throws JSONException
	{
		findClassList("3");
	}
	
	
	/**
	 * 获得供选择的2位的所有部件编码
	 * @throws JSONException
	 */
	public void findPartListForSelect() throws JSONException
	{
		getClassListForSelect("%","3",2);
	}
	
	
	
	/**
	 * 根据标识获得不同编码信息列表
	 * @param classLevel 1：系统，2：设备，3：部件
	 * @throws JSONException
	 */
	public void findClassList(String classLevel) throws JSONException
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
		 PageObject obj=remote.findClassList(fuzzy,classLevel, enterpriseCode,start,limit);
		 String str=JSONUtil.serialize(obj);
		write(str);
	}
	
	
	/**
	 * 通过编码或名称和编码位数获得设备系统码或设备码或部件码列表
	 * @param code
	 * @param classLevel
	 * @param length
	 * @throws JSONException
	 */
	public void getClassListForSelect(String code, String classLevel,int length) throws JSONException
	{
		String fuzzy="";
		Object myobj=request.getParameter("fuzzy");
		if(myobj!=null)
		{
			fuzzy=myobj.toString();
		}
	    int start = Integer.parseInt(request.getParameter("start"));
		int limit = Integer.parseInt(request.getParameter("limit"));
		PageObject obj=remote.getClassListForSelect(fuzzy, code, length, classLevel, employee.getEnterpriseCode(), start,limit);
		String str=JSONUtil.serialize(obj);
		write(str);
	}
	
	/**
	 * 增加或修改系统编码信息
	 */
	public void addOrUpdateSys()
	{
		addOrUpdateClass("1");
	}
	
	/**
	 * 增加或修改设备编码信息
	 */
	public void addOrUpdateEqu()
	{
		addOrUpdateClass("2");
	}
	
	/**
	 * 增加或修改部件编码信息
	 */
	public void addOrUpdatePart()
	{
		addOrUpdateClass("3");
	}
	
	/**
	 * 增加或修改编码信息
	 * @param classLevel
	 */
	public void addOrUpdateClass(String classLevel)
	{
		 String method=request.getParameter("method");
			if(method.equals("update"))
			{
				updateClass();
			}
			if(method.equals("add"))
			{
				addClass(classLevel);
			}
	}
	
	/**
	 * 修改编码信息
	 */
	public void updateClass()
	{
		EquCClass model=new EquCClass();
        model=remote.findById(Long.parseLong(equClassId));
        model.setClassCode(equCclass.getClassCode());
        model.setClassName(equCclass.getClassName());
        model.setRemark(equCclass.getRemark());
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
	 * 增加编码信息
	 * @param classLevel
	 */
	public void addClass(String classLevel)
	{
		equCclass.setEnterpriseCode(employee.getEnterpriseCode());
		equCclass.setClassLevel(classLevel);
	    int id=remote.save(equCclass);
	    if(id!=-1)
	    {
	    	write("{success:true,msg:'增加成功！'}");
	    }
	    else
	    {
	    	write("{success:true,msg:'增加失败:编码重复！'}");
	    }
	
	}
	
	/**
	 * 删除编码信息
	 */
	public void deleteClass()
	{
		String ids= request.getParameter("ids");
	     String [] classids= ids.split(",");
	     for(int i=0;i<classids.length;i++)
	     {
	    	 if(!classids[i].equals(""))
	    	 {
	    		 remote.delete(Long.parseLong(classids[i]));
	    	 }
	     }
	     String	str = "{success: true,msg:\'ok\'}";
		 write(str);
	}
	
	
	
}
