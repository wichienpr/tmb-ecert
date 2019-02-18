import { Component, OnInit } from '@angular/core';
import { Modal } from 'models/';
import { Router } from '@angular/router';
import { Observable } from 'rxjs';
import { ModalService, AuthService } from 'app/baiwa/common/services';
import { UserDetail } from 'app/user.model';
import { Store } from '@ngrx/store';
import { UpdateUser } from 'app/user.action';

@Component({
  selector: 'app-semantic-body',
  templateUrl: './semantic-body.component.html',
  styleUrls: ['./semantic-body.component.css']
})
export class SemanticBodyComponent implements OnInit {

  modalAlert: Modal;
  modalAlertFunction: Modal;
  modalAlertSuccess: Modal;
  modalConfirm: Modal;
  loading: Observable<boolean>;

  constructor(
    private router: Router,
    private store: Store<AppState>,
    private modal: ModalService,
    private auth: AuthService
  ) {
    this.modalAlert = {
      modalId: "alert",
      size: "small",
      title: "แจ้งเตือน",
      class: "notification",
      type: "alert",
      for: "notify"
    }
    this.modalAlertFunction = {
      modalId: "alert-func",
      size: "small",
      title: "แจ้งเตือน",
      class: "notification",
      type: "alert",
      for: "confirm"
    }
    this.modalAlertSuccess = {
      modalId: "alert-success",
      size: "small",
      title: "แจ้งเตือน",
      class: "notification",
      type: "alert",
      for: "confirm"
    }
    this.modalConfirm = {
      modalId: "confirm",
      size: "small",
      title: "การยืนยัน",
      type: "confirm"
    };
    
  }

  ngOnInit() { }

  changeOfRoutes() {
    this.auth.getUser().catch(error => {
      console.error("ไม่สามารถรับข้อมูลผู้ใช้ได้ ", error);
      this.modal.alertWithAct({ msg: "ไม่สามารถทำรายการได้ กรุณาเข้าสู่ระบบใหม่อีกครั้ง" }, clicked => {
        if (clicked) {
          const INIT_USER_DETAIL: UserDetail = {
            roles: [],
            userId: "",
            username: "",
            firstName: "",
            lastName: "",
            auths: [],
            segment: "",
            department:""
          };
          this.store.dispatch(new UpdateUser(INIT_USER_DETAIL));
          this.router.navigate(['/login']);
        }
      })
    })
  }

}

interface AppState {
  user: UserDetail
}
