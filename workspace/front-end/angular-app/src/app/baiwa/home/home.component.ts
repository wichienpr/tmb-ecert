import { Component, OnInit, AfterViewInit } from '@angular/core';
import { UserDetail } from 'app/user.model';
import { Observable } from 'rxjs';
import { Store } from '@ngrx/store';
import { EcerDashBoard } from 'app/dash-board.reducer';
import { DashboardService } from 'app/baiwa/home/dashboard.service';
import * as DashboardAction from 'app/dash-board.action';

@Component({
  selector: 'app-home',
  templateUrl: './home.component.html',
  styleUrls: ['./home.component.css']
})
export class HomeComponent implements OnInit ,AfterViewInit{

  userdetail: Observable<UserDetail>;
  dashboard: Observable<EcerDashBoard>;

  constructor(private store: Store<AppState>, private dashboardService:DashboardService) {
    this.userdetail = this.store.select("user");
    this.dashboard = this.store.select("ecerdashboard");
  }

  ngOnInit() {

  }

  ngAfterViewInit(): void {
    this.dashboardService.getDashBoard().subscribe(
      (data:any) => {
        // console.log(data);
        const initdashBord: EcerDashBoard = {
          newReq: data.countStatus1,
          paying: data.countStatus2,
          reject: data.countStatus3,
          cancle: data.countStatus4,
          paywaiting: data.countStatus5,
          payapprove: data.countStatus6,
          payreject: data.countStatus7,
          payfail: data.countStatus8,
          waituploadcert: data.countStatus9,
          complete: data.countStatus10,
          keyinReq: data.countStatus11
      };
        this.store.dispatch(new DashboardAction.Update(initdashBord));
      },
      error=>{
        console.log("error");
        alert("ไม่สามารถทำรายการได้.")
      }
    );
  }

}

interface AppState {
  user: UserDetail,
  ecerdashboard : EcerDashBoard
}
