export interface CertFile {
    id: number;
    status: string;
    certificates: string;
    certificatesFile: FormData;
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