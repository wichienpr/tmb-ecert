import { Component, OnInit, ViewChild } from '@angular/core';
import { AjaxService } from "services/";
import { Calendar, CalendarFormatter, CalendarLocal, CalendarType } from 'models/';
import { FormGroup } from '@angular/forms';
import { isValid, ThMonthYearToEnMonthYear } from 'app/baiwa/common/helpers';
import { Rep03000Service } from 'app/tmb-ecert/rep00000/rep03000/rep03000.service';
import { NgCalendarComponent, NgCalendarConfig } from 'app/baiwa/common/components/calendar/ng-calendar.component';
import { DatatableDirective, DatatableCofnig } from 'app/baiwa/common/directives/datatable/datatable.directive';

const URL = {
  export: "/api/rep/rep03000/exportFile"
}

@Component({
  selector: 'app-rep03000',
  templateUrl: './rep03000.component.html',
  styleUrls: ['./rep03000.component.css'],
  providers: [Rep03000Service]
})
export class Rep03000Component implements OnInit {
  showData: boolean = false;

  @ViewChild("repDT")
  repDT: DatatableDirective;

  @ViewChild("calendar")
  calendar: NgCalendarComponent;

  customerNameHead: String;
  organizeIdHead: String;
  companyNameHead: String;
  branchHead: String;
  addressHead: String;

  dataT: any[] = [];
  loading: boolean = false;

  calendarConig: NgCalendarConfig;
  calendar1: Calendar;
  form: FormGroup;

  repDTConfig: DatatableCofnig;
  
  constructor(
    private ajax: AjaxService,
    private service: Rep03000Service
  ) {
    this.service.getForm().subscribe(form => {
      this.form = form
    });
    this.calendarConig = {
      id: "cal1",
      formControl: this.form.get("dateVat"),
      startCalendar: "dateForm",
      type: CalendarType.MONTH,
      formatter:  CalendarFormatter.MMyyyy
    };
    // this.calendar1 = {
    //   calendarId: "cal1",
    //   calendarName: "cal1",
    //   formGroup: this.form,
    //   formControlName: "dateVat",
    //   type: CalendarType.MONTH,
    //   formatter: CalendarFormatter.MMyyyy,
    //   local: CalendarLocal.EN,
    //   icon: "time icon",
    //   initial: new Date
    // };
  }

  ngOnInit() {     
    this.repDTConfig = {
    url: "/api/rep/rep03000/list",
    serverSide: true,
    useBlockUi: true
  };
}

  calendarValue(name, e) {
    this.form.controls[name].setValue(e);
  }

  getData = () => {
    this.loading = true;
    this.dataT = [];
    const URL = "/api/rep/rep03000/list";
    let customerName = this.form.get('customerName').value;
    if (customerName != null) {
      this.form.get('customerName').patchValue(customerName.trim());
    }
    this.ajax.post(URL, {
      paymentDate: ThMonthYearToEnMonthYear(this.form.get('dateVat').value),
      organizeId: this.form.get('organizeId').value,
      customerName: this.form.get('customerName').value
    }, res => {
      const {
        customerNameHead,
        organizeIdHead,
        companyNameHead,
        branchHead,
        addressHead,
        rep03000VoList
      } = res.json();

      setTimeout(() => {
        this.loading = false;
      }, 200);

      this.customerNameHead = customerNameHead;
      this.organizeIdHead = organizeIdHead;
      this.companyNameHead = companyNameHead;
      this.branchHead = branchHead;
      this.addressHead = addressHead;

      rep03000VoList.forEach(element => {
        if (element.address != "") {
          this.addressHead = element.address;
        }
        this.dataT.push(element);
      });
    });
  }

  searchData(): void {
    if (this.form.valid) {
      const searchParam = {
        paymentDate: ThMonthYearToEnMonthYear(this.form.get('dateVat').value),
        organizeId: this.form.get('organizeId').value,
        customerName: this.form.get('customerName').value
      }
      this.repDT.searchParams(searchParam);
      this.repDT.search();

    }
  }

  clearData(): void {
    this.form.reset();
    this.showData = false;
    this.repDT.clear();
  }

  exportFile = () => {
    let param = "";
    param += "?paymentDate=" + ThMonthYearToEnMonthYear(this.form.get('dateVat').value);
    param += "&organizeId=" + this.form.get('organizeId').value;
    let customerName = this.form.get('customerName').value;
    if (customerName != null) {
      customerName = customerName.trim();
      this.form.get('customerName').patchValue(customerName);
      param += "&customerName=" + customerName;
    }
    this.ajax.download(URL.export + param);
  }

  validate(input: string, submitted: boolean) {
    return isValid(this.form, input, submitted);
  }

  onlyNumber(e) {
    return e.charCode >= 48 && e.charCode <= 57;
  }

  noSymbol(e) {
    var txt = String.fromCharCode(e.which);
    if (!txt.toString().match(/[A-Za-z0-9ก-๙. ]/) || e.charCode == 3647) {
      return false;
    }
  }

  noPipe(e) {
    if (e.charCode == 124) {
      return false;
    }
  }

}
