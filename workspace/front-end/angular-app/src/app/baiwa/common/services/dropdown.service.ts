import { Injectable } from "@angular/core";
import { Lov } from "models/";
import { Observable } from "rxjs";
import { AjaxService } from "./ajax.service";

const URL = {
    LOV_BY_TYPE: "/api/lov/type"
}

@Injectable()
export class DropdownService { // TABLE => ECERT_LISTOFVALUE
    private reqType: Lov[]; // ประเภทคำขอ
    private customSeg: Lov[]; // Customer Segment
    private payMethod: Lov[]; // วิธีชำระ
    private subAccMethod: Lov[]; // วีธีหักจากธนาคาร
    private action: Lov[]; // Action
    private operationTypeLOV: Lov[]; // สถานะการทำงาน
    private batchJobSchLOV: Lov[]; // BatchJobSchedu
    private jobMonitoringStus: Lov[];  //Monitoring Status
    
    constructor(private ajax: AjaxService) {
        /**
         * ถ้าไม่มีข้อมูลจะใช้ข้อมูล Mock
         */
        // this.reqType = reqTypeMock;
        // this.customSeg = customSegMock;
        // this.payMethod = payMethodMock;
        // this.subAccMethod = subAccMethodMock;
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

    getaction(): Observable<Lov[]> { // Action
        return new Observable(obs => {
            this.ajax.post(URL.LOV_BY_TYPE, { type: 7 }, result => {
                const data = result.json();
                if (data && data.length > 0) {
                    this.action = data;
                }
                obs.next([...this.action]);
            });
        });
    }

    getStatusType(): Observable<Lov[]> { // สถานะการทำงาน
        return new Observable(obs => {
            this.ajax.post(URL.LOV_BY_TYPE, { type: 9 }, result => {
                const data = result.json();
                if (data && data.length > 0) {
                    this.operationTypeLOV = data;
                }
                obs.next([...this.operationTypeLOV]);
            });
        });
    }

    getBatchJobSchedu(): Observable<Lov[]> { // Batch Job Scheduler
        return new Observable(obs => {
            this.ajax.post(URL.LOV_BY_TYPE, { type: 6 }, result => {
                const data = result.json();
                if (data && data.length > 0) {
                    this.batchJobSchLOV = data;
                }
                obs.next([...this.batchJobSchLOV]);
            });
        });
    }

    getMonitoringStatus(): Observable<Lov[]> { // Monitoring Status
        return new Observable(obs => {
            this.ajax.post(URL.LOV_BY_TYPE, { type: 8 }, result => {
                const data = result.json();
                if (data && data.length > 0) {
                    this.jobMonitoringStus = data;
                }
                obs.next([...this.jobMonitoringStus]);
            });
        });
    }

    getRejectReason(): Observable<Lov[]> {
        return new Observable<Lov[]>( obs => {
            const lov: Lov[] = [
                { code: "1", name: "ลูกค้ามียอดเงินในบัญชีไม่พอจ่าย", type: 0, typeDesc: "", sequence: 0, glType: null, tranCode: null, accountType: null, status: null, accountNo: null },
                { code: "2", name: "ลูกค้าแนบเอกสารไม่ครบถ้วน", type: 0, typeDesc: "", sequence: 1, glType: null, tranCode: null, accountType: null, status: null, accountNo: null },
                { code: "3", name: "ลูกค้าขอยกเลิกคำขอ", type: 0, typeDesc: "", sequence: 2, glType: null, tranCode: null, accountType: null, status: null, accountNo: null },
                { code: "4", name: "เจ้าหน้าที่ธนาคารขอยกเลิกคำขอ", type: 0, typeDesc: "", sequence: 3, glType: null, tranCode: null, accountType: null, status: null, accountNo: null },
                { code: "5", name: "อื่นๆ", type: 0, typeDesc: "", sequence: 4, glType: null, tranCode: null, accountType: null, status: null, accountNo: null }
            ];
            obs.next(lov);
            obs.complete();
        });
    }

}



// /**
//  * Initial Mock Data
//  */
// const reqTypeMock: Lov[] = [
//     { code: "50001", type: 5, typeDesc: "ประเภทคำขอ", name: "ห้างหุ้นส่วนจำกัด บริษัทจำกัดและบริษัทมหาชนจำกัด", sequence: 0, glType: null, trncode: null, acctype: null, status: null, acctno: null },
//     { code: "50002", type: 5, typeDesc: "ประเภทคำขอ", name: "การประกอบธุรกิจของคนต่างด้าว", sequence: 1, glType: null, trncode: null, acctype: null, status: null, acctno: null },
//     { code: "50003", type: 5, typeDesc: "ประเภทคำขอ", name: "สมาคมและหอการค้า", sequence: 2, glType: null, trncode: null, acctype: null, status: null, acctno: null }
// ];
// const customSegMock: Lov[] = [
//     { code: "20001", type: 2, typeDesc: "Customer Segment", name: "Retail", sequence: 0, glType: null, trncode: null, acctype: null, status: null, acctno: null },
//     { code: "20002", type: 2, typeDesc: "Customer Segment", name: "SE", sequence: 1, glType: null, trncode: null, acctype: null, status: null, acctno: null },
//     { code: "20003", type: 2, typeDesc: "Customer Segment", name: "BB", sequence: 2, glType: null, trncode: null, acctype: null, status: null, acctno: null },
//     { code: "20004", type: 2, typeDesc: "Customer Segment", name: "CB", sequence: 3, glType: null, trncode: null, acctype: null, status: null, acctno: null },
//     { code: "20005", type: 2, typeDesc: "Customer Segment", name: "MB", sequence: 4, glType: null, trncode: null, acctype: null, status: null, acctno: null },
//     { code: "20006", type: 2, typeDesc: "Customer Segment", name: "Legal", sequence: 5, glType: null, trncode: null, acctype: null, status: null, acctno: null },
//     { code: "20007", type: 2, typeDesc: "Customer Segment", name: "ETC", sequence: 6, glType: null, trncode: null, acctype: null, status: null, acctno: null }
// ];
// const payMethodMock: Lov[] = [
//     { code: "30001", type: 3, typeDesc: "วิธีการรับชำระ", name: "ลูกค้าชำระค่าธรรมเนียม DBD,TMB", sequence: 0, glType: null, trncode: null, acctype: null, status: null, acctno: null },
//     { code: "30002", type: 3, typeDesc: "วิธีการรับชำระ", name: "ลูกค้าชำระค่าธรรมเนียม DBD ยกเว้น TMB", sequence: 1, glType: null, trncode: null, acctype: null, status: null, acctno: null },
//     { code: "30003", type: 3, typeDesc: "วิธีการรับชำระ", name: "TMB ชำระค่าธรรมเนียม DBD ทั้งหมด", sequence: 2, glType: null, trncode: null, acctype: null, status: null, acctno: null }
// ]
// const subAccMethodMock: Lov[] = [
//     { code: "40001", type: 4, typeDesc: "วิธีหักบัญชีจาก", name: "ธนาคาร (กลุ่มรายย่อย)", sequence: 0, glType: null, trncode: null, acctype: null, status: null, acctno: null },
//     { code: "40002", type: 4, typeDesc: "วิธีหักบัญชีจาก", name: "ธนาคาร (กลุ่ม SE)", sequence: 1, glType: null, trncode: null, acctype: null, status: null, acctno: null },
//     { code: "40003", type: 4, typeDesc: "วิธีหักบัญชีจาก", name: "ธนาคาร (กลุ่ม CB/MB/BB)", sequence: 2, glType: null, trncode: null, acctype: null, status: null, acctno: null }
// ];