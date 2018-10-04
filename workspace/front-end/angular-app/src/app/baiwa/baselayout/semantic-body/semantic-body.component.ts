import { Component, OnInit } from '@angular/core';
import { Modal } from 'models/';
import { Router, NavigationStart, NavigationEnd, NavigationCancel } from '@angular/router';

@Component({
  selector: 'app-semantic-body',
  templateUrl: './semantic-body.component.html',
  styleUrls: ['./semantic-body.component.css']
})
export class SemanticBodyComponent implements OnInit {

  modalAlert: Modal;
  modalAlertSuccess: Modal;
  modalConfirm: Modal;
  loading: boolean = false;

  constructor(private router: Router) {
    this.modalAlert = {
      modalId: "alert",
      size: "small",
      title: "แจ้งเตือน",
      class: "notification",
      type: "alert",
      for: "notify"
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

    this.router.events.subscribe(event => {
      if (event instanceof NavigationStart) {
        this.loading = true;
      } else if (
        event instanceof NavigationEnd ||
        event instanceof NavigationCancel
      ) {
        this.loading = false;
      }
    });
  }

  ngOnInit() {
  }

}
