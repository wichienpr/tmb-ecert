import { Component, OnInit } from '@angular/core';
import { AjaxService } from "services/";
import { Calendar, CalendarFormatter, CalendarLocal, CalendarType } from 'models/';
import { FormGroup } from '@angular/forms';
import { isValid } from 'app/baiwa/common/helpers';
import { Rep03000Service } from 'app/tmb-ecert/rep00000/rep03000/rep03000.service';

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

  customerNameHead: String;
  organizeIdHead: String;
  companyNameHead: String;
  branchHead: String;
  addressHead: String;

  dataT: any[] = [];
  loading: boolean = false;

  calendar1: Calendar;
  form: FormGroup;

  constructor(
    private ajax: AjaxService,
    private service: Rep03000Service
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
      icon: "time icon",
      initial: new Date
    };
  }

  ngOnInit() { }

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
      paymentDate: this.form.get('dateVat').value,
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
      this.showData = true;
      this.getData();
    }
  }

  clearData(): void {
    this.form.reset();
    this.showData = false;
  }

  exportFile = () => {
    let param = "";
    param += "?paymentDate=" + this.form.get('dateVat').value;
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

}
