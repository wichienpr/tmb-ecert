import { Component, OnInit, ChangeDetectorRef, AfterViewInit, ViewChild } from '@angular/core';
import { AjaxService } from 'app/baiwa/common/services/ajax.service';
import { Calendar, CalendarFormatter, CalendarLocal, CalendarType } from 'models/';
import { FormGroup } from '@angular/forms';
import { Rep01000Service } from './rep01000.service';
import { Certificate } from 'models/';
import { ThDateToEnDate } from 'app/baiwa/common/helpers';
import { NgCalendarComponent, NgCalendarConfig } from 'app/baiwa/common/components/calendar/ng-calendar.component';
import { DatatableDirective, DatatableCofnig } from 'app/baiwa/common/directives/datatable/datatable.directive';

declare var $: any;

const URL = {
  export: "/api/rep/rep01000/exportFile"
}

@Component({
  selector: 'app-rep01000',
  templateUrl: './rep01000.component.html',
  styleUrls: ['./rep01000.component.css'],
  providers: [Rep01000Service]
})
export class Rep01000Component implements OnInit, AfterViewInit {

  @ViewChild("repDT")
  repDT: DatatableDirective;

  @ViewChild("calendarFrom")
  calendarFrom: NgCalendarComponent;

  @ViewChild("calendarTo")
  calendarTo: NgCalendarComponent;

  @ViewChild("calendarPaymentFrom")
  calendarPaymentFrom: NgCalendarComponent;

  @ViewChild("calendarPaymentTo")
  calendarPaymentTo: NgCalendarComponent;
  
  showData: boolean = false;
  dataT: any[] = [];
  loading: boolean = false;

  dropdownObj: any;
  reqTypeChanged: Certificate[];
  paidTypeChanged: Certificate[];

  calendar1: Calendar;
  calendar2: Calendar;
  form: FormGroup;

  calendarFromConig: NgCalendarConfig;
  calendarToConig: NgCalendarConfig;

  calendarPaymentFromConig: NgCalendarConfig;
  calendarPaymentToConig: NgCalendarConfig;

  repDTConfig: DatatableCofnig;
  constructor(
    private ajax: AjaxService,
    private service: Rep01000Service,
    private cdRef: ChangeDetectorRef,
  ) {
    this.dropdownObj = this.service.getDropdownObj();
    this.service.getForm().subscribe(form => {
      this.form = form
    });

    this.calendarFromConig = {
      id: "dateForm",
      formControl: this.form.get("dateForm"),
      endCalendar: "dateTo",
      formatter: "dd/mm/yyyy"
    };
    this.calendarToConig = {
      id: "dateTo",
      formControl: this.form.get("dateTo"),
      startCalendar: "dateForm",
      formatter: "dd/mm/yyyy"
    };

    this.calendarPaymentFromConig = {
      id: "paymentDateForm",
      formControl: this.form.get("paymentDateForm"),
      endCalendar: "paymentDateTo",
      formatter: "dd/mm/yyyy"
    };
    this.calendarPaymentToConig = {
      id: "paymentDateTo",
      formControl: this.form.get("paymentDateTo"),
      startCalendar: "paymentDateForm",
      formatter: "dd/mm/yyyy"
    };
    // this.calendar1 = {
    //   calendarId: "cal1",
    //   calendarName: "cal1",
    //   formGroup: this.form,
    //   formControlName: "dateForm",
    //   type: CalendarType.DATE,
    //   formatter: CalendarFormatter.DEFAULT,
    //   local: CalendarLocal.EN,
    //   icon: "time icon",
    //   endId: "cal2",
    //   initial: new Date
    // };
    // this.calendar2 = {
    //   calendarId: "cal2",
    //   calendarName: "cal2",
    //   formGroup: this.form,
    //   formControlName: "dateTo",
    //   type: CalendarType.DATE,
    //   formatter: CalendarFormatter.DEFAULT,
    //   local: CalendarLocal.EN,
    //   icon: "time icon",
    //   startId: "cal1",
    //   initial: new Date
    // };
  }

  ngOnInit() {

    this.repDTConfig = {
      url: "/api/rep/rep01000/list",
      serverSide: true,
      useBlockUi: true
    };
   }

  ngAfterViewChecked() {
    this.cdRef.detectChanges();
  }

  ngAfterViewInit() { }

  calendarValue(name, e) {
    this.form.controls[name].setValue(e);
    // console.log(this.form);
    // console.log(this.form.controls[name].value);
  }
  reqTypeChange(e) {
    // console.log("requestTypeCode : ", e);
    this.reqTypeChanged = e;
  }
  paidTypeChange(e) {
    // console.log("requestTypeCode : ", e);
    this.paidTypeChanged = e;
  }

