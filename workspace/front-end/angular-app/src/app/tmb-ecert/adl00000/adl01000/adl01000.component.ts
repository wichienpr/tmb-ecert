import { Component, OnInit } from '@angular/core';
import { AjaxService } from 'app/baiwa/common/services/ajax.service';
import { forEach } from '@angular/router/src/utils/collection';
import { Calendar, CalendarFormatter, CalendarLocal, CalendarType } from 'models/';
import { NgForm, FormGroup, FormControl, Validators } from '@angular/forms';
import { Adl01000Service } from './adl01000.service';
import { Certificate } from 'models/';
import { isValid } from 'app/baiwa/common/helpers';
declare var $: any;
const URL = {
  export:"/api/adl/adl01000/exportFile"
}
@Component({
  selector: 'app-adl01000',
  templateUrl: './adl01000.component.html',
  styleUrls: ['./adl01000.component.css'],
  providers: [Adl01000Service]
})
export class Adl01000Component implements OnInit {
  showData: boolean = false; 
  dataT: any[]= [];
  loading: boolean = false;

  dropdownObj: any;
  actionChanged: Certificate[];


  calendar1: Calendar;
  calendar2: Calendar;
  form: FormGroup;

  constructor(
    private ajax: AjaxService,
    private service: Adl01000Service
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
      icon: "time icon",
      endId: "cal2",
      initial: new Date
    };
    this.calendar2 = {
      calendarId: "cal2",
      calendarName: "cal2",
      formGroup: this.form,
      formControlName: "dateTo",
      type: CalendarType.DATE,
      formatter: CalendarFormatter.DEFAULT,
      local: CalendarLocal.EN,
      icon: "time icon",
      startId: "cal1",
      initial: new Date
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
  actionChange(e) {
    console.log("actionChanged : ",e);
      this.actionChanged = e;
  }


  getData=()=>{
    console.log(this.form);
    this.loading = true;
    this.dataT=[];
    const URL = "/api/adl/adl01000/list";
    this.ajax.post(URL,{
      dateForm: this.form.controls.dateForm.value,
      dateTo: this.form.controls.dateTo.value,
      createdById: this.form.controls.userId.value,
      actionCode:this.actionChanged
    
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
    if(this.form.valid){
    console.log("searchData True");
        this.showData = true;
        this.getData();
    }else{
      console.log("searchData False");
    }
   
  }
  
  clearData(): void {
    console.log("clearData");
    this.form.reset();
    $('#action').dropdown('restore defaults');
    this.showData = false;
  }

  exportFile=()=>{
    console.log("exportFile");
    let param = "";
    param+="?dateForm="+this.form.controls.dateForm.value;
    param+="&dateTo="+this.form.controls.dateTo.value;

    (this.form.controls.userId.value!=null)?param+="&userId="+this.form.controls.userId.value:"";

    (this.actionChanged!=null)?param+="&action="+this.actionChanged:"";

    this.ajax.download(URL.export+param);
  }

  validate(input: string, submitted: boolean) {
    return isValid(this.form, input, submitted);
  }
 
}
