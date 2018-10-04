import { Component, OnInit, ViewChild } from '@angular/core';
import { Store } from '@ngrx/store';
import { Observable } from 'rxjs';

import { UserDetail } from 'app/user.model';
import * as UserActions from 'app/user.action';
import { Calendar, CalendarFormatter, CalendarLocal, CalendarType } from 'models/';
import { NgForm, FormControl, Validators, FormGroup } from '@angular/forms';
import { ROLES, PAGE_AUTH } from 'app/baiwa/common/constants';

interface AppState { }

@Component({
  selector: 'app-nrq01000',
  templateUrl: './nrq01000.component.html',
  styleUrls: ['./nrq01000.component.css']
})
export class Nrq01000Component implements OnInit {

  calendar: Calendar;
  form: FormGroup = new FormGroup({
    calendar: new FormControl('', Validators.required)
  });

  users: Observable<UserDetail>;
  constructor(private store: Store<AppState>) {
    this.calendar = {
      calendarId: "example",
      calendarName: "example",
      formGroup: this.form,
      formControlName: "calendar",
      type: CalendarType.DATE,
      formatter: CalendarFormatter.DEFAULT,
      local: CalendarLocal.TH
    };
  }

  ngOnInit() {
    this.users = this.store.select('user');
  }

  formSubmit(form: FormGroup) {
    if (form.valid) {
      console.log(form);
    }
  }

  calendarValue(name, e) {
    this.form.controls[name].setValue(e);
    console.log(this.form);
  }


  // Test NGRX
  change() {
    const { ADMIN } = ROLES;
    let list = [];
    for(let key in PAGE_AUTH) {
      list.push(PAGE_AUTH[key]);
    }
    let data: UserDetail = { // Mock User Detail to Update
      userId: "000",
      username: "admin",
      firstName: "Administrator",
      lastName: "-",
      roles: [ADMIN],
      auths: list
    };
    this.store.dispatch(new UserActions.UpdateUser(data)); // Update UserDetail
  }

  reset() {
    this.store.dispatch(new UserActions.ResetUser(true));
  }

}
