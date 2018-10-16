import { Component, OnInit, ViewChild } from '@angular/core';
import { Router } from '@angular/router';
import { FormGroup, FormControl, Validators } from '@angular/forms';
import { Sup01000Service } from 'app/tmb-ecert/sup00000/sup01000/sup01000.service';
import { ModalModule } from 'app/baiwa/common/components';
import { Modal, Lov } from 'app/baiwa/common/models';
import { ModalService, AjaxService, CommonService } from 'app/baiwa/common/services';
import { Sup01000 } from 'app/tmb-ecert/sup00000/sup01000/sup01000.model';
import * as SUP01000ACTION from "app/tmb-ecert/sup00000/sup01000/sup01000.action";
import { Store } from '@ngrx/store';
import { Observable } from 'rxjs';
import { DatatableDirective, DatatableCofnig } from 'app/baiwa/common/directives/datatable/datatable.directive';
import { ModalComponent } from 'app/baiwa/common/components/modal/modal.component';
import { PAGE_AUTH, MESSAGE_STATUS } from 'app/baiwa/common/constants';
import { UserDetail } from 'app/user.model';

@Component({
  selector: 'app-sup01000',
  templateUrl: './sup01000.component.html',
  styleUrls: ['./sup01000.component.css'],
  providers: [Sup01000Service]
})
export class Sup01000Component implements OnInit {

  @ViewChild("roleDT")
  roleDT: DatatableDirective;

  @ViewChild("modalUpload")
  modalUpload: ModalComponent;


  isShowResult: Boolean = false;
  isShowImport: Boolean = false;

  objDropdownType: any;
  objRoleResult: any;
  roleResult: any;
  nodataResult: boolean;
  fileExcelUpload: any;
  responseObj: any;
  dataStateSearch: any;
  onSubmitUpload: boolean;
  dropDownStatus: any;
  storeRole: Observable<Sup01000>
  sup01000: Sup01000;
  user:UserDetail;

  roleDTConfig: DatatableCofnig;

  userRoleForm: FormGroup = new FormGroup({
    roleName: new FormControl('', Validators.required),
    status: new FormControl('90001', Validators.required)
  });

  uploadForm: FormGroup = new FormGroup({
    fileUpload: new FormControl(null, Validators.required)
  });

  uploadModal: Modal = {
    modalId: "upload",
    type: "custom"
  };


  constructor(private store: Store<AppState>, private router: Router, private service: Sup01000Service, private modal: ModalService,
    private ajax: AjaxService, private commonService: CommonService) {

    this.objDropdownType = [{
      name: "all",
      value: "101"
    }];
    this.objRoleResult = []

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
    this.onSubmitUpload = false;

    this.dropDownStatus = {
      dropdownId: "statusType",
      dropdownName: "statusType",
      type: "search",
      formGroup: this.userRoleForm,
      formControlName: "status",
      values: [],
      valueName: "code",
      labelName: "name"
    }
    this.service.getStatusType().subscribe((obj: Lov[]) => {
      this.dropDownStatus.values = obj
    });


    this.storeRole = this.store.select(state => state.sup00000.sup01000);
    this.storeRole.subscribe(data => {
      this.sup01000 = data;
    });

    this.store.select('user').subscribe(user => {
      this.user = user;
    });
  }

  ngOnInit() {
    this.roleDTConfig = {
      url: "/api/setup/sup01000/getRole",
      serverSide: false,
      useBlockUi: true
    };


  }

  ngAfterViewInit(): void {
    if (this.sup01000.isSearch) {
      this.isShowResult = true;
      this.userRoleForm.setValue({ roleName: this.sup01000.searchRoleName, status: this.sup01000.searchRoleStatus });
      this.clickSerch();
    }

  }

