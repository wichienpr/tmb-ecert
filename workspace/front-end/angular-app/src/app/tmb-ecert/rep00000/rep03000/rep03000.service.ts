import { Injectable } from "@angular/core";
import { Certificate, Lov } from "tmb-ecert/models";
import { AjaxService, ModalService, DropdownService } from "services/";
import { dateLocale } from "helpers/";

import { FormGroup, FormControl, Validators } from "@angular/forms";
import { Modal } from "models/";
import { Observable } from "rxjs";

const URL = {
    LOV_BY_TYPE: "lov/type",
    CER_BY_TYPE: "cer/typeCode"
}

@Injectable()
export class Rep01000Service {

    dropdownObj: any;
    form: FormGroup = new FormGroup({
        dateVat: new FormControl('', Validators.required),              // เดือนปีภาษี
        corpNo: new FormControl('', Validators.required),               // เลขที่นิติบุคคล
        corpName: new FormControl('', Validators.required),             // ชื่อนิติบุคคล
        reqTypeSelect: new FormControl('', Validators.required),        // ประเภทคำขอ
       
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
            }
        };
        // Dropdowns
        this.dropdown.getReqType().subscribe((obj: Lov[]) => this.dropdownObj.reqType.values = obj);
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