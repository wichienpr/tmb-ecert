import { Component, OnInit } from '@angular/core';
import { Router, ActivatedRoute } from '@angular/router';
import { Sup03100Service } from 'app/tmb-ecert/sup00000/sup03100/sup03100.service';
import { FormGroup, FormControl, Validators } from '@angular/forms';
import { Sup03100 } from 'app/tmb-ecert/sup00000/sup03100/sup03100.model';
import { ModalService, CommonService } from 'app/baiwa/common/services';
import { Modal } from 'app/baiwa/common/models';
import { sup03000 } from 'app/tmb-ecert/sup00000/sup03000/sup03000.model';
import { Store } from '@ngrx/store';
import { Observable } from 'rxjs';
import { DomSanitizer } from '@angular/platform-browser';

@Component({
  selector: 'app-sup03100',
  templateUrl: './sup03100.component.html',
  styleUrls: ['./sup03100.component.css']
})
export class Sup03100Component implements OnInit {
  emailId: any;
  responseObj: any;
  emailForm: FormGroup;
  requestObj: any;
  response: any;
  emailName: any;
  emailStatus: any;
  emailTemplate: Observable<sup03000>;
  sup03000: sup03000;

  config = {
    tabsize: 2,
    height: 150,
    toolbar: [
      // [groupName, [list of button]]
      ['misc', ['codeview', 'undo', 'redo']],
      ['style', ['bold', 'italic', 'underline', 'clear']],
      ['font', [ 'strikethrough']],
      ['fontsize', ['fontsize', 'color']],
      ['para', ['style0', 'ul', 'ol', 'paragraph', 'height']]
    ],
  };

  constructor(private store: Store<AppState>, private service: Sup03100Service, private router: Router,
    private route: ActivatedRoute, private modal: ModalService, private sanitizer: DomSanitizer, private commonsvr: CommonService) {

    this.emailTemplate = this.store.select(state => state.sup00000.sup03000);
    this.emailTemplate.subscribe(data => {
      this.sup03000 = data;
    })

    this.emailForm = new FormGroup({
      subject: new FormControl('', Validators.required),
      body: new FormControl(null, Validators.required),
      from: new FormControl(null, Validators.required),
      to: new FormControl(null, Validators.required),
      attachFileflag: new FormControl('0', Validators.required),
    });

    this.response = {
      data: "",
      message: ""
    }
  }

  ngOnInit() {
    this.service.callSearchEmailDetailAPI(this.sup03000.emailConfigId).subscribe(res => {
      this.responseObj = res;
      console.debug("response ", this.responseObj)
      this.emailForm.setValue({
        subject: this.responseObj.subject,
        body: this.responseObj.body,
        from: this.responseObj.from,
        to: this.responseObj.to,
        attachFileflag: String(this.sup03000.emailStatus)
      });
    }, error => {

    });
  }

  clickSave() {
    // console.log("html tag ==> ", this.emailForm.value.body);
    // console.log("html tag ==> ", this.sanitizer.bypassSecurityTrustHtml(this.emailForm.value.body))

    this.requestObj = {
      emailDetail_id: this.responseObj.emailDetail_id,
      emailConfig_id: this.responseObj.emailConfig_id,
      subject: this.emailForm.value.subject,
      body: this.emailForm.value.body,
      from: this.emailForm.value.from,
      to: this.emailForm.value.to,
      attachFile_flag: this.emailForm.value.attachFileflag
    }

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
    // console.log("body value ",this.emailForm.value.body  );

    if (this.emailForm.value.body == "" || this.emailForm.value.body =="<p><br></p>") {
      this.modal.alert({ msg: "กรุณากรอก Body" });
    } else {
      if (this.emailForm.valid) {
        this.modal.confirm((e) => {
          if (e) {
            this.commonsvr.blockui();
            this.service.callSaveEmailDetailAPI(this.requestObj).subscribe(res => {
              console.debug("seve email success.", res);
              this.response = res;
              this.commonsvr.unblockui();
              if (this.response.message == null) {
                this.modal.alert({ msg: "ทำรายการไม่สำเร็จ กรุณาดำเนินการอีกครั้งหรือติดต่อผู้ดูแลระบบ โทร. 02-299-2765" });
              } else {
                modalresp.msg = "ทำรายการสำเร็จ";
                this.modal.confirm((e) => {
                  if (e) {
                    // this.modal.alert({ msg: this.responseObj.message });
                    this.router.navigate(["/sup/sup03000"], {
                      queryParams: {
                        emailName: this.emailName,
                        emailStatus: this.emailStatus
                      }

                    });
                  }
                }, modalresp);
              }
            }, error => {
              this.commonsvr.unblockui();
            });
          }
        }, modalConf);
      }
    }



  }

  clickCancel() {
    this.router.navigate(["/sup/sup03000"], {});
  }
  get subject() {
    return this.emailForm.get("subject");
  }
  get body() {
    return this.emailForm.get("body");
  }
  get from() {
    return this.emailForm.get("from");
  }
  get to() {
    return this.emailForm.get("to");
  }
  get attachFile_flag() {
    return this.emailForm.get("attachFile_flag");
  }
  get sanitizedHtml() {
    return this.sanitizer.bypassSecurityTrustHtml(this.emailForm.get('body').value);
  }


}


class AppState {
  sup00000: {
    "sup03000": sup03000
  }
}
