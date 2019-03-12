import { Injectable } from '@angular/core';
import { Location } from '@angular/common';
import { ModalService, AjaxService, CommonService, DropdownService } from 'services/';
import { Modal, RequestForm, initRequestForm, RequestCertificate, Certificate } from 'models/';
import { ActivatedRoute, Router } from '@angular/router';
import { CertFile, Rejected, ResponseVo } from './crs02000.models';
import { REQ_STATUS } from 'app/baiwa/common/constants';
import { FormGroup } from '@angular/forms';

declare var $: any;

export const URL = {
  CONFIRM: "/api/nrq/confirm",
  REQUEST_FORM: "/api/crs/crs02000/data",
  REQUEST_CERTIFICATE: "/api/crs/crs02000/cert",
  DOWNLOAD: "/api/crs/crs02000/download/",
  CER_BY_TYPE: "/api/crs/crs02000/cert/list",
  CER_BY_TYPECODE: "/api/cer/typeCode",
  CREATE_COVER: "/api/report/pdf/coverSheet/",
  CREATE_RECEIPT: "/api/report/pdf/createAndUpload/receiptTax/",
  REQUEST_HISTORY: "/api/history/list",
  RECEIPT_HISTORY:"/api/history/receipt/list",
  PDF: "/api/report/pdf/view/",
  UPLOAD: "/api/crs/crs01000/upLoadCertificate",
  CER_REJECT: "/api/crs/crs02000/cert/reject",
  CER_APPROVE: "/api/crs/crs02000/cert/approve",
  PAYMENT_RETRY: "/api/crs/crs02000/retry",
  REPRINT_RECEIPT: "/api/report/pdf/createAndUpload/reprintReceiptTax/",
  CANCEL_RECEIPT: "/api/report/pdf/createAndUpload/cancelReceiptTax/",
  RECEIPT_DATA:"/api/crs/crs02000/receipt"
}

@Injectable({
  providedIn: 'root'
})
export class Crs02000Service {

  private hasAuthed: string = "false";
  public isPrinted:boolean = false;

  constructor(
    private route: ActivatedRoute,
    private router: Router,
    private modal: ModalService,
    private ajax: AjaxService,
    private location: Location,
    private common: CommonService,
    private dropdown: DropdownService
  ) { }

  getId() {
    return this.route.snapshot.queryParams["id"] || "";
  }

  getStatusCode() {
    return this.route.snapshot.queryParams["statusCode"] || "";
  }

  getHistory(id: string) {
    return this.ajax.get(`${URL.REQUEST_HISTORY}/${id}`, response => {
      let data: RequestForm[] = response.json() as RequestForm[];
      return data;
    });
  }

  getPaidType() {
    return this.dropdown.getpayMethod();
  }

  getChkList(id: string) {
    return this.ajax.get(`${URL.CER_BY_TYPE}/${id}`, response => {
      if (response) {
        let lists = response.json();
        const list = lists.slice(0, 1);
        let data: Certificate = {
          code: "",
          typeCode: list[0].typeCode,
          typeDesc: list[0].typeDesc,
          certificate: list[0].certificate,
          feeDbd: list[0].feeDbd,
          feeTmb: list[0].feeTmb,
        };
        lists.unshift(data);
        return [...lists];
      }
      return [];
    }, error => {
      return [];
    });
  }

  toAuthed(user: any) {
    const data = {
      username: user.authUsername,
      password: user.authPassword
    }
    return new Promise(resolve => {
      this.ajax.post(URL.CONFIRM, data, response => {
        let state = false;
        if (response) {
          state = response.json() as boolean;
        }
        if (state) {
          this.hasAuthed = "true";
        }
        resolve(state);
      })
    });
  }

  getChkListMore(id: string) {
    return this.ajax.post(URL.CER_BY_TYPECODE, { typeCode: id }, res => {
      return res.text() ? res.json() : {};
    });
  }

  getData(id: string) {
    return this.ajax.get(`${URL.REQUEST_FORM}/${id}`, response => {
      let data: RequestForm = response.json() as RequestForm;
      return data ? data : initRequestForm;
    })
  }

  getCert(id: String) {
    return this.ajax.get(`${URL.REQUEST_CERTIFICATE}/${id}`, response => {
      let data: RequestCertificate[] = response.json() as RequestCertificate[];
      return data;
    });
  }

  rejected(data: Rejected) {
    const code = data.for == "checker" ? REQ_STATUS.ST10007 : REQ_STATUS.ST10004;
    this.ajax.post(URL.CER_REJECT, data, response => {
      const data = response.json();
      if (data && data.message == "SUCCESS") {
        this.modal.alert({ msg: "ทำรายการสำเร็จ", success: true });
        this.router.navigate(['/crs/crs01000'], {
          queryParams: { codeStatus: code }
        });
        this.modal.alert({ msg: "ทำรายการสำเร็จ", success: true });
      } else {
        this.modal.alert({ msg: "ทำรายการไม่สำเร็จ กรุณาดำเนินการอีกครั้งหรือติดต่อผู้ดูแลระบบ โทร. 02-299-2765" });
      }
    }, error => {
      this.modal.alert({ msg: "ทำรายการไม่สำเร็จ กรุณาดำเนินการอีกครั้งหรือติดต่อผู้ดูแลระบบ โทร. 02-299-2765" });
    });
  }
  getReceiptData(id: String) {
    return this.ajax.get(URL.RECEIPT_DATA+"/"+id, response => {
      let data: any = response.json();
      return data;
    });
  }

