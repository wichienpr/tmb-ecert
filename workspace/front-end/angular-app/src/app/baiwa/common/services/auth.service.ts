import { Injectable } from '@angular/core';
import { UserDetail } from 'app/user.model';
import { Http } from '@angular/http';
import { longStackSupport } from 'q';
import { AjaxService } from 'app/baiwa/common/services/ajax.service';
import { HttpClient } from '@angular/common/http';
import { timeout } from 'rxjs/operators';

@Injectable({
  providedIn: 'root'
})
export class AuthService {

  constructor(private http: HttpClient) { }


  public login(username: string, password: string) {
    let f: FormData = new FormData();

    f.append("username", username);
    f.append("password", password);

    let url: string = AjaxService.CONTEXT_PATH + "/login";
    return this.http.post(url, f).pipe(timeout(10000));
  }

  public logout() {
    let logouturl: string = AjaxService.CONTEXT_PATH + "/onlogout";
    return this.http.get(logouturl);
  }

  // public getUser() {
  //   let getuserurl: string = AjaxService.CONTEXT_PATH + "/onloginseccess";
  //   return this.http.post(getuserurl, {}).toPromise();
  // }
}