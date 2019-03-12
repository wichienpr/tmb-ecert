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
  reprintForm: FormGroup;
  cancelForm: FormGroup;

  id: string = "";
  date: Date = new Date();
  dataLoading: boolean = false;
  certSubmitted: boolean = false;
  rejectSubmitted: boolean = false;
  authSubmitted: boolean = false;
  reprintSubmitted: boolean = false;
  history: RequestForm[] = [];
  chkList: Certificate[] = [];
  data: RequestForm = initRequestForm;
  cert: RequestCertificate[] = [];
  paidModal: Modal;
  allowedModal: Modal;
  documentModal: Modal;  
  reprintModal: Modal;
  cancelReceiptModal: Modal;
  allowed: Dropdown;
  tab: any;
  paidTypeString: string = "";
  paidType: Lov[] = [];
  receiptData:any;

  toggleDoc: string = "content";
  toggleTitle: string = "title";

  toggleRecp: string = "content";
  toggleTitleRecp: string = "title";

  @ViewChild("historyDt")
  historyDt: DatatableDirective;
  historyConfig: DatatableCofnig;

  @ViewChild("receiptHisDt")
  receiptHisDt: DatatableDirective;
  receiptHisConfig:DatatableCofnig;
  
  
  authForSubmit: Modal;

  files: any;

  isprintReceipt:boolean = false;
  cancelSubmitted:boolean = false;


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
    this.reprintModal = { modalId: "reprint", type: "custom" };
    this.cancelReceiptModal= { modalId: "cancel", type: "custom" };
    this.files = {
      certFile: null,
    };
    this.formAuth = new FormGroup({
      authUsername: new FormControl('', Validators.required),
      authPassword: new FormControl('', Validators.required),
    });
    this.tab = {
      A: "active",
      B: "",
      C:""

    };
    this.authForSubmit = { modalId: "auth", type: "custom" };

    this.historyConfig = {
      url: URL.REQUEST_HISTORY,
      serverSide: true,
      useBlockUi: true
    };

    this.receiptHisConfig = {
      url: URL.RECEIPT_HISTORY,
      serverSide: true,
      useBlockUi: true
    }

  }

  ngOnInit() {

    this.reprintForm = this.formBuilder.group({
      reprintReason: ['', Validators.required]
    });

    this.cancelForm = this.formBuilder.group({
      companyName: ['', Validators.required],
      taxid: ['', Validators.required],
      barnchCode: ['', Validators.required],
      address: ['', Validators.required],
      cancelReason: ['', Validators.required],
    });

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
      // this.receiptData =  await this.service.getReceiptData(this.id);
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
        this.checkPrintReceipt();
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
          this.modal.alert({ msg: "ข้อมูล Username หรือ Password ไม่ถูกต้อง กรุณาลองใหม่อีกครั้ง" })
        }
        this.common.isLoaded();
      }).catch(error => {
        console.error(error);
        this.common.isLoaded();
        this.modal.alert({ msg: "ทำรายการไม่สำเร็จ กรุณาดำเนินการอีกครั้งหรือติดต่อผู้ดูแลระบบ โทร. 02-299-2765" })
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
    } if ("" == this.tab.C){
      this.receiptHisDt.searchParams({ reqFormId: parseInt(this.id) });
      this.receiptHisDt.search()
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

  async pdf(what: string) {
    const cover = "crsCover02000";
    const receipt = "crsReceipt02000";
    // this.service.pdf(what == 'c' ? cover : receipt, this.id, this.data.tmbRequestNo);
    // setTimeout(() => {
    //   this.isprintReceipt = this.service.isPrinted;
    //   // console.log("service printed is "+this.service.isPrinted)
    // }, 500);

    if (what == 'c') {
      this.service.pdf( cover, this.id, this.data.tmbRequestNo);
    }
    else{
      this.isprintReceipt = await this.service.pdfReceipt( this.id, this.data.tmbRequestNo);
    }

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
      if (this.data.paidTypeCode == "30004"||  this.data.paidTypeCode == '30003' ||  this.data.paidTypeCode == '30002') {
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

  toggleReceipt() {
    if (this.toggleRecp=== "content") {
      this.toggleRecp = "";
      this.toggleTitleRecp = "";
    } else {
      this.toggleRecp = "content";
      this.toggleTitleRecp = "title";
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
  reprint(){
    $('#reprint').modal('show');
    // console.log("data ",this.data );
  }
  reprintSumit(){

    this.reprintSubmitted = true;
    if (this.reprintForm.valid) {
      this.service.reprintReceipt(this.id,this.reprintForm.value.reprintReason)
    }
  }

  async cancel(){
    this.receiptData = await this.service.getReceiptData(this.id);
    this.cancelForm.setValue({
      companyName:this.receiptData.customer_name,
      taxid:this.receiptData.organize_id,
      barnchCode:this.receiptData.major_no,
      address:this.receiptData.address,
      cancelReason:''});
    $('#cancel').modal('show');
  }

  cancelReceiptSubmit(){
    this.cancelSubmitted = true;
    // console.log("cancel receipt",this.cancelForm.value);
    let form = {id: parseInt(this.id),reason: this.cancelForm.value.cancelReason,
    customerName:this.cancelForm.value.companyName ,
    barnchCode:this.cancelForm.value.barnchCode,
    address:this.cancelForm.value.address,
    organizeId:this.cancelForm.value.taxid}
   
    if (this.cancelForm.valid){
      this.service.cancelReceipt(form)
    }


  }

  checkPrintReceipt(){
    if (this.data.receiptFile != "" && this.data.receiptFile != null ){
      this.isprintReceipt = true;
      // console.log("already print receipt..",this.data.receiptFile);
    }else{
      this.isprintReceipt = false;
    }

    if (this.chkStatus(REQ_STATUS.ST10010) == true){
      this.isprintReceipt = true;
    }
  }

  accNoPress(e, hasDot?: boolean) {
    if (hasDot) {
      return e.charCode == 46 || e.charCode >= 48 && e.charCode <= 57;
    }
    return e.charCode >= 48 && e.charCode <= 57;
  }


  get reprintReason() { return this.reprintForm.get("reprintReason") }

  get companyName(){return this.cancelForm.get("companyName")}
  get taxid(){return this.cancelForm.get("taxid")}
  get barnchCode(){return this.cancelForm.get("barnchCode")}
  get address(){return this.cancelForm.get("address")}
  get cancelReason(){return this.cancelForm.get("cancelReason")}

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
  get btnPrintReciept() { 
    if ( this.data.paidTypeCode == '30004' ||  this.data.paidTypeCode == '30003' ||  this.data.paidTypeCode == '30002'){
      return false;
    }else{
      // return this.roles(ROLES.MAKER) && this.chkStatus(REQ_STATUS.ST10009) && this.common.isAuth(PAGE_AUTH.P0000404) 
      return this.chkStatus(REQ_STATUS.ST10009) && this.common.isAuth(PAGE_AUTH.P0000404) && !this.isprintReceipt
    }
  }
  get btnPrintCover() { return this.chkStatus(REQ_STATUS.ST10009) && this.common.isAuth(PAGE_AUTH.P0000405) }
  get btnUpload() {
    // return (this.roles(ROLES.MAKER)
    //   && this.chkStatus(REQ_STATUS.ST10009)
    //   && this.common.isAuth(PAGE_AUTH.P0000406));
    return ( this.chkStatus(REQ_STATUS.ST10009)
    && this.common.isAuth(PAGE_AUTH.P0000406));
    
  }

  get btnPayment(){
    return this.chkStatus(REQ_STATUS.ST10008) && this.common.isAuth(PAGE_AUTH.P0000401)
  }

  get btnReprint(){
    if ( this.data.paidTypeCode == '30004' ||  this.data.paidTypeCode == '30003' ||  this.data.paidTypeCode == '30002'){
      return false;
    }else{
      // return this.roles(ROLES.MAKER) && this.chkStatus(REQ_STATUS.ST10009) && this.common.isAuth(PAGE_AUTH.P0000404) 
      return (this.chkStatus(REQ_STATUS.ST10009) || this.chkStatus(REQ_STATUS.ST10010)) && this.common.isAuth(PAGE_AUTH.P0000404) && this.isprintReceipt
    }
  }

  get btnCancelReceipt(){
    if ( this.data.paidTypeCode == '30004' ||  this.data.paidTypeCode == '30003' ||  this.data.paidTypeCode == '30002'){
      return false;
    }else{
      // return this.roles(ROLES.MAKER) && this.chkStatus(REQ_STATUS.ST10009) && this.common.isAuth(PAGE_AUTH.P0000404) 
      return (this.chkStatus(REQ_STATUS.ST10009) || this.chkStatus(REQ_STATUS.ST10010)) && this.common.isAuth(PAGE_AUTH.P0000404)  && this.isprintReceipt;
    }
  }
  get receiptToggle() {
    return (this.chkStatus(REQ_STATUS.ST10009) || this.chkStatus(REQ_STATUS.ST10010)) && this.common.isAuth(PAGE_AUTH.P0000404) ;
  }

}