  tabsToggle(name: string, tab: any): any {
    for (let key in tab) {
      if (name == key) {
        tab[key] = "active";
      } else {
        tab[key] = "";
      }
    }
    return tab;
  }

  authForSubmit() {
    $("#auth").modal("show");
  }

  approveToggle(reqFormId: string): any {
    const modal: Modal = {
      approveMsg: "ยืนยัน",
      rejectMsg: "ยกเลิก",
      type: "confirm",
      size: "small",
      modalId: "approve",
      msg: `<label>ยืนยันการอนุมัติการชำระเงินค่าธรรมเนียม</label>`,
      title: "อนุมัติการชำระเงินค่าธรรมเนียม"
    };
    this.modal.confirm(e => {
      if (e) {
        this.approveDirectly(reqFormId);
      }
    }, modal);
  }

  approveDirectly(reqFormId: string): void {
    this.common.isLoading();
    this.ajax.get(`${URL.CER_APPROVE}/${reqFormId}/${this.hasAuthed}`, response => {
      let data: ResponseVo = {
        data: {
          status: "",
          message: ""
        },
        message: "",
      };
      if (response) {
        data = response.json() as ResponseVo;
      }
      if (data.message == "SUCCESS") {
        this.modal.alert({ msg: "ทำรายการสำเร็จ", success: true });
        this.router.navigate(['/crs/crs01000'], {
          queryParams: { codeStatus: "10009" }
        });
        this.hasAuthed = "false";
        this.common.isLoaded(); // Loading page
        return;
      }
      if (data.data && data.data.message == "NEEDLOGIN") {
        this.authForSubmit();
        this.hasAuthed = "false";
        this.common.isLoaded(); // Loading page
        return;
      }
      this.modal.alert({ msg: "ทำรายการไม่สำเร็จ กรุณาดำเนินการอีกครั้งหรือติดต่อผู้ดูแลระบบ โทร. 02-299-2765", success: false });
      this.router.navigate(['/crs/crs01000'], {
        queryParams: { codeStatus: "10008" }
      });
      this.hasAuthed = "false";
      this.common.isLoaded(); // Loading page
    }, error => {
      console.error("ERROR => ", error);
      this.modal.alert({ msg: "ทำรายการไม่สำเร็จ กรุณาดำเนินการอีกครั้งหรือติดต่อผู้ดูแลระบบ โทร. 02-299-2765", success: false });
      this.router.navigate(['/crs/crs01000'], {
        queryParams: { codeStatus: "10008" }
      });
      this.hasAuthed = "false";
      this.common.isLoaded(); // Loading page
    });
  }

  matchChkList(chkList: Certificate[], cert: RequestCertificate[]) {
    return new Promise<Certificate[]>(resolve => {
      chkList.forEach((obj, index) => {
        obj.value = 0;
        obj.check = false;
        cert.forEach((ob, idx) => {
          if (obj.code == ob.certificateCode) {
            obj.check = true;
            obj.value = ob.totalNumber;
          }
        });
        if (obj.children) {
          obj.children.forEach((ob, idx) => {
            ob.check = false;
            ob.value = 0;
            cert.forEach((o, id) => {
              if (ob.code == o.certificateCode) {
                ob.registeredDate = o.registeredDate;
                ob.acceptedDate = o.acceptedDate;
                ob.statementYear = o.statementYear;
                ob.other = o.other;
                ob.check = true;
                ob.value = o.totalNumber;
                obj.check = true;
                obj.value += ob.value;
              }
            });
          });
        }
      });
      resolve(chkList);
    });
  }

  download(fileName: string): void {
    if (fileName) {
      this.ajax.download(URL.DOWNLOAD + fileName);
    } else {
      const modal: Modal = {
        msg: "ไม่พบไฟล์"
      };
      this.modal.alert(modal);
    }
  }

  pdf(what: string, id: string, tmbNo: string = "") {
    this.common.isLoading();

    const cover = "crsCover02000";
    const receipt = "crsReceipt02000";

    
      this.ajax.post(URL.CREATE_COVER, { id: parseInt(id), tmpReqNo: tmbNo }, response => {
        this.ajax.download(URL.PDF + response._body + "/download");
        this.common.isLoaded();
      }, error => {
        this.modal.alert({ msg: "ทำรายการไม่สำเร็จ กรุณาดำเนินการอีกครั้งหรือติดต่อผู้ดูแลระบบ โทร. 02-299-2765" });
        this.common.isLoaded();
      });
    

    // if (what == receipt) {
    //   this.ajax.post(URL.CREATE_RECEIPT, { id: parseInt(id) }, response => {
    //     this.ajax.download(URL.PDF + response._body + "/download");
    //     this.isPrinted = true;
    //     this.common.isLoaded();
    //   }, error => {
    //     this.modal.alert({ msg: "ทำรายการไม่สำเร็จ กรุณาดำเนินการอีกครั้งหรือติดต่อผู้ดูแลระบบ โทร. 02-299-2765" });
    //     this.common.isLoaded();
    //   });
    // }
  }

