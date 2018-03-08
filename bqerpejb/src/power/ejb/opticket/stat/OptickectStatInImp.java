package power.ejb.opticket.stat;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.ejb.EJB;
import javax.ejb.Stateless;

import power.ejb.comm.NativeSqlHelperRemote;
import power.ejb.opticket.form.OptickectQuyStat;
import power.ejb.opticket.form.OptickectStatuStat;

@Stateless
public class OptickectStatInImp implements OptickectStatInterf {

	@EJB(beanName = "NativeSqlHelper")
	protected NativeSqlHelperRemote bll;

	public List<OptickectStatuStat> getOptickectStatuStat(String yearMonth,
			String spacialCode, String enterpriseCode) {
		String sql = "select decode(v4.tp,'00','电气操作票','热机操作票'), v4.minno, v4.maxno, nvl(v1.c1,0), nvl(v2.c2,0), nvl(v3.c3,0),nvl(v4.c4,0)\n"
				+ "  from (select count(1) c1, substr(t.opticket_type, 0, 2) tp\n"
				+ "          from run_j_opticket t\n"
				+ "         where to_char(t.create_date, 'yyyy-MM') = '"
				+ yearMonth
				+ "'\n"
				+ "           and t.speciality_code like '"
				+ spacialCode
				+ "'\n"
				+ "           and t.is_use = 'Y'\n"
				+ "           and t.is_standar = 'N'\n"
				+ "           and t.enterprise_code = '"
				+ enterpriseCode
				+ "'\n"
				+ "           and t.opticket_status = '3'\n"
				+ "         group by substr(t.opticket_type, 0, 2)) v1,\n"
				+ "       (select count(1) c2, substr(t.opticket_type, 0, 2) tp\n"
				+ "          from run_j_opticket t\n"
				+ "         where to_char(t.create_date, 'yyyy-MM') = '"
				+ yearMonth
				+ "'\n"
				+ "           and t.speciality_code like '"
				+ spacialCode
				+ "'\n"
				+ "           and t.is_use = 'Y'\n"
				+ "           and t.is_standar = 'N'\n"
				+ "           and t.enterprise_code = '"
				+ enterpriseCode
				+ "'\n"
				+ "           and t.opticket_status = '5'\n"
				+ "         group by substr(t.opticket_type, 0, 2)) v2,\n"
				+ "\n"
				+ "       (select count(1) c3, substr(t.opticket_type, 0, 2) tp\n"
				+ "          from run_j_opticket t\n"
				+ "         where to_char(t.create_date, 'yyyy-MM') = '"
				+ yearMonth
				+ "'\n"
				+ "           and t.speciality_code like '"
				+ spacialCode
				+ "'\n"
				+ "           and t.is_use = 'Y'\n"
				+ "           and t.is_standar = 'N'\n"
				+ "           and t.enterprise_code = '"
				+ enterpriseCode
				+ "'\n"
				+ "           and t.opticket_status = 'Z'\n"
				+ "         group by substr(t.opticket_type, 0, 2)) v3,\n"
				+ "       (select substr(t.opticket_type, 0, 2) tp,count(1) c4,\n"
				+ "               '"
				+ yearMonth.replace("-", "")
				+ "' || min(substr(t.opticket_code, 10, 4)) minno,\n"
				+ "               '"
				+ yearMonth.replace("-", "")
				+ "' || max(substr(t.opticket_code, 10, 4)) maxno\n"
				+ "          from run_j_opticket t\n"
				+ "         where to_char(t.create_date, 'yyyy-MM') = '"
				+ yearMonth
				+ "'\n"
				+ "           and t.speciality_code like '"
				+ spacialCode
				+ "'\n"
				+ "           and t.is_use = 'Y'\n"
				+ "           and t.is_standar = 'N'\n"
				+ "           and t.enterprise_code = '"
				+ enterpriseCode
				+ "'\n"
				+ " 		and t.opticket_status <> 'Z'"// modify by ywliu 20091231 去掉作废状态, 'Z'
				+ "         group by substr(t.opticket_type, 0, 2)) v4\n"
				+ " where v4.tp = v2.tp(+)\n"
				+ "   and v4.tp = v3.tp(+)\n"
				+ "   and v4.tp = v1.tp(+)\n" + "\n" + "\n" + "";
		List list = bll.queryByNativeSQL(sql);
		if (list != null) {
			List<OptickectStatuStat> arr = new ArrayList<OptickectStatuStat>();
			Iterator it = list.iterator();
			while (it.hasNext()) {
				Object[] ob = (Object[]) it.next();
				OptickectStatuStat model = new OptickectStatuStat();
				if (ob[0] != null) {
					model.setOptickectType(ob[0].toString());
				}
				if (ob[1] != null)
					model.setBeginNo(ob[1].toString());
				if (ob[2] != null)
					model.setEndNo(ob[2].toString());
				if (ob[3] != null)
					model.setBeginWorkCount(Integer.parseInt(ob[3].toString()));
				if (ob[4] != null)
					model.setEndCount(Integer.parseInt(ob[4].toString()));
				if (ob[5] != null)
					model.setInvaliCount(Integer.parseInt(ob[5].toString()));
				if (ob[6] != null)
					model.setTotlaNum(Integer.parseInt(ob[6].toString()));
				arr.add(model);
			}
			return arr;
		} else
			return null;
	}

