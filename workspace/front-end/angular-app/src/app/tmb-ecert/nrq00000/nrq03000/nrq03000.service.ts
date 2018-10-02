import { Injectable } from '@angular/core';
import { ModalService } from 'services/';
import { Modal } from 'models/';

@Injectable({
  providedIn: 'root'
})
export class Nrq03000Service {

  constructor(private modal: ModalService) { }

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

}
