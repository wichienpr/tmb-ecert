import { Component, OnInit } from '@angular/core';
import { Modal, RequestForm, initRequestForm, RequestCertificate, Certificate } from 'models/';
import { Crs02000Service } from './crs02000.service';

declare var $: any;
@Component({
  selector: 'app-crs02000',
  templateUrl: './crs02000.component.html',
  styleUrls: ['./crs02000.component.css']
})
export class Crs02000Component implements OnInit {

  id: string = "";
  date: Date = new Date();
  dataLoading: boolean = false;
  history: RequestForm[] = [];
  chkList: Certificate[] = [];
  data: RequestForm = initRequestForm;
  cert: RequestCertificate[] = [];
  paidModal: Modal = {
    modalId: "desp",
    type: "custom"
  };
  allowedModal: Modal = {
    modalId: "allowed",
    type: "custom"
  };
  documentModal: Modal = {
    modalId: "document",
    type: "custom"
  };
  allowed: string[] = [
    "กรุณาเลือก",
    "ลูกค้ามียอดเงินในบัญชีไม่พอจ่าย",
    "ลูกค้าแนบเอกสารไม่ครบถ้วน",
    "ลูกค้าขอยกเลิกคำขอ",
    "เจ้าหน้าที่ธนาคารขอยกเลิกคำขอ",
    "อื่นๆ",
  ];
  tab: any = {
    A: "active",
    B: ""
  };

  constructor(private service: Crs02000Service) {
    this.init();
  }

  ngOnInit() { }

  async init() {
    this.id = this.service.getId();
    if (this.id !== "") {
      this.dataLoading = true;
      this.data = await this.service.getData(this.id);
      this.cert = await this.service.getCert(this.id);
      this.history = await this.service.getHistory(this.id);
      this.chkList = await this.service.getChkList(this.id);
      for (let i = 0; i < this.chkList.length; i++) {
        if (this.chkList[i].feeDbd == "" && i != 0) {
          this.chkList[i].children = await this.service.getChkListMore(this.chkList[i].code);
        }
      }
      this.chkList = await this.service.matchChkList(this.chkList, this.cert);
      setTimeout(() => {
        this.dataLoading = false;
      }, 500);
    }
  }

  tabs(name: string) {
    this.tab = this.service.tabsToggle(name, this.tab);
  }

  approveToggle() {
    this.service.approveToggle();
  }

  modalToggle(name: string) {
    $(`#${name}`).modal('show');
  }

  selectColor() {
    return 'blue';
  }

  download(fileName: string) {
    this.service.download(fileName);
  }

  pdf(what: string) {
    const cover = "crsCover02000";
    const receipt = "crsReceipt02000";
    this.service.pdf(what == 'c' ? cover : receipt);
  }

  back() {
    this.service.cancel();
  }

}
