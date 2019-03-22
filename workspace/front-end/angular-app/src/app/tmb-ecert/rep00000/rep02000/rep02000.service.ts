import { Injectable } from "@angular/core";
import { Lov } from "models/";
import { DropdownService } from "services/";
import { dateLocale } from "helpers/";

import { FormGroup, FormControl, Validators } from "@angular/forms";
import { Observable } from "rxjs";
import * as am4core from "@amcharts/amcharts4/core";

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

    getColorObj() : am4core.Color[] {
        let colorList = [
            am4core.color('#f44336'),
            am4core.color('#e91e63'),
            am4core.color('#9c27b0'),
            am4core.color('#673ab7'),
            am4core.color('#3f51b5'),
            am4core.color('#2196f3'),
            am4core.color('#03a9f4'),
            am4core.color('#00bcd4'),
            am4core.color('#009688'),
            am4core.color('#4caf50'),
            am4core.color('#8bc34a'),
            am4core.color('#cddc39'),
            am4core.color('#ffeb3b'),
            am4core.color('#ffc107'),
            am4core.color('#ff9800'),
            am4core.color('#ff5722'),
            am4core.color('#795548')
          ]
          return colorList;
    }
}