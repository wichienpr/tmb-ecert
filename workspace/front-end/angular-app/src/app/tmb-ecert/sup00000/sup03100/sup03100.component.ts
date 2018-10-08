import { Component, OnInit } from '@angular/core';
import { Router, ActivatedRoute } from '@angular/router';
import { Sup03100Service } from 'app/tmb-ecert/sup00000/sup03100/sup03100.service';
import { FormGroup, FormControl, Validators } from '@angular/forms';

@Component({
  selector: 'app-sup03100',
  templateUrl: './sup03100.component.html',
  styleUrls: ['./sup03100.component.css']
})
export class Sup03100Component implements OnInit {
  emailId: any;
  responseObj: any;
  emailForm:FormGroup;

  constructor(private service: Sup03100Service, private router: Router, private route: ActivatedRoute, ) {

    this.emailId = this.route.snapshot.queryParams["emailId"];
    this.responseObj = {
      emailDetail_id: "",
      emailConfig_id: "",
      subject: "",
      body: "",
      from: "",
      to: "",
      attachFile_flag: ""
    }

    this.emailForm = new FormGroup({
      subject: new FormControl(null, Validators.required),
      body: new FormControl(null, Validators.required),
      from: new FormControl(null, Validators.required),
      to: new FormControl(null, Validators.required),
    });
  }

  ngOnInit() {
    this.service.callSearchEmailDetailAPI(this.emailId).subscribe(res => {
      this.responseObj = res[0];
      this.emailForm.setValue({
        subject:this.responseObj.subject,
        body:this.responseObj.body,
        from:this.responseObj.from,
        to:this.responseObj.to
      })
      // console.log("response "+res);
    }, error => {

    });

  }

  clickCancel() {
    this.router.navigate(["/sup/sup03000"], {});
  }
  get subject() {
    return this.emailForm.get("subject");
  }
  get body() {
    return this.emailForm.get("body");
  }
  get form() {
    return this.emailForm.get("form");
  }
  get to() {
    return this.emailForm.get("to");
  }

}
