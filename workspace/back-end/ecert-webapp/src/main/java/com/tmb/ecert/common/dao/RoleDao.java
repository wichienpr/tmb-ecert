package com.tmb.ecert.common.dao;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import com.tmb.ecert.common.domain.RoleVo;

@Repository
public class RoleDao {

	@Autowired
	private JdbcTemplate jdbcTemplate;
	
	private static String SQL = " SELECT R.ROLE_ID ROLE_ID, R.ROLE_NAME ROLE_NAME, R.STATUS STATUS, RP.ROLE_PERMISSION_ID ROLE_PERMISSION_ID, RP.FUNCTION_CODE FUNCTION_CODE FROM ECERT_ROLE R INNER JOIN ECERT_ROLE_PERMISSION RP ON R.ROLE_ID = RP.ROLE_ID WHERE 1=1 ";
	
	public List<RoleVo> findRoleJoinRolePermission() {
		StringBuilder sql = new StringBuilder(SQL);
		return jdbcTemplate.query(sql.toString(), mapping);
	}
	
	private RowMapper<RoleVo> mapping = new RowMapper<RoleVo> () {
		@Override
		public RoleVo mapRow(ResultSet rs, int arg1) throws SQLException {
			RoleVo row = new RoleVo();
			row.setRoldId(rs.getLong("ROLE_ID"));
			row.setRoleName(rs.getString("ROLE_NAME"));
			row.setStatus(rs.getInt("STATUS"));
			row.setRolePermissionId(rs.getString("ROLE_PERMISSION_ID"));
			row.setFunctionCode(rs.getString("FUNCTION_CODE"));
			return row;
		}
	};
}
