package power.ejb.hr.test;

import java.util.List;

import org.junit.BeforeClass;
import org.junit.Test;

import power.ear.comm.ejb.Ejb3Factory;
import power.ejb.hr.HrCDept;
import power.ejb.hr.HrCDeptFacadeRemote;

public class HrCDeptFacadeRemoteTest {
    protected static HrCDeptFacadeRemote remote;
    
    @BeforeClass
	public static void setUpBeforeClass() throws Exception {
		remote = (HrCDeptFacadeRemote) Ejb3Factory.getInstance().getFacadeRemote("HrCDeptFacade");
	}
	@Test
	public void testGetDeptByPId() {
		List<HrCDept> list = remote.getDeptByPId(Long.parseLong("0"));
		String str = "[";
		int i = 0;
		for (HrCDept cmodel : list) {
			i++;
			str += "[\"" + cmodel.getDeptCode() + "\",\""
					+ cmodel.getDeptName() + "\"]";
			if (i < list.size()) {
				str += ",";
			}
		}
		str += "]";
		System.out.println(str);
	}

}
