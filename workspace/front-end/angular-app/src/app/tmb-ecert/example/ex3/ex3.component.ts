import { Component, OnInit, ViewChild } from '@angular/core';
import { NgForm } from '@angular/forms';
import { Store } from '@ngrx/store';
import { Observable } from 'rxjs';

import { Ex3 } from './ex3.model';
import * as Ex3Actions from './ex3.actions';
import { getEx3State } from 'app/tmb-ecert/example/example.reducer';

@Component({
  selector: 'app-ex3',
  templateUrl: './ex3.component.html',
  styleUrls: ['./ex3.component.css']
})
export class Ex3Component implements OnInit {

  @ViewChild("form") form: NgForm;

  ex3: Observable<Ex3[]>;

  constructor(private store: Store<{}>) {
    this.ex3 = this.store.select(getEx3State);
  }

  ngOnInit() {
  }

  onSubmit(form: NgForm) {
    const { name, no } = form.controls;
    let _name = name.value ? name.value : '';
    let _no = no.value ? no.value : '';
    this.store.dispatch(new Ex3Actions.SearchEx3({ name: _name, no: _no }));
  }

}