  getData() {
    this.loading = true;
    this.dataT = [];
    const URL = "/api/rep/rep01000/list";
    // console.log(typeof ThDateToEnDate(this.form.get('dateForm').value));
    // console.log("ON SEARCH REP01000 => FROM " + ThDateToEnDate(this.form.get('dateForm').value) + " TO " + ThDateToEnDate(this.form.get('dateTo').value));
    this.ajax.post(URL, {
      dateForm: ThDateToEnDate(this.form.get('dateForm').value),
      dateTo: ThDateToEnDate(this.form.get('dateTo').value),
      organizeId: this.form.get('organizeId').value,
      companyName: this.form.get('companyName').value,
      requestTypeCode: this.reqTypeChanged,
      paidtypeCode: this.paidTypeChanged
    }, res => {
      const data = res.json();
      setTimeout(() => {
        this.loading = false;
      }, 200);
      data.forEach(element => {
        this.dataT.push(element);
      });
      // console.log("getData True : Data s", this.dataT);
    });
  }

  searchData(): void {
    if (this.form.valid) {
      // console.log("searchData True");
      // this.showData = true;
      // this.getData();
      const searchParam = {
        dateForm: ThDateToEnDate(this.form.get('dateForm').value),
        dateTo: ThDateToEnDate(this.form.get('dateTo').value),
        paymentDateForm :ThDateToEnDate(this.form.get('paymentDateForm').value),
        paymentDateTo :ThDateToEnDate(this.form.get('paymentDateTo').value),
        organizeId: this.form.get('organizeId').value,
        companyName: this.form.get('companyName').value,
        requestTypeCode: this.reqTypeChanged,
        paidtypeCode: this.paidTypeChanged
      }

      this.repDT.searchParams(searchParam);
      this.repDT.search();
    } else {
      // console.log("searchData False");
    }
  }

  clearData(): void {
    // console.log("clearData");
    this.form.reset({        dateForm: '',          
    dateTo: '' ,            
    paymentDateForm: '' ,           
    paymentDateTo: '' ,             
    organizeId: ''   ,                               
    companyName: ''     });
    $('#reqtype').dropdown('restore defaults');
    $('#paidtype').dropdown('restore defaults');
    // this.showData = false;
    this.repDT.clear();
  }

  exportFile = () => {

    let param = "";
    // param += "?dateForm=" + ThDateToEnDate(this.form.controls.dateForm.value);
    // param += "&dateTo=" + ThDateToEnDate(this.form.controls.dateTo.value);
    // param += "&paymentDateForm=" + ThDateToEnDate(this.form.controls.paymentDateForm.value);
    // param += "&paymentDateTo=" + ThDateToEnDate(this.form.controls.paymentDateTo.value);

    (ThDateToEnDate(this.form.controls.dateForm.value) != null ) ? param += "?dateForm=" + ThDateToEnDate(this.form.controls.dateForm.value) : param +="?dateForm=";
    (ThDateToEnDate(this.form.controls.dateTo.value) != null ) ? param += "&dateTo=" + ThDateToEnDate(this.form.controls.dateTo.value) : param +="&dateTo=";
    (ThDateToEnDate(this.form.controls.paymentDateForm.value) != null ) ? param += "&paymentDateForm=" + ThDateToEnDate(this.form.controls.paymentDateForm.value) : param +="&paymentDateForm=";
    (ThDateToEnDate(this.form.controls.paymentDateTo.value) != null ) ? param += "&paymentDateTo=" + ThDateToEnDate(this.form.controls.paymentDateTo.value) : param +="&paymentDateTo=";
 

    (this.form.controls.organizeId.value != null) ? param += "&organizeId=" + this.form.controls.organizeId.value : "";
    (this.form.controls.companyName.value != null) ? param += "&companyName=" + this.form.controls.companyName.value : "";

    (this.reqTypeChanged != null) ? param += "&requestTypeCode=" + this.reqTypeChanged : "";
    (this.paidTypeChanged != null) ? param += "&paidtypeCode=" + this.paidTypeChanged : "";
    this.ajax.download(URL.export + param);
  }

  invalid(input: string, submitted: boolean) {
    const control = this.form.get(input);
    return control.invalid && submitted;
  }
  onlyNumber(event) { event.charCode >= 48 && event.charCode <= 57 }

  get dateFrom() {
    return this.form.get("dateForm");
  }
  get dateTo() {
    return this.form.get("dateTo");
  }

  get paymentDateForm() {
    return this.form.get("paymentDateForm");
  }
  get paymentDateTo() {
    return this.form.get("paymentDateTo");
  }

}
