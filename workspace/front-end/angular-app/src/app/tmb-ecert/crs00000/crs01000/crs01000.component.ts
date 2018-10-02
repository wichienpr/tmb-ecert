import { Component, OnInit, ViewChild } from '@angular/core';
import { Crs01000Service } from 'app/tmb-ecert/crs00000/crs01000/crs01000.service';
import { Calendar, CalendarFormatter, CalendarLocal, CalendarType } from 'models/';
import { NgForm, FormGroup, FormControl, Validators } from '@angular/forms';
import { AjaxService } from 'app/baiwa/common/services/ajax.service';
import { Router, ActivatedRoute, Params } from "@angular/router";

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
  idReq : any;
  dataT: any[]= [];
  showData: boolean = false; 

  constructor(private crs01000Service : Crs01000Service,private ajax: AjaxService,private router: Router,) { 
    
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
   //this.crs01000Service.findReqFormByStatus()
    this.getData();
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
    $('.ui.sidebar')
      .sidebar({
        context: '.ui.grid.pushable'
      })
      .sidebar('setting', 'transition', 'push')
      .sidebar('toggle');
  }


  calendarValue(name,e) {
    this.form.controls[name].setValue(e);
    console.log(this.form);
    console.log(this.form.controls[name].value);
  
  }


  getData=()=>{
    console.log(this.form);
    this.dataT=[];
    const URL = "crs/crs01000/findReqFormByStatus";
    this.ajax.post(URL,{
      // reqDate: this.form.controls.reqDate.value,
      // toReqDate: this.form.controls.toReqDate.value,
      // organizeId: this.form.controls.organizeId.value,
      // companyName: this.form.controls.companyName.value,
      // tmbReqNo: this.form.controls.tmbReqNo.value,
     

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
    $('#reqtype').dropdown('restore defaults');
    this.showData = false;
  }

  description(idReq): void {
    console.log(idReq)
    this.router.navigate(["/nrq03000"], {
      queryParams: { id: idReq }
    });
  }

  
}

