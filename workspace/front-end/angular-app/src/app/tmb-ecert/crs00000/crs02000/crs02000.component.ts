import { Component, OnInit } from '@angular/core';
import { Modal, RequestForm, initRequestForm, RequestCertificate, Certificate, Dropdown } from 'models/';
import { Crs02000Service } from './crs02000.service';
import { ROLES } from 'app/baiwa/common/constants';
import { CommonService, DropdownService } from 'app/baiwa/common/services';
import { FormGroup, FormBuilder, Validators, NgForm, FormControl } from '@angular/forms';
import { CertFile, Rejected } from './crs02000.models';

declare var $: any;
@Component({
  selector: 'app-crs02000',
  templateUrl: './crs02000.component.html',
  styleUrls: ['./crs02000.component.css']
})
export class Crs02000Component implements OnInit {

  _roles = ROLES;

  formCert: FormGroup;
  formReject: FormGroup;

  id: string = "";
  date: Date = new Date();
  dataLoading: boolean = false;
  certSubmitted: boolean = false;
  rejectSubmitted: boolean = false;
  history: RequestForm[] = [];
  chkList: Certificate[] = [];
  data: RequestForm = initRequestForm;
  cert: RequestCertificate[] = [];
  paidModal: Modal;
  allowedModal: Modal;
  documentModal: Modal;
  allowed: Dropdown;
  tab: any;

  files: any;

  constructor(
    private service: Crs02000Service,
    private common: CommonService,
    private formBuilder: FormBuilder,
    private dropdown: DropdownService
  ) {
    this.init();
    this.paidModal = { modalId: "desp", type: "custom" };
    this.allowedModal = { modalId: "allowed", type: "custom" };
    this.documentModal = { modalId: "document", type: "custom" };
    this.allowedModal = { modalId: "allowed", type: "custom" };
    this.files = {
      certFile: null,
    };
    this.tab = {
      A: "active",
      B: ""
    };
  }

  ngOnInit() {
    this.formCert = this.formBuilder.group({
      certFile: ['', Validators.required]
    });
    this.formReject = this.formBuilder.group({
      allowedSelect: ['', Validators.required],
      otherReason: [''],
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
    this.getAllowed();
  }

  get certFile() { return this.formCert.get('certFile') }
  get btnApprove() { return this.roles(ROLES.ADMIN) || this.roles(ROLES.CHECKER) }
  get btnReject() { return this.roles(ROLES.ADMIN) || this.roles(ROLES.CHECKER) }
  get btnPrintReciept() { return this.roles(ROLES.ADMIN) || this.roles(ROLES.MAKER) }
  get btnPrintCover() { return this.roles(ROLES.ADMIN) || this.roles(ROLES.MAKER) }
  get btnUpload() { return this.roles(ROLES.ADMIN) || this.roles(ROLES.MAKER) }

  async getAllowed() {
    this.allowed.values = await this.dropdown.getRejectReason().toPromise();
  }

  async init() {
    this.id = this.service.getId();
    if (this.id !== "") {
      this.dataLoading = true;
      this.data = await this.service.getData(this.id);
      this.cert = await this.service.getCert(this.id);
      this.history = await this.service.getHistory(this.id);
      this.chkList = await this.service.getChkList(this.id);
      for (let i = 0; i < this.chkList.length; i++) {
        if (this.chkList[i].feeDbd == "" && i != 0) {
          this.chkList[i].children = await this.service.getChkListMore(this.chkList[i].code);
        }
      }
      this.chkList = await this.service.matchChkList(this.chkList, this.cert);
      setTimeout(() => {
        this.dataLoading = false;
      }, 500);
    }
  }

  roles(role: ROLES) {
    return this.common.isRole(role) || this.common.isRole(ROLES.ADMIN);
  }

  tabs(name: string) {
    this.tab = this.service.tabsToggle(name, this.tab);
  }

  approveToggle() {
    this.service.approveToggle();
  }

  modalToggle(name: string) {
    $(`#${name}`).modal('show');
  }

  download(fileName: string) {
    this.service.download(fileName);
  }

  pdf(what: string) {
    const cover = "crsCover02000";
    const receipt = "crsReceipt02000";
    this.service.pdf(what == 'c' ? cover : receipt, this.id, this.data.tmbRequestNo);
  }

  invalid(formGroup: FormGroup, control: string): boolean {
    const controls = formGroup.controls;
    return controls[control] && (controls[control].dirty || controls[control].touched || this.certSubmitted) && controls[control].invalid;
  }

  changeUpload(control: string, data: any) {
    this.files[control] = data.target.files[0];
  }

  back() {
    this.service.cancel();
  }

  saveCertFile() {
    this.certSubmitted = true;
    if (this.formCert.valid) {
      const data: CertFile = {
        id: parseInt(this.id),
        status: "",
        certificates: "",
        certificatesFile: this.files.certFile
      };
      this.service.saveCertFile(data);
    }
  }

  reject() {
    this.rejectSubmitted = true;
    if (this.formReject.valid) {
      const data: Rejected = {
        reqFormId: this.data.reqFormId,
        rejectReasonCode: this.allowedSelect.value,
        rejectReasonOther: this.otherReason.value
      };
      this.service.rejected(data);
    }
  }

  get allowedSelect() { return this.formReject.controls.allowedSelect }
  get otherReason() { return this.formReject.controls.otherReason }

}