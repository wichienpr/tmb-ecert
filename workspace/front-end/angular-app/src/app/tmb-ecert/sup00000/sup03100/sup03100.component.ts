import { Component, OnInit } from '@angular/core';
import { Router, ActivatedRoute } from '@angular/router';
import { Sup03100Service } from 'app/tmb-ecert/sup00000/sup03100/sup03100.service';
import { FormGroup, FormControl, Validators } from '@angular/forms';
import { Sup03100 } from 'app/tmb-ecert/sup00000/sup03100/sup03100.model';
import { ModalService } from 'app/baiwa/common/services';
import { Modal } from 'app/baiwa/common/models';
import { sup03000 } from 'app/tmb-ecert/sup00000/sup03000/sup03000.model';
import { Store } from '@ngrx/store';
import { Observable } from 'rxjs';

@Component({
   selector: 'app-sup03100',
   templateUrl: './sup03100.component.html',
   styleUrls: ['./sup03100.component.css']
})
export class Sup03100Component implements OnInit {
   emailId: any;
   responseObj: Sup03100;
   emailForm: FormGroup;
   requestObj: any;
   response: any;
   emailName: any;
   emailStatus: any;
   emailTemplate: Observable<sup03000>;
   sup03000: sup03000;

   constructor(private store: Store<AppState>, private service: Sup03100Service, private router: Router,
      private route: ActivatedRoute, private modal: ModalService) {

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
         this.responseObj = res[0];
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

      if (this.emailForm.valid) {
         this.modal.confirm((e) => {
            if (e) {
               this.service.callSaveEmailDetailAPI(this.requestObj).subscribe(res => {
                  console.debug("seve email success.", res);
                  this.response = res;
                  if (this.response.message == null) {
                     this.modal.alert({ msg: "ทำรายการล้มเหลว" })
                  } else {
                     modalresp.msg = this.response.message;
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

               });
            }
         }, modalConf);

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

}


class AppState {
   sup00000: {
      "sup03000": sup03000
   }
}