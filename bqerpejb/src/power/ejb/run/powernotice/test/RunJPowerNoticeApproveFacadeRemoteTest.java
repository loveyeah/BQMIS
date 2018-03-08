package power.ejb.run.powernotice.test;

import static org.junit.Assert.*;

import java.text.ParseException;

import org.junit.BeforeClass;
import org.junit.Test;

import power.ear.comm.ejb.Ejb3Factory;
import power.ejb.run.powernotice.RunJPowerNoticeApproveFacadeRemote;
import power.ejb.run.powernotice.form.PowerNoticeForPrint;

public class RunJPowerNoticeApproveFacadeRemoteTest {
	protected static RunJPowerNoticeApproveFacadeRemote remote;
	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
		remote=(RunJPowerNoticeApproveFacadeRemote)Ejb3Factory.getInstance().getFacadeRemote("RunJPowerNoticeApproveFacade");
	}

	@Test
	public void testFindByNoForPrint() throws ParseException {
		PowerNoticeForPrint model=remote.findByNoForPrint("hfdc","T200902005");
		System.out.println(model.getDeptName());
	}

}
