import { Injectable } from '@angular/core';
import { AjaxService } from 'services/';

const URL = {
  GEN_KEY: "/api/nrq/generate/key",
  SAVE_BY_SELF: "/api/nrq/save/by/self",
  NRQ_PDF: "/api/nrq/pdf/",
  CREATE_FORM: "/api/report/pdf/reqFormOriginal",
  FORM_PDF: "/api/report/pdf/view/"
}

@Injectable({
  providedIn: 'root'
})
export class Nrq01000Service {

  tmbReqFormId: string = "";

  constructor(private ajax: AjaxService) { }

  /**
   * @return { data: string, msg: string }
   */
  save() {
    return this.ajax.post(URL.SAVE_BY_SELF, {}, response => {
      return response.json();
    });
  }

  pdf(tmpNo): boolean {
    this.ajax.post(URL.CREATE_FORM, { tmpReqNo: tmpNo, typeCertificate: "" }, response => {
      this.ajax.download(URL.FORM_PDF + response._body + "/download");
    });
    return true;
  }

}
