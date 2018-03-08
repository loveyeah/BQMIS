package power.ejb.opticket.test;

import static org.junit.Assert.*;

import java.util.List;

import org.junit.BeforeClass;
import org.junit.Test;

import power.ear.comm.ejb.Ejb3Factory;
import power.ejb.opticket.OpticketApprove;
import power.ejb.opticket.form.CheckBaseForPrint;
import power.ejb.opticket.form.DangerousBaseForPrint;
import power.ejb.opticket.form.OpticketPrintModel;
import power.ejb.opticket.form.WorkBaseForPrint;

public class OpticketApproveTest {
	protected static OpticketApprove bll;
	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
		bll = (OpticketApprove)Ejb3Factory.getInstance().getFacadeRemote("OpticketApproveImpl");
	}

	@Test
	public void testGetOpticketData() {
		OpticketPrintModel model=bll.getOpticketData("H2009020014");
		if(model!=null){
			System.out.print(model.getList().size());
			System.out.print(model.getModel().getChargeName());
		}else
			System.out.print("null");	
	}

	@Test
	public void testGetBefCheckStepData() {
		CheckBaseForPrint model=bll.getBefCheckStepData("H2009020014");
		if(model!=null){
			System.out.print(model.getList().size());
			System.out.print(model.getModel().getChargeName());
		}else
			System.out.print("null");	
	}

	@Test
	public void testGetAftWorkData() {
		WorkBaseForPrint model=bll.getAftWorkData("H2009020014");
		if(model!=null){
			System.out.print(model.getList().size());
			System.out.print(model.getModel().getChargeName());
		}else
			System.out.print("null");	
	}

	@Test
	public void testGetDangeroursData() {
		DangerousBaseForPrint model=bll.getDangeroursData("H2009020005");
		if(model!=null){
			System.out.print(model.getList().size());
			System.out.print(model.getModel().getChargeName());
		}else
			System.out.print("null");	
	}
}
