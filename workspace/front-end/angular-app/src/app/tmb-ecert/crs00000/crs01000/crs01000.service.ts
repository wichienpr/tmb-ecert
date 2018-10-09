import { Injectable } from '@angular/core';
import { AjaxService } from 'app/baiwa/common/services';
import { NgForm, FormGroup, FormControl, Validators } from "@angular/forms";
import { Observable } from "rxjs";
import { dateLocale } from "helpers/";
import { HttpClient } from '@angular/common/http';


const URL = {
  FIND_REQ: "/api/crs/crs01000/findReq",
  FIND_REQ_BY_STATUS: "/api/crs/crs01000/findReqByStatus",
  COUNT_STATUS: "/api/crs/crs01000/countStatus"

  
};


@Injectable()


export class Crs01000Service {



  form: FormGroup = new FormGroup({
    reqDate: new FormControl('', Validators.required),     //วันที่ขอ
    toReqDate: new FormControl('', Validators.required),   //ถึงวันที่
    organizeId: new FormControl('', Validators.required),  //เลขที่นิติบุคคล
    companyName: new FormControl('', Validators.required),  //ชื่อนิติบุคคล
    tmbReqNo: new FormControl('', Validators.required),     //TMB Req. No.
  });




  constructor(private ajax: AjaxService) {
  }

  getfindReq() {
    return this.ajax.post(URL.FIND_REQ, { 
    reqDate: this.form.controls.reqDate.value,
    toReqDate: this.form.controls.toReqDate.value,
    organizeId: this.form.controls.organizeId.value,
    companyName: this.form.controls.companyName.value,
    tmbReqNo: this.form.controls.tmbReqNo.value,
     }, res => {
      return res.text() ? res.json() : {};
    });
  }

  
  // findReqFormByStatus() {
  //   this.ajax.post(URL.findReqFormByStatus, {}, res => {
  //     console.log(res.json());
  //   });

  // }

    /**
     * Initial Data
     */
    getForm(): Observable<FormGroup> {
      return new Observable<FormGroup>(obs => {
          obs.next(this.form);
      });
  }


  getReqDate(): string {
    let date = new Date();
    return dateLocale(date);
  }

}

