import { Component, OnInit, ViewChild } from '@angular/core';
import { Store } from '@ngrx/store';
import { Observable } from 'rxjs';

import { UserDetail } from 'app/user.model';
import * as UserActions from 'app/user.action';
import { Calendar } from 'models/';
import { NgForm, FormControl, Validators } from '@angular/forms';

interface AppState { }

@Component({
  selector: 'app-nrq01000',
  templateUrl: './nrq01000.component.html',
  styleUrls: ['./nrq01000.component.css']
})
export class Nrq01000Component implements OnInit {

  calendar: Calendar;

  users: Observable<UserDetail>;
  constructor(private store: Store<AppState>) {
    this.calendar = {
      calendarId: "example",
      calendarName: "example",
      type: "date"
    };
  }

  ngOnInit() {
    this.users = this.store.select('user');
  }

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

  calendarValue(e) {
    console.log(e);
  }

}
