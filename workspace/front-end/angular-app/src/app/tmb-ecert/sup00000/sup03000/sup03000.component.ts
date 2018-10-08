import { Component, OnInit } from '@angular/core';
import { Sup03000Service } from 'app/tmb-ecert/sup00000/sup03000/sup03000.service';
import { Lov } from 'app/baiwa/common/models';
import { FormGroup, FormControl, Validators } from '@angular/forms';
import { Router } from '@angular/router';
declare var $: any;
@Component({
  selector: 'app-sup03000',
  templateUrl: './sup03000.component.html',
  styleUrls: ['./sup03000.component.css']
})
export class Sup03000Component implements OnInit {
  isShowResult: Boolean = false;
  dropDownStatus: any;
  form: FormGroup;
  responseObj: any;
  nodataResult: boolean;

  constructor(private service: Sup03000Service,private router: Router,) {

    this.form = new FormGroup({
      emailName: new FormControl(null, Validators.required),
      status: new FormControl('90001', Validators.required)
    });


    this.dropDownStatus = {
      dropdownId: "statusType",
      dropdownName: "statusType",
      type: "search",
      formGroup: this.form,
      formControlName: "status",
      values: [],
      valueName: "code",
      labelName: "name"
    }
    this.service.getStatusType().subscribe((obj: Lov[]) => {
      this.dropDownStatus.values = obj
      console.log("drop down ", obj);
    });

    this.responseObj = [];
    this.nodataResult = false;

  }

  ngOnInit() {

  }

  get status() {
    return this.form.get("status");
  }
  get emailName() {
    return this.form.get("emailName");
  }

  clickSearch() {
    console.log("form data ", this.form.value.emailName, " - ", this.form.value.status)
    this.service.callSearchAPI(this.form).subscribe(res => {
      this.responseObj = res;
      console.log("res =>", this.responseObj)
      if (this.responseObj.length > 0) {
        this.nodataResult = false;
      } else {
        this.nodataResult = true;
      }
    }, error => {

    });
    this.isShowResult = true;
  }

  clickClear() {
    this.form.reset({ emailName: '', status: '' });
    // $('#status').val('90001');
    // this.form.setValue({status:''});
    console.log()

  }

  clickEditEmailTemplate(id){
    this.router.navigate(["/sup/sup03100"], {
      queryParams: {
        emailId: id
      }
    });
  }

}
