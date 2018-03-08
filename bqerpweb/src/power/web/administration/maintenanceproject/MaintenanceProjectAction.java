/**
 * Copyright ustcsoft.com
 * All right reserved.
 */
package power.web.administration.maintenanceproject;

import java.sql.SQLException;
import java.util.List;
import java.util.logging.Level;

import power.ear.comm.ejb.PageObject;
import power.ejb.administration.AdCCarwhPay;
import power.ejb.administration.AdCCarwhPayFacadeRemote;
import power.ejb.administration.AdCCarwhPro;
import power.ejb.administration.AdCCarwhProFacadeRemote;
import power.ejb.administration.comm.CodeCommonFacadeRemote;
import power.ejb.hr.LogUtil;
import power.web.comm.AbstractAction;
import power.web.comm.Constants;

import com.googlecode.jsonplugin.JSONException;
import com.googlecode.jsonplugin.JSONUtil;

/**
 * 维修项目维护
 * @author chen shoujiang
 *  
 */
public class MaintenanceProjectAction extends AbstractAction{

	private static final long serialVersionUID = 1L;
	/**维修项目维护*/
	private AdCCarwhProFacadeRemote adcCarwhProFacadeRemote;
	/**费用类别维护*/
	private AdCCarwhPayFacadeRemote adcCarwhPayFacadeRemote;
	/**共通接口*/
	private CodeCommonFacadeRemote codeCommonFacadeRemote;
	/**序号*/
	private String id;
	/**项目编码*/
	private String proCode; 
	/**项目名称*/
	private String proName;
	/**费用类别代码*/
	private String payCode;
	/**是否使用*/
	private String haveLise;
	/**费用类别名称*/
	private String payName;
	/**开始行数*/
	private String start;
	/**查询行数*/
	private String limit;

	/**
	 * 构造函数
	 */
	public MaintenanceProjectAction() {
		/**维修项目维护*/
		adcCarwhProFacadeRemote = (AdCCarwhProFacadeRemote) factory
				.getFacadeRemote("AdCCarwhProFacade");
		/**费用类别维护*/
		adcCarwhPayFacadeRemote = (AdCCarwhPayFacadeRemote) factory
				.getFacadeRemote("AdCCarwhPayFacade");
		/**共通接口*/
		codeCommonFacadeRemote = (CodeCommonFacadeRemote) factory
				.getFacadeRemote("CodeCommonFacade");
	}
	
	/**
	 *  维修项目维护 
	 * @throws JSONException
	 * 
	 */
	public void getMaintenanceProjectList() throws JSONException, Exception {
		try{
			LogUtil.log("Action:查询维修项目维护开始", Level.INFO, null);
			/** 获取开始行数和查询行数 */
			PageObject obj = new PageObject();
			// 如果非空
			if (start != null && limit != null) {
				obj = adcCarwhProFacadeRemote.findAll(employee.getEnterpriseCode(),Integer.parseInt(start),Integer.parseInt(limit));
			} else {
				// 要是不为空的话，就不传入
				obj = adcCarwhProFacadeRemote.findAll(employee.getEnterpriseCode());
			}
			// 输出
			String strOutput = "";
			//　要是查询结果不为空的话，就赋值
			if(obj.getList() != null && obj.getList().size() > 0) {
				strOutput = JSONUtil.serialize(obj);
			} else {
				// 否则设为空值
				strOutput = "{\"list\":[],\"totalCount\":0}";
			}
			write(strOutput);
			LogUtil.log("Action:查询维修项目维护结束", Level.INFO, null);
		}catch (JSONException jsone) {
            write(Constants.DATA_FAILURE);
			LogUtil.log("Action:查询维修项目维护失败", Level.SEVERE, null);
		}catch (SQLException sqle) {
            write(Constants.SQL_FAILURE);
            LogUtil.log("Action:查询维修项目维护失败", Level.SEVERE, null);
		}
	}
	
