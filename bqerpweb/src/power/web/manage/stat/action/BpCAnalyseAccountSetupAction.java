package power.web.manage.stat.action;

import java.util.List;

import power.ejb.manage.stat.BpCAnalyseAccountSetupFacadeRemote;
import power.ejb.manage.stat.BpCAnalyseAccountSetup;
import power.ejb.manage.stat.BpCAnalyseAccountSetupId;
import power.web.comm.AbstractAction;
import power.web.comm.Constants;

import com.googlecode.jsonplugin.JSONException;
import com.googlecode.jsonplugin.JSONUtil;

@SuppressWarnings("serial")
public class BpCAnalyseAccountSetupAction extends AbstractAction {

	private BpCAnalyseAccountSetupFacadeRemote setupRemote;

	public BpCAnalyseAccountSetupAction() {
		setupRemote = (BpCAnalyseAccountSetupFacadeRemote) factory
				.getFacadeRemote("BpCAnalyseAccountSetupFacade");
	}

	/**
	 * 保存所选择的时间
	 * 
	 * @throws JSONException
	 */
	@SuppressWarnings("unchecked")
	public void saveAccountDateType() throws JSONException {
		// 是否全选中
		String selectAll = request.getParameter("selectAll");
		// 时间类型
		String dateType = request.getParameter("dateType");
		// 指标编码
		String accountCode = request.getParameter("accountCode");
		// 选择明细
		String saveDatail = request.getParameter("saveDatail");

		if(setupRemote.ifUpdate(accountCode, employee.getEnterpriseCode())>0){
			setupRemote.delete(accountCode);
		}
		if ("true".equals(selectAll)) {
			this.saveSelectAllData(dateType, accountCode);
		} else if ("false".equals(selectAll)) {
			Object object = JSONUtil.deserialize(saveDatail);
			if (object != null) {
				if (List.class.isAssignableFrom(object.getClass())) {
					// 如果是数组
					List lst = (List) object;
					this.saveSelectPart(dateType, accountCode, lst);
				}
			}
		}
		write(Constants.MODIFY_SUCCESS);

	}
	
	/**
	 * 查询已经保存的时间类型数据
	 * 
	 * @throws JSONException
	 */
	public void queryAccountDateType() throws JSONException {
		
		//指标编码
		String accountCode = request.getParameter("accountCode");
		List<BpCAnalyseAccountSetup> list = setupRemote.findByProperty("id.accountCode", Long.parseLong(accountCode));
		String str = JSONUtil.serialize(list);
		write(str);
	}
	

