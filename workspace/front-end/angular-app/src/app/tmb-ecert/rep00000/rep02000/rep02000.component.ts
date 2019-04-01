import { Component, OnInit, ChangeDetectorRef, ViewChild } from '@angular/core';
import { AjaxService, ModalService } from "services/";
import { Modal } from "models/";
import { Calendar, CalendarFormatter, CalendarLocal, CalendarType } from 'models/';
import { FormGroup } from '@angular/forms';
import { isValid, ThMonthYearToEnMonthYear } from 'app/baiwa/common/helpers';

import { Certificate } from 'models/';
import { Rep02000Service } from 'app/tmb-ecert/rep00000/rep02000/rep02000.service';
import { ActivatedRoute, Router } from '@angular/router';
import { NgCalendarConfig, NgCalendarComponent } from 'app/baiwa/common/components/calendar/ng-calendar.component';

import * as am4core from "@amcharts/amcharts4/core";
import * as am4charts from "@amcharts/amcharts4/charts";
import { AttrAst } from '@angular/compiler';

const URL = {
  export: "/api/rep/rep02000/exportFile"
}
@Component({
  selector: 'app-rep02000',
  templateUrl: './rep02000.component.html',
  styleUrls: ['./rep02000.component.css'],
  providers: [Rep02000Service]
})
export class Rep02000Component implements OnInit {

  @ViewChild("calendarFrom")
  calendarFrom: NgCalendarComponent;

  @ViewChild("calendarTo")
  calendarTo: NgCalendarComponent;

  showData: boolean = false;
  dataT: any[] = [];
  loading: boolean = false;

  calendar1: Calendar;
  calendar2: Calendar;
  form: FormGroup;
  dropdownObj: any;
  paidTypeChanged: Certificate[];
  paidTypeClear: boolean = false;

  calendarFromConig: NgCalendarConfig;
  calendarToConig: NgCalendarConfig;

