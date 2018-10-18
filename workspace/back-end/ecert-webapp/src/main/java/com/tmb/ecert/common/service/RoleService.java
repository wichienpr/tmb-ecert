package com.tmb.ecert.common.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.tmb.ecert.common.dao.RoleDao;
import com.tmb.ecert.common.domain.RoleVo;

@Service
public class RoleService {
	
	@Autowired
	private RoleDao roleDao;
	
	public List<RoleVo> findRoleJoinRolePermission() {
		return roleDao.findRoleJoinRolePermission();
	}
}
