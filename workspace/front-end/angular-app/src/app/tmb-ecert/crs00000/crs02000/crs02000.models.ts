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
}