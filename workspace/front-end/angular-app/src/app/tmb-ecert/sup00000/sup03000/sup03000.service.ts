import { Injectable } from '@angular/core';
import { AjaxService, DropdownService } from 'app/baiwa/common/services';
import { HttpClient, HttpHeaders } from '@angular/common/http';

const URL = {
  SUP03000_GET: "/api/setup/sup03000/getEmailTemplate",
  SUP03000_SAVE: "/api/setup/sup02000/saveParameter"
}

@Injectable({
  providedIn: 'root'
})
export class Sup03000Service {

  constructor(private ajax: AjaxService, private httpClient: HttpClient,private dropdown : DropdownService) {


   }

  getStatusType(){
    return this.dropdown.getStatusType();
  }

  callSearchAPI(form){
    const path = URL.SUP03000_GET;
    const httpOptions = {
      headers: new HttpHeaders({ 'Content-Type': 'application/json' })
    };
    return this.httpClient.post(AjaxService.CONTEXT_PATH + path,{
      name:form.value.emailName ,
      status:form.value.status
    }, httpOptions);
  }


}
