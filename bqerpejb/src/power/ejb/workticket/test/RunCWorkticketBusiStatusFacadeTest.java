package power.ejb.workticket.test;

import java.text.ParseException;
import java.util.List;

import org.junit.BeforeClass;
import org.junit.Test;

import power.ear.comm.ejb.Ejb3Factory;
import power.ejb.workticket.RunCWorkticketBusiStatus;
import power.ejb.workticket.RunCWorkticketBusiStatusFacadeRemote;

public class RunCWorkticketBusiStatusFacadeTest {

	protected static RunCWorkticketBusiStatusFacadeRemote remote;

	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
		remote = (RunCWorkticketBusiStatusFacadeRemote) Ejb3Factory
				.getInstance()
				.getFacadeRemote("RunCWorkticketBusiStatusFacade");
	}

	@Test
	public void testFindAll() throws ParseException {
		List<RunCWorkticketBusiStatus> list = remote.findAll();
		String str = "[";
		int i = 0;
		for (RunCWorkticketBusiStatus cmodel : list) {
			i++;
			str += "[\"" + cmodel.getWorkticketStausId() + "\",\""
					+ cmodel.getWorkticketStatusName() + "\"]";
			if (i < list.size()) {
				str += ",";
			}
		}
		str += "]";
		System.out.println(str);
	}
}
