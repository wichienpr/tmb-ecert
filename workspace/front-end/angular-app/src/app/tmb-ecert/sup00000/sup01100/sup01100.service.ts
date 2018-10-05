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

@Injectable({
    providedIn: 'root'
})
export class sup01100Service {
    userRolePermission: any;
    dataT: any;

    constructor(private ajax: AjaxService, private httpClient: HttpClient) {
        this.userRolePermission = [
            {
                status: 0,
                functionCode: PAGE_AUTH.P0000200
            },
            {
                status: 0,
                functionCode: PAGE_AUTH.P0000300
            },
            {
                status: 0,
                functionCode: PAGE_AUTH.P0000301
            },
            {
                status: 0,
                functionCode: PAGE_AUTH.P0000400
            },
            {
                status: 0,
                functionCode: PAGE_AUTH.P0000401
            },
            {
                status: 0,
                functionCode: PAGE_AUTH.P0000402
            },
            {
                status: 0,
                functionCode: PAGE_AUTH.P0000403
            },
            {
                status: 0,
                functionCode: PAGE_AUTH.P0000404
            },
            {
                status: 0,
                functionCode: PAGE_AUTH.P0000405
            },
            {
                status: 0,
                functionCode: PAGE_AUTH.P0000406
            },
            {
                status: 0,
                functionCode: PAGE_AUTH.P0000407
            },
            {
                status: 0,
                functionCode: PAGE_AUTH.P0000408
            },
            {
                status: 0,
                functionCode: PAGE_AUTH.P0000409
            },
            {
                status: 0,
                functionCode: PAGE_AUTH.P0000410
            },
            {
                status: 0,
                functionCode: PAGE_AUTH.P0000500
            },
            {
                status: 0,
                functionCode: PAGE_AUTH.P0000600
            },
            {
                status: 0,
                functionCode: PAGE_AUTH.P0000700
            },
            {
                status: 0,
                functionCode: PAGE_AUTH.P0000701
            },
            {
                status: 0,
                functionCode: PAGE_AUTH.P0000800
            },
            {
                status: 0,
                functionCode: PAGE_AUTH.P0000801
            },
            {
                status: 0,
                functionCode: PAGE_AUTH.P0000900
            },
            {
                status: 0,
                functionCode: PAGE_AUTH.P0000901
            },
            {
                status: 0,
                functionCode: PAGE_AUTH.P0001100
            },
            {
                status: 0,
                functionCode: PAGE_AUTH.P0001101
            },
            {
                status: 0,
                functionCode: PAGE_AUTH.P0001200
            },
            {
                status: 0,
                functionCode: PAGE_AUTH.P0001201
            },
            {
                status: 0,
                functionCode: PAGE_AUTH.P0001300
            },
            {
                status: 0,
                functionCode: PAGE_AUTH.P0001301
            },
            {
                status: 0,
                functionCode: PAGE_AUTH.P0001302
            },
            {
                status: 0,
                functionCode: PAGE_AUTH.P0001303
            },
            {
                status: 0,
                functionCode: PAGE_AUTH.P0001304
            },
            {
                status: 0,
                functionCode: PAGE_AUTH.P0001400
            },
            {
                status: 0,
                functionCode: PAGE_AUTH.P0001401
            },
            {
                status: 0,
                functionCode: PAGE_AUTH.P0001500
            },
            {
                status: 0,
                functionCode: PAGE_AUTH.P0001501
            }]
    }

    saveNewRole(form, rolepermisson, roleId) {
        let index = 0;
        for (const iterator of rolepermisson) {
            this.userRolePermission[index].status = iterator.status;
            index++;
            for (const temp of iterator.chliddata) {
                this.userRolePermission[index].status = temp.status;
                index++;
            }
        }

        this.userRolePermission.forEach(element => {
            console.log("key ", element.functionCode, " statsu", element.status);
        });

        return this.callSaveAPI(form, roleId)
    }

    callSaveAPI(form, roleId) {
        const path = "/api/setup/saveUserRole";
        const httpOptions = {
            headers: new HttpHeaders({ 'Content-Type': 'application/json' })
        };
        return this.httpClient.post(AjaxService.CONTEXT_PATH + path, {
            roleId: roleId,
            roleName: form.value.roleName,
            status: form.value.status,
            rolePermission: this.userRolePermission
        }, httpOptions);


    }

    serviceSearchPermissionByRole(roleId) {
        const path = "/api/setup/getRolePermission/" + roleId;
        return this.ajax.Get(path)

    }



}