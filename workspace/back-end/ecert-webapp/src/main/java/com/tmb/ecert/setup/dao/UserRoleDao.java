package com.tmb.ecert.setup.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Types;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.PreparedStatementCreator;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import com.tmb.ecert.common.constant.ProjectConstant.APPLICATION_LOG_NAME;
import com.tmb.ecert.common.domain.RoleVo;
import com.tmb.ecert.report.persistence.vo.Rep02100Vo;
import com.tmb.ecert.requestorform.service.RequestorFormService;
import com.tmb.ecert.setup.vo.Sup01100FormVo;
import com.tmb.ecert.setup.vo.Sup01100Vo;

@Repository
public class UserRoleDao {
	
	@Autowired
	private JdbcTemplate jdbcTemplate;
	
	private static final Logger logger = LoggerFactory.getLogger(APPLICATION_LOG_NAME.ECERT_ROLEMANAGEMENT);
	
	private static String INSERT_SQL = "  INSERT INTO ECERT_ROLE(ROLE_NAME,STATUS,CREATED_BY_ID,CREATED_BY_NAME " + 
			" ,CREATED_DATETIME ) VALUES ( ?, ?, ?, ?, GETDATE() ) ";
	
	
	
	public Long createUserRole( Sup01100FormVo form,String username , String userID) {
//		jdbcTemplate.batchUpdate(sql, batchArgs)
		KeyHolder holder = new GeneratedKeyHolder();
		jdbcTemplate.update(new PreparedStatementCreator() {
			@Override
			public PreparedStatement createPreparedStatement(Connection connection) throws SQLException {
				PreparedStatement ps = connection.prepareStatement(INSERT_SQL, Statement.RETURN_GENERATED_KEYS);
				ps.setString(1, form.getRoleName());
				ps.setInt(2, form.getStatus());
				ps.setString(3, userID);
				ps.setString(4, username);
				return ps;
			}
		}, holder);

		Long newKey= holder.getKey().longValue();
//		user.setId(newUserId);
		return newKey;
	}
	
	public List<RoleVo> getRole(Sup01100FormVo form){
		StringBuilder sql = new StringBuilder("");
		List<Object> params = new ArrayList<>();
		
		List<RoleVo> list = new ArrayList<>();
		
		sql.append(" SELECT ROLE_ID,ROLE_NAME,STATUS FROM ECERT_ROLE WHERE 1 = 1 ");
		
		if (StringUtils.isNotBlank(form.getRoleName())) {
			sql.append(" AND  ROLE_NAME LIKE ? ");
			params.add("%"+StringUtils.trim(form.getRoleName())+"%");
		}
		if(!(form.getStatus() == 2)) {
			sql.append(" AND  STATUS =  ? ");
			params.add(form.getStatus());
		}
		
		sql.append(" ORDER BY CREATED_DATETIME ");
	
		list = jdbcTemplate.query(sql.toString(), params.toArray(), sup01000RowMapper);
		return list;
		
	}
	
	private  RowMapper<RoleVo> sup01000RowMapper = new RowMapper<RoleVo>() {

		@Override
		public RoleVo mapRow(ResultSet rs, int rowNum) throws SQLException {
			RoleVo vo = new RoleVo();
			vo.setRoldId(rs.getLong("ROLE_ID"));
			vo.setRoleName(rs.getString("ROLE_NAME"));
			vo.setStatus(rs.getInt("STATUS"));

			return vo;
		}
		
	};
	
	public void createRolePermission(final List<Sup01100Vo> permissionList ,String username , String userID) {
		StringBuilder sql = new StringBuilder(""); 
		sql.append("  INSERT INTO ECERT_ROLE_PERMISSION ( ROLE_ID , FUNCTION_CODE , STATUS ,CREATED_BY_ID, CREATED_BY_NAME ,CREATED_DATETIME )");
		sql.append(" VALUES ( ?, ?, ?, ?, ?,  GETDATE() )  ");
		jdbcTemplate.batchUpdate(sql.toString(), new BatchPreparedStatementSetter() {
			
			@Override
			public void setValues(PreparedStatement ps, int i) throws SQLException {
				Sup01100Vo vo = permissionList.get(i);
				ps.setLong(1, vo.getRoleId());
				ps.setString(2, vo.getFunctionCode());
				ps.setInt(3, vo.getStatus());
				ps.setString(4, userID);
				ps.setString(5, username);
			}
			
			@Override
			public int getBatchSize() {
				return permissionList.size();
			}
		});
	}
	
