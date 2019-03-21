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
    // alert(sessionStorage.getItem("ssologin-id"));

    if(sessionStorage.getItem("ssologin-id")!=null){
      this.onSingleSignon(sessionStorage.getItem("ssologin-id"));
    }

    this.modalObj = {
      modalId: "confirm",
      size: "small",
      title: "การยืนยัน",
      type: "confirm"
    };
  }

  onSingleSignon(username) {
    this.loading = true;
    this.showLoginMessage = false;

    this.loginsv.login(username,"defaultpasspwd","false").subscribe(resp => {
      let result: AjaxLoginVo = resp as AjaxLoginVo;
      // console.log("ACTION : LOGIN", result);
      this.loading = false;
      if (result.status == "SUCCESS" && result.userId) {
        const INIT_USER_DETAIL: UserDetail = {
          roles: result.roles,
          userId: result.userId,
          username: result.username,
          firstName: result.firstName,
          lastName: result.lastName,
          auths: result.auths,
          segment: result.segment || "",
          department: result.department,
        };

        console.log('Show INIT_USER_DETAIL : ' + INIT_USER_DETAIL.username);
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
          department : result.department
        };
        this.modal.confirm((e) => {
          if (e) {
            this.loginsv.kickPreviousUser().then(() => {
              this.store.dispatch(new UpdateUser(INIT_USER_DETAIL));
              this.router.navigate(["/home"]);
            }).catch(error => {
              console.error(error);
            });
          }
        }, modal);
      } else if (result.status == "OUTOFF_SERVICE") {
        this.loginMessage = "ไม่สามารถดำเนินการต่อได้ในขณะนี้เนื่องจากปิดระบบตั้งแต่เวลา " + result.discription; //........ถึงเวลา........";
        this.showLoginMessage = true;
      } else if (result.status == "MULTI_ROLE"){
        this.loginMessage = "เกิดข้อผิดผลาด โปรดติดต่อ ITIA เนื่องจาก User ของท่านมีสิทธิ์มากกว่า 1 Role";
        this.showLoginMessage = true;
      } else if(result.status ="NOT_ALLOW"){
        this.loginMessage = "ท่านไม่มีสิทธิ์เข้าใช้งานระบบ";
        this.showLoginMessage = true;
      }else{
        this.loginMessage = "ข้อมูล Username หรือ Password ไม่ถูกต้อง กรุณาลองใหม่อีกครั้ง";
        this.showLoginMessage = true;
      }

    },
      error => {
        this.loading = false;
        this.loginMessage = "เกิดข้อผิดผลาดกรุณาติดต่อผู้ดูแลระบบ";
        this.showLoginMessage = true;
        console.error("error", error)
      },
      () => {
        this.loading = false;
        // console.log("complete")
      }
    );
    //console.log(sessionStorage.getItem("ssologin-id"));
    sessionStorage.removeItem("ssologin-id");
    return false;
  }

  onSubmit(event) {
    this.loading = true;
    this.showLoginMessage = false;

    this.loginsv.login(this.username, this.password,"true").subscribe(resp => {
      let result: AjaxLoginVo = resp as AjaxLoginVo;
      // console.log("ACTION : LOGIN", result);
      this.loading = false;
      if (result.status == "SUCCESS" && result.userId) {
        const INIT_USER_DETAIL: UserDetail = {
          roles: result.roles,
          userId: result.userId,
          username: result.username,
          firstName: result.firstName,
          lastName: result.lastName,
          auths: result.auths,
          segment: result.segment || "",
          department: result.department,
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
          department : result.department
        };
        this.modal.confirm((e) => {
          if (e) {
            this.loginsv.kickPreviousUser().then(() => {
              this.store.dispatch(new UpdateUser(INIT_USER_DETAIL));
              this.router.navigate(["/home"]);
            }).catch(error => {
              console.error(error);
            });
          }
        }, modal);
      } else if (result.status == "OUTOFF_SERVICE") {
        this.loginMessage = "ไม่สามารถดำเนินการต่อได้ในขณะนี้เนื่องจากปิดระบบตั้งแต่เวลา " + result.discription; //........ถึงเวลา........";
        this.showLoginMessage = true;
      } else if (result.status == "MULTI_ROLE"){
        this.loginMessage = "เกิดข้อผิดผลาด โปรดติดต่อ ITIA เนื่องจาก User ของท่านมีสิทธิ์มากกว่า 1 Role";
        this.showLoginMessage = true;
      } else if(result.status ="NOT_ALLOW"){
        this.loginMessage = "ท่านไม่มีสิทธิ์เข้าใช้งานระบบ";
        this.showLoginMessage = true;
      }else{
        this.loginMessage = "ข้อมูล Username หรือ Password ไม่ถูกต้อง กรุณาลองใหม่อีกครั้ง";
        this.showLoginMessage = true;
      }

    },
      error => {
        this.loading = false;
        this.loginMessage = "เกิดข้อผิดผลาดกรุณาติดต่อผู้ดูแลระบบ";
        this.showLoginMessage = true;
        console.error("error", error)
      },
      () => {
        this.loading = false;
        // console.log("complete")
      }
    );

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
  segment: string,
  department:string
}
interface AppState {
  user: UserDetail
}
