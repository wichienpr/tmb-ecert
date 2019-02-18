import { Component, OnInit, AfterViewInit } from '@angular/core';
import { UserDetail } from 'app/user.model';
import { Observable } from 'rxjs';
import { Store } from '@ngrx/store';
import { EcerDashBoard } from 'app/dash-board.reducer';
import { DashboardService } from 'app/baiwa/home/dashboard.service';
import * as DashboardAction from 'app/dash-board.action';
import { Router } from "@angular/router";
import { CommonService, ModalService } from '../common/services';
import { ROLES, PAGE_AUTH } from '../common/constants';
import { UpdateUser } from 'app/user.action';

@Component({
  selector: 'app-home',
  templateUrl: './home.component.html',
  styleUrls: ['./home.component.css']
})
export class HomeComponent implements OnInit, AfterViewInit {

  userdetail: Observable<UserDetail>;
  dashboard: Observable<EcerDashBoard>;

  constructor(private store: Store<AppState>, private dashboardService: DashboardService,
    private router: Router, private commonserv: CommonService, private modal: ModalService) {
    this.userdetail = this.store.select("user");
    this.dashboard = this.store.select("ecerdashboard");
  }

  ngOnInit() { }

  ngAfterViewInit(): void {
    this.dashboardService.getDashBoard().subscribe(
      (data: any) => {
        const initdashBord: EcerDashBoard = {
          newReq: data.newrequest,
          paying: data.paymentProcessing,
          reject: data.refuseRequest,
          cancle: data.cancelRequest,
          paywaiting: data.waitPaymentApproval,
          payapprove: data.paymentApprovals,
          payreject: data.chargeback,
          payfail: data.paymentfailed,
          waituploadcert: data.waitUploadCertificate,
          complete: data.succeed,
          keyinReq: data.waitSaveRequest
        };
        this.store.dispatch(new DashboardAction.Update(initdashBord));
      },
      error => {
        console.error("error");
        // alert("ไม่สามารถทำรายการได้.") 
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
      }
    );
  }

  searchStatus(code): void {
    if (code == 10011) {
      this.router.navigate(["/srn/srn01000"], {
        queryParams: { codeStatus: code }
      });
    } else {
      this.router.navigate(["/crs/crs01000"], {
        queryParams: { codeStatus: code }
      });
    }
  }

  get isShowDashBoard() {
    // return !this.commonserv.isRole(ROLES.ADMIN);
    return this.commonserv.isAuth(PAGE_AUTH.P0000200);
  }

}

interface AppState {
  user: UserDetail,
  ecerdashboard: EcerDashBoard
}
