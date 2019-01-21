package com.tmb.ecert.batchjob.dao;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import com.tmb.ecert.batchjob.domain.EcmMasterData;

@Repository
public class EcmMasterDataDao {
	
	@Autowired
	private JdbcTemplate jdbcTemplate;
	
	private final String INSERT_ECERT_ECM_MASTERDATA = " INSERT INTO ECERT_ECM_MASTERDATA  (REC_INDICATOR,SOURCE,CHANNEL,SEGMENT,TYPE_CODE,TYPE_NAME_TH,TYPE_NAME_EN,TYPE_SHORT_NAME,DOC_LOCATION,DOMAIN_FOLDER, " + 
			"DIMENSION_FOLDER,FUNCTION_FOLDER,ARCHIVAL_PERIOD,DISPOSAL_PERIOD,EXPIRY_PERIOD,REPOSITORY,DOC_TEMPLATE) " + 
			"VALUES(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?) ";
	
	private final String DELETE_ECERT_ECM_MASTERDATA = " DELETE FROM ECERT_ECM_MASTERDATA  ";
	
	
	public void insertECMMasterData(List<EcmMasterData> listMaster) {
		List<Object[]> batchObj = new ArrayList<>();
		
		for (EcmMasterData master : listMaster) {
			batchObj.add(new Object[] {
				master.getRecIndicator(),
				master.getSource(),
				master.getChannel(),
				master.getSegment(),
				master.getTypeCode(),
				master.getTypeNameTh(),
				master.getTypeNameEn(),
				master.getTypeShortName(),
				master.getDocLocation(),
				master.getDomainFolder(),
				master.getDimensionFolder(),
				master.getFunctionFolder(),
				master.getArchivalPeriod(),
				master.getDisposalPeriod(),
				master.getExpiryPeriod(),
				master.getRepository(),
				master.getDocTemplate()
			});
		}
		jdbcTemplate.batchUpdate(this.INSERT_ECERT_ECM_MASTERDATA, batchObj);
	}
	
	public void deleteECMMasterData() {
		jdbcTemplate.update(this.DELETE_ECERT_ECM_MASTERDATA);
	}

}
