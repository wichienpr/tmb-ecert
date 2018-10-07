import { Component, OnInit } from '@angular/core';
import { Router } from '@angular/router';
import { FormGroup, FormControl, Validators } from '@angular/forms';
import { Sup01000Service } from 'app/tmb-ecert/sup00000/sup01000/sup01000.service';
import { ModalModule } from 'app/baiwa/common/components';
import { Modal } from 'app/baiwa/common/models';
import { ModalService, AjaxService } from 'app/baiwa/common/services';

declare var $: any;

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
  nodataResult: boolean;
  fileExcelUpload: any;
  responseObj: any;
  dataStateSearch:any;


  userRoleForm: FormGroup = new FormGroup({
    roleName: new FormControl('', Validators.required),
    status: new FormControl('2', Validators.required)
  });

  uploadForm: FormGroup = new FormGroup({
    fileUpload: new FormControl(null, Validators.required)
  });

  uploadModal: Modal = {
    modalId: "upload",
    type: "custom"
  };


  constructor(private router: Router, private service: Sup01000Service, private modal: ModalService,private ajax: AjaxService) {

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

    this.responseObj = {
      data: "",
      message: ""
    }
    this.dataStateSearch = {
      roleName: "",
      status: "",
    }


  }

  ngOnInit() {
  }
  reqTypeChange(e) {
    console.log("requestTypeCode : ", e);
  }

  clickSerch() {
    // console.log("from "+ this.userRoleForm.value.roleName +" status"+this.userRoleForm.value.status)
    this.service.callSearchAPI(this.userRoleForm).subscribe(src => {
      this.dataStateSearch.roleName = this.userRoleForm.value.roleName;
      this.dataStateSearch.status = this.userRoleForm.value.status;
      this.objRoleResult = src
      // console.log("get role ", this.roleResult );
      if (this.objRoleResult.length > 0) {
        this.nodataResult = false;
      } else {
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
    this.router.navigate(["/sup/sup01100"], {});

  }
  clickEditRole(roleObj) {
    this.roleResult = roleObj;
    console.log("role id " + this.roleResult.roldId);
    this.router.navigate(["/sup/sup01100"], {
      queryParams: {
        roleId: this.roleResult.roldId
        , roleName: this.roleResult.roleName
        , roleStatus: this.roleResult.status
      }
    });

  }
  clickExportRole() {
    // this.service.callExportAPI(this.objRoleResult).subscribe(src => {
    //   console.log("export success", src);

    // }, error => {
    //   console.log("export error");
    // });
    this.service.callExportAPI(this.dataStateSearch.roleName,this.dataStateSearch.status);

  }
  clickExportTemplate() {
    this.service.callExportTemplateAPI();
  }
  uploadRole() {

    this.service.callUploadAPI(this.fileExcelUpload).subscribe(src => {
      this.responseObj = src;
      if (this.responseObj.message == null) {
        this.modal.alert({ msg: "ทำรายการล้มเหลว" })
      } else {
        this.modal.alert({ msg: this.responseObj.message });
      }
    });

  }
  changeUpload(data: any) {
    this.fileExcelUpload = data.target.files[0];
    // console.log("file upload ", data.target.files[0]);
  }

  showModalUpload() {
    console.log("show modal")
    $('#upload').modal('show');
  }

  closeModal() {
    console.log("hide modal")
    $('#upload').modal('hide');
  }

  get fileUpload() {
    return this.uploadForm.get("fileUpload");
  }

  // exportToExcel() {
  //   // this.loadingOverlayFlag = true;
  //   this.service.DownloadData(this.objRoleResult).subscribe(result=>{
  //     // console.log(result);
  //     this.downloadFile(result,'application/vnd.openxmlformats-officedocument.spreadsheetml.sheet', 'export.xlsx');
  //   })
  // }
  // downloadFile(blob: any, type: string, filename: string) {

  //   var binaryData = [];
  //   binaryData.push(blob);
  
  //   const url = window.URL.createObjectURL(new Blob(binaryData, { type: type })); // <-- work with blob directly
  
  //    // create hidden dom element (so it works in all browsers)
  //    const a = document.createElement('a');
  //    a.setAttribute('style', 'display:none;');
  //    document.body.appendChild(a);
  
  //    // create file, attach to hidden element and open hidden element
  //    a.href = url;
  //    a.download = filename;
  //    a.click();
  // }
  


}
