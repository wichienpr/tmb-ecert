import { Component, OnInit } from '@angular/core';
import { Store } from '@ngrx/store';
import { Observable } from 'rxjs';

import { Ex2 } from './ex2.model';
import * as Ex2Actions from './ex2.actions';
import { getEx2State } from 'app/tmb-ecert/example/example.reducer';
import { Modal, Calendar, CalendarType, CalendarFormatter, CalendarLocal } from 'app/baiwa/common/models';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';

declare var $: any;

@Component({
  selector: 'app-ex2',
  templateUrl: './ex2.component.html',
  styleUrls: ['./ex2.component.css']
})
export class Ex2Component implements OnInit {

  form: FormGroup;

  ex2: Observable<Ex2>;
  modal: Modal;
  calendar: Calendar;
  callCalendar: boolean;

  constructor(private store: Store<{}>, private formBuilder: FormBuilder) { }

  ngOnInit() {
    this.form = this.formBuilder.group({
      calendar: ['', Validators.required]
    });
    this.modal = {
      modalId: "modal",
      type: "custom"
    };
    this.calendar = {
      calendarId: `calendar`,
      calendarName: `calendar`,
      formGroup: this.form,
      formControlName: `calendar`,
      type: CalendarType.DATE,
      formatter: CalendarFormatter.DEFAULT,
      local: CalendarLocal.EN,
      icon: 'calendar'
    };
    // NGRX
    this.ex2 = this.store.select(getEx2State);
  }

  plus() {
    this.store.dispatch(new Ex2Actions.InCreseEx2(1));
  }

  minus() {
    this.store.dispatch(new Ex2Actions.DeCreseEx2(1));
  }

  toggleModal() {
    $('#modal').modal('show');
    this.callCalendar = true;
  }

}
