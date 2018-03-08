/**
 * 
 */
package power.web.hr.assistantunits;

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
import power.ejb.hr.HrCAssistantManagerUnits;
import power.ejb.hr.HrCAssistantManagerUnitsFacadeRemote;

import com.googlecode.jsonplugin.JSONException;
import com.googlecode.jsonplugin.JSONUtil;
import power.web.comm.ListRange;

/**
 * @author Administrator
 * 
 */
public class AssistantUnitManageAction extends AbstractAction {
	private HrCAssistantManagerUnits unit;
	private String start;
	private String limit;

	// 取得协理单位列表
	public void getAssistantUnits() throws NamingException, IOException,
			JSONException {
		try {
			int[] obj = { Integer.parseInt(start), Integer.parseInt(limit) };
			HrCAssistantManagerUnitsFacadeRemote bll = (HrCAssistantManagerUnitsFacadeRemote) Ejb3Factory
					.getInstance().getFacadeRemote(
							"HrCAssistantManagerUnitsFacade");
			ListRange<HrCAssistantManagerUnits> list = new ListRange<HrCAssistantManagerUnits>();
			List<HrCAssistantManagerUnits> rec = bll.findByProperties(obj[0],
					obj[1]);
			if (rec != null && rec.size() > 0) {
				for (int i = 0; i < rec.size(); i++) {
					String s = rec.get(i).getIsUse();
					rec.get(i).setIsUse(s.equals("Y") ? "启用" : "停用");//update by sychen 20100831
//					rec.get(i).setIsUse(s.equals("U") ? "启用" : "停用");
				}
			}
			list.setRoot(rec);
			list.setSuccess(true);
			long s = bll.findAll().size();
			list.setTotalProperty(s);
			String jsonstr = JSONUtil.serialize(list);
			write(jsonstr);
		} catch (Exception ex) {
			LogUtil.log("获取协理单位信息错误", Level.INFO, ex);
		}
	}

	/**
	 * 添加协理单位信息
	 * 
	 */
	public void addAssistantUnits() throws NamingException, IOException,
			JSONException {  

		HrCAssistantManagerUnitsFacadeRemote bll = (HrCAssistantManagerUnitsFacadeRemote) factory.getFacadeRemote("HrCAssistantManagerUnitsFacade");
		try {
			bll.save(unit);
			write("{success: true, msg: \'增加成功\'}");
		}  
		catch (CodeRepeatException e) {
			write("{failure:true,errMsg:'" + e.getMessage() + "'}");
		}
	}

	/**
	 * 返回协理单位id最大值
	 * 
	 */
	private long getMaxUnitId() {
		long maxid = 0;
		String sqlstr = String
				.format("select nvl(max(assistant_manager_units_id)+1,1) from hr_c_assistant_manager_units");
		try {
			NativeSqlHelperRemote bll = (NativeSqlHelperRemote) Ejb3Factory
					.getInstance().getFacadeRemote("NativeSqlHelper");
			Object ob = bll.getSingal(sqlstr);
			maxid = Long.parseLong(ob.toString());
		} catch (NumberFormatException e) {
			// TODO Auto-generated catch block
			LogUtil.log("hr_c_assistant_manager_units err", Level.INFO, e);

		} catch (Exception e) {
			// TODO Auto-generated catch block
			LogUtil.log("hr_c_assistant_manager_units err", Level.INFO, e);
		}
		return maxid;
	}

	public void getMaxId() throws IOException {
		write("{maxId: '" + getMaxUnitId() + "'}");
	}

	// 删除协理单位
	public void deleteAssistantUnits() throws IOException {
		String str = request.getParameter("str");
		String[] id = str.split(",");
		try {
			HrCAssistantManagerUnitsFacadeRemote bll = (HrCAssistantManagerUnitsFacadeRemote) Ejb3Factory
					.getInstance().getFacadeRemote(
							"HrCAssistantManagerUnitsFacade");
			for (int i = 0; i < id.length; i++) {
				HrCAssistantManagerUnits hrcassistantmanagerunits = new HrCAssistantManagerUnits();
				hrcassistantmanagerunits.setAssistantManagerUnitsId(Long
						.parseLong(id[i]));
				bll.delete(hrcassistantmanagerunits);
			}
			write("{success:true,msg:\'deleted\'}");
		} catch (Exception e) {
			write("{failure:true,msg:\'wrong\'}");
		}
	}

	// 取得选中的单位的信息
	public void getSelectedUnit() throws NamingException, IOException,
			JSONException {
//		String str = request.getParameter("id");
		HrCAssistantManagerUnitsFacadeRemote bll = (HrCAssistantManagerUnitsFacadeRemote) Ejb3Factory
				.getInstance()
				.getFacadeRemote("HrCAssistantManagerUnitsFacade");
//		ListRange<HrCAssistantManagerUnits> list = new ListRange<HrCAssistantManagerUnits>();
//		list.getRoot().add(bll.findById(Long.parseLong(str)));
//		list.setSuccess(true);
//		write(JSONUtil.serialize(list));
		String id = request.getParameter("id");
		HrCAssistantManagerUnits model=bll.findById(Long.parseLong(id));
		String str=JSONUtil.serialize(model);
		 write("{success: true,data:"+str+"}");
	}

	// 更新单位信息
	public void updateAssistantUnits() throws IOException, NamingException {

		HrCAssistantManagerUnitsFacadeRemote bll = (HrCAssistantManagerUnitsFacadeRemote) factory.getFacadeRemote("HrCAssistantManagerUnitsFacade");
		HrCAssistantManagerUnits model = bll.findById(unit.getAssistantManagerUnitsId());

		model.setAssistantManagerUnitsName(unit.getAssistantManagerUnitsName());
		model.setIsUse(unit.getIsUse());
		model.setRetrieveCode(unit.getRetrieveCode());
		try {
			bll.update(model);
			write("{success: true, msg: \'修改成功\'}");
		} catch (CodeRepeatException e) {
			write("{failure:true,errMsg:'" + e.getMessage() + "'}");
		}
	}

	/**
	 * @return the start
	 */
	public String getStart() {
		return start;
	}

	/**
	 * @param start
	 *            the start to set
	 */
	public void setStart(String start) {
		this.start = start;
	}

	/**
	 * @return the limit
	 */
	public String getLimit() {
		return limit;
	}

	/**
	 * @param limit
	 *            the limit to set
	 */
	public void setLimit(String limit) {
		this.limit = limit;
	}

	public HrCAssistantManagerUnits getUnit() {
		return unit;
	}

	public void setUnit(HrCAssistantManagerUnits unit) {
		this.unit = unit;
	}
}