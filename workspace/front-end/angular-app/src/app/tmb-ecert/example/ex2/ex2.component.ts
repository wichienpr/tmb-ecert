import { Component, OnInit } from '@angular/core';
import { Store } from '@ngrx/store';
import { Observable } from 'rxjs';

import { Ex2 } from './ex2.model';
import * as Ex2Actions from './ex2.actions';
import { getEx2State } from 'app/tmb-ecert/example/example.reducer';

@Component({
  selector: 'app-ex2',
  templateUrl: './ex2.component.html',
  styleUrls: ['./ex2.component.css']
})
export class Ex2Component implements OnInit {

  ex2: Observable<Ex2>;

  constructor(private store: Store<{}>) { }

  ngOnInit() {
    this.ex2 = this.store.select(getEx2State);
  }

  plus() {
    this.store.dispatch(new Ex2Actions.InCreseEx2(1));
  }

  minus() {
    this.store.dispatch(new Ex2Actions.DeCreseEx2(1));
  }

}