	/**
	 * 查询费用类别
	 * @throws JSONException
	 */
	public void getPaySortList() throws JSONException {
		try{
			LogUtil.log("Action:查询费用类别开始", Level.INFO, null);
			// 分页信息
			PageObject obj = new PageObject();
			// 如果非空
			if (start != null && limit != null) {
				obj = adcCarwhPayFacadeRemote.findAll(employee.getEnterpriseCode(),Integer.parseInt(start),Integer.parseInt(limit));
			} else {
				// 要是不为空的话，就不传入
				obj = adcCarwhPayFacadeRemote.findAll(employee.getEnterpriseCode());
			}
			// 输出
			String strOutput = "";
			//　要是查询结果不为空的话，就赋值
			if(obj.getList() != null && obj.getList().size() > 0) {
				strOutput = JSONUtil.serialize(obj);
			} else {
				// 否则设为空值
				strOutput = "{\"list\":[],\"totalCount\":0}";
			}
			write(strOutput);
			LogUtil.log("Action:查询费用类别结束", Level.INFO, null);
		}catch (JSONException jsone) {
            write(Constants.DATA_FAILURE);
			LogUtil.log("Action:查询维修项目维护失败", Level.SEVERE, null);
		}catch (SQLException sqle) {
            write(Constants.SQL_FAILURE);
            LogUtil.log("Action:查询维修项目维护失败", Level.SEVERE, null);
		}
	}
	
	/**
	 * 费用类别列表
	 * @throws JSONException
	 */
	@SuppressWarnings("unchecked")
	public void getPaySortCboList() throws JSONException {
		try{
			LogUtil.log("Action:查询费用类别CboList开始", Level.INFO, null);
			PageObject obj = adcCarwhPayFacadeRemote.findAll(employee.getEnterpriseCode());
			List<AdCCarwhPay> list = obj.getList();
			if (list.size() > 0) {
				AdCCarwhPay inv = new AdCCarwhPay();
				inv.setPayCode("");
				inv.setPayName("");
				list.add(0, inv);
				obj.setList(list);
			}	
			String str = JSONUtil.serialize(obj);
			LogUtil.log("Action:查询费用类别CboList结束", Level.INFO, null);
			write(str);
		}catch (JSONException jsone) {
            write(Constants.DATA_FAILURE);
			LogUtil.log("Action:查询费用类别CboList失败", Level.SEVERE, null);
		}catch (SQLException sqle) {
            write(Constants.SQL_FAILURE);
            LogUtil.log("Action:查询费用类别CboList失败", Level.SEVERE, null);
		}
	}
	
	/**
	 * 费用类别列表
	 * @throws JSONException
	 */
	@SuppressWarnings("unchecked")
	public void getPaySortShowList() throws JSONException {
		try{
			LogUtil.log("Action:查询费用类别显示数据开始", Level.INFO, null);
			PageObject obj = adcCarwhPayFacadeRemote.findAllData(employee.getEnterpriseCode());
//			List<AdCCarwhPay> list = obj.getList();
//			if (list.size() > 0) {
//				AdCCarwhPay inv = new AdCCarwhPay();
//				inv.setPayCode("");
//				inv.setPayName("");
//				list.add(0, inv);
//				obj.setList(list);
//			}	
			String str = JSONUtil.serialize(obj);
			LogUtil.log("Action:查询费用类别显示数据结束", Level.INFO, null);
			write(str);
		}catch (JSONException jsone) {
            write(Constants.DATA_FAILURE);
			LogUtil.log("Action:查询费用类别显示数据失败", Level.SEVERE, null);
		}catch (SQLException sqle) {
            write(Constants.SQL_FAILURE);
            LogUtil.log("Action:查询费用类别显示数据失败", Level.SEVERE, null);
		}
	}
	/**
	 * 删除维修项目维护
	 * @throws JSONException
	 * @throws Exception
	 */
	public void deleteMaintenanceProject() throws JSONException, Exception {
		LogUtil.log("Action:删除维修项目维护开始", Level.INFO, null);
		try{
			// 创建一个
			AdCCarwhPro entity = new AdCCarwhPro();
			// 如果序号非空的话
			if(null != id && !"".equals(id))
			{
				// 找到对应序号信息		
				entity = adcCarwhProFacadeRemote.findById(Long.parseLong(id));
				// 上次修改人
				entity.setUpdateUser(employee.getWorkerCode());
				// 删除
				adcCarwhProFacadeRemote.delete(entity);
				write(Constants.DELETE_SUCCESS);
			}else {
				write(Constants.DELETE_FAILURE);
			}
			LogUtil.log("Action:删除维修项目维护结束", Level.INFO, null);
		}catch (SQLException sqle) {
            LogUtil.log("Action:删除维修项目维护失败", Level.SEVERE, null);
            write(Constants.SQL_FAILURE);
		} 
	}

