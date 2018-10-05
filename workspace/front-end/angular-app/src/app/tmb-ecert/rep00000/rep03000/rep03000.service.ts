import { Injectable } from "@angular/core";
import { Certificate, Lov } from "models/";
import { AjaxService, ModalService } from "services/";
import { dateLocale } from "helpers/";

import { FormGroup, FormControl, Validators } from "@angular/forms";
import { Modal } from "models/";
import { Observable } from "rxjs";

const URL = {
    LOV_BY_TYPE: "/api/lov/type",
    CER_BY_TYPE: "/api/cer/typeCode"
}

@Injectable()
export class Rep03000Service {

    dropdownObj: any;
    form: FormGroup = new FormGroup({
        dateVat: new FormControl('', Validators.required),                  // เดือนปีภาษี
        organizeId: new FormControl('', Validators.required),               // เลขประจำตัวผู้เสียภาษีอากรเลขที่นิติบุคคล
        customerName: new FormControl(),                                    // ชื่อผู้ประกอบการ
         
   
        
    });

    constructor(
        private ajax: AjaxService,
        private modal: ModalService) {

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