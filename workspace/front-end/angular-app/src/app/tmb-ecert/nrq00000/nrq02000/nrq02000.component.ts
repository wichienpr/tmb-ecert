import { Component, OnInit, ViewChild, ChangeDetectorRef, AfterViewInit, ElementRef } from '@angular/core';
import { FormGroup, FormControl, Validators, NgForm } from '@angular/forms';

import { Nrq02000Service } from './nrq02000.service';
import { Certificate, Calendar, CalendarType, CalendarFormatter, CalendarLocal, RequestForm, initRequestForm, RequestCertificate, Modal, Dropdown } from 'models/';
import { Acc, digit, dateLocaleEN, DecimalFormat } from 'helpers/';
import { Store } from '@ngrx/store';
import { UserDetail } from 'app/user.model';
import { CommonService } from 'app/baiwa/common/services';
import { ROLES, PAGE_AUTH } from 'app/baiwa/common/constants';

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

  data: RequestForm = initRequestForm;
  tmbReqFormId: String = "";
  form: FormGroup;
  formReject: FormGroup;
  formAuth: FormGroup;
  files: any;
  reqDate: Date;
  dropdownObj: any;
  submitted: boolean = false;
  loading: boolean = false;
  saving: boolean = false;
  isdownload: boolean = false;
  showChildren: boolean = false;
  rejectSubmitted: boolean = false;
  authSubmitted: boolean = false;
  hiddenReceipt3: boolean = false;
  hiddenReceipt4: boolean = false;

  glType: string = "";
  tranCode: string = "";
  accType: string = "";
  status: string = "";
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

  constructor(
    private service: Nrq02000Service,
    private cdRef: ChangeDetectorRef,
    private store: Store<{}>,
    private common: CommonService
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
    this.store.select("user").subscribe(user => this.user = user);
    this.init();
  }

  ngOnInit() { }

  ngAfterViewChecked() {
    this.cdRef.detectChanges();
  }

  ngAfterViewInit() {
    const code = this.data && this.data.cerTypeCode ? this.data.cerTypeCode : '50001';
    this.form.controls.reqTypeSelect.setValue(code);
    this.reqTypeChange(code);
  }

  /**
   * โหลดข้อมูล
   * @ `dropdownObj` ข้อมูล dropdown {`reqType`,`customSeg`,`payMethod`,`subAccMethod`}.
   * @ `form` ข้อมูลแบบฟอร์มคำขอ
   */
  async init() {
    this.dropdownObj = this.service.getDropdownObj();
    this.form = this.service.getForm();
    this.checkRoles();
    this.allowed.values = await this.service.getRejectReason();
    this.data = await this.service.getData();
    if (this.data && this.data.tmbRequestNo) {
      this.isdownload = true;
      const {
        accNo, accName, corpName, corpName1, corpNo, address,
        acceptNo, telReq, reqFormId, note, requestFile, copyFile,
        departmentName, ref1, ref2, amountDbd, amountTmb
      } = this.form.controls;
      const {
        accountNo, accountName, tmbRequestNo, requestDate,
        glType, tranCode, accountType, status, companyName,
        caNumber, organizeId, customerName, telephone, remark,
        requestFormFile, idCardFile, department
      } = this.data;
      const id = this.data.reqFormId.toString();
      this.reqDate = requestDate;
      this.glType = glType;
      this.tranCode = tranCode;
      this.accType = accountType;
      this.status = status;
      this.tmbReqFormId = tmbRequestNo;
      this.accNo = Acc.convertAccNo(accountNo);

      this.cert = await this.service.getCert(id);
      this.chkList = await this.service.getChkList(id);
      for (let i = 0; i < this.chkList.length; i++) {
        if (this.chkList[i].feeDbd == "" && i != 0) {
          this.chkList[i].children = await this.service.getChkListMore(this.chkList[i].code);
        }
      }
      if (this.chkList && this.chkList.length > 0) {
        this.chkList = await this.service.matchChkList(this.chkList, this.cert);
      }

      accNo.setValidators([Validators.required, Validators.minLength(13), Validators.maxLength(13)]);
      accNo.setValue(Acc.convertAccNo(accountNo));
      address.setValue(this.data.address);
      accName.setValue(accountName);
      acceptNo.setValue(caNumber);
      corpName1.setValue(companyName);
      corpName.setValue(customerName);
      corpNo.setValue(organizeId);
      departmentName.setValue(department);
      note.setValue(remark);
      telReq.setValue(telephone);
      reqFormId.setValue(this.data.reqFormId);
      ref1.setValue(this.data.ref1);
      ref2.setValue(this.data.ref2);
      amountDbd.setValue(this.data.amountDbd);
      amountTmb.setValue(this.data.amountTmb);
      this.amountBlur('amountDbd');
      this.amountBlur('amountTmb');
      if (requestFormFile) {
        requestFile.clearValidators();
        requestFile.updateValueAndValidity();
      }
      if (idCardFile) {
        copyFile.clearValidators();
        copyFile.updateValueAndValidity();
      }
    } else {
      this.reqDate = this.service.getReqDate();
      this.tmbReqFormId = await this.service.getTmbReqFormId();
    }
  }

  checkRoles() {
    if (this.roles(ROLES.REQUESTOR)) {
      this.form.controls.customSegSelect.clearValidators();
      this.form.controls.acceptNo.clearValidators();
      this.form.controls.address.clearValidators();
      this.form.controls.customSegSelect.setValue(this.user.segment);
      this.form.controls.payMethodSelect.setValue('30001');
    }

    if (this.roles(ROLES.MAKER)) {
      if (this.data.paidTypeCode != "30004") {
        this.form.controls.ref1.setValidators([Validators.required]);
        this.form.controls.ref2.setValidators([Validators.required]);
        this.form.controls.amountDbd.setValidators([Validators.required]);
        this.form.controls.amountTmb.setValidators([Validators.required]);
      } else {
        this.hiddenReceipt4 = true;
      }
      this.form.controls.acceptNo.clearValidators();
      this.form.controls.address.clearValidators();
      this.form.controls.customSegSelect.setValue(this.data.custsegmentCode);
      this.form.controls.payMethodSelect.setValue('30001');
    }

    this.form.updateValueAndValidity();
  }

  roles(role: ROLES) {
    return this.common.isRole(role);
  }

  get btnForPay() { return this.form.get('payMethodSelect').value == "30004" }
  get allowedSelect() { return this.formReject.controls.allowedSelect }
  get otherReason() { return this.formReject.controls.otherReason }
  get reqTypeIsNull() { return !this.reqTypeChanged || this.reqTypeChanged.length == 0 || this.form.controls.reqTypeSelect.value == '' }
  get btnRequestor() { return this.roles(ROLES.REQUESTOR) }
  get btnChecker() { return this.roles(ROLES.CHECKER) }
  get btnMaker() { return this.roles(ROLES.MAKER) }
  get btnMakerApprove() { return this.roles(ROLES.MAKER)&&this.common.isAuth(PAGE_AUTH.P0000401) }
  get btnMakerReject() { return this.roles(ROLES.MAKER) && this.common.isAuth(PAGE_AUTH.P0000403) }

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
    if (this.common.isRole(ROLES.MAKER)) {
      return "10005"; // "10005"
    }
  }

  formSubmit(form: FormGroup, _data?) {
    event.preventDefault();
    this.submitted = true;
    const data = _data ? _data : {
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
      glType: this.glType,
      tranCode: this.tranCode,
      accType: this.accType,
      acctno: this.accNo,
      changeNameFile: this.form.controls.changeNameFile.value ? null : this.data.changeNameFile,
      idCardFile: this.form.controls.copyFile.value ? null : this.data.idCardFile,
      requestFormFile: this.form.controls.requestFile.value ? null : this.data.requestFormFile,
      status: "10003", // ปฏิเสธ
      rejectReasonCode: this.allowedSelect.value,
      rejectReasonOther: this.otherReason.value,
      tmbRequestNo: this.data.tmbRequestNo
    }
    if (this.formReject.valid) {
      this.formSubmit(this.form, data);
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
      if(this.service.toAuthed()) {
        this.formSubmit(this.form);
      }
    }
  }

  keytab(one, two) {
    let element1 = this[one].nativeElement; // get the sibling element
    let element2 = this[two].nativeElement; // get the sibling element
    if (this.firstEnter) {
      element1.value = "";
      this.firstEnter = false;
      return ;
    } else {
      if (two=='tmb') {
        this.firstEnter = true; // reset FirstEnter
        const df = new DecimalFormat('###,###.00');
        let value = element1.value.replace(/,/g,'');
        value = parseFloat(value)/100;
        this.form.get('amountDbd').patchValue(df.format(value));
      }
      element2.focus();   // focus if not null
    }
  }

  cancel() {
    this.service.cancel();
  }

  rejectModal() {
    this.service.toggleModal('allowed');
  }

  toggleDataCorp(e) {
    let data = e.target.checked ? this.form.controls.corpName.value : '';
    this.form.controls.corpName1.setValue(data);
  }

  async reqTypeChange(e) {
    this.loading = true;
    if (e != "") {
      this.reqTypeChanged = await this.service.reqTypeChange(e);
      this.reqTypeChanged.forEach(async (obj, index) => {
        if (index != 0) {
          if (obj.code == '10007') {
            this.calend[index] = {
              calendarId: `cal${index}`,
              calendarName: `cal${index}`,
              formGroup: this.form,
              formControlName: `cal${index}`,
              type: CalendarType.DATE,
              formatter: CalendarFormatter.DEFAULT,
              local: CalendarLocal.EN,
              icon: 'calendar'
            };
            this.form.addControl(`cal${index}`, new FormControl('', Validators.required));
          }
          if (obj.code == '10006' || obj.code == '20006' || obj.code == '30005') {
            this.calend[index] = {
              calendarId: `cal${index}`,
              calendarName: `cal${index}`,
              formGroup: this.form,
              formControlName: `cal${index}`,
              type: CalendarType.YEAR,
              formatter: CalendarFormatter.yyyy,
              local: CalendarLocal.EN,
              icon: 'calendar'
            };
            this.form.addControl(`cal${index}`, new FormControl('', Validators.required));
          }
          if (this.form.controls[`chk${index}`]) {
            this.form.setControl(`chk${index}`, new FormControl(false, [Validators.required, Validators.min(1)]));
            this.form.setControl(`cer${index}`, new FormControl({ value: '', disabled: true }, Validators.required));
          } else {
            this.form.addControl(`chk${index}`, new FormControl(false, [Validators.required, Validators.min(1)]));
            this.form.addControl(`cer${index}`, new FormControl({ value: '', disabled: true }, Validators.required));
          }
          if (!obj.feeDbd) {
            obj.children = await this.service.reqTypeChange(obj.code);
            obj.children.forEach((ob, idx) => {
              if (idx != 0) {
                if (this.calendar.length !== obj.children.length && idx != 1) {
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
                if (idx === obj.children.length - 1) {
                  if (this.form.controls[`etc${index}Child${idx}`]) {
                    this.form.setControl(`etc${index}Child${idx}`, new FormControl('', Validators.required));
                  } else {
                    this.form.addControl(`etc${index}Child${idx}`, new FormControl('', Validators.required));
                  }
                }
                if (this.form.controls[`chk${index}Child${idx}`] && !(ob.code == '10003' || ob.code == '20003' || ob.code == '30002')) {
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
            });
          }
        }
        if (index == this.reqTypeChanged.length - 1) {
          return;
        }
      });
    }
    setTimeout(() => {
      const controls = this.form.controls;
      if (this.data && this.data.reqFormId
        && this.chkList && this.chkList.length > 0
        && this.chkList[0].typeCode == this.reqTypeChanged[0].typeCode) {
        this.reqTypeChanged.forEach((obj, index) => {
          if (index != 0) {
            obj.reqcertificateId = this.chkList[index - 1].reqcertificateId;
            obj.statementYear = this.chkList[index - 1].statementYear;
            obj.value = this.chkList[index - 1].value;
            obj.check = this.chkList[index - 1].check;
            obj.other = this.chkList[index - 1].other;
            obj.acceptedDate = this.chkList[index - 1].acceptedDate;
            obj.registeredDate = this.chkList[index - 1].registeredDate;
            controls[`chk${index}`].setValue(obj.check);
            controls[`cer${index}`].setValue(obj.value);
            if (controls[`cal${index}`] && obj.statementYear) {
              controls[`cal${index}`].setValue(obj.statementYear);
            }
            if (controls[`cal${index}`] && obj.acceptedDate) {
              var d = new Date(obj.acceptedDate),
                month = '' + (d.getMonth() + 1),
                day = '' + d.getDate(),
                year = d.getFullYear();
              controls[`cal${index}`].setValue([digit(day), digit(month), year].join("/"));
            }
            if (obj.children) {
              this.showChildren = obj.check;
              obj.children.forEach((ob, idx) => {
                if (idx != 0) {
                  ob.reqcertificateId = this.chkList[index - 1].children[idx - 1].reqcertificateId;
                  ob.statementYear = this.chkList[index - 1].children[idx - 1].statementYear;
                  ob.value = this.chkList[index - 1].children[idx - 1].value;
                  ob.check = this.chkList[index - 1].children[idx - 1].check;
                  ob.other = this.chkList[index - 1].children[idx - 1].other;
                  ob.acceptedDate = this.chkList[index - 1].children[idx - 1].acceptedDate;
                  ob.registeredDate = this.chkList[index - 1].children[idx - 1].registeredDate;
                  controls[`chk${index}Child${idx}`].setValue(ob.check);
                  controls[`cer${index}Child${idx}`].setValue(ob.value);
                  if (controls[`etc${index}Child${idx}`]) {
                    controls[`etc${index}Child${idx}`].setValue(ob.other);
                  }
                  if (controls[`cal${index}Child${idx}`] && ob.registeredDate) {
                    var d = new Date(ob.registeredDate),
                      month = '' + (d.getMonth() + 1),
                      day = '' + d.getDate(),
                      year = d.getFullYear();
                    controls[`cal${index}Child${idx}`].setValue([digit(day), digit(month), year].join("/"));
                  }
                }
              });
            }
          }
        });
      }
    }, 1500);
    setTimeout(() => {
      this.loading = false;
    }, 1700);
  }

  calendarValue(name, e) {
    this.form.controls[name].setValue(e);
  }

  toggleChk(index) {
    if (!this.form.controls[`chk${index}`].value) {
      this.form.controls[`cer${index}`].setValue(1);
      // if (this.form.controls[`cal${index}`]) {
      //   this.form.controls[`cal${index}`].setValue(dateLocaleEN(new Date()).split("/")[2]);
      // }
    } else {
      this.form.controls[`cer${index}`].setValue('');
    }
    if (this.reqTypeChanged[index].children) {
      this.showChildren = !this.form.controls[`chk${index}`].value;
      if (this.form.controls[`chk${index}`].value) {
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
      }
    }
  }

  toggleChkChild(parent, child) {
    if (this.form.controls[`chk${parent}Child${child}`].value) {
      this.reqTypeChanged.forEach((obj, index) => {
        if (obj.children) {
          obj.children.forEach((ob, idx) => {
            if (idx != 0 && this.form.controls[`chk${index}Child${idx}`].invalid) {
              this.form.get(`chk${index}Child${idx}`).clearValidators();
              this.form.get(`chk${index}Child${idx}`).updateValueAndValidity();
            }
          });
        }
      });
    }
    if (child != 1) {
      this.form.controls[`cal${parent}Child${child}`].setValue('');
    }
    this.form.controls[`cer${parent}Child${child}`].setValue('');
    if (this.form.controls[`etc${parent}Child${child}`]) {
      this.form.controls[`etc${parent}Child${child}`].setValue('');
    }
    if (!this.form.controls[`chk${parent}Child${child}`].value) {
      this.form.controls[`cer${parent}Child${child}`].setValue(1);
    }
  }

  customSegChange(e) {
    console.log('customSegChange => ', e);
  }

  payMethodChange(e) {
    console.log('payMethodChange => ', e);
    if (e != "30004") {
      this.hiddenReceipt4 = false;
      this.form.controls.ref1.setValidators([Validators.required]);
      this.form.controls.ref2.setValidators([Validators.required]);
      this.form.controls.amountDbd.setValidators([Validators.required]);
      if (e != "30002" && e != "30003") {
        this.hiddenReceipt3 = false;
        this.form.controls.amountTmb.setValidators([Validators.required]);
      } else {
        this.hiddenReceipt3 = true;
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
    console.log('subAccMethodChange => ', e);
  }

  handleCorpName(e) {
    this.form.controls.corpName1.setValue(e.target.value);
  }

  changeUpload(control: string, data: any) {
    this.files[control] = data.target.files[0];
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

}
