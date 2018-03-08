package power.web.manage.plan.action;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.googlecode.jsonplugin.JSONException;
import com.googlecode.jsonplugin.JSONUtil;

import power.ear.comm.ejb.PageObject;
import power.ejb.manage.plan.BpCPlanJobDept;
import power.ejb.manage.plan.BpCPlanJobDeptFacadeRemote;
import power.ejb.manage.plan.BpJPlanJobDepDetail;
import power.web.comm.AbstractAction;
@SuppressWarnings("serial")
public  class LevalOneDeptAction  extends AbstractAction
{
	private BpCPlanJobDeptFacadeRemote remote;
	public  LevalOneDeptAction()
	{
		remote = (BpCPlanJobDeptFacadeRemote) factory
		.getFacadeRemote("BpCPlanJobDeptFacade");
	}
	public void getLevelOneDept() throws JSONException
	{
		String deptName=request.getParameter("deptName");
		PageObject result=remote.getAllDept(deptName);
		write(JSONUtil.serialize(result));
	}
	public void saveJobDept() throws Exception {
		try {
			String str = request.getParameter("isUpdate");
			Object obj = JSONUtil.deserialize(str);

			List<BpCPlanJobDept> addList = new ArrayList<BpCPlanJobDept>();
			List<BpCPlanJobDept> updateList = new ArrayList<BpCPlanJobDept>();

			List<Map> list = (List<Map>) obj;
			String  ids="";
			for (Map data : list) {	
				String deptCode = null;
				String id=null;
				Long orderBy = null;
				if (data.get("id") != null&&!data.get("id").equals("")) {
					id = data.get("id").toString();
				}
			  if (data.get("deptCode") != null&&!data.get("deptCode").equals("")) {
					deptCode = data.get("deptCode").toString();
				}
				if (data.get("orderBy") != null&&!data.get("orderBy").equals("")) {
					orderBy =Long.parseLong(data.get("orderBy").toString());
				}
				BpCPlanJobDept model = new BpCPlanJobDept();

				// 增加
				if ((id == null)&&(orderBy!=null)) {
					model.setDeptCode(deptCode);
					model.setOrderBy(orderBy);
					addList.add(model);

				} else  if((id!=null)&&(orderBy==null))
					{
					   model = remote.findById(Long.parseLong(id));
					   ids=ids+id+",";
					}else if((id!=null)&&(orderBy!=null))
					
					{
						model = remote.findById(Long.parseLong(id));
					    model.setOrderBy(orderBy);
					    updateList.add(model);
					}else 
					{
						
					}
				

				}

			

			if (addList.size() > 0)
				remote.saveLevelOneDept(addList);

			if (updateList.size() > 0)
				remote.updateLevalOneDept(updateList);
            if(ids!=null&&!ids.equals(""))
            	remote.deleteDept(ids);
			
			write("{success:true,msg:'保存成功！'}");

		} catch (Exception exc) {
			write("{success:false,msg:'操作失败'}");
			throw exc;

		}
	}

	}
