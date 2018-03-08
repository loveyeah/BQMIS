package power.ejb.workticket.test;

import java.util.List;

import org.junit.BeforeClass;
import org.junit.Test;

import power.ear.comm.ejb.Ejb3Factory;
import power.ejb.workticket.business.WorkticketPrint;
import power.ejb.workticket.form.SecurityMeasureCardModel;
import power.ejb.workticket.form.WorkticketDangerPointForPrintHF;
import power.ejb.workticket.form.WorkticketPrintModel;

public class WorkticketPrintTest {

	private static WorkticketPrint remote;

	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
		remote = (WorkticketPrint) Ejb3Factory.getInstance().getFacadeRemote(
				"WorkticketPrintImp");
	}

	@Test
	public void testGetWorkticketPrintInfo() {
		WorkticketPrintModel model = remote
				.getWorkticketPrintInfo("H1200903j004");
		System.out.println(model.getHistory().size());
	}

	@Test
	public void testGetSecurityMeasureCardInfo() {
		SecurityMeasureCardModel model = remote
				.getSecurityMeasureCardInfo("H1200903d005");
		System.out.println(model.getSafety().size());
	}

	@Test
	public void testgetWorkticketDangerPointForPrintHF() {
		WorkticketPrintModel model = remote
				.getWorkticketPrintInfo("H1200902C015");
		System.out.println(model.getSafety().size());
	}
}
