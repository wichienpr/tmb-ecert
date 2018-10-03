import { Injectable } from '@angular/core';
import { UserDetail } from 'app/user.model';
import { PAGE_AUTH } from 'app/baiwa/common/constants';

@Injectable({
  providedIn: 'root'
})
export class CommonService {

  constructor() { }


  ishasRole( user : UserDetail, page_auth:PAGE_AUTH ):boolean{
      if(user.auths){
        return user.auths.indexOf(page_auth) >=0;
      }
      return false;
  }

}
