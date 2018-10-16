package com.tmb.ecert.common.dao;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import com.tmb.ecert.common.utils.SQLUtils;

@Repository
public class UserProfileDao {

	@Autowired
	private JdbcTemplate jdbcTemplate;

	public List<String> getAuthbyRole(List<String> roles) {
		StringBuilder sql = new StringBuilder();
		sql.append(" select p.FUNCTION_CODE from  dbo.ECERT_ROLE r              ");
		sql.append(" join dbo.ECERT_ROLE_PERMISSION p on r.ROLE_ID=p.ROLE_ID    ");
		sql.append(
				"  where r.ROLE_NAME in ( " + SQLUtils.whereinTypeStringSQL(roles) + ")                            ");
		sql.append(" AND p.status = 0 group by p.FUNCTION_CODE                                  ");

		return jdbcTemplate.queryForList(sql.toString(), String.class);
	}

}
