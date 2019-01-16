import { Component, OnInit, ViewChild, ChangeDetectorRef, AfterViewInit, ElementRef } from '@angular/core';
import { FormGroup, FormControl, Validators, NgForm } from '@angular/forms';

import { Nrq02000Service } from './nrq02000.service';
import { Certificate, Calendar, CalendarType, CalendarFormatter, CalendarLocal, RequestForm, initRequestForm, RequestCertificate, Modal, Dropdown } from 'models/';
import { Acc, digit, DecimalFormat, Assigned, EnDateToThDate } from 'helpers/';
import { Store } from '@ngrx/store';
import { UserDetail } from 'app/user.model';
import { CommonService, ModalService } from 'app/baiwa/common/services';
import { ROLES, PAGE_AUTH } from 'app/baiwa/common/constants';
import { DateConstant } from 'app/baiwa/common/components/calendar/calendar.component';
import * as moment from 'moment';
import { Router, ActivatedRoute } from '@angular/router';
import { Location } from '@angular/common';

declare var $: any;

@Component({
  selector: 'app-nrq02000',
  templateUrl: './nrq02000.component.html',
  styleUrls: ['./nrq02000.component.css'],
  providers: [Nrq02000Service]
})
export class Nrq02000Component implements OnInit, AfterViewInit {

  @ViewChild("ngForm") ngForm: NgForm;
  @ViewChild("ref1") ref1: ElementRef;
  @ViewChild("ref2") ref2: ElementRef;
  @ViewChild("dbd") dbd: ElementRef;
  @ViewChild("tmb") tmb: ElementRef;

  _roles = ROLES;
  list: string[] = [];
  isMaker: boolean = false;
  data: RequestForm = initRequestForm;
  tmbReqFormId: String = "";
  custsegmentCode: string = "";
  form: FormGroup;
  formReject: FormGroup;
  formAuth: FormGroup;
  files: any;
  reqDate: Date;
  dropdownObj: any;
  submitted: boolean = false;
  saving: boolean = false;
  isdownload: boolean = false;
  showChildren: boolean = false;
  rejectSubmitted: boolean = false;
  authSubmitted: boolean = false;
  hiddenReceipt3: boolean = false;
  hiddenReceipt4: boolean = false;
  toggleDoc: string = "content";
  toggleTitle: string = "title";
  glType: string = "";
  tranCode: string = "";
  accType: string = "";
  status: string = "";
  statusMsg: string = "";
  accNo: string = "";

  cert: RequestCertificate[] = [];
  chkList: Certificate[] = [];
  reqTypeChanged: Certificate[];
  calend: Calendar[] = [];
  calendar: Calendar[] = [];
  user: UserDetail;
  allowed: Dropdown;
  allowedModal: Modal;
  authForSubmit: Modal;
  firstEnter: boolean = true;
  loadTable: boolean = false;
  retryCount: number = 3;
  private isIEOrEdge: boolean = /msie\s|trident\/|edge\//i.test(window.navigator.userAgent);

  constructor(
    private service: Nrq02000Service,
    private cdRef: ChangeDetectorRef,
    private store: Store<{}>,
    private common: CommonService,
    private modal: ModalService,
    private route: ActivatedRoute,
    private router: Router,
    private location: Location,
  ) {
    this.reqTypeChanged = [];
    this.files = {
      requestFile: null,
      copyFile: null,
      changeNameFile: null
    };
    this.formReject = new FormGroup({
      allowedSelect: new FormControl('', Validators.required),
      otherReason: new FormControl(),
    });
    this.formAuth = new FormGroup({
      authUsername: new FormControl('', Validators.required),
      authPassword: new FormControl('', Validators.required),
    });
    this.allowed = {
      type: "selection",
      dropdownId: "allowedSelect",
      dropdownName: "allowedSelect",
      formControlName: "allowedSelect",
      formGroup: this.formReject,
      valueName: "code",
      labelName: "name",
      values: [],
      placehold: "กรุณาเลือก"
    };
    this.allowedModal = { modalId: "allowed", type: "custom" };
    this.authForSubmit = { modalId: "auth", type: "custom" };
    this.store.select("user").subscribe(user => {
      this.user = user
    });
    console.clear();

    this.form = this.service.getForm();
    this.dropdownObj = this.service.getDropdownObj();
  }

  ngOnInit() {
    // change status to locked
    this.service.lock();
    this.initial();
  }

  ngAfterViewChecked() {
    this.cdRef.detectChanges();
  }

