import { Injectable } from "@angular/core";
import { Certificate, Lov } from "tmb-ecert/models";
import { AjaxService, ModalService, DropdownService } from "services/";
import { dateLocale } from "helpers/";

import { Store } from "@ngrx/store";
import { FormGroup, FormControl, Validators } from "@angular/forms";
import { Modal } from "models/";
import { Observable } from "rxjs";

const URL = {
    LOV_BY_TYPE: "lov/type",
    CER_BY_TYPE: "cer/typeCode"
}

@Injectable()
export class Nrq02000Service {

    dropdownObj: any;
    form: FormGroup = new FormGroup({
        reqTypeSelect: new FormControl('', Validators.required),        // ประเภทคำขอ
        customSegSelect: new FormControl('', Validators.required),      // Customer Segment
        payMethodSelect: new FormControl('', Validators.required),      // วิธีการรับชำระ
        subAccMethodSelect: new FormControl('', Validators.required),   // วิธีหักบัญชีจาก
        accNo: new FormControl('', Validators.required),                // เลขที่บัญชี
        accName: new FormControl('', Validators.required),              // ชื่อบัญชี
        corpNo: new FormControl('', Validators.required),               // เลขที่นิติบุคคล
        corpName: new FormControl('', Validators.required),             // ชื่อนิติบุคคล
        corpName1: new FormControl('', Validators.required),            // ชื่อนิติบุคคล 1
        acceptNo: new FormControl('', Validators.required),             // เลขที่ CA/มติอนุมัติ
        departmentName: new FormControl('', Validators.required),       // ชื่อหน่วยงาน
        tmbReceiptChk: new FormControl('', Validators.required),        // ชื่อบนใบเสร็จธนาคาร TMB
        telReq: new FormControl('', Validators.required),               // เบอร์โทรผู้ขอ/ลูกค้า
        address: new FormControl('', Validators.required),              // ที่อยู่
        note: new FormControl('', Validators.required),                 // หมายเหตุ
        requestFile: new FormControl('', Validators.required),          // ใบคำขอหนังสือรับรองนิติบุคคลและหนังสือยินยอมให้หักเงินจากบัญชีเงินฝาก
        copyFile: new FormControl('', Validators.required),             // สำเนาบัตรประชาชน
        changeNameFile: new FormControl('', Validators.required),       // สำเนาใบเปลี่ยนชื่อหรือนามสกุล
    });

    constructor(
        private ajax: AjaxService,
        private store: Store<{}>,
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
            },
            customSeg: {
                dropdownId: "customSeg",
                dropdownName: "customSeg",
                type: "search",
                formGroup: this.form,
                formControlName: "customSegSelect",
                values: [],
                valueName: "code",
                labelName: "name"
            },
            payMethod: {
                dropdownId: "payMethod",
                dropdownName: "payMethod",
                type: "search",
                formGroup: this.form,
                formControlName: "payMethodSelect",
                values: [],
                valueName: "code",
                labelName: "name"
            },
            subAccMethod: {
                dropdownId: "subAccMethod",
                dropdownName: "subAccMethod",
                type: "search",
                formGroup: this.form,
                formControlName: "subAccMethodSelect",
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

    /**
     * Forms Action
     */
    save(form: FormGroup): void {
        const modalConf: Modal = {
            msg: "...?",
            success: true
        };
        const modalAler: Modal = {
            msg: "กรุณากรอกข้อมูลให้ครบ",
            success: false
        }
        if (form.valid) {
            this.modal.confirm((e) => {
            }, modalConf);
        } else {
            this.modal.alert(modalAler);
        }
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

    reqTypeChange(e): Promise<any> {
        return this.ajax.post(URL.CER_BY_TYPE, { typeCode: e }, response => {
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
            return [...lists];
        });
    }

}