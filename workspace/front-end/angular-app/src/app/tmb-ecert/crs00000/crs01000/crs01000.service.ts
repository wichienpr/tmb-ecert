import { Injectable } from '@angular/core';
import { AjaxService } from 'app/baiwa/common/services';
import { NgForm, FormGroup, FormControl, Validators } from "@angular/forms";
import { Observable } from "rxjs";
import { dateLocale } from "helpers/";


const URL = {
  testList: "crs/crs01000/list",
  findAll: "crs/crs01000/findAll",
  findReqFormByStatus: "crs/crs01000/findReqFormByStatus"
};


@Injectable()


export class Crs01000Service {

  form: FormGroup = new FormGroup({
    reqDate: new FormControl('', Validators.required),     //วันที่ขอ
    toReqDate: new FormControl('', Validators.required),   //ถึงวันที่
    organizeId: new FormControl('', Validators.required),  //เลขที่นิติบุคคล
    companyName: new FormControl('', Validators.required),  //ชื่อนิติบุคคล
    tmbReqNo: new FormControl('', Validators.required),     //TMB Req. No.
    //status : new FormControl('', Validators.required),        //status
  });




  constructor(private ajax: AjaxService) {


  }



  findAll() {
    this.ajax.post(URL.findAll, {}, res => {
      console.log(res.json());
    });
  }

  findReqFormByStatus() {
    this.ajax.post(URL.findReqFormByStatus, {}, res => {
      console.log(res.json());
    });

  }

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

