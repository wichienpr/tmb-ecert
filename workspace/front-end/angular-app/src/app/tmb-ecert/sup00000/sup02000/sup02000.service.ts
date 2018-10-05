import { Injectable } from '@angular/core';
import { AjaxService } from 'app/baiwa/common/services';
import { HttpClient, HttpHeaders } from '@angular/common/http';

@Injectable({
  providedIn: 'root'
})
export class Sup02000Service {

  constructor(private ajax: AjaxService, private httpClient: HttpClient) {

   }

   callGetParamAPI() {
    const path = "/api/setup/sup02000/getParameter";
    const httpOptions = {
        headers: new HttpHeaders({ 'Content-Type': 'application/json' })
    };
    return this.httpClient.get(AjaxService.CONTEXT_PATH + path, httpOptions);

}

}