  ngAfterViewInit() {
    setTimeout(() => {
      // After 7 second will auto loading
      const reqId = this.data.reqFormId.toString();
      const id = this.route.snapshot.queryParams["id"] || "";
      if (id != reqId && this.data.reqFormId != 0 && this.data.tmbRequestNo) {
        // loading data
        this.initial();
        setTimeout(() => {
          // After 5 second if no data will return to main page
          if (id != reqId && this.data.reqFormId != 0 && this.data.tmbRequestNo) {
            console.error("LOAD DATA ERROR");
            this.modal.alertWithAct({ msg: "ไม่สามารถทำรายการได้กรุณาลองใหม่อีกครั้ง" }, clicked => {
              if (clicked) {
                this.router.navigate(['/crs/crs01000']);
              }
            });
          } else {
            this.common.isLoaded();
          }
        }, 5000);
      } else {
        this.common.isLoaded();
      }
    }, 7000);
  }

  async initial() {

    this.common.isLoading();

    // fetching data 
    const _allowed = this.service.getRejectReason();
    const _data = this.service.getData();

    this.checkRoles();
    this.allowed.values = await _allowed;

    this.data = await _data;

    const code = this.data.cerTypeCode || '50001';

    if (this.data && this.data.tmbRequestNo) { // ถ้ามีข้อมูลให้เข้าเงื่อนไขแก้ไข
      this.isdownload = true;
      const { requestFile, copyFile, customSegSelect } = this.form.controls;
      const {
        accountNo, accountName, tmbRequestNo, requestDate,
        glType, tranCode, accountType, status, companyName,
        caNumber, organizeId, customerName, telephone, remark,
        requestFormFile, idCardFile, department, reqFormId, address,
        ref1, ref2, amountTmb, amountDbd, majorNo
      } = this.data;
      if (requestFormFile) {
        requestFile.clearValidators();
        requestFile.updateValueAndValidity();
      }
      if (idCardFile) {
        copyFile.clearValidators();
        copyFile.updateValueAndValidity();
      }
      if (this.user.segment) {
        customSegSelect.patchValue(this.user.segment);
      } else {
        this.custsegmentCode = this.dropdownObj.customSeg.values[0].code
        customSegSelect.patchValue(this.custsegmentCode);
      }
      this.form.get('accNo').setValidators([Validators.required, Validators.minLength(13), Validators.maxLength(13)]);
      this.form.get('accNo').updateValueAndValidity();
      this.form.patchValue({
        accNo: Acc.convertAccNo(accountNo),
        accName: accountName,
        corpName: customerName,
        corpName1: companyName,
        corpNo: organizeId,
        address: address,
        acceptNo: caNumber,
        telReq: telephone,
        reqFormId: reqFormId,
        note: remark,
        departmentName: department,
        ref1: ref1,
        ref2: ref2,
        amountDbd: amountDbd,
        amountTmb: amountTmb,
        majorNo: majorNo,
        customSegSelect: this.user.segment || this.dropdownObj.customSeg.values[0].code
      });
      const id = this.data.reqFormId.toString();
      this.reqDate = requestDate;
      this.glType = glType;
      this.tranCode = tranCode;
      this.accType = accountType;
      this.status = status;
      this.statusMsg = status;
      this.tmbReqFormId = tmbRequestNo;
      this.accNo = Acc.convertAccNo(accountNo);
      this.amountBlur('amountDbd');
      this.amountBlur('amountTmb');
      await this.checkBox(id);
    } else {
      this.reqTypeChange(code);
      this.reqDate = this.service.getReqDate();
      this.tmbReqFormId = await this.service.getTmbReqFormId();
    }

    if (this.isIEOrEdge) { // IE or EDGE
      await setTimeout(async () => {
        $('#reqtype').dropdown('set selected', code);
      }, 500);
      if (this.roles(this._roles.MAKER)) {
        await setTimeout(async () => { this.isMaker = true }, 1500);
        await setTimeout(async () => { this.common.isLoaded() }, 2000);
      } else {
        await setTimeout(async () => { this.common.isLoaded() }, 1500);
      }
    } else { // Other browsers
      if (this.roles(this._roles.MAKER)) {
        await setTimeout(async () => { this.isMaker = true }, 800);
        await setTimeout(async () => { this.common.isLoaded() }, 1250);
      } else {
        await setTimeout(async () => { $('#reqtype').dropdown('set selected', code) }, 1000);
        this.common.isLoaded();
      }
    }
  }

  private checkMatchTypeCode(): boolean {
    for (let chk = 0; chk < this.chkList.length; chk++) {
      for (let req = 0; req < this.reqTypeChanged.length; req++) {
        if (this.chkList[chk].typeCode == this.reqTypeChanged[req].typeCode) {
          return true;
        }
      }
    }
    return false;
  }

