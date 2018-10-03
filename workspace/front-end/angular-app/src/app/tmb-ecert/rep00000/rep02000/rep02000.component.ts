import { Component, OnInit } from '@angular/core';
import { AjaxService, ModalService, DropdownService } from "services/";
import { Modal } from "models/";
import { forEach } from '@angular/router/src/utils/collection';
import { Calendar, CalendarFormatter, CalendarLocal, CalendarType } from 'models/';
import { NgForm, FormControl, Validators, FormGroup } from '@angular/forms';

import { Certificate } from 'models/';
import { Rep02000Service } from 'app/tmb-ecert/rep00000/rep02000/rep02000.service';
import { ActivatedRoute, Router } from '@angular/router';

declare var $: any;
const URL = {
  export:"/api/rep/rep02000/exportFile"
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
    private modal: ModalService,
    private service: Rep02000Service,
    private route: ActivatedRoute,
    private router: Router
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

  ngOnInit() {
    let dForm = this.route.snapshot.queryParams["dateForm"];
    let dTo = this.route.snapshot.queryParams["dateTo"];
    if(dForm!=""||dTo!=""){
      this.form.controls[`dateForm`].setValue(dForm); 
      this.form.controls[`dateTo`].setValue(dTo); 
      this.searchData();
    }
    console.log("form : ",this.form);
  }

  ngAfterViewInit() {}

  calendarValue(name,e) {
    this.form.controls[name].setValue(e);
    // console.log(this.form);
    // console.log(this.form.controls[name].value);
    
  }

  getData=()=>{
    console.log(this.form);
    this.loading = true;
    this.dataT=[];
    const URL = "/api/rep/rep02000/list";
    this.ajax.post(URL,{
      dateForm: this.form.controls.dateForm.value,
      dateTo: this.form.controls.dateTo.value
      
    },async res => {
      const data = await res.json();
      
      setTimeout(() => {
        this.loading = false;
      },200);

      data.forEach(element => {
        this.dataT.push(element);
      });
    console.log("getData True : Data length",this.dataT.length);
    console.log("getData True : Data ",this.dataT);
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
  remark=custsegmentCode=>{
    this.router.navigate(['/rep/rep02100'], {
      queryParams: {  custsegmentCode:custsegmentCode,
                      dateForm:this.form.controls.dateForm.value,
                      dateTo:this.form.controls.dateTo.value}
    });
  }
  openModalDetails=department=>{
    const modal: Modal = {
      title:"รายละเอียด",
      msg: "ชื่อหน่อยงาน : "+department,
      success: false
  };
  this.modal.alert(modal);
  }

}