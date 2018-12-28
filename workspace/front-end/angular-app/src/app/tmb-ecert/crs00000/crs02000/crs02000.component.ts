import { Component, OnInit, ViewChild } from '@angular/core';
import { Modal, RequestForm, initRequestForm, RequestCertificate, Certificate, Dropdown, Lov } from 'models/';
import { Crs02000Service, URL } from './crs02000.service';
import { ROLES, PAGE_AUTH, REQ_STATUS } from 'app/baiwa/common/constants';
import { CommonService, DropdownService, ModalService } from 'app/baiwa/common/services';
import { FormGroup, FormBuilder, Validators, NgForm, FormControl } from '@angular/forms';
import { CertFile, Rejected } from './crs02000.models';
import { DatatableCofnig, DatatableDirective } from 'app/baiwa/common/directives/datatable/datatable.directive';

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
  formAuth: FormGroup;

  id: string = "";
  date: Date = new Date();
  dataLoading: boolean = false;
  certSubmitted: boolean = false;
  rejectSubmitted: boolean = false;
  authSubmitted: boolean = false;
  history: RequestForm[] = [];
  chkList: Certificate[] = [];
  data: RequestForm = initRequestForm;
  cert: RequestCertificate[] = [];
  paidModal: Modal;
  allowedModal: Modal;
  documentModal: Modal;
  allowed: Dropdown;
  tab: any;
  paidTypeString: string = "";
  paidType: Lov[] = [];

  toggleDoc: string = "content";
  toggleTitle: string = "title";

  @ViewChild("historyDt")
  historyDt: DatatableDirective;
  historyConfig: DatatableCofnig;
  
  authForSubmit: Modal;

  files: any;

  constructor(
    private service: Crs02000Service,
    private common: CommonService,
    private formBuilder: FormBuilder,
    private dropdown: DropdownService,
    private modal: ModalService
  ) {
    this.init();
    this.paidModal = { modalId: "desp", type: "custom" };
    this.allowedModal = { modalId: "allowed", type: "custom" };
    this.documentModal = { modalId: "document", type: "custom" };
    this.allowedModal = { modalId: "allowed", type: "custom" };
    this.files = {
      certFile: null,
    };
    this.formAuth = new FormGroup({
      authUsername: new FormControl('', Validators.required),
      authPassword: new FormControl('', Validators.required),
    });
    this.tab = {
      A: "active",
      B: ""
    };
    this.authForSubmit = { modalId: "auth", type: "custom" };

    this.historyConfig = {
      url: URL.REQUEST_HISTORY,
      serverSide: true,
      useBlockUi: true
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

  async getAllowed() {
    this.allowed.values = await this.dropdown.getRejectReason().toPromise();
  }

  async init() {
    this.id = this.service.getId();
    if (this.id !== "") {
      this.dataLoading = true;
      this.data = await this.service.getData(this.id);
      this.cert = await this.service.getCert(this.id);
      this.paidType = await this.service.getPaidType().toPromise();
      this.history = await this.service.getHistory(this.id);
      this.chkList = await this.service.getChkList(this.id);
      if (this.service.getStatusCode() !== "10011") {
        this.paidTypeString = this.paidType.find(obj => obj.code == this.data.paidTypeCode).name;
        for (let i = 0; i < this.chkList.length; i++) {
          if (this.chkList[i].feeDbd == "" && i != 0) {
            this.chkList[i].children = await this.service.getChkListMore(this.chkList[i].code);
          }
        }
        this.chkList = await this.service.matchChkList(this.chkList, this.cert);
      }
      setTimeout(() => {
        this.dataLoading = false;
      }, 500);
    }
  }

  auth(e) {
    e.preventDefault();
    this.authSubmitted = true;
    if (this.formAuth.valid) {
      this.common.isLoading();
      this.service.toAuthed(this.formAuth.value).then(result => {
        if (result) {
          this.service.approveDirectly(this.id);
        } else {
          this.modal.alert({ msg: "ทำรายการไม่สำเร็จ กรุณาทำรายการใหม่อีกครั้ง" })
        }
        this.common.isLoaded();
      }).catch(error => {
        console.error(error);
        this.common.isLoaded();
        this.modal.alert({ msg: "ทำรายการไม่สำเร็จ กรุณาทำรายการใหม่อีกครั้ง" })
      })
    }
  }

  roles(role: ROLES) {
    return this.common.isRole(role) || this.common.isRole(ROLES.ADMIN);
  }

  tabs(name: string) {
    if ("" == this.tab.B) {
      this.historyDt.searchParams({ reqFormId: parseInt(this.id) });
      this.historyDt.search()
    }
    this.tab = this.service.tabsToggle(name, this.tab);
  }

  approveToggle() {
    this.service.approveToggle(this.id);
  }

  modalToggle(name: string) {
    if (name == 'document' && this.data.paidTypeCode == "30004") {
      this.modal.confirm(e => {
        if (e) {
          setTimeout(() => {
            $(`#${name}`).modal('show');
          }, 400);
        }
      }, { msg: "Request นี้ยังไม่ได้ชำระเงินผ่าน E-Payment คุณยืนยันที่จะ upload file นี้หรือไม่ ?" });
    } else {
      $(`#${name}`).modal('show');
    }
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
      if (this.data.paidTypeCode == "30004") {
        const data: CertFile = {
          id: parseInt(this.id),
          status: "",
          certificates: "",
          certificatesFile: this.files.certFile,
          ignoreReceipt: "true"
        };
        this.service.saveCertFile(data);
      } else if (this.chkStatus(REQ_STATUS.ST10009)) {
        const data: CertFile = {
          id: parseInt(this.id),
          status: "",
          certificates: "",
          certificatesFile: this.files.certFile,
          ignoreReceipt: "false"
        };
        this.service.saveCertFile(data);
      }
    }
  }

  reject() {
    const what = this.roles(ROLES.CHECKER) ? 'checker' : 'requestor';
    this.rejectSubmitted = true;
    if (this.formReject.valid) {
      const data: Rejected = {
        reqFormId: this.data.reqFormId,
        rejectReasonCode: this.allowedSelect.value,
        rejectReasonOther: this.otherReason.value,
        status: this.service.getStatusCode(),
        for: what
      };
      this.service.rejected(data);
    }
  }
  paymentRetry(){
    this.service.paymentRetry();
  }

  chkStatus = status => {
    return this.service.getStatusCode() == status;
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

  get authUsername() { return this.formAuth.get("authUsername") }
  get authPassword() { return this.formAuth.get("authPassword") }

  get documentMsg() {
    if (this.roles(ROLES.MAKER) || this.roles(ROLES.CHECKER)) {
      return 'รายละเอียดเอกสาร';
    }
    return 'โปรดแนบเอกสาร';
  }
  get onlyMaker() { return this.roles(ROLES.MAKER) }
  get onRequestor() { return this.roles(ROLES.REQUESTOR) }
  get onlyChecker() { return this.roles(ROLES.CHECKER) }

  get allowedSelect() { return this.formReject.controls.allowedSelect }
  get otherReason() { return this.formReject.controls.otherReason }

  get certFile() { return this.formCert.get('certFile') }
  get btnApprove() { return (this.roles(ROLES.CHECKER) || this.roles(ROLES.SUPER_CHECKER) ) && this.chkStatus(REQ_STATUS.ST10005) && this.common.isAuth(PAGE_AUTH.P0000402) }
  get btnReject() {
    if (this.common.isAuth(PAGE_AUTH.P0000403)) {
      if ((this.roles(ROLES.CHECKER) || this.roles(ROLES.SUPER_CHECKER) ) && this.chkStatus(REQ_STATUS.ST10005)) {
        return true;
      }
      return false;
    }
    return false;
  }
  get btnPrintReciept() { return this.roles(ROLES.MAKER) && this.chkStatus(REQ_STATUS.ST10009) && this.common.isAuth(PAGE_AUTH.P0000404) }
  get btnPrintCover() { return this.roles(ROLES.MAKER) && this.chkStatus(REQ_STATUS.ST10009) && this.common.isAuth(PAGE_AUTH.P0000405) }
  get btnUpload() {
    return (this.roles(ROLES.MAKER)
      && this.chkStatus(REQ_STATUS.ST10009)
      && this.common.isAuth(PAGE_AUTH.P0000406));
    // (this.roles(ROLES.MAKER)
    // && this.chkStatus(REQ_STATUS.ST10005)
    // && this.common.isAuth(PAGE_AUTH.P0000406))
    // || 
  }

  get btnPayment(){
    return this.roles(ROLES.MAKER) && this.chkStatus(REQ_STATUS.ST10008) && this.common.isAuth(PAGE_AUTH.P0000401)
  }

}