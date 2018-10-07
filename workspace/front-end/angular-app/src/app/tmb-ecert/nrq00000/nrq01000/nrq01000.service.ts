import { Injectable } from '@angular/core';
import { AjaxService } from 'services/';

const URL = {
  GEN_KEY: "/api/nrq/generate/key"
}

@Injectable({
  providedIn: 'root'
})
export class Nrq01000Service {

  tmbReqFormId: string = "";

  constructor(private ajax: AjaxService) { }

  getTmbReqFormId() {
    return this.ajax.get(URL.GEN_KEY, response => {
      this.tmbReqFormId = response.json();
      return this.tmbReqFormId;
    });
  }

}
