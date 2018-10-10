import { Component, OnInit, ViewChild, ChangeDetectorRef, ElementRef, ViewChildren, QueryList, AfterViewInit } from '@angular/core';
import { FormGroup, FormControl, Validators, NgForm } from '@angular/forms';

import { Nrq02000Service } from './nrq02000.service';
import { Certificate, Calendar, CalendarType, CalendarFormatter, CalendarLocal, RequestForm, initRequestForm, RequestCertificate } from 'models/';
import { Acc, dateLocaleEN } from 'helpers/';
import { Store } from '@ngrx/store';
import { UserDetail } from 'app/user.model';
import { CommonService } from 'app/baiwa/common/services';
import { ROLES } from 'app/baiwa/common/constants';

@Component({
  selector: 'app-nrq02000',
  templateUrl: './nrq02000.component.html',
  styleUrls: ['./nrq02000.component.css'],
  providers: [Nrq02000Service]
})
export class Nrq02000Component implements OnInit, AfterViewInit {

  @ViewChild("ngForm") ngForm: NgForm;
  @ViewChild("corpNo") corpNo: ElementRef;
  @ViewChild("corpName") corpName: ElementRef;
  @ViewChild("acceptNo") acceptNo: ElementRef;
  @ViewChild("departmentName") departmentName: ElementRef;
  @ViewChild("accNo") accNo: ElementRef;
  @ViewChild("accName") accName: ElementRef;
  @ViewChild("tmbReceiptChk") tmbReceiptChk: ElementRef;
  @ViewChild("corpName1") corpName1: ElementRef;
  @ViewChild("telReq") telReq: ElementRef;
  @ViewChild("requestFile") requestFile: ElementRef;
  @ViewChild("address") address: ElementRef;
  @ViewChild("copyFile") copyFile: ElementRef;
  @ViewChild("reqTypeSelect") reqTypeSelect: ElementRef;
  @ViewChild("customSegSelect") customSegSelect: ElementRef;
  @ViewChild("payMethodSelect") payMethodSelect: ElementRef;
  @ViewChild("subAccMethodSelect") subAccMethodSelect: ElementRef;
  @ViewChildren("cal") cals: QueryList<ElementRef>;
  @ViewChildren("cer") cers: QueryList<ElementRef>;
  @ViewChildren("chk") chks: QueryList<ElementRef>;
  @ViewChildren("chkChild") chkChilds: QueryList<ElementRef>;
  @ViewChildren("etcChild") etcChilds: QueryList<ElementRef>;
  @ViewChildren("calChild") calChilds: QueryList<ElementRef>;
  @ViewChildren("cerChild") cerChilds: QueryList<ElementRef>;

  _roles = ROLES;

  data: RequestForm = initRequestForm;
  tmbReqFormId: String;
  form: FormGroup;
  files: any;
  reqDate: Date;
  dropdownObj: any;
  loading: boolean = false;
  saving: boolean = false;
  isdownload: boolean = false;
  showChildren: boolean = false;

  glType: string = "";
  tranCode: string = "";
  accType: string = "";
  status: string = "";
  acctno: string = "";

