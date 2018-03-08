package power.ejb.workticket.test;

import static org.junit.Assert.*;

import java.util.List;

import org.junit.BeforeClass;
import org.junit.Test;

import power.ear.comm.ejb.Ejb3Factory;
import power.ejb.workticket.business.WorkticketCharger;

public class WorkticketChargerTest {

	protected static WorkticketCharger remote;
	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
		remote=(WorkticketCharger) Ejb3Factory.getInstance().getFacadeRemote("WorkticketChargerImpl");
	}

	@Test
	public void testFindCharger() {
		List list=remote.findCharger("电气一种票", "");
		System.out.print(list.size());
	}

}
