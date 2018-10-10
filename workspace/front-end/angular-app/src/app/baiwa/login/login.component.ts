import { Component, OnInit } from '@angular/core';
import { Router, ActivatedRoute } from "@angular/router";
import { ModalService } from 'app/baiwa/common/services';
import { Modal } from 'models/';
import { AuthService } from 'app/baiwa/common/services/auth.service';
import { UserDetail } from 'app/user.model';
import { Store } from '@ngrx/store';
import { UpdateUser } from 'app/user.action';
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
  loading: boolean = false;

  constructor(private router: Router, private modal: ModalService, private loginsv: AuthService
    , private store: Store<AppState>) {

  }

  ngOnInit() {
    this.modalObj = {
      modalId: "confirm",
      size: "small",
      title: "การยืนยัน",
      type: "confirm"
    };
  }

  onSubmit(event) {
    this.loading = true;

    this.loginsv.login(this.username, this.password).subscribe(resp => {
      let result: AjaxLoginVo = resp.json() as AjaxLoginVo;
      console.log(result);
      this.loading = false;
      if (result.status == "SUCCESS") {
        const INIT_USER_DETAIL: UserDetail = {
          roles: result.roles,
          userId: result.userId,
          username: result.username,
          firstName: result.firstName,
          lastName: result.lastName,
          auths: result.auths,
          segment: result.segment || "",
        };

        this.store.dispatch(new UpdateUser(INIT_USER_DETAIL));
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

        const INIT_USER_DETAIL: UserDetail = {
          roles: result.roles,
          userId: result.userId,
          username: result.username,
          firstName: result.firstName,
          lastName: result.lastName,
          auths: result.auths,
          segment: result.segment || "",
        };
        this.modal.confirm((e) => {
          if (e) {
            this.store.dispatch(new UpdateUser(INIT_USER_DETAIL));
            this.router.navigate(["/home"]);
          }
        }, modal);
      } else if (result.status == "OUTOFF_SERVICE") {
        this.loginMessage = "ไม่สามารถดำเนินการต่อได้ในขณะนี้เนื่องจากปิดระบบตั้งแต่เวลา........ถึงเวลา........";
        this.showLoginMessage = true;
      }

    },
      error => {
        $.unblockUI();
      })
      ;

    return false;
  }

}

export interface AjaxLoginVo {
  username: string
  status: string
  userId: string
  firstName: string
  lastName: string
  discription: string
  roles: string[]
  auths: string[],
  segment: string
}
interface AppState {
  user: UserDetail
}
