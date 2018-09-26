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
        let lists = reqTypeSelectMock.filter(obj => obj.typeCode == e);
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
        return lists;
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
const reqTypeSelectMock: Certificate[] = [
    { code: "10001", typeCode: "50001", typeDesc: "ห้างหุ้นส่วนจำกัด บริษัทจำกัดและบริษัทมหาชนจำกัด", certificate: "หนังสือรับรองนิติบุคคล", feeDbd: "ฉบับละ 200 บาท", feeTmb: "ฉบับละ 150 บาท" },
    { code: "10002", typeCode: "50001", typeDesc: "ห้างหุ้นส่วนจำกัด บริษัทจำกัดและบริษัทมหาชนจำกัด", certificate: "รับรองสำเนาเอกสารทะเบียน", feeDbd: "ฉบับละ 50 บาท", feeTmb: "1-5 หน้าแรก 100 บาทหน้าถัดไปหน้าละ 20 บาท" },
    { code: "10003", typeCode: "50001", typeDesc: "ห้างหุ้นส่วนจำกัด บริษัทจำกัดและบริษัทมหาชนจำกัด", certificate: "รับรองสำเนางบการเงิน", feeDbd: "ฉบับละ 50 บาท", feeTmb: "1-5 หน้าแรก 100 บาทหน้าถัดไปหน้าละ 20 บาท" },
    { code: "10004", typeCode: "50001", typeDesc: "ห้างหุ้นส่วนจำกัด บริษัทจำกัดและบริษัทมหาชนจำกัด", certificate: "รับรองสำเนาบัญชีรายชื่อผู้ถือหุ้น", feeDbd: "ฉบับละ 50 บาท", feeTmb: "1-5 หน้าแรก 100 บาทหน้าถัดไปหน้าละ 20 บาท" },
    { code: "20001", typeCode: "50002", typeDesc: "การประกอบธุรกิจของคนต่างด้าว", certificate: "หนังสือรับรองข้อความที่กรมทะเบียนเก็บรักษาไว้ของการประกอบธุรกิจของคนต่างด้าว", feeDbd: "ฉบับละ 300-500 บาท", feeTmb: "ฉบับละ 150 บาท" },
    { code: "20002", typeCode: "50002", typeDesc: "การประกอบธุรกิจของคนต่างด้าว", certificate: "รับรองสำเนาเอกสารทะเบียน", feeDbd: "หน้าละ 100 บาท", feeTmb: "1-5 หน้าแรก 100 บาทหน้าถัดไปหน้าละ 20 บาท" },
    { code: "20003", typeCode: "50002", typeDesc: "การประกอบธุรกิจของคนต่างด้าว", certificate: "รับรองสำเนางบการเงิน", feeDbd: "หน้าละ 50 บาท", feeTmb: "1-5 หน้าแรก 100 บาทหน้าถัดไปหน้าละ 20 บาท" },
    { code: "30001", typeCode: "50003", typeDesc: "สมาคมและหอการค้า", certificate: "รับรองสำเนาเอกสารทะเบียน", feeDbd: "เรื่องละ 20 บาท", feeTmb: "1-5 หน้าแรก 100 บาทหน้าถัดไปหน้าละ 20 บาท" },
    { code: "30002", typeCode: "50003", typeDesc: "สมาคมและหอการค้า", certificate: "รับรองสำเนางบการเงิน", feeDbd: "เรื่องละ 20 บาท", feeTmb: "1-5 หน้าแรก 100 บาทหน้าถัดไปหน้าละ 20 บาท" }
];