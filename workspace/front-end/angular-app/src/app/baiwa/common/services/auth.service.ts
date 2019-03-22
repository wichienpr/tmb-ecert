import { Injectable } from '@angular/core';
import { UserDetail } from 'app/user.model';
import { Http } from '@angular/http';
import { longStackSupport } from 'q';
import { AjaxService } from 'app/baiwa/common/services/ajax.service';
import { HttpClient } from '@angular/common/http';
import { timeout } from 'rxjs/operators';
import { Store } from '@ngrx/store';
import { AjaxLoginVo } from 'app/baiwa/login/login.component';
import { UpdateUser } from 'app/user.action';

@Injectable({
  providedIn: 'root'
})
export class AuthService {

  private userLogin: UserDetail = null;

  constructor(private http: HttpClient, private store: Store<AppState>) {
    this.store.select("user").subscribe(u => this.userLogin = u);
  }


  public login(username: string, password: string, authflag: string) {
    let f: FormData = new FormData();

    f.append("username", username+" "+authflag);
    f.append("password", password);

    let url: string = AjaxService.CONTEXT_PATH + "/login";
    return this.http.post(url, f).pipe(timeout(10000));
  }

  public logout() {
    let logouturl: string = AjaxService.CONTEXT_PATH + "/onlogout";
    return this.http.get(logouturl);
  }

  public getUser() : Promise<boolean> {
    return new Promise<boolean>((resolve, reject) => {
      // let getuserurl: string = AjaxService.CONTEXT_PATH + "/onloginseccess";
      let getuserurl: string = AjaxService.CONTEXT_PATH + "/getUserlogin";
      // console.log("getUser");
      this.http.post(getuserurl, {}).subscribe(resp => {
        let result: AjaxLoginVo = resp as AjaxLoginVo;
        // console.log("ACTION : RE GET PROFILE" , result);
        if (result.status == "SUCCESS" || result.status == "DUP_LOGIN") {
          const INIT_USER_DETAIL: UserDetail = {
            roles: result.roles,
            userId: result.userId,
            username: result.username,
            firstName: result.firstName,
            lastName: result.lastName,
            auths: result.auths,
            segment: result.segment || "",
            department:result.department
          };
          this.store.dispatch(new UpdateUser(INIT_USER_DETAIL));
          resolve(true);
        }else{
          reject(false);
        }
      },
      error=>{
        reject(false);
      });

    });
  }

  public kickPreviousUser(): Promise<any> {
    const url = AjaxService.CONTEXT_PATH + "/forcekick";;
    return this.http.post(url, {}).toPromise();
  }

  get isLoggedIn() {
    if (this.userLogin == null) return false;
    if (this.userLogin.userId === "") {
      return false;
    }
    return true;
  }

}


interface AppState {
  user: UserDetail
}