  constructor(
    private ajax: AjaxService,
    private modal: ModalService,
    private service: Rep02000Service,
    private route: ActivatedRoute,
    private router: Router,
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
      type: CalendarType.MONTH,
      formatter: CalendarFormatter.MMyyyy
    };
    this.calendarToConig = {
      id: "dateTo",
      formControl: this.form.get("dateTo"),
      startCalendar: "dateForm",
      type: CalendarType.MONTH,
      formatter: CalendarFormatter.MMyyyy
    };
    // this.calendar1 = {
    //   calendarId: "cal1",
    //   calendarName: "cal1",
    //   formGroup: this.form,
    //   formControlName: "dateForm",
    //   type: CalendarType.MONTH,
    //   formatter: CalendarFormatter.MMyyyy,
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
    //   type: CalendarType.MONTH,
    //   formatter: CalendarFormatter.MMyyyy,
    //   local: CalendarLocal.EN,
    //   icon: "time icon",
    //   startId: "cal1",
    //   initial: new Date
    // };
  }

  ngOnInit() {
    let dForm = this.route.snapshot.queryParams["dateForm"];
    let dTo = this.route.snapshot.queryParams["dateTo"];
    if (dForm != "" || dTo != "") {
      this.form.controls[`dateForm`].setValue(dForm);
      this.form.controls[`dateTo`].setValue(dTo);
      this.searchData();
    }
    // console.log("form : ", this.form);
  }

  ngAfterViewChecked() {
    this.cdRef.detectChanges();
  }

  ngAfterViewInit() { }

  calendarValue(name, e) {
    this.form.controls[name].setValue(e);
  }

  paidTypeChange(e) {
    // console.log("requestTypeCode : ", e);
    this.paidTypeChanged = e;
  }

  getData = () => {
    // console.log(this.form);
    this.loading = true;
    this.dataT = [];
    const URL = "/api/rep/rep02000/list";
    this.ajax.post(URL, {
      dateForm: ThMonthYearToEnMonthYear(this.form.controls.dateForm.value),
      dateTo: ThMonthYearToEnMonthYear(this.form.controls.dateTo.value),
      paidtypeCode: this.paidTypeChanged
    }, res => {
      const data = res.json();

      setTimeout(() => {
        this.loading = false;
      }, 200);

      data.forEach(element => {
        this.dataT.push(element);
      });
      this.createDashboard(this.dataT);
    });

  }

  createDashboard(dataT : any[]) : void{
    let sumOfAmt = 0;
    let sumOfReq = 0;
    let arrayPie = [], objPie = {};
    let countLeft = 0;
    let sumLeft = 0;
    let countRight = 0;
    let sumRight = 0;
    let jLeft = 0;
    let iLeft = 0;
    let kLeft = 0;
    let jRight = 0;
    let iRight = 0;
    let kRight = 0;

    let chart1 = am4core.create("chartdiv1", am4charts.PieChart);
    let chart2 = am4core.create("chartdiv2", am4charts.PieChart);
    for (let x in dataT) { 
      sumOfAmt += dataT[x].amountTmb; 
      sumOfReq += dataT[x].custsegmentCount; 
    }

    for (let y in dataT) { 
      objPie = {
        "Segment": dataT[y].custsegmentDesc,
        "Request": dataT[y].custsegmentCount,
        "Amount": dataT[y].amountTmb,
        "PercentOfAmt": (dataT[y].amountTmb / sumOfAmt * 100).toFixed(1),
        "PercentOfReq": (dataT[y].custsegmentCount / sumOfReq * 100).toFixed(1)
      };
      arrayPie.push(objPie);
    }

    chart1.data = arrayPie;
    chart2.data = arrayPie;
    chart1.radius = am4core.percent(45);
    chart1.innerRadius = am4core.percent(25);
    chart2.radius = am4core.percent(45);
    chart2.innerRadius = am4core.percent(25);

    let pieSeriesRt1 = chart1.series.push(new am4charts.PieSeries());
    pieSeriesRt1.dataFields.value = "Amount";
    pieSeriesRt1.dataFields.category = "Segment";
    pieSeriesRt1.labels.template.text = "{Segment}\n({Amount}): {PercentOfAmt}%[/]";
    pieSeriesRt1.labels.template.fontSize = 10;
    pieSeriesRt1.slices.template.stroke = am4core.color("#fff");
    pieSeriesRt1.slices.template.strokeWidth = 1;
    pieSeriesRt1.slices.template.strokeOpacity = 1;
    pieSeriesRt1.colors.list = this.service.getColorObj();

    pieSeriesRt1.labels.template.adapter.add("fill", function (color, target) {
      countLeft += 1;
      if (countLeft > arrayPie.length) {
        iLeft = arrayPie[jLeft].PercentOfAmt / 2 + sumLeft;
        sumLeft += parseFloat(arrayPie[jLeft].PercentOfAmt);
        jLeft += 1;
      }
      if (countLeft / 2 > pieSeriesRt1.colors.list.length) {
        return color;
      } else if (countLeft > arrayPie.length && iLeft <= 50) {
        return pieSeriesRt1.colors.list[(countLeft - 1) - arrayPie.length];
      } else if (countLeft > arrayPie.length && iLeft > 50) {
        kLeft += 1;
        return pieSeriesRt1.colors.list[arrayPie.length - kLeft];
      }
    });
    
    // Add and configure Series
    let pieSeriesRt2 = chart2.series.push(new am4charts.PieSeries());
    pieSeriesRt2.dataFields.value = "Request";
    pieSeriesRt2.dataFields.category = "Segment";
    pieSeriesRt2.slices.template.stroke = am4core.color("#fff");
    pieSeriesRt2.labels.template.text = "{Segment}\n({Request}): {PercentOfReq}%[/]";
    pieSeriesRt2.labels.template.fontSize = 10;
    pieSeriesRt2.colors.list = this.service.getColorObj();

    pieSeriesRt2.labels.template.adapter.add("fill", function (color, target) {
      countRight += 1;
      if (countRight > arrayPie.length) {
        iRight = arrayPie[jRight].PercentOfReq / 2 + sumRight;
        sumRight += parseFloat(arrayPie[jRight].PercentOfReq);
        jRight += 1;
      }
      if (countRight / 2 > pieSeriesRt2.colors.list.length) {
        return color;
      } else if (countRight > arrayPie.length && iRight <= 50) {
        return pieSeriesRt2.colors.list[(countRight - 1) - arrayPie.length];
      } else if (countRight > arrayPie.length && iRight > 50) {
        kRight += 1;
        return pieSeriesRt2.colors.list[arrayPie.length - kRight];
      }
    });
  }

  searchData(): void {
    if (this.form.valid) {
      // console.log("searchData True");
      this.showData = true;
      this.getData();
    } else {
      // console.log("searchData False");
    }
  }

  clearData(): void {
    // console.log("clearData");
    this.form.reset();
    this.paidTypeClear = true;
    this.showData = false;
    setTimeout(() => {
      this.paidTypeClear = false;
    }, 500);
  }

  exportFile = () => {
    // console.log("exportFile");
    let param = "";
    param += "?dateForm=" + ThMonthYearToEnMonthYear(this.form.controls.dateForm.value);
    param += "&dateTo=" + ThMonthYearToEnMonthYear(this.form.controls.dateTo.value);

    (this.paidTypeChanged != null) ? param += "&paidtypeCode=" + this.paidTypeChanged : "";

    this.ajax.download(URL.export + param);
  }

  remark = custsegmentCode => {
    this.router.navigate(['/rep/rep02100'], {
      queryParams: {
        custsegmentCode: custsegmentCode,
        dateForm: ThMonthYearToEnMonthYear(this.form.controls.dateForm.value),
        dateTo: ThMonthYearToEnMonthYear(this.form.controls.dateTo.value)
      }
    });
  }

  departmentDetails = custsegmentCode => {
    this.router.navigate(['/rep/rep02200'], {
      queryParams: {
        custsegmentCode: custsegmentCode,
        dateForm: this.form.controls.dateForm.value,
        dateTo: this.form.controls.dateTo.value
      }
    });
  }

  openModalDetails = department => {
    const modal: Modal = {
      title: "รายละเอียด",
      msg: "ชื่อหน่อยงาน : " + department,
      success: false
    };
    this.modal.alert(modal);
  }

  validate(input: string, submitted: boolean) {
    return isValid(this.form, input, submitted);
  }

}
