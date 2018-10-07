import { Component, OnInit, ViewChild, ChangeDetectorRef } from '@angular/core';
import { Location } from '@angular/common';
import { Store } from '@ngrx/store';
import { Observable } from 'rxjs';

import { UserDetail } from 'app/user.model';
import { Modal } from 'models/';
import { ModalService } from 'app/baiwa/common/services';
import { Nrq01000Service } from './nrq01000.service';
import { Router } from '@angular/router';
// import { Calendar, CalendarFormatter, CalendarLocal, CalendarType,  } from 'models/';
// import { FormControl, Validators, FormGroup } from '@angular/forms';
// import { ROLES, PAGE_AUTH } from 'app/baiwa/common/constants';
// import * as UserActions from 'app/user.action';

@Component({
  selector: 'app-nrq01000',
  templateUrl: './nrq01000.component.html',
  styleUrls: ['./nrq01000.component.css']
})
export class Nrq01000Component implements OnInit {

  // calendar: Calendar;
  // form: FormGroup = new FormGroup({
  //   calendar: new FormControl('', Validators.required)
  // });

  users: Observable<UserDetail>;
  constructor(
    private store: Store<{}>,
    private modal: ModalService,
    private service: Nrq01000Service,
    private router: Router,
    private location: Location,
    private cdRef: ChangeDetectorRef
  ) {
    // this.calendar = {
    //   calendarId: "example",
    //   calendarName: "example",
    //   formGroup: this.form,
    //   formControlName: "calendar",
    //   type: CalendarType.DATE,
    //   formatter: CalendarFormatter.DEFAULT,
    //   local: CalendarLocal.EN,
    //   initial: new Date()
    // };
    this.confirm();
  }

  ngOnInit() {
    // this.users = this.store.select('user');
  }

  ngAfterViewChecked() {
    this.cdRef.detectChanges();
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

  // formSubmit(form: FormGroup) {
  //   if (form.valid) {
  //     console.log(form);
  //   }
  // }

  // calendarValue(name, e) {
  //   this.form.controls[name].setValue(e);
  //   console.log(this.form);
  // }


  // Test NGRX
  // change() {
  //   const { ADMIN } = ROLES;
  //   let list = [];
  //   for (let key in PAGE_AUTH) {
  //     list.push(PAGE_AUTH[key]);
  //   }
  //   let data: UserDetail = { // Mock User Detail to Update
  //     userId: "000",
  //     username: "admin",
  //     firstName: "Administrator",
  //     lastName: "-",
  //     roles: [ADMIN],
  //     auths: list
  //   };
  //   this.store.dispatch(new UserActions.UpdateUser(data)); // Update UserDetail
  // }

  // reset() {
  //   this.store.dispatch(new UserActions.ResetUser(true));
  // }

}
