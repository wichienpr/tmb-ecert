import { Component, OnInit } from '@angular/core';
import { Router, ActivatedRoute } from "@angular/router";
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

  constructor(private router: Router) { }

  ngOnInit() { }

  openModal(id) {
    $('#' + id).modal('show');
  }

  closeModal(id) {
    $('#' + id).modal('hide');
    this.router.navigate(["/home"]);
  }

  onSubmit($event) {
    if (this.username == "admin" && this.password == "password") {
      $("#modal").modal('show');
    } else {
      let promise = new Promise((resolve, reject) => {
        $(".message").show();
        setTimeout(() => {
          resolve(true); // wait for click
        });
      }).then(resolve => {
        if (resolve) {
          $('.message .close').on('click', function () {
            $(".message").hide();
          });
        }
      });
    }
  }

}


