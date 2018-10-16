import { Injectable } from "@angular/core";
import { FormGroup, FormControl, Validators } from "@angular/forms";
import { Router, ActivatedRoute } from "@angular/router";
import { Observable } from "rxjs";

import { Certificate, Lov, RequestForm, initRequestForm, Modal, RequestCertificate } from "models/";
import { AjaxService, ModalService, DropdownService, RequestFormService } from "services/";
import { Acc, Assigned } from "helpers/";

import { Nrq02000 } from "./nrq02000.model";

const URL = {
    LOV_BY_TYPE: "/api/lov/type",
    CER_BY_TYPE: "/api/cer/typeCode",
    CER_BY_CODE: "/api/crs/crs02000/cert/list",
    CER_BY_TYPECODE: "/api/cer/typeCode",
    NRQ_SAVE: "/api/nrq/save",
    NRQ_UPDATE: "/api/nrq/update",
    NRQ_DOWNLOAD: "/api/nrq/download/",
    NRQ_PDF: "/api/nrq/pdf/",
    REQUEST_FORM: "/api/nrq/data",
    CREATE_FORM: "/api/report/pdf/reqFormOriginal/",
    FORM_PDF: "/api/report/pdf/",
    REQUEST_CERTIFICATE: "/api/crs/crs02000/cert",
    DOWNLOAD: "/api/crs/crs02000/download/",
}

@Injectable()
export class Nrq02000Service {
    private tmbReqFormId: string = "";
    private dropdownObj: any;
    private form: FormGroup = new FormGroup({
        reqFormId: new FormControl(''),
        reqTypeSelect: new FormControl('', Validators.required),        // ประเภทคำขอ
        customSegSelect: new FormControl('', Validators.required),      // Customer Segment
        payMethodSelect: new FormControl('', Validators.required),      // วิธีการรับชำระ
        subAccMethodSelect: new FormControl(),                          // วิธีหักบัญชีจาก
        accNo: new FormControl('', Validators.required),                // เลขที่บัญชี
        accName: new FormControl('', Validators.required),              // ชื่อบัญชี
        corpNo: new FormControl('', Validators.required),               // เลขที่นิติบุคคล
        corpName: new FormControl('', Validators.required),             // ชื่อนิติบุคคล
        corpName1: new FormControl(),                                   // ชื่อนิติบุคคล 1
        acceptNo: new FormControl('', Validators.required),             // เลขที่ CA/มติอนุมัติ
        departmentName: new FormControl(),                              // ชื่อหน่วยงาน
        telReq: new FormControl(),                                      // เบอร์โทรผู้ขอ/ลูกค้า
        address: new FormControl(),                                     // ที่อยู่
        note: new FormControl(),                                        // หมายเหตุ
        requestFile: new FormControl('', Validators.required),          // ใบคำขอหนังสือรับรองนิติบุคคลและหนังสือยินยอมให้หักเงินจากบัญชีเงินฝาก
        copyFile: new FormControl('', Validators.required),             // สำเนาบัตรประชาชน
        changeNameFile: new FormControl(),                              // สำเนาใบเปลี่ยนชื่อหรือนามสกุล
        ref1: new FormControl(),
        ref2: new FormControl(),
        amount: new FormControl(),
    });

