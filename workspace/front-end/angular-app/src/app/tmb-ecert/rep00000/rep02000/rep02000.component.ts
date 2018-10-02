import { Component, OnInit } from '@angular/core';
import { AjaxService } from 'app/baiwa/common/services/ajax.service';
import { forEach } from '@angular/router/src/utils/collection';
import { Calendar, CalendarFormatter, CalendarLocal, CalendarType } from 'models/';
import { NgForm, FormControl, Validators, FormGroup } from '@angular/forms';

import { Certificate } from 'models/';
import { Rep02000Service } from 'app/tmb-ecert/rep00000/rep02000/rep02000.service';
declare var $: any;
const URL = {
  export:"rep/rep02000/exportFile"
}
@Component({
  selector: 'app-rep02000',
  templateUrl: './rep02000.component.html',
  styleUrls: ['./rep02000.component.css'],
  providers: [Rep02000Service]
})
export class Rep02000Component implements OnInit {
  showData: boolean = false; 
  dataT: any[]= [];
  loading: boolean = false;

  calendar1: Calendar;
  calendar2: Calendar;
  form: FormGroup;

  constructor(
    private ajax: AjaxService,
    private service: Rep02000Service
  ) {
    this.service.getForm().subscribe(form => {
      this.form = form
    });
    this.calendar1 = {
      calendarId: "cal1",
      calendarName: "cal1",
      formGroup: this.form,
      formControlName: "dateForm",
      type: CalendarType.MONTH,
      formatter: CalendarFormatter.MMyyyy,
      local: CalendarLocal.EN,
      icon: "time icon"
    };
    this.calendar2 = {
      calendarId: "cal2",
      calendarName: "cal2",
      formGroup: this.form,
      formControlName: "dateTo",
      type: CalendarType.MONTH,
      formatter: CalendarFormatter.MMyyyy,
      local: CalendarLocal.EN,
      icon: "time icon"
    };
  }

  ngOnInit() { }
  ngAfterViewInit() {

  }
  calendarValue(name,e) {
    this.form.controls[name].setValue(e);
    console.log(this.form);
    console.log(this.form.controls[name].value);
    
  }

  getData=()=>{
    console.log(this.form);
    this.dataT=[];
    const URL = "rep/rep02000/list";
    this.ajax.post(URL,{
      dateForm: this.form.controls.dateForm.value,
      dateTo: this.form.controls.dateTo.value
      
    },async res => {
      const data = await res.json();
      data.forEach(element => {
        this.dataT.push(element);
      });
    console.log("getData True : Data s",this.dataT);
    });
  }

  searchData(): void {
    console.log("searchData");
    this.showData = true;
    this.getData();
  }
  
  clearData(): void {
    console.log("clearData");
    this.form.reset();
    this.showData = false;
  }

  exportFile=()=>{
    console.log("exportFile");
    this.ajax.download(URL.export);
  }


}
