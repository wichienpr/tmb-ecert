import { Injectable } from '@angular/core';
import { UserDetail } from 'app/user.model';
import { Http } from '@angular/http';
import { longStackSupport } from 'q';
import { AjaxService } from 'app/baiwa/common/services/ajax.service';

@Injectable({
  providedIn: 'root'
})
export class AuthService {

  constructor(private http: Http) { }


  public login(username: string, password: string) {
    let f: FormData = new FormData();

    f.append("username", username);
    f.append("password", password);

    let url: string = AjaxService.CONTEXT_PATH + "/login";
    return this.http.post(url, f);
  }

  public logout() {
    let logouturl: string = AjaxService + "/onlogout";
    return this.http.get(logouturl);
  }
}