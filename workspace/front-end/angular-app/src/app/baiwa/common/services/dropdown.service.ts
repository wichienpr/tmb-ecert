import { Injectable } from "@angular/core";
import { Lov } from "tmb-ecert/models";
import { Observable } from "rxjs";
import { AjaxService } from "./ajax.service";

const URL = {
    LOV_BY_TYPE: "lov/type"
}

@Injectable()
export class DropdownService { // TABLE => ECERT_LISTOFVALUE
    private reqType: Lov[]; // ประเภทคำขอ
    private customSeg: Lov[]; // Customer Segment
    private payMethod: Lov[]; // วิธีชำระ
    private subAccMethod: Lov[]; // วีธีหักจากธนาคาร
    
    constructor(private ajax: AjaxService) {
        /**
         * ถ้าไม่มีข้อมูลจะใช้ข้อมูล Mock
         */
        this.reqType = reqTypeMock;
        this.customSeg = customSegMock;
        this.payMethod = payMethodMock;
        this.subAccMethod = subAccMethodMock;
    }

    getReqType(): Observable<Lov[]> { // ประเภทคำขอ
        return new Observable(obs => {
            this.ajax.post(URL.LOV_BY_TYPE, { type: 5 }, result => {
                const data = result.json();
                if (data && data.length > 0) {
                    this.reqType = data;
                }
                obs.next([...this.reqType]);
            });
        });
    }

    getCustomSeg(): Observable<Lov[]> { // Customer Segment
        return new Observable(obs => {
            this.ajax.post(URL.LOV_BY_TYPE, { type: 2 }, result => {
                const data = result.json();
                if (data && data.length > 0) {
                    this.customSeg = data;
                }
                obs.next([...this.customSeg]);
            });
        });
    }

    getpayMethod(): Observable<Lov[]> { // วิธีชำระ
        return new Observable(obs => {
            this.ajax.post(URL.LOV_BY_TYPE, { type: 3 }, result => {
                const data = result.json();
                if (data && data.length > 0) {
                    this.payMethod = data;
                }
                obs.next([...this.payMethod]);
            });
        });
    }

    getsubAccMethod(): Observable<Lov[]> { // วีธีหักจากธนาคาร
        return new Observable(obs => {
            this.ajax.post(URL.LOV_BY_TYPE, { type: 4 }, result => {
                const data = result.json();
                if (data && data.length > 0) {
                    this.subAccMethod = data;
                }
                obs.next([...this.subAccMethod]);
            });
        });
    }
}



/**
 * Initial Mock Data
 */
const reqTypeMock: Lov[] = [
    { code: "50001", type: 5, typeDesc: "ประเภทคำขอ", name: "ห้างหุ้นส่วนจำกัด บริษัทจำกัดและบริษัทมหาชนจำกัด", sequence: 0 },
    { code: "50002", type: 5, typeDesc: "ประเภทคำขอ", name: "การประกอบธุรกิจของคนต่างด้าว", sequence: 1 },
    { code: "50003", type: 5, typeDesc: "ประเภทคำขอ", name: "สมาคมและหอการค้า", sequence: 2 }
];
const customSegMock: Lov[] = [
    { code: "20001", type: 2, typeDesc: "Customer Segment", name: "Retail", sequence: 0 },
    { code: "20002", type: 2, typeDesc: "Customer Segment", name: "SE", sequence: 1 },
    { code: "20003", type: 2, typeDesc: "Customer Segment", name: "BB", sequence: 2 },
    { code: "20004", type: 2, typeDesc: "Customer Segment", name: "CB", sequence: 3 },
    { code: "20005", type: 2, typeDesc: "Customer Segment", name: "MB", sequence: 4 },
    { code: "20006", type: 2, typeDesc: "Customer Segment", name: "Legal", sequence: 5 },
    { code: "20007", type: 2, typeDesc: "Customer Segment", name: "ETC", sequence: 6 }
];
const payMethodMock: Lov[] = [
    { code: "30001", type: 3, typeDesc: "วิธีการรับชำระ", name: "ลูกค้าชำระค่าธรรมเนียม DBD,TMB", sequence: 0 },
    { code: "30002", type: 3, typeDesc: "วิธีการรับชำระ", name: "ลูกค้าชำระค่าธรรมเนียม DBD ยกเว้น TMB", sequence: 1 },
    { code: "30003", type: 3, typeDesc: "วิธีการรับชำระ", name: "TMB ชำระค่าธรรมเนียม DBD ทั้งหมด", sequence: 2 }
]
const subAccMethodMock: Lov[] = [
    { code: "40001", type: 4, typeDesc: "วิธีหักบัญชีจาก", name: "ธนาคาร (กลุ่มรายย่อย)", sequence: 0 },
    { code: "40002", type: 4, typeDesc: "วิธีหักบัญชีจาก", name: "ธนาคาร (กลุ่ม SE)", sequence: 1 },
    { code: "40003", type: 4, typeDesc: "วิธีหักบัญชีจาก", name: "ธนาคาร (กลุ่ม CB/MB/BB)", sequence: 2 }
];