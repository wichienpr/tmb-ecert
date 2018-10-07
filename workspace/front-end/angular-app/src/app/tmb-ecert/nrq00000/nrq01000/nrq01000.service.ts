import { Injectable } from '@angular/core';
import { AjaxService } from 'services/';

const URL = {
  GEN_KEY: "/api/nrq/generate/key",
  SAVE_BY_SELF: "/api/nrq/save/by/self",
  NRQ_PDF: "/api/nrq/pdf/",
}

@Injectable({
  providedIn: 'root'
})
export class Nrq01000Service {

  tmbReqFormId: string = "";

  constructor(private ajax: AjaxService) { }
  /**
   * @return { tmbReqFormId: string }
   */
  getTmbReqFormId() {
    return this.ajax.get(URL.GEN_KEY, response => {
      this.tmbReqFormId = response.json();
      return this.tmbReqFormId;
    });
  }

  /**
   * @return { data: string, msg: string }
   */
  save() {
    return this.ajax.post(URL.SAVE_BY_SELF, {}, response => {
      return response.json();
    });
  }

  pdf(): boolean {
    this.ajax.download(URL.NRQ_PDF + "nrq02000");
    return true;
  }

}
