package power.web.hr.worktype;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import javax.naming.NamingException;
import power.web.comm.AbstractAction;
import power.ear.comm.ejb.Ejb3Factory;
import power.ear.comm.CodeRepeatException;
import power.ear.comm.LogUtil;
import power.ejb.comm.NativeSqlHelperRemote;
import power.ejb.hr.HrCTypeOfWork;
import power.ejb.hr.HrCTypeOfWorkFacade;
import power.ejb.hr.HrCTypeOfWorkFacadeRemote;
import com.googlecode.jsonplugin.JSONException;
import com.googlecode.jsonplugin.JSONUtil;
import power.web.comm.ListRange;

public class WorkTypeManageAction extends AbstractAction {

	/**
	 * 取得工种信息
	 * 
	 */
	private WorkTypeInfo worktypeinfo;
	private String worktypeids;
	private String method;
	
	public void getWorkTypeList() throws IOException, NamingException,
			JSONException {
		ListRange listwti = new ListRange();
		HrCTypeOfWorkFacadeRemote bll = (HrCTypeOfWorkFacadeRemote) Ejb3Factory
				.getInstance().getFacadeRemote("HrCTypeOfWorkFacade");
		List<HrCTypeOfWork> hrcworktype = bll.findAll();
		List<WorkTypeInfo> lwti = new ArrayList<WorkTypeInfo>();
		for (HrCTypeOfWork o : hrcworktype) {
			WorkTypeInfo di = new WorkTypeInfo();
			di.setTypeOfWorkId(o.getTypeOfWorkId());
			di.setTypeOfWorkName(o.getTypeOfWorkName());
			di.setTypeOfWorkType(changetotextb(o.getTypeOfWorkType()));
			di.setIsUse(changetotexta(o.getIsUse()));
			di.setRetrieveCode(o.getRetrieveCode());
			lwti.add(di);
		}
		System.out.println();
		listwti.setRoot(lwti);
		String jsonstr = JSONUtil.serialize(listwti);
		write(jsonstr);
	}

	private String changetotexta(String uxString) {
		String str = "不明";
		if ("U".equals(uxString)) {
			str = "开启";
		} else if ("N".equals(uxString)) {
			str = "禁用";
		} else if ("D".equals(uxString)) {
			str = "删除";
		} else if ("L".equals(uxString)) {
			str = "注销";
		} else {
			str = "不明";
		}
		return str;
	}

	private String changetotextb(String uxString) throws NamingException {
		String str = "不明";
		if (uxString.equals("1")) {
			str = "特殊工种";
		} else if (uxString.equals("2")) {
			str = "行业工种";
		} else if (uxString.equals("3")) {
			str = "社会工种";
		} else {
			str = "不明";
		}
		return str;
	}

	// public void getWorkTypetype() throws IOException, NamingException,
	// JSONException {
	// List<ListCombox> typecombox = new ArrayList<ListCombox>();
	// HrCEmpTypeFacadeRemote bll = (HrCEmpTypeFacadeRemote) Ejb3Factory
	// .getInstance().getFacadeRemote("HrCEmpTypeFacade");
	// List<HrCEmpType> typelist = bll.findAll();
	// if (typelist.size() > 0) {
	// for (HrCEmpType o : typelist) {
	// ListCombox lCombox = new ListCombox();
	// lCombox.setId(o.getEmpTypeId());
	// lCombox.setName(o.getEmpTypeName());
	// typecombox.add(lCombox);
	// }
	// }
	// String strlist = JSONUtil.serialize(typecombox);
	// write(strlist);
	// }

	public void addWorkType() throws Exception  {
		try{
			if("insert".equals(method))
			{
				HrCTypeOfWorkFacadeRemote dll = (HrCTypeOfWorkFacadeRemote) Ejb3Factory
						.getInstance().getFacadeRemote("HrCTypeOfWorkFacade");
				ListRange lRange = new ListRange();
				
				if (worktypeinfo != null) {
					HrCTypeOfWork worktype = new HrCTypeOfWork();
					worktype.setTypeOfWorkId(GetMaxRoleId());
					worktype.setTypeOfWorkName(worktypeinfo.getTypeOfWorkName());
					worktype.setTypeOfWorkType(worktypeinfo.getTypeOfWorkType());
					worktype.setIsUse(worktypeinfo.getIsUse());
					worktype.setRetrieveCode(HrCTypeOfWorkFacade.convertoString(worktypeinfo.getTypeOfWorkName()));
					dll.save(worktype);
				}
				lRange.setSuccess(true);
				lRange.setMessage("工种信息添加成功!");
				String jsonstr = JSONUtil.serialize(lRange);
				write(jsonstr);
			}
			else 
			{
				updataWorkType();
			}
		}catch (CodeRepeatException e) {
			write("{failure:true,errMsg:'" + e.getMessage() + "'}");
		}
		
	}

