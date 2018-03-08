package power.web.birt.action;

import bsh.Remote;
import power.ear.comm.ejb.Ejb3Factory;
import power.ejb.resource.business.MonthBalance;
import power.ejb.resource.form.MaterialDiscrepancyForReport;

public class MatericalDifferentApportion
{
	private MonthBalance remote;
	
	public static Ejb3Factory factory;

	static {
		factory = Ejb3Factory.getInstance();
	}
	public MatericalDifferentApportion()
	{
		remote = (MonthBalance)factory.getFacadeRemote("MonthBalanceImp");
	}
	
	public MaterialDiscrepancyForReport findMaterialDiscrepancy(String dateMonth)
	{
		MaterialDiscrepancyForReport temp = remote.getMaterialDiscrepancyInfo(dateMonth);
		return temp;
	}
}