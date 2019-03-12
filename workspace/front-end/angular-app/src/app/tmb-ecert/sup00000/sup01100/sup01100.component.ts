import { Component, OnInit, ElementRef, ViewChild } from '@angular/core';
import { PAGE_AUTH, MESSAGE_STATUS } from 'app/baiwa/common/constants';
import { FormGroup, FormControl, Validators, FormBuilder, FormArray } from '@angular/forms';
import { sup01100Service } from 'app/tmb-ecert/sup00000/sup01100/sup01100.service';
import { Router, ActivatedRoute } from '@angular/router';
import { ModalService } from 'app/baiwa/common/services';
import { Modal } from 'app/baiwa/common/models';
import { Sup01000 } from 'app/tmb-ecert/sup00000/sup01000/sup01000.model';
import { Store } from '@ngrx/store';
import { Observable } from 'rxjs';


@Component({
  selector: 'app-sup01100',
  templateUrl: './sup01100.component.html',
  styleUrls: ['./sup01100.component.css'],
})


export class sup01100Component implements OnInit {
  @ViewChild('roleNameView') roleNameEliment: ElementRef;
  rolepermisson: any
  userRolePermission: any
  submited: boolean;
  form: FormGroup;
  responseObj: any;
  storeRole: Observable<Sup01000>
  sup01000: Sup01000;
  listRoleForm:FormGroup;
  roleStatus:boolean;

