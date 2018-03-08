package power.ejb.workticket.test;

import static org.junit.Assert.*;

import org.junit.BeforeClass;
import org.junit.Test;

import power.ear.comm.ejb.Ejb3Factory;
import power.ejb.workticket.business.WorkticketDangerPrint;
import power.ejb.workticket.form.WorkticketDangerForPrint;

public class WorkticketDangerPrintTest {

	private static WorkticketDangerPrint remote;
	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
		remote=(WorkticketDangerPrint)Ejb3Factory.getInstance().getFacadeRemote("WorkticketDangerPrintImpl");
	}

	@Test
	public void testGetDangerInfo() {
		WorkticketDangerForPrint model=remote.getDangerInfo("H1200901C0014");
		System.out.println(model.getChargeBy());
	}

}
