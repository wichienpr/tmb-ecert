import { Injectable } from "@angular/core";
import { Certificate, Lov } from "tmb-ecert/models";
import { AjaxService, ModalService, DropdownService } from "services/";
import { dateLocale } from "helpers/";

import * as Actions from './nrq02000.actions';
import { Store } from "@ngrx/store";
import { NgForm } from "@angular/forms";
import { Modal } from "models/";

const URL = {
    LOV_BY_TYPE: "lov/type",
    CER_BY_TYPE: "cer/typeCode"
}

@Injectable()
export class Nrq02000Service {

    dropdownObj: any;

    constructor(
        private ajax: AjaxService,
        private store: Store<{}>,
        private modal: ModalService,
        private dropdown: DropdownService) {
        // Reset Certificate
        this.store.dispatch(new Actions.CertificateReset([]));

        this.dropdownObj = {
            reqType: {
                dropdownId: "reqtype",
                dropdownName: "reqtype",
                type: "search",
                values: [],
                valueName: "code",
                labelName: "name"
            },
            customSeg: {
                dropdownId: "customSeg",
                dropdownName: "customSeg",
                type: "search",
                values: [],
                valueName: "code",
                labelName: "name"
            },
            payMethod: {
                dropdownId: "payMethod",
                dropdownName: "payMethod",
                type: "search",
                values: [],
                valueName: "code",
                labelName: "name"
            },
            subAccMethod: {
                dropdownId: "subAccMethod",
                dropdownName: "subAccMethod",
                type: "search",
                values: [],
                valueName: "code",
                labelName: "name"
            },
        };
        // Dropdowns
        this.dropdown.getReqType().subscribe((obj: Lov[]) => this.dropdownObj.reqType.values = obj);
        this.dropdown.getCustomSeg().subscribe((obj: Lov[]) => this.dropdownObj.customSeg.values = obj);
        this.dropdown.getpayMethod().subscribe((obj: Lov[]) => this.dropdownObj.payMethod.values = obj);
        this.dropdown.getsubAccMethod().subscribe((obj: Lov[]) => this.dropdownObj.subAccMethod.values = obj);
    }

    save(form: NgForm, reqTypeChanged: Certificate[]): void {
        const modal: Modal = {
            msg: "...?",
            success: true
        };
        this.modal.confirm((e) => {
            if (e) {
                this.certificateUpdate(reqTypeChanged);
            }
        }, modal);
    }

    send(): void {
        const modal: Modal = {
            msg: `<label>เนื่องจากระบบตรวจสอบข้อมูลพบว่าลูกค้าได้ทำการยื่นใบคำขอเอกสารรับรองประเภทนี้ไปแล้วนั้น
            <br> ลูกค้ามีความประสงค์ต้องการขอเอกสารรับรองอีกครั้งหรือไม่ ถ้าต้องการกรุณากดปุ่ม "ดำเนินการต่อ"
            <br> หากไม่ต้องการกรุณากดปุ่ม "ยกเลิก"</label>`,
            title: "แจ้งเตือนยื่นใบคำขอเอกสารรับรองซ้ำ",
            approveMsg: "ดำเนินการต่อ",
            color: "notification"
        }
        this.modal.confirm((e) => { }, modal);
    }

    certificateUpdate(data: Certificate[]): void {
        this.store.dispatch(new Actions.CertificateUpdate(data));
    }

    getReqDate(): string {
        let date = new Date();
        return dateLocale(date);
    }

    getDropdownObj(): any {
        return this.dropdownObj;
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