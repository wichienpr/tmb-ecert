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
    statementYear?: number;
    acceptedDate?: Date;
    other?: string;
}