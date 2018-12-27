import { Injectable } from "@angular/core";
import { Lov } from "models/";
import { DropdownService } from "services/";
import { dateLocale } from "helpers/";

import { FormGroup, FormControl, Validators } from "@angular/forms";
import { Observable } from "rxjs";

@Injectable()
export class Rep01000Service {

    dropdownObj: any;
    form: FormGroup = new FormGroup({
        dateForm: new FormControl(''),             // วันที่ขอ
        dateTo: new FormControl(''),               // ถึงวันที่
        paymentDateForm: new FormControl(''),             // วันที่ขอ
        paymentDateTo: new FormControl(''),               // ถึงวันที่
        organizeId: new FormControl(),                                      // เลขที่นิติบุคคล
        companyName: new FormControl(),                                    // ชื่อนิติบุคคล
        reqTypeSelect: new FormControl(),                               // ประเภทคำขอ
        paidTypeSelect: new FormControl(),                              // ประเภทการชำระเงิน
    });

    constructor(private dropdown: DropdownService) {

        // Dropdowns Object
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

        // Dropdowns Data
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