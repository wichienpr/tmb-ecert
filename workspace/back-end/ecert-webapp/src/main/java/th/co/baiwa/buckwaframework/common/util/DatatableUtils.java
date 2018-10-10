package th.co.baiwa.buckwaframework.common.util;

public class DatatableUtils {

	public static String countForDatatable(String sql) {
		StringBuilder sqlBuilger = new StringBuilder();
		sqlBuilger.append("select count(1) from ( ");
		sqlBuilger.append(sql.toString());
		sqlBuilger.append(" ) counttb ");
		return sqlBuilger.toString();
	}

	public static String limitForDataTable(String sql, int start, int length) {
		StringBuilder sqlBuilger = new StringBuilder();
		sqlBuilger.append(sql);
		sqlBuilger.append(" OFFSET " + start + " ROWS FETCH NEXT " + length + " ROWS ONLY ");
		return sqlBuilger.toString();
	}
}
