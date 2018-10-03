import { Injectable } from '@angular/core';
import { ModalService, AjaxService } from 'services/';
import { Modal, RequestForm, initRequestForm } from 'models/';
import { Observable } from 'rxjs';

const URL = {
  REQUEST_FORM: "/api/nrq/data"
}

@Injectable({
  providedIn: 'root'
})
export class Nrq03000Service {

  constructor(private modal: ModalService, private ajax: AjaxService) { }

  approveToggle(): any {
    const modal: Modal = {
      approveMsg: "ยืนยัน",
      rejectMsg: "ยกเลิก",
      type: "confirm",
      size: "small",
      modalId: "approve",
      msg: `<label>ยืนยันการอนุมัติการชำระเงินค่าธรรมเนียม</label>`,
      title: "อนุมัติการชำระเงินค่าธรรมเนียม"
    };
    this.modal.confirm(e => { }, modal);
  }

  getData(id: String): Observable<RequestForm> {
    return new Observable<RequestForm>(obs => {
      this.ajax.get(`${URL.REQUEST_FORM}/${id}`, response => {
        let data: RequestForm[] = response.json() as RequestForm[];
        obs.next(data.length > 0 ? data[0] : initRequestForm);
      })
    });
  }

}