	/**
	 * 删除费用维护
	 * @throws JSONException
	 * @throws Exception
	 */
	public void deletePaySort() throws JSONException, Exception {
		LogUtil.log("Action:删除费用类别维护开始", Level.INFO, null);
		try{
			// 创建一个
			AdCCarwhPay entity = new AdCCarwhPay();
			// 如果序号非空的话
			if(null != id && !"".equals(id))
			{
				// 找到对应序号信息		
				entity = adcCarwhPayFacadeRemote.findById(Long.parseLong(id));
				// 上次修改人
				entity.setUpdateUser(employee.getWorkerCode());
				// 删除
				adcCarwhPayFacadeRemote.delete(entity);
				write(Constants.DELETE_SUCCESS);
			}else {
				write(Constants.DELETE_FAILURE);
			}
			LogUtil.log("Action:删除费用类别维护结束", Level.INFO, null);
		}catch (SQLException sqle) {
            LogUtil.log("Action:删除费用类别维护失败", Level.SEVERE, null);
            write(Constants.SQL_FAILURE);
		}
	}
	
	/**
	 * 添加一个项目维护
	 * @throws JSONException
	 * @throws Exception
	 */
	public void addMaintenanceProject() throws JSONException, Exception {
		LogUtil.log("Action:添加维修项目维护开始", Level.INFO, null);
		try{
			// 创建一个
			AdCCarwhPro entity = new AdCCarwhPro();
			// 如果费用类别代码非空的话
			if(null != payCode && !"".equals(payCode)) {
				entity.setPayCode(payCode);
				String proCodeAction = codeCommonFacadeRemote.getMaintenanceItemCode(payCode);
				entity.setProCode(proCodeAction);
			}
			// 如果项目名称非空的话
			if(null != proName && !"".equals(proName)) {
				entity.setProName(proName);
			}
			// 如果是否使用非空的话
			if(null != haveLise && !"".equals(haveLise)) {
				entity.setHaveLise(haveLise);
			}
			// 设置企业代码
			entity.setEnterpriseCode(employee.getEnterpriseCode());
			entity.setUpdateUser(employee.getWorkerCode());
			// 保存数据
			adcCarwhProFacadeRemote.save(entity);
			write("{success:true,msg:'&nbsp&nbsp&nbsp保存成功。&nbsp&nbsp&nbsp'}");
			LogUtil.log("Action:添加维修项目维护结束", Level.INFO, null);
		}catch (SQLException sqle) {
            LogUtil.log("Action:添加维修项目维护失败", Level.SEVERE, null);
            write(Constants.SQL_FAILURE);
		}
	}
	
	/**
	 * 添加一个项目维护
	 * @throws JSONException
	 * @throws Exception
	 */
	public void addPaySort() throws JSONException, Exception {
		LogUtil.log("Action:添加费用类别维护开始", Level.INFO, null);
		try{
			// 创建一个
			AdCCarwhPay entity = new AdCCarwhPay();
			// 如果费用类别名称非空的话
			if(null != payName && !"".equals(payName)) {
				entity.setPayName(payName);
			}
			
			entity.setUpdateUser(employee.getWorkerCode());
			// 设置企业代码
			entity.setEnterpriseCode(employee.getEnterpriseCode());
			// 保存数据
			adcCarwhPayFacadeRemote.save(entity);
			write("{success:true,msg:'&nbsp&nbsp&nbsp保存成功。&nbsp&nbsp&nbsp'}");
			LogUtil.log("Action:添加费用类别维护结束", Level.INFO, null);
		} catch (SQLException sqle) {
			LogUtil.log("Action:添加费用类别维护失败", Level.SEVERE, null);
			write(Constants.SQL_FAILURE);
		}
	}
	
