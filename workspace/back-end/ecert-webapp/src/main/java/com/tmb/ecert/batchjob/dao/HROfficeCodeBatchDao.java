package com.tmb.ecert.batchjob.dao;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import com.tmb.ecert.batchjob.domain.EcertHROfficeCode;

@Repository
public class HROfficeCodeBatchDao {
	
//	private static final Logger log = LoggerFactory.getLogger(BACHJOB_LOG_NAME.ECERT_HROFFICECODE);
	
	@Autowired
	private JdbcTemplate jdbcTemplate;
	
	private final String INSERT_ECERT_HR_OFFICE_CODE = " INSERT INTO ECERT_HR_OFFICECODE ( " +
		" EFFECTIVE_DATE , " +
		" TYPE           , " +
		" OFFICE_CODE1   , " +
		" OFFICE_CODE2   , " +
		" TDEPT_EN       , " +
		" DESCRSHORT_EN  , " +
		" TDEPT_TH       , " +
		" DESCRSHORT_TH  , " +
		" STATUS         ) " +
		" VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?) ";
	
	private final String DELETE_ECERT_HR_OFFICE_CODE = " DELETE FROM ECERT_HR_OFFICECODE ";

	public void insertEcertHROfficeCode(List<EcertHROfficeCode> ecertHROfficeCodes) {
		List<Object[]> batchObj = new ArrayList<>();
		for (EcertHROfficeCode ecertHROfficeCode : ecertHROfficeCodes) {
			batchObj.add(new Object[] {
					ecertHROfficeCode.getEffectiveDate(),
					ecertHROfficeCode.getType(),
					ecertHROfficeCode.getOfficeCode1(),
					ecertHROfficeCode.getOfficeCode2(),
					ecertHROfficeCode.getTdeptEn(),
					ecertHROfficeCode.getDescrshortEn(),
					ecertHROfficeCode.getTdeptTh(),
					ecertHROfficeCode.getDescrshortTh(),
					ecertHROfficeCode.getStatus()
			});
		}
		
		jdbcTemplate.batchUpdate(this.INSERT_ECERT_HR_OFFICE_CODE, batchObj);
	}
	
	public void deleteEcertHROfficeCode() {
		jdbcTemplate.update(this.DELETE_ECERT_HR_OFFICE_CODE);
	}
}
