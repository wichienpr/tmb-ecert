package com.tmb.ecert.history.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.tmb.ecert.history.persistence.dao.RequestHistoryDao;
import com.tmb.ecert.history.persistence.vo.RequestHistoryVo;
import com.tmb.ecert.report.persistence.vo.ReqReceiptVo;

import th.co.baiwa.buckwaframework.common.bean.DataTableResponse;

@Service
public class RequestHistoryService {

	@Autowired
	private RequestHistoryDao requestHistoryDao;

	public List<RequestHistoryVo> findAll() {
		return requestHistoryDao.findAll();
	}

	public List<RequestHistoryVo> findByReqFormId(String reqFormId) {
		Long id = Long.valueOf(reqFormId);
		return requestHistoryDao.findByReqFormId(id);
	}

	public DataTableResponse<RequestHistoryVo> findByVo(RequestHistoryVo vo) {
		List<RequestHistoryVo> list = requestHistoryDao.findByReqFormId(vo);
		DataTableResponse<RequestHistoryVo> response = new DataTableResponse<RequestHistoryVo>();
		response.setData(list);
		int count = requestHistoryDao.count(vo);
		response.setRecordsTotal(count);
		return response;
	}
	
	public DataTableResponse<ReqReceiptVo> findReceiptHisByReqID(RequestHistoryVo vo) {
		List<ReqReceiptVo> list = requestHistoryDao.findReciptHis(vo);
		DataTableResponse<ReqReceiptVo> response = new DataTableResponse<ReqReceiptVo>();
		response.setData(list);
		int count = requestHistoryDao.receiptHisCount(vo);
		response.setRecordsTotal(count);
		return response;
	}
}
