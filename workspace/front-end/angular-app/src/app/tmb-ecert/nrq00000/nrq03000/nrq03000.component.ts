import { Component, OnInit, AfterViewInit } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';
import { Modal, RequestForm, initRequestForm } from 'models/';
import { Nrq03000Service } from './nrq03000.service';

declare var $: any;

@Component({
  selector: 'app-nrq03000',
  templateUrl: './nrq03000.component.html',
  styleUrls: ['./nrq03000.component.css']
})
export class Nrq03000Component implements OnInit, AfterViewInit {

  id: string;
  data: RequestForm = initRequestForm;
  paidModal: Modal;
  allowedModal: Modal;
  documentModal: Modal;
  allowed: string[] = [];

  constructor(private route: ActivatedRoute, private router: Router, private service: Nrq03000Service) {
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
  }

  ngOnInit() {
    this.id = this.route.snapshot.queryParams["id"] || "";
    if (this.id !== "") {
      this.service.getData(this.id).subscribe(result => {
        console.log(result);
        this.data = result;
      });
    }
  }

  ngAfterViewInit() {
    $('.menu .item').tab();
  }

  approveToggle() {
    this.service.approveToggle();
  }

  modalToggle(name: string) {
    $(`#${name}`).modal('show');
  }

}
