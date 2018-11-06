import { Component, OnInit } from '@angular/core';
import { Modal } from 'models/';
import { Router, NavigationStart, NavigationEnd, NavigationCancel } from '@angular/router';
import { Observable } from 'rxjs';
import { CommonService } from 'app/baiwa/common/services';

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
    private common: CommonService
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

}