  constructor(private store: Store<AppState>,private router: Router, private route: ActivatedRoute,
     private service: sup01100Service, private modal: ModalService, private fb: FormBuilder ) {
    this.rolepermisson = [
      {
        rolename: "UI-00002 - ยินดีต้อนรับ",
        status: 0,
        fuctioncode: PAGE_AUTH.P0000200,
        chliddata: []
      },
      {
        rolename: "UI-00003 - ตรวจสอบสถานะคำขอ",
        status: 0,
        fuctioncode: PAGE_AUTH.P0000300,
        chliddata: [
          {
            rolename: "แสดงรายละเอียด",
            status: 0,
            fuctioncode: PAGE_AUTH.P0000301,
          }
        ]
      },
      {
        rolename: "UI-00004 - รายละเอียดบันทึกคำขอและพิมพ์แบบฟอร์มให้ลูกค้าลงนาม",
        status: 0,
        fuctioncode: PAGE_AUTH.P0000400,
        chliddata: [{
          rolename: "ดำเนินการชำระเงิน",
          status: 0,
          fuctioncode: PAGE_AUTH.P0000401,
        },
        {
          rolename: "อนุมัติการชำระเงิน",
          status: 0,
          fuctioncode: PAGE_AUTH.P0000402,

        }
          ,
        {
          rolename: "ปฏิเสธ",
          status: 0,
          fuctioncode: PAGE_AUTH.P0000403,

        }
          ,
        {
          rolename: "พิมพ์ใบเสร็จ",
          status: 0,
          fuctioncode: PAGE_AUTH.P0000404,

        }
          ,
        {
          rolename: "พิมพ์ใบนำส่งเอกสาร",
          status: 0,
          fuctioncode: PAGE_AUTH.P0000405,

        }
          ,
        {
          rolename: "Upload Certificate",
          status: 0,
          fuctioncode: PAGE_AUTH.P0000406,

        }
          ,
        {
          rolename: "ดาวน์โหลดใบคำขอหนังสือรับรองนิติบุคคลและหนังสือยินยอมให้หักเงินจากบัญชีเงินฝาก	",
          status: 0,
          fuctioncode: PAGE_AUTH.P0000407,

        }
          ,
        {
          rolename: "ดาวน์โหลดสำเนาบัตรประชาชน",
          status: 0,
          fuctioncode: PAGE_AUTH.P0000408,

        },
        {
          rolename: "ดาวน์โหลดสำเนาใบเปลี่ยนชื่อหรือนามสกุล",
          status: 0,
          fuctioncode: PAGE_AUTH.P0000409,

        }
          ,
        {
          rolename: "ดาวน์โหลดเอกสาร Certificate",
          status: 0,
          fuctioncode: PAGE_AUTH.P0000410,

        }]
      },
      {
        rolename: "UI-00005 - Request Form (พิมพ์ใบคำขอเปล่าให้ลูกค้าลงนาม และบันทึกข้อมูลภายหลัง)",
        status: 0,
        fuctioncode: PAGE_AUTH.P0000500,
        chliddata: []
      }
      ,
      {
        rolename: "UI-00006 - Request Form (บันทึกคำขอก่อน และพิมพ์ใบคำขอให้ลูกค้าลงนาม)",
        status: 0,
        fuctioncode: PAGE_AUTH.P0000600,
        chliddata: []

      },
      {
        rolename: "UI-00007 - รายงานสรุปการให้บริการ ขอหนังสือรับรองนิติบุคคล ( e-Certificate ) : End day",
        status: 0,
        fuctioncode: PAGE_AUTH.P0000700,

        chliddata: [{
          rolename: "Export to Excel",
          status: 0,
          fuctioncode: PAGE_AUTH.P0000701
        }]
      }
      ,
      {
        rolename: "UI-00008 - รายงานสรุปการให้บริการ ขอหนังสือรับรองนิติบุคคล ( e-Certificate ) : Monthly",
        status: 0,
        fuctioncode: PAGE_AUTH.P0000800,
        chliddata: [{
          rolename: "Export to Excel",
          status: 0,
          fuctioncode: PAGE_AUTH.P0000801
        }]
      }
      ,
      {
        rolename: "UI-00009 - รายงาน Output VAT",
        status: 0,
        fuctioncode: PAGE_AUTH.P0000900,

        chliddata: [{
          rolename: "Export to Excel",
          status: 0,
          fuctioncode: PAGE_AUTH.P0000901
        }]
      }
      ,
      {
        rolename: "UI-00011 - Batch Monitoring",
        status: 0,
        fuctioncode: PAGE_AUTH.P0001100,
        chliddata: [{
          rolename: "Rerun",
          status: 0,
          fuctioncode: PAGE_AUTH.P0001101,

        }]
      }
      ,
      {
        rolename: "UI-00012 - Audit Log",
        status: 0,
        fuctioncode: PAGE_AUTH.P0001200,
        chliddata: [{
          rolename: "Export to Excel",
          status: 0,
          fuctioncode: PAGE_AUTH.P0001201
        }]
      }
      ,
      {
        rolename: "UI-00013 - Role Management",
        status: 0,
        fuctioncode: PAGE_AUTH.P0001300,
        chliddata: [{
          rolename: "เพิ่ม Role",
          status: 0,
          fuctioncode: PAGE_AUTH.P0001301
        }
          ,
        {
          rolename: "แก้ไข Role",
          status: 0,
          fuctioncode: PAGE_AUTH.P0001302
        }
          ,
        {
          rolename: "Import Role",
          status: 0,
          fuctioncode: PAGE_AUTH.P0001303
        }
          ,
        {
          rolename: "Export Role",
          status: 0,
          fuctioncode: PAGE_AUTH.P0001304
        }]
      }
      ,

      {
        rolename: "UI-00014 - Parameter Configuration",
        status: 0,
        fuctioncode: PAGE_AUTH.P0001400,
        chliddata: [{
          rolename: "บันทึก",
          status: 0,
          fuctioncode: PAGE_AUTH.P0001401
        }]
      }
      ,
      {
        rolename: "UI-00015 - Email Configuration",
        status: 0,
        fuctioncode: PAGE_AUTH.P0001500,
        chliddata: [{
          rolename: "แก้ไข",
          status: 0,
          fuctioncode: PAGE_AUTH.P0001501
        }]
      }
      ,
      {
        rolename: "UI-00016 - บันทึกข้อมูลจากเลขที่คำขอ (TMB Req No.)",
        status: 0,
        fuctioncode: PAGE_AUTH.P0001600,
        chliddata: []
      }
    ];

    this.responseObj = {
      data: "",
      message: ""
    }
    this.submited = false;
    this.roleStatus = false;
    // this.storeRole = this.store.select(state => state.sup00000.sup01000);
    // this.storeRole.subscribe(data => {
    //   this.sup01000 = data;
    // });
    // this.roleStatus = this.sup01000.status.toString();

    this.listRoleForm = this.fb.group({
      formArray: this.fb.array([])
    });


  }

