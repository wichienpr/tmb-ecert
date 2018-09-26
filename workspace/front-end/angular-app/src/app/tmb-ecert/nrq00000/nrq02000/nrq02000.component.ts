import { Component, OnInit } from '@angular/core';
import { Router } from '@angular/router';

import { Nrq02000Service } from './nrq02000.service';
import { Observable } from 'rxjs';
import { Lov } from 'app/tmb-ecert/models/lov';

declare var $: any;

@Component({
  selector: 'app-nrq02000',
  templateUrl: './nrq02000.component.html',
  styleUrls: ['./nrq02000.component.css'],
  providers: [Nrq02000Service]
})
export class Nrq02000Component implements OnInit {

  reqType: Observable<Lov[]>;
  customSeg: Observable<Lov[]>;
  payMethod: Observable<Lov[]>;
  subAccMethod: Observable<Lov[]>;

  constructor(
    private router: Router,
    private nrq02000: Nrq02000Service
    ) { }

  ngOnInit() {
    this.reqType = this.nrq02000.getReqType();
    this.customSeg = this.nrq02000.getCustomSeg();
    this.payMethod = this.nrq02000.getpayMethod();
    this.subAccMethod = this.nrq02000.getsubAccMethod();
  }

  send() {
    $('#send-req').modal('show');
  }

  redirect() {
    $('#send-req').modal('hide');
    this.router.navigate(['performa']);
  }

  reqTypeChange(e) {
    console.log(e);
  }

}
