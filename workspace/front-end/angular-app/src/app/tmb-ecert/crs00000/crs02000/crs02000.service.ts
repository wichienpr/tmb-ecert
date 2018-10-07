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
        let child = [];
        obj.value = 0;
        obj.check = false;
        cert.forEach((ob, idx) => {
          console.log(obj, ob);
          if (obj.code == ob.certificateCode) {
            obj.check = true;
            obj.value += ob.totalNumber;
            child.push(ob);
          }
        });
        if (child.length > 0) {
          obj.children = child;
        }
      });
      resolve([...chkList]);
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

  cancel(): void {
    const modal: Modal = {
      msg: "ท่านต้องการกลับหรือไม่?"
    }
    this.modal.confirm(e => {
      if (e) {
        this.location.back();
      }
    }, modal);
  }

}
