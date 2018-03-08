package power.web.equ.base.action;

import com.googlecode.jsonplugin.JSONException;
import com.googlecode.jsonplugin.JSONUtil;

import power.ear.comm.ejb.PageObject;
import power.ejb.equ.base.EquCBlock;
import power.ejb.equ.base.EquCBlockFacadeRemote;
import power.web.comm.AbstractAction;

public class EquBlockAction extends AbstractAction{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	protected EquCBlockFacadeRemote remote;
	private String id;
	private EquCBlock equcblock;
	
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public EquCBlock getEqucblock() {
		return equcblock;
	}
	public void setEqucblock(EquCBlock equcblock) {
		this.equcblock = equcblock;
	}
	
	/**
	 * 构造函数
	 */
	public EquBlockAction() 
	{
		remote=(EquCBlockFacadeRemote) factory.getFacadeRemote("EquCBlockFacade");
	}
	
	/**
	 * 根据id获取机组信息
	 */
	public void findBlockInfo() throws JSONException
	{
		EquCBlock model=remote.findById(Long.parseLong(id));
		String str=JSONUtil.serialize(model);
		 write(str);
		
	}
	
	/**
	 * 获取机组信息列表
	 */
	public void findBlockList() throws JSONException
	{
		String fuzzy="";
		Object myobj=request.getParameter("fuzzy");
		if(myobj!=null)
		{
			fuzzy=myobj.toString();
		}
	    String enterpriseCode=employee.getEnterpriseCode();
	    Object objstart=request.getParameter("start");
	    Object objlimit=request.getParameter("limit");
	    PageObject obj=new  PageObject();
	    if(objstart!=null&&objlimit!=null)
	    {
	        int start = Integer.parseInt(request.getParameter("start"));
			int limit = Integer.parseInt(request.getParameter("limit"));
			 obj=remote.findEquList(fuzzy, enterpriseCode,start,limit);
	    }
	    else
	    {
	    	obj=remote.findEquList(fuzzy, enterpriseCode);
	    }
	
//		 List<EquCBlock> list=obj.getList();
//		 Long count=obj.getTotalCount();
		 String str=JSONUtil.serialize(obj);
		write(str);
	}
	
	/**
	 * 增加或修改机组信息
	 */
	public void addOrUpdateBlock()
	{
		 String method=request.getParameter("method");
		if(method.equals("update"))
		{
			updateBlock();
		}
		if(method.equals("add"))
		{
			addBlock();
		}
	}
	
	
	/**
	 * 修改机组信息
	 */
		
	public void updateBlock()
	{
		EquCBlock model=new EquCBlock();
		model=remote.findById(Long.parseLong(id));
		if(!remote.CheckBlockCodeSame(model.getEnterpriseCode(), equcblock.getBlockCode(),model.getId() ))
		{
		    model.setBlockCode(equcblock.getBlockCode());
		    model.setBlockName(equcblock.getBlockName());
		    remote.update(model);
		    write("{success:true,msg:'修改成功！'}");
		}
		else
		{
			write("{success:true,msg:'编码存在，修改失败！'}");
		}
	}
	
	/**
	 * 增加机组信息
	 */
	public void addBlock()
	{
		equcblock.setEnterpriseCode(employee.getEnterpriseCode());
		int i= remote.save(equcblock);
		if(i != -1)
		{
			write("{success:true,msg:'增加成功！'}");
		}
		else
		{
			write("{success:true,msg:'增加失败:编码重复！'}");
		}
	}
	
	/**
	 *删除机组信息
	 */
	public void deleteBlock()
	{
		
		 EquCBlock model=new EquCBlock();
		 String ids= request.getParameter("ids");
	     String [] blockids= ids.split(",");
			for(int i=0;i<blockids.length;i++)
			{
				if(!blockids[i].equals(""))
				{
				   Long blockid=Long.parseLong(blockids[i]);
				
					model=remote.findById(blockid);
					model.setIsUse("N");
					remote.update(model);
				}
			}
		String	str = "{success: true,msg:\'ok\'}";
	    write(str);
	}

	


}
