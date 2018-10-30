import { Injectable } from '@angular/core';
import { AjaxService, DropdownService, CommonService, ModalService } from 'app/baiwa/common/services';
import { dateLocale } from "helpers/";
import { Router } from '@angular/router';
import { ROLES } from 'app/baiwa/common/constants';

@Injectable()
export class Crs01000Service {

  dropdownObj: any;

  constructor(
    private dropdown: DropdownService,
    private router: Router,
    private modal: ModalService,
    private common: CommonService
  ) {
  }

  getActionDropdown() {
    return this.dropdown.getaction();
  }

  getReqDate(): string {
    let date = new Date();
    return dateLocale(date);
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

