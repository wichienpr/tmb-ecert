package th.co.baiwa.buckwaframework.common.persistence.jdbc;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Types;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.IncorrectResultSizeDataAccessException;
import org.springframework.dao.support.DataAccessUtils;
import org.springframework.jdbc.core.ColumnMapRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.ParameterizedPreparedStatementSetter;
import org.springframework.jdbc.core.PreparedStatementCreator;
import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.SqlParameterValue;
import org.springframework.jdbc.datasource.DataSourceUtils;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.JdbcUtils;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.transaction.annotation.Transactional;

/**
 * Common set of JDBC operations.
 * Implemented cover {@link JdbcTemplate} for easy to use.
 * 
 * @author: Taechapon Himarat (Su)
 * @since: Sep 7, 2012
 * @see JdbcTemplate
 */
public class CommonJdbcTemplate {
	
	private static final Logger logger = LoggerFactory.getLogger(CommonJdbcTemplate.class);
	
	private JdbcTemplate jdbcTemplate;
	
	public CommonJdbcTemplate(JdbcTemplate jdbcTemplate) {
		this.jdbcTemplate = jdbcTemplate;
	}
	
	public JdbcTemplate getJdbcTemplate() {
		return jdbcTemplate;
	}
	
	//--------------------------------------------------------------------------------
	// Methods dealing with select prepared statements
	//--------------------------------------------------------------------------------
	
	/**
	 * Query given SQL to create a prepared statement from SQL and a list
	 * of arguments to bind to the query, reading the ResultSet with a
	 * ResultSetExtractor.
	 * @param sql SQL query to execute
	 * @param params arguments to bind to the query
	 * (leaving it to the PreparedStatement to guess the corresponding SQL type);
	 * may also contain {@link SqlParameterValue} objects which indicate not
	 * only the argument value but also the SQL type and optionally the scale
	 * @param extractor object that will extract results
	 * @return an arbitrary result object, as returned by the ResultSetExtractor
	 * @throws DataAccessException if the query fails
	 */
	public <T> T executeQuery(String sql, Object[] params, ResultSetExtractor<T> extractor) throws DataAccessException {
		return jdbcTemplate.query(sql, params, extractor);
	}
	
	/**
	 * Execute a query given static SQL, reading the ResultSet with a
	 * ResultSetExtractor.
	 * <p>Uses a JDBC Statement, not a PreparedStatement. If you want to
	 * execute a static query with a PreparedStatement, use the overloaded
	 * {@code query} method with {@code null} as argument array.
	 * @param sql SQL query to execute
	 * @param extractor object that will extract all rows of results
	 * @return an arbitrary result object, as returned by the ResultSetExtractor
	 * @throws DataAccessException if there is any problem executing the query
	 * @see #executeQuery(String, Object[], ResultSetExtractor)
	 */
	public <T> T executeQuery(String sql, ResultSetExtractor<T> extractor) throws DataAccessException {
		return jdbcTemplate.query(sql, extractor);
	}
	
	/**
	 * Query given SQL to create a prepared statement from SQL and a list
	 * of arguments to bind to the query, mapping each row to a Java object
	 * via a RowMapper.
	 * @param sql SQL query to execute
	 * @param params arguments to bind to the query
	 * (leaving it to the PreparedStatement to guess the corresponding SQL type);
	 * may also contain {@link SqlParameterValue} objects which indicate not
	 * only the argument value but also the SQL type and optionally the scale
	 * @param rowMapper object that will map one object per row
	 * @return the result List, containing mapped objects
	 * @throws DataAccessException if the query fails
	 */
	public <T> List<T> executeQuery(String sql, Object[] params, RowMapper<T> rowMapper) throws DataAccessException {
		return jdbcTemplate.query(sql, params, rowMapper);
	}
	
	/**
	 * Execute a query given static SQL, mapping each row to a Java object
	 * via a RowMapper.
	 * <p>Uses a JDBC Statement, not a PreparedStatement. If you want to
	 * execute a static query with a PreparedStatement, use the overloaded
	 * {@code query} method with {@code null} as argument array.
	 * @param sql SQL query to execute
	 * @param rowMapper object that will map one object per row
	 * @return the result List, containing mapped objects
	 * @throws DataAccessException if there is any problem executing the query
	 * @see #executeQuery(String, Object[], RowMapper)
	 */
	public <T> List<T> executeQuery(String sql, RowMapper<T> rowMapper) throws DataAccessException {
		return jdbcTemplate.query(sql, rowMapper);
	}
	
