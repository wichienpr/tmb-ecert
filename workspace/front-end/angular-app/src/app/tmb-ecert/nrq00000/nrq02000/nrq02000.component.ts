import { Component, OnInit, ViewChild } from '@angular/core';
import { Router } from '@angular/router';
import { NgForm } from '@angular/forms';
import { Store } from '@ngrx/store';
import { Observable } from 'rxjs';

import { Nrq02000Service } from './nrq02000.service';
import { Certificate, Lov } from 'tmb-ecert/models';
import { DropdownService, ModalService, Modal } from 'services/';

declare var $: any;

@Component({
  selector: 'app-nrq02000',
  templateUrl: './nrq02000.component.html',
  styleUrls: ['./nrq02000.component.css'],
  providers: [Nrq02000Service]
})
export class Nrq02000Component implements OnInit {

  @ViewChild("form") form: NgForm;

  reqType: Observable<Lov[]>;
  customSeg: Observable<Lov[]>;
  payMethod: Observable<Lov[]>;
  subAccMethod: Observable<Lov[]>;

  reqTypeChanged: Certificate[];

  reqDate: string;

  constructor(
    private store: Store<{}>,
    private router: Router,
    private dropdown: DropdownService,
    private service: Nrq02000Service,
    private modal: ModalService
  ) {
    this.reqTypeChanged = [];
  }

  ngOnInit() {
    // Current Date
    this.reqDate = this.service.getReqDate();
    // Dropdowns
    this.reqType = this.dropdown.getReqType();
    this.customSeg = this.dropdown.getCustomSeg();
    this.payMethod = this.dropdown.getpayMethod();
    this.subAccMethod = this.dropdown.getsubAccMethod();
    // Certificate
    this.store.select('certificate').subscribe(result => this.reqTypeChanged = result );
  }

  onSubmit(form: NgForm) {
    const modal: Modal = {
      msg: "...?",
      success: true
    };
    this.modal.confirm((e) => { },modal);
    this.service.certificateUpdate(this.reqTypeChanged);
  }

  send() {
    const modal: Modal = {
      msg: `<label>เนื่องจากระบบตรวจสอบข้อมูลพบว่าลูกค้าได้ทำการยื่นใบคำขอเอกสารรับรองประเภทนี้ไปแล้วนั้น
      <br> ลูกค้ามีความประสงค์ต้องการขอเอกสารรับรองอีกครั้งหรือไม่ ถ้าต้องการกรุณากดปุ่ม "ดำเนินการต่อ"
      <br> หากไม่ต้องการกรุณากดปุ่ม "ยกเลิก"</label>`,
      title: "แจ้งเตือนยื่นใบคำขอเอกสารรับรองซ้ำ",
      approveMsg: "ดำเนินการต่อ",
      color: "notification"
    }
    this.modal.confirm((e) => { }, modal);
  }

  redirect() {
    $('#send-req').modal('hide');
    this.router.navigate(['performa']);
  }

  reqTypeChange(e) {
    this.service.reqTypeChange(e);
  }

  customSegChange(e) {
    console.log('customSegChange => ', e);
  }

  payMethodChange(e) {
    console.log('payMethodChange => ', e);
  }

  subAccMethodChange(e) {
    console.log('subAccMethodChange => ', e);
  }

  toggleChk(index: number) {
    this.reqTypeChanged[index].value = null;
  }

}