  ngOnInit() {
    this.form = new FormGroup({
      roleName: new FormControl('', Validators.required),
      status: new FormControl(0, Validators.required)
    });

    this.storeRole = this.store.select(state => state.sup00000.sup01000);
    this.storeRole.subscribe(data => {
      this.sup01000 = data;
      if (this.sup01000.status != null ){
        this.roleStatus = true;
        this.form.setValue({ roleName: this.sup01000.roleName, status: this.sup01000.status });
      }
      // else{
      //   this.form.setValue({ roleName: this.sup01000.roleName, status: "0" });
      // }
    });

    if (this.sup01000.roldId != null || this.sup01000.roldId != undefined) {

      this.service.serviceSearchPermissionByRole(this.sup01000.roldId).subscribe(res => {
        this.setRolePermission(res);
      }, error => {
        // console.log("error", error);
      }, () => {
        // console.log("complete....");
      });
    }

  }

  clickSave() {
    this.submited = true;
    if (this.form.valid) {
      const modalConf: Modal = {
        msg: `<label>ท่านต้องการบันทึกข้อมูลหรือไม่</label>`,
        title: "ยืนยันการบันทึก",
        color: "notification"
      }
      const modalresp: Modal = {
        msg: '',
        title: "แจ้งเตือน",
        color: "alert"
      }
      this.modal.confirm((e) => {
        if (e) {
          this.service.saveNewRole(this.form, this.rolepermisson, this.sup01000.roldId ).subscribe(res => {
            this.responseObj = res;
            if (this.responseObj.message == MESSAGE_STATUS.FAILED) {
              this.modal.alert({ msg: "ทำรายการไม่สำเร็จ กรุณาดำเนินการอีกครั้งหรือติดต่อผู้ดูแลระบบ โทร. 02-299-2765" });
            } else {
              modalresp.msg  = "ทำรายการสำเร็จ";
              this.modal.confirm((e) => {
                if (e) {
              // this.modal.alert({ msg: this.responseObj.message });
                this.router.navigate(["/sup/sup01000"], {});
                }
              },modalresp);
            }
          }, error => {
          });

        }
      }, modalConf);
    } else {
      this.roleNameEliment.nativeElement.focus()

    }
  }
  // swich check
  chanageStatus(fuctioncode, index, index2) {
    if (index2 == -1) {
      if (this.rolepermisson[index].status == 0) {
        this.rolepermisson[index].status = 1
        this.changeStatusChlid(index,1);
      } else {
        this.rolepermisson[index].status = 0
        this.changeStatusChlid(index,0);
      }
    } else {
      if (this.rolepermisson[index].chliddata[index2].status == 0) {
        this.rolepermisson[index].chliddata[index2].status = 1
      } else {
        this.rolepermisson[index].chliddata[index2].status = 0
      }
    }
  }

  changeStatusChlid(index,status){

    for (let i = 0; i < this.rolepermisson[index].chliddata.length; i++) {
      if (status ==0 ){
        this.rolepermisson[index].chliddata[i].status = 0;
      }else{
        this.rolepermisson[index].chliddata[i].status = 1;
      }
   }

  }

  clickCancle() {
    // console.log("swift toggle ");
    this.router.navigate(["/sup/sup01000"], {});
  }

  setRolePermission(data) {
    let index = 0;
    // console.log("swift toggle ", data);
    if (data.length == 1) {
      this.rolepermisson.forEach(element => {
        element.status = 0;
        index++;
        element.chliddata.forEach(childs => {
          childs.status = 0;
          index++;
        });
      });
    } else {
      this.rolepermisson.forEach(element => {
        element.status = data[index].status;
        // console.log("item  key ",data[index].functionCode,index++);
        index++;
        element.chliddata.forEach(childs => {
          childs.status = data[index].status;
          // console.log("child  key ",data[index].functionCode,"",index++);
          index++;
        });
      });

    }

  }
  
  get formPropArray(): FormArray {
    return this.listRoleForm.get('formArray') as FormArray;
  }

  get listformPropArray(): FormArray {
    return this.listRoleForm.get('formArray.controls') as FormArray;
  }

  get roleName() {
    return this.form.get("roleName");
  }


}
class AppState {
  sup00000: {
    "sup01000": Sup01000
  }
}