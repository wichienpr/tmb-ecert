import { Component, OnInit, ElementRef, ViewChild } from '@angular/core';
import { PAGE_AUTH } from 'app/baiwa/common/constants';
import { FormGroup, FormControl, Validators } from '@angular/forms';
import { sup01100Service } from 'app/tmb-ecert/sup00000/sup01100/sup01100.service';
import { Router, ActivatedRoute } from '@angular/router';
import { ModalService } from 'app/baiwa/common/services';


@Component({
  selector: 'app-sup01100',
  templateUrl: './sup01100.component.html',
  styleUrls: ['./sup01100.component.css'],
})


export class sup01100Component implements OnInit {
  @ViewChild('roleNameView') roleNameEliment: ElementRef;
  rolepermisson: any
  userRolePermission: any
  roleId: Number;
  submited: boolean;


  form: FormGroup;
  responseObj: any;
  // roleName = new FormControl('');

  constructor(private router: Router, private route: ActivatedRoute, private service: sup01100Service, private modal: ModalService) {
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
          rolename: "พิมพ์ Cover Sheet",
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
        rolename: "UI-00005 - Request Form สำหรับลูกค้าทำรายการเอง",
        status: 0,
        fuctioncode: PAGE_AUTH.P0000500,
        chliddata: []
      }
      ,
      {
        rolename: "UI-00006 - Request Form สำหรับทำรายการให้ลูกค้าลงนาม",
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
          rolename: "Export to Exce",
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
        rolename: "UI-00012 - Audit Loge",
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
        rolename: "UI-00013 - Role Manamen",
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
          rolename: "แก้ไข",
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
    ];

    this.responseObj = {
      data: "",
      message: ""
    }

    this.submited = false;

  }

  ngOnInit() {
    this.form = new FormGroup({
      roleName: new FormControl('', Validators.required),
      status: new FormControl('0', Validators.required)
    });

    this.roleId = this.route.snapshot.queryParams["roleId"];
    let roleName = this.route.snapshot.queryParams["roleName"];
    let roleStatus = this.route.snapshot.queryParams["roleStatus"];


    if (this.roleId != null || this.roleId != undefined) {
      this.form.setValue({ roleName: roleName, status: roleStatus })
      this.service.serviceSearchPermissionByRole(this.roleId).subscribe(res => {
        this.setRolePermission(res);
      }, error => {
        console.log("error", error)
      }, () => {
        console.log("complete....")
      })
      // this.setRolePermission();
    }

  }

  clickSave() {
    this.submited = true;

    if (this.form.valid) {
      this.service.saveNewRole(this.form, this.rolepermisson, this.roleId).subscribe(res => {
        this.responseObj = res;
        if (this.responseObj.message == null) {
          this.modal.alert({ msg: "ทำรายการล้มเหลว" })
        } else {
          this.modal.alert({ msg: this.responseObj.message });
        }
      }, error => {
        console.log("error", error)
      }, () => {
        console.log(" save complete. ")
      });

    } else {
      this.roleNameEliment.nativeElement.focus()

    }
  }
  // swich check
  chanageStatus(fuctioncode, index, index2) {
    console.log("swift toggle ", fuctioncode, " index", index, " j ", index2);
    if (index2 == -1) {
      if (this.rolepermisson[index].status == 0) {
        this.rolepermisson[index].status = 1
      } else {
        this.rolepermisson[index].status = 0
      }
    } else {
      if (this.rolepermisson[index].chliddata[index2].status == 0) {
        this.rolepermisson[index].chliddata[index2].status = 1
      } else {
        this.rolepermisson[index].chliddata[index2].status = 0
      }
    }
  }

  clickCancle() {
    console.log("swift toggle ");
    this.router.navigate(["/sup/sup01000"], {});
  }

  setRolePermission(data) {
    let index = 0;
    console.log("swift toggle ",data);
    if(data.length == 1){
      this.rolepermisson.forEach(element => {
        element.status = 0;
        index++;
        element.chliddata.forEach(childs => {
          childs.status = 0;
          index++;
        });
      });
    }else{
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
    
    // this.rolepermisson.forEach(element => {
    //   console.log(element.fuctioncode, " status ", element.status)
    // });

  }



  get roleName() {
    return this.form.get("roleName");
  }


}
