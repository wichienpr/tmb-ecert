import { Injectable } from "@angular/core";
import { Lov } from "app/tmb-ecert/models/lov";

@Injectable()
export class Nrq02000Service {

    private reqType: Lov[]; // Request Type
    private customSeg: Lov[]; // Customer Segment
    private payMethod: Lov[]; // Payment Method
    private subAccMethod: Lov[]; // Subtract from Account Method

    constructor() {
        this.reqType = reqTypeMock;
        this.customSeg = customSeg;
    }

}

/**
 * Initial Mock Data
 */
const reqTypeMock = [
    { code: "50001", type: 5, typeDesc: "ประเภทคำขอ", name: "ห้างหุ้นส่วนจำกัด บริษัทจำกัดและบริษัทมหาชนจำกัด", sequence: 0 },
    { code: "50002", type: 5, typeDesc: "ประเภทคำขอ", name: "การประกอบธุรกิจของคนต่างด้าว", sequence: 1 },
    { code: "50003", type: 5, typeDesc: "ประเภทคำขอ", name: "สมาคมและหอการค้า", sequence: 2 }
];
const customSeg = [
    { code: "20001", type: 2, typeDesc: "Customer Segment", name: "Retail", sequence: 0 },
    { code: "20002", type: 2, typeDesc: "Customer Segment", name: "SE", sequence: 1 },
    { code: "20003", type: 2, typeDesc: "Customer Segment", name: "BB", sequence: 2 },
    { code: "20004", type: 2, typeDesc: "Customer Segment", name: "CB", sequence: 3 },
    { code: "20005", type: 2, typeDesc: "Customer Segment", name: "MB", sequence: 4 },
    { code: "20006", type: 2, typeDesc: "Customer Segment", name: "Legal", sequence: 5 },
    { code: "20007", type: 2, typeDesc: "Customer Segment", name: "ETC", sequence: 6 }
];