package com.tmb.ecert.checkrequeststatus.service;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.tmb.ecert.checkrequeststatus.persistence.dao.CheckRequestStatusDao;
import com.tmb.ecert.checkrequeststatus.persistence.vo.CountStatusVo;
import com.tmb.ecert.checkrequeststatus.persistence.vo.Crs01000FormVo;
import com.tmb.ecert.checkrequeststatus.persistence.vo.Crs01000Vo;
import com.tmb.ecert.checkrequeststatus.persistence.vo.StatusVo;
import com.tmb.ecert.common.constant.ProjectConstant.APPLICATION_LOG_NAME;
import com.tmb.ecert.common.constant.StatusConstant;

import th.co.baiwa.buckwaframework.common.bean.DataTableResponse;
import th.co.baiwa.buckwaframework.security.constant.ADConstant;
import th.co.baiwa.buckwaframework.security.domain.UserDetails;
import th.co.baiwa.buckwaframework.security.util.UserLoginUtils;;

@Service
public class CheckRequestStatusService {

	private Logger logger = LoggerFactory.getLogger(APPLICATION_LOG_NAME.ECERT_SEARCH_REQFORM);

	@Autowired
	private CheckRequestStatusDao crs01000Dao;

	public DataTableResponse<Crs01000Vo> findReqForm(Crs01000FormVo formVo) {
		logger.info("findReqByTmbReqNo_Service");

		DataTableResponse<Crs01000Vo> dt = new DataTableResponse<>();
		List<Crs01000Vo> crs01000VoList = new ArrayList<Crs01000Vo>();
		UserDetails user = UserLoginUtils.getCurrentUserLogin();
		int count = 0;
		if (UserLoginUtils.ishasRoleName(user, ADConstant.ROLE_REQUESTER)) {
			formVo.setUserId(user.getUserId());
		}
		if (StringUtils.isNotBlank(formVo.getStatus())) {
			crs01000VoList = crs01000Dao.findReqByStatus(formVo);
			count = crs01000Dao.countFindReqByStatusDataTable(formVo);
		} 
//		else if (StringUtils.isNotBlank(formVo.getReqDate()) && StringUtils.isNotBlank(formVo.getToReqDate())) {
//			crs01000VoList = crs01000Dao.findReq(formVo);
//			count = crs01000Dao.countFindReqDataTable(formVo);
//		}
		else {
			crs01000VoList = crs01000Dao.findReq(formVo);
			count = crs01000Dao.countFindReqDataTable(formVo);
		}
		dt.setData(crs01000VoList);
		dt.setRecordsTotal(count);
		return dt;
	}

	public CountStatusVo countStatus(CountStatusVo countStatusVo) {
		logger.info("countStatus_Service");
		UserDetails user = UserLoginUtils.getCurrentUserLogin();
		if (UserLoginUtils.ishasRoleName(user, ADConstant.ROLE_REQUESTER)) {
			countStatusVo.setUserId(user.getUserId());
		}
		List<StatusVo> statusVoList = crs01000Dao.countStatus(countStatusVo);
		for (StatusVo data : statusVoList) {
			if (StatusConstant.NEW_REQUEST.equals(data.getStatus())) {
				countStatusVo.setNewrequest(data.getCount());
			} else if (StatusConstant.PAYMENT_PROCESSING.equals(data.getStatus())) {
				countStatusVo.setPaymentProcessing(data.getCount());
			} else if (StatusConstant.REFUSE_REQUEST.equals(data.getStatus())) {
				countStatusVo.setRefuseRequest(data.getCount());
			} else if (StatusConstant.CANCEL_REQUEST.equals(data.getStatus())) {
				countStatusVo.setCancelRequest(data.getCount());
			} else if (StatusConstant.WAIT_PAYMENT_APPROVAL.equals(data.getStatus())) {
				countStatusVo.setWaitPaymentApproval(data.getCount());
			} else if (StatusConstant.PAYMENT_APPROVALS.equals(data.getStatus())) {
				countStatusVo.setPaymentApprovals(data.getCount());
			} else if (StatusConstant.REJECT_PAYMENT.equals(data.getStatus())) {
				countStatusVo.setChargeback(data.getCount());
			} else if (StatusConstant.PAYMENT_FAILED.equals(data.getStatus())) {
				countStatusVo.setPaymentfailed(data.getCount());
			} else if (StatusConstant.WAIT_UPLOAD_CERTIFICATE.equals(data.getStatus())) {
				countStatusVo.setWaitUploadCertificate(data.getCount());
			} else if (StatusConstant.SUCCEED.equals(data.getStatus())) {
				countStatusVo.setSucceed(data.getCount());
			} else if (StatusConstant.WAIT_SAVE_REQUEST.equals(data.getStatus())) {
				countStatusVo.setWaitSaveRequest(data.getCount());
			}

		}

		return countStatusVo;
	}
}
