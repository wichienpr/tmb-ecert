import { Component, OnInit, AfterViewInit, OnDestroy } from '@angular/core';
import { Router } from '@angular/router';
import { Store } from '@ngrx/store';

import { Modal } from 'models/';
import { UserDetail } from 'app/user.model';
import { AuthService, ModalService, CommonService } from 'services/';
import { ROLES, PAGE_AUTH } from 'app/baiwa/common/constants';
import { Observable } from 'rxjs';
import { digit } from 'app/baiwa/common/helpers';
import { UpdateUser } from 'app/user.action';

declare var $: any;

@Component({
  selector: 'app-semantic-menu',
  templateUrl: './semantic-menu.component.html',
  styleUrls: ['./semantic-menu.component.css']
})
export class SemanticMenuComponent implements OnInit, OnDestroy, AfterViewInit {

  routes: Routing[];
  user: Observable<UserDetail>;
  currentDate: Date = new Date();
  clockdisplay: any = { text: "---" };
  timeticker;
  R: any = ROLES;

  constructor(
    private store: Store<{}>,
    private router: Router,
    private modal: ModalService,
    private loginsv: AuthService,
    private commonsv: CommonService
  ) { }
  ngOnInit() {
    this.user = this.store.select('user');
    this.routes = [
      { // Main Menu 1
        label: "ทำคำขอใหม่",
        url: null,
        /**
         * @var role
         * this.checkR(ROLES.ADMIN) 
         * this.checkA(PAGE_AUTH.P0001501) 
         * this.checkRA(ROLES.ADMIN, PAGE_AUTH.P0001501)
         */
        role: this.checkAuthMenu([PAGE_AUTH.P0000600 , PAGE_AUTH.P0000500]),
        child: [ // Sub Menu 1.1 
          {
            label: "Request Form (พิมพ์ใบคำขอเปล่าให้ลูกค้าลงนาม และบันทึกข้อมูลภายหลัง)",
            url: "/nrq/nrq01000",
            role: this.checkA(PAGE_AUTH.P0000500)
          },
          {
            label: "Request Form (บันทึกคำขอก่อน และพิมพ์ใบคำขอให้ลูกค้าลงนาม)", url: "/nrq/nrq02000",
            role: this.checkA(PAGE_AUTH.P0000600)
          },
        ]
      },
      { // Main Menu New
        label: "บันทึกข้อมูลจากเลขที่คำขอ (TMB Req No.)",
        url: "/srn/srn01000",
        role: this.checkA(PAGE_AUTH.P0001600),
      },
      { // Main Menu 2
        label: "ตรวจสอบสถานะคำขอ",
        url: "/crs/crs01000",
        role: this.checkA(PAGE_AUTH.P0000300),
      },
      { // Main Menu 3
        label: "รายงาน",
        url: null,
        role: this.checkAuthMenu([PAGE_AUTH.P0000700 , PAGE_AUTH.P0000800 , PAGE_AUTH.P0000900]),
        child: [ // Sub Menu 3.1
          {
            label: "รายงานสรุปการให้บริการขอหนังสือรับรองนิติบุคคล ( e-Certificate ) : End day",
            url: "/rep/rep01000",
            role: this.checkA(PAGE_AUTH.P0000700)
          },
          {
            label: "รายงานสรุปการให้บริการขอหนังสือรับรองนิติบุคคล ( e-Certificate ) : Monthly",
            url: "/rep/rep02000",
            role: this.checkA(PAGE_AUTH.P0000800)
          },
          {
            label: "รายงาน Output VAT",
            url: "/rep/rep03000",
            role: this.checkA(PAGE_AUTH.P0000900)
          },
        ]
      },
      { // Main Menu 4
        label: "Batch Monitoring",
        url: "/btm/btm01000",
        role: this.checkA(PAGE_AUTH.P0001100)
      },
      { // Main Menu 5
        label: "Audit Log",
        url: "/adl/adl01000",
        role: this.checkA(PAGE_AUTH.P0001200),
      },
      { // Main Menu 6
        label: "Setup",
        url: null,
        role: this.checkAuthMenu([PAGE_AUTH.P0001300 , PAGE_AUTH.P0001400 , PAGE_AUTH.P0001500]),
        child: [ // Sub Menu 6.1
          { label: "Role Management", url: "/sup/sup01000", role: this.checkA(PAGE_AUTH.P0001300) },
          { label: "Parameter Configuration", url: "/sup/sup02000", role: this.checkA(PAGE_AUTH.P0001400) },
          { label: "Email Configuration", url: "/sup/sup03000", role: this.checkA(PAGE_AUTH.P0001500) }
        ]
      },
      { // Main Menu 7
        label: "คู่มือการใช้งาน",
        url: null,
        role: true,
        child: [ // Sub Menu 6.1
          { label: "Video การใช้งานสำหรับ Requester", url: "/man/man02000", role: true },
          { label: "เอกสารคู่มือการใช้งานระบบ", url: "/man/man01000" , role: true },

        ]
      },
    ];

    this.timeticker = setInterval(() => { this.clock() }, 1000);
  }

  ngAfterViewInit() {
    // $('#status').dropdown();
  }

  ngOnDestroy(): void {
    clearInterval(this.timeticker);
  }

  // changeStatus(e) {
  //   const value = e.target.value;
  //   this.store.dispatch(new UserActions.UpdateStatus(value));
  // }
  checkAuthMenu(_auths: any[]) {
    return this.commonsv.isAuthMenu(_auths);
  }
  checkA(_auths: PAGE_AUTH) {
    return this.commonsv.isAuth(_auths);
  }
  checkR(_roles: ROLES) {
    return this.commonsv.isRole(_roles);
  }
  checkRA(_roles: ROLES, _auths: PAGE_AUTH) { // Check roles or auths
    return this.commonsv.isRole(_roles) || this.commonsv.isAuth(_auths);
  }

  downloadManual(){
    return this.commonsv.downloadManual();
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
      this.loginsv.logout().subscribe(rsp => {
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
      });
    }
  }

  clock() {
    let d = new Date();
    let h = d.getHours();
    let m = d.getMinutes();
    let s = d.getSeconds();
    this.clockdisplay.text = (digit(h) + ":" + digit(m));
    // console.log(this.clockdisplay);
  }

  get currentRoute() { return this.router.url == '/home' }

}

class Routing {
  label: string;      // functional
  url: string;        // functional
  role?: boolean;     // optional
  func?: Function;    // optional
  child?: Routing[];  // optional
}