	public void getWorkType() throws Exception {
		HrCTypeOfWorkFacadeRemote dll = (HrCTypeOfWorkFacadeRemote) Ejb3Factory
				.getInstance().getFacadeRemote("HrCTypeOfWorkFacade");
		HrCTypeOfWork basemodel=new HrCTypeOfWork();
		basemodel=dll.findById(Long.parseLong(worktypeids));
		
		ListRange lRange = new ListRange();
		WorkTypeInfo model =new WorkTypeInfo();
		model.setTypeOfWorkId(basemodel.getTypeOfWorkId());
		model.setTypeOfWorkName(basemodel.getTypeOfWorkName());
		model.setTypeOfWorkType(basemodel.getTypeOfWorkType());
		model.setIsUse(basemodel.getIsUse());
		model.setRetrieveCode(basemodel.getRetrieveCode());
		List<WorkTypeInfo> lrole = new ArrayList<WorkTypeInfo>();
		lrole.add(model);
		lRange.setRoot(lrole);
		String jsonstr = JSONUtil.serialize(lRange);
		write(jsonstr);
	}
	
	public WorkTypeInfo getWorktypeinfo() {
		return worktypeinfo;
	}

	public void setWorktypeinfo(WorkTypeInfo worktypeinfo) {
		this.worktypeinfo = worktypeinfo;
	}

	public String getWorktypeids() {
		return worktypeids;
	}

	public void setWorktypeids(String worktypeids) {
		this.worktypeids = worktypeids;
	}
	
	public String getMethod() {
		return method;
	}

	public void setMethod(String method) {
		this.method = method;
	}

	// 取最大ID
	private long GetMaxRoleId() {
		long maxid = 0;
		String sqlstr = String
				.format("select nvl(max(TYPE_OF_WORK_ID)+1,1) from hr_c_type_of_work");
		try {
			NativeSqlHelperRemote bll = (NativeSqlHelperRemote) Ejb3Factory
					.getInstance().getFacadeRemote("NativeSqlHelper");
			Object ob = bll.getSingal(sqlstr);
			maxid = Long.parseLong(ob.toString());

		} catch (NumberFormatException e) {
			LogUtil.log("hr_c_type_of_work err", Level.INFO, e);

		} catch (Exception e) {
			LogUtil.log("hr_c_type_of_work err", Level.INFO, e);
		}
		return maxid;
	}

	public void updataWorkType() throws Exception {
		HrCTypeOfWorkFacadeRemote bll = (HrCTypeOfWorkFacadeRemote) Ejb3Factory
				.getInstance().getFacadeRemote("HrCTypeOfWorkFacade");
		ListRange lRange = new ListRange();
		try {
			HrCTypeOfWork model = bll.findById(worktypeinfo.getTypeOfWorkId());
			model.setTypeOfWorkName(worktypeinfo.getTypeOfWorkName());
			model.setTypeOfWorkType(worktypeinfo.getTypeOfWorkType());
			model.setIsUse(worktypeinfo.getIsUse());
			model.setRetrieveCode(worktypeinfo.getRetrieveCode());
			bll.update(model);
			lRange.setSuccess(true);
			lRange.setMessage("工种信息修改成功!");
			String jsonstr = JSONUtil.serialize(lRange);
			write(jsonstr);
		}  catch (CodeRepeatException e) {
			write("{failure:true,errMsg:'" + e.getMessage() + "'}");
		}
	}

	public void deleteWorkType() throws Exception {
//		HrCTypeOfWorkFacadeRemote bll = (HrCTypeOfWorkFacadeRemote) Ejb3Factory
//				.getInstance().getFacadeRemote("HrCTypeOfWorkFacade");
//		ListRange lRange = new ListRange();
//		if (this.worktypeids != null && worktypeids.length() > 0) {
//			String[] roleidArray = worktypeids.split(",");
//			for (String idCode : roleidArray) {
//				HrCTypeOfWork model = bll.findById(Long.parseLong(idCode));
//				bll.delete(model);
//			}
//		}
//		lRange.setSuccess(true);
//		lRange.setMessage("工种信息删除成功!");
//		String jsonstr = JSONUtil.serialize(lRange);
//		write(jsonstr);
	}

	/**
	 * add by liuyi 091117
	 * @throws Exception
	 */
	public void getTypeOfWorkList() throws Exception{
		HrCTypeOfWorkFacadeRemote bll = (HrCTypeOfWorkFacadeRemote) Ejb3Factory
		             .getInstance().getFacadeRemote("HrCTypeOfWorkFacade");
		List<HrCTypeOfWork> list = bll.getTypeOfWorkList();
		String str = "[";
		int i = 0;
		for (HrCTypeOfWork model : list) {
			i++;
			str += "[\"" + model.getTypeOfWorkName() + "\",\""
					+ model.getTypeOfWorkId() + "\"]";
			if (i < list.size()) {
				str += ",";
			}
		}
		str += "]";
		write(str);
	}
}