	/**
	 * Query given SQL to create a prepared statement from SQL and a list
	 * of arguments to bind to the query, mapping a single result row to a
	 * Java object via a RowMapper.
	 * @param sql SQL query to execute
	 * @param params arguments to bind to the query
	 * (leaving it to the PreparedStatement to guess the corresponding SQL type);
	 * may also contain {@link SqlParameterValue} objects which indicate not
	 * only the argument value but also the SQL type and optionally the scale
	 * @param rowMapper object that will map one object per row
	 * @return the single result object, or {@code null} if none
	 * @throws DataAccessException if the query fails
	 */
	public <T> T executeQueryForObject(String sql, Object[] params, RowMapper<T> rowMapper) throws DataAccessException {
		return DataAccessUtils.singleResult(jdbcTemplate.query(sql, params, rowMapper));
	}
	
	/**
	 * Execute a query given static SQL, mapping a single result row to a Java
	 * object via a RowMapper.
	 * <p>Uses a JDBC Statement, not a PreparedStatement. If you want to
	 * execute a static query with a PreparedStatement, use the overloaded
	 * {@link #queryForObject(String, Object[], RowMapper)} method with
	 * {@code null} as argument array.
	 * @param sql SQL query to execute
	 * @param rowMapper object that will map one object per row
	 * @return the single result object, or {@code null} if none
	 * @throws DataAccessException if there is any problem executing the query
	 * @see #queryForObject(String, Object[], RowMapper)
	 */
	public <T> T executeQueryForObject(String sql, RowMapper<T> rowMapper) throws DataAccessException {
		return DataAccessUtils.singleResult(jdbcTemplate.query(sql, rowMapper));
	}
	
	/**
	 * Query given SQL to create a prepared statement from SQL and a
	 * list of arguments to bind to the query, expecting a result object.
	 * <p>The query is expected to be a single row/single column query; the returned
	 * result will be directly mapped to the corresponding object type.
	 * @param sql SQL query to execute
	 * @param params arguments to bind to the query
	 * (leaving it to the PreparedStatement to guess the corresponding SQL type);
	 * may also contain {@link SqlParameterValue} objects which indicate not
	 * only the argument value but also the SQL type and optionally the scale
	 * @param requiredType the type that the result object is expected to match
	 * @return the result object of the required type, or {@code null} in case of SQL NULL
	 * @throws IncorrectResultSizeDataAccessException if the query does not return
	 * exactly one row, or does not return exactly one column in that row
	 * @throws DataAccessException if the query fails
	 * @see #executeQueryForObject(String, Class)
	 */
	public <T> T executeQueryForObject(String sql, Object[] params, Class<T> requiredType) throws DataAccessException {
		return jdbcTemplate.queryForObject(sql, params, requiredType);
	}
	
	/**
	 * Execute a query for a result object, given static SQL.
	 * <p>Uses a JDBC Statement, not a PreparedStatement. If you want to
	 * execute a static query with a PreparedStatement, use the overloaded
	 * {@link #executeQueryForObject(String, Object[], Class)} method with
	 * {@code null} as argument array.
	 * <p>This method is useful for running static SQL with a known outcome.
	 * The query is expected to be a single row/single column query; the returned
	 * result will be directly mapped to the corresponding object type.
	 * @param sql SQL query to execute
	 * @param requiredType the type that the result object is expected to match
	 * @return the result object of the required type, or {@code null} in case of SQL NULL
	 * @throws IncorrectResultSizeDataAccessException if the query does not return
	 * exactly one row, or does not return exactly one column in that row
	 * @throws DataAccessException if there is any problem executing the query
	 * @see #executeQueryForObject(String, Object[], Class)
	 */
	public <T> T executeQueryForObject(String sql, Class<T> requiredType) throws DataAccessException {
		return jdbcTemplate.queryForObject(sql, requiredType);
	}
	
