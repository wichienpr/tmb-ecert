import { Injectable } from '@angular/core';
import { FormGroup, FormControl, Validators } from "@angular/forms";
import { Observable } from "rxjs";
import { Router } from '@angular/router';
import { ROLES } from 'app/baiwa/common/constants';
import { CommonService, ModalService } from 'app/baiwa/common/services';

@Injectable({
  providedIn: 'root'
})
export class Srn01000Service {

  form: FormGroup = new FormGroup({
    tmbReqNo: new FormControl('', Validators.required),
    status: new FormControl('')
  });


  constructor(
    private router: Router,
    private common: CommonService,
    private modal: ModalService
  ) { }

  /**
 * Initial Data
 */
  getForm(): Observable<FormGroup> {
    return new Observable<FormGroup>(obs => {
      obs.next(this.form);
    });
  }

  redirectFor(idReq: number, status: string, lockFlag: number, userId: string) {
    if (lockFlag == 1 && !this.common.isUser(userId)) {
      this.modal.confirm(
        e => {
          if (e) {
            this.router.navigate(["/crs/crs02000"], {
              queryParams: { id: idReq, statusCode: status }
            });
          }
        },
        { msg: `เนื่องจากใบคำขอนี้กำลังอยู่ในขั้นตอนดำเนินการชำระเงิน ผู้ใช้งานต้องการดูรายละเอียดข้อมูลต่อหรือไม่` });
      return;
    }
    if (status == "10001" && (this.common.isRole(ROLES.MAKER) || this.common.isRole(ROLES.REQUESTOR))) {
      this.router.navigate(["/nrq/nrq02000"], {
        queryParams: { id: idReq }
      });
      return;
    }
    if (status == "10011" && this.common.isRole(ROLES.REQUESTOR)) {
      this.router.navigate(["/nrq/nrq02000"], {
        queryParams: { id: idReq }
      });
      return;
    }
    if (status == "10003" && this.common.isRole(ROLES.REQUESTOR) && this.common.isUser(userId)) {
      this.router.navigate(["/nrq/nrq02000"], {
        queryParams: { id: idReq }
      });
      return;
    }
    this.router.navigate(["/crs/crs02000"], {
      queryParams: { id: idReq, statusCode: status }
    });
    return;
  }

}
