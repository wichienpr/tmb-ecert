package th.co.baiwa.buckwaframework.common.util;

public class OracleUtils {

	public static String limit(String sql , int min , int max) {
		StringBuilder sqlBuilger = new StringBuilder();                              
		sqlBuilger.append("	select *  from ( select                           ");
		sqlBuilger.append("	  a.*, ROWNUM rnum                                ");
		sqlBuilger.append("	      from (                                      ");
		sqlBuilger.append(sql);
		sqlBuilger.append("	      ) a                                         ");
		sqlBuilger.append("	      where ROWNUM <=  ").append(max).append(" )  ");
		sqlBuilger.append("	where rnum  >= ").append(min).append("            ");
		
		return sqlBuilger.toString();
	}
	
	public static String limitForDataTable(String sql , long start , long length ) {
			int min = (int)start + 1;
			int max = (int) (start +  (int)length);
			return limit(sql, min, max);
	}
	
	public static String limitForDataTable(StringBuilder sql , long start , long length ) {
		return limitForDataTable(sql.toString(), start, length);
	}
	
	public static String countForDatatable(String sql) {
		StringBuilder sqlBuilger = new StringBuilder();                              
		sqlBuilger.append("	select count(1)  from ( ");
		sqlBuilger.append(sql.toString());
		sqlBuilger.append(" ) ");
		return sqlBuilger.toString();
	}
	
	public static String countForDatatable(StringBuilder sql) {
		return countForDatatable(sql.toString());
	}
	
	
}
