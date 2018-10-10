import { Injectable } from '@angular/core';
import { UserDetail } from 'app/user.model';
import { PAGE_AUTH, ROLES } from 'app/baiwa/common/constants';
import { Store } from '@ngrx/store';
declare var $;

@Injectable({
  providedIn: 'root'
})
export class CommonService {

  private user: UserDetail;

  constructor(private store: Store<{}>) {
    this.store.select('user').subscribe(user => this.user = user);
  }

  isAuth(page_auth: PAGE_AUTH): boolean {
    return this.ishasAuth(this.user, page_auth);
  }

  ishasAuth(user: UserDetail, page_auth: PAGE_AUTH): boolean {
    if (user.auths) {
      return user.auths.indexOf(page_auth) >= 0;
    }
    return false;
  }

  isRole(roles: ROLES): boolean {
    return this.ishasRole(this.user, roles);
  }

  ishasRole(user: UserDetail, roles: ROLES): boolean {
    if (user.roles) {
      return user.roles.indexOf(roles) >= 0;
    }
    return false;
  }

  blockui() {
    $.blockUI({
      message: null
    });
  }

  unblockui() {
    $.unblockUI()
  }

}
