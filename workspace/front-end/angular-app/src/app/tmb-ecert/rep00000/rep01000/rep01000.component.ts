import { Component, OnInit } from '@angular/core';
import { AjaxService } from 'app/baiwa/common/services/ajax.service';
import { Calendar, CalendarFormatter, CalendarLocal, CalendarType } from 'models/';
import { FormGroup } from '@angular/forms';
import { Rep01000Service } from './rep01000.service';
import { Certificate } from 'models/';
import { isValid } from 'app/baiwa/common/helpers';

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
export class Rep01000Component implements OnInit {
  showData: boolean = false;
  dataT: any[] = [];
  loading: boolean = false;

  dropdownObj: any;
  reqTypeChanged: Certificate[];
  paidTypeChanged: Certificate[];

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

  ngOnInit() { }

  ngAfterViewInit() { }

  calendarValue(name, e) {
    this.form.controls[name].setValue(e);
    console.log(this.form);
    console.log(this.form.controls[name].value);
  }
  reqTypeChange(e) {
    console.log("requestTypeCode : ", e);
    this.reqTypeChanged = e;
  }
  paidTypeChange(e) {
    console.log("requestTypeCode : ", e);
    this.paidTypeChanged = e;
  }

  getData() {
    this.loading = true;
    this.dataT = [];
    const URL = "/api/rep/rep01000/list";
    this.ajax.post(URL, {
      dateForm: this.form.get('dateForm').value,
      dateTo: this.form.get('dateTo').value,
      organizeId: this.form.get('corpNo').value,
      companyName: this.form.get('corpName').value,
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
      console.log("getData True : Data s", this.dataT);
    });
  }

  searchData(): void {
    if (this.form.valid) {
      console.log("searchData True");
      this.showData = true;
      this.getData();
    } else {
      console.log("searchData False");
    }
  }

  clearData(): void {
    console.log("clearData");
    this.form.reset();
    $('#reqtype').dropdown('restore defaults');
    $('#paidtype').dropdown('restore defaults');
    this.showData = false;
  }

  exportFile = () => {
    console.log("exportFile");
    let param = "";
    param += "?dateForm=" + this.form.controls.dateForm.value;
    param += "&dateTo=" + this.form.controls.dateTo.value;

    (this.form.controls.corpNo.value != null) ? param += "&organizeId=" + this.form.controls.corpNo.value : "";
    (this.form.controls.corpName.value != null) ? param += "&companyName=" + this.form.controls.corpName.value : "";

    (this.reqTypeChanged != null) ? param += "&requestTypeCode=" + this.reqTypeChanged : "";
    (this.paidTypeChanged != null) ? param += "&paidtypeCode=" + this.paidTypeChanged : "";

    this.ajax.download(URL.export + param);
  }

  invalid(input: string, submitted: boolean) {
    const control = this.form.get(input);
    return control.invalid && submitted;
  }
  onlyNumber(event) { event.charCode >= 48 && event.charCode <= 57 }

}