  pdfReceipt(id: string, tmbNo: string = ""){
    this.common.isLoading();
    return this.ajax.post(URL.CREATE_RECEIPT, { id: parseInt(id) }, response => {
      this.ajax.download(URL.PDF + response._body + "/download");
      // this.isPrinted = true;
      this.common.isLoaded();
      return true;
    }, error => {
      this.modal.alert({ msg: "ทำรายการไม่สำเร็จ กรุณาดำเนินการอีกครั้งหรือติดต่อผู้ดูแลระบบ โทร. 02-299-2765" });
      this.common.isLoaded();
      return false;
    });

  }
  reprintReceipt(id: string,reason:string){
    this.common.isLoading();
    this.ajax.post(URL.REPRINT_RECEIPT, { id: parseInt(id), reason: reason }, response => {
      this.ajax.download(URL.PDF + response._body + "/download");
      this.common.isLoaded();
    }, error => {
      this.modal.alert({ msg: "ทำรายการไม่สำเร็จ กรุณาดำเนินการอีกครั้งหรือติดต่อผู้ดูแลระบบ โทร. 02-299-2765" });
      this.common.isLoaded();
    });
  }

  cancelReceipt(formCancel:any){
    this.common.isLoading();
    this.ajax.post(URL.CANCEL_RECEIPT, formCancel, response => {
      this.ajax.download(URL.PDF + response._body + "/download");
      this.common.isLoaded();
    }, error => {
      this.modal.alert({ msg: "ทำรายการไม่สำเร็จ กรุณาดำเนินการอีกครั้งหรือติดต่อผู้ดูแลระบบ โทร. 02-299-2765" });
      this.common.isLoaded();
    });
  }

  saveCertFile(data: CertFile) {
    this.common.isLoading();
    const formData: FormData = new FormData();
    for (let key in data) {
      formData.append(key, data[key]);
    }
    this.ajax.upload(URL.UPLOAD, formData, response => {
      this.common.isLoaded();
      const data = response.json();
      if (data && data.message == "SUCCESS") {
        this.modal.alert({ msg: "ทำรายการสำเร็จ", success: true });
        this.router.navigate(['/crs/crs01000'], {
          queryParams: { codeStatus: "10010" }
        });
      }
      else if (data && data.message == "PRESS_UPLOAD_RECIEPTTAX") {
        this.modal.alert({ msg: "ทำรายการไม่สำเร็จ กรุณาพิมพ์ใบเสร็จ" });
      } else {
        this.modal.alert({ msg: "ทำรายการไม่สำเร็จ กรุณาดำเนินการอีกครั้งหรือติดต่อผู้ดูแลระบบ โทร. 02-299-2765" });
      }
    }, error => {
      console.error(error);
      this.common.isLoaded();
      this.modal.alert({ msg: "ทำรายการไม่สำเร็จ กรุณาดำเนินการอีกครั้งหรือติดต่อผู้ดูแลระบบ โทร. 02-299-2765" });
    });
  }

  cancel(): void {
    this.location.back();
  }

  paymentRetry(){
    // this.common.isLoading();
    let reqId = this.getId();
    const modal: Modal = {
      approveMsg: "ยืนยัน",
      rejectMsg: "ยกเลิก",
      type: "confirm",
      size: "small",
      modalId: "approve",
      msg: `<label>ยืนยันการอนุมัติการชำระเงินค่าธรรมเนียม</label>`,
      title: "อนุมัติการชำระเงินค่าธรรมเนียม"
    };
    this.modal.confirm(e => {
      if (e) {
        this.common.isLoading();
        this.ajax.get(URL.PAYMENT_RETRY+"/"+reqId, response => {
          const data = response.json();
          this.common.isLoaded();
          if (data && data.message == "SUCCESS") {
            this.modal.alert({ msg: "ทำรายการสำเร็จ", success: true });
            this.router.navigate(['/crs/crs01000'], {
              queryParams: { codeStatus: "10005" }
            });
          }else {
            this.modal.alert({ msg: "ทำรายการไม่สำเร็จ กรุณาดำเนินการอีกครั้งหรือติดต่อผู้ดูแลระบบ โทร. 02-299-2765" });
          }
        },error => {
          console.error(error);
          this.common.isLoaded();
          this.modal.alert({ msg: "ทำรายการไม่สำเร็จ กรุณาดำเนินการอีกครั้งหรือติดต่อผู้ดูแลระบบ โทร. 02-299-2765" });
        });
      }
    }, modal);


  }

}