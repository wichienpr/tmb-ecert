import { Component, OnInit } from '@angular/core';
import { Store } from '@ngrx/store';
import { Observable } from 'rxjs';
import { UserDetail } from 'app/user.model';

import * as UserActions from 'app/user.action';

interface AppState { }

@Component({
  selector: 'app-nrq000100',
  templateUrl: './nrq000100.component.html',
  styleUrls: ['./nrq000100.component.css']
})
export class Nrq000100Component implements OnInit {

  users: Observable<UserDetail>;
  constructor(private store: Store<AppState>) {

  }

  ngOnInit() {
    this.users = this.store.select('user');
  }

  change() {
    let data: UserDetail = { // Mock User Detail to Update
      username: "RyanGek",
      firstName: "Arthit",
      lastName: "Kanjai",
      roles: ["user"]
    };
    this.store.dispatch(new UserActions.UpdateUser(data)); // Update UserDetail
  }

}
