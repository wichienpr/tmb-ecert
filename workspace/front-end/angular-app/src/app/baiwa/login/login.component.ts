import { Component, OnInit } from '@angular/core';
import { Router, ActivatedRoute } from "@angular/router";
import { ModalService } from 'app/baiwa/common/services';
import { Modal } from 'models/';
import { AuthService } from 'app/baiwa/common/services/auth.service';
declare var $: any;
@Component({
  selector: 'app-login',
  templateUrl: './login.component.html',
  styleUrls: ['./login.component.css']
})
export class LoginComponent implements OnInit {
  username: string = "";
  password: string = "";
  modalObj: Modal;
  showLoginMessage: boolean = false;
  loginMessage: string;

  constructor(private router: Router, private modal: ModalService, private loginsv: AuthService) { }

  ngOnInit() {
    this.modalObj = {
      modalId: "confirm",
      size: "small",
      title: "การยืนยัน",
      type: "confirm"
    };
  }

  onSubmit(event) {
    $.blockUI({ message: null }); 

    this.loginsv.login(this.username, this.password).subscribe(resp => {
      let result: AjaxLoginVo = resp.json() as AjaxLoginVo;
      console.log(result);
      $.unblockUI();

      if (result.status == "SUCCESS") {
        this.router.navigate(["/home"]);
      } else if (result.status == "FAIL") {
        this.loginMessage = "ข้อมูล Username หรือ Password ไม่ถูกต้อง กรุณาลองใหม่อีกครั้ง";
        this.showLoginMessage = true;
      } else if (result.status == "DUP_LOGIN") {
        const modal: Modal = {
          msg: "ระบบตรวจสอบพบว่ามีผู้กำลังใช้งานอยู่ ต้องการใช้งานต่อหรือไม่",
          title: "แจ้งเตือนการเข้าใช้งาน",
          color: "notification",
          approveMsg: "ดำเนินการต่อ"
        }
        this.modal.confirm((e) => {
          if (e) {
            this.router.navigate(["/home"]);
          }
        }, modal);
      } else if (result.status == "OUTOFF_SERVICE") {
        this.loginMessage = "ไม่สามารถดำเนินการต่อได้ในขณะนี้เนื่องจากปิดระบบตั้งแต่เวลา........ถึงเวลา........";
        this.showLoginMessage = true;
      }

    });

    return false;
  }

}

export interface AjaxLoginVo {
  username: string
  status: string
}
