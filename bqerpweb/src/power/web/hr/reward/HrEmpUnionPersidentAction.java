package power.web.hr.reward;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import com.googlecode.jsonplugin.JSONException;
import com.googlecode.jsonplugin.JSONUtil;
import power.ear.comm.ejb.PageObject;
import power.ejb.hr.HrJEmpInfoFacadeRemote;
import power.ejb.hr.reward.HrCUnionPresident;
import power.ejb.hr.reward.HrCUnionPresidentFacadeRemote;
import power.web.comm.AbstractAction;

public class  HrEmpUnionPersidentAction   extends AbstractAction 
{
	
	private HrJEmpInfoFacadeRemote  remote;
	private HrCUnionPresidentFacadeRemote  remoteA;
	public HrEmpUnionPersidentAction()
	{
		remote = (HrJEmpInfoFacadeRemote) factory.getFacadeRemote("HrJEmpInfoFacade");
		remoteA = (HrCUnionPresidentFacadeRemote) factory.getFacadeRemote("HrCUnionPresidentFacade");
	}
	//---------------分工会主席维护--------------------------start---------
	public void getChairmanList() throws JSONException
	{
		PageObject obj=new PageObject();
		String empName=request.getParameter("name");
		obj=remote.getChairmanList(empName);
		if(obj!=null)
		{
			write(JSONUtil.serialize(obj));
		}else
		{
			write("{totalCount : 0,list :[]}");
		}
		 
	}
	public void delChairmanList()
	{
		String ids=request.getParameter("ids");
		remote.delUnionPersident(ids);
		write("{success:true,msg:'删除成功！'}");
		
	}
	public void  saveChairmanList() throws JSONException
	{
		String modifyIds=request.getParameter("modifyIds");
	
			remote.saveUnionPersideng(modifyIds);		
			write("{success:true,msg:'保存成功！'}");

		}
	//---------------工会主席标准维护----------------start-----------------------
	public void findUnionPerStandard() throws JSONException
	{
		PageObject  obj=new PageObject();
		obj=	remoteA.getUnionPresident();
		if (obj != null) {

			write(JSONUtil.serialize(obj));
		} else {
			write("{totalCount : 0,list :[]}");
		}
		
	}
	public void delUnionPerStandard()
	{
		String ids=request.getParameter("ids");
		remoteA.delUnionPresident(ids);
		write("{success:true,msg:'删除成功！'}");
		
		
	}
	public void getMaxEndtime()
	{
		String time=remoteA.getMaxEndTime();
		write(time);
		
	}
	public void saveUnionPerStandard() throws java.text.ParseException, JSONException
	{
		
		String str = request.getParameter("isUpdate");
		Object object = JSONUtil.deserialize(str);
		List<Map> list = (List<Map>) object;
		List<HrCUnionPresident> addList = null;
		List<HrCUnionPresident> updateList = null;
		if (list != null && list.size() > 0) {
			addList = new ArrayList<HrCUnionPresident>();
			updateList = new ArrayList<HrCUnionPresident>();
			for (Map data : list) {
				
				String unionPerId = null;
				String unionPerStandard = null;
				String effectStartTime = null;
				String effectEndTime = null;
				String memo = null;
				

				if (data.get("unionPerId") != null&&!data.get("unionPerId").equals("") )
					unionPerId = data.get("unionPerId").toString();
				if (data.get("unionPerStandard") != null&&!data.get("unionPerStandard").equals(""))
					unionPerStandard = data.get("unionPerStandard").toString();

				if (data.get("effectStartTime") != null&&!data.get("effectStartTime").equals(""))
					effectStartTime = data.get("effectStartTime").toString();
				if (data.get("effectEndTime") != null&&!data.get("effectEndTime").equals(""))
					effectEndTime = data.get("effectEndTime").toString();
				if (data.get("memo") != null&&!data.get("memo").equals(""))
					memo = data.get("memo").toString();
			
				
				HrCUnionPresident model = new HrCUnionPresident();
				if (unionPerId == null) {
					if(unionPerStandard!=null)
					{
					model.setUnionPerStandard(Double.parseDouble(unionPerStandard));
					}
					if (effectStartTime != null )
					{
						DateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
						model.setEffectStartTime(sdf.parse(effectStartTime));
				     }
					if (effectEndTime != null)
					{
						DateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
						model.setEffectEndTime(sdf.parse(effectEndTime));
					}
					if (memo != null)
						model.setMemo(memo);

					model.setEnterpriseCode(employee.getEnterpriseCode());
					addList.add(model);
				} else {
					model = remoteA.findById(Long.parseLong(unionPerId));
					if(unionPerStandard!=null)
					{
					model.setUnionPerStandard(Double.parseDouble(unionPerStandard));
					}
					if (effectStartTime != null )
					{
						DateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
						model.setEffectStartTime(sdf.parse(effectStartTime));
				     }
					if (effectEndTime != null)
					{
						DateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
						model.setEffectEndTime(sdf.parse(effectEndTime));
					}
					if (memo != null)
						model.setMemo(memo);

					model.setEnterpriseCode(employee.getEnterpriseCode());
					updateList.add(model);
				}
			}
		}
		
			remoteA.saveUnionPresident(addList, updateList);
			write("{success:true,msg:'操作成功！'}");
		

		
		
	}
	
	
}
