import { Injectable } from "@angular/core";
import { Observable } from "rxjs";
import { FormGroup, FormControl, Validators } from "@angular/forms";
import { AjaxService, ModalService, DropdownService } from "app/baiwa/common/services";
import { PAGE_AUTH } from "app/baiwa/common/constants";
import { HttpClient, HttpHeaders } from "@angular/common/http";
import { catchError, retry } from 'rxjs/operators';

const URL = {
    SUP_API_SAVE: "setup/api/saveUserRole",
    CER_BY_TYPE: "cer/typeCode",
    EXPORT_TEMPLATE :"/api/setup/sup01000/exportTemplate",
    EXPORT_EXCELL :"/api/setup/sup01000/exportRole"
}

@Injectable()
export class Sup01000Service {
    userRolePermission: any;
    dataT: any[] = [];
    responseObj :any;
    log:any;


    constructor(private ajax: AjaxService, private httpClient: HttpClient, 
        private modal: ModalService,private dropdown : DropdownService) {
        
    }

    callSearchAPI(form) {
        const path = "/api/setup/sup01000/getRole";
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

    callExportAPI(roleName,roleStatus) {
        if(roleName == "" || roleName == undefined){
            roleName = "NULL"
        }
        if(roleStatus == "" || roleStatus == undefined){
            roleStatus = "NULL"
        }
        let path = URL.EXPORT_EXCELL+"/"+roleName+"/"+roleStatus;
        return this.ajax.download(path);
    }
    getStatusType(){
        return this.dropdown.getStatusType();
    }

    callExportTemplateAPI() {
    
        return this.ajax.download(URL.EXPORT_TEMPLATE);
    }

    
    callUploadAPI(fileUpload:any) {
        let formData = new FormData();
        // formData.append('listRole', null);
        formData.append('fileUpload', fileUpload);
        // console.log("file upload ", fileUpload);
        const path = "/api/setup/sup01000/uploadFile";
        const httpOptions = {
            headers: new HttpHeaders({  'Content-Type': 'application/json' })
        };
        return this.httpClient.post(AjaxService.CONTEXT_PATH + path,formData);

    }


}