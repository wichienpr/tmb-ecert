import { Injectable } from '@angular/core';
import { Location } from '@angular/common';
import { ModalService, AjaxService } from 'services/';
import { Modal, RequestForm, initRequestForm, RequestCertificate, Certificate } from 'models/';
import { ActivatedRoute, Router } from '@angular/router';
import { CertFile } from './crs02000.models';

const URL = {
  REQUEST_FORM: "/api/crs/crs02000/data",
  REQUEST_CERTIFICATE: "/api/crs/crs02000/cert",
  DOWNLOAD: "/api/crs/crs02000/download/",
  CER_BY_TYPE: "/api/crs/crs02000/cert/list",
  CER_BY_TYPECODE: "/api/cer/typeCode",
  CREATE_COVER: "/api/report/pdf/coverSheet/",
  CREATE_RECEIPT: "/api/report/pdf/createAndUpload/receiptTax/",
  REQUEST_HISTORY: "/api/history/list",
  PDF: "/api/report/pdf/view/",
  UPLOAD: "/api/crs/crs01000/upLoadCertificate",
  CER_REJECT: "/api/crs/crs02000/cert/reject",
  CER_APPROVE: "/api/crs/crs02000/cert/approve"
}

@Injectable({
  providedIn: 'root'
})
export class Crs02000Service {

  constructor(
    private route: ActivatedRoute,
    private router: Router,
    private modal: ModalService,
    private ajax: AjaxService,
    private location: Location
  ) { }

  getId() {
    return this.route.snapshot.queryParams["id"] || "";
  }

  getHistory(id: string) {
    return this.ajax.get(`${URL.REQUEST_HISTORY}/${id}`, response => {
      let data: RequestForm[] = response.json() as RequestForm[];
      return data;
    });
  }

  getChkList(id: string) {
    return this.ajax.get(`${URL.CER_BY_TYPE}/${id}`, response => {
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

  rejected(data: any) {
    this.ajax.post(URL.CER_REJECT, data, response => {
      const data = response.json();
      if (data && data.message == "SUCCESS") {
        this.modal.alert({ msg: "ทำรายการสำเร็จ", success: true });
        this.router.navigate(['/crs/crs01000'], {
          queryParams: { codeStatus: "10003" }
        });
        this.modal.alert({ msg: "ทำรายการสำเร็จ", success: true });
      } else {
        this.modal.alert({ msg: "ทำรายการไม่สำเร็จ กรุณาทำรายการใหม่หรือติดต่อผู้ดูแลระบบ", success: true });
      }
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
        this.ajax.get(`${URL.CER_APPROVE}/${reqFormId}`, response => {
          if (response.json().message == "SUCCESS") {
            this.router.navigate(['/crs/crs01000'], {
              queryParams: { codeStatus: "10009" }
            });
          } else {
            this.modal.alert({ msg: "ทำรายการไม่สำเร็จ กรุณาดำเนินการอีกครั้งหรือติดต่อผู้ดูแลระบบ" });
          }
        });
      }
    }, modal);
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
            obj.acceptedDate = ob.acceptedDate;
            obj.statementYear = ob.statementYear;
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

    const cover = "crsCover02000";
    const receipt = "crsReceipt02000";

    if (what == cover) {
      this.ajax.post(URL.CREATE_COVER, { id: parseInt(id), tmpReqNo: tmbNo }, response => {
        this.ajax.download(URL.PDF + response._body + "/download");
      });
    }

    if (what == receipt) {
      this.ajax.post(URL.CREATE_RECEIPT, { id: parseInt(id) }, response => {
        this.ajax.download(URL.PDF + response._body + "/download");
      });
    }

  }

  saveCertFile(data: CertFile) {
    const formData: FormData =  new FormData();
    for(let key in data) {
      formData.append(key, data[key]);
    }
    this.ajax.upload(URL.UPLOAD, formData, response => {
      const data = response.json();
      if(data && data.message == "SUCCESS") {
        this.modal.alert({ msg: "ทำรายการสำเร็จ", success: true });
      }
      else if (data && data.message == "PRESS_UPLOAD_RECIEPTTAX") {
        this.modal.alert({ msg: "ทำรายการไม่สำเร็จ กรุณาพิมพ์ใบเสร็จ" });
      } else {
        this.modal.alert({ msg: "ทำรายการไม่สำเร็จ กรุณาดำเนินการอีกครั้งหรือติดต่อผู้ดูแลระบบ" });
      }
    });
  }

  cancel(): void {
    this.location.back();
  }

}