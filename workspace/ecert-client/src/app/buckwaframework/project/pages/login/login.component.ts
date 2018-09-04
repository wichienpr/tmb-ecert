import { Component, OnInit } from '@angular/core';
declare var $: any;
@Component({
  selector: 'app-login',
  templateUrl: './login.component.html',
  styleUrls: ['./login.component.css']
})
export class LoginComponent implements OnInit {
  username: string = "";
  password: string = "";
  modal: string[] = ["modal"];
  errorMessage: any = false;


  constructor() { }

  ngOnInit() {

  }
  ngAfterViewInit() {
    $('.message .close')
      .on('click', function () {
        $(this)
          .closest('.message')
          .transition('fade')
          ;
      })
      ;
  }

  openModal(id) {
    $('#' + id).modal('show');
  }

  closeModal(id) {
    $('#' + id).modal('hide');
  }

  onClickSubmit() {
    if (this.username == "admin" && this.password == "password") {
      $("#modal").modal('show');
    } else {
      this.errorMessage = true;
    }
  }

}


