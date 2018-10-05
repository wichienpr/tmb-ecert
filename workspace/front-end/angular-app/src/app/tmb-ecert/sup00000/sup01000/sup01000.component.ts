import { Component, OnInit } from '@angular/core';
import { Router } from '@angular/router';
import { FormGroup, FormControl, Validators } from '@angular/forms';
import { Sup01000Service } from 'app/tmb-ecert/sup00000/sup01000/sup01000.service';

@Component({
  selector: 'app-sup01000',
  templateUrl: './sup01000.component.html',
  styleUrls: ['./sup01000.component.css'],
  providers: [Sup01000Service]
})
export class Sup01000Component implements OnInit {
  isShowResult: Boolean = false;
  isShowImport: Boolean = false;

  objDropdownType: any;
  objRoleResult: any;
  roleResult: any;
  nodataResult:boolean;


  userRoleForm: FormGroup = new FormGroup({
    roleName: new FormControl('', Validators.required),
    status: new FormControl('2', Validators.required)
  });


  constructor(private router: Router, private service: Sup01000Service) {

    this.objDropdownType = [{
      name: "all",
      value: "101"
    }];
    this.objRoleResult = [{
      roldId: "",
      roleName: "",
      status: "",
      rolePermissionId: "",
      functionCode: ""
    }]
    this.roleResult = {
      roldId: "",
      roleName: "",
      status: "",
      rolePermissionId: "",
      functionCode: ""
    }
  this.nodataResult = false;

  }

  ngOnInit() {
  }
  reqTypeChange(e) {
    console.log("requestTypeCode : ", e);
  }

  clickSerch() {
    // console.log("from "+ this.userRoleForm.value.roleName +" status"+this.userRoleForm.value.status)
    this.service.callSaveAPI(this.userRoleForm).subscribe(src => {
      this.objRoleResult = src
      // console.log("get role ", this.roleResult );
      if (this.objRoleResult.length > 0 ){
        this.nodataResult = false;
      }else{
        this.nodataResult = true;
      }
    }, error => {
      console.log("call get error");
    });
    this.isShowResult = true;
  }

  clickClear() {
    this.userRoleForm.reset({ roleName: "", status: "2" })
    // console.log("clear btn");
  }

  clickImportRole() {

  }

  clickAddRole() {
    this.router.navigate(["/sup/sup01010"], {});

  }
  clickEditRole(roleObj) {
    this.roleResult = roleObj;
    console.log("role id " + this.roleResult.roldId);
    this.router.navigate(["/sup/sup01010"], {
      queryParams: {
        roleId: this.roleResult.roldId
        , roleName: this.roleResult.roleName
        , roleStatus: this.roleResult.status
      }
    });

  }
  clickExportRole(){
    this.service.callExportAPI(this.userRoleForm).subscribe(src => {
      console.log("export success",src );

    }, error => {
      console.log("export error");
    });
  }

}