  async checkBox(id) {
    const _cert = this.service.getCert(id);
    const _chkList = this.service.getChkList(id);
    this.cert = await _cert;
    this.chkList = await _chkList;
    for (let i = 0; i < this.chkList.length; i++) {
      if (this.chkList[i].feeDbd == "" && i != 0 || this.chkList[i].typeCode == "50003") {
        this.chkList[i].children = await this.service.getChkListMore(this.chkList[i].code);
      }
    }
    this.chkList = await this.service.matchChkList(this.chkList, this.cert);
    const controls = this.form.controls;
    if (this.data && this.data.reqFormId
      && this.chkList && this.chkList.length > 0
      && this.checkMatchTypeCode()) {
      for (let index = 0; index < this.reqTypeChanged.length; index++) {
        if (index != 0) {
          this.reqTypeChanged[index].reqcertificateId = this.chkList[index - 1].reqcertificateId;
          this.reqTypeChanged[index].statementYear = this.chkList[index - 1].statementYear;
          this.reqTypeChanged[index].value = this.chkList[index - 1].value;
          this.reqTypeChanged[index].check = this.chkList[index - 1].check;
          this.reqTypeChanged[index].other = this.chkList[index - 1].other;
          this.reqTypeChanged[index].acceptedDate = this.chkList[index - 1].acceptedDate;
          this.reqTypeChanged[index].registeredDate = this.chkList[index - 1].registeredDate;
          controls[`chk${index}`].setValue(this.reqTypeChanged[index].check);
          controls[`cer${index}`].setValue(this.reqTypeChanged[index].value);
          if (this.reqTypeChanged[index].children) {
            this.showChildren = this.reqTypeChanged[index].check;
            for (let idx = 0; idx < this.reqTypeChanged[index].children.length; idx++) {
              if (idx != 0) {
                if (this.chkList[index - 1].children) {
                  this.reqTypeChanged[index].children[idx].reqcertificateId = this.chkList[index - 1].children[idx - 1].reqcertificateId;
                  this.reqTypeChanged[index].children[idx].statementYear = this.chkList[index - 1].children[idx - 1].statementYear;
                  this.reqTypeChanged[index].children[idx].value = this.chkList[index - 1].children[idx - 1].value;
                  this.reqTypeChanged[index].children[idx].check = this.chkList[index - 1].children[idx - 1].check;
                  this.reqTypeChanged[index].children[idx].other = this.chkList[index - 1].children[idx - 1].other;
                  this.reqTypeChanged[index].children[idx].acceptedDate = this.chkList[index - 1].children[idx - 1].acceptedDate;
                  this.reqTypeChanged[index].children[idx].registeredDate = this.chkList[index - 1].children[idx - 1].registeredDate;
                  controls[`chk${index}Child${idx}`].setValue(this.reqTypeChanged[index].children[idx].check);
                  controls[`cer${index}Child${idx}`].setValue(this.reqTypeChanged[index].children[idx].value);
                  if (controls[`etc${index}Child${idx}`]) {
                    controls[`etc${index}Child${idx}`].setValue(this.reqTypeChanged[index].children[idx].other);
                  }
                  if (controls[`cal${index}Child${idx}`] && this.reqTypeChanged[index].children[idx].statementYear) {
                    const years = this.reqTypeChanged[index].children[idx].statementYear.search(",") != -1 ? this.reqTypeChanged[index].children[idx].statementYear.split(",") : (this.reqTypeChanged[index].children[idx].statementYear ? [this.reqTypeChanged[index].children[idx].statementYear] : []);
                    controls[`cal${index}Child${idx}`].setValue(years); this.list = years;
                    setTimeout(() => {
                      $('#multi-select').dropdown('set selected', years);
                    }, 150);
                  }
                  if (controls[`cal${index}Child${idx}`] && this.reqTypeChanged[index].children[idx].acceptedDate) {
                    let d = new Date(this.reqTypeChanged[index].children[idx].acceptedDate),
                      month = '' + (d.getMonth() + 1),
                      day = '' + d.getDate(),
                      year = d.getFullYear();
                    this.calendar[idx].initial = moment(EnDateToThDate([digit(day), digit(month), year].join("/")), 'DD/MM/YYYY').toDate();
                  }
                  if (controls[`cal${index}Child${idx}`] && this.reqTypeChanged[index].children[idx].registeredDate) {
                    let d = new Date(this.reqTypeChanged[index].children[idx].registeredDate),
                      month = '' + (d.getMonth() + 1),
                      day = '' + d.getDate(),
                      year = d.getFullYear();
                    this.calendar[idx].initial = moment(EnDateToThDate([digit(day), digit(month), year].join("/")), 'DD/MM/YYYY').toDate();
                  }
                }
              }
            }
          }
        }
      }
    }
  }

