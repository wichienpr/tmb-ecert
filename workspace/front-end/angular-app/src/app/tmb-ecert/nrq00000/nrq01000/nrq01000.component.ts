import { Component, OnInit } from '@angular/core';
import { Location } from '@angular/common';

import { Modal } from 'models/';
import { ModalService } from 'app/baiwa/common/services';
import { Nrq01000Service } from './nrq01000.service';
import { Router } from '@angular/router';

@Component({
  selector: 'app-nrq01000',
  templateUrl: './nrq01000.component.html',
  styleUrls: ['./nrq01000.component.css']
})
export class Nrq01000Component implements OnInit {

  constructor(
    private modal: ModalService,
    private service: Nrq01000Service,
    private router: Router,
    private location: Location
  ) { }

  ngOnInit() {
    this.confirm();
  }

  confirm = () => {
    const modalC: Modal = {
      title: "ยืนยันการทำรายการ ?"
    };
    this.modal.confirm(async e => {
      if (e) {
        let num = await this.service.getTmbReqFormId();
        setTimeout(() => {
          const modalConf: Modal = {
            title: "Request Form สำหรับลูกค้าทำรายการเอง",
            msg: "TMB Req. No: " + num
          };
          this.modal.confirm(e => {
            if (e) {
              
            }
          }, modalConf);
        }, 200);
      } else {
        this.location.back();
      }
    }, modalC);
  }

}
