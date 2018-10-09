import { Component, OnInit, ViewChild, AfterViewInit } from '@angular/core';
import { Crs01000Service } from 'app/tmb-ecert/crs00000/crs01000/crs01000.service';
import { Calendar, CalendarFormatter, CalendarLocal, CalendarType } from 'models/';
import { NgForm, FormGroup, FormControl, Validators } from '@angular/forms';
import { AjaxService } from 'app/baiwa/common/services/ajax.service';
import { Router, ActivatedRoute, Params } from "@angular/router";
import { isValid } from 'app/baiwa/common/helpers';
import { DatePipe } from '@angular/common';

declare var $: any;
@Component({
  selector: 'app-crs01000',
  templateUrl: './crs01000.component.html',
  styleUrls: ['./crs01000.component.css'],
  providers: [Crs01000Service, DatePipe]
})
export class Crs01000Component implements OnInit, AfterViewInit {
  calendar1: Calendar;
  calendar2: Calendar;
  form: FormGroup;
  idReq: any;
  dataT: any[] = [];
  showData: boolean = false;
  loading: boolean = false;
  status: string;
  statusHome: string;

  countNewrequest: Number;
  countPaymentProcessing: Number;
  countRefuseRequest: Number;
  countCancelRequest: Number;
  countWaitPaymentApproval: Number;
  countPaymentApprovals: Number;
  countChargeback: Number;
  countPaymentfailed: Number;
  countWaitUploadCertificate: Number;
  countSucceed: Number;
  countWaitSaveRequest: Number;

  tmpDate: Date;

  constructor(private crs01000Service: Crs01000Service, 
    private ajax: AjaxService, 
    private router: Router,
    private route: ActivatedRoute, 
    private datePipe: DatePipe) {

    this.crs01000Service.getForm().subscribe(form => {
      this.form = form
    });

    this.calendar1 = {
      calendarId: "cal1",
      calendarName: "cal1",
      formGroup: this.form,
      formControlName: "reqDate",
      type: CalendarType.DATE,
      formatter: CalendarFormatter.DEFAULT,
      local: CalendarLocal.EN,
      initial: new Date(),
      icon: "time icon"
    };
    this.calendar2 = {
      calendarId: "cal2",
      calendarName: "cal2",
      formGroup: this.form,
      formControlName: "toReqDate",
      type: CalendarType.DATE,
      formatter: CalendarFormatter.DEFAULT,
      local: CalendarLocal.EN,
      initial: new Date(),
      icon: "time icon"
    };


    //this.tmpDate = new Date()
    //console.log(this.datePipe.transform(this.tmpDate, 'dd/MM/yyyy'))
  }

  ngOnInit() {
    this.dataT = [];
    this.getCountStatus();
    
    this.statusHome = this.route.snapshot.queryParams["codeStatus"];


    if(this.statusHome){
      this.searchStatusByHomePage(this.statusHome)
    }else{
      $('.ui.sidebar')
      .sidebar({
        context: '.ui.grid.pushable'
      })
      .sidebar('setting', 'transition', 'push')
      .sidebar('toggle');
    }


  }

  ngAfterViewInit() {


  }

  onToggle() {
    this.getCountStatus();
    $('.ui.sidebar')
      .sidebar({
        context: '.ui.grid.pushable'
      })
      .sidebar('setting', 'transition', 'push')
      .sidebar('toggle');
  }


  calendarValue(name, e) {
    this.form.controls[name].setValue(e);
    //console.log(this.form);
    //console.log(this.form.controls[name].value);

  }


  getData = () => {
    console.log(this.form);
    this.loading = true;
    const URL = "/api/crs/crs01000/findReq";
    this.ajax.post(URL, {
      reqDate: this.form.controls.reqDate.value,
      toReqDate: this.form.controls.toReqDate.value,
      organizeId: this.form.controls.organizeId.value,
      companyName: this.form.controls.companyName.value,
      tmbReqNo: this.form.controls.tmbReqNo.value,

    }, async res => {
      const data = await res.json();

      setTimeout(() => {
        this.loading = false;
      }, 200);
      data.forEach(element => {
        this.dataT.push(element);
      });
      //console.log("getData True : Data s", this.dataT);
    });
  }





  getDataByStatus(code) {
    this.status = code;
    this.loading = true;
    const URL = "/api/crs/crs01000/findReqByStatus";
    this.ajax.post(URL, { status: this.status }, res => {
      //console.log(res.json());
      setTimeout(() => {
        this.loading = false;
      }, 200);
      res.json().forEach(element => {
        this.dataT.push(element);
      });
    });

  }

  getCountStatus() {
    const URL = "/api/crs/crs01000/countStatus";
    this.ajax.post(URL, {}, res => {
      this.countNewrequest = res.json().newrequest;
      this.countPaymentProcessing = res.json().paymentProcessing;
      this.countRefuseRequest = res.json().refuseRequest;
      this.countCancelRequest = res.json().cancelRequest;
      this.countWaitPaymentApproval = res.json().waitPaymentApproval;
      this.countPaymentApprovals = res.json().paymentApprovals;
      this.countChargeback = res.json().chargeback;
      this.countPaymentfailed = res.json().paymentfailed;
      this.countWaitUploadCertificate = res.json().waitUploadCertificate;
      this.countSucceed = res.json().succeed;
      this.countWaitSaveRequest = res.json().waitSaveRequest;
    });

  }

  searchData(): void {
    console.log(this.form.controls['reqDate'].value);
    console.log("searchData");
    if ((this.form.controls['reqDate'].value == "" || this.form.controls['reqDate'].value == null) &&
      (this.form.controls['toReqDate'].value == "" || this.form.controls['toReqDate'].value == null) &&
      (this.form.controls['organizeId'].value == "" || this.form.controls['organizeId'].value == null) &&
      (this.form.controls['companyName'].value == "" || this.form.controls['companyName'].value == null) &&
      (this.form.controls['tmbReqNo'].value == "" || this.form.controls['tmbReqNo'].value == null)
    ) {
      this.dataT = [];
      this.showData = true;
    } else {
      this.showData = true;
      this.getData();
      this.dataT = [];
    }



  }

  

  searchStatus(code): void {
    $('.ui.sidebar')
      .sidebar({
        context: '.ui.grid.pushable'
      })
      .sidebar('setting', 'transition', 'push')
      .sidebar('toggle');


    console.log("searchStatus");
    this.showData = true;
    this.getDataByStatus(code);
    this.dataT = [];
  }


  searchStatusByHomePage(code): void {
    //console.log("STATUS FOR HOME::"+code);
    this.showData = true;
    this.getDataByStatus(code);
    this.dataT = [];
  }


  clearData(): void {
    console.log("clearData");
    this.form.reset();
    this.showData = false;
    this.dataT = [];
  }

  detail(idReq, status): void {
    console.log(idReq + "," + status)
    if (status == "10011") {
      this.router.navigate(["/nrq/nrq02000"], {
        queryParams: { id: idReq }
      });
    } else {
      this.router.navigate(["/crs/crs02000"], {
        queryParams: { id: idReq }
      });
    }


  }



  validate(input: string, submitted: boolean) {
    return isValid(this.form, input, submitted);
  }
}



