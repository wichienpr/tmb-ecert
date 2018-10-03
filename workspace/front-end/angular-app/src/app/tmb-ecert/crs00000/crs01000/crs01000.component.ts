import { Component, OnInit, ViewChild } from '@angular/core';
import { Crs01000Service } from 'app/tmb-ecert/crs00000/crs01000/crs01000.service';
import { Calendar, CalendarFormatter, CalendarLocal, CalendarType } from 'models/';
import { NgForm, FormGroup, FormControl, Validators } from '@angular/forms';
import { AjaxService } from 'app/baiwa/common/services/ajax.service';
import { Router, ActivatedRoute, Params } from "@angular/router";
import { isValid } from 'app/baiwa/common/helpers';

declare var $: any;
@Component({
  selector: 'app-crs01000',
  templateUrl: './crs01000.component.html',
  styleUrls: ['./crs01000.component.css'],
  providers: [Crs01000Service]
})
export class Crs01000Component implements OnInit {
  calendar1: Calendar;
  calendar2: Calendar;
  form: FormGroup;
  idReq: any;
  dataT: any[] = [];
  showData: boolean = false;
  loading: boolean = false;
  status: string;

  countStatus1:Number;
  countStatus2:Number;
  countStatus3:Number;
  countStatus4:Number;
  countStatus5:Number;
  countStatus6:Number;
  countStatus7:Number;
  countStatus8:Number;
  countStatus9:Number;
  countStatus10:Number;
  countStatus11:Number;

  constructor(private crs01000Service: Crs01000Service, private ajax: AjaxService, private router: Router, ) {

    this.crs01000Service.getForm().subscribe(form => {
      this.form = form
    });
    
    this.calendar1 = {
      calendarId: "cal1",
      calendarName: "cal1",
      formGroup: this.form,
      formControlName: "reqDate",
      type: CalendarType.DATE,
      formatter: CalendarFormatter.DEFAULT,
      local: CalendarLocal.EN,
      icon: "time icon"
    };
    this.calendar2 = {
      calendarId: "cal2",
      calendarName: "cal2",
      formGroup: this.form,
      formControlName: "toReqDate",
      type: CalendarType.DATE,
      formatter: CalendarFormatter.DEFAULT,
      local: CalendarLocal.EN,
      icon: "time icon"
    };

  }

  ngOnInit() {
    this.getCountStatus();
  }

  ngAfterViewInit() {
    $('.ui.sidebar')
      .sidebar({
        context: '.ui.grid.pushable'
      })
      .sidebar('setting', 'transition', 'push')
      .sidebar('toggle');
  }

  onToggle() {
    this.getCountStatus();
    $('.ui.sidebar')
      .sidebar({
        context: '.ui.grid.pushable'
      })
      .sidebar('setting', 'transition', 'push')
      .sidebar('toggle');
  }


  calendarValue(name, e) {
    this.form.controls[name].setValue(e);
    console.log(this.form);
    console.log(this.form.controls[name].value);

  }


  getData = () => {
    console.log(this.form);

    const URL = "/api/crs/crs01000/findReq";
    this.ajax.post(URL, {
      reqDate: this.form.controls.reqDate.value,
      toReqDate: this.form.controls.toReqDate.value,
      organizeId: this.form.controls.organizeId.value,
      companyName: this.form.controls.companyName.value,
      tmbReqNo: this.form.controls.tmbReqNo.value,

    }, async res => {
      const data = await res.json();
      data.forEach(element => {
        this.dataT.push(element);
      });
      console.log("getData True : Data s", this.dataT);
    });
  }



  getDataByStatus(code) {

    this.status = code;
    const URL = "/api/crs/crs01000/findReqByStatus";
    this.ajax.post(URL, { status: this.status }, res => {
      console.log(res.json());
      res.json().forEach(element => {
        this.dataT.push(element);
      });
    });

  }

  getCountStatus() {
    const URL = "/api/crs/crs01000/countStatus";
    this.ajax.post(URL, { }, res => {
      this.countStatus1 = res.json().countStatus1;
      this.countStatus2 = res.json().countStatus2;
      this.countStatus3 = res.json().countStatus3;
      this.countStatus4 = res.json().countStatus4;
      this.countStatus5 = res.json().countStatus5;
      this.countStatus6 = res.json().countStatus6;
      this.countStatus7 = res.json().countStatus7;
      this.countStatus8 = res.json().countStatus8;
      this.countStatus9 = res.json().countStatus9;
      this.countStatus10 = res.json().countStatus10;
      this.countStatus11 = res.json().countStatus11;
    });

  }

  searchData(): void {
    console.log("searchData");
    this.showData = true;
    this.getData();
    this.dataT = [];
  }

  searchStatus(code): void {
    $('.ui.sidebar')
    .sidebar({
      context: '.ui.grid.pushable'
    })
    .sidebar('setting', 'transition', 'push')
    .sidebar('toggle');


    console.log("searchStatus");
    this.showData = true;
    this.getDataByStatus(code);
    this.dataT = [];
  }

  clearData(): void {
    console.log("clearData");
    this.form.reset();
    this.showData = false;
  }

  description(idReq): void {
    console.log(idReq)
    this.router.navigate(["/nrq/nrq03000"], {
      queryParams: { id: idReq }
    });
  }



  validate(input: string, submitted: boolean) {
    return isValid(this.form, input, submitted);
  }
}



