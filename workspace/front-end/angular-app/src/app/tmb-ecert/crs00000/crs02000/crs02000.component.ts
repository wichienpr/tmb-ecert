import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
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

  constructor(
    private service: Crs02000Service
  ) { }

  async ngOnInit() {
    this.id = this.service.getId();
    if (this.id !== "") {
      this.dataLoading = true;
      this.data = await this.service.getData(this.id);
      this.cert = await this.service.getCert(this.id);
      this.chkList = await this.service.getChkList(this.id);
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

  download(fileName: string) {
    this.service.download(fileName);
  }

  back() {
    this.service.cancel();
  }

}
