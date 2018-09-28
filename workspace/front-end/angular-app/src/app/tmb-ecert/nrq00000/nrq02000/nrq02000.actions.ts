import { Action } from "@ngrx/store";
import { Certificate } from "tmb-ecert/models/";

// Certitcate Actions
export const CER_CRE = "[CERTIFICATE] => CREATE";
export const CER_UDT = "[CERTIFICATE] => UPDATE";
// Certificate TODO
export class CertificateCreate implements Action {
    readonly type = CER_CRE
    constructor(public payload: Certificate[]) { }
}
export class CertificateUpdate implements Action {
    readonly type = CER_UDT
    constructor(public payload: Certificate[]) { }
}

// Export Actions
export type CertificateActions = CertificateCreate | CertificateUpdate;