	public List<Sup01100Vo> getListPermissionByRoleID (Long roleID){
		StringBuilder sql = new StringBuilder("");
		List<Object> params = new ArrayList<>();
		
		List<Sup01100Vo> list = new ArrayList<>();
		sql.append(" SELECT a.ROLE_PERMISSION_ID , a.FUNCTION_CODE, a.STATUS  "
				+ " FROM ECERT_ROLE_PERMISSION a RIGHT JOIN  ECERT_ROLE b ON  a.ROLE_ID = b.ROLE_ID WHERE b.ROLE_ID = ? ");
		params.add(roleID);

		list = jdbcTemplate.query(sql.toString(), params.toArray(), sup01010RowMapper);
		return list;
	}
	
	private RowMapper<Sup01100Vo> sup01010RowMapper = new RowMapper<Sup01100Vo>() {

		@Override
		public Sup01100Vo mapRow(ResultSet rs, int rowNum) throws SQLException {
			Sup01100Vo vo = new Sup01100Vo();
			vo.setRolePermissionId(rs.getLong("ROLE_PERMISSION_ID"));
			vo.setFunctionCode(rs.getString("FUNCTION_CODE"));
			vo.setStatus(rs.getInt("STATUS"));
			return vo;
		}
		
	};
	
	public int delectRolePermissByRoleID(Long roleID) {
		StringBuilder sql = new StringBuilder("");
		List<Object> params = new ArrayList<>();
		
		sql.append(" DELETE FROM ECERT_ROLE_PERMISSION WHERE ROLE_ID = ? ");
		params.add(roleID);
		int[] types = {Types.BIGINT};
		int row = jdbcTemplate.update(sql.toString(), params.toArray(),types);
		return row;
	}
	
	public void updateRolePermissByRoleID(Sup01100FormVo form , String username , String userID ) {
		StringBuilder sql = new StringBuilder("");
		List<Object> params = new ArrayList<>();
		Long roleId = form.getRoleId();
		
		sql.append(" UPDATE ECERT_ROLE SET  STATUS = ? , UPDATED_BY_ID = ? , " + 
				" UPDATED_BY_NAME = ? , UPDATED_DATETIME = GETDATE()  WHERE 1=1 ");

//		params.add(form.getRoleName());
		params.add(form.getStatus());
		params.add(userID);
		params.add(username);
		
		if (form.getRoleId() != null) {
			sql.append(" AND ROLE_ID = ?  ");
			params.add(form.getRoleId());
		}
		if (!form.getRoleName().isEmpty()) {
			sql.append(" AND ROLE_NAME = ?  ");
			params.add(StringUtils.trim(form.getRoleName()));
		} 
		
		jdbcTemplate.update(sql.toString(), params.toArray());

	}
	
	public int validateDuplicateRoleName(Sup01100FormVo form ) {
		
		StringBuilder sql = new StringBuilder("");
		List<Object> params = new ArrayList<>();
		
		sql.append(" SELECT count(1) FROM ECERT_ROLE WHERE ROLE_NAME = ? ");
		params.add(StringUtils.trim(form.getRoleName()));
		
		return jdbcTemplate.queryForObject(sql.toString(), params.toArray(), Integer.class);
		
	}
	public List<RoleVo> getRoleInfo(Sup01100FormVo form){
		StringBuilder sql = new StringBuilder("");
		List<Object> params = new ArrayList<>();
		
		List<RoleVo> list = new ArrayList<>();
		
		sql.append(" SELECT ROLE_ID,ROLE_NAME,STATUS FROM ECERT_ROLE WHERE 1 = 1 ");
		
		if (StringUtils.isNotBlank(form.getRoleName())) {
			sql.append(" AND  ROLE_NAME = ? ");
			params.add(StringUtils.trim(form.getRoleName()));
		}


		list = jdbcTemplate.query(sql.toString(), params.toArray(), sup01000RowMapper);
		return list;
		
	}
	

}