	/**
	 * 修改一个项目维护
	 * @throws JSONException
	 * @throws Exception
	 */
	public void modifyMaintenanceProject() throws JSONException, Exception {
		LogUtil.log("Action:修改维修项目维护开始", Level.INFO, null);
		try{
			// 创建一个
			AdCCarwhPro entity = new AdCCarwhPro();
			if(null != id && !"".equals(id)) {
				entity = adcCarwhProFacadeRemote.findById(Long.parseLong(id));
//				// 如果费用类别代码非空的话
//				if(null != payCode) {
//					entity.setPayCode(payCode);
////					String proCodeAction = codeCommonFacadeRemote.getMaintenanceItemCode(payCode);
////					entity.setProCode(proCodeAction);
//				}
				// 如果项目名称呢个非空的话
				if(null != proName && !"".equals(proName)) {
					entity.setProName(proName);
				}
				// 如果是否使用非空的话
				if(!"null".equals(haveLise) && !"".equals(haveLise)) {
					entity.setHaveLise(haveLise);
				}
				entity.setUpdateUser(employee.getWorkerCode());
				// 保存数据
				adcCarwhProFacadeRemote.update(entity);
				write("{success:true,msg:'&nbsp&nbsp&nbsp保存成功。&nbsp&nbsp&nbsp'}");
			}else 
			write("{success:true,msg:'&nbsp&nbsp&nbsp保存失败！&nbsp&nbsp&nbsp'}");
			LogUtil.log("Action:修改维修项目维护结束", Level.INFO, null);
		} catch(SQLException sqle) {
			LogUtil.log("Action:修改维修项目维护失败", Level.SEVERE, null);
			write(Constants.SQL_FAILURE);
		}
	}
	
	/**
	 * 修改一个项目维护
	 * @throws JSONException
	 * @throws Exception
	 */
	public void modifyPaySort() throws JSONException, Exception {
		LogUtil.log("Action:修改费用类别维护开始", Level.INFO, null);
		try{
			// 创建一个
			AdCCarwhPay entity = new AdCCarwhPay();
			if(null != id && !"".equals(id)) {
				entity = adcCarwhPayFacadeRemote.findById(Long.parseLong(id));
//				// 如果费用类别代码非空的话
//				if(null != payCode && !"".equals(payCode)) {
//					entity.setPayCode(payCode);
//				}
				// 如果费用类别名称非空的话
				if(null != payName && !"".equals(payName)) {
					entity.setPayName(payName);
				}
				entity.setUpdateUser(employee.getWorkerCode());
				// 保存数据
				adcCarwhPayFacadeRemote.update(entity);
				write("{success:true,msg:'&nbsp&nbsp&nbsp保存成功。&nbsp&nbsp&nbsp'}");
			}else 
				write("{success:true,msg:'&nbsp&nbsp&nbsp保存失败！&nbsp&nbsp&nbsp'}");
			LogUtil.log("Action:修改费用类别维护结束", Level.INFO, null);
		}  catch(SQLException sqle) {
			LogUtil.log("Action:修改费用类别维护失败", Level.SEVERE, null);
			write(Constants.SQL_FAILURE);
		}
	}
	
	/**
	 * 获取序号
	 * @return
	 */
	public String getId() {
		return id;
	}
	/**
	 * 设置序号
	 * @return
	 */
	public void setId(String id) {
		this.id = id;
	}
	
	/**
	 *  获取项目代码
	 * @return
	 */
	public String getProCode() {
		return proCode;
	}

	/**
	 * 设置项目代码
	 * @param proCode
	 */
	public void setProCode(String proCode) {
		this.proCode = proCode;
	}

	/**
	 * 获取项目名称
	 * @return
	 */
	public String getProName() {
		return proName;
	}

	/**
	 * 设置项目名称
	 * @param proName
	 */
	public void setProName(String proName) {
		this.proName = proName;
	}

	/**
	 * 获取费用类别
	 * @return
	 */
	public String getPayCode() {
		return payCode;
	}

	/**
	 * 设置费用类别
	 * @param payCode
	 */
	public void setPayCode(String payCode) {
		this.payCode = payCode;
	}
	/**
	 * 获取是否使用
	 * @return
	 */
	public String getHaveLise() {
		return haveLise;
	}

	/**
	 * 设置是否使用
	 * @param haveLise
	 */
	public void setHaveLise(String haveLise) {
		this.haveLise = haveLise;
	}

	/**
	 * 获取费用类别名称
	 * @return
	 */
	public String getPayName() {
		return payName;
	}

	/**
	 *设置费用类别名称
	 * @param payName
	 */
	public void setPayName(String payName) {
		this.payName = payName;
	}
	public String getStart() {
		return start;
	}

	public void setStart(String start) {
		this.start = start;
	}

	public String getLimit() {
		return limit;
	}

	public void setLimit(String limit) {
		this.limit = limit;
	}
}
