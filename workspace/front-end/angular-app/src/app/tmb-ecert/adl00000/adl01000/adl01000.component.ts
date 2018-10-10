import { Component, OnInit, ViewChild } from '@angular/core';
import { AjaxService } from 'app/baiwa/common/services/ajax.service';
import { Calendar, CalendarFormatter, CalendarLocal, CalendarType, Dropdown, Lov, DropdownMode } from 'models/';
import { NgForm, FormGroup, FormControl, Validators } from '@angular/forms';
import { Adl01000Service } from './adl01000.service';
import { Certificate } from 'models/';
import { NgCalendarConfig } from 'app/baiwa/common/components/calendar/ng-calendar.component';
import { DatatableCofnig, DatatableDirective } from 'app/baiwa/common/directives/datatable/datatable.directive';
import { CommonService } from 'app/baiwa/common/services';
declare var $: any;
const URL = {
  export: "/api/adl/adl01000/exportFile"
}
@Component({
  selector: 'app-adl01000',
  templateUrl: './adl01000.component.html',
  styleUrls: ['./adl01000.component.css'],
  providers: [Adl01000Service]
})
export class Adl01000Component implements OnInit {
  showData: boolean = false;
  dataT: any[] = [];
  loading: boolean = false;

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
      formControll: this.formadl.get("dateForm"),
      endCalendar: "calendar2"
    };

    this.calendar2 = {
      id: "calendar2",
      formControll: this.formadl.get("dateTo"),
      startCalendar: "calendar1"
    };

    this.auditConfig = {
      url: "/api/adl/adl01000/list",
      serverSide: true,
      useBlockUi : true
    };

  }

  ngAfterViewInit() { }

  calendarValueDateForm(value) {
    console.log("calendarValueDateForm : ", value);
    // this.formadl.controls[name].setValue(e);

    this.formadl.patchValue({ "dateForm": value })
  }

  calendarValueDateTo(value) {
    console.log("calendarValueDateTo : ", value);
    // this.formadl.controls[name].setValue(e);

    this.formadl.patchValue({ "dateTo": value })
  }


  searchData(): void {
    console.log("searchData False", this.formadl.value);
    this.auditDt.searchParams(this.formadl.value);
    this.auditDt.search();
  }

  clearData(): void {
    this.formadl.reset();
  }

  exportFile() {
    // console.log("exportFile");
    // let param = "";
    // param += "?dateForm=" + this.form.controls.dateForm.value;
    // param += "&dateTo=" + this.form.controls.dateTo.value;

    // (this.formadl.controls.userId.value != null) ? param += "&userId=" + this.form.controls.userId.value : "";

    // (this.actionChanged != null) ? param += "&action=" + this.actionChanged : "";

    // this.ajax.download(URL.export + param);
  }

  get dateForm() {
    return this.formadl.get("dateForm");
  }

  get dateTo() {
    return this.formadl.get("dateTo");
  }

}