	/**
	 * 选择所有时间点调用的方法
	 * 
	 * @param dateType
	 *            时间类型
	 * @param itemCode
	 *            指标编码
	 */
	@SuppressWarnings("unused")
	private void saveSelectAllData(String dateType, String itemCode) {
		// 选择的时间类型是否为时间
		if ("1".equals(dateType)) {
			for (int i = 1; i < 25; i++) {
				BpCAnalyseAccountSetup bean = new BpCAnalyseAccountSetup();
				BpCAnalyseAccountSetupId beanId = new BpCAnalyseAccountSetupId();
				bean.setIfAutoSetup("1");
				bean.setIfCollect("1");
				bean.setTimeType("1");
				beanId.setDataTimeDot(Long.valueOf(i));
				beanId.setAccountCode(Long.parseLong(itemCode));
				bean.setId(beanId);
				bean.setEnterpriseCode(employee.getEnterpriseCode());
				setupRemote.save(bean);
			}
			// 选择的时间类型是否为天数
		} else if ("3".equals(dateType)) {
			for (int i = 1; i < 32; i++) {
				BpCAnalyseAccountSetup bean = new BpCAnalyseAccountSetup();
				BpCAnalyseAccountSetupId beanId = new BpCAnalyseAccountSetupId();
				bean.setIfAutoSetup("1");
				bean.setIfCollect("1");
				bean.setTimeType("3");
				beanId.setDataTimeDot(Long.valueOf(i));
				beanId.setAccountCode(Long.parseLong(itemCode));
				bean.setId(beanId);
				bean.setEnterpriseCode(employee.getEnterpriseCode());
				setupRemote.save(bean);
			}
			// 选择的时间类型是否为月
		} else if ("4".equals(dateType)) {
			for (int i = 1; i < 13; i++) {
				BpCAnalyseAccountSetup bean = new BpCAnalyseAccountSetup();
				BpCAnalyseAccountSetupId beanId = new BpCAnalyseAccountSetupId();
				bean.setIfAutoSetup("1");
				bean.setIfCollect("1");
				bean.setTimeType("4");
				beanId.setDataTimeDot(Long.valueOf(i));
				beanId.setAccountCode(Long.parseLong(itemCode));
				bean.setId(beanId);
				bean.setEnterpriseCode(employee.getEnterpriseCode());
				setupRemote.save(bean);
			}
			// 选择的时间类型是否为季度
		} else if ("5".equals(dateType)) {
			for (int i = 1; i < 5; i++) {
				BpCAnalyseAccountSetup bean = new BpCAnalyseAccountSetup();
				BpCAnalyseAccountSetupId beanId = new BpCAnalyseAccountSetupId();
				bean.setIfAutoSetup("1");
				bean.setIfCollect("1");
				bean.setTimeType("5");
				beanId.setDataTimeDot(Long.valueOf(i));
				beanId.setAccountCode(Long.parseLong(itemCode));
				bean.setId(beanId);
				bean.setEnterpriseCode(employee.getEnterpriseCode());
				setupRemote.save(bean);
			}
			// 选择的时间类型是否为年
		} else if ("6".equals(dateType)) {
			BpCAnalyseAccountSetup bean = new BpCAnalyseAccountSetup();
			BpCAnalyseAccountSetupId beanId = new BpCAnalyseAccountSetupId();
			bean.setIfAutoSetup("1");
			bean.setIfCollect("1");
			bean.setTimeType("6");
			beanId.setDataTimeDot(1l);
			beanId.setAccountCode(Long.parseLong(itemCode));
			bean.setId(beanId);
			bean.setEnterpriseCode(employee.getEnterpriseCode());
			setupRemote.save(bean);
		}
	}

