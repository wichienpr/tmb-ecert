import { Injectable } from '@angular/core';
import { AjaxService, DropdownService, CommonService } from 'app/baiwa/common/services';
import { dateLocale } from "helpers/";
import { Router } from '@angular/router';
import { ROLES } from 'app/baiwa/common/constants';



@Injectable()
export class Crs01000Service {

  dropdownObj: any;

  constructor(
    private ajax: AjaxService,
    private dropdown: DropdownService,
    private router: Router,
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

  redirectFor(idReq: number, status: string) {
    if (status == "10001" && (this.common.isRole(ROLES.MAKER) || this.common.isRole(ROLES.REQUESTOR))) {
      this.router.navigate(["/nrq/nrq02000"], {
        queryParams: { id: idReq }
      });
      return;
    }
    if (status == "10011") {
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