  clickSerch() {

    this.roleDT.searchParams(this.userRoleForm.value);
    this.roleDT.search();
    this.dataStateSearch.roleName = this.userRoleForm.value.roleName;
    this.dataStateSearch.status = this.userRoleForm.value.status;
    this.store.dispatch(new SUP01000ACTION.ClearRole());

    // console.log("from "+ this.userRoleForm.value.roleName +" status"+this.userRoleForm.value.status)
    //   this.commonService.blockui;
    //   this.service.callSearchAPI(this.userRoleForm).subscribe(src => {
    //     this.dataStateSearch.roleName = this.userRoleForm.value.roleName;
    //     this.dataStateSearch.status = this.userRoleForm.value.status;
    //     this.objRoleResult = src
    //     // console.log("get role ", this.roleResult );
    //     if (this.objRoleResult.length > 0) {
    //       this.nodataResult = false;
    //     } else {
    //       this.nodataResult = true;
    //     }
    //   }, error => {
    //     console.log("call get error");
    //   },() =>{
    //     this.commonService.unblockui;
    //     this.store.dispatch(new SUP01000ACTION.ClearRole());
    //   });
    //   this.isShowResult = true;

    }


    clickClear() {
      this.userRoleForm.reset({ roleName: "", status: "90001" });
      this.store.dispatch(new SUP01000ACTION.ClearRole());
    }

    clickImportRole() {

    }

    clickAddRole() {
      const roleState: Sup01000 = {
        roldId: null,
        status: null,
        roleName: '',
        rolePermissionId: '',
        functionCode: '',

        searchRoleName: this.dataStateSearch.roleName,
        searchRoleStatus:  this.dataStateSearch.status,
        isSearch: true
      }

      this.store.dispatch(new SUP01000ACTION.UpdateRole(roleState));

      this.router.navigate(["/sup/sup01100"], {});

    }
    clickEditRole(roleObj) {
      this.roleResult = roleObj;
      const roleState: Sup01000 = {
        roldId: this.roleResult.roldId,
        status: this.roleResult.status,
        roleName: this.roleResult.roleName,
        rolePermissionId: '',
        functionCode: '',

        searchRoleName: this.userRoleForm.value.roleName,
        searchRoleStatus: this.userRoleForm.value.status,
        isSearch: true
      }

      this.store.dispatch(new SUP01000ACTION.UpdateRole(roleState));
      // console.log("role id " + this.roleResult.roldId);
      this.router.navigate(["/sup/sup01100"]);

    }
    clickExportRole() {

      this.service.callExportAPI(this.dataStateSearch.roleName, this.dataStateSearch.status);

    }
    clickExportTemplate() {
      this.service.callExportTemplateAPI();
    }
    uploadRole() {
      this.onSubmitUpload = true;
      console.log(this.uploadForm.valid);
      if (this.uploadForm.valid) {
        this.service.callUploadAPI(this.fileExcelUpload).subscribe(src => {
          this.responseObj = src;
          if (this.responseObj.message == MESSAGE_STATUS.SUCCEED) {
            this.modal.alert({ msg: "ทำรายการสำเร็จ" });
          } else {
            this.modal.alert({ msg: "ทำรายการล้มเหลว" });
          }
        }, error => {
          this.modal.alert({ msg: "ทำรายการล้มเหลว" });
          // console.log("call get error");
        });
      }
    }
    changeUpload(data: any) {
      this.fileExcelUpload = data.target.files[0];
    }

    showModalUpload() {
      this.onSubmitUpload = false;
      this.uploadForm.reset({ fileUpload: null });
      this.modalUpload.showModal()
    }

    closeModal() {
      this.onSubmitUpload = false;
      this.uploadForm.reset({ fileUpload: null });
      this.modalUpload.closeModal()
    }

    get fileUpload() {
      return this.uploadForm.get("fileUpload");
    }

    get isCanAdd() {
      return this.commonService.ishasAuth(this.user, PAGE_AUTH.P0001301)
    }

    get isCanEdit() {
      return this.commonService.ishasAuth(this.user, PAGE_AUTH.P0001302)
    }

    get isCanImport() {
      return this.commonService.ishasAuth(this.user, PAGE_AUTH.P0001303)
    }

    get isCanExport() {
      return this.commonService.ishasAuth(this.user, PAGE_AUTH.P0001304)
    }


  }
class AppState {
  sup00000: {
    "sup01000": Sup01000
  }
}

