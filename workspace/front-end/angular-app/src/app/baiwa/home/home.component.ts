import { Component, OnInit } from '@angular/core';
import { UserDetail } from 'app/user.model';
import { Observable } from 'rxjs';
import { Store } from '@ngrx/store';

@Component({
  selector: 'app-home',
  templateUrl: './home.component.html',
  styleUrls: ['./home.component.css']
})
export class HomeComponent implements OnInit {

  userdetail: Observable<UserDetail>;

  constructor(private store: Store<AppState>) {
    this.userdetail = this.store.select("user");
  }

  ngOnInit() {

  }

}

interface AppState {
  user: UserDetail
}
