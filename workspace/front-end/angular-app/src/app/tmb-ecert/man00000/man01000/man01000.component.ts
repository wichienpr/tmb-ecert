import { Component, OnInit } from '@angular/core';
import { AjaxService, ModalService } from 'app/baiwa/common/services';
import { Router } from '@angular/router';
import { Modal } from 'app/baiwa/common/models';

@Component({
  selector: 'app-man01000',
  templateUrl: './man01000.component.html',
  styleUrls: ['./man01000.component.css']
})
export class Man01000Component implements OnInit {

  constructor(private ajax: AjaxService,private router: Router, private modalService: ModalService,) { }

  ngOnInit() {
    const modalConf: Modal = {
      msg: `<label>ท่านต้องการดาวน์โหลดเอกสารคู่มือการใช้งานระะบบ หรือไม่</label>`,
      title: "ยืนยันการดาวน์โหลดเอกสาร",
      color: "notification"
    }
    this.modalService.confirm((e) => {
      if (e) {
        this.ajax.download("/api/manual/pdf");
        this.router.navigate(["/"], {})
      }else{
        this.router.navigate(["/"], {})
      }
    }, modalConf);

  }

}
