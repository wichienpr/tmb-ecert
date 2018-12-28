import { Component, OnInit, ViewChild, AfterViewInit } from '@angular/core';
import { Crs01000Service } from 'app/tmb-ecert/crs00000/crs01000/crs01000.service';
import { Dropdown, DropdownMode, Lov } from 'models/';
import { FormGroup, FormControl, Validators } from '@angular/forms';
import { AjaxService } from 'app/baiwa/common/services/ajax.service';
import { Router, ActivatedRoute } from "@angular/router";
import { DatePipe } from '@angular/common';
import { CommonService } from 'app/baiwa/common/services';
import { ROLES } from 'app/baiwa/common/constants';
import { DatatableCofnig, DatatableDirective } from 'app/baiwa/common/directives/datatable/datatable.directive';
import { NgCalendarConfig } from 'app/baiwa/common/components/calendar/ng-calendar.component';
import * as moment from 'moment';
import { ThDateToEnDate,dateLocale,EnDateToThDate} from 'app/baiwa/common/helpers';

declare var $: any;

@Component({
  selector: 'app-crs01000',
  templateUrl: './crs01000.component.html',
  styleUrls: ['./crs01000.component.css'],
  providers: [Crs01000Service, DatePipe]
})
export class Crs01000Component implements OnInit, AfterViewInit {

  @ViewChild("dataDt")
  dataDt: DatatableDirective

  actionDropdown: Dropdown;
  calendar1: NgCalendarConfig;
  calendar2: NgCalendarConfig;
  form: FormGroup;
  dataConfig: DatatableCofnig;

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
    this.form = new FormGroup({
      reqDate: new FormControl(now),   //วันที่ขอ
      toReqDate: new FormControl(now), //ถึงวันที่
      organizeId: new FormControl(''),                      //เลขที่นิติบุคคล
      companyName: new FormControl(''),                     //ชื่อนิติบุคคล
      tmbReqNo: new FormControl(''),                        //TMB Req. No.
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
  }

  ngAfterViewInit() {
    this.statusHome = this.route.snapshot.queryParams["codeStatus"];
    if (this.statusHome) {
      setTimeout(() => {
        this.searchStatusByHomePage(this.statusHome);
      }, 500);
    } else {
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
      .sidebar({ context: '.ui.grid.pushable' })
      .sidebar('setting', 'transition', 'push')
      .sidebar('toggle');
  }

  searchData() {
    if (!this.form.touched) {
      Object.keys(this.form.value).forEach(element => {
        let fc = this.form.get(element);
        fc.markAsTouched({ onlySelf: true });
      });
    }
    if (this.form.invalid) {
      return false;
    }
    const data = {
      status: "",
      reqDate: ThDateToEnDate(this.form.value.reqDate),
      toReqDate: ThDateToEnDate(this.form.value.toReqDate),
      organizeId: this.form.value.organizeId,
      companyName: this.form.value.companyName,
      tmbReqNo: this.form.value.tmbReqNo
    };
    this.dataDt.searchParams(data);
    this.dataDt.search();
  }

  searchStatusByHomePage(code): void {
    this.form.setValue({ status: code, reqDate: "", toReqDate: "", organizeId: "", companyName: "", tmbReqNo: "" });
    this.dataDt.searchParams(this.form.value);
    this.dataDt.search();
  }

  searchStatus(code): void {
    if (code == 10011) {
      this.router.navigate(["/srn/srn01000"], {
        queryParams: { codeStatus: code }
      });
    } else {
      
      if (code == 10010 ){
        let now = EnDateToThDate(moment().format('DD/MM/YYYY'));
        this.form.setValue({ status: code, reqDate: now, toReqDate: now, organizeId: "", companyName: "", tmbReqNo: "" });
      }else {
        this.form.setValue({ status: code, reqDate: "", toReqDate: "", organizeId: "", companyName: "", tmbReqNo: "" });
      }
      $('.ui.sidebar')
        .sidebar({
          context: '.ui.grid.pushable'
        })
        .sidebar('setting', 'transition', 'push')
        .sidebar('toggle');
        // this.form.setValue({ status: code, reqDate: "", toReqDate: "", organizeId: "", companyName: "", tmbReqNo: "" });
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

  detail(idReq, status, lockFlag, updatedById): void {
    return this.crs01000Service.redirectFor(idReq, status, lockFlag, updatedById);
  }

  noSymbol(e) {
    var txt = String.fromCharCode(e.which);
    if (!txt.toString().match(/[A-Za-z0-9ก-๙ ]/) || e.charCode == 3647) {
      return false;
    }
  }

  numberOnly(e, hasDot?: boolean) {
    if (hasDot) {
      return e.charCode == 46 || e.charCode >= 48 && e.charCode <= 57;
    }
    return e.charCode >= 48 && e.charCode <= 57;
  }

  roles(role: ROLES) {
    return this.common.isRole(role);
  }

  get reqDate() { return this.form.get("reqDate") }
  get toReqDate() { return this.form.get("toReqDate") }

  statusForBlue(status) { return status == '10001' || status == '10005' || status == '10009' || status == '10011' }
  statusForGray(status) { return status == '10002' || status == '10010' || status == '10006' }
  statusForRed(status) { return status == '10003' || status == '10007' || status == '10004' || status == '10005' || status == '10008' }

  getFontStyeColor(status, lockFlag, userId) {
    if (lockFlag == "1" && !this.common.isUser(userId)) {
      return 'gray';
    }
    if (this.statusForBlue(status)) {
      return '#2185D0';
    } else if (this.statusForGray(status)) {
      return 'gray';
    } else if (this.statusForRed(status)) {
      return 'red';
    } else {
      return 'gray';
    }
  }

  getButtonStlyeColor(status, lockFlag, userId) {
    if (lockFlag == "1" && !this.common.isUser(userId)) {
      return 'ui gray basic button center';
    }
    if (this.statusForBlue(status)) {
      return 'ui blue basic button center';
    } else if (this.statusForGray(status)) {
      return 'ui gray basic button center';
    } else if (this.statusForRed(status)) {
      return 'ui red basic button center';
    } else {
      return 'ui gray basic button center';
    }
  }

}



