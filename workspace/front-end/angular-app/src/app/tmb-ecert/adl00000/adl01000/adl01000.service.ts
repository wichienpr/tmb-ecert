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
export class Adl01000Service {

    dropdownObj: any;
    form: FormGroup = new FormGroup({
        dateForm: new FormControl('', Validators.required),             // วันที่ดำเนินการ
        dateTo: new FormControl('', Validators.required),               // ถึงวันที่
        userId: new FormControl(),                                      // User ID
        action: new FormControl(),                                      // Action
       
    });

    constructor(
        private ajax: AjaxService,
        private modal: ModalService,
        private dropdown: DropdownService) {

        this.dropdownObj = {
            action: {
                dropdownId: "action",
                dropdownName: "action",
                type: "search",
                formGroup: this.form,
                formControlName: "action",
                values: [],
                valueName: "code",
                labelName: "name"
            }
            
        };
        // Dropdowns
        this.dropdown.getaction().subscribe((obj: Lov[]) => this.dropdownObj.action.values = obj);
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