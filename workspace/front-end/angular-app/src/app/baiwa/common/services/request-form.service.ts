import { Injectable } from "@angular/core";
import { AjaxService } from "./ajax.service";
import { HttpClient } from "@angular/common/http";
import { Observable } from "rxjs";
import { RequestForm } from "../models";

const URL = {
    GEN_KEY: "/api/nrq/generate/key",
    FORM_PDF: "/api/report/pdf/view/",
    REQUEST_FORM: "/api/nrq/data",
}

@Injectable({
    providedIn: 'root'
})
export class RequestFormService {

    private tmbReqNumber: Observable<string>;
    private requestForm: Observable<RequestForm>;

    constructor(private ajax: AjaxService, private httpClient: HttpClient) { }

    /**
     * @return `tmbReqNo` รหัส TmbRequestNumber ใหม่
     */
    getTmbReqFormNo(): Observable<string> {
        this.tmbReqNumber = this.httpClient.get(AjaxService.CONTEXT_PATH + URL.GEN_KEY, { responseType: 'text' })
        return this.tmbReqNumber;
    }

    /**
     * 
     * @param url ลิ้งค์สำหรับสร้างไฟล์ pdf
     * @param data ข้อมูลที่จะใช้ในการเจนไฟล์
     */
    getPdf(url: string, data: any, error?: Function) {
        this.ajax.post(url, data, response => {
            this.ajax.download(URL.FORM_PDF + response._body + "/download");
        }, err => {
            error(err)
        });
    }

    /**
     * 
     * @param formId รหัสแบบฟอร์มคำขอ
     * @returns `requestForm` ข้อมูลแบบฟอร์มจาก `formId`
     */
    getReqFormByFormId(formId: number): Observable<RequestForm> {
        this.requestForm = this.httpClient.get<RequestForm>(`${AjaxService.CONTEXT_PATH}${URL.REQUEST_FORM}/${formId}`);
        return this.requestForm;
    }

}