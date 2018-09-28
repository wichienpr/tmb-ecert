import { Component, OnInit } from '@angular/core';
import { Router, ActivatedRoute } from "@angular/router";
import { ModalService } from 'app/baiwa/common/services';
import { Modal } from 'models/';
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

  constructor(private router: Router, private modal: ModalService) { }

  ngOnInit() {
    this.modalObj = {
      modalId: "confirm",
      size: "small",
      title: "การยืนยัน",
      type: "confirm"
    };
  }

  onSubmit(event) {
    if (this.username == "admin" && this.password == "password") {
      const modal: Modal = {
        msg: `ระบบตรวจสอบพบว่ามีผู้กำลังใช้งานอยู่ ต้องการใช้งานต่อหรือไม่`,
        title: "แจ้งเตือนการเข้าใช้งาน",
        color: "notification",
        approveMsg: "ดำเนินการต่อ"
      }
      this.modal.confirm((e) => {
        if (e) {
          this.router.navigate(["/home"]);
        }
      }, modal);
    } else {
      let promise = new Promise((resolve, reject) => {
        $(".message").show();
        setTimeout(() => {
          resolve(true); // wait for click
        });
      }).then(resolve => {
        if (resolve) {
          $('.message .close').on('click', function () {
            $(".message").hide();
          });
        }
      });
    }
  }

}