	public List<OptickectQuyStat> getOptickectQuyStat(String yearMonth,
			String spacialCode, String enterpriseCode) {
		String sql = "select decode(v.tp,'00','电气操作票','热机操作票'),v6.minno, v6.maxno,nvl(v.c1,0), nvl(v1.c2,0), nvl(v2.c3,0),  nvl(v4.c5,0),nvl(v5.c6,0)\n"
				+ "  from (select substr(t.opticket_type, 0, 2) tp, count(1) c1\n"
				+ "          from run_j_opticket t\n"
				+ "         where to_char(t.create_date, 'yyyy-MM') = '"
				+ yearMonth
				+ "'\n"
				+ "           and t.is_standar = 'N'\n"
				+ "           and t.is_use = 'Y'\n"
				+ "           and t.enterprise_code = '"
				+ enterpriseCode
				+ "'\n"
				+ "           and t.speciality_code like '"
				+ spacialCode
				+ "'\n"
				+ "           and t.opticket_status in ('5', 'Z')\n"
				+ "         group by substr(t.opticket_type, 0, 2)) v,\n"
				+ "\n"
				+ "       (select substr(t.opticket_type, 0, 2) tp, count(1) c2\n"
				+ "          from run_j_opticket t\n"
				+ "         where to_char(t.create_date, 'yyyy-MM') = '"
				+ yearMonth
				+ "'\n"
				+ "           and t.is_standar = 'N'\n"
				+ "           and t.is_use = 'Y'\n"
				+ "           and t.enterprise_code = '"
				+ enterpriseCode
				+ "'\n"
				+ "           and t.speciality_code like '"
				+ spacialCode
				+ "'\n"
				+ "           and t.opticket_status = '5'\n"
				+ "         group by substr(t.opticket_type, 0, 2)) v1,\n"
				+ "\n"
				+ "       (select substr(t.opticket_type, 0, 2) tp, count(1) c3\n"
				+ "          from run_j_opticket t\n"
				+ "         where to_char(t.create_date, 'yyyy-MM') = '"
				+ yearMonth
				+ "'\n"
				+ "           and t.is_standar = 'N'\n"
				+ "           and t.is_use = 'Y'\n"
				+ "           and t.enterprise_code = '"
				+ enterpriseCode
				+ "'\n"
				+ "           and t.speciality_code like '"
				+ spacialCode
				+ "'\n"
				+ "           and t.opticket_status = 'Z'\n"
				+ "         group by substr(t.opticket_type, 0, 2)) v2,\n"
				+ "\n"
				+ "       (select count(1) c5, substr(t.opticket_type, 0, 2) tp\n"
				+ "          from run_j_opticket t\n"
				+ "         where substr(t.opticket_code, length(t.opticket_code), 1) = 'B'\n"
				+ "           and to_char(t.create_date, 'yyyy-MM') = '"
				+ yearMonth
				+ "'\n"
				+ "           and t.is_standar = 'N'\n"
				+ "           and t.is_use = 'Y'\n"
				+ "           and t.enterprise_code = '"
				+ enterpriseCode
				+ "'\n"
				+ "           and t.speciality_code like '"
				+ spacialCode
				+ "'\n"
				+ "           and t.opticket_status in ('5')\n"// modify by ywliu 20091231 去掉作废状态, 'Z'
				+ "         group by substr(t.opticket_type, 0, 2)) v4,\n"
				+ "\n"
				+ "       (select count(1) c6, substr(t.opticket_type, 0, 2) tp\n"
				+ "          from run_j_opticket t\n"
				+ "         where substr(t.opticket_code, length(t.opticket_code), 1) = 'B'\n"
				+ "           and to_char(t.create_date, 'yyyy-MM') = '"
				+ yearMonth
				+ "'\n"
				+ "           and t.is_standar = 'N'\n"
				+ "           and t.is_use = 'Y'\n"
				+ "           and t.enterprise_code = '"
				+ enterpriseCode
				+ "'\n"
				+ "           and t.speciality_code like '"
				+ spacialCode
				+ "'\n"
				+ "           and t.opticket_status = '5'\n"
				+ "         group by substr(t.opticket_type, 0, 2)) v5,\n"
				+ "\n"
				+ "       (select substr(t.opticket_type, 0, 2) tp,\n"
				+ "               '"
				+ yearMonth.replace("-", "")
				+ "' || min(substr(t.opticket_code, 10, 4)) minno,\n"
				+ "               '"
				+ yearMonth.replace("-", "")
				+ "' || max(substr(t.opticket_code, 10, 4)) maxno\n"
				+ "\n"
				+ "          from run_j_opticket t\n"
				+ "         where to_char(t.create_date, 'yyyy-MM') = '"
				+ yearMonth
				+ "'\n"
				+ "           and t.speciality_code like '"
				+ spacialCode
				+ "'\n"
				+ "           and t.is_use = 'Y'\n"
				+ "           and t.is_standar = 'N'\n"
				+ "           and t.enterprise_code = '"
				+ enterpriseCode
				+ "'\n"
				+ "         group by substr(t.opticket_type, 0, 2)) v6\n"
				+ "\n"
				+ " where v6.tp = v1.tp(+)\n"
				+ "   and v6.tp = v2.tp(+)\n"
				+ "   and v6.tp = v4.tp(+)\n"
				+ "   and v6.tp = v5.tp(+)\n"
				+ "   and v6.tp = v.tp(+)";
		List list = bll.queryByNativeSQL(sql);
		if (list != null) {
			List<OptickectQuyStat> arr = new ArrayList<OptickectQuyStat>();
			Iterator it = list.iterator();
			while (it.hasNext()) {
				Object[] ob = (Object[]) it.next();
				OptickectQuyStat model = new OptickectQuyStat();
				if (ob[0] != null)
					model.setOptickectType(ob[0].toString());
				if (ob[1] != null)
					model.setBeginNo(ob[1].toString());
				if (ob[2] != null)
					model.setEndNo(ob[2].toString());
				if (ob[3] != null)
					model.setStatCount(Integer.parseInt(ob[3].toString()));
				if (ob[4] != null)
					model.setQuyCount(Integer.parseInt(ob[4].toString()));
				if (ob[5] != null)
					model.setInvaliCount(Integer.parseInt(ob[5].toString()));
				if (ob[6] != null)
					model
							.setUseStandOpCount(Integer.parseInt(ob[6]
									.toString()));
				if (ob[7] != null)
					model.setUseStandOpQuyCount(Integer.parseInt(ob[7]
							.toString()));
				arr.add(model);
			}
			return arr;
		} else
			return null;

	}

}
