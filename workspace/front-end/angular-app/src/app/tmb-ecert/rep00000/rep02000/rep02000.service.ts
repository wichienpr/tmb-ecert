import { Injectable } from "@angular/core";
import { Lov } from "models/";
import { DropdownService } from "services/";
import { dateLocale } from "helpers/";

import { FormGroup, FormControl, Validators } from "@angular/forms";
import { Observable } from "rxjs";

@Injectable()
export class Rep02000Service {
    dropdownObj: any;
    form: FormGroup = new FormGroup({
        dateForm: new FormControl('', Validators.required),             // ตั้งแต่เดือน
        dateTo: new FormControl('', Validators.required),               // ถึงเดือน
        paidTypeSelect: new FormControl()                               // ประเภทการชำระเงิน
    });

    constructor(private dropdown: DropdownService) {
        // Dropdowns Object
        this.dropdownObj = {
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