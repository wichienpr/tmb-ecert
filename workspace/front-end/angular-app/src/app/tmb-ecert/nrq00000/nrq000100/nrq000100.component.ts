import { Component, OnInit } from '@angular/core';
import { Store } from '@ngrx/store';
import { UserDetail } from '../../../user.reducer';
import { Observable } from 'rxjs';

interface AppState {}

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
    this.users.subscribe((u : UserDetail)=>{
      console.log(u.username);
    })
  }

}
