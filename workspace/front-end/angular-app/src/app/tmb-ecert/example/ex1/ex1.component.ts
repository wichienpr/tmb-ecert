import { Component, OnInit, ViewChild } from '@angular/core';
import { NgForm } from '@angular/forms';
import { Store } from '@ngrx/store';
import { Observable } from 'rxjs';

import { getEx1State } from 'app/tmb-ecert/example/example.reducer';
import { Ex1 } from './ex1.model';
import * as Ex1Actions from './ex1.actions';

@Component({
  selector: 'app-ex1',
  templateUrl: './ex1.component.html',
  styleUrls: ['./ex1.component.css']
})
export class Ex1Component implements OnInit {

  @ViewChild("form") form: NgForm;

  ex1: Observable<Ex1[]>;

  constructor(private store: Store<{}>) { }

  ngOnInit() {
    this.ex1 = this.store.select(getEx1State);
  }

  add(form: NgForm) {
    const { id, name, value } = form.controls;
    const data: Ex1 = {
      id: id.value,
      name: name.value,
      value: value.value
    };
    this.store.dispatch(new Ex1Actions.AddEx1(data));
  }

  delete(index: number) {
    this.store.dispatch(new Ex1Actions.DeleteEx1(index));
  }

}
