import { Injectable } from "@angular/core";
import { dateLocale } from "helpers/";

import { FormGroup, FormControl, Validators } from "@angular/forms";
import { Observable } from "rxjs";

@Injectable()
export class Rep02100Service {

    form: FormGroup = new FormGroup({
        custsegmentCode: new FormControl('', Validators.required),       // Segment Code
        dateForm: new FormControl('', Validators.required),             // ตั้งแต่เดือน
        dateTo: new FormControl('', Validators.required)                // ถึงเดือน
    });

    constructor() {

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