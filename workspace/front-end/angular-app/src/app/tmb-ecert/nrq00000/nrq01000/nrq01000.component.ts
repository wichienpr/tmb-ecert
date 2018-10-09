import { Component, OnInit } from '@angular/core';
import { Location } from '@angular/common';

import { Modal } from 'models/';
import { ModalService } from 'app/baiwa/common/services';
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

  constructor(
    private modal: ModalService,
    private service: Nrq01000Service,
    private router: Router,
    private location: Location
  ) {
    this.nrq01000 = {
      modalId: "nrq01000",
      type: "custom",
      title: "Request Form สำหรับลูกค้าทำรายการเอง",
      msg: "TMB Req. No:  - "
    };
  }

  ngOnInit() {
    this.confirm();
  }

  confirm = () => {
    const modalConf: Modal = {
      title: "ยืนยันการทำรายการ ?",
      msg: ""
    };
    this.modal.confirm(async e => {
      if (e) {
        let response = await this.service.save();
        setTimeout(() => {
          this.nrq01000 = {
            modalId: "nrq01000",
            type: "custom",
            title: "Request Form สำหรับลูกค้าทำรายการเอง",
            msg: "TMB Req. No: " + response.data
          };
          $("#nrq01000").modal('show');
        }, 200);
      } else {
        this.location.back();
      }
    }, modalConf);
  }

  print = () => {
    this.service.pdf();
  }

  requestPage() {
    setTimeout(() => {
      this.router.navigate(['crs/crs01000'], {
        queryParams: { codeStatus: "10011" }
      });
    });
  }

}
