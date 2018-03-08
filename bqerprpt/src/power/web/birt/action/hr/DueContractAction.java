/**
　* Copyright ustcsoft.com
　* All right reserved.
　*/
package power.web.birt.action.hr;

import java.util.ArrayList;
import java.util.List;

import power.ear.comm.ejb.PageObject;
import power.ejb.hr.ContractQuery;
import power.ejb.hr.ContractQueryFacadeRemote;
import power.web.birt.bean.hr.ContractBean;
import power.web.birt.constant.Constant;
import power.web.birt.constant.commUtils;
import power.web.comm.AbstractAction;

/**
 * （到期）劳动合同台帐Action
 * @author zhujie
 *
 */
public class DueContractAction extends AbstractAction{

	/** serialVersionUID */
	private static final long serialVersionUID = 1L;
	/** 远程 */
	private ContractQueryFacadeRemote remote;
	/** 员工姓名截断字节数 */
	private int ten = 10;
	
	/**
	 * 构造函数
	 */
	public DueContractAction() {
		remote = (ContractQueryFacadeRemote) factory.getFacadeRemote("ContractQueryFacade");
	}
	
	/**
	  * 获取（到期）劳动合同台帐信息
	  * @param startDate 开始时间
	  * @param endDate 结束时间
	  * @param deptCode 部门编码
	  * @param contractTerm 合同有效期
      * @param contractType 合同形式
	  * @param duetoTime 合同到期月份
	  * @param enterpriseCode 企业编码
	  * @return ContractBean
	  */
	public ContractBean getContractQueryEmployee(String startDate, String endDate,
			String deptCode, String contractTerm, String contractType,
			String duetoTime, String enterpriseCode){
		ContractBean entity = new ContractBean();
		PageObject pg = new PageObject();
		List<ContractQuery> result = new ArrayList<ContractQuery>();
		// 调用EJB取得劳动合同台帐信息
		pg = remote.getContractQueryEmployee(startDate, endDate, deptCode,
				contractTerm, contractType, duetoTime, enterpriseCode);
		if(pg!=null){
			result = (List<ContractQuery>)pg.getList();
		}
		// 对信息进行处理
		if(result!=null&&result.size()>0){
			ContractQuery lstBean = null;
			for(int i=0;i<result.size();i++){
				List<String> strList = new ArrayList();
				lstBean = result.get(i);
				// 对员工姓名进行截断前10个字节数处理
				if(lstBean.getChsName()!=null&&lstBean.getChsName().getBytes().length>ten){
					lstBean.setChsName(commUtils.cutByByte(lstBean.getChsName(), ten));
				}
				// 对甲方部门进行换行处理
		    	String deptNameFirst = commUtils.addBrByByteLengthForHR(lstBean.getDeptNameFirst(),
		    			Constant.EIGHTPOINTPERSETY9);
		    	lstBean.setDeptNameFirst(deptNameFirst.replace(" ", "&nbsp;"));
		    	strList.add(deptNameFirst);
		    	// 对甲方地址进行换行处理
		    	String firstAddress = commUtils.addBrByByteLengthForHR(lstBean.getFirstAddress(),
		    			Constant.EIGHTPOINTPERSETY9);
		    	lstBean.setFirstAddress(firstAddress.replace(" ", "&nbsp;"));
		    	strList.add(firstAddress);
		    	// 对所属部门进行换行处理
		    	String deptNameSecond = commUtils.addBrByByteLengthForHR(lstBean.getDeptNameSecond(),
		    			Constant.EIGHTPOINTPERSETY9);
		    	lstBean.setDeptNameSecond(deptNameSecond.replace(" ", "&nbsp;"));
		    	strList.add(deptNameSecond);
		    	// 对岗位名称进行换行处理
		    	String stationName = commUtils.addBrByByteLengthForHR(lstBean.getStationName(),
		    			Constant.EIGHTPOINTPERSETY9);
		    	lstBean.setStationName(stationName.replace(" ", "&nbsp;"));
		    	strList.add(stationName);
		    	// 对备注进行换行处理
		    	String memoContract = commUtils.addBrByByteLengthForHR(lstBean.getMemoContract(),
		    			Constant.EIGHTPOINTPERSETY10);
		    	lstBean.setMemoContract(memoContract.replace(" ", "&nbsp;"));
		    	strList.add(memoContract);
		    	// 叠加后的计数
		    	lstBean.setCntRow(commUtils.countMaxContain(strList, Constant.HTML_CHANGE_LINE) + 1);
			}
			result = commUtils.delectName(result,41.41);
			entity.setList(result);	
		}
		return entity;
	}
	
}