  checkRoles() {
    if (this.roles(ROLES.REQUESTOR)) {
      this.toggleDoc = "";
      this.toggleTitle = "";
      this.form.controls.customSegSelect.clearValidators();
      this.form.controls.acceptNo.clearValidators();
      this.form.controls.address.clearValidators();
      this.form.controls.customSegSelect.setValue(this.user.segment);
      this.form.controls.payMethodSelect.setValue('30001');
      // this.payMethodChange('30001');
    }

    if (this.roles(ROLES.MAKER)) {
      this.toggleDoc = "content";
      this.toggleTitle = "title";
      if (this.data.paidTypeCode != "30004") {
        this.form.controls.ref1.setValidators([Validators.required]);
        this.form.controls.ref2.setValidators([Validators.required]);
        this.form.controls.amountDbd.setValidators([Validators.required]);
        this.form.controls.amountTmb.setValidators([Validators.required]);
      } else {
        this.hiddenReceipt4 = true;
      }
      this.form.controls.address.clearValidators();
      this.form.controls.customSegSelect.setValue('');
      this.form.controls.payMethodSelect.setValue('30001');
      this.payMethodChange('30001');
    }

    this.form.updateValueAndValidity();
  }

  roles(role: ROLES) {
    return this.common.isRole(role);
  }

  get documentMsg() {
    if (this.roles(ROLES.MAKER) || this.roles(ROLES.CHECKER)) {
      return 'รายละเอียดเอกสาร';
    }
    return 'โปรดแนบเอกสาร';
  }

  get isMakerAndSelectPaidType() {
    if (this.roles(ROLES.MAKER)) {
      if (this.form.get('payMethodSelect').value === '30002') {
        return true;
      }
      if (this.form.get('payMethodSelect').value === '30003') {
        return true;
      }
      return false;
    } else {
      return false;
    }
  }

  get onlyMaker() { return this.roles(ROLES.MAKER) }
  get onlyRequestor() { return this.roles(ROLES.REQUESTOR) }
  get onlyChecker() { return this.roles(ROLES.CHECKER) }

  get btnForPay() { return this.form.get('payMethodSelect').value == "30004" }
  get allowedSelect() { return this.formReject.controls.allowedSelect }
  get otherReason() { return this.formReject.controls.otherReason }
  get reqTypeIsNull() { return !this.reqTypeChanged || this.reqTypeChanged.length == 0 || this.form.controls.reqTypeSelect.value == '' }
  get canRequest() { return this.isdownload || this.data.reqFormId != 0 }
  get btnRequestor() { return this.roles(ROLES.REQUESTOR) }
  get btnChecker() { return this.roles(ROLES.CHECKER) }
  get btnMaker() { return this.roles(ROLES.MAKER) }
  get btnMakerApprove() { return this.roles(ROLES.MAKER) && this.common.isAuth(PAGE_AUTH.P0000401) }
  get btnMakerReject() { return (this.roles(ROLES.MAKER) || this.roles(ROLES.REQUESTOR)) && this.common.isAuth(PAGE_AUTH.P0000403) && this.data.reqFormId }
  get btnBranch() { return this.roles(ROLES.MAKER)  && this.service.getStatusCode() != "10008" }
  get fromPayment() { return this.service.getStatusCode() != "10008" }

  get authUsername() { return this.formAuth.get("authUsername") }
  get authPassword() { return this.formAuth.get("authPassword") }

  getStatus() {
    // 10001	1	สถานะคำขอ	คำขอใหม่
    // 10002	1	สถานะคำขอ	ดำเนินการชำระเงิน
    // 10003	1	สถานะคำขอ	ปฏิเสธคำขอ
    // 10004	1	สถานะคำขอ	ยกเลิกคำขอ
    // 10005	1	สถานะคำขอ	รอนุมัติการชำระเงิน
    // 10006	1	สถานะคำขอ	อนุมัติการชำระเงิน
    // 10007	1	สถานะคำขอ	ปฏิเสธการชำระเงิน
    // 10008	1	สถานะคำขอ	ชำระเงินไม่สำเร็จ
    // 10009	1	สถานะคำขอ	รออัพโหลด Certificate
    // 10010	1	สถานะคำขอ	ดำเนินการสำเร็จ
    // 10011	1	สถานะคำขอ	รอบันทึกคำขอ(ลูกค้าทำรายการเอง)
    if (this.common.isRole(ROLES.ADMIN)) {
      return "10001";
    }
    if (this.common.isRole(ROLES.REQUESTOR)) {
      return "10001";
    }
    if (this.common.isRole(ROLES.MAKER) && this.form.get('payMethodSelect').value != "30004") {
      return "10005"; // "10005"
    }
    if (this.common.isRole(ROLES.MAKER) && this.form.get('payMethodSelect').value == "30004") {
      return "10009"; // "10009"
    }
  }

