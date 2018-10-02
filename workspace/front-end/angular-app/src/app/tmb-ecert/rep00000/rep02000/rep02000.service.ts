import { Injectable } from "@angular/core";
import { Certificate, Lov } from "models/";
import { AjaxService, ModalService} from "services/";
import { dateLocale } from "helpers/";

import { FormGroup, FormControl, Validators } from "@angular/forms";
import { Modal } from "models/";
import { Observable } from "rxjs";

const URL = {
    LOV_BY_TYPE: "lov/type",
    CER_BY_TYPE: "cer/typeCode"
}

@Injectable()
export class Rep02000Service {

    form: FormGroup = new FormGroup({
        dateForm: new FormControl('', Validators.required),             // ตั้งแต่เดือน
        dateTo: new FormControl('', Validators.required)                // ถึงเดือน

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