    constructor(
        private ajax: AjaxService,
        private modal: ModalService,
        private dropdown: DropdownService,
        private router: Router,
        private route: ActivatedRoute,
        private reqService: RequestFormService
    ) {

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

    async getRejectReason() {
        return await this.dropdown.getRejectReason().toPromise();
    }


    /**
     * @return `requestForm` ข้อมูลแบบฟอร์มคำขอจาก `id`
     */
    getData(): Promise<RequestForm> {
        let id = this.route.snapshot.queryParams["id"] || "";
        if (id !== "") {
            return new Promise(async resolve => {
                const data = await this.reqService.getReqFormByFormId(id).toPromise();
                resolve(data && data.length > 0 ? data[0] : initRequestForm);
            });
        } else {
            return new Promise(resolve => {
                resolve(initRequestForm);
            });
        }
    }

    /**
     * @return `tmbReqFormId` รหัส tmbRequestFormNo
     */
    getTmbReqFormId(): Promise<string> {
        return new Observable<string>(obs => {
            this.reqService.getTmbReqFormNo().subscribe(tmbReqNo => {
                this.tmbReqFormId = tmbReqNo;
                obs.next(this.tmbReqFormId);
                obs.complete();
            });
        }).toPromise();
    }

    /**
     * 
     * @param id รหัส cerTypeCode
     * @return `lists` ลิสต์ของ certificates
     */
    getChkList(id: string) {
        return this.ajax.get(`${URL.CER_BY_CODE}/${id}`, response => {
            let lists = response.json();
            let list;
            if (list && list.length > 0) {
                list = lists.slice(0, 1);
                let data: Certificate = {
                    code: "",
                    typeCode: list[0].typeCode,
                    typeDesc: list[0].typeDesc,
                    certificate: list[0].certificate,
                    feeDbd: list[0].feeDbd,
                    feeTmb: list[0].feeTmb,
                };
                lists.unshift(data);
            }
            return [...lists];
        });
    }

    /**
     * 
     * @param id รหัส typeCode
     * @return `children` ดึงข้อมูลลูกของลิสต์ certificates
     */
    getChkListMore(id: string) {
        return this.ajax.post(URL.CER_BY_TYPECODE, { typeCode: id }, res => {
            return res.text() ? res.json() : {};
        });
    }

    getCert(id: String) {
        return this.ajax.get(`${URL.REQUEST_CERTIFICATE}/${id}`, response => {
            let data: RequestCertificate[] = response.json() as RequestCertificate[];
            return data;
        });
    }

    getForm(): FormGroup {
        return this.form;
    }

    getReqDate(): Date {
        let date = new Date();
        return date;
    }

    getDropdownObj(): any {
        return this.dropdownObj;
    }

    /**
     * ขั้นตอนการทำการบันทึกหรืออัพเดท
     * @param form ค่า `FormGroup` จาก `Component`
     * @param files ค่า `Files` ที่อัพโหลดไว้
     * @param _certificates ลิสต์ของ `certificates`
     * @param viewChilds ตัวแปรสำหรับดึง `Element` ใน `HTML`
     * @param addons ข้อมูลเพิ่มเติม
     * @param what เงื่อนไข `save` หรือ `update`
     */
    save(form: FormGroup, files: any, _certificates: Certificate[], addons: any, what: string = "save") {
        let certificates = new Assigned().getValue(_certificates);
        let chkCerts = this.chkCerts(certificates, form);
        certificates = [...chkCerts.data]; // clear validators checkbox
        if (!chkCerts.flag) {
            this.modal.alertWithAct({ msg: ValidatorMessages.selectSomeCerts });
            return chkCerts.form;
        }
        if (!chkCerts.total) {
            this.modal.alertWithAct({ msg: ValidatorMessages.totalCerts });
            return chkCerts.form;
        }
        let modalAler: Modal = { msg: "", success: false }
        for (let key in form.controls) {
            if (form.controls[key].invalid) {
                for (let enu in ValidatorMessages) {
                    if (enu == key) {
                        modalAler.msg = ValidatorMessages[enu];
                        modalAler.success = false;
                        this.modal.alert(modalAler);
                        return;
                    }
                }
            }
        }
        const modalConf: Modal = {
            msg: `ต้องการดำเนินการบันทึกหรือไม่ ?`,
            title: "ยืนยันการทำรายการ"
            // msg: `<label>เนื่องจากระบบตรวจสอบข้อมูลพบว่าลูกค้าได้ทำการยื่นใบคำขอเอกสารรับรองประเภทนี้ไปแล้วนั้น
            //     <br> ลูกค้ามีความประสงค์ต้องการขอเอกสารรับรองอีกครั้งหรือไม่ ถ้าต้องการกรุณากดปุ่ม "ดำเนินการต่อ"
            //     <br> หากไม่ต้องการกรุณากดปุ่ม "ยกเลิก"</label>`,
            // title: "แจ้งเตือนยื่นใบคำขอเอกสารรับรองซ้ำ",
            // approveMsg: "ดำเนินการต่อ",
            // color: "notification"
        }
        this.modal.confirm((e) => {
            if (e) {
                const formData = this.bindingData(certificates, files, form, addons);
                let url = what == "save" ? URL.NRQ_SAVE : URL.NRQ_UPDATE;
                this.ajax.upload(url, formData, response => {
                    if (response.json().message == "SUCCESS") {
                        const modal: Modal = {
                            msg: "บันทึกข้อมูลสำเร็จ",
                            // msg: "ระบบบันทึกข้อมูล Request Form สำหรับทำรายการให้ลูกค้าลงนามเข้าสู่ระบบ e-Certificate พร้อมสถานะการทำงานเป็น “คำขอใหม่” จากนั้นระบบแสดงหน้าจอรายละเอียดบันทึกคำขอและพิมพ์แบบฟอร์มให้ลูกค้าลงนาม",
                            success: true
                        };
                        this.modal.alert(modal);
                        this.router.navigate(['/crs/crs01000'], {
                            queryParams: { codeStatus: "10001" }
                        });
                    } else {
                        const modal: Modal = {
                            msg: "ทำรายการไม่สำเร็จ กรุณาดำเนินการอีกครั้งหรือติดต่อผู้ดูแลระบบ",
                            success: false
                        };
                        this.modal.alert(modal);
                    }
                }, err => {
                    console.error(err)
                });
            }
        }, modalConf);
    }

    chkCerts(certificates: Certificate[], form: FormGroup): any {
        let has: boolean = false;
        let hasTotal: boolean = false;
        certificates.forEach((obj, index) => {
            if (index != 0) {
                if (form.controls['chk' + index].valid) {
                    if (obj.children) {
                        obj.children.forEach((ob, idx) => {
                            if (idx != 0) {
                                if (form.controls['chk' + index + 'Child' + idx].valid) {
                                    has = true;
                                    if (form.controls['cer' + index + 'Child' + idx].value > 0) {
                                        hasTotal = true;
                                    }
                                } else {
                                    form.get('chk' + index + 'Child' + idx).setValidators([Validators.required]);
                                    form.get('chk' + index + 'Child' + idx).updateValueAndValidity();
                                }
                            }
                        });
                    } else {
                        has = true;
                        if (form.controls['cer' + index].value > 0) {
                            hasTotal = true;
                        }
                    }
                } else {
                    form.get('chk' + index).setValidators([Validators.required]);
                    form.get('chk' + index).updateValueAndValidity();
                }
            }
        });
        if (has) {
            certificates.forEach((obj, index) => {
                if (index != 0) {
                    if (form.controls['chk' + index].invalid) {
                        form.get('chk' + index).clearValidators();
                        form.get('chk' + index).updateValueAndValidity();
                    } else {
                        if (obj.children) {
                            obj.children.forEach((ob, idx) => {
                                if (idx != 0) {
                                    if (form.controls['chk' + index + 'Child' + idx].invalid) {
                                        form.get('chk' + index + 'Child' + idx).clearValidators();
                                        form.get('chk' + index + 'Child' + idx).updateValueAndValidity();
                                    }
                                }
                            });
                        }
                    }
                }
            });
        }
        return { data: certificates, flag: has, total: hasTotal, form: form };
    }

    pdf(data: any): boolean {
        this.reqService.getPdf(URL.CREATE_FORM, data);
        return true;
    }

    cancel(): void {
        const modal: Modal = {
            msg: "ท่านต้องการยกเลิกส่งคำขอหรือไม่?"
        }
        this.modal.confirm(e => {
            if (e) {
                this.router.navigate(['/home']);
            }
        }, modal);
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

    download(fileName: string): void {
        if (fileName) {
            this.ajax.download(URL.DOWNLOAD + fileName);
        } else {
            const modal: Modal = {
                msg: "ไม่พบไฟล์"
            };
            this.modal.alert(modal);
        }
    }

    matchChkList(chkList: Certificate[], cert: RequestCertificate[]) {
        return new Promise<Certificate[]>(resolve => {
            chkList.forEach((obj, index) => {
                obj.value = 0;
                obj.check = false;
                cert.forEach((ob, idx) => {
                    if (obj.code == ob.certificateCode) {
                        obj.reqcertificateId = ob.reqCertificateId;
                        obj.check = true;
                        obj.value = ob.totalNumber;
                        obj.acceptedDate = ob.acceptedDate;
                        obj.statementYear = ob.statementYear;
                    }
                });
                if (obj.children) {
                    obj.children.forEach((ob, idx) => {
                        ob.check = false;
                        ob.value = 0;
                        cert.forEach((o, id) => {
                            if (ob.code == o.certificateCode) {
                                ob.reqcertificateId = o.reqCertificateId;
                                ob.registeredDate = o.registeredDate;
                                ob.acceptedDate = o.acceptedDate;
                                ob.other = o.other;
                                ob.check = true;
                                ob.value = o.totalNumber;
                                obj.check = true;
                                obj.value += ob.value;
                            }
                        });
                    });
                }
            });
            resolve(chkList);
        });
    }

    /**
     * จัดการข้อมูล เพื่อส่งไป บันทึก(`save`) หรือ เปลี่ยนสถานะ (`status`)
     * @param certificates ลิสต์ของ `certificates`
     * @param files ค่า `Files` ที่อัพโหลดไว้
     * @param form ค่า `FormGroup` จาก `Component`
     * @param addons ข้อมูลเพิ่มเติม
     */
    bindingData(certificates: Certificate[], files: any, form: FormGroup, addons: any): FormData {
        let formData = new FormData();
        let _data = [];
        certificates.forEach((obj, index) => {
            if (index != 0) {
                obj.check = form.controls['chk' + index].value;
                obj.value = form.controls['cer' + index].value;
                if (obj.check == "" && obj.value == "") {
                    obj.check = false;
                    obj.value = 0;
                }
                if (obj.code == '10007') {
                    let str = form.controls['cal' + index].value.split("/");
                    obj.acceptedDate = new Date(str[2], str[1], str[0]);
                }
                if (obj.code == '10006' || obj.code == '20006' || obj.code == '30005') {
                    let value = parseInt(form.controls['cal' + index].value);
                    obj.statementYear = value;
                }
                if (obj.children) {
                    obj.children.forEach((ob, idx) => {
                        if (idx != 0) {
                            ob.check = form.controls['chk' + index + 'Child' + idx].value;
                            ob.value = form.controls['cer' + index + 'Child' + idx].value;
                            if (idx == 1) {
                                ob.registeredDate = null;
                            }
                            if (idx != 1) {
                                let str = form.controls['cal' + index + 'Child' + idx].value.split("/");
                                ob.registeredDate = new Date(parseInt(str[2]), parseInt(str[1]) - 1, parseInt(str[0]));
                            }
                            if (idx == obj.children.length - 1) {
                                ob.other = form.controls['etc' + index + 'Child' + idx].value;
                            }
                            if (ob.check == "" && ob.value == "") {
                                ob.check = false;
                                ob.value = 0;
                            }
                            _data = [..._data, ob];
                        }
                    })
                    obj.children = [];
                    obj.check = false;
                    obj.value = 0;
                }
                _data = [..._data, obj];
            }
        });
        let data: Nrq02000 = {
            glType: addons.glType,
            tranCode: addons.tranCode,
            accountType: addons.accType,
            status: addons.status,
            reqFormId: form.controls.reqFormId.value,
            tmbReqFormNo: this.tmbReqFormId,
            acceptNo: form.controls.acceptNo.value,
            accName: form.controls.accName.value,
            accNo: Acc.revertAccNo(form.controls.accNo.value),
            address: form.controls.address.value,
            changeNameFile: files.changeNameFile ? files.changeNameFile : null,
            copyFile: files.copyFile ? files.copyFile : null,
            requestFile: files.requestFile ? files.requestFile : null,
            corpName: form.controls.corpName.value,
            corpName1: form.controls.corpName1.value,
            corpNo: form.controls.corpNo.value,
            departmentName: form.controls.departmentName.value,
            note: form.controls.note.value,
            reqTypeSelect: form.controls.reqTypeSelect.value,
            customSegSelect: form.controls.customSegSelect.value,
            payMethodSelect: form.controls.payMethodSelect.value,
            subAccMethodSelect: form.controls.subAccMethodSelect.value,
            telReq: form.controls.telReq.value,
            certificates: _data,
            changeNameFileName: addons.changeNameFile,
            copyFileName: addons.idCardFile,
            requestFileName: addons.requestFormFile,
            ref1: form.controls.ref1.value,
            ref2: form.controls.ref2.value,
            amount: form.controls.amount.value,
            rejectReasonCode: addons.rejectReasonCode,
            rejectReasonOther: addons.rejectReasonOther
        };
        for (let key in data) {
            if (data[key]) {
                if (key == "certificates") {
                    formData.append(key, JSON.stringify(data[key]));
                } else {
                    formData.append(key, data[key]);
                }
            }
        }
        return formData;
    }

    toggleModal(name: string) {
        this.modal.show(name);
    }

}

export enum ValidatorMessages {
    totalCerts = "กรุณาระบุจำนวนเอกสารรับรอง ที่ทำการยื่นคำขอ",
    selectSomeCerts = "กรุณาเลือกเอกสารรับรองที่ต้องการอย่างน้อย 1 รายการ",
    reqTypeSelect = "reqTypeSelect",
    customSegSelect = "customSegSelect",
    payMethodSelect = "payMethodSelect",
    subAccMethodSelect = "subAccMethodSelect",
    accNo = "accNo",
    accName = "accName",
    corpNo = "corpNo",
    corpName = "corpName",
    corpName1 = "corpName1",
    acceptNo = "acceptNo",
    departmentName = "departmentName",
    telReq = "telReq",
    address = "address",
    note = "note",
    requestFile = "requestFile",
    copyFile = "copyFile",
    changeNameFile = "changeNameFile",
    ref1 = "ref1",
    ref2 = "ref2",
    amount = "amount",
}