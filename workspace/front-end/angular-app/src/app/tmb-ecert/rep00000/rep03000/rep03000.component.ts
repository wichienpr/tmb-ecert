import { Component, OnInit } from '@angular/core';
import { AjaxService, ModalService, DropdownService } from "services/";
import { Modal } from "models/";
import { forEach } from '@angular/router/src/utils/collection';
import { Calendar, CalendarFormatter, CalendarLocal, CalendarType } from 'models/';
import { NgForm, FormControl, Validators, FormGroup } from '@angular/forms';

import { Certificate } from 'models/';
import { ActivatedRoute, Router } from '@angular/router';
import { Rep03000Service } from 'app/tmb-ecert/rep00000/rep03000/rep03000.service';

declare var $: any;
const URL = {
  export:"/api/rep/rep03000/exportFile"
}
@Component({
  selector: 'app-rep03000',
  templateUrl: './rep03000.component.html',
  styleUrls: ['./rep03000.component.css'],
  providers: [Rep03000Service]
})
export class Rep03000Component implements OnInit {
  showData: boolean = false; 
  dataT: any[]= [];
  loading: boolean = false;

  calendar1: Calendar;
  form: FormGroup;

  constructor(
    private ajax: AjaxService,
    private modal: ModalService,
    private service: Rep03000Service,
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
      formControlName: "dateVat",
      type: CalendarType.MONTH,
      formatter: CalendarFormatter.MMyyyy,
      local: CalendarLocal.EN,
      icon: "time icon"
    };
   }

  ngOnInit() {}
  ngAfterViewInit() {}
  
  calendarValue(name,e) {
    this.form.controls[name].setValue(e);
    console.log(this.form);
    console.log(this.form.controls[name].value);
    
  }

  getData=()=>{
    console.log(this.form);
    this.loading = true;
    this.dataT=[];
    const URL = "/api/rep/rep03000/list";
    this.ajax.post(URL,{
      dateVat: this.form.controls.dateVat.value,
      organizeId: this.form.controls.organizeId.value,
      customerName: this.form.controls.customerName.value
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

}