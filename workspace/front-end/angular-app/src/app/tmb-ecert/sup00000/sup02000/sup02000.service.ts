import { Injectable } from '@angular/core';
import { AjaxService } from 'app/baiwa/common/services';
import { HttpClient, HttpHeaders } from '@angular/common/http';

const URL = {
  SUP01000_GET: "/api/setup/sup02000/getParameter",
  SUP01000_SAVE: "/api/setup/sup02000/saveParameter"
}

@Injectable({
  providedIn: 'root'
})
export class Sup02000Service {

  constructor(private ajax: AjaxService, private httpClient: HttpClient) {

  }

  callGetParamAPI() {
    const path = URL.SUP01000_GET;
    const httpOptions = {
      headers: new HttpHeaders({ 'Content-Type': 'application/json' })
    };
    return this.httpClient.get(AjaxService.CONTEXT_PATH + path, httpOptions);

  }

  callSaveParameterAPI(form){
    const path = URL.SUP01000_SAVE;
    const httpOptions = {
      headers: new HttpHeaders({ 'Content-Type': 'application/json' })
    };
    return this.httpClient.post(AjaxService.CONTEXT_PATH + path,{listParameter:form}, httpOptions);
  }

}
