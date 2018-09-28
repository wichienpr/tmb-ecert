import { Injectable } from "@angular/core";
import { Certificate } from "tmb-ecert/models";
import { AjaxService } from "services/";
import { dateLocale } from "helpers/";

import * as Actions from './nrq02000.actions';
import { Store } from "@ngrx/store";
import { NgForm } from "@angular/forms";

const URL = {
    LOV_BY_TYPE: "lov/type",
    CER_BY_TYPE: "cer/typeCode"
}

@Injectable()
export class Nrq02000Service {

    constructor(private ajax: AjaxService, private store: Store<{}>) { }

    save(form: NgForm): void {
    }

    certificateUpdate(data: Certificate[]): void {
        this.store.dispatch(new Actions.CertificateUpdate(data));
    }

    getReqDate(): string {
        let date = new Date();
        return dateLocale(date);
    }

    reqTypeChange(e): void {
        this.ajax.post(URL.CER_BY_TYPE, { typeCode: e }, response => {
            let lists = response.json();
            const list = lists.slice(0, 1);
            let data: Certificate = {
                code: "",
                typeCode: list[0].typeCode,
                typeDesc: list[0].typeDesc,
                certificate: list[0].certificate,
                feeDbd: list[0].feeDbd,
                feeTmb: list[0].feeTmb,
            };
            lists.unshift(data);
            this.store.dispatch(new Actions.CertificateCreate(lists));
        });
    }

}