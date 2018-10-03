import { Component, OnInit } from '@angular/core';
import { AjaxService } from 'app/baiwa/common/services/ajax.service';
import { forEach } from '@angular/router/src/utils/collection';
import { Calendar, CalendarFormatter, CalendarLocal, CalendarType } from 'models/';
import { NgForm, FormControl, Validators, FormGroup } from '@angular/forms';
import { Rep01000Service } from './rep01000.service';
import { Certificate } from 'models/';
declare var $: any;
const URL = {
  export:"/api/rep/rep01000/exportFile"
}
@Component({
  selector: 'app-rep01000',
  templateUrl: './rep01000.component.html',
  styleUrls: ['./rep01000.component.css'],
  providers: [Rep01000Service]
})
export class Rep01000Component implements OnInit {
  showData: boolean = false; 
  dataT: any[]= [];
  loading: boolean = false;

  dropdownObj: any;
  reqTypeChanged: Certificate[];

  calendar1: Calendar;
  calendar2: Calendar;
  form: FormGroup;

  constructor(
    private ajax: AjaxService,
    private service: Rep01000Service
  ) { 
    this.dropdownObj = this.service.getDropdownObj();
    this.service.getForm().subscribe(form => {
      this.form = form
    });
    this.calendar1 = {
      calendarId: "cal1",
      calendarName: "cal1",
      formGroup: this.form,
      formControlName: "dateForm",
      type: CalendarType.DATE,
      formatter: CalendarFormatter.DEFAULT,
      local: CalendarLocal.EN,
      icon: "time icon"
    };
    this.calendar2 = {
      calendarId: "cal2",
      calendarName: "cal2",
      formGroup: this.form,
      formControlName: "dateTo",
      type: CalendarType.DATE,
      formatter: CalendarFormatter.DEFAULT,
      local: CalendarLocal.EN,
      icon: "time icon"
    };
  }

  ngOnInit() {
  }

  ngAfterViewInit() {}

  calendarValue(name,e) {
    this.form.controls[name].setValue(e);
    console.log(this.form);
    console.log(this.form.controls[name].value);
    
  }
  reqTypeChange(e) {
    console.log("requestTypeCode : ",e);
      this.reqTypeChanged = e;
  }


  getData=()=>{
    console.log(this.form);
    this.loading = true;
    this.dataT=[];
    const URL = "/api/rep/rep01000/list";
    this.ajax.post(URL,{
      dateForm: this.form.controls.dateForm.value,
      dateTo: this.form.controls.dateTo.value,
      organizeId: this.form.controls.corpNo.value,
      companyName: this.form.controls.corpName.value,
      requestTypeCode:this.reqTypeChanged
      
    },async res => {
      const data = await res.json();
      
      setTimeout(() => {
        this.loading = false;
      },200);
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
    $('#reqtype').dropdown('restore defaults');
    this.showData = false;
  }

  exportFile=()=>{
    console.log("exportFile");
    this.ajax.download(URL.export);
  }

 
}
