import { Injectable } from "@angular/core";
import { Observable } from "rxjs";
import { FormGroup, FormControl, Validators } from "@angular/forms";
import { AjaxService } from "app/baiwa/common/services";
import { PAGE_AUTH } from "app/baiwa/common/constants";
import { HttpClient, HttpHeaders } from "@angular/common/http";

const URL = {
    SUP_API_SAVE: "setup/api/saveUserRole",
    CER_BY_TYPE: "cer/typeCode"
}

@Injectable()
export class Sup01000Service {
    userRolePermission: any;
    dataT: any[] = [];

    constructor(private ajax: AjaxService, private httpClient: HttpClient) {
    }

    callSaveAPI(form) {
        const path = "/api/setup/getRole";
        const httpOptions = {
            headers: new HttpHeaders({ 'Content-Type': 'application/json' })
        };
        return this.httpClient.post(AjaxService.CONTEXT_PATH + path, {
            roleName: form.value.roleName,
            status: form.value.status,
            rolePermission: [{
                status: 0,
                functionCode: ""
            }]
        }, httpOptions);

    }

    callExportAPI(form) {
        const path = "/api/setup/exportRole";
        const httpOptions = {
            headers: new HttpHeaders({ 'Content-Type': 'application/json' })
        };
        return this.httpClient.post(AjaxService.CONTEXT_PATH + path, {
            roleName: form.value.roleName,
            status: form.value.status,
            rolePermission: [{
                status: 0,
                functionCode: ""
            }]
        }, httpOptions);
    }

}