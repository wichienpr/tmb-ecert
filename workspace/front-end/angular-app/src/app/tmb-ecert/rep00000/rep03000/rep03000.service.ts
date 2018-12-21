import { Injectable } from "@angular/core";
import { dateLocale } from "helpers/";

import { FormGroup, FormControl, Validators } from "@angular/forms";
import { Observable } from "rxjs";

@Injectable()
export class Rep03000Service {

    dropdownObj: any;
    form: FormGroup = new FormGroup({
        dateVat: new FormControl('', Validators.required),                  // เดือนปีภาษี
        organizeId: new FormControl('',),               // เลขประจำตัวผู้เสียภาษีอากรเลขที่นิติบุคคล
        customerName: new FormControl(),                                    // ชื่อผู้ประกอบการ
    });

    constructor() { }

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