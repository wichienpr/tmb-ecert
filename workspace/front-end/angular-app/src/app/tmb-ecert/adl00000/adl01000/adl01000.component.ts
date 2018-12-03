import { Component, OnInit, ViewChild } from '@angular/core';
import { AjaxService } from 'app/baiwa/common/services/ajax.service';
import { Calendar, CalendarFormatter, CalendarLocal, CalendarType, Dropdown, Lov, DropdownMode } from 'models/';
import { NgForm, FormGroup, FormControl, Validators } from '@angular/forms';
import { Adl01000Service } from './adl01000.service';
import { Certificate } from 'models/';
import { NgCalendarConfig } from 'app/baiwa/common/components/calendar/ng-calendar.component';
import { DatatableCofnig, DatatableDirective } from 'app/baiwa/common/directives/datatable/datatable.directive';
import { CommonService } from 'app/baiwa/common/services';
import * as moment from 'moment';
import { DropdownComponent } from 'app/baiwa/common/components/dropdown/dropdown.component';
import {  dateLocaleEN, ThDateToEnDate, ThYearToEnYear, dateLocale, EnYearToThYear,EnDateToThDate } from "helpers/";

@Component({
  selector: 'app-adl01000',
  templateUrl: './adl01000.component.html',
  styleUrls: ['./adl01000.component.css'],
  providers: [Adl01000Service]
})
export class Adl01000Component implements OnInit {

  actionDropdown: Dropdown;
  actionChanged: Certificate[];
  calendar1: NgCalendarConfig;
  calendar2: NgCalendarConfig;
  formadl: FormGroup;
  auditConfig: DatatableCofnig;
  @ViewChild("auditDt")
  auditDt: DatatableDirective

  @ViewChild("statusDropDown")
  statusDropDown: DropdownComponent;

  constructor(
    private ajax: AjaxService,
    private service: Adl01000Service,
    private commonsvr : CommonService
  ) {
  }

  ngOnInit() {
    let now = moment().format('DD/MM/YYYY');
    this.formadl = new FormGroup({
      dateForm: new FormControl(now, Validators.required),             // วันที่ดำเนินการ
      dateTo: new FormControl(now, Validators.required),               // ถึงวันที่
      createdById: new FormControl(''),                                      // User ID
      actionCode: new FormControl(''),                                      // Action
    });

    this.actionDropdown = {
      dropdownId: "actionCode",
      dropdownName: "actionCode",
      type: DropdownMode.SEARCH,
      formGroup: this.formadl,
      formControlName: "actionCode",
      values: [],
      valueName: "code",
      labelName: "name",
      placehold: "กรุณาเลือก"
    };

    this.service.getActionDropdown().subscribe((obj: Lov[]) => this.actionDropdown.values = obj)

    this.calendar1 = {
      id: "calendar1",
      formControl: this.formadl.get("dateForm"),
      endCalendar: "calendar2"
    };

    this.calendar2 = {
      id: "calendar2",
      formControl: this.formadl.get("dateTo"),
      startCalendar: "calendar1"
    };

    this.auditConfig = {
      url: "/api/adl/adl01000/list",
      serverSide: true,
      useBlockUi : true
    };

  }

  ngAfterViewInit() { }

  searchData() {
    // console.log(this.formadl);

    if(!this.formadl.touched){
      Object.keys(this.formadl.value).forEach(element => {
        let fc = this.formadl.get(element);
        fc.markAsTouched({onlySelf : true});
      });
    }
    
    if(this.formadl.invalid){
      // console.log("form invalid");
      return false;
    }
    let strFrom = this.formadl.get("dateForm").value;
    let strTo = this.formadl.get("dateTo").value;
    let type = this.formadl.get("createdById").value;
    let oper = this.formadl.get("actionCode").value
    let param = {
      dateForm: ThDateToEnDate(strFrom),  
      dateTo: ThDateToEnDate(strTo),             
      createdById: type,    
      actionCode:oper,   
    }
     
    // console.log("searchData False", this.formadl.value);
    this.auditDt.searchParams(param);
    this.auditDt.search();
  }

  clearData(): void {
    // this.formadl.reset();
    let now =  EnDateToThDate(moment().format('DD/MM/YYYY'));
    this.formadl.setValue({dateForm:now,dateTo:now,createdById:'',actionCode:""});
    this.auditDt.clear();
    this.statusDropDown.clear();
  }

  exportFile() {
    if(this.formadl.invalid){
      // console.log("form invalid");
      return false;
    }
    let strFrom = this.formadl.get("dateForm").value;
    let strTo = this.formadl.get("dateTo").value;
    let type = this.formadl.get("createdById").value;
    let oper = this.formadl.get("actionCode").value
    let param = {
      dateForm: ThDateToEnDate(strFrom),  
      dateTo: ThDateToEnDate(strTo),             
      createdById: type,    
      actionCode:oper,   
    }

    let urldownload = "/api/adl/adl01000/exportFile" + "?" + this.commonsvr.toGetQuery(param);
    // console.log(urldownload);
    this.ajax.download(urldownload);
  }

  get dateForm() {
    return this.formadl.get("dateForm");
  }

  get dateTo() {
    return this.formadl.get("dateTo");
  }

}
