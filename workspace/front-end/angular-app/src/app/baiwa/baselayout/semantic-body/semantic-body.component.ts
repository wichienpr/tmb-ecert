import { Component, OnInit } from '@angular/core';
import { Modal } from 'models/';

@Component({
  selector: 'app-semantic-body',
  templateUrl: './semantic-body.component.html',
  styleUrls: ['./semantic-body.component.css']
})
export class SemanticBodyComponent implements OnInit {

  modalAlert: Modal;
  modalAlertSuccess: Modal;
  modalConfirm: Modal;

  constructor() {
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
  }

  ngOnInit() {
  }

}