  formSubmit(form: FormGroup, _data?) {
    event.preventDefault();
    this.submitted = true;
    const data = _data ? _data : {
      id: this.data.reqFormId || null,
      glType: this.glType,
      tranCode: this.tranCode,
      accType: this.accType,
      acctno: this.accNo,
      changeNameFile: this.form.controls.changeNameFile.value ? null : this.data.changeNameFile,
      idCardFile: this.form.controls.copyFile.value ? null : this.data.idCardFile,
      requestFormFile: this.form.controls.requestFile.value ? null : this.data.requestFormFile,
      status: this.getStatus(),
      tmbRequestNo: this.data.tmbRequestNo
    }
    if (this.data && this.data.reqFormId != 0) {
      this.service.save(form, this.files, this.reqTypeChanged, data, "update");
    } else {
      this.service.save(form, this.files, this.reqTypeChanged, data, "save");
    }
  }

  reject(event) {
    this.rejectSubmitted = true;
    const data = {
      reqFormId: this.data.reqFormId,
      rejectReasonCode: this.allowedSelect.value,
      rejectReasonOther: this.otherReason.value,
      status: this.service.getStatusCode()
    };
    if (this.formReject.valid) {
      this.service.rejected(data);
    }
  }

  pdf() {
    this.submitted = true;
    this.isdownload = this.service.pdf(this.form, this.data, this.reqTypeChanged, this.reqDate);
  }

  plus(control: string) {
    this.form.controls[control].setValue(parseInt(this.form.controls[control].value) + 1);
  }

  minus(control: string) {
    this.form.controls[control].setValue(parseInt(this.form.controls[control].value) - 1);
  }

  auth(e) {
    e.preventDefault();
    this.authSubmitted = true;
    if (this.formAuth.valid) {
      this.common.isLoading();
      this.service.toAuthed(this.formAuth.value).then(result => {
        if (result) {
          this.formSubmit(this.form);
        } else {
          this.modal.alert({ msg: "ทำรายการไม่สำเร็จ กรุณาดำเนินการอีกครั้งหรือติดต่อผู้ดูแลระบบ โทร. 02-299-2765" })
        }
        this.common.isLoaded();
      }).catch(error => {
        console.error(error);
        this.common.isLoaded();
        this.modal.alert({ msg: "ทำรายการไม่สำเร็จ กรุณาดำเนินการอีกครั้งหรือติดต่อผู้ดูแลระบบ โทร. 02-299-2765" })
      })
    }
  }

