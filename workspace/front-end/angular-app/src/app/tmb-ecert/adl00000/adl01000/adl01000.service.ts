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


    constructor(
        private ajax: AjaxService,
        private modal: ModalService,
        private dropdown: DropdownService) {

    }

    getActionDropdown(){
        return this.dropdown.getaction();
    }


    getReqDate(): string {
        let date = new Date();
        return dateLocale(date);
    }


}