export interface Certificate {
    [x: string]: any;
    code: string;
    typeCode: string;
    typeDesc: string;
    certificate: string;
    children?: Certificate[];
    feeDbd: string;
    feeTmb: string;
    registeredDate?: Date;
    statementYear?: string;
    acceptedDate?: Date;
    other?: string;
}