import { Component, OnInit, ViewChild } from '@angular/core';
import { Store } from '@ngrx/store';
import { Observable } from 'rxjs';

import { UserDetail } from 'app/user.model';
import * as UserActions from 'app/user.action';
import { Calendar, CalendarFormatter, CalendarLocal, CalendarType } from 'models/';
import { NgForm, FormControl, Validators, FormGroup } from '@angular/forms';

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
    let data: UserDetail = { // Mock User Detail to Update
      userId: "99999",
      username: "kimjaeha",
      firstName: "อาทิตย์",
      lastName: "แก่นใจ",
      roles: ["Developer"]
    };
    this.store.dispatch(new UserActions.UpdateUser(data)); // Update UserDetail
  }

  reset() {
    this.store.dispatch(new UserActions.ResetUser(true));
  }

}
