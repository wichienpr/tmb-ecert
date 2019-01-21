import { Component, OnInit, AfterViewInit } from '@angular/core';
import { Sup02000Service } from 'app/tmb-ecert/sup00000/sup02000/sup02000.service';
import { FormControl, FormGroup, FormBuilder, FormArray, Validators } from '@angular/forms';
import { Sup02000 } from 'app/tmb-ecert/sup00000/sup02000/sup02000.model';
import { ModalService, CommonService, DropdownService } from 'app/baiwa/common/services';
import { Modal, Lov, Dropdown, DropdownMode } from 'app/baiwa/common/models';
import { UserDetail } from 'app/user.model';
import { Store } from '@ngrx/store';
import { PAGE_AUTH, MESSAGE_STATUS } from 'app/baiwa/common/constants';
import { DashboardService } from 'app/baiwa/home/dashboard.service';
import { updateLocale } from 'moment';
import { every } from 'rxjs/operators';

@Component({
  selector: 'app-sup02000',
  templateUrl: './sup02000.component.html',
  styleUrls: ['./sup02000.component.css']
})
export class Sup02000Component implements OnInit, AfterViewInit {

  paramResult: any;
  paramsGroup: Lov[];
  paramsDropdown: Dropdown;
  parameterForm = this.fb.group({
    formPropArray: this.fb.array([]),
    paramsCode: "ALL"
  });
  paramsGroupSelected: string = "ALL";
  responseObj: any;
  user: UserDetail;
  remark_ecm:boolean = false;

  constructor(private store: Store<AppState>, private service: Sup02000Service, private fb: FormBuilder,
    private modal: ModalService, private commonService: CommonService,
    private dashboardService: DashboardService, private dropdown: DropdownService) {

    this.paramResult = [{
      parameterconfigId: "",
      propertyName: "",
      propertyValue: ""
    }];

    this.responseObj = {
      data: "",
      message: ""
    }

    this.store.select('user').subscribe(user => {
      this.user = user;
    });

    this.paramsDropdown = {
      dropdownId: "paramsCode",
      dropdownName: "paramsCode",
      type: DropdownMode.SELECTION,
      formGroup: this.parameterForm,
      formControlName: "paramsCode",
      values: [],
      valueName: "code",
      labelName: "name",
      placehold: "กรุณาเลือก"
    };

    this.dropdown.getParameterGroup().subscribe(response => {
      this.paramsGroup = response as Lov[];
      const data: Lov = {
        accountNo: null, accountType: null,
        glType: null, sequence: null,
        status: null, tranCode: null,
        type: null, typeDesc: null,
        code: "ALL", name: "ทั้งหมด"
      }
      this.paramsGroup.unshift(data);
      this.paramsDropdown.values = this.paramsGroup;
    });

  }

  ngOnInit() { }

  ngAfterViewInit() {
    this.callSearchAPI();
  }

  callSearchAPI() {
    this.formPropArray.reset();
    this.service.callGetParamAPI().subscribe(src => {
      this.paramResult = src;
      let i = 0;
      this.paramResult.forEach(item => {

        let name = item.propertyName
        let value = item.propertyValue
        let id = item.parameterconfigId
        let group = item.propertyGroup

        const newFormGroup: FormGroup = this.fb.group(
          {
            parameterconfigId: new FormControl(id),
            propertyGroup: new FormControl(group),
            propertyName: new FormControl(name),
            propertyValue: new FormControl(value)
          }
        );
        this.formPropArray.push(newFormGroup);
        i++;
      });
      console.log("search parameter " , this.formPropArray)
    })
  }

  paramsGroupChange(event) {
    console.log("selection is ", event);
    if (event == "12006"){
      this.remark_ecm = true;
    }else{
      this.remark_ecm = false;
    }
    this.paramsGroupSelected = event;
  }

  paramsSelected(group) {
    return group && (this.paramsGroupSelected === group || this.paramsGroupSelected === 'ALL');
  }

  clickSave() {
    let listParameter = [];

    let listFormProp: any = this.parameterForm.get('formPropArray');
    listFormProp.controls.forEach(element => {
      if (!(element.pristine)) {
        let obj = {
          parameterconfigId: element.controls.parameterconfigId.value,
          propertyName: element.controls.propertyName.value,
          propertyValue: element.controls.propertyValue.value
        }
        listParameter.push(obj)
      }
    });

    const modalConf: Modal = {
      msg: `<label>ท่านต้องการบันทึกข้อมูลหรือไม่</label>`,
      title: "ยืนยันการบันทึก",
      color: "notification"
    }
    this.modal.confirm((e) => {
      this.commonService.isLoading();
      if (e) {
        this.service.callSaveParameterAPI(listParameter).subscribe(res => {
          this.responseObj = res;
          if (this.responseObj.message == MESSAGE_STATUS.FAILED) {
            this.modal.alert({ msg: "ทำรายการไม่สำเร็จ กรุณาดำเนินการอีกครั้งหรือติดต่อผู้ดูแลระบบ โทร. 02-299-2765" });
            this.commonService.isLoaded();
          } else {
            setTimeout(() => {
              this.dashboardService.reloadCache().subscribe(() => {
                this.callSearchAPI();
                this.commonService.isLoaded();
                this.modal.alert({ msg: "ทำรายการสำเร็จ", success: true });
              });
            }, 1000);
          }
        }, err => {
          this.modal.alert({ msg: "ทำรายการไม่สำเร็จ กรุณาดำเนินการอีกครั้งหรือติดต่อผู้ดูแลระบบ โทร. 02-299-2765" });
          this.commonService.isLoaded();
        });
      } else {
        this.commonService.isLoaded();
      }
    }, modalConf);

  }

  get formPropArray(): FormArray {
    return this.parameterForm.get('formPropArray') as FormArray;
  }

  get listformPropArray(): FormArray {
    return this.parameterForm.get('formPropArray.controls') as FormArray;
  }

  get isCanSave() {
    return this.commonService.ishasAuth(this.user, PAGE_AUTH.P0001401)
  }

}

interface AppState {
  user: UserDetail
}