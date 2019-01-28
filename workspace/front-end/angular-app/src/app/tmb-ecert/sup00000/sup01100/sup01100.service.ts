import { Injectable } from "@angular/core";
import { Observable } from "rxjs";
import { FormGroup, FormControl, Validators } from "@angular/forms";
import { AjaxService } from "app/baiwa/common/services";
import { PAGE_AUTH } from "app/baiwa/common/constants";
import { HttpClient, HttpHeaders } from "@angular/common/http";

const URL = {
    SUP_API_SAVE: "/api/setup/sup01000/saveUserRole",
    SUP_API_GET:"/api/setup/sup01000/getRolePermission/"
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
                functionCode: PAGE_AUTH.P0000200,
                roleName:"UI-00002 - ยินดีต้อนรับ"
            },
            {
                status: 0,
                functionCode: PAGE_AUTH.P0000300,
                roleName: "UI-00003 - ตรวจสอบสถานะคำขอ"
            },
            {
                status: 0,
                functionCode: PAGE_AUTH.P0000301,
                roleName: "แสดงรายละเอียด"
            },
            {
                status: 0,
                functionCode: PAGE_AUTH.P0000400,
                roleName: "UI-00004 - รายละเอียดบันทึกคำขอและพิมพ์แบบฟอร์มให้ลูกค้าลงนาม"
            },
            {
                status: 0,
                functionCode: PAGE_AUTH.P0000401,
                roleName: "ดำเนินการชำระเงิน"
            },
            {
                status: 0,
                functionCode: PAGE_AUTH.P0000402,
                roleName: "อนุมัติการชำระเงิน"
            },
            {
                status: 0,
                functionCode: PAGE_AUTH.P0000403,
                roleName: "ปฏิเสธ"
            },
            {
                status: 0,
                functionCode: PAGE_AUTH.P0000404,
                roleName: "พิมพ์ใบเสร็จ"
            },
            {
                status: 0,
                functionCode: PAGE_AUTH.P0000405,
                roleName: "พิมพ์ใบนำส่งเอกสาร"
            },
            {
                status: 0,
                functionCode: PAGE_AUTH.P0000406,
                roleName: "Upload Certificate"
            },
            {
                status: 0,
                functionCode: PAGE_AUTH.P0000407,
                roleName: "ดาวน์โหลดใบคำขอหนังสือรับรองนิติบุคคลและหนังสือยินยอมให้หักเงินจากบัญชีเงินฝาก"
            },
            {
                status: 0,
                functionCode: PAGE_AUTH.P0000408,
                roleName: "ดาวน์โหลดสำเนาบัตรประชาชน"
            },
            {
                status: 0,
                functionCode: PAGE_AUTH.P0000409,
                roleName: "ดาวน์โหลดสำเนาใบเปลี่ยนชื่อหรือนามสกุล"
            },
            {
                status: 0,
                functionCode: PAGE_AUTH.P0000410,
                roleName: "ดาวน์โหลดเอกสาร Certificate"
            },
            {
                status: 0,
                functionCode: PAGE_AUTH.P0000500,
                roleName: "UI-00005 - Request Form (พิมพ์ใบคำขอเปล่าให้ลูกค้าลงนาม และบันทึกข้อมูลภายหลัง)"
            },
            {
                status: 0,
                functionCode: PAGE_AUTH.P0000600,
                roleName: "UI-00006 - Request Form (บันทึกคำขอก่อน และพิมพ์ใบคำขอให้ลูกค้าลงนาม)"
            },
            {
                status: 0,
                functionCode: PAGE_AUTH.P0000700,
                roleName: "UI-00007 - รายงานสรุปการให้บริการ ขอหนังสือรับรองนิติบุคคล ( e-Certificate ) : End day"
            },
            {
                status: 0,
                functionCode: PAGE_AUTH.P0000701,
                roleName: "Export to Excel"
            },
            {
                status: 0,
                functionCode: PAGE_AUTH.P0000800,
                roleName: "UI-00008 - รายงานสรุปการให้บริการ ขอหนังสือรับรองนิติบุคคล ( e-Certificate ) : Monthly"
            },
            {
                status: 0,
                functionCode: PAGE_AUTH.P0000801,
                roleName: "Export to Excel"
            },
            {
                status: 0,
                functionCode: PAGE_AUTH.P0000900,
                roleName: "UI-00009 - รายงาน Output VAT"
            },
            {
                status: 0,
                functionCode: PAGE_AUTH.P0000901,
                roleName: "Export to Excel"
            },
            {
                status: 0,
                functionCode: PAGE_AUTH.P0001100,
                roleName: "UI-00011 - Batch Monitoring"
            },
            {
                status: 0,
                functionCode: PAGE_AUTH.P0001101,
                roleName: "Rerun"
            },
            {
                status: 0,
                functionCode: PAGE_AUTH.P0001200,
                roleName: "UI-00012 - Audit Log"
            },
            {
                status: 0,
                functionCode: PAGE_AUTH.P0001201,
                roleName: "Export to Excel"
            },
            {
                status: 0,
                functionCode: PAGE_AUTH.P0001300,
                roleName: "UI-00013 - Role Management"
            },
            {
                status: 0,
                functionCode: PAGE_AUTH.P0001301,
                roleName: "เพิ่ม Role"
            },
            {
                status: 0,
                functionCode: PAGE_AUTH.P0001302,
                roleName: "แก้ไข Role"
            },
            {
                status: 0,
                functionCode: PAGE_AUTH.P0001303,
                roleName: "Import Role"
            },
            {
                status: 0,
                functionCode: PAGE_AUTH.P0001304,
                roleName: "Export Role"
            },
            {
                status: 0,
                functionCode: PAGE_AUTH.P0001400,
                roleName: "UI-00014 - Parameter Configuration"
            },
            {
                status: 0,
                functionCode: PAGE_AUTH.P0001401,
                roleName: "แก้ไข"
            },
            {
                status: 0,
                functionCode: PAGE_AUTH.P0001500,
                roleName: "UI-00015 - Email Configuration"
            },
            {
                status: 0,
                functionCode: PAGE_AUTH.P0001501,
                roleName: "แก้ไข"
            },
            {
                status: 0,
                functionCode: PAGE_AUTH.P0001600,
                roleName: "UI-00016 - บันทึกข้อมูลจากเลขที่คำขอ (TMB Req No.)"
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
            // console.log("key ", element.functionCode, " statsu", element.status);
        });

        return this.callSaveAPI(form, roleId)
    }

    callSaveAPI(form, roleId) {
        const path = URL.SUP_API_SAVE;
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
        const path = URL.SUP_API_GET + roleId;
        return this.ajax.Get(path);

    }



}