	/**
	 * 任选时间点时保存选择时间方法
	 * 
	 * @param dateType
	 *            时间类型
	 * @param itemCode
	 *            指标编码
	 * @param list
	 *            选择部分明细内容
	 */
	@SuppressWarnings("unchecked")
	private void saveSelectPart(String dateType, String itemCode, List list) {
		
		for (int i = 0; i < list.size(); i++) {
			BpCAnalyseAccountSetup bean = new BpCAnalyseAccountSetup();
			BpCAnalyseAccountSetupId beanId = new BpCAnalyseAccountSetupId();
			bean.setIfAutoSetup("0");
			if ((Boolean) ((List) list.get(i)).get(1)) {
				bean.setIfCollect("1");
			} else {
				bean.setIfCollect("0");
			}
			bean.setTimeType(dateType);
			beanId.setDataTimeDot(Long.valueOf(i + 1));
			beanId.setAccountCode(Long.parseLong(itemCode));
			bean.setId(beanId);
			bean.setEnterpriseCode(employee.getEnterpriseCode());
			setupRemote.save(bean);
		}
		// 选择的时间类型是否为时间
//		if ("1".equals(dateType)) {
//			for (int i = 0; i < list.size(); i++) {
//				BpCAnalyseAccountSetup bean = new BpCAnalyseAccountSetup();
//				BpCAnalyseAccountSetupId beanId = new BpCAnalyseAccountSetupId();
//				bean.setIfAutoSetup("0");
//				if ((Boolean) ((List) list.get(i)).get(1)) {
//					bean.setIfCollect("1");
//				} else {
//					bean.setIfCollect("0");
//				}
//				bean.setTimeType("1");
//				beanId.setDataTimeDot(Long.valueOf(i + 1));
//				beanId.setAccountCode(Long.parseLong(itemCode));
//				bean.setId(beanId);
//				bean.setEnterpriseCode(employee.getEnterpriseCode());
//				setupRemote.save(bean);
//			}
//			// 选择的时间类型是否为天数
//		} else if ("3".equals(dateType)) {
//			for (int i = 0; i < list.size(); i++) {
//				BpCAnalyseAccountSetup bean = new BpCAnalyseAccountSetup();
//				BpCAnalyseAccountSetupId beanId = new BpCAnalyseAccountSetupId();
//				bean.setIfAutoSetup("0");
//				if ((Boolean) ((List) list.get(i)).get(1)) {
//					bean.setIfCollect("1");
//				} else {
//					bean.setIfCollect("0");
//				}
//				bean.setTimeType("3");
//				beanId.setDataTimeDot(Long.valueOf(i + 1));
//				beanId.setAccountCode(Long.parseLong(itemCode));
//				bean.setId(beanId);
//				bean.setEnterpriseCode(employee.getEnterpriseCode());
//				setupRemote.save(bean);
//			}
//			// 选择的时间类型是否为月
//		} else if ("4".equals(dateType)) {
//			for (int i = 0; i < list.size(); i++) {
//				BpCAnalyseAccountSetup bean = new BpCAnalyseAccountSetup();
//				BpCAnalyseAccountSetupId beanId = new BpCAnalyseAccountSetupId();
//				bean.setIfAutoSetup("0");
//				if ((Boolean) ((List) list.get(i)).get(1)) {
//					bean.setIfCollect("1");
//				} else {
//					bean.setIfCollect("0");
//				}
//				bean.setTimeType("4");
//				beanId.setDataTimeDot(Long.valueOf(i + 1));
//				beanId.setAccountCode(Long.parseLong(itemCode));
//				bean.setId(beanId);
//				bean.setEnterpriseCode(employee.getEnterpriseCode());
//				setupRemote.save(bean);
//			}
//			// 选择的时间类型是否为月季度
//		} else if ("5".equals(dateType)) {
//			for (int i = 0; i < list.size(); i++) {
//				BpCAnalyseAccountSetup bean = new BpCAnalyseAccountSetup();
//				BpCAnalyseAccountSetupId beanId = new BpCAnalyseAccountSetupId();
//				bean.setIfAutoSetup("0");
//				if ((Boolean) ((List) list.get(i)).get(1)) {
//					bean.setIfCollect("1");
//				} else {
//					bean.setIfCollect("0");
//				}
//				bean.setTimeType("5");
//				beanId.setDataTimeDot(Long.valueOf(i + 1));
//				beanId.setAccountCode(Long.parseLong(itemCode));
//				bean.setId(beanId);
//				bean.setEnterpriseCode(employee.getEnterpriseCode());
//				setupRemote.save(bean);
//			}
//			// 选择的时间类型是否为年
//		} else if ("6".equals(dateType)) {
//			BpCAnalyseAccountSetup bean = new BpCAnalyseAccountSetup();
//			BpCAnalyseAccountSetupId beanId = new BpCAnalyseAccountSetupId();
//			bean.setIfAutoSetup("0");
//			if ((Boolean) ((List) list.get(0)).get(1)) {
//				bean.setIfCollect("1");
//			} else {
//				bean.setIfCollect("0");
//			}
//			bean.setTimeType("6");
//			beanId.setDataTimeDot(1l);
//			beanId.setAccountCode(Long.parseLong(itemCode));
//			bean.setId(beanId);
//			bean.setEnterpriseCode(employee.getEnterpriseCode());
//			setupRemote.save(bean);
//		}
	}
}