  dropdownActive() {

    const maxLength = 5;

    setTimeout(() => {
      this.list = [];
      const form = this.form.get($('#multi-select').attr('name'));
      let data = form ? form.value : [];
      if (typeof data == "object" && data.length > 0) {
        this.list = data;
        form.setValue(data);
      }
      $('#multi-calendar').calendar({
        type: CalendarType.YEAR,
        formatter: DateConstant.formatter(CalendarFormatter.yyyy),
        onChange: (date, text) => {
          if (this.list.length < maxLength) {
            const form = this.form.get($('#multi-select').attr('name'));
            if (this.list.findIndex(obj => obj == text) == -1) {
              this.list.push(text);
            }
            form.setValue(this.list);
            setTimeout(() => {
              $('#multi-select').dropdown('set selected', this.list);
            }, 150);
          } else {
            this.modal.alert({ msg: `เลือกปีงบประมาณได้ไม่เกิน ${maxLength} จำนวน` });
          }
        }
      });
      $('.ui.multiple.selection.dropdown').dropdown({
        onChange: (value, text, $selectedItem) => {
          this.list = this.list.filter(val => {
            for (let key in value) {
              let data = value[key].split(": ");
              data[1] = data[1].replace(/'/g, "");
              if (data[1] == val) {
                return true;
              }
            }
            return false;
          });
        }
      }).css({ "max-width": "215px", "padding": ".25em 0 .25em .25em" });
    }, 1250);
  }

  ref1Focus() {
    this.firstEnter = true; // reset FirstEnter
  }

  keytab(one, two) {
    let element1 = this[one].nativeElement; // get the sibling element
    let element2 = this[two].nativeElement; // get the sibling element
    if (this.firstEnter) {
      this.ref1.nativeElement.focus();
      this.form.get('ref1').patchValue('');
      this.form.get('ref2').patchValue('');
      this.form.get('amountDbd').patchValue('');
      if (this.form.get('amountTmb')) {
        this.form.get('amountTmb').patchValue('');
      }
      this.firstEnter = false;
      return;
    } else {
      if (two == 'tmb') {
        this.firstEnter = true; // reset FirstEnter
        const df = new DecimalFormat('###,###.00');
        let value = element1.value.replace(/,/g, '');
        if (value) {
          value = df.format(parseFloat(value) / 100);
        } else {
          value = "";
        }
        this.form.get('amountDbd').patchValue(value);
      }
      element2.focus();   // focus if not null
    }
  }

  focusOn(viewChild: string) {
    this[viewChild].nativeElement.focus();
  }

  cancel() {
    this.service.cancel(this.data.reqFormId != 0);
  }
  back() {
    this.location.back();
  }

  rejectModal() {
    this.service.toggleModal('allowed');
  }

  toggleDataCorp(e) {
    let data = e.target.checked ? this.form.controls.corpName.value : '';
    this.form.controls.corpName1.setValue(data);
  }

  toggleDocument() {
    if (this.toggleDoc === "content") {
      this.toggleDoc = "";
      this.toggleTitle = "";
    } else {
      this.toggleDoc = "content";
      this.toggleTitle = "title";
    }
  }

  async reqTypeChange(code: string) {
    this.showChildren = false;
    this.dropdownActive();
    if (code != "") {
      this.reqTypeChanged = await this.service.reqTypeChange(code);
      for (let index in this.reqTypeChanged) {
        if (index != "0") {
          let value = '';
          if (this.form.controls[`chk${index}`]) {
            this.form.setControl(`chk${index}`, new FormControl(false, [Validators.required, Validators.min(1)]));
            this.form.setControl(`cer${index}`, new FormControl({ value: value, disabled: true }, Validators.required));
          } else {
            this.form.addControl(`chk${index}`, new FormControl(false, [Validators.required, Validators.min(1)]));
            this.form.addControl(`cer${index}`, new FormControl({ value: value, disabled: true }, Validators.required));
          }
          if (!this.reqTypeChanged[index].feeDbd && !this.reqTypeChanged[index].feeTmb) {
            this.reqTypeChanged[index].children = await this.service.reqTypeChange(this.reqTypeChanged[index].code);
            for (let idx = 0; idx < this.reqTypeChanged[index].children.length; idx++) {
              if (idx != 0) {
                if (this.calendar.length !== this.reqTypeChanged[index].children.length && idx != 1) {
                  this.calendar[idx] = {
                    calendarId: `cal${index}Child${idx}`,
                    calendarName: `cal${index}Child${idx}`,
                    formGroup: this.form,
                    formControlName: `cal${index}Child${idx}`,
                    type: CalendarType.DATE,
                    formatter: CalendarFormatter.DEFAULT,
                    local: CalendarLocal.EN,
                    icon: 'calendar'
                  };
                  if (this.reqTypeChanged[index].children[idx].code == '10007') {
                    this.calendar[idx] = {
                      calendarId: `cal${index}Child${idx}`,
                      calendarName: `cal${index}Child${idx}`,
                      formGroup: this.form,
                      formControlName: `cal${index}Child${idx}`,
                      type: CalendarType.DATE,
                      formatter: CalendarFormatter.DEFAULT,
                      local: CalendarLocal.EN,
                      icon: 'calendar'
                    };
                  }
                }
                if (idx === this.reqTypeChanged[index].children.length - 1) {
                  if (this.form.controls[`etc${index}Child${idx}`]) {
                    this.form.setControl(`etc${index}Child${idx}`, new FormControl('', Validators.required));
                  } else {
                    this.form.addControl(`etc${index}Child${idx}`, new FormControl('', Validators.required));
                  }
                }
                if (this.form.controls[`chk${index}Child${idx}`] && !(this.reqTypeChanged[index].children[idx].code == '10003' || this.reqTypeChanged[index].children[idx].code == '20003' || this.reqTypeChanged[index].children[idx].code == '30002')) {
                  this.form.setControl(`chk${index}Child${idx}`, new FormControl(false, Validators.required));
                  this.form.setControl(`cer${index}Child${idx}`, new FormControl('', [Validators.required, Validators.min(1)]));
                  if (idx != 1) {
                    this.form.setControl(`cal${index}Child${idx}`, new FormControl('', Validators.required));
                  }
                } else {
                  this.form.addControl(`chk${index}Child${idx}`, new FormControl(false, Validators.required));
                  this.form.addControl(`cer${index}Child${idx}`, new FormControl('', [Validators.required, Validators.min(1)]));
                  if (idx != 1) {
                    this.form.addControl(`cal${index}Child${idx}`, new FormControl('', Validators.required));
                  }
                }
              }
            }
          }
        }
        if (index == (this.reqTypeChanged.length - 1).toString()) {
          break;
        }
      }
    }
    if (this.form.get('cer1').value == "") {
      this.form.controls[`chk1`].setValue(true);
      this.form.controls[`cer1`].setValue(1);
      if (this.form.controls[`chk2Child1`]) {
        this.form.controls[`chk2Child1`].setValue(false);
        this.form.controls[`cer2Child1`].setValue('');
      }
      this.toggleChk(1);
    }
    if (this.data && this.data.tmbRequestNo) {
      await this.checkBox(this.data.reqFormId.toString());
    }
  }

  calendarValue(name, e) {
    this.form.controls[name].setValue(e);
  }

  classByCode(e) {
    switch (e) {
      case '50001':
        return 'bg-red';
      case '50002':
        return 'bg-pink';
      case '50003':
        return 'bg-yellow';
      default:
        return ''
    }
  }

  toggleChk(index) {
    if (this.form.controls[`chk${index}`].value) {
      this.form.controls[`cer${index}`].setValue(1);
    } else {
      this.form.controls[`cer${index}`].setValue('');
    }
    if (this.reqTypeChanged[index] && this.reqTypeChanged[index].children) {
      this.showChildren = this.form.controls[`chk${index}`].value;
      this.form.get(`chk${index}`).setValue(this.showChildren);
      if (this.showChildren) {
        for (let i = 1; i < this.reqTypeChanged[index].children.length; i++) {
          this.form.controls[`chk${index}Child${i}`].setValue(false);
          this.form.controls[`cer${index}Child${i}`].setValue('');
          if (this.form.controls[`cal${index}Child${i}`]) {
            this.form.controls[`cal${index}Child${i}`].setValue('');
          }
          if (this.form.controls[`etc${index}Child${i}`]) {
            this.form.controls[`etc${index}Child${i}`].setValue('');
          }
        }
      } else {
        this.list = [];
        $('a.ui.label').remove();
        for (let i = 1; i < this.reqTypeChanged[index].children.length; i++) {
          this.form.controls[`chk${index}Child${i}`].setValue(false);
          this.form.controls[`cer${index}Child${i}`].setValue('');
          if (this.form.controls[`cal${index}Child${i}`]) {
            if (typeof this.form.controls[`cal${index}Child${i}`].value == "object") {
              if (!this.showChildren) {
                this.form.controls[`cal${index}Child${i}`].setValue(this.list);
              }
            } else {
              if (!this.showChildren) {
                this.form.controls[`cal${index}Child${i}`].setValue('');
                this.calendar[i].initial = null;
              }
            }
          }
          if (this.form.controls[`etc${index}Child${i}`]) {
            this.form.controls[`etc${index}Child${i}`].setValue('');
          }
        }
      }
    }
  }

  toggleChkChild(parent, child, item: any) {
    if (!this.form.controls[`chk${parent}Child${child}`].value) {
      for (let index = 0; index < this.reqTypeChanged.length; index++) {
        if (this.reqTypeChanged[index].children) {
          for (let idx = 0; idx < this.reqTypeChanged[index].children.length; idx++) {
            if (idx != 0 && this.form.controls[`chk${index}Child${idx}`].invalid) {
              this.form.get(`chk${index}Child${idx}`).clearValidators();
              this.form.get(`chk${index}Child${idx}`).updateValueAndValidity();
            }
          }
        }
      }
    }
    if (child != 1) {
      if (typeof this.form.controls[`cal${parent}Child${child}`].value == "object") {
        this.list = [];
        $('a.ui.label').remove();
        this.form.controls[`cal${parent}Child${child}`].setValue(this.list);
        $('#multi-select').dropdown('set selected', this.list);
      } else {
        this.form.controls[`cal${parent}Child${child}`].setValue('');
        this.calendar[child].initial = null;
      }
    }
    this.form.controls[`cer${parent}Child${child}`].setValue('');
    if (this.form.controls[`etc${parent}Child${child}`]) {
      this.form.controls[`etc${parent}Child${child}`].setValue('');
    }
    if (this.form.controls[`chk${parent}Child${child}`].value) {
      this.form.controls[`cer${parent}Child${child}`].setValue(1);
    }

    // for change muti calendar
    if (item.code == "20006" && item.typeCode == "20002") {
      this.dropdownDeactive();
    } else if (item.code == "10006" && item.typeCode == "10002") {
      this.dropdownDeactive();
    } else if (item.code == "30005" && item.typeCode == "30001") {
      this.dropdownDeactive();
    }

  }

  dropdownDeactive() {
    this.list = [];
    const form = this.form.get($('#multi-select').attr('name'));
    let data = form ? form.value : [];
    if (typeof data == "object" && data.length > 0) {
      this.list = data;
      form.setValue(data);
    }
    $('#multi-select').dropdown('restore defaults');
  }

  customSegChange(e) { }

  payMethodChange(code: string) {
    if (code !== "30003") {
      this.accNo = Acc.convertAccNo(this.data.accountNo);
      this.form.get('accNo').patchValue(this.accNo);
      this.subAccMethodChange("");
    } else {
      this.data.debitAccountType = '40002';
      this.subAccMethodChange('40002');
      this.form.controls.subAccMethodSelect.setValue('40002');
    }
    if (code !== "30004") {
      this.hiddenReceipt4 = false;
      this.form.controls.ref1.setValidators([Validators.required]);
      this.form.controls.ref2.setValidators([Validators.required]);
      this.form.controls.amountDbd.setValidators([Validators.required]);
      if (code !== "30002" && code !== "30003") {
        this.hiddenReceipt3 = false;
        this.form.controls.amountTmb.setValidators([Validators.required]);
        this.form.controls.subAccMethodSelect.clearValidators();
      } else {
        this.hiddenReceipt3 = true;
        if (code === "30003") {
          this.form.controls.subAccMethodSelect.setValidators([Validators.required]);
        } else {
          this.form.controls.subAccMethodSelect.clearValidators();
        }
        this.form.controls.amountTmb.patchValue('');
        this.form.controls.amountTmb.clearValidators();
      }
    } else {
      this.hiddenReceipt4 = true;
      this.form.controls.ref1.patchValue('');
      this.form.controls.ref2.patchValue('');
      this.form.controls.amountDbd.patchValue('');
      this.form.controls.amountTmb.patchValue('');
      this.form.controls.ref1.clearValidators();
      this.form.controls.ref2.clearValidators();
      this.form.controls.amountDbd.clearValidators();
      this.form.controls.amountTmb.clearValidators();
    }
    this.form.controls.ref1.updateValueAndValidity();
    this.form.controls.ref2.updateValueAndValidity();
    this.form.controls.amountDbd.updateValueAndValidity();
    this.form.controls.amountTmb.updateValueAndValidity();
    this.form.controls.subAccMethodSelect.updateValueAndValidity();
  }

  subAccMethodChange(e) {
    this.dropdownObj.subAccMethod.values.find(obj => {
      if (obj.code == e) {
        this.glType = obj.glType;
        this.tranCode = obj.tranCode;
        this.accType = obj.accountType;
        this.accNo = obj.accountNo;
        this.form.controls.accNo.setValidators([Validators.required, Validators.minLength(13), Validators.maxLength(13)]);
        this.form.controls.accNo.setValue(Acc.convertAccNo(obj.accountNo));
      }
    });
  }

  handleCorpName(e) {
    this.form.controls.corpName1.setValue(e.target.value);
  }

  changeUpload(control: string, data: any) {
    if (data.target.files && data.target.files[0]) {
      var reader = new FileReader();

      reader.onload = (e: any) => {
        const { name, type } = data.target.files[0];
        const fileName = name;
        const fileExtension = fileName.substr(fileName.length - 4).toLowerCase();
        if (
          fileExtension != ".pdf" && fileExtension != ".png" &&
          fileExtension != ".jpg" && fileExtension != "jpeg" &&
          fileExtension != ".doc" && fileExtension != "docx"
        ) {
          this.modal.alert({ msg: "กรุณาเลือกไฟล์ที่เป็น PDF, PNG, JPG, JPEG, DOC, DOCX เท่านั้น" });
          data.target.value = "";
          this.form.get(control).patchValue("");
        } else {
          this.files[control] = data.target.files[0];
        }
      }

      reader.readAsDataURL(data.target.files[0]);
    }
  }

  amountFocus(control: string): void {
    const controls = this.form.controls;
    let data: string = controls[control].value;
    if (data) {
      data = data.replace(/,/g, '');
    }
    controls[control].patchValue(data);
  }

  amountBlur(control: string): void {
    const controls = this.form.controls;
    const df = new DecimalFormat('###,###.00');
    controls[control].patchValue(df.format(controls[control].value));
  }

  accNoPress(e, hasDot?: boolean) {
    if (hasDot) {
      return e.charCode == 46 || e.charCode >= 48 && e.charCode <= 57;
    }
    return e.charCode >= 48 && e.charCode <= 57;
  }

  accNoBlur(): void {
    const { accNo } = this.form.controls;
    this.form.controls.accNo.setValidators([Validators.required, Validators.minLength(13), Validators.maxLength(13)]);
    this.form.controls.accNo.patchValue(Acc.convertAccNo(accNo.value));
  }

  accNoFocus(): void {
    const { accNo } = this.form.controls;
    this.form.controls.accNo.setValidators([Validators.required, Validators.minLength(10), Validators.maxLength(10)]);
    this.form.controls.accNo.patchValue(Acc.revertAccNo(accNo.value));
  }

  invalid(control: string): boolean {
    const controls = this.form.controls;
    return controls[control] && (controls[control].dirty || controls[control].touched || this.submitted) && controls[control].invalid;
  }

  download(fileName: string) {
    this.service.download(fileName);
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