	/**
	 * Query given SQL to create a prepared statement from SQL and a
	 * list of arguments to bind to the query, expecting a result Map.
	 * The queryForMap() methods defined by this interface are appropriate
	 * when you don't have a domain model. Otherwise, consider using
	 * one of the queryForObject() methods.
	 * <p>The query is expected to be a single row query; the result row will be
	 * mapped to a Map (one entry for each column, using the column name as the key).
	 * @param sql SQL query to execute
	 * @param params arguments to bind to the query
	 * (leaving it to the PreparedStatement to guess the corresponding SQL type);
	 * may also contain {@link SqlParameterValue} objects which indicate not
	 * only the argument value but also the SQL type and optionally the scale
	 * @return the result Map (one entry for each column, using the
	 * column name as the key), or {@code null} if none
	 * @throws DataAccessException if the query fails
	 * @see ColumnMapRowMapper
	 */
	public Map<String, Object> executeQueryForMap(String sql, Object... params) throws DataAccessException {
		return DataAccessUtils.singleResult(jdbcTemplate.query(sql, params, new ColumnMapRowMapper()));
	}
	
	/**
	 * Query given SQL to create a prepared statement from SQL and a
	 * list of arguments to bind to the query, expecting a result list.
	 * <p>The results will be mapped to a List (one entry for each row) of
	 * Maps (one entry for each column, using the column name as the key).
	 * Each element in the list will be of the form returned by this interface's
	 * executeQueryForMap() methods.
	 * @param sql SQL query to execute
	 * @param params arguments to bind to the query
	 * (leaving it to the PreparedStatement to guess the corresponding SQL type);
	 * may also contain {@link SqlParameterValue} objects which indicate not
	 * only the argument value but also the SQL type and optionally the scale
	 * @return a List that contains a Map per row
	 * @throws DataAccessException if the query fails
	 */
	public List<Map<String, Object>> executeQueryForList(String sql, Object... params) throws DataAccessException {
		return jdbcTemplate.queryForList(sql, params);
	}

	//--------------------------------------------------------------------------------
	// Methods dealing with insert prepared statements
	//--------------------------------------------------------------------------------
	
	/**
	 * Single SQL insert statement via a prepared statement, binding the given arguments.
	 * @param sql SQL containing bind parameters
	 * @param params arguments to bind to the query
	 * (leaving it to the PreparedStatement to guess the corresponding SQL type);
	 * may also contain {@link SqlParameterValue} objects which indicate not
	 * only the argument value but also the SQL type and optionally the scale
	 * @return the number of rows affected
	 * @throws DataAccessException if there is any problem issuing the insert
	 */
	public int executeInsert(final String sql, final Object... params) throws DataAccessException {
		return jdbcTemplate.update(new PreparedStatementCreator() {
			public PreparedStatement createPreparedStatement(Connection connection) throws SQLException {
				PreparedStatement ps = connection.prepareStatement(sql);
				preparePs(ps, params);
				return ps;
			}
		});
	}
	
	/**
	 * Single SQL insert statement via a prepared statement, binding the given arguments.
	 * Generated keys will be put into the given KeyHolder.
	 * @param sql SQL containing bind parameters
	 * @param params arguments to bind to the query
	 * (leaving it to the PreparedStatement to guess the corresponding SQL type);
	 * may also contain {@link SqlParameterValue} objects which indicate not
	 * only the argument value but also the SQL type and optionally the scale
	 * @return the long value of KeyHolder
	 * @throws DataAccessException if there is any problem issuing the insert
	 */
	public Long executeInsertWithKeyHolder(final String sql, final Object... params) throws DataAccessException {
		KeyHolder keyHolder = new GeneratedKeyHolder();
		jdbcTemplate.update(new PreparedStatementCreator() {
			public PreparedStatement createPreparedStatement(Connection connection) throws SQLException {
				PreparedStatement ps = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
				preparePs(ps, params);
				return ps;
			}
		}, keyHolder);
		Long key = keyHolder.getKey().longValue();
		
		return key;
	}
	
