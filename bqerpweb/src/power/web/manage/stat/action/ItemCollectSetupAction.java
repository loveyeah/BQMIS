package power.web.manage.stat.action;

import java.util.ArrayList;
import java.util.List;

import power.ejb.manage.stat.BpCItemCollectSetup;
import power.ejb.manage.stat.BpCItemCollectSetupFacadeRemote;
import power.ejb.manage.stat.BpCItemCollectSetupId;
import power.web.comm.AbstractAction;
import power.web.comm.Constants;

import com.googlecode.jsonplugin.JSONException;
import com.googlecode.jsonplugin.JSONUtil;

@SuppressWarnings("serial")
public class ItemCollectSetupAction extends AbstractAction {
	private BpCItemCollectSetupFacadeRemote setupRemote;
	public ItemCollectSetupAction() {
		setupRemote = (BpCItemCollectSetupFacadeRemote) factory.getFacadeRemote("BpCItemCollectSetupFacade");
	}
	
	/**
	 * 保存所选择的时间
	 * @throws JSONException
	 */
	@SuppressWarnings("unchecked")
	public void saveDateType() throws JSONException {
		
		
		//是否全选中
		String selectAll = request.getParameter("selectAll");
		//时间类型
		String dateType = request.getParameter("dateType");
		//指标编码
		String itemCode = request.getParameter("itemCode");
		//选择明细
		String saveDatail = request.getParameter("saveDatail");
		
		if(setupRemote.ifUpdate(itemCode, employee.getEnterpriseCode())>0){
			setupRemote.delete(itemCode);
		}
		if("true".equals(selectAll)) {
			this.saveSelectAllData(dateType, itemCode);
		} else if("false".equals(selectAll)){
			Object object = JSONUtil.deserialize(saveDatail);
			if (object != null) {
	    		if (List.class.isAssignableFrom(object.getClass())) {
	    			// 如果是数组
	    			List lst = (List) object;
	    			this.saveSelectPart(dateType, itemCode, lst);
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
	public void queryDateType() throws JSONException {
		
		//指标编码
		String itemCode = request.getParameter("itemCode");
		List<BpCItemCollectSetup> list = setupRemote.findByProperty("id.itemCode", itemCode );
		String str = JSONUtil.serialize(list);
		write(str);
	}
	
	/**
	 *	选择所有时间点调用的方法
	 * @param dateType 时间类型
	 * @param itemCode 指标编码
	 */
	@SuppressWarnings("unused")
	private void saveSelectAllData(String dateType , String itemCode) {
		
		List<BpCItemCollectSetup> dateList = new ArrayList<BpCItemCollectSetup>();
		// 选择的时间类型是否为时间
		if("1".equals(dateType)) {
			for(int i = 1 ; i < 25 ; i++) {
				BpCItemCollectSetup bean = new BpCItemCollectSetup();
				BpCItemCollectSetupId beanId = new BpCItemCollectSetupId();
				bean.setIfAutoSetup("1");
				bean.setIfCollect("1");
				bean.setDataTimeType("1");
				beanId.setDataTimeDot(Long.valueOf(i));
				beanId.setItemCode(itemCode);
				bean.setId(beanId);
				bean.setEnterpriseCode(employee.getEnterpriseCode());
				dateList.add(bean);
			}
			// 选择的时间类型是否为天数
		} else if("3".equals(dateType)) {
			for(int i = 1 ; i < 32 ; i++) {
				BpCItemCollectSetup bean = new BpCItemCollectSetup();
				BpCItemCollectSetupId beanId = new BpCItemCollectSetupId();
				bean.setIfAutoSetup("1");
				bean.setIfCollect("1");
				bean.setDataTimeType("3");
				beanId.setDataTimeDot(Long.valueOf(i));
				beanId.setItemCode(itemCode);
				bean.setId(beanId);
				bean.setEnterpriseCode(employee.getEnterpriseCode());
				dateList.add(bean);
			}
			// 选择的时间类型是否为月
		} else if("4".equals(dateType)) {
			for(int i = 1 ; i < 13 ; i++) {
				BpCItemCollectSetup bean = new BpCItemCollectSetup();
				BpCItemCollectSetupId beanId = new BpCItemCollectSetupId();
				bean.setIfAutoSetup("1");
				bean.setIfCollect("1");
				bean.setDataTimeType("4");
				beanId.setDataTimeDot(Long.valueOf(i));
				beanId.setItemCode(itemCode);
				bean.setId(beanId);
				bean.setEnterpriseCode(employee.getEnterpriseCode());
				dateList.add(bean);
			}
			// 选择的时间类型是否为季度
		} else if("5".equals(dateType)) {
			for(int i = 1 ; i < 5 ; i++) {
				BpCItemCollectSetup bean = new BpCItemCollectSetup();
				BpCItemCollectSetupId beanId = new BpCItemCollectSetupId();
				bean.setIfAutoSetup("1");
				bean.setIfCollect("1");
				bean.setDataTimeType("5");
				beanId.setDataTimeDot(Long.valueOf(i));
				beanId.setItemCode(itemCode);
				bean.setId(beanId);
				bean.setEnterpriseCode(employee.getEnterpriseCode());
				dateList.add(bean);
			}
			// 选择的时间类型是否为年
		} else if("6".equals(dateType)) {
				BpCItemCollectSetup bean = new BpCItemCollectSetup();
				BpCItemCollectSetupId beanId = new BpCItemCollectSetupId();
				bean.setIfAutoSetup("1");
				bean.setIfCollect("1");
				bean.setDataTimeType("6");
				beanId.setDataTimeDot(1l);
				beanId.setItemCode(itemCode);
				bean.setId(beanId);
				bean.setEnterpriseCode(employee.getEnterpriseCode());
				dateList.add(bean);
		}
		setupRemote.save(dateList);
	}
	
	/**
	 * 任选时间点时保存选择时间方法
	 * @param dateType 时间类型
	 * @param itemCode 指标编码
	 * @param list 选择部分明细内容
	 */
	@SuppressWarnings("unchecked")
	private void saveSelectPart(String dateType , String itemCode ,List list) {
		
		List<BpCItemCollectSetup> dateList = new ArrayList<BpCItemCollectSetup>();
		for(int i = 0 ; i < list.size() ; i++) {
			BpCItemCollectSetup bean = new BpCItemCollectSetup();
			BpCItemCollectSetupId beanId = new BpCItemCollectSetupId();
			bean.setIfAutoSetup("0");
			if((Boolean)((List)list.get(i)).get(1)) {
				bean.setIfCollect("1");
			} else {
				bean.setIfCollect("0");
			}
			bean.setDataTimeType(dateType);
			beanId.setDataTimeDot(Long.valueOf(i+1));
			beanId.setItemCode(itemCode);
			bean.setId(beanId);
			bean.setEnterpriseCode(employee.getEnterpriseCode());
			dateList.add(bean);
		}
		setupRemote.save(dateList);
	}
}
