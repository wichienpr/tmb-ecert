import { Injectable } from '@angular/core';
import { UserDetail } from 'app/user.model';
import { PAGE_AUTH, ROLES } from 'app/baiwa/common/constants';
declare var $;

@Injectable({
  providedIn: 'root'
})
export class CommonService {

  constructor() { }

  ishasAuth(user: UserDetail, page_auth: PAGE_AUTH): boolean {
    if (user.auths) {
      return user.auths.indexOf(page_auth) >= 0;
    }
    return false;
  }

  ishasRole(user: UserDetail, roles: ROLES): boolean {
    if (user.roles) {
      return user.roles.indexOf(roles) >= 0;
    }
    return false;
  }

  blockui(){
    $.blockUI({
      message: null
    }); 
  }

  unblockui(){
    $.unblockUI()
  }

}
