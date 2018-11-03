import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { AjaxService } from 'app/baiwa/common/services';

@Injectable({
  providedIn: 'root'
})
export class DashboardService {

  private contextPath:string = AjaxService.CONTEXT_PATH ;
  constructor(private httpclient:HttpClient) { }


  getDashBoard(){
   let url = this.contextPath + "/api/crs/crs01000/countStatus";
   return this.httpclient.post(url,{}); 
  }

  reloadCache() {
    let url = this.contextPath + "/api/cache/reload";
    return this.httpclient.get(url);
  }


}
