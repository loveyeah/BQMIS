package power.ejb.basedata.test;

import org.junit.BeforeClass;
import org.junit.Test;

import power.ear.comm.Employee;
import power.ear.comm.ejb.Ejb3Factory;
import power.ear.comm.ejb.PageObject;
import power.ejb.basedata.BaseDataManager;

public class BaseDataManagerTest {
	static BaseDataManager remote;
	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
		remote=(BaseDataManager)Ejb3Factory.getInstance().getFacadeRemote("BaseDataManagerImpl");
	}

	@Test
	public void testGetEmployeeInfo() {
		 Employee e = remote.getEmployeeInfo("999999");
		 System.out.println(e.getWorkerName());
	}
	@Test
	public void testFindUnitList() {
		 
		PageObject p = remote.findUnitList(null, "hfdc");
		System.out.println(p.getTotalCount());
		
	}

}
