/**
 * Copyright ustcsoft.com
 * All right reserved.
 */
package power.web.birt.action.bqmis;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.List;

import org.eclipse.birt.report.engine.api.script.IReportContext;
import org.eclipse.birt.report.engine.api.script.element.IReportDesign;

import power.web.comm.AbstractAction;

import power.ear.comm.ejb.Ejb3Factory;

import power.ejb.manage.contract.business.ConJBalance;
import power.ejb.manage.contract.business.ConJBalanceFacadeRemote;
import power.ejb.manage.contract.business.ConJContractInfoFacadeRemote;

import power.ejb.manage.contract.form.BpAppDetailForm;
import power.ejb.manage.contract.form.ConApproveForm;
import power.ejb.manage.contract.form.ConBalanceForm;
import power.ejb.manage.contract.form.ConBalanceFullForm;
import power.ejb.manage.contract.form.ContractForm;

public class GCContractBalance extends AbstractAction {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	/** 远程 */
	private ConJBalanceFacadeRemote remote;
	private ConJContractInfoFacadeRemote CRemote;

	public static Ejb3Factory factory;

	static {
		factory = Ejb3Factory.getInstance();
	}

	/**
	 * 构造函数
	 */
	public GCContractBalance() {

		remote = (ConJBalanceFacadeRemote) factory
				.getFacadeRemote("ConJBalanceFacade");
		CRemote = (ConJContractInfoFacadeRemote) factory
				.getFacadeRemote("ConJContractInfoFacade");

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.engine.api.script.eventhandler.IReportEventHandler#afterFactory(org.eclipse.birt.report.engine.api.script.IReportContext)
	 */
	public void afterFactory(IReportContext arg0) {
		// TODO Auto-generated method stub

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.engine.api.script.eventhandler.IReportEventHandler#afterRender(org.eclipse.birt.report.engine.api.script.IReportContext)
	 */
	public void afterRender(IReportContext arg0) {
		// TODO Auto-generated method stub

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.engine.api.script.eventhandler.IReportEventHandler#beforeFactory(org.eclipse.birt.report.engine.api.script.element.IReportDesign,
	 *      org.eclipse.birt.report.engine.api.script.IReportContext)
	 */
	public void beforeFactory(IReportDesign design, IReportContext context) {

	}

	/**
	 * 项目合同结算id
	 * 
	 * @param balanceId
	 *            项目合同结算审批
	 * @return
	 * @throws ParseException
	 */
	public List<ConApproveForm> setApprove(String balanceId)
			throws ParseException {
		ConJBalance baseInfo = remote.findById(Long.parseLong(balanceId));
		if (baseInfo.getWorkflowStatus() != null) {
			List<ConApproveForm> list = remote.getApproveList(Long
					.parseLong(balanceId));
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy年MM月dd日");
			SimpleDateFormat sdf1 = new SimpleDateFormat("yyyy-MM-dd");
			for (ConApproveForm data : list) {
				data.setOpinionTime(sdf.format(sdf1
						.parse(data.getOpinionTime())));
			}
			return list;
		} else
			return null;

	}

	/**
	 * 项目合同结算id
	 * 
	 * @param balanceId
	 *            某一项目合同所有结算记录
	 * @return
	 * @throws ParseException
	 */
	public List<ConBalanceForm> setPaymentList(String balanceId)
			throws ParseException {

		ConJBalance baseInfo = remote.findById(Long.parseLong(balanceId));
		Long conId = baseInfo.getConId();
		List<ConBalanceForm> list = remote.findBalanceListByConId(conId,
				baseInfo.getEnterpriseCode());
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy年MM月dd日");
		SimpleDateFormat sdf1 = new SimpleDateFormat("yyyy-MM-dd");
		for (ConBalanceForm data : list) {
			data
					.setApplicatDate(sdf.format(sdf1.parse(data
							.getApplicatDate())));
		}
		return list;

	}

	/**
	 * 项目合同结算id
	 * 
	 * @param balanceId
	 *            项目合同结算对象
	 * @return
	 */
	public ConBalanceFullForm setBaseInfo(String balanceId) {
		// String id = (String)context.getParameterValue("id");
		// Long projectId = Long.valueOf(id);
		ConBalanceFullForm baseInfo = remote.findBalanceByBalanceId((Long
				.parseLong(balanceId)));
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy年MM月");
		SimpleDateFormat sdf1 = new SimpleDateFormat("yyyy-MM");
		String entryDate = "";

		try {
			if (baseInfo.getEntryDate() != null)
				entryDate = sdf.format(sdf1.parse(baseInfo.getEntryDate()));

		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		baseInfo.setEntryDate(entryDate);
		baseInfo.setCurrencyName(CRemote.getCurrencyNameByItsCode(baseInfo
				.getCurrencyType()));
		return baseInfo;
	}

	/**
	 * 项目合同结算id
	 * 
	 * @param balanceId
	 *            项目合同对象
	 * @return
	 */
	public ContractForm setBaseInfoAdd(String balanceId) {
		// String id = (String)context.getParameterValue("id");
		// Long projectId = Long.valueOf(id);
		ConJBalance baseInfo = remote.findById(Long.parseLong(balanceId));
		Long conId = baseInfo.getConId();
		ContractForm obj = remote.findContractByConId(conId);
		return obj;
	}

	/**
	 * 采购合同报表列表 bq
	 */
	public List<BpAppDetailForm> bqfindBalanceListByAppId(String appId) {
		List<BpAppDetailForm> list = remote.bqFindBalaceReportByAppId(appId);
		if (list.size() > 0) {
			return list;
		} else {
			return null;
		}
	}

	/**
	 * 采购合同审批 bq
	 */
	public List<ConApproveForm> setCGApprove(String appId)
			throws ParseException {
		List<ConApproveForm> list = remote.getCgApproveList(appId);
		if (list != null && list.size() > 0) {
			// SimpleDateFormat sdf = new SimpleDateFormat("yyyy年MM月dd日");
			// SimpleDateFormat sdf1 = new SimpleDateFormat("yyyy-MM-dd");
			// for (ConApproveForm data : list) {
			// data.setOpinionTime(sdf.format(sdf1
			// .parse(data.getOpinionTime())));
			// }
			return list;
		} else {
			return null;
		}
	}

	/**
	 * 
	 */

	/**
	 * 需要转换的阿拉伯数字金额
	 * 
	 * @param toBeParse
	 *            转换好的汉字大写金额
	 * @return
	 */
	public String cnUperParse(String toBeParse) {

		return new CnUpperCaser(toBeParse).getCnString();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.engine.api.script.eventhandler.IReportEventHandler#beforeRender(org.eclipse.birt.report.engine.api.script.IReportContext)
	 */
	public void beforeRender(IReportContext arg0) {
		// TODO Auto-generated method stub

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.engine.api.script.eventhandler.IReportEventHandler#initialize(org.eclipse.birt.report.engine.api.script.IReportContext)
	 */
	public void initialize(IReportContext arg0) {
		// TODO Auto-generated method stub

	}

	/**
	 * 电气一工作票所需数据
	 * 
	 * @return electricOneBean 电气一工作票所需数据
	 */
	// public ElectricOneBean setElectricOneBean(String id) {
	// Long projectId = Long.valueOf(id);
	// PrjJInfo baseInfo = remote.findById(projectId);
	// if(baseInfo.getPrjStatus() != 3 && baseInfo.getPrjStatus() != 0) {
	// List list = remote.getApplyApprovelist(projectId);
	// }
	// }
}

/**
 * 将10亿以内的阿拉伯数字转成汉字大写形式
 * 
 * @author xizhenyin
 * 
 */
class CnUpperCaser {
	// 整数部分
	private String integerPart;
	// 小数部分
	private String floatPart;

	// 将数字转化为汉字的数组,因为各个实例都要使用所以设为静态
	private static final char[] cnNumbers = { '零', '壹', '贰', '叁', '肆', '伍',
			'陆', '柒', '捌', '玖' };

	// 供分级转化的数组,因为各个实例都要使用所以设为静态
	private static final char[] series = { '元', '拾', '百', '仟', '万', '拾', '百',
			'仟', '亿' };

	/**
	 * 构造函数,通过它将阿拉伯数字形式的字符串传入
	 * 
	 * @param original
	 */
	CnUpperCaser(String Param) {
		String original = Param.replaceAll(",", "");
		if (original.indexOf(".") > 0)
			while (original.substring(original.length() - 1).equals("0")
					|| original.substring(original.length() - 1).equals(".")) {
				original = original.substring(0, original.length() - 1);
			}

		// 成员变量初始化
		integerPart = "";
		floatPart = "";

		if (original.contains(".")) {
			// 如果包含小数点

			int dotIndex = original.indexOf(".");
			integerPart = original.substring(0, dotIndex);
			floatPart = original.substring(dotIndex + 1);
		} else {
			// 不包含小数点
			integerPart = original;
		}
	}

	/**
	 * 取得大写形式的字符串
	 * 
	 * @return
	 */
	public String getCnString() {
		// 因为是累加所以用StringBuffer
		StringBuffer sb = new StringBuffer();

		// 整数部分处理
		for (int i = 0; i < integerPart.length(); i++) {
			int number = getNumber(integerPart.charAt(i));
			if ((i == integerPart.length() - 1)
					&& (integerPart.charAt(i) == '0')) {
				sb.append(series[integerPart.length() - 1 - i]);
				continue;
			}

			if (integerPart.charAt(i) == '0'
					&& integerPart.charAt(i + 1) == '0')
				continue;

			sb.append(cnNumbers[number]);
			if (integerPart.charAt(i) == '0')
				continue;
			sb.append(series[integerPart.length() - 1 - i]);
		}

		// 小数部分处理
		if (floatPart.length() > 0) {

			sb.append("点");
			for (int i = 0; i < floatPart.length(); i++) {
				int number = getNumber(floatPart.charAt(i));

				sb.append(cnNumbers[number]);
			}
		}

		// 返回拼接好的字符串
		return sb.toString();
	}

	/**
	 * 将字符形式的数字转化为整形数字 因为所有实例都要用到所以用静态修饰
	 * 
	 * @param c
	 * @return
	 */
	private static int getNumber(char c) {
		String str = String.valueOf(c);
		return Integer.parseInt(str);
	}

	/**
	 * @param args
	 */
	// public static void main(String[] args) {
	// System.out.println(new CnUpperCaser("123456789.12345").getCnString());
	// System.out.println(new CnUpperCaser("123456789").getCnString());
	// System.out.println(new CnUpperCaser(".123456789").getCnString());
	// System.out.println(new CnUpperCaser("0.1234").getCnString());
	// System.out.println(new CnUpperCaser("1").getCnString());
	// System.out.println(new CnUpperCaser("12").getCnString());
	// System.out.println(new CnUpperCaser("123").getCnString());
	// System.out.println(new CnUpperCaser("1234").getCnString());
	// System.out.println(new CnUpperCaser("12345").getCnString());
	// System.out.println(new CnUpperCaser("123456").getCnString());
	// System.out.println(new CnUpperCaser("1234567").getCnString());
	// System.out.println(new CnUpperCaser("12345678").getCnString());
	// System.out.println(new CnUpperCaser("123456789").getCnString());
	// }
}
