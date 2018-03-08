package power.web.birt.action.bqmis;
import java.text.ParseException;
import java.util.List;
import power.ear.comm.ejb.Ejb3Factory;
import power.ejb.manage.plan.trainplan.TrainPlanManager;
import power.ejb.manage.plan.trainplan.form.BpTrainPlanApproveForm;

public class TrainPlanReport {
	private TrainPlanManager remote;
	public static Ejb3Factory factory;
	static {
		factory = Ejb3Factory.getInstance();
	}

	/**
	 * 构造函数
	 */
	public TrainPlanReport() {
		remote = (TrainPlanManager) factory
				.getFacadeRemote("TrainPlanManagerImpl");
	}

	public List<BpTrainPlanApproveForm> setApprove(String yearmonth)
			throws ParseException {
		List<BpTrainPlanApproveForm> list = remote
				.getGatherApprovelist(yearmonth);
		if (list != null) {
			return list;
		} else {
			return null;
		}
	}
}
