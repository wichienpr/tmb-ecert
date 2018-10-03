import { Component, OnInit, AfterViewInit } from '@angular/core';
import { Store } from '@ngrx/store';
import { Observable } from 'rxjs';
import { UserDetail } from 'app/user.model';
import { Router } from '@angular/router';
import { ModalService } from 'app/baiwa/common/services';
import { Modal } from 'models/';
import { AuthService } from 'app/baiwa/common/services/auth.service';

declare var $: any;

@Component({
  selector: 'app-semantic-menu',
  templateUrl: './semantic-menu.component.html',
  styleUrls: ['./semantic-menu.component.css']
})
export class SemanticMenuComponent implements OnInit {

  routes: Routing[];
  user: Observable<UserDetail>;
  constructor(private store: Store<{}>, private router: Router, private modal: ModalService
    , private loginsv:AuthService) {
    this.routes = [
      { // Main Menu 1
        label: "ทำคำขอใหม่",
        url: null,
        child: [ // Sub Menu 1.1 
          {
            label: "Examples", url: null,
            child: [
              { label: "Example 1", url: "/examples/ex1" },
              { label: "Example 2", url: "/examples/ex2" },
              { label: "Example 3", url: "/examples/ex3" },
              { label: "Example 4", url: "/examples/ex4" },
            ]
          },
          { label: "Request Form สำหรับลูกค้าทำรายการเอง", url: "/nrq/nrq01000" },
          { label: "Request Form สำหรับทำรายการให้ลูกค้าลงนาม", url: "/nrq/nrq02000" },
        ]
      },
      { // Main Menu 2
        label: "ตรวจสอบสถานะคำขอ",
        url: "/crs/crs01000"
      },
      { // Main Menu 3
        label: "รายงาน",
        url: null,
        child: [ // Sub Menu 3.1
          { label: "รายงานสรุปการให้บริการขอหนังสือรับรองนิติบุคคล ( e-Certificate ) : End day", url: "/rep/rep01000" },
          { label: "รายงานสรุปการให้บริการขอหนังสือรับรองนิติบุคคล ( e-Certificate ) : Monthly", url: "/rep/rep02000" },
          { label: "รายงาน Output VAT", url: "/rep/rep03000" },
        ]
      },
      { // Main Menu 4
        label: "Batch Monitoring",
        url: "/monitoring"
      },
      { // Main Menu 5
        label: "Audit Log",
        url: "/auditLog"
      },
      { // Main Menu 6
        label: "Setup",
        url: null,
        child: [ // Sub Menu 6.1
          { label: "Role Management", url: "/role-Management" },
          { label: "Parameter Configuration", url: "/parameter-Configuration" },
          { label: "Email Configuration", url: "/email-configuration" }
        ]
      },
      /*
      { // Main Menu test
        label: "LV1", url: null,
        child: [ // Sub Menu test.1
          {
            label: "LV2", url: null,
            child: [ // Sub Menu test.1.1
              {
                label: "LV3", url: null,
                child: [ // Sub Menu test1.1.1
                  { label: "LV4", url: "./test" }
                ] // Sub Menu test1.1.1
              }
            ] // Sub Menu test.1.1
          }
        ] // Sub Menu test.1
      } // Main Menu test
      */
    ];
  }

  ngOnInit() {
    this.user = this.store.select('user');
  }

  logout() {
    const modal: Modal = {
      msg: "ยืนยันการออกจากระบบ ?",
      title: "ยืนยัน"
    };
    this.modal.confirm((e) => { this.logoutConfirm(e) }, modal);
  }

  logoutConfirm(e: boolean) {
    if (e == true) {

      this.loginsv.logout().subscribe( rsp => {
        this.router.navigate(['login']);
      });
    }
  }

}

class Routing {
  label: string;      // functional
  url: string;        // functional
  child?: Routing[];  // optional
}