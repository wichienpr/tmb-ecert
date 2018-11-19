import { Component, OnInit, ViewChild, AfterViewInit } from '@angular/core';
import { Srn01000Service } from 'app/tmb-ecert/srn00000/srn01000/srn01000.service';
import { FormGroup, FormControl, Validators } from '@angular/forms';
import { Router, ActivatedRoute } from '@angular/router';
import { AjaxService, CommonService } from 'app/baiwa/common/services';
import { isValid } from 'app/baiwa/common/helpers';
import { DatatableCofnig, DatatableDirective } from 'app/baiwa/common/directives/datatable/datatable.directive';
import { ROLES } from 'app/baiwa/common/constants';

@Component({
  selector: 'app-srn01000',
  templateUrl: './srn01000.component.html',
  styleUrls: ['./srn01000.component.css'],
  providers: [Srn01000Service]
})
export class Srn01000Component implements OnInit, AfterViewInit {
  form: FormGroup = new FormGroup({
    tmbReqNo: new FormControl('', Validators.required),
    status: new FormControl('')
  });

  statusHomePage: string;



  dataConfig: DatatableCofnig;
  @ViewChild("dataDt")
  dataDt: DatatableDirective

  constructor(
    private srn01000Service: Srn01000Service,
    private ajax: AjaxService,
    private router: Router,
    private route: ActivatedRoute,
    private common: CommonService
  ) { }

  ngOnInit() {
    this.dataConfig = {
      url: "/api/srn/srn01000/list",
      serverSide: true,
      useBlockUi: true
    };
  }

  ngAfterViewInit() {
    this.statusHomePage = this.route.snapshot.queryParams["codeStatus"];
    // console.log(this.statusHomePage)
    if (this.statusHomePage) {
      setTimeout(() => {
        this.searchStatusByHomePage(this.statusHomePage);
      }, 500);
    }
  }

  searchData() {
    this.form.setValue({ status: "", tmbReqNo: this.form.value.tmbReqNo });
    // console.log(this.form.value);

    if (!this.form.touched) {
      Object.keys(this.form.value).forEach(element => {
        let fc = this.form.get(element);
        fc.markAsTouched({ onlySelf: true });
      });
    }

    if (this.form.invalid) {
      // console.log("form invalid");
      return false;
    }

    this.dataDt.searchParams(this.form.value);
    this.dataDt.search();

  }

  clearData(): void {
    this.form.reset();
    this.dataDt.clear();
  }

  searchStatusByHomePage(code): void {
    // console.log(code);
    this.form.setValue({ status: code, tmbReqNo: "" });
    // console.log(this.form.value);
    this.dataDt.searchParams(this.form.value);
    this.dataDt.search();
  }

  detail(idReq, status, lockFlag, updatedById): void {
    return this.srn01000Service.redirectFor(idReq, status, lockFlag, updatedById);
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

  get tmbReqNo() {
    return this.form.get("tmbReqNo");
  }

}
