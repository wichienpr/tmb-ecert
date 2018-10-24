import { Component, OnInit, ViewChild, AfterViewInit } from '@angular/core';
import { Crs01000Service } from 'app/tmb-ecert/crs00000/crs01000/crs01000.service';
import { Calendar, CalendarFormatter, CalendarLocal, CalendarType, Dropdown, DropdownMode,Lov } from 'models/';
import { NgForm, FormGroup, FormControl, Validators } from '@angular/forms';
import { AjaxService } from 'app/baiwa/common/services/ajax.service';
import { Router, ActivatedRoute, Params } from "@angular/router";
import { DatePipe } from '@angular/common';
import { CommonService } from 'app/baiwa/common/services';
import { ROLES, PAGE_AUTH } from 'app/baiwa/common/constants';
import { DatatableCofnig, DatatableDirective } from 'app/baiwa/common/directives/datatable/datatable.directive';
import { NgCalendarConfig } from 'app/baiwa/common/components/calendar/ng-calendar.component';
import * as moment from 'moment';

declare var $: any;
@Component({
  selector: 'app-crs01000',
  templateUrl: './crs01000.component.html',
  styleUrls: ['./crs01000.component.css'],
  providers: [Crs01000Service, DatePipe]
})
export class Crs01000Component implements OnInit, AfterViewInit {
  actionDropdown: Dropdown;
  calendar1: NgCalendarConfig;
  calendar2: NgCalendarConfig;
  form: FormGroup;
  dataConfig: DatatableCofnig;
  @ViewChild("dataDt")
  dataDt: DatatableDirective



  status: string;
  statusHome: string;

  countNewrequest: Number = 0;
  countPaymentProcessing: Number = 0;
  countRefuseRequest: Number = 0;
  countCancelRequest: Number = 0;
  countWaitPaymentApproval: Number = 0;
  countPaymentApprovals: Number = 0;
  countChargeback: Number = 0;
  countPaymentfailed: Number = 0;
  countWaitUploadCertificate: Number = 0;
  countSucceed: Number = 0;
  countWaitSaveRequest: Number = 0;

  
  constructor(
    private crs01000Service: Crs01000Service,
    private ajax: AjaxService,
    private router: Router,
    private route: ActivatedRoute,
    private common: CommonService) {
  }

  ngOnInit() {
    let now = moment().format('DD/MM/YYYY');
    this.form  = new FormGroup({
      reqDate: new FormControl(now, Validators.required),     //วันที่ขอ
      toReqDate: new FormControl(now, Validators.required),   //ถึงวันที่
      organizeId: new FormControl(''),  //เลขที่นิติบุคคล
      companyName: new FormControl(''),  //ชื่อนิติบุคคล
      tmbReqNo: new FormControl(''),     //TMB Req. No.
      status: new FormControl(''),
    });

    this.actionDropdown = {
      dropdownId: "actionCode",
      dropdownName: "actionCode",
      type: DropdownMode.SEARCH,
      formGroup: this.form,
      formControlName: "actionCode",
      values: [],
      valueName: "code",
      labelName: "name",
      placehold: "กรุณาเลือก"
    };
    this.crs01000Service.getActionDropdown().subscribe((obj: Lov[]) => this.actionDropdown.values = obj)
    
    this.calendar1 = {
      id: "calendar1",
      formControl: this.form.get("reqDate"),
      endCalendar: "calendar2"
    };
    
    this.calendar2 = {
      id: "calendar2",
      formControl: this.form.get("toReqDate"),
      startCalendar: "calendar1"
    };

    this.dataConfig = {
      url: "/api/crs/crs01000/list",
      serverSide: true,
      useBlockUi: true
    };

    this.getCountStatus();

    //this.getTest();
  }

  ngAfterViewInit() {
    this.statusHome = this.route.snapshot.queryParams["codeStatus"];
    console.log(this.statusHome)
    if (this.statusHome) {
      setTimeout(() => {
        this.searchStatusByHomePage(this.statusHome);
      }, 500);
      
    }else{
      $('.ui.sidebar')
      .sidebar({
        context: '.ui.grid.pushable'
      })
      .sidebar('setting', 'transition', 'push')
      .sidebar('toggle');
    }
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


  searchData() {
    this.form.setValue({ status: "", 
    reqDate: this.form.value.reqDate ,
    toReqDate:this.form.value.toReqDate,
    organizeId:this.form.value.organizeId,
    companyName:this.form.value.companyName,
    tmbReqNo:this.form.value.tmbReqNo});
    console.log(this.form.value)

    if (!this.form.touched) {
      Object.keys(this.form.value).forEach(element => {
        let fc = this.form.get(element);
        fc.markAsTouched({ onlySelf: true });
      });
    }

    if (this.form.invalid) {
      console.log("form invalid");
      return false;
    }

    this.dataDt.searchParams(this.form.value);
    this.dataDt.search();

  }

  searchStatusByHomePage(code): void {
    this.form.setValue({ status: code, reqDate: "" ,toReqDate:"",organizeId:"",companyName:"",tmbReqNo:""});
    console.log(this.form.value);
    this.dataDt.searchParams(this.form.value);
    this.dataDt.search();
  }


  searchStatus(code): void {

    if (code == 10011) {
      this.router.navigate(["/srn/srn01000"], {
        queryParams: { codeStatus: code }
      });
    } else {
      $('.ui.sidebar')
        .sidebar({
          context: '.ui.grid.pushable'
        })
        .sidebar('setting', 'transition', 'push')
        .sidebar('toggle');
      
      console.log("searchStatus");
      
      this.form.setValue({ status: code, reqDate: "" ,toReqDate:"",organizeId:"",companyName:"",tmbReqNo:""});
      console.log(this.form.value);
      this.dataDt.searchParams(this.form.value);
      this.dataDt.search();  
    }
  }

  clearData(): void {
    this.form.reset();
    this.dataDt.clear();
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


  detail(idReq, status): void {
    console.log(idReq + "," + status, "ROLES IS MAKER: " + this.roles(ROLES.MAKER))
    return this.crs01000Service.redirectFor(idReq, status);
  }

  roles(role: ROLES) {
    return this.common.isRole(role);
  }


  getFontStyeColor(status) {
    if (status == '10001' || status == '10005' || status == '10009' || status == '10011') {
      return '#2185D0';
    } else if (status == '10002' || status == '10007' || status == '10010') {
      return 'gray';
    } else if (status == '10003' || status == '10004' || status == '10005' || status == '10006') {
      return 'red';
    }

  }


  getButtonStyeColor(status) {
    if (status == '10001' || status == '10005' || status == '10009' || status == '10011') {
      return 'ui blue basic button center';
    } else if (status == '10002' || status == '10007' || status == '10010') {
      return 'ui gray basic button center';
    } else if (status == '10003' || status == '10004' || status == '10005' || status == '10006') {
      return 'ui red basic button center';
    }

  }



  // receiptTaxTest
  // getTest() {
  //   const URL = "/api/report/pdf/createAndUpload/receiptTax";
  //   this.ajax.post(URL, {
  //     id: "297"
  //   }, res => {
  //     console.log(res)
  //     this.ajax.download("/api/report/pdf/view/" + res._body + "/download");
  //   });
  // }

  getTest() {
    const URL = "/api/report/pdf/coverSheet";
    this.ajax.post(URL, {
      id: "275"
    }, res => {
      console.log(res)
      this.ajax.download("/api/report/pdf/view/" + res._body + "/download");
    });
  }



  get reqDate() {
    return this.form.get("reqDate");
  }

  get toReqDate() {
    return this.form.get("toReqDate");
  }

}



