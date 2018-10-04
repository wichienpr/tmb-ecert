import { Component, OnInit } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';
import { Modal, RequestForm, initRequestForm } from 'models/';
import { ModalService } from 'app/baiwa/common/services';
import { Crs02000Service } from './crs02000.service';

declare var $: any;
@Component({
  selector: 'app-crs02000',
  templateUrl: './crs02000.component.html',
  styleUrls: ['./crs02000.component.css']
})
export class Crs02000Component implements OnInit {

  id: string;
  date: Date;
  dataLoading: boolean = false;
  data: RequestForm = initRequestForm;
  paidModal: Modal;
  allowedModal: Modal;
  documentModal: Modal;
  allowed: string[] = [];
  tab: any = {
    A: "active",
    B: ""
  };

  constructor(
    private route: ActivatedRoute,
    private router: Router,
    private service: Crs02000Service,
    private modal: ModalService
  ) {
    this.paidModal = {
      modalId: "desp",
      type: "custom"
    };
    this.allowedModal = {
      modalId: "allowed",
      type: "custom"
    }
    this.documentModal = {
      modalId: "document",
      type: "custom"
    }
    this.allowed = [
      "กรุณาเลือก",
      "ลูกค้ามียอดเงินในบัญชีไม่พอจ่าย",
      "ลูกค้าแนบเอกสารไม่ครบถ้วน",
      "ลูกค้าขอยกเลิกคำขอ",
      "เจ้าหน้าที่ธนาคารขอยกเลิกคำขอ",
      "อื่นๆ",
    ];
    this.date = new Date();
  }

  ngOnInit() {
    this.id = this.route.snapshot.queryParams["id"] || "";
    if (this.id !== "") {
      this.dataLoading = true;
      this.service.getData(this.id).subscribe(result => {
        console.log(result);
        this.data = result;
        setTimeout(() => {
          this.dataLoading = false;
        }, 200);
      });
    }
  }

  tabs(name: string) {
    for (let key in this.tab) {
      if (name == key) {
        this.tab[key] = "active";
      } else {
        this.tab[key] = "";
      }
    }
  }

  approveToggle() {
    this.service.approveToggle();
  }

  modalToggle(name: string) {
    $(`#${name}`).modal('show');
  }

  download(fileName: string) {
    if (fileName) {
      this.service.download(fileName);
    } else {
      const modal: Modal = {
        msg: "ไม่พบไฟล์"
      };
      this.modal.alert(modal);
    }
  }

}
