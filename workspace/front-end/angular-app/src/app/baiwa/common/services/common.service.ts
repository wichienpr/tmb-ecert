import { Injectable } from '@angular/core';
import { UserDetail } from 'app/user.model';
import { PAGE_AUTH, ROLES } from 'app/baiwa/common/constants';
import { Store } from '@ngrx/store';
import { Observable } from 'rxjs';
import { debounceTime } from 'rxjs/operators';
declare var $;

@Injectable({
  providedIn: 'root'
})
export class CommonService {

  private user: UserDetail;
  private loading: boolean = false;

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

  isUser(userId: string): boolean {
    return this.user.userId === userId
  }

  blockui() {
    $.blockUI({
      css: { border: 0 },
      message: `<div class="ui active dimmer" style="background:transparent"><div class="ui text loader">กำลังโหลด</div></div>`
    });
  }

  unblockui() {
    $.unblockUI()
  }

  toGetQuery(param:any){
    return $.param(param);
  }

  loadStatus(): Observable<boolean> {
    return new Observable<boolean>( obs => {
      setInterval(() => {
        obs.next(this.loading);
      }, 500);
    });
  }

  isLoading() {
    $.blockUI({
      css: { border: 0 },
      message: `<div class="ui active dimmer" style="background:transparent"><div class="ui text loader">กำลังโหลด</div></div>`
    });
    this.loading = true;
  }

  isLoaded() {
    $.unblockUI()
    this.loading = false;
  }

}
