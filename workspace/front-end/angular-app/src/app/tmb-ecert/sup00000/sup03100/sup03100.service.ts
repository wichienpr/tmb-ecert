import { Injectable } from '@angular/core';
import { HttpClient, HttpHeaders } from '@angular/common/http';
import { AjaxService } from 'app/baiwa/common/services';

const URL = {
  SUP03100_SEARCH: "/api/setup/sup03000/getEmailDetail",
  SUP03000_SAVE: "/api/setup/sup02000/saveParameter"
}

@Injectable({
  providedIn: 'root'
})
export class Sup03100Service {

  constructor( private ajax: AjaxService,private httpClient: HttpClient,) { 

  }

  callSearchEmailDetailAPI(id){
    const path = URL.SUP03100_SEARCH;
    const httpOptions = {
      headers: new HttpHeaders({ 'Content-Type': 'application/json' })
    };
    return this.httpClient.post(AjaxService.CONTEXT_PATH + path,{
      emailConfig_id:id,
      name:"",
      status:""
    }, httpOptions);
  }
}
