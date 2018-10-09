import { Injectable } from '@angular/core';
import { Location } from '@angular/common';
import { ModalService, AjaxService } from 'services/';
import { Modal, RequestForm, initRequestForm, RequestCertificate, Certificate } from 'models/';
import { ActivatedRoute } from '@angular/router';

const URL = {
  REQUEST_FORM: "/api/crs/crs02000/data",
  REQUEST_CERTIFICATE: "/api/crs/crs02000/cert",
  DOWNLOAD: "/api/crs/crs02000/download/",
  CER_BY_TYPE: "/api/crs/crs02000/cert/list",
  CER_BY_TYPECODE: "/api/cer/typeCode",
  CREATE_COVER: "/api/report/pdf/coverSheet/",
  CREATE_RECEIPT: "/api/report/pdf/receiptTax/",
  PDF: "/api/report/pdf/"
}

@Injectable({
  providedIn: 'root'
})
export class Crs02000Service {

  constructor(
    private route: ActivatedRoute,
    private modal: ModalService,
    private ajax: AjaxService,
    private location: Location
  ) { }

  getId() {
    return this.route.snapshot.queryParams["id"] || "";
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

  approveToggle(): any {
    const modal: Modal = {
      approveMsg: "ยืนยัน",
      rejectMsg: "ยกเลิก",
      type: "confirm",
      size: "small",
      modalId: "approve",
      msg: `<label>ยืนยันการอนุมัติการชำระเงินค่าธรรมเนียม</label>`,
      title: "อนุมัติการชำระเงินค่าธรรมเนียม"
    };
    this.modal.confirm(e => { }, modal);
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
      let data: RequestForm[] = response.json() as RequestForm[];
      return data.length > 0 ? data[0] : initRequestForm;
    })
  }

  getCert(id: String) {
    return this.ajax.get(`${URL.REQUEST_CERTIFICATE}/${id}`, response => {
      let data: RequestCertificate[] = response.json() as RequestCertificate[];
      return data;
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

  pdf(what: string) {
      const cover = "crsCover02000";
      const receipt = "crsReceipt02000";
      if (what == cover) {
        this.ajax.post(URL.CREATE_COVER + cover, {}, response => {
            this.ajax.download(URL.PDF + cover+ "/file");
        });
      }

      if (what == receipt) {
        this.ajax.post(URL.CREATE_RECEIPT + receipt, {}, response => {
            this.ajax.download(URL.PDF + receipt+ "/file");
        });
      }
  }

  cancel(): void {
    this.location.back();
  }

}
