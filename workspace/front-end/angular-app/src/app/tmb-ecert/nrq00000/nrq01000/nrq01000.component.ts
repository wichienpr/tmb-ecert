import { Component, OnInit } from '@angular/core';

import { Modal } from 'models/';
import { Nrq01000Service } from './nrq01000.service';
import { Router } from '@angular/router';

declare var $: any;

@Component({
  selector: 'app-nrq01000',
  templateUrl: './nrq01000.component.html',
  styleUrls: ['./nrq01000.component.css']
})
export class Nrq01000Component implements OnInit {

  nrq01000: Modal;
  tmbReqNum: string = "";

  constructor(
    private service: Nrq01000Service,
    private router: Router
  ) {
    this.nrq01000 = {
      modalId: "nrq01000",
      type: "custom",
      title: "Request Form (พิมพ์ใบคำขอเปล่าให้ลูกค้าลงนาม และบันทึกข้อมูลภายหลัง)",
      msg: "TMB Req. No:  - "
    };
  }

  ngOnInit() {
    this.confirm();
  }

  confirm = async () => {
    let response = await this.service.save();
    this.tmbReqNum = response.data;
    this.nrq01000 = {
      modalId: "nrq01000",
      type: "custom",
      title: "Request Form (พิมพ์ใบคำขอเปล่าให้ลูกค้าลงนาม และบันทึกข้อมูลภายหลัง)"
    };
    $("#nrq01000").modal('show');
  }

  print = () => {
    this.requestPage();
    this.service.pdf(this.tmbReqNum);
  }

  requestPage() {
    setTimeout(() => {
      this.router.navigate(['crs/crs01000'], {
        queryParams: { codeStatus: "10011" }
      });
    });
  }

}
