export interface CertFile {
    id: number;
    status: string;
    certificates: string;
    certificatesFile: FormData;
    ignoreReceipt: string;
}

export interface Rejected {
    reqFormId: number;
	rejectReasonCode: string;
    rejectReasonOther: string;
    for: string;
}

export interface ResponseVo {
    message: string;
    data: ResponseData;
}

export interface ResponseData {
    message: string;
    status: string;
}