export interface Nrq02000 {
    [x: string]: any;
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
    // tmbReceiptChk: boolean;       // ชื่อบนใบเสร็จธนาคาร TMB
    telReq: string;               // เบอร์โทรผู้ขอ/ลูกค้า
    address: string;              // ที่อยู่
    note: string;                 // หมายเหตุ
    requestFile: File;            // ใบคำขอหนังสือรับรองนิติบุคคลและหนังสือยินยอมให้หักเงินจากบัญชีเงินฝาก
    copyFile: File;               // สำเนาบัตรประชาชน
    changeNameFile?: File;        // สำเนาใบเปลี่ยนชื่อหรือนามสกุล
    tmbReqFormNo: string;         // TMB Req No.
    lockFlag: number;
    requestFileName?: string;
    copyFileName?: string;
    changeNameFileName?: string;
}

export interface ResponseVo {
    message: string;
    data: string;
}