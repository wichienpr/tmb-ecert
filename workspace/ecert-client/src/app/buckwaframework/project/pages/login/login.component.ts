import { Component, OnInit } from '@angular/core';
declare var $: any;
@Component({
  selector: 'app-login',
  templateUrl: './login.component.html',
  styleUrls: ['./login.component.css']
})
export class LoginComponent implements OnInit {
  username: string  = "";
  password: string  = "";
  modal: string[] = ["modal"];

  constructor() { }

  ngOnInit() {  

  }

  openModal(id) {
    $('#' + id).modal('show');
  }

  closeModal(id) {
    $('#' + id).modal('hide');
  }

  onClickSubmit(){
    if(this.username =="admin" && this.password == "password"){      
   $("#modal").modal('show');
    }else{
      window.alert("รหัสไม่ถูกต้อง");
    }
  }

}
