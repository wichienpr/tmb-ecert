import { Injectable } from '@angular/core';
import { DropdownService, AjaxService } from 'app/baiwa/common/services';
import { HttpClient } from '@angular/common/http';

const URL = {
  BTM03000_GET: "/api/btm/btm01000/getListBatch",
  // SUP03000_SAVE: "/api/setup/sup02000/saveParameter"
  BTM01000RERUN: "/api/btm/btm01000/reRunJob",
  PARAMETERS: "/api/parameter/"
}

@Injectable({
  providedIn: 'root'
})
export class Btm01000Service {
  dropdownObj: any;

  constructor(private dropdown: DropdownService, private httpClient: HttpClient) { }

  getDropdownBatchJob() {
    return this.dropdown.getBatchJobSchedu();
  }

  getDropdownJobMonitoringStatus() {
    return this.dropdown.getMonitoringStatus();
  }

  getListBatch(form) {
    return this.httpClient.post(AjaxService.CONTEXT_PATH + URL.BTM03000_GET, form);
  }

  callRerunJobService(form) {
    return this.httpClient.post(AjaxService.CONTEXT_PATH + URL.BTM01000RERUN, form);
  }

  getParameters() {
    return this.httpClient.get(AjaxService.CONTEXT_PATH + URL.PARAMETERS);
  }

}
