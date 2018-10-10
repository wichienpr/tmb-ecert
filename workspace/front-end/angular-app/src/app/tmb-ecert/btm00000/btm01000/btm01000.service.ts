import { Injectable } from '@angular/core';
import { DropdownService, AjaxService } from 'app/baiwa/common/services';
import { Lov } from 'app/baiwa/common/models';
import { HttpClient } from '@angular/common/http';
import { FormControl, FormGroup } from '@angular/forms';

const URL = {
  BTM03000_GET: "/api/btm/btm01000/getListBatch",
  SUP03000_SAVE: "/api/setup/sup02000/saveParameter"
}

@Injectable({
  providedIn: 'root'
})
export class Btm01000Service {
  dropdownObj: any;

  constructor(private dropdown: DropdownService,private httpClient: HttpClient,private ajax: AjaxService) { 

  }

  getDropdownBatchJob(){
    return this.dropdown.getBatchJobSchedu();
  }

  getDropdownJobMonitoringStatus(){
    return this.dropdown.getMonitoringStatus();
  }

  getListBatch(form){

    return this.httpClient.post(AjaxService.CONTEXT_PATH + URL.BTM03000_GET,form);
  }


}
