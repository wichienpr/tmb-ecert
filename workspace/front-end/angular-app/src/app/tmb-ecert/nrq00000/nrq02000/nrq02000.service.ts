import { Injectable } from "@angular/core";
import { Lov, Certificate } from "app/tmb-ecert/models/index";
import { Observable } from "rxjs";

@Injectable()
export class Nrq02000Service {

    private reqType: Lov[]; // Request Type
    private customSeg: Lov[]; // Customer Segment
    private payMethod: Lov[]; // Payment Method
    private subAccMethod: Lov[]; // Subtract from Account Method

    constructor() {
        this.reqType = reqTypeMock;
        this.customSeg = customSegMock;
        this.payMethod = payMethodMock;
        this.subAccMethod = subAccMethodMock;
    }

    getReqType(): Observable<Lov[]> { // reqType
        return new Observable(obs => {
            obs.next(this.reqType);
        });
    }

    getCustomSeg(): Observable<Lov[]> { // customSeg
        return new Observable(obs => {
            obs.next(this.customSeg);
        });
    }
    
    getpayMethod(): Observable<Lov[]> { // payMethod
        return new Observable(obs => {
            obs.next(this.payMethod);
        });
    }

    getsubAccMethod(): Observable<Lov[]> { // subAccMethod
        return new Observable(obs => {
            obs.next(this.subAccMethod);
        });
    }

    reqTypeChange(e) {
        
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
    { code: "30001", type: 3, typeDesc: "วิธีการรับชำระ", name: "ลูกค้าชำระค่าธรรมเนียม DBD,TMB", sequence: 0},
    { code: "30002", type: 3, typeDesc: "วิธีการรับชำระ", name: "ลูกค้าชำระค่าธรรมเนียม DBD ยกเว้น TMB", sequence: 1},
    { code: "30003", type: 3, typeDesc: "วิธีการรับชำระ", name: "TMB ชำระค่าธรรมเนียม DBD ทั้งหมด", sequence: 2}
]
const subAccMethodMock: Lov[] = [
    { code: "40001", type: 4, typeDesc: "วิธีหักบัญชีจาก", name: "ธนาคาร (กลุ่มรายย่อย)", sequence: 0 },
    { code: "40002", type: 4, typeDesc: "วิธีหักบัญชีจาก", name: "ธนาคาร (กลุ่ม SE)", sequence: 1 },
    { code: "40003", type: 4, typeDesc: "วิธีหักบัญชีจาก", name: "ธนาคาร (กลุ่ม CB/MB/BB)", sequence: 2 }
];
const reqTypeSelectMock = [
    {
        code: "10001",
        typeCode: "50001",
        header: "ห้างหุ้นส่วนจำกัด บริษัทจำกัดและบริษัทมหาชนจำกัด",
        child: [
            { detail: "หนังสือรับรองนิติบุคคล", feeDBD: "ฉบับละ 200 บาท", bankService: "ฉบับละ 150 บาท", select: false, value: "" },
            { detail: "รับรองสำเนาเอกสารทะเบียน", feeDBD: "ฉบับละ 50 บาท", bankService: "1-5 หน้าแรก 100 บาทหน้าถัดไปหน้าละ 20 บาท", select: false, value: "" },
            { detail: "รับรองสำเนางบการเงิน", feeDBD: "ฉบับละ 50 บาท", bankService: "1-5 หน้าแรก 100 บาทหน้าถัดไปหน้าละ 20 บาท", select: false, value: "" },
            { detail: "รับรองสำเนาบัญชีรายชื่อผู้ถือหุ้น", feeDBD: "ฉบับละ 50 บาท", bankService: "1-5 หน้าแรก 100 บาทหน้าถัดไปหน้าละ 20 บาท", select: false, value: "" },   
        ]
    }
];