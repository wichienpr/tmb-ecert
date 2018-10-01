import { Certificate } from "tmb-ecert/models";

export interface Nrq02000 {
    reqTypeSelect: string;        // ประเภทคำขอ
    customSegSelect: string;      // Customer Segment
    payMethodSelect: string;      // วิธีการรับชำระ
    subAccMethodSelect: string;   // วิธีหักบัญชีจาก
    accNo: string;                // เลขที่บัญชี
    accName: string;              // ชื่อบัญชี
    corpNo: string;               // เลขที่นิติบุคคล
    corpName: string;             // ชื่อนิติบุคคล
    corpName1: string;            // ชื่อนิติบุคคล 1
    acceptNo: string;             // เลขที่ CA/มติอนุมัติ
    departmentName: string;       // ชื่อหน่วยงาน
    tmbReceiptChk: boolean;       // ชื่อบนใบเสร็จธนาคาร TMB
    telReq: string;               // เบอร์โทรผู้ขอ/ลูกค้า
    address: string;              // ที่อยู่
    note: string;                 // หมายเหตุ
    requestFile: any;             // ใบคำขอหนังสือรับรองนิติบุคคลและหนังสือยินยอมให้หักเงินจากบัญชีเงินฝาก
    copyFile: any;                // สำเนาบัตรประชาชน
    changeNameFile: any;          // สำเนาใบเปลี่ยนชื่อหรือนามสกุล
}