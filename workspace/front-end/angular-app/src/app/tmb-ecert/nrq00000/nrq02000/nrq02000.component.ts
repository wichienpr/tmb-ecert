import { Component, OnInit, ViewChild, ChangeDetectorRef, ElementRef, ViewChildren, QueryList } from '@angular/core';
import { Router } from '@angular/router';
import { FormGroup, FormControl, Validators, NgForm } from '@angular/forms';
import { Store } from '@ngrx/store';

import { Nrq02000Service } from './nrq02000.service';
import { Certificate, Calendar, CalendarType, CalendarFormatter, CalendarLocal } from 'models/';
import { Acc } from 'helpers/';

@Component({
  selector: 'app-nrq02000',
  templateUrl: './nrq02000.component.html',
  styleUrls: ['./nrq02000.component.css'],
  providers: [Nrq02000Service]
})
export class Nrq02000Component implements OnInit {

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
  @ViewChildren("cer") cers: QueryList<ElementRef>;
  @ViewChildren("chk") chks: QueryList<ElementRef>;
  @ViewChildren("chkChild") chkChilds: QueryList<ElementRef>;
  @ViewChildren("etcChild") etcChilds: QueryList<ElementRef>;
  @ViewChildren("calChild") calChilds: QueryList<ElementRef>;
  @ViewChildren("cerChild") cerChilds: QueryList<ElementRef>;

  tmbReqFormId: string;
  form: FormGroup;
  files: any;
  reqDate: string;
  dropdownObj: any;
  saving: boolean = false;
  isdownload: boolean = false;
  showChildren: boolean = false;

  reqTypeChanged: Certificate[];
  calendar: Calendar[] = [];

  constructor(
    private store: Store<{}>,
    private router: Router,
    private service: Nrq02000Service,
    private cdRef: ChangeDetectorRef
  ) {
    this.reqTypeChanged = [];
    this.files = {
      requestFile: null,
      copyFile: null,
      changeNameFile: null
    };
  }

  async ngOnInit() {
    // Initial Data
    this.reqDate = this.service.getReqDate();
    this.dropdownObj = this.service.getDropdownObj();
    this.form = this.service.getForm();
    this.tmbReqFormId = await this.service.getTmbReqFormId();
  }

  ngAfterViewChecked() {
    this.cdRef.detectChanges();
  }

  formSubmit(form: FormGroup, event) {
    event.preventDefault();
    // this.reqTypeSelect.nativeElement.querySelector('input').focus()
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
      chkChilds: this.chkChilds.toArray(),
      calChilds: this.chkChilds.toArray(),
      cerChilds: this.chkChilds.toArray(),
    };
    return this.service.save(form, this.files, this.reqTypeChanged, viewChilds);
  }

  pdf() {
    this.isdownload = this.service.pdf();
  }

  cancel() {
    this.service.cancel();
  }

  async reqTypeChange(e) {
    this.reqTypeChanged = await this.service.reqTypeChange(e);
    this.reqTypeChanged.forEach(async (obj, index) => {
      if (index != 0) {
        if (this.form.controls[`chk${index}`]) {
          this.form.setControl(`chk${index}`, new FormControl('', Validators.required));
          this.form.setControl(`cer${index}`, new FormControl({ value: '', disabled: true }, Validators.required));
        } else {
          this.form.addControl(`chk${index}`, new FormControl('', Validators.required));
          this.form.addControl(`cer${index}`, new FormControl({ value: '', disabled: true }, Validators.required));
        }
        if (!obj.feeDbd) {
          obj.children = await this.service.reqTypeChange(obj.code);
          obj.children.forEach((ob, idx) => {
            if (this.calendar.length !== obj.children.length) {
              if (idx == obj.children.length - 1) {
                this.calendar[idx] = {
                  calendarId: `cal${index}Child${idx}`,
                  calendarName: `cal${index}Child${idx}`,
                  formGroup: this.form,
                  formControlName: `cal${index}Child${idx}`,
                  type: CalendarType.YEAR,
                  formatter: CalendarFormatter.yyyy,
                  local: CalendarLocal.EN
                };
              } else {
                this.calendar[idx] = {
                  calendarId: `cal${index}Child${idx}`,
                  calendarName: `cal${index}Child${idx}`,
                  formGroup: this.form,
                  formControlName: `cal${index}Child${idx}`,
                  type: CalendarType.DATE,
                  formatter: CalendarFormatter.DEFAULT,
                  local: CalendarLocal.EN
                };
              }
            }
            if (idx === obj.children.length - 1) {
              if (this.form.controls[`etc${index}Child${idx}`]) {
                this.form.setControl(`etc${index}Child${idx}`, new FormControl('', Validators.required));
              } else {
                this.form.addControl(`etc${index}Child${idx}`, new FormControl('', Validators.required));
              }
            }
            if (this.form.controls[`chk${index}Child${idx}`]) {
              this.form.setControl(`chk${index}Child${idx}`, new FormControl('', Validators.required));
              this.form.setControl(`cer${index}Child${idx}`, new FormControl('', Validators.required));
              this.form.setControl(`cal${index}Child${idx}`, new FormControl('', Validators.required));
            } else {
              this.form.addControl(`chk${index}Child${idx}`, new FormControl('', Validators.required));
              this.form.addControl(`cer${index}Child${idx}`, new FormControl('', Validators.required));
              this.form.addControl(`cal${index}Child${idx}`, new FormControl('', Validators.required));
            }
          });
        }
      }
    });
  }

  accNoPress(e) {
    return e.charCode >= 48 && e.charCode <= 57;
  }

  calendarValue(name, e) {
    this.form.controls[name].setValue(e);
    console.log(this.form.controls[name]);
  }

  toggleChk(index) {
    if (this.reqTypeChanged[index].children) {
      this.showChildren = !this.form.controls[`chk${index}`].value;
    }
    this.form.controls[`cer${index}`].setValue('');
  }

  toggleChkChild(parent, child) {
    if (this.form.controls[`chk${parent}Child${child}`].value) {
      this.reqTypeChanged.forEach((obj, index) => {
        if (obj.children) {
          obj.children.forEach((ob, idx) => {
            if (this.form.controls[`chk${index}Child${idx}`].invalid && idx != 0) {
              this.form.get[`chk${index}Child${idx}`].clearValidators();
              this.form.get[`chk${index}Child${idx}`].updateValueAndValidity();
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
  }

  customSegChange(e) {
    console.log('customSegChange => ', e);
  }

  payMethodChange(e) {
    console.log('payMethodChange => ', e);
  }

  subAccMethodChange(e) {
    console.log('subAccMethodChange => ', e);
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