  cert: RequestCertificate[] = [];
  chkList: Certificate[] = [];
  reqTypeChanged: Certificate[];
  calend: Calendar[] = [];
  calendar: Calendar[] = [];
  user: UserDetail;

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
    this.store.select("user").subscribe(user => this.user = user);
    // Initial Data
    this.init();
  }

  ngOnInit() {
  }

  ngAfterViewChecked() {
    this.cdRef.detectChanges();
  }

  ngAfterViewInit() {
    const code = this.data && this.data.cerTypeCode ? this.data.cerTypeCode : '50001';
    this.form.controls.reqTypeSelect.setValue(code);
    this.reqTypeChange(code);
  }

  async init() {
    this.dropdownObj = this.service.getDropdownObj();
    this.form = this.service.getForm();
    this.checkRoles();
    this.data = await this.service.getData();
    if (this.data && this.data.tmbRequestNo) {
      this.isdownload = true;
      const {
        accNo, accName, corpName, corpName1, corpNo, address,
        acceptNo, telReq, reqFormId, note
      } = this.form.controls;
      const {
        accountNo, accountName, tmbRequestNo, requestDate,
        glType, tranCode, accountType, status, companyName,
        caNumber, organizeId, customerName, telephone, remark,
      } = this.data;
      const id = this.data.reqFormId.toString();
      this.reqDate = requestDate;
      this.glType = glType;
      this.tranCode = tranCode;
      this.accType = accountType;
      this.status = status;
      this.tmbReqFormId = tmbRequestNo;
      this.acctno = Acc.convertAccNo(accountNo);

      this.cert = await this.service.getCert(id);
      this.chkList = await this.service.getChkList(id);
      for (let i = 0; i < this.chkList.length; i++) {
        if (this.chkList[i].feeDbd == "" && i != 0) {
          this.chkList[i].children = await this.service.getChkListMore(this.chkList[i].code);
        }
      }
      this.chkList = await this.service.matchChkList(this.chkList, this.cert);

      accNo.setValidators([Validators.required, Validators.minLength(13), Validators.maxLength(13)]);
      accNo.setValue(Acc.convertAccNo(accountNo));
      accName.setValue(accountName);
      corpName.setValue(customerName);
      corpName1.setValue(companyName);
      acceptNo.setValue(caNumber);
      corpNo.setValue(organizeId);
      address.setValue(this.data.address);
      telReq.setValue(telephone);
      note.setValue(remark);
      reqFormId.setValue(this.data.reqFormId);
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
      this.form.controls.acceptNo.clearValidators();
      this.form.controls.address.clearValidators();
      this.form.controls.customSegSelect.setValue(this.user.segment);
      this.form.controls.payMethodSelect.setValue('30001');
    }
  }

  roles(role: string) {
    switch (role) {
      case ROLES.REQUESTOR:
        return this.common.isRole(ROLES.REQUESTOR);
      case ROLES.MAKER:
        return this.common.isRole(ROLES.MAKER);
    }
  }

  formSubmit(form: FormGroup, event) {
    event.preventDefault();
    // this.reqTypeSelect.nativeElement.querySelector('input').focus()
    const data = {
      glType: this.glType,
      tranCode: this.tranCode,
      accType: this.accType,
      status: this.status,
      acctno: this.acctno,
    }
    let viewChilds = {
      corpNo: this.corpNo,
      corpName: this.corpName,
      acceptNo: this.acceptNo,
      departmentName: this.departmentName,
      accNo: this.accNo,
      accName: this.accName,
      tmbReceiptChk: this.tmbReceiptChk,
      corpName1: this.corpName1,
      telReq: this.telReq,
      requestFile: this.requestFile,
      address: this.address,
      copyFile: this.copyFile,
      reqTypeSelect: this.reqTypeSelect,
      customSegSelect: this.customSegSelect,
      payMethodSelect: this.payMethodSelect,
      subAccMethodSelect: this.subAccMethodSelect,
      chks: this.chks.toArray(),
      cers: this.cers.toArray(),
      cals: this.cals.toArray(),
      chkChilds: this.chkChilds.toArray(),
      calChilds: this.calChilds.toArray(),
      cerChilds: this.cerChilds.toArray(),
    };
    if (this.data && this.data.reqFormId != 0) {
      this.service.save(form, this.files, this.reqTypeChanged, viewChilds, data, "update");
    } else {
      this.service.save(form, this.files, this.reqTypeChanged, viewChilds, data, "save");
    }
  }

  pdf() {
    this.isdownload = this.service.pdf();
  }

  cancel() {
    this.service.cancel();
  }

  toggleDataCorp(e) {
    let data = e.target.checked ? this.form.controls.corpName.value : '';
    this.form.controls.corpName1.setValue(data);
  }

  async reqTypeChange(e) {
    this.loading = true;
    if (e != "") {
      this.reqTypeChanged = await this.service.reqTypeChange(e);
      // if (this.reqTypeChanged[0].typeCode == this.chkList[0].typeCode) {
      //   this.reqTypeChanged = this.chkList;
      // }
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
              if (this.calendar.length !== obj.children.length) {
                // if (idx == obj.children.length - 1) {
                //   this.calendar[idx] = {
                //     calendarId: `cal${index}Child${idx}`,
                //     calendarName: `cal${index}Child${idx}`,
                //     formGroup: this.form,
                //     formControlName: `cal${index}Child${idx}`,
                //     type: CalendarType.YEAR,
                //     formatter: CalendarFormatter.yyyy,
                //     local: CalendarLocal.EN,
                //     icon: 'calendar'
                //   };
                // } else {
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
                // }
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
                this.form.setControl(`cal${index}Child${idx}`, new FormControl('', Validators.required));
              } else {
                this.form.addControl(`chk${index}Child${idx}`, new FormControl(false, Validators.required));
                this.form.addControl(`cer${index}Child${idx}`, new FormControl('', [Validators.required, Validators.min(1)]));
                this.form.addControl(`cal${index}Child${idx}`, new FormControl('', Validators.required));
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
      if (this.data && this.data.reqFormId && this.chkList && this.chkList[0].typeCode == this.reqTypeChanged[0].typeCode) {
        this.reqTypeChanged.forEach((obj, index) => {
          if (index != 0) {
            obj.reqcertificateId = this.chkList[index].reqcertificateId;
            obj.statementYear = this.chkList[index].statementYear;
            obj.value = this.chkList[index].value;
            obj.check = this.chkList[index].check;
            obj.other = this.chkList[index].other;
            obj.acceptedDate = this.chkList[index].acceptedDate;
            obj.registeredDate = this.chkList[index].registeredDate;
            controls[`chk${index}`].setValue(obj.check);
            controls[`cer${index}`].setValue(obj.value);
            if (controls[`cal${index}`] && obj.statementYear) {
              controls[`cal${index}`].setValue(obj.statementYear);
            }
            if (controls[`cal${index}`] && obj.acceptedDate) {
              const splited = obj.acceptedDate.split("-");
              const date = new Date(splited[2], splited[1], splited[0]);
              controls[`cal${index}`].setValue(dateLocaleEN(date));
            }
            if (obj.children) {
              this.showChildren = obj.check;
              obj.children.forEach((ob, idx) => {
                if (idx != 0) {
                  ob.reqcertificateId = this.chkList[index].children[idx - 1].reqcertificateId;
                  ob.statementYear = this.chkList[index].children[idx - 1].statementYear;
                  ob.value = this.chkList[index].children[idx - 1].value;
                  ob.check = this.chkList[index].children[idx - 1].check;
                  ob.other = this.chkList[index].children[idx - 1].other;
                  ob.acceptedDate = this.chkList[index].children[idx - 1].acceptedDate;
                  ob.registeredDate = this.chkList[index].children[idx - 1].registeredDate;
                  controls[`chk${index}Child${idx}`].setValue(ob.check);
                  controls[`cer${index}Child${idx}`].setValue(ob.value);
                  if (controls[`etc${index}Child${idx}`]) {
                    controls[`etc${index}Child${idx}`].setValue(ob.other);
                  }
                  if (controls[`cal${index}Child${idx}`] && ob.registeredDate) {
                    const splited = ob.registeredDate.split("-");
                    const date = new Date(splited[2], splited[1], splited[0]);
                    controls[`cal${index}Child${idx}`].setValue(dateLocaleEN(date));
                  }
                }
              });
            }
          }
        });
      }
      console.log(this.chkList, this.reqTypeChanged);
    }, 800);
    setTimeout(() => {
      this.loading = false;
    }, 1000);
  }

  accNoPress(e) {
    return e.charCode >= 48 && e.charCode <= 57;
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
        for (let i = 0; i < this.reqTypeChanged[index].children.length; i++) {
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
    this.form.controls[`cal${parent}Child${child}`].setValue('');
    this.form.controls[`cer${parent}Child${child}`].setValue('');
    if (this.form.controls[`etc${parent}Child${child}`]) {
      this.form.controls[`etc${parent}Child${child}`].setValue('');
    }
    if (!this.form.controls[`chk${parent}Child${child}`].value) {
      // this.form.controls[`cal${parent}Child${child}`].setValue(dateLocaleEN(new Date()));
      this.form.controls[`cer${parent}Child${child}`].setValue(1);
    }
  }

  customSegChange(e) {
    console.log('customSegChange => ', e);
  }

  payMethodChange(e) {
    console.log('payMethodChange => ', e);
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

  accNoBlur(): void {
    const { accNo } = this.form.controls;
    this.form.controls.accNo.setValidators([Validators.required, Validators.minLength(13), Validators.maxLength(13)]);
    this.form.controls.accNo.setValue(Acc.convertAccNo(accNo.value));
  }

  accNoFocus(): void {
    const { accNo } = this.form.controls;
    this.form.controls.accNo.setValidators([Validators.required, Validators.minLength(10), Validators.maxLength(10)]);
    this.form.controls.accNo.setValue(Acc.revertAccNo(accNo.value));
  }

  invalid(input: FormControl): boolean {
    return input && (input.dirty || input.touched || this.ngForm.submitted) && input.invalid;
  }

}
