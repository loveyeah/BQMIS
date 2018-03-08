package power.ejb.opticket.test;

import org.junit.BeforeClass;
import org.junit.Test;

import power.ear.comm.ejb.Ejb3Factory;
import power.ejb.opticket.OpticketApprove;
import power.ejb.opticket.form.OpticketPrintModel;

public class OpticketApproveImplTest {
	static OpticketApprove bll;
	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
		bll = (OpticketApprove)Ejb3Factory.getInstance().getFacadeRemote("OpticketApproveImpl"); 
	}

//	@Test
//	public void testReportTo() {
//		bll.reportTo("S2008120001", "999999");
//	}

//	@Test
//	public void testOperatorSign() {
//		bll.operatorSign("S2008120001", "999999", 45L, "hellox");
//	}
//
//	@Test
//	public void testWatcherSign() {
//		bll.watcherSign("S2008120001", "999999", 56L, "hellox");
//	}
//
//	@Test
//	public void testDutySign() {
//		bll.operatorSign("S2008120001", "999999", 63L, "hellox");
//	}
//	@Test
//	public void testGetOpticketData(){
//		OpticketPrintModel model=bll.getOpticketData("S2008120001");
//		System.out.print(model.getList().size());
//		System.out.print(model.getModel().getSpecialityName());
//	}
}