	/**
	 * Prepare PreparedStatement from Array of Object following Object Type.
	 * @param ps the PreparedStatement Object
	 * @param params Array of Object will be set in PreparedStatement
	 */
	public void preparePs(PreparedStatement ps, Object[] params) throws SQLException {
		int i = 1;
		for (Object o : params) {
			if (o instanceof String) {
				ps.setString(i, (String) o);
			} else if (o instanceof java.sql.Timestamp) {
				ps.setTimestamp(i, (java.sql.Timestamp) o);
			} else if (o instanceof java.sql.Date) {
				ps.setDate(i, (java.sql.Date) o);
			} else if (o instanceof java.sql.Time) {
				ps.setTime(i, (java.sql.Time) o);
			} else if (o instanceof java.util.Date) {
				ps.setTimestamp(i, new java.sql.Timestamp(((java.util.Date) o).getTime()));
			} else if (o instanceof Byte) {
				ps.setByte(i, (Byte) o);
			} else if (o instanceof Short) {
				ps.setShort(i, (Short) o);
			} else if (o instanceof Integer) {
				ps.setInt(i, (Integer) o);
			} else if (o instanceof Long) {
				ps.setLong(i, (Long) o);
			} else if (o instanceof Float) {
				ps.setFloat(i, (Float) o);
			} else if (o instanceof Double) {
				ps.setDouble(i, (Double) o);
			} else if (o instanceof BigDecimal) {
				ps.setBigDecimal(i, (BigDecimal) o);
			} else if (o instanceof Object) {
				ps.setObject(i, o);
			} else if (o == null) {
				ps.setNull(i, Types.NULL);
			}
			i++;
		}
	}

	//--------------------------------------------------------------------------------
	// Methods dealing with update, delete prepared statements
	//--------------------------------------------------------------------------------
	
	/**
	 * Single SQL update operation (such as an update or delete statement)
	 * via a prepared statement, binding the given arguments.
	 * @param sql SQL containing bind parameters
	 * @param params arguments to bind to the query
	 * (leaving it to the PreparedStatement to guess the corresponding SQL type);
	 * may also contain {@link SqlParameterValue} objects which indicate not
	 * only the argument value but also the SQL type and optionally the scale
	 * @return the number of rows affected
	 * @throws DataAccessException if there is any problem issuing the update
	 */
	public int executeUpdate(String sql, Object... params) throws DataAccessException {
		return jdbcTemplate.update(sql, params);
	}

	//--------------------------------------------------------------------------------
	// Methods dealing with batch prepared statements
	//--------------------------------------------------------------------------------
	
	/**
	 * Execute a batch using the supplied SQL statement with the batch of supplied arguments.
	 * <p>Note Recommend using with {@link Transactional @Transactional} in Service Layer for fully batch process.
	 * @param sql the SQL statement to execute
	 * @param pss object to set parameters on the PreparedStatement
	 * @return an array containing the numbers of rows affected by each round update in the batch
	 */
	public <T> int[][] executeBatch(String sql, final Collection<T> batchArgs, final int batchSize,
			final ParameterizedPreparedStatementSetter<T> pss) throws SQLException {
		logger.debug("Batch Process Start");
		long start = System.currentTimeMillis();
		
		Connection connection = null;
		PreparedStatement ps = null;
		List<int[]> rowsAffected = new ArrayList<int[]>();
		int[][] result = null;
		try {
			boolean batchSupported = true;
			connection = DataSourceUtils.getConnection(jdbcTemplate.getDataSource());
			if (!JdbcUtils.supportsBatchUpdates(connection)) {
				batchSupported = false;
				logger.warn("JDBC Driver does not support Batch updates; resorting to single statement execution");
			}
			
			ps = connection.prepareStatement(sql.toString());
			if (batchSupported) {
				int count = 0;
				for (T obj : batchArgs) {
					count++;
					pss.setValues(ps, obj);
					ps.addBatch();
					if (count % batchSize == 0) {
						logger.debug("## ps.executeBatch() at count={}", count);
						count = 0;
						rowsAffected.add(ps.executeBatch());
						ps.clearBatch();
					}
				}
				if (count > 0) {
					logger.debug("## ps.executeBatch() at count={}", count);
					rowsAffected.add(ps.executeBatch());
					ps.clearBatch();
				}
			} else {
				for (T obj : batchArgs) {
					pss.setValues(ps, obj);
					int i = ps.executeUpdate();
					rowsAffected.add(new int[] {i});
				}
			}
			
			result = new int[rowsAffected.size()][];
			for (int i = 0; i < result.length; i++) {
				result[i] = rowsAffected.get(i);
			}
		} catch (SQLException e) {
			throw e;
		} finally {
			JdbcUtils.closeStatement(ps);
			DataSourceUtils.releaseConnection(connection, jdbcTemplate.getDataSource());
		}
		
		long end = System.currentTimeMillis();
		logger.debug("Batch Process Success, using {} seconds", (float) (end - start) / 1000F);
		return result;
	}
	
}
