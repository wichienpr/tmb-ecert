import { Component, OnInit, ViewChild } from '@angular/core';
import { AjaxService } from 'app/baiwa/common/services/ajax.service';
import { Calendar, CalendarFormatter, CalendarLocal, CalendarType, Dropdown, Lov, DropdownMode } from 'models/';
import { NgForm, FormGroup, FormControl, Validators } from '@angular/forms';
import { Adl01000Service } from './adl01000.service';
import { Certificate } from 'models/';
import { NgCalendarConfig } from 'app/baiwa/common/components/calendar/ng-calendar.component';
import { DatatableCofnig, DatatableDirective } from 'app/baiwa/common/directives/datatable/datatable.directive';
import { CommonService } from 'app/baiwa/common/services';


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

  constructor(
    private ajax: AjaxService,
    private service: Adl01000Service,
    private commonsvr : CommonService
  ) {
  }

  ngOnInit() {

    this.formadl = new FormGroup({
      dateForm: new FormControl('', Validators.required),             // วันที่ดำเนินการ
      dateTo: new FormControl('', Validators.required),               // ถึงวันที่
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
    // console.log("searchData False", this.formadl.value);
    this.auditDt.searchParams(this.formadl.value);
    this.auditDt.search();
  }

  clearData(): void {
    this.formadl.reset();
    this.auditDt.clear();
  }

  exportFile() {
    if(this.formadl.invalid){
      // console.log("form invalid");
      return false;
    }

    let urldownload = "/api/adl/adl01000/exportFile" + "?" + this.commonsvr.toGetQuery(this.formadl.value);
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
