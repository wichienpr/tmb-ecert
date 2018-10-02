package com.tmb.ecert.requestorform.persistence.dao;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import com.tmb.ecert.CheckRequestStatus.persistence.entity.RequestForm;

@Repository
public class RequestorDao {
	
	@Autowired
	private JdbcTemplate jdbcTemplate;

	private Logger logger = LoggerFactory.getLogger(RequestorDao.class);
	
	private final String SQL_ECERT_REQUEST_FORM = "INSERT INTO ECERT_REQUEST_FORM";
	
	public void save(RequestForm vo) {
		
		StringBuilder sql = new StringBuilder(SQL_ECERT_REQUEST_FORM);
		sql.append("(REQUEST_DATE,CERTYPE_CODE,ORGANIZE_ID,CUSTOMER_NAME,COMPANY_NAME,");
		sql.append("BRANCH,CUSTSEGMENT_CODE,CA_NUMBER,DEPARTMENT,PAIDTYPE_CODE,");
		sql.append("DEBIT_ACCOUNT_TYPE,TRANCODE,GLTYPE,ACCOUNTTYPE,ACCOUNT_NO,");
		sql.append("ACCOUNT_NAME,CUSTOMER_NAMERECEIPT,TELEPHONE,REQUESTFORM_FILE,");
		sql.append("IDCARD_FILE,CHANGENAME_FILE,CERTIFICATE_FILE,ADDRESS,");
		sql.append("REMARK,RECEIPT_NO,STATUS,CREATED_BY_ID,CREATED_BY_NAME,");
		sql.append("CREATED_DATETIME,MAKER_BY_ID,MAKER_BY_NAME,TMB_REQUESTNO");
		sql.append(") VALUES (GETDATE(),?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,GETDATE(),?,?,?)");
		
		List<Object> params = new ArrayList<>();
		params.add(vo.getCerTypeCode()); params.add(vo.getOrganizeId());
		params.add(vo.getCustomerName()); params.add(vo.getCompanyName());
		params.add(vo.getBranch()); params.add(vo.getCustsegmentCode());
		params.add(vo.getCaNumber()); params.add(vo.getDepartment());
		params.add(vo.getPaidTypeCode()); params.add(vo.getDebitAccountType());
		params.add(vo.getTranCode()); params.add(vo.getGlType());
		params.add(vo.getAccountType()); params.add(vo.getAccountNo());
		params.add(vo.getAccountName()); params.add(vo.getCustomerNameReceipt());
		params.add(vo.getTelephone()); params.add(vo.getRequestFormFile());
		params.add(vo.getIdCardFile()); params.add(vo.getChangeNameFile());
		params.add(vo.getCertificateFile()); params.add(vo.getAddress());
		params.add(vo.getRemark()); params.add(vo.getReceiptNo());
		params.add(vo.getStatus()); params.add(vo.getCreatedById());
		params.add(vo.getCreatedByName()); params.add(vo.getMakerById());
		params.add(vo.getMakerByName()); params.add(vo.getAccountNo());
		
		jdbcTemplate.update(sql.toString(), params.toArray());
	}
	
}
