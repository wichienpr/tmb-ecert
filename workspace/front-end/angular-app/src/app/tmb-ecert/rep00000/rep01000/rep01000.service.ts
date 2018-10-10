import { Injectable } from "@angular/core";
import { Certificate, Lov } from "models/";
import { AjaxService, ModalService, DropdownService } from "services/";
import { dateLocale } from "helpers/";

import { FormGroup, FormControl, Validators } from "@angular/forms";
import { Modal } from "models/";
import { Observable } from "rxjs";

const URL = {
    LOV_BY_TYPE: "/api/lov/type",
    CER_BY_TYPE: "/api/cer/typeCode"
}

@Injectable()
export class Rep01000Service {

    dropdownObj: any;
    form: FormGroup = new FormGroup({
        dateForm: new FormControl('', Validators.required),             // วันที่ขอ
        dateTo: new FormControl('', Validators.required),               // ถึงวันที่
        corpNo: new FormControl(),                                      // เลขที่นิติบุคคล
        corpName: new FormControl(),                                    // ชื่อนิติบุคคล
        reqTypeSelect: new FormControl(),                               // ประเภทคำขอ
        paidTypeSelect: new FormControl(),                              // ประเภทการชำระเงิน
       
    });

    constructor(
        private ajax: AjaxService,
        private modal: ModalService,
        private dropdown: DropdownService) {

        this.dropdownObj = {
            reqType: {
                dropdownId: "reqtype",
                dropdownName: "reqtype",
                type: "search",
                formGroup: this.form,
                formControlName: "reqTypeSelect",
                values: [],
                valueName: "code",
                labelName: "name"
            },
            paidType: {
                dropdownId: "paidtype",
                dropdownName: "paidtype",
                type: "search",
                formGroup: this.form,
                formControlName: "paidTypeSelect",
                values: [],
                valueName: "code",
                labelName: "name"
            }
        };
        // Dropdowns
        this.dropdown.getReqType().subscribe((obj: Lov[]) => this.dropdownObj.reqType.values = obj);
        this.dropdown.getpayMethod().subscribe((obj: Lov[]) => this.dropdownObj.paidType.values = obj);
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

    getDropdownObj(): any {
        return this.dropdownObj;
    }


}