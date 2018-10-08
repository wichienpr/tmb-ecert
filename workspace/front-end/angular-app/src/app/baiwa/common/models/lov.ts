export interface Lov {
    code: string; // char(5)
    type: number; // tinyint
    typeDesc: string; // nvarchar
    name: string; // nvarchar
    sequence: number; // smallint
    glType: string;
    trncode: string;
    acctype: string;
    status: number;
    acctno: string;
}

export const listOfValue: Lov[] = [ // List Of Value
    { code: null, type: 1, typeDesc: "สถานะคำขอ", name: null, sequence: null, glType: null, trncode: null, acctype: null, status: null, acctno: null },
    { code: null, type: 2, typeDesc: "Customer Segment", name: null, sequence: null, glType: null, trncode: null, acctype: null, status: null, acctno: null },
    { code: null, type: 3, typeDesc: "วิธีการรับชำระ", name: null, sequence: null, glType: null, trncode: null, acctype: null, status: null, acctno: null },
    { code: null, type: 4, typeDesc: "วิธีหักบัญชีจาก", name: null, sequence: null, glType: null, trncode: null, acctype: null, status: null, acctno: null },
    { code: null, type: 5, typeDesc: "ประเภทคำขอ", name: null, sequence: null, glType: null, trncode: null, acctype: null, status: null, acctno: null },
    { code: null, type: 6, typeDesc: "Batch Job Scheduler", name: null, sequence: null, glType: null, trncode: null, acctype: null, status: null, acctno: null },
    { code: null, type: 7, typeDesc: "Action", name: null, sequence: null, glType: null, trncode: null, acctype: null, status: null, acctno: null },
]