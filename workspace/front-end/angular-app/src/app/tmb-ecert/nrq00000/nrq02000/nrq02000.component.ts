import { Component, OnInit, ViewChild } from '@angular/core';
import { Router } from '@angular/router';
import { NgForm, FormGroup, FormControl, Validators } from '@angular/forms';
import { Store } from '@ngrx/store';

import { Nrq02000Service } from './nrq02000.service';
import { Certificate, Lov } from 'tmb-ecert/models';
import { DropdownService, ModalService } from 'services/';
import { Modal } from 'models/';
import { convertAccNo } from 'app/baiwa/common/helpers';

declare var $: any;

@Component({
  selector: 'app-nrq02000',
  templateUrl: './nrq02000.component.html',
  styleUrls: ['./nrq02000.component.css'],
  providers: [Nrq02000Service]
})
export class Nrq02000Component implements OnInit {

  form: FormGroup = new FormGroup({
    reqTyped: new FormControl('', Validators.required)
  });

  // reqType: Observable<Lov[]>;
  // customSeg: Observable<Lov[]>;
  // payMethod: Observable<Lov[]>;
  // subAccMethod: Observable<Lov[]>;

  reqType: Lov[];
  customSeg: Lov[];
  payMethod: Lov[];
  subAccMethod: Lov[];

  dropdownObj: any;

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
    this.dropdownObj = {
      reqType: {
        dropdownId: "reqtype",
        formControlName: "reqTyped",
        dropdownName: "reqtype",
        type: "search",
        values: [],
        valueName: "code",
        labelName: "name"
      },
      customSeg: {
        dropdownId: "customSeg",
        formControlName: "customSegd",
        dropdownName: "customSeg",
        type: "search",
        values: [],
        valueName: "code",
        labelName: "name"
      },
      payMethod: {
        dropdownId: "payMethod",
        formControlName: "payMethodd",
        dropdownName: "payMethod",
        type: "search",
        values: [],
        valueName: "code",
        labelName: "name"
      },
      subAccMethod: {
        dropdownId: "subAccMethod",
        formControlName: "subAccMethodd",
        dropdownName: "subAccMethod",
        type: "search",
        values: [],
        valueName: "code",
        labelName: "name"
      },
    };
  }

  ngOnInit() {
    console.log(convertAccNo(1234567890));
    // Current Date
    this.reqDate = this.service.getReqDate();
    // Dropdowns
    this.dropdown.getReqType().subscribe((obj: Lov[]) => this.dropdownObj.reqType.values = obj);
    this.dropdown.getCustomSeg().subscribe((obj: Lov[]) => this.dropdownObj.customSeg.values = obj);
    this.dropdown.getpayMethod().subscribe((obj: Lov[]) => this.dropdownObj.payMethod.values = obj);
    this.dropdown.getsubAccMethod().subscribe((obj: Lov[]) => this.dropdownObj.subAccMethod.values = obj);

    // Certificate
    this.store.select('certificate').subscribe(result => this.reqTypeChanged = result);
  }

  onSubmit(form: NgForm) {
    const modal: Modal = {
      msg: "...?",
      success: true
    };
    this.modal.confirm((e) => {
      if (e) {
        this.service.certificateUpdate(this.reqTypeChanged);
      }
    }, modal);
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
