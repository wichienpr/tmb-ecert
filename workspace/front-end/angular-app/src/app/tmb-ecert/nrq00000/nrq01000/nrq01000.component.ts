import { Component, OnInit, OnDestroy } from '@angular/core';

import { Modal } from 'models/';
import { Nrq01000Service } from './nrq01000.service';
import { Router } from '@angular/router';
import { Observable } from 'rxjs';

declare var $: any;

@Component({
  selector: 'app-nrq01000',
  templateUrl: './nrq01000.component.html',
  styleUrls: ['./nrq01000.component.css']
})
export class Nrq01000Component implements OnInit, OnDestroy {

  nrq01000: Modal;
  tmbReqNum: string = "";

  canMove: boolean = false;

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
    window.addEventListener("beforeunload", (e) => {
      const confirmationMessage = "\o/";
      if (true) {
        (e || window.event).returnValue = confirmationMessage;
        return confirmationMessage;
      }
    });
  }

  ngOnInit() {
    this.confirm();
  }

  ngOnDestroy() {
    $("#nrq01000").modal('hidden');
  }

  canDeactivate(): Observable<boolean> | boolean {
      // Not Allow Redirect
      return this.canMove;
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
    this.canMove = true;
    setTimeout(() => {
      this.router.navigate(['crs/crs01000'], {
        queryParams: { codeStatus: "10011" }
      });
    });